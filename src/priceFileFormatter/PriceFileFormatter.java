package priceFileFormatter;

import java.sql.Connection;

import org.hsqldb.util.DatabaseManagerSwing;

public class PriceFileFormatter {
	public static void main(String[] args) {
		PriceFileFormatter pff = new PriceFileFormatter();
		pff.go();
	}
		
	public void go() {
		// Create database and insert new products from CSV
		Connection conn = Database.connect();
		CSVReader csvReader = new CSVReader();
		csvReader.insertCsvIntoDatabase("data/sample-products.csv", conn);
		
		// Create formatted table and populate with the new items
		OutputModel outputModel = new OutputModel();
		outputModel.createTable(conn);
		outputModel.populateModel(conn);
		
		/* Debug GUI database 	*/		
		DatabaseManagerSwing manager = new DatabaseManagerSwing();
		manager.main();		manager.connect(conn);
		manager.start();

	}
}
