package priceFileFormatter;

import java.sql.Connection;

public class ModelImport {
	public static final Enum<?> TABLE = Tables.IMPORT; 
	
	public ModelImport(Connection conn, String fileLocation) {
		// Create table from CSV
		String fieldsSql = String.format("CREATE TABLE %s(%s)", TABLE, CsvGetFields.get(fileLocation));
		SqlHelper.execute(conn, fieldsSql);
		
		// Import data into fields
		CsvImportData.insertCsvIntoDatabase(conn, fileLocation, TABLE);
		
	}
}
