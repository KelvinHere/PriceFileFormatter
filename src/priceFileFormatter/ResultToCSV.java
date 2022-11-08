/* ResultToCSV extracts information from a Result Set and returns it in CSV format
 *
 * ResultToCSV.convert takes a Result Set and returns it in CSV format with a line break
 * 
 * ResultToCSV.getHeaders returns headers from metadata, takes a Result Set and a 
 * boolean value as arguments to add or omit a line break in the result
 * 
 */

package priceFileFormatter;

import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;

public class ResultToCSV {
	// Takes a result-set and returns its headers from metadata
	public static String getHeaders(ResultSet rs, Boolean addLineBreak) throws SQLException{
		ResultSetMetaData rsmd = rs.getMetaData();
		int size = rsmd.getColumnCount();
		String headersAsCsv = "";
		
		for (int i=1; i <= size; i++) {
			headersAsCsv = headersAsCsv + rsmd.getColumnName(i) + ",";
		}

		headersAsCsv = headersAsCsv.substring(0, (headersAsCsv.length() -1));
		headersAsCsv = (addLineBreak == true) ? (headersAsCsv + "\n") : headersAsCsv;
		
		return headersAsCsv;
	}
	
	
	// Takes a result-set line and returns it in CSV format
	public static String convert(ResultSet rs) throws SQLException{
		String[] headers = getHeaders(rs, false).split(",");
		String csvLine = "";
		
		for (String header : headers) {
			csvLine = csvLine + rs.getString(header) + ",";
		}
		
		csvLine = csvLine.substring(0, (csvLine.length() -1));
		csvLine = csvLine + "\n";
		return csvLine;
	}
}
