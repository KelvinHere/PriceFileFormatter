package priceFileFormatter;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

public class CSVReader {
	public void readCSV(String fileLocation, Connection conn) {
		String line = "";
		String splitBy = ",";
		
		try {
			BufferedReader br = new BufferedReader(new FileReader(fileLocation));

			// Create SQL for creation of DB table and fields from first line of CSV
			line = br.readLine();
			String[] headers = line.split(splitBy);

			// Format headers for SQL
			String formattedHeadersForTable = "";
			String formattedHeadersForInsertion = "";
			for (String header : headers) {
				String column = header.replace(" ", "_");
				formattedHeadersForTable = formattedHeadersForTable + column + " VARCHAR(255), ";
				formattedHeadersForInsertion = formattedHeadersForInsertion + column + ", ";
			}
			formattedHeadersForTable = formattedHeadersForTable.substring(0, (formattedHeadersForTable.length() -2));
			formattedHeadersForInsertion = formattedHeadersForInsertion.substring(0, (formattedHeadersForInsertion.length() -2));
			
			// Insert table and fields from formattedHeaders
			String sql = String.format("CREATE TABLE new_imports(%s)", formattedHeadersForTable);
			PreparedStatement ps = conn.prepareStatement(sql);
			ps.execute();
			
			// Fill database from CSV
			while ((line = br.readLine()) != null) {
				String[] item = line.split(splitBy);
				String values = "?, ".repeat(headers.length);
				values = values.substring(0, (values.length() - 2));
				sql = String.format("INSERT INTO new_imports(%s) VALUES(%s)", formattedHeadersForInsertion, values);
				ps = conn.prepareStatement(sql);
				for (int i=0; i < headers.length; i++) {
					ps.setString(i+1, item[i]);
				}
				ps.execute();
			}
		} catch(IOException e) {
			e.printStackTrace();
		} catch(SQLException e) {
			e.printStackTrace();
		}
	}
}
