package priceFileFormatter;

import static org.junit.jupiter.api.Assertions.*;

import java.sql.Connection;
import java.sql.SQLException;

import org.junit.jupiter.api.Test;

class DatabaseTest {

	@Test
	void testConnect() throws SQLException {
		Connection conn = Database.connect();
		Boolean connectionIsAlive = conn.isValid(0);
		assertTrue(connectionIsAlive);
	}
}
