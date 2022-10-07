package priceFileFormatter;

import static org.junit.jupiter.api.Assertions.*;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;

class CSVReaderTest {

	private static final String SAMPLE_ITEMS_CSV = "data/sample-products.csv";
	private static final String KNOWN_SKU = "AYO8RP45SC";

	
	@Test
	void testDataInserted() throws SQLException {
		Connection conn = Database.connect();
		CSVReader csvReader = new CSVReader();
		csvReader.insertCsvIntoDatabase(conn, SAMPLE_ITEMS_CSV, Tables.IMPORT);
		String sql = String.format("SELECT * from %s WHERE PRODUCT_CODE = '%s';", Tables.IMPORT, KNOWN_SKU);
		String resultSku = ""; 
		ResultSet rs = ExecuteSql.query(conn, sql);

		if (rs.next()) {
			resultSku = rs.getString(1);
		}
		assertEquals(KNOWN_SKU, resultSku);
	}
	
	

}
