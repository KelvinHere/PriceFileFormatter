/* Creates an SQL statement to create a table with headers from supplied CSV file
 * 
 * Takes argument of fileLocation
 * 
 */

package priceFileFormatter;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;

public class CsvFileFieldsToSqlTable {
	
	public static String createTable(String fileLocation) {
		// Creates SQL statement for creation of TABLE and FIELDS from first line of CSV
		String tableFieldsSql = null;
		String[] headers = getColumnNames(fileLocation); 
		
		// Format headers for SQL
		tableFieldsSql = "";
		for (String header : headers) {
			tableFieldsSql = tableFieldsSql + header + " VARCHAR(255), ";
		}
		
		// Assemble headers 
		tableFieldsSql = tableFieldsSql.substring(0, (tableFieldsSql.length() -2));

		return tableFieldsSql;
	}
	
	
	public static String[] getColumnNames(String fileLocation) {
		// Creates SQL statement for creation of TABLE and FIELDS from first line of CSV
		String[] columnNames = {"Error : No Columns Found"};
		try {
			// Get array of CSV file headers
			BufferedReader br = new BufferedReader(new FileReader(fileLocation));
			String line = br.readLine();
			columnNames = line.split(",");
			
			for (int x=0; x < columnNames.length; x++) {
				columnNames[x] = columnNames[x].replace(" ", "_");
				columnNames[x] = columnNames[x].toUpperCase();
			}
			br.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
		
		return columnNames;
	}
}
