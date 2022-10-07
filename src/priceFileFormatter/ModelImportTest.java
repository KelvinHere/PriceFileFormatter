package priceFileFormatter;

import static org.junit.jupiter.api.Assertions.*;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;

class ModelImportTest {

	private static final String SAMPLE_ITEMS_CSV = "data/sample-products.csv";
	private static final String KNOWN_SKU = "AYO8RP45SC";
	Connection conn;
	Enum<?> table = Tables.IMPORT;

	@AfterEach
	void tearDown() throws SQLException {
		conn.close();
	}
	
	@Test
	void importTableIsPopulatedWithImportedItems() throws SQLException {
		conn = Database.connect(); 
		ModelImport modelImport = new ModelImport(conn, SAMPLE_ITEMS_CSV);
		
		// Get item from output
		String sql = String.format("SELECT * from %s WHERE PRODUCT_CODE = '%s';", table, KNOWN_SKU);
		ResultSet rs = SqlHelper.query(conn, sql);

		String resultSku = ""; 
		if (rs.next()) {
			resultSku = rs.getString("PRODUCT_CODE");
		}
		assertEquals(KNOWN_SKU, resultSku);
	}

}
