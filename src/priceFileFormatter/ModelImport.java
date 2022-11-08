/* This Model dynamically creates the table for the new supplier price file.
 * 
 */

package priceFileFormatter;

import java.sql.Connection;
import java.sql.ResultSet;

public class ModelImport {
	private String[] columnNames;
	private Connection conn;
	
	
	public ModelImport(Connection conn, String fileLocation) {
		this.conn = conn;
		// Get column names from import file
		columnNames = CsvFileFieldsToSqlTable.getColumnNames(fileLocation);

		// Create IMPORT table from CSV file
		String fieldsSql = String.format("CREATE TABLE %s(%s)", Tables.IMPORT, CsvFileFieldsToSqlTable.createTable(fileLocation));
		SqlHelper.execute(conn, fieldsSql);
		
		// Import data into fields of IMPORT table
		CsvImportData.insertCsvIntoDatabase(conn, fileLocation, Tables.IMPORT);
	}
	
	
	public ResultSet getAllItemsInTable() {
		String sql = String.format("SELECT * FROM %s", Tables.IMPORT);
		ResultSet rs = SqlHelper.query(conn, sql); 
		return rs;
	}
	
	
	public Enum<?> getTableName() {
		return Tables.IMPORT;
	}
	
	
	public String[] getColumnNames() {
		return columnNames;
	}
}
