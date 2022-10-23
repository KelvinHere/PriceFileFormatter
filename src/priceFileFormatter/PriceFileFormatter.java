package priceFileFormatter;

import java.sql.Connection;
import java.sql.ResultSet;

import org.hsqldb.util.DatabaseManagerSwing;

public class PriceFileFormatter {
	
	private String csvImportFile = "data/sample-products.csv";
	private String csvSupplierFile = "data/sample-suppliers.csv";
	private String csvOutputFile = "data/sample-output.csv";
	private final boolean SHOW_DB_GUI = true;
	private Connection conn;
	private Gui gui;
	ModelOutput modelOutput;
	
	
	public static void main(String[] args) {
		PriceFileFormatter priceFileFormatter = new PriceFileFormatter();
		priceFileFormatter.getFiles();
	}
		
	
	public void getFiles() {
		// Stage 1 - Get files to process
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
	
	
	public void processFiles(String supplier) {
		// Stage 2 - Process selected files
		modelOutput = new ModelOutput(conn, csvOutputFile, supplier);
		ResultSet rs = modelOutput.getOutputData();
		gui.getCardOutput().updateOutputField(rs);
	}
	
	
	private void showDBGui(Connection conn) {
		DatabaseManagerSwing manager = new DatabaseManagerSwing();
		manager.main();
		manager.connect(conn);
		manager.start();
	}
	
	
	public void resetSelf() {
		String sql = "DROP SCHEMA PUBLIC CASCADE";
		SqlHelper.execute(conn, sql);
		gui.closeGUI();
		gui = null;
		conn = null;
		csvImportFile = "data/sample-products.csv";
		csvSupplierFile = "data/sample-suppliers.csv";
		csvOutputFile = "data/sample-output.csv";
		getFiles();
	}
	
	
	public void createOutputCsv() {
		modelOutput.createCsv(csvOutputFile);
	}

	
	// Getters & Setters
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
	
	public void setGui(Gui gui) {
		this.gui = gui;
	}
	
	public Gui getGui() {
		return gui;
	}
}
