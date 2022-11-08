/* Creates a temporary in memory database for the program, returns the connection 
 * 
 */

package priceFileFormatter;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class Database {
	   //static final String DB = "jdbc:hsqldb:file:data/db";
	   static final String DB = "jdbc:hsqldb:mem:data/db;sql.syntax_mys=true;shutdown=true";
	   static final String USER_NAME = "admin";
	   static final String PASSWORD = "";

	public static Connection connect() {
		Connection conn = null;
		try {
			conn = DriverManager.getConnection(DB, USER_NAME, PASSWORD);
			System.out.println("Database connected:" + DB);
		} catch (SQLException e ) {
			System.out.println(e.getMessage() + " *Database connection class");
		}
		return conn;
	}
}