package priceFileFormatter;

import static org.junit.jupiter.api.Assertions.*;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.Map;

class ModelOutputTest {
	
	private static final String SAMPLE_ITEMS_CSV = "data/sample-products.csv";
	private static final String SAMPLE_SUPPLIERS_CSV = "data/sample-suppliers.csv";
	private static final String SAMPLE_OUTPUT_CSV = "data/sample-output.csv";
	private static final String DEFAULT_SUPPLIER = "Flair";
	private static final String THEIR_KNOWN_SKU = "AYO8HRP50CBRBC";
	private static final String SKU_FOR_ITEM_WITH_EXTRA_WHITE_SPACES = "AYO8RP45SC";
	
	private static final String EXPECTED_SKU = "FLA AYO8HRP50CBRBC";
	private static final String EXPECTED_DESCRIPTION = "• AYO 8MM HINGE ROTATING DEFLECTOR PANEL 500MM BRUSHED BRASS";
	private static final String EXPECTED_EXTRA_DESCRIPTION = " COMPLETE";
	private static final String EXPECTED_ABBREVIATED_DESCRIPTION = "AYO 8MM";
	private static final String EXPECTED_VAT_SWITCH = "I";
	private static final String EXPECTED_GROUP_1 = "BA";
	private static final String EXPECTED_GROUP_2 = "FLA";
	private static final Double EXPECTED_PRICE_1 = 167.89;
	private static final Double EXPECTED_PRICE_2 = 154.98;
	private static final String EXPECTED_DESCRITPTION_WITH_EXTRA_WHITE_SPACES = "AYO 8MM ROTATING            PANEL 450MM SILVER-R8MM (C)";
	private static final String EXPECTED_DESCRITPTION_WITH_EXTRA_WHITE_SPACES_REMOVED = "AYO 8MM ROTATING PANEL 450MM SILVER-R8MM (C)";
	
	
	private static final String FOREIGN_SUPPLIER_NAME = "Flair";
	private static final String FOREIGN_SUPPLIER_CODE = "FL01";
	private static final String FOREIGN_SUPPLIER_SKU_PREFIX = "FLA";
	
