package priceFileFormatter;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;

public class CsvGetFields {
	
	public static String get(String fileLocation) {
		// Create SQL for creation of DB importTable and fields from first line of CSV
		String tableFieldsSql = null;
		String line = "";
		
		try {
			// Get array of CSV file headers
			BufferedReader br = new BufferedReader(new FileReader(fileLocation));
			line = br.readLine();
			String[] headers = line.split(",");
		
			// Format headers for SQL
			tableFieldsSql = "";
			for (String header : headers) {
				String column = header.replace(" ", "_");
				tableFieldsSql = tableFieldsSql + column + " VARCHAR(255), ";
			}
			
			// Assemble headers 
			tableFieldsSql = tableFieldsSql.substring(0, (tableFieldsSql.length() -2));
			br.close();
			
		} catch (IOException e) {
			e.printStackTrace();
		}
		
		return tableFieldsSql;
	}
}
