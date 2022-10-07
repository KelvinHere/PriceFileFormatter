package priceFileFormatter;

import static org.junit.jupiter.api.Assertions.*;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;

class ModelOutputTest {
	
	private static final String SAMPLE_ITEMS_CSV = "data/sample-products.csv";
	private static final String SAMPLE_SUPPLIERS_CSV = "data/sample-suppliers.csv";
	private static final String SAMPLE_OUTPUT_CSV = "data/sample-output.csv";
	private static final String KNOWN_SKU = "AYO8RP45SC";
	Connection conn;

	@AfterEach
	void tearDown() throws SQLException {
		conn.close();
	}
	
	@Test
	void outputTableIsPopulatedWithFormattedImportedItems() throws SQLException {
		conn = Database.connect(); 
		ModelImport modelImport = new ModelImport(conn, SAMPLE_ITEMS_CSV);
		ModelSupplier modelSupplier = new ModelSupplier(conn, SAMPLE_SUPPLIERS_CSV);
		ModelOutput modelOutput = new ModelOutput(conn, SAMPLE_OUTPUT_CSV);
		
		
		// Get item from output
		String sql = String.format("SELECT * from %s WHERE their_sku = '%s';", Tables.OUTPUT, KNOWN_SKU);
		ResultSet rs = SqlHelper.query(conn, sql);

		String resultSku = ""; 
		if (rs.next()) {
			resultSku = rs.getString("their_sku");
		}
		assertEquals(KNOWN_SKU, resultSku);
	}

}
