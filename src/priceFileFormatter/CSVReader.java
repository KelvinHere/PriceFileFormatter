package priceFileFormatter;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;

public class CSVReader {
	public void readCSV(String fileLocation) {
		String line = "";
		String splitBy = ",";
		
		try {
			BufferedReader br = new BufferedReader(new FileReader(fileLocation));
			while ((line = br.readLine()) != null) {
				String[] item = line.split(splitBy);
				for (int i=0; i < item.length; i++) {
					System.out.print(item[i] + " | ");
				}
				System.out.println("");
			}
		} catch(IOException e) {
			e.printStackTrace();
		}
	}
}
