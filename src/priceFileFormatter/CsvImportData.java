/* CsvImportData takes a connection, file location and table name enum
 * 
 * It inserts the CSV data into the specified database table and removes any
 * unwanted characters from said data
 * 
 */

package priceFileFormatter;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;


public class CsvImportData {
	// Imports CSV data from a file into a table
	
	public static void insertCsvIntoDatabase(Connection conn, String fileLocation, Enum<?> tableName) {
		String line = "";
		String splitBy = ",";
		
		try {
			// Open file - get number of fields and replace spaces in names - this also disposes of first line
			BufferedReader br = new BufferedReader(new FileReader(fileLocation));
			String fields = br.readLine().replace(" ", "_");;
			int numOfFields = numberOfFields(fields);
			String sqlValues = sqlValues(numOfFields);

			
			// Fill fields from CSV
			PreparedStatement ps;
			while ((line = br.readLine()) != null) {
				String cleanedLine = cleanLine(line);
				String[] item = cleanedLine.split(splitBy);
				String sql = String.format("INSERT INTO %s(%s) VALUES(%s)", tableName.toString(), fields, sqlValues);
				ps = conn.prepareStatement(sql);
				for (int i=0; i < numOfFields; i++) {
					ps.setString(i+1, item[i]);
				}
				ps.execute();
			}
			br.close();
		} catch(SQLException e) {
			e.printStackTrace();
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	
	private static int numberOfFields(String line) {
		// Returns number of fields from the line
		String[] fields = line.split(",");
		int numOfFields = fields.length;
		return numOfFields;
	}
	
	
	private static String sqlValues(int numOfFields) {
		// Creates the prepared statement (?, ?, ?, ?,) parameter
		String sqlValues = "?, ".repeat(numOfFields);
		sqlValues = sqlValues.substring(0, (sqlValues.length() - 2));
		return sqlValues;
	}
	
	
	static String cleanLine(String line) {
		// Remove any commas in within quotes
		Boolean openingQuote = false;
		StringBuilder output = new StringBuilder();
		
		for (int i=0; i < line.length(); i++) {
			if (line.charAt(i) == '"') {
				System.out.println("Double detected#############");
				System.out.println(line.charAt(i));
				openingQuote = !openingQuote;
			}
			if ((line.charAt(i) == ',') && (openingQuote == true)) {
				System.out.println("Comma detected#############");
				continue;
			}
			output.append(line.charAt(i));
		}
		
		// Remove any quotes
		String result = output.toString().replace("\"", "").replace("\\", "");


		System.out.println(output.toString());
		return result;
	}
}
