/**
 * SqlHelper class executes SQL statements or returns ResultSets from SQL queries
 * 
 * SqlHelper.execute takes an active connection and SQL string, puts this into a
 * prepared statement and executes the statement.
 * 
 * SqlHelper.query takes an active connection and SQL string, puts this into a
 * prepared statement and executes the query and returns a result set.
 * 
 */

package priceFileFormatter;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class SqlHelper {

	public static void execute(Connection conn, String sql) {
		PreparedStatement ps;
		try {
			ps = conn.prepareStatement(sql);
			ps.execute();
			ps.close();
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}
	
	public static ResultSet query(Connection conn, String sql) {
		PreparedStatement ps;
		ResultSet rs = null;
		try {
			ps = conn.prepareStatement(sql);
			rs = ps.executeQuery();
			ps.close();
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return rs;		
	}
}
