package priceFileFormatter;

import java.sql.Connection;

public class ModelImport {
	private static final Enum<?> IMPORT_TABLE = Tables.IMPORT; 
	
	public static void importItems(Connection conn, String fileLocation) {
		// Create importTable from CSV
		String fieldsSql = String.format("CREATE TABLE %s(%s)", IMPORT_TABLE, CsvGetFields.get(fileLocation));
		SqlHelper.execute(conn, fieldsSql);
		
		// Import data into fields
		CsvImportData.insertCsvIntoDatabase(conn, fileLocation, IMPORT_TABLE);
	}
	
	public static Enum<?> getTableName() {
		return IMPORT_TABLE;
	}
}
