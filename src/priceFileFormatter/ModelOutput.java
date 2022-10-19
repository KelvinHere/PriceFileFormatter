package priceFileFormatter;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.HashMap;

public class ModelOutput {
	private String csvOutputFileLocation;
	private Enum<?> table = Tables.OUTPUT;
	private String foreignKey = "supplier_code";
	private String supplierTableSupplierCodeField = "supplier_code";
	private String[] neededFields = new String[] {"their_sku", "their_description", "net_cost"};
	private String selectedSupplier = "FL01";
	private HashMap<String, String> supplierData = new HashMap<>();
	
	private String model = String.format("CREATE TABLE IF NOT EXISTS output("
			+ "abbrev_description VARCHAR(50), "
			+ "their_sku VARCHAR(50), "
			+ "our_sku VARCHAR(50), "
			+ "description VARCHAR(255), "
			+ "extra_description VARCHAR(255), "
			+ "Net_Cost DECIMAL(15,2), "
			+ "price_1 DECIMAL(15,2), "
			+ "price_2 DECIMAL(15,2), "
			+ "group_1 VARCHAR(4), "
			+ "group_2 VARCHAR(4), "
			+ "supplier_code VARCHAR(4), "
			+ "vat_switch VARCHAR(1), "
			+ "their_description VARCHAR(255))");

	
	public ModelOutput(Connection conn, String csvOutputFile, String supplier) {
		this.selectedSupplier = supplier;
		this.csvOutputFileLocation = csvOutputFile;
		// Create importTable
		SqlHelper.execute(conn, model);
		
		// Set foreign key
		String sql = String.format("ALTER TABLE %s ADD FOREIGN KEY (%s) REFERENCES %s(%s);", table, foreignKey, Tables.SUPPLIER, supplierTableSupplierCodeField);
		SqlHelper.execute(conn, sql);
		
		populateModel(conn);
	}
	
	
	private void populateModel(Connection conn) {
		// Set up supplierData data 
		String sql = String.format("SELECT * FROM %s WHERE supplier_name = '%s'", Tables.SUPPLIER, selectedSupplier);
		ResultSet rs = SqlHelper.query(conn, sql);
		supplierData = supplierRsToHashMap(rs);
		
		// Fetch data from new imports
		sql = String.format("INSERT INTO %s(their_sku, their_description, net_cost) "
								+ "SELECT PRODUCT_CODE, PRODUCT_DESCRIPTION, NET_COST "
								+ "from %s", Tables.OUTPUT, Tables.IMPORT);
		SqlHelper.execute(conn, sql);

		// Add prefix to our_sku
		sql = String.format("UPDATE output SET our_sku = '%s ' + their_sku", supplierData.get("sku_prefix"));
		SqlHelper.execute(conn, sql);
		
		// Populate our description (Up to 58 characters and prefixed with bullet point + space)
		sql = "UPDATE output SET description = '\u2022 ' + LEFT(their_description, 58)";
		SqlHelper.execute(conn, sql);
		
		// Populate extra description (Anything over 59 characters)
		sql = "UPDATE output SET extra_description = SUBSTRING(their_description, 59, 60)";
		SqlHelper.execute(conn, sql);
		
		// Populate abbreviated description (First 2 words)
		sql = "UPDATE output SET abbrev_description = REGEXP_SUBSTR(their_description, '([^\\s]+\\s+[^\\s]+)')";
		SqlHelper.execute(conn, sql);
		
		// Populate Supplier fields
		sql = String.format("UPDATE output SET supplier_code = '%s', "
							+ "group_1 = '%s', "
							+ "group_2 = '%s' ,"
							+ "vat_switch = '%s';", 
							supplierData.get("supplier_code"), supplierData.get("group_code_1"), 
							supplierData.get("group_code_2"), supplierData.get("vat_switch"));
		SqlHelper.execute(conn, sql);
		
		// Price 1 = Net Cost * Mark-up 1 * Vat
		sql = String.format("UPDATE output SET price_1 = output.net_cost * %s * %s", supplierData.get("markup_1"), supplierData.get("vat"));
		SqlHelper.execute(conn, sql);
		
		// Price 2 = Net Cost * Mark-up 2 * Vat
		sql = String.format("UPDATE output SET price_2 = output.net_cost * %s * %s", supplierData.get("markup_2"), supplierData.get("vat"));
		SqlHelper.execute(conn, sql);
	}
	
	
	private HashMap<String, String> supplierRsToHashMap(ResultSet rs) {
		HashMap<String, String> result = new HashMap<>();
		try {
			rs.next();
			result.put("supplier_name", rs.getString("supplier_name"));
			result.put("sku_prefix", rs.getString("sku_prefix"));
			result.put("supplier_code", rs.getString("supplier_code"));
			result.put("markup_1", rs.getString("markup_1"));
			result.put("markup_2", rs.getString("markup_2"));
			result.put("group_code_1", rs.getString("group_code_1"));
			result.put("group_code_2", rs.getString("group_code_2"));
			result.put("vat_switch", rs.getString("vat_switch"));
			result.put("vat", rs.getString("vat"));
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return result;
	}
	
	
	public HashMap<String, String> getSupplierHashMap() {
		return supplierData;
	}
}
