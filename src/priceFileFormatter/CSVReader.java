package priceFileFormatter;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

public class CSVReader {
	public void insertCsvIntoDatabase(Connection conn, String fileLocation, Enum tableName) {
		String line = "";
		String splitBy = ",";
		
		try {
			BufferedReader br = new BufferedReader(new FileReader(fileLocation));

			// Insert table and fields from formattedHeaders
			CsvToSqlFields csvToSqlFields = new CsvToSqlFields(br);
			String sql = String.format("CREATE TABLE %s(%s)", tableName.toString(), csvToSqlFields.getHeadersForTable());
			PreparedStatement ps = conn.prepareStatement(sql);
			ps.execute();
			
			// Fill database from CSV
			while ((line = br.readLine()) != null) {
				String[] item = line.split(splitBy);
				String values = "?, ".repeat(csvToSqlFields.getNoOfFields());
				values = values.substring(0, (values.length() - 2));
				sql = String.format("INSERT INTO %s(%s) VALUES(%s)", tableName.toString(), csvToSqlFields.getFieldNamesForInsertion(), values);
				ps = conn.prepareStatement(sql);
				for (int i=0; i < csvToSqlFields.getNoOfFields(); i++) {
					ps.setString(i+1, item[i]);
				}
				ps.execute();
			}
		} catch(SQLException e) {
			e.printStackTrace();
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
}
