package priceFileFormatter;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;


public class CsvImportData {
	
	public static void insertCsvIntoDatabase(Connection conn, String fileLocation, Enum<?> tableName) {
		String line = "";
		String splitBy = ",";
		
		try {
			// Open buffer and collect number of fields present in file this also disposes of first line
			BufferedReader br = new BufferedReader(new FileReader(fileLocation));
			String fields = br.readLine().replace(" ", "_");;
			int numOfFields = numberOfFields(fields);
			String sqlValues = sqlValues(numOfFields);

			
			// Fill fields from CSV
			PreparedStatement ps;
			while ((line = br.readLine()) != null) {
				String[] item = line.split(splitBy);
				String sql = String.format("INSERT INTO %s(%s) VALUES(%s)", tableName.toString(), fields, sqlValues);
				ps = conn.prepareStatement(sql);
				for (int i=0; i < numOfFields; i++) {
					// Replaced unwanted characters before insertion
					String currentItem = cleanImportedItem(item[i]);
					ps.setString(i+1, currentItem);
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
	
	
	private static String cleanImportedItem(String currentItem) {
		// Basic cleaning of string
		String item = currentItem;
		item = item.replace(",", "-");
		item = item.replace("\"", "");
		return item;
	}
}
