/* PriceFileFormatter creates a formatted CSV price file
 * 
 * Inputs are 	- new supplier price file
 * 				- existing report file (with current pricing)
 * 
 * User chooses data to pull from new supplier price file, this is formatted to a
 * predetermined structure, with pricing / mark-up / formatted descriptions already
 * applied and ready for legacy database to accept.
 * 
 * Outputs are 	- new CSV formatted price file to update legacy database
 * 				- CSV file of existing items not in new price file to be deactivated if no stock remains
 * 				- CSV file of existing items not in new price file that are in stock, to be marked down and sold
 * 
 */

package priceFileFormatter;

import java.sql.Connection;
import java.sql.ResultSet;
import java.util.Arrays;
import java.util.HashMap;

import org.hsqldb.util.DatabaseManagerSwing;

public class PriceFileFormatter {
	
	private String csvImportFile = "data/sample-products.csv";
	private String csvSupplierFile = "data/sample-suppliers.csv";
	private String csvOutputFile = "data/sample-output.csv";
	private final boolean SHOW_DB_GUI = true;
	private Connection conn;
	private Gui gui;
	private String selectedSupplier;
	private HashMap<Integer, String> dataLocations = new HashMap<>();
	ModelOutput modelOutput;
	ModelImport modelImport;
	
	
	public static void main(String[] args) {
		PriceFileFormatter priceFileFormatter = new PriceFileFormatter();
		priceFileFormatter.getFiles();
	}
		
	
	public void getFiles() {
		// Stage 1 - Get files to process
		conn = Database.connect();
		// Create supplier tables and populate
		ModelSupplier.importSuppliers(conn, csvSupplierFile);

		if (SHOW_DB_GUI)
			showDBGui(conn);
		
		// Initialise GUI
		Gui gui = new Gui(this);
		gui.go();
	}
	
	
	public void selectData(String selectedSupplier) {
		// Stage 2 - Select columns with needed data to create output & update gui
		// Create new items import table
		modelImport = new ModelImport(conn, csvImportFile);
		this.selectedSupplier = selectedSupplier;
		CardSelectData cardSelectData = gui.getCardSelectData();
		ResultSet rs = modelImport.getAllItemsInTable();
		cardSelectData.updateDataField(rs);
	}
	
	
	public void processFiles() {
		// Stage 3 - Process selected files
		modelOutput = new ModelOutput(conn, csvOutputFile, selectedSupplier, dataLocations);
		ResultSet rs = modelOutput.getAllItemsInTable();
		gui.getCardOutput().updateOutputField(rs);
	}
	
	
	public void createOutputCsv() {
		modelOutput.createCsv(csvOutputFile);
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
		dataLocations.clear();
		gui.closeGUI();
		gui = null;
		conn = null;
		modelOutput = null;
		modelImport = null;
		csvImportFile = "data/sample-products.csv";
		csvSupplierFile = "data/sample-suppliers.csv";
		csvOutputFile = "data/sample-output.csv";
		getFiles();
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
	
	public ModelImport getModelImport() {
		return modelImport;
	}
	
	public void setDataLocations(HashMap<Integer, String> locations) {
		dataLocations = locations;
	}
	
	public HashMap<Integer, String> getDataLocations() {
		return dataLocations;
	}
}
