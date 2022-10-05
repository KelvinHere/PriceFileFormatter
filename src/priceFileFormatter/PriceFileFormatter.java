package priceFileFormatter;

import java.sql.Connection;

import org.hsqldb.util.DatabaseManagerSwing;

public class PriceFileFormatter {
	public static void main(String[] args) {
		PriceFileFormatter pff = new PriceFileFormatter();
		pff.go();
	}
		
	public void go() {
		Connection conn = Database.connect();
		CSVReader csvReader = new CSVReader();
		csvReader.readCSV("data/sample-products.csv", conn);
		
		/* Debug GUI database 	*/		
		DatabaseManagerSwing manager = new DatabaseManagerSwing();
		manager.main();		manager.connect(conn);
		manager.start();
		/* End of debug GUI database */
	}
}
