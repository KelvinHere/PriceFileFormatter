package priceFileFormatter;

import java.sql.Connection;
import org.hsqldb.util.DatabaseManagerSwing;

public class PriceFileFormatter {
	
	private String csvImportFile = "data/sample-products.csv";
	private String csvSupplierFile = "data/sample-suppliers.csv";
	private String csvOutputFile = "data/sample-output.csv";
	private final boolean SHOW_DB_GUI = true;
	private Connection conn;
	
	
	public static void main(String[] args) {
		PriceFileFormatter priceFileFormatter = new PriceFileFormatter();
		priceFileFormatter.getFiles();
	}
		
	
	public void getFiles() {
		conn = Database.connect();
		// Create import & supplier tables and populate
		ModelImport.importItems(conn, csvImportFile);
		ModelSupplier.importSuppliers(conn, csvSupplierFile);
		
		if (SHOW_DB_GUI)
			showDBGui(conn);
		
		// GUI
		Gui gui = new Gui(this);
		gui.go();
	}
	
	public void processFiles() {
		// Create output table in database
		ModelOutput modelOutput = new ModelOutput(conn, csvOutputFile);
	}

	
	public String getCsvImportFile() {
		return csvImportFile;
	}

	
	public void setCsvImportFile(String csvImportFile) {
		this.csvImportFile = csvImportFile;
	}
	
	
	public String getCsvReportFile() {
		return csvImportFile;
	}

	
	public void setCsvReportFile(String csvImportFile) {
		this.csvImportFile = csvImportFile;
	}

	
	public String getCsvSupplierFile() {
		return csvSupplierFile;
	}

	
	public void setCsvSupplierFile(String csvSupplierFile) {
		this.csvSupplierFile = csvSupplierFile;
	}

	
	public String getCsvOutputFile() {
		return csvOutputFile;
	}

	
	public void setCsvOutputFile(String csvOutputFile) {
		this.csvOutputFile = csvOutputFile;
	}
	
	
	public Connection getConnection() {
		return this.conn;
	}
	
	
	private void showDBGui(Connection conn) {
		DatabaseManagerSwing manager = new DatabaseManagerSwing();
		manager.main();
		manager.connect(conn);
		manager.start();
	}
		
}
