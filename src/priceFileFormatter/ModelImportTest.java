package priceFileFormatter;

import static org.junit.jupiter.api.Assertions.*;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;

class ModelImportTest {

	private static final String SAMPLE_ITEMS_CSV = "data/sample-products.csv";
	private static final String FIRST_IMPORTED_SKU = "AYO8RP45SC";
	private static final String FIRST_IMPORTED_PRODUCT_DESCRIPTION = "AYO 8MM ROTATING            PANEL 450MM SILVER-R8MM (C)";
	private static final String FIRST_IMPORTED_NET_COST = "131.00";
	
	private static final String LAST_IMPORTED_SKU = "BP5EWH";
	private static final String LAST_IMPORTED_PRODUCT_DESCRIPTION = "BATH PANEL END WHITE & ANGLE";
	private static final String LAST_IMPORTED_NET_COST = "138.00";
	private static final int TOTAL_ITEMS_IN_FILE = 66;
	
	Connection conn;
	ModelImport modelImport;

	
	@AfterEach
	void tearDown() throws SQLException {
		String sql = "DROP SCHEMA PUBLIC CASCADE";
		SqlHelper.execute(conn, sql);
		conn.close();
		conn = null;
		modelImport = null; 
	}

	
	@Test
	void importTableIsPopulatedFirstItem() throws SQLException {
		conn = Database.connect(); 
		// import sample items to import importTable
		modelImport = new ModelImport(conn, SAMPLE_ITEMS_CSV);
		
		// Get item from output
		String sql = String.format("SELECT * from %s WHERE PRODUCT_CODE = '%s';", Tables.IMPORT, FIRST_IMPORTED_SKU);
		ResultSet rs = SqlHelper.query(conn, sql);
		HashMap<String, String> unpackedRs = resultSetToHashMap(rs);
		
		assertEquals(FIRST_IMPORTED_SKU, unpackedRs.get("sku"));
		assertEquals(FIRST_IMPORTED_PRODUCT_DESCRIPTION, unpackedRs.get("description"));
		assertEquals(FIRST_IMPORTED_NET_COST, unpackedRs.get("netCost"));
	}
	
	
	@Test
	void importTableIsPopulatedLastItem() throws SQLException {
		conn = Database.connect(); 
		// import sample items to import importTable
		modelImport = new ModelImport(conn, SAMPLE_ITEMS_CSV);
		
		// Get item from output
		String sql = String.format("SELECT * from %s WHERE PRODUCT_CODE = '%s';", Tables.IMPORT, LAST_IMPORTED_SKU);
		ResultSet rs = SqlHelper.query(conn, sql);
		HashMap<String, String> unpackedRs = resultSetToHashMap(rs);
		
		assertEquals(LAST_IMPORTED_SKU, unpackedRs.get("sku"));
		assertEquals(LAST_IMPORTED_PRODUCT_DESCRIPTION, unpackedRs.get("description"));
		assertEquals(LAST_IMPORTED_NET_COST, unpackedRs.get("netCost"));
	}
	
	
	@Test
	void correctAmountOfItemsImported() throws SQLException {
		conn = Database.connect(); 
		// import sample items to import importTable
		modelImport = new ModelImport(conn, SAMPLE_ITEMS_CSV);
		
		// Get amount of items in SUPPLIER_TABLE
		String sql = String.format("SELECT COUNT(*) AS total FROM %s", Tables.IMPORT);
		ResultSet rs = SqlHelper.query(conn, sql);
		rs.next();
		int actualItemCount = rs.getInt("total");
		
		assertEquals(TOTAL_ITEMS_IN_FILE, actualItemCount);
	}
	
	
	@Test 
	void getAllItemsInTable() throws SQLException {
		conn = Database.connect();
		modelImport = new ModelImport(conn, SAMPLE_ITEMS_CSV);
		
		ResultSet rs = modelImport.getAllItemsInTable();
		int actualItems= 0;
		while (rs.next()) actualItems++;
		assertEquals(TOTAL_ITEMS_IN_FILE, actualItems);
	}

	
	private HashMap<String, String> resultSetToHashMap(ResultSet rs) throws SQLException {
		HashMap<String, String> results = new HashMap<>();
		
		if (rs.next()) {
			results.put("sku", rs.getString("PRODUCT_CODE"));
			results.put("description", rs.getString("PRODUCT_DESCRIPTION"));
			results.put("netCost", rs.getString("NET_COST"));
		}
		return results;
	}
}
