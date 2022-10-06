package priceFileFormatter;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

public class OutputModel {
	String model = "CREATE TABLE IF NOT EXISTS output("
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
			+ "supplier VARCHAR(4), "
			+ "vat_switch VARCHAR(1), "
			+ "their_description VARCHAR(255))";

	
	public void createTable(Connection conn) {
		String sql = String.format(model);
		PreparedStatement ps;
		try {
			ps = conn.prepareStatement(sql);
			ps.execute();
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}

	
	public void populateModel(Connection conn) {
		// Fetch data from new imports
		String sql = "INSERT INTO output(their_sku, their_description) "
								+ "SELECT PRODUCT_CODE, PRODUCT_DESCRIPTION "
								+ "from new_imports";
		executeSQL(conn, sql);

		// Add prefix to our stock coded
		sql = "UPDATE output SET our_sku = 'FLA ' + their_sku";
		executeSQL(conn, sql);
		
		// Populate our description
		sql = "UPDATE output SET description = '* ' + LEFT(their_description, 58)";
		executeSQL(conn, sql);
		
		// Populate extra description
		sql = "UPDATE output SET extra_description = SUBSTRING(their_description, 59, 60)";
		executeSQL(conn, sql);
		
		// Populate abbreviated description
		sql = "UPDATE output SET abbrev_description = REGEXP_SUBSTR(their_description, '([^\\s]+\\s+[^\\s]+)')";
		executeSQL(conn, sql);
	}
	
	
	public void executeSQL(Connection conn, String sql) {
		try {
			PreparedStatement ps;
			ps = conn.prepareStatement(sql);
			ps.execute();
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}
		
}
