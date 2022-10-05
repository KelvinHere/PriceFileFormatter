package priceFileFormatter;

import java.io.BufferedReader;
import java.io.IOException;

public class CsvToSqlFields {
	String headersForTable;
	String fieldNamesForInsertion;
	int noOfFields;

	
	public CsvToSqlFields(BufferedReader br) {
		// Create SQL for creation of DB table and fields from first line of CSV
		String line = "";
		
		try {
			line = br.readLine();
			String[] headers = line.split(",");
			this.noOfFields = headers.length;
		
			// Format headers for SQL
			headersForTable = "";
			fieldNamesForInsertion = "";
			for (String header : headers) {
				String column = header.replace(" ", "_");
				headersForTable = headersForTable + column + " VARCHAR(255), ";
				fieldNamesForInsertion = fieldNamesForInsertion + column + ", ";
			}
			
			headersForTable = headersForTable.substring(0, (headersForTable.length() -2));
			fieldNamesForInsertion = fieldNamesForInsertion.substring(0, (fieldNamesForInsertion.length() -2));
			
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	public String getHeadersForTable() {
		return headersForTable;
	}
	
	public String getFieldNamesForInsertion() {
		return fieldNamesForInsertion;
	}
	
	public int getNoOfFields() {
		return noOfFields;
	}
}
