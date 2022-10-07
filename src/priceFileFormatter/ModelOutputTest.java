package priceFileFormatter;

import static org.junit.jupiter.api.Assertions.*;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;

class ModelOutputTest {
	
	private static final String SAMPLE_ITEMS_CSV = "data/sample-products.csv";
	private static final String SAMPLE_SUPPLIERS_CSV = "data/sample-suppliers.csv";
	private static final String SAMPLE_OUTPUT_CSV = "data/sample-output.csv";
	private static final String KNOWN_SKU = "AYO8RP45SC";
	
	private static final String FOREIGN_SUPPLIER_NAME = "Flair";
	private static final String FOREIGN_SUPPLIER_CODE = "FL01";
	private static final String FOREIGN_SUPPLIER_SKU_PREFIX = "FLA";
	
	Connection conn;
	
	
	@AfterEach
	void after() throws SQLException {
		// Drop database
		String sql = "DROP SCHEMA PUBLIC CASCADE";
		SqlHelper.execute(conn, sql);
	}
	
	
	@Test
	void outputTableIsPopulatedWithFormattedImportedItems() throws SQLException {
		Connection conn = setup();
		
		// Get item from output table
		String sql = String.format("SELECT * from %s WHERE their_sku = '%s';", Tables.OUTPUT, KNOWN_SKU);
		ResultSet rs = SqlHelper.query(conn, sql);

		String result = null;
		if (rs.next()) {
			result = rs.getString("their_sku");
		}
		
		assertEquals(KNOWN_SKU, result);
	}
	
	
	@Test
	void canPullForeignKeyDataFromOutputItem() throws SQLException {
		Connection conn = setup();
		
		// Get item from output table
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

	
	public Connection setup() {
		conn = Database.connect(); 
		ModelImport modelImport = new ModelImport(conn, SAMPLE_ITEMS_CSV);
		ModelSupplier modelSupplier = new ModelSupplier(conn, SAMPLE_SUPPLIERS_CSV);
		ModelOutput modelOutput = new ModelOutput(conn, SAMPLE_OUTPUT_CSV);
		return conn;
	}
}
