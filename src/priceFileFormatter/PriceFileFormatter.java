package priceFileFormatter;

import java.sql.Connection;
import org.hsqldb.util.DatabaseManagerSwing;

public class PriceFileFormatter {
	
	private static final String CSV_IMPORT_FILE = "data/sample-products.csv";
	private static final String CSV_SUPPLIER_FILE = "data/sample-suppliers.csv";
	private static final String CSV_OUTPUT_FILE = "data/sample-output.csv";
	
	public static void main(String[] args) {
		PriceFileFormatter priceFileFormatter = new PriceFileFormatter();
		priceFileFormatter.go();
	}
		
	public void go() {
		// Create database and insert new products from CSV
		Connection conn = Database.connect();
		// Create import & supplier tables and populate
		ModelImport.importItems(conn, CSV_IMPORT_FILE);
		ModelSupplier.importSuppliers(conn, CSV_SUPPLIER_FILE);
		
		// Create output table
		ModelOutput modelOutput = new ModelOutput(conn, CSV_OUTPUT_FILE);
		
		/* Debug GUI database 	*/		
		DatabaseManagerSwing manager = new DatabaseManagerSwing();
		manager.main();		manager.connect(conn);
		manager.start();
	}
}