	Connection conn;
	ModelOutput modelOutput;
	
	
	@AfterEach
	void after() throws SQLException {
		// Drop database
		String sql = "DROP SCHEMA PUBLIC CASCADE";
		SqlHelper.execute(conn, sql);
		modelOutput = null;
	}
	
	
	@Test
	void outputTableIsPopulatedWithFormattedImportedItems() throws SQLException {
		Connection conn = setup();
		
		// Get item from output importTable
		String sql = String.format("SELECT * from %s WHERE their_sku = '%s';", Tables.OUTPUT, THEIR_KNOWN_SKU);
		ResultSet rs = SqlHelper.query(conn, sql);

		rs.next();
		String theirSku = rs.getString("their_sku");
		String formattedSku = rs.getString("our_sku");
		String description = rs.getString("description");
		String extraDescription = rs.getString("extra_description");
		String abbreviatedDescription = rs.getString("abbrev_description");
		String vatSwitch = rs.getString("vat_switch");
		String group1 = rs.getString(OutputFields.GROUP_1.lowerCase());
		String group2 = rs.getString(OutputFields.GROUP_2.lowerCase());
		Double price1 = rs.getDouble(OutputFields.PRICE_1.lowerCase());
		Double price2 = rs.getDouble(OutputFields.PRICE_2.lowerCase());
		
		assertEquals(THEIR_KNOWN_SKU, theirSku);
		assertEquals(EXPECTED_SKU, formattedSku);
		assertEquals(EXPECTED_DESCRIPTION, description);
		assertEquals(EXPECTED_EXTRA_DESCRIPTION, extraDescription);
		assertEquals(EXPECTED_ABBREVIATED_DESCRIPTION, abbreviatedDescription);
		assertEquals(EXPECTED_VAT_SWITCH, vatSwitch);
		assertEquals(EXPECTED_GROUP_1, group1);
		assertEquals(EXPECTED_GROUP_2, group2);
		assertEquals(EXPECTED_PRICE_1, price1);
		assertEquals(EXPECTED_PRICE_2, price2);
	}
	
	
	@Test
	void outputTablePricesAreCorrect() throws SQLException {
		Connection conn = setup();
		
		// Get item from output importTable
		String sql = String.format("SELECT * from %s WHERE their_sku = '%s';", Tables.OUTPUT, THEIR_KNOWN_SKU);
		ResultSet rs = SqlHelper.query(conn, sql);

		rs.next();
		Double actualPrice1 = rs.getDouble(OutputFields.PRICE_1.lowerCase());
		Double actualPrice2 = rs.getDouble(OutputFields.PRICE_2.lowerCase());
		
		assertEquals(EXPECTED_PRICE_1, actualPrice1);
		assertEquals(EXPECTED_PRICE_2, actualPrice2);
	}
	
	
	@Test
	void canPullForeignKeyDataFromOutputItem() throws SQLException {
		Connection conn = setup();
		
		// Get item from output importTable
		String sql = String.format("SELECT * FROM OUTPUT INNER JOIN SUPPLIER ON output.supplier_code = supplier.supplier_code WHERE their_sku = 'AYO8RP45SC';");
		ResultSet rs = SqlHelper.query(conn, sql);
		
		String foreignSupplierName = null;
		String foreignSupplierCode = null;
		String foreignSupplierSkuPrefix = null;
		if (rs.next()) {
			foreignSupplierName = rs.getString("supplier_name");
			foreignSupplierCode = rs.getString("supplier_code");
			foreignSupplierSkuPrefix = rs.getString("sku_prefix");
		}
		
		assertEquals(FOREIGN_SUPPLIER_NAME, foreignSupplierName);
		assertEquals(FOREIGN_SUPPLIER_CODE, foreignSupplierCode);
		assertEquals(FOREIGN_SUPPLIER_SKU_PREFIX, foreignSupplierSkuPrefix);

	}
	
	
	@Test
	void suppliersRsToHashMapContainsCorrectData() throws SQLException{
		Connection conn = setup();
		
		HashMap<String, String> expectedData = new HashMap<>();
		expectedData.put("supplier_name", "Flair");
		expectedData.put("sku_prefix", "FLA");
		expectedData.put("supplier_code", "FL01");
		expectedData.put("markup_1", "1.3");
		expectedData.put("markup_2", "1.2");
		expectedData.put("group_code_1", "BA");
		expectedData.put("group_code_2", "FLA");
		expectedData.put("vat_switch", "I");
		expectedData.put("vat", "1.23");
		HashMap<String, String> actualData = modelOutput.getSupplierHashMap();
		
		assertTrue(actualData.equals(expectedData));
	}
	
	
	@Test
	void testOutputDescriptionRemovesExtraWhiteSpaces() throws SQLException {
		Connection conn = setup();
		
		// Get item from importTable
		String sql = String.format("SELECT * from %s WHERE PRODUCT_CODE = '%s';", Tables.IMPORT, SKU_FOR_ITEM_WITH_EXTRA_WHITE_SPACES);
		ResultSet rs = SqlHelper.query(conn, sql);
		rs.next();
		String actualImportDescription = rs.getString("PRODUCT_DESCRIPTION");
		assertEquals(actualImportDescription, EXPECTED_DESCRITPTION_WITH_EXTRA_WHITE_SPACES);
		rs = null;
		
		// Get item from outputTable
		sql = String.format("SELECT * from %s WHERE their_sku = '%s';", Tables.OUTPUT, SKU_FOR_ITEM_WITH_EXTRA_WHITE_SPACES);
		rs = SqlHelper.query(conn, sql);
		rs.next();
		String actualOutputDescription = rs.getString(OutputFields.THEIR_DESCRIPTION.lowerCase());
		assertEquals(actualOutputDescription, EXPECTED_DESCRITPTION_WITH_EXTRA_WHITE_SPACES_REMOVED);
		
		
	}

	
	public Connection setup() {
		conn = Database.connect(); 
		ModelSupplier.importSuppliers(conn, SAMPLE_SUPPLIERS_CSV);
		ModelImport modelImport = new ModelImport(conn, SAMPLE_ITEMS_CSV);
		modelOutput = new ModelOutput(conn, SAMPLE_OUTPUT_CSV, DEFAULT_SUPPLIER);
		return conn;
	}
}
