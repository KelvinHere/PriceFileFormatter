package priceFileFormatter;

import static org.junit.jupiter.api.Assertions.*;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;

import org.junit.jupiter.api.Test;

class ModelOutputTest {
	
	private static final String SAMPLE_ITEMS_CSV = "data/sample-products.csv";
	private static final String KNOWN_SKU = "AYO8RP45SC";

	@Test
	void outputIsPopulatedWithImportedItems() throws SQLException {
		Connection conn = Database.connect();
		CSVReader csvReader = new CSVReader();
		// Create imports
		csvReader.insertCsvIntoDatabase(conn, SAMPLE_ITEMS_CSV, Tables.IMPORT);
		
		// Create output
		ModelOutput modelOutput = new ModelOutput();
		modelOutput.createTable(conn);
		modelOutput.populateModel(conn);
		
		// Get item from output
		String sql = String.format("SELECT * from %s WHERE their_sku = '%s';", Tables.OUTPUT, KNOWN_SKU);
		String resultSku = ""; 
		ResultSet rs = ExecuteSql.query(conn, sql);

		if (rs.next()) {
			resultSku = rs.getString("their_sku");
		}
		assertEquals(KNOWN_SKU, resultSku);
	}

}
