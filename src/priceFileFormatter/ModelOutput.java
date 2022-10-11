package priceFileFormatter;

import java.sql.Connection;

public class ModelOutput {
	private String csvOutputFileLocation;
	private Enum<?> table = Tables.OUTPUT;
	private String foreignKey = "supplier_code";
	private String supplierTableSupplierCodeField = "supplier_code";
	private String[] neededFields = new String[] {"their_sku", "their_description", "net_cost"};
	
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

	
	public ModelOutput(Connection conn, String csvOutputFile) {
		this.csvOutputFileLocation = csvOutputFile;
		// Create importTable
		SqlHelper.execute(conn, model);
		
		// Set foreign key
		// Set primary key
		String sql = String.format("ALTER TABLE %s ADD FOREIGN KEY (%s) REFERENCES %s(%s);", table, foreignKey, Tables.SUPPLIER, supplierTableSupplierCodeField);
		SqlHelper.execute(conn, sql);
		
		populateModel(conn);
	}

	
	private void populateModel(Connection conn) {
		// Fetch data from new imports
		String sql = String.format("INSERT INTO output(their_sku, their_description) "
								+ "SELECT PRODUCT_CODE, PRODUCT_DESCRIPTION "
								+ "from %s", Tables.IMPORT.toString());
		SqlHelper.execute(conn, sql);

		// Add prefix to our stock coded
		sql = "UPDATE output SET our_sku = 'FLA ' + their_sku";
		SqlHelper.execute(conn, sql);
		
		// Populate our description
		sql = "UPDATE output SET description = '\u2022 ' + LEFT(their_description, 58)";
		SqlHelper.execute(conn, sql);
		
		// Populate extra description
		sql = "UPDATE output SET extra_description = SUBSTRING(their_description, 59, 60)";
		SqlHelper.execute(conn, sql);
		
		// Populate abbreviated description
		sql = "UPDATE output SET abbrev_description = REGEXP_SUBSTR(their_description, '([^\\s]+\\s+[^\\s]+)')";
		SqlHelper.execute(conn, sql);
		
		// Populate Supplier code
		sql = "UPDATE output SET supplier_code = 'FL01'";
		SqlHelper.execute(conn, sql);
	}
}
