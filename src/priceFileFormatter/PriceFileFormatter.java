package priceFileFormatter;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;

import org.hsqldb.util.DatabaseManagerSwing;

public class PriceFileFormatter {
	
	private static final String CSV_IMPORT_FILE = "data/sample-products.csv";
	private static final String CSV_SUPPLIER_FILE = "data/sample-suppliers.csv";
	private static final String CSV_OUTPUT_FILE = "data/sample-output.csv";
	
	public static void main(String[] args) {
		PriceFileFormatter pff = new PriceFileFormatter();
		pff.go();
	}
		
	public void go() {
		// Create database and insert new products from CSV
		Connection conn = Database.connect();
		// Create all needed tables
		ModelImport modelImport = new ModelImport(conn, CSV_IMPORT_FILE);
		ModelSupplier modelSupplier = new ModelSupplier(conn, CSV_SUPPLIER_FILE);
		ModelOutput modelOutput = new ModelOutput(conn, CSV_OUTPUT_FILE);
		
		String sql = "SELECT * FROM OUTPUT INNER JOIN SUPPLIER ON output.supplier_code = supplier.supplier_code WHERE their_sku = 'AYO8RP45SC'";
		ResultSet rs = SqlHelper.query(conn, sql);
		try {
			if (rs.next()) {
				System.out.println(rs.getString("our_sku"));
				System.out.println(rs.getString("their_sku"));
				System.out.println(rs.getString("sku_prefix"));
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
		
		
		
		/* Debug GUI database 	*/		
		DatabaseManagerSwing manager = new DatabaseManagerSwing();
		manager.main();		manager.connect(conn);
		manager.start();

	}
}
