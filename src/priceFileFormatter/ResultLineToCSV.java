package priceFileFormatter;

import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;

public class ResultLineToCSV {
	public static String convert(ResultSet rs) throws SQLException {
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
	
	public static String getHeaders(ResultSet rs) throws SQLException{
		ResultSetMetaData rsmd = rs.getMetaData();
		int size = rsmd.getColumnCount();
		String headersAsCsv = "";
		
		for (int i=1; i <= size; i++) {
			headersAsCsv = headersAsCsv + rsmd.getColumnName(i) + ",";
		}

		headersAsCsv = headersAsCsv.substring(0, (headersAsCsv.length() -1));
		headersAsCsv = headersAsCsv + "\n";
		
		return headersAsCsv;
	}
}
