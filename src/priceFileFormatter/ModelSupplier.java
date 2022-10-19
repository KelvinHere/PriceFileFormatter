package priceFileFormatter;

import java.sql.Connection;

public class ModelSupplier {
	public static final Enum<?> SUPPLIER_TABLE = Tables.SUPPLIER; 
	public static final String PRIMARY_KEY_FIELD = "supplier_code";
	public static final String UNIQUE_FIELD = "supplier_name"; 
	
	public static void importSuppliers(Connection conn, String fileLocation) {
		// Create importTable from CSV
		String sql = String.format("CREATE TABLE %s(%s);", SUPPLIER_TABLE, CsvGetFields.get(fileLocation));
		SqlHelper.execute(conn, sql);
		
		// Set primary key
		sql = String.format("ALTER TABLE %s ADD PRIMARY KEY (%s);", SUPPLIER_TABLE, PRIMARY_KEY_FIELD);
		SqlHelper.execute(conn, sql);
		
		// Only allow unique names
		sql = String.format("ALTER TABLE %s ADD UNIQUE (%s);", SUPPLIER_TABLE, UNIQUE_FIELD);
		SqlHelper.execute(conn, sql);
		
		// Import data into fields
		CsvImportData.insertCsvIntoDatabase(conn, fileLocation, SUPPLIER_TABLE);
	}
	
	public static Enum<?> getTableName() {
		return SUPPLIER_TABLE;
	}
}
