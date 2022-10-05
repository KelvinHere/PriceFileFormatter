package priceFileFormatter;

public class PriceFileFormatter {
	public static void main(String[] args) {
		CSVReader csvReader = new CSVReader();
		csvReader.readCSV("data/sample-products.csv");
		
	}
}
