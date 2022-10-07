package priceFileFormatter;

import java.sql.Connection;

public class ModelSupplier {
	private Enum<?> table = Tables.SUPPLIER; 
	private String primaryKeyField = "supplier_code"; 
	
	public ModelSupplier(Connection conn, String fileLocation) {
		// Create table from CSV
		String sql = String.format("CREATE table %s(%s);", table, CsvGetFields.get(fileLocation));
		SqlHelper.execute(conn, sql);
		
		// Set primary key
		sql = String.format("ALTER table %s ADD PRIMARY KEY (%s);", table, primaryKeyField);
		SqlHelper.execute(conn, sql);
		
		// Import data into fields
		CsvImportData.insertCsvIntoDatabase(conn, fileLocation, table);
		
	}
}
