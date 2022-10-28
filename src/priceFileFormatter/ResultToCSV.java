package priceFileFormatter;

import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;

public class ResultToCSV {
	// TODO - Change these getStrings to their Enum counterparts
	public static String resultForOutputFile(ResultSet rs) throws SQLException {
		String csvLine = rs.getString("abbrev_description") + "," +
							rs.getString("their_sku") + "," +
							rs.getString("our_sku") + "," +
							rs.getString("description") + "," + 
							rs.getString("extra_description") + "," +
							rs.getDouble("Net_Cost") + "," +
							rs.getDouble("price_1") + "," +
							rs.getDouble("price_2") + "," +
							rs.getString("group_1") + "," +
							rs.getString("group_2") + "," +
							rs.getString("supplier_code") + "," +
							rs.getString("vat_switch") + "\n";
		return csvLine;
	}
	
	public static String getHeaders(ResultSet rs, Boolean addNewLine) throws SQLException{
		ResultSetMetaData rsmd = rs.getMetaData();
		int size = rsmd.getColumnCount();
		String headersAsCsv = "";
		
		for (int i=1; i <= size; i++) {
			headersAsCsv = headersAsCsv + rsmd.getColumnName(i) + ",";
		}

		headersAsCsv = headersAsCsv.substring(0, (headersAsCsv.length() -1));
		
		if (addNewLine == true) {
			headersAsCsv = headersAsCsv + "\n";
		}
		
		return headersAsCsv;
	}
	
	
	public static String resultFromImportTable(ResultSet rs) throws SQLException{
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
