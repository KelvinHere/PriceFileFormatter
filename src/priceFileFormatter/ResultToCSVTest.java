package priceFileFormatter;

import static org.junit.jupiter.api.Assertions.*;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.HashMap;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;

class ResultToCSVTest {
	
	private static final String SAMPLE_ITEMS_CSV = "data/sample-products.csv";
	private static final String SAMPLE_SUPPLIERS_CSV = "data/sample-suppliers.csv";
	private static final String SAMPLE_OUTPUT_CSV = "data/sample-output.csv";
	private static final String DEFAULT_SUPPLIER = "Flair";
	
	private static final String EXPECTED_CSV_HEADERS_IMPORT = "PRODUCT_CODE,PRODUCT_DESCRIPTION,BARCODE,CATEGORY,DISCOUNT_TYPE,NEW_RRP,NET_COST";
	private static final String EXPECTED_CSV_HEADERS_OUTPUT = "ABBREV_DESCRIPTION,THEIR_SKU,OUR_SKU,DESCRIPTION,EXTRA_DESCRIPTION,NET_COST,PRICE_1,PRICE_2,GROUP_1,GROUP_2,SUPPLIER_CODE,VAT_SWITCH,THEIR_DESCRIPTION";
	private static final String EXPECTED_CSV_LINE = "1700 X,TSL17070R,FLA TSL17070R,• 1700 X 700mm Slimline Rectangle Tray,,87.00,139.11,128.41,BA,FLA,FL01,I,1700 X 700mm Slimline Rectangle Tray\n";
	
	private static HashMap<Integer, String> DATA_LOCATIONS = new HashMap<>();
	{
	DATA_LOCATIONS.put(0, "PRODUCT_CODE");
	DATA_LOCATIONS.put(1, "PRODUCT_DESCRIPTION");
	DATA_LOCATIONS.put(2, "NET_COST");
	}
	
	Connection conn;
	ModelOutput modelOutput;
	

	@AfterEach
	void tearDown() throws SQLException {
		String sql = "DROP SCHEMA PUBLIC CASCADE";
		SqlHelper.execute(conn, sql);
		modelOutput = null;
	}
	

	@Test
	void csvHeadersFromImportTable() throws SQLException {
		Connection conn = setup();

		String sql = String.format("SELECT * from %s", Tables.IMPORT);
		ResultSet rs = SqlHelper.query(conn, sql);
		String actualCsvHeaders = ResultToCSV.getHeaders(rs, false);
		assertEquals(EXPECTED_CSV_HEADERS_IMPORT, actualCsvHeaders);
	}
	
	
	@Test
	void csvHeadersFromOutputTable() throws SQLException {
		Connection conn = setup();

		String sql = String.format("SELECT * from %s", Tables.OUTPUT);
		ResultSet rs = SqlHelper.query(conn, sql);
		String actualCsvHeaders = ResultToCSV.getHeaders(rs, false);
		assertEquals(EXPECTED_CSV_HEADERS_OUTPUT, actualCsvHeaders);
	}
	
	
	@Test
	void csvForItemCreatedProperly() throws SQLException {
		Connection conn = setup();

		String sql = String.format("SELECT * from %s WHERE THEIR_SKU = 'TSL17070R'", Tables.OUTPUT);
		ResultSet rs = SqlHelper.query(conn, sql);
		rs.next();
		String actualCsvLine = ResultToCSV.convert(rs);
		assertEquals(EXPECTED_CSV_LINE, actualCsvLine);
	}
	
	
	public Connection setup() {
		conn = Database.connect(); 
		ModelSupplier.importSuppliers(conn, SAMPLE_SUPPLIERS_CSV);
		ModelImport modelImport = new ModelImport(conn, SAMPLE_ITEMS_CSV);
		modelOutput = new ModelOutput(conn, SAMPLE_OUTPUT_CSV, DEFAULT_SUPPLIER, DATA_LOCATIONS);
		return conn;
	}

}
