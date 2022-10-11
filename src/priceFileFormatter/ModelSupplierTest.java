package priceFileFormatter;

import static org.junit.jupiter.api.Assertions.*;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;

class ModelSupplierTest {

	private static final String SAMPLE_SUPPLIERS_CSV = "data/sample-suppliers.csv";
	private static final String KNOWN_SUPPLIER_CODE = "FL01";
	Connection conn;
	Enum<?> table = Tables.SUPPLIER;

	@AfterEach
	void tearDown() throws SQLException {
		conn.close();
	}
	
	@Test
	void supplierTableIsPopulatedFromSupplierCSV() throws SQLException {
		conn = Database.connect(); 
		ModelSupplier.importSuppliers(conn, SAMPLE_SUPPLIERS_CSV);
		
		// Get item from output
		String sql = String.format("SELECT * from %s WHERE supplier_code = '%s';", table, KNOWN_SUPPLIER_CODE);
		ResultSet rs = SqlHelper.query(conn, sql);

		String actual = ""; 
		if (rs.next()) {
			actual = rs.getString("supplier_code");
		}
		assertEquals(KNOWN_SUPPLIER_CODE, actual);
	}

}
