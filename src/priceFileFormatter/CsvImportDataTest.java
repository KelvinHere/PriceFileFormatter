package priceFileFormatter;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;

class CsvImportDataTest {
	private static final String STRING_WITH_QUOTES = "This \"has\" too many\" quotes in\" it\"\"";
	private static final String STRING_WITH_QUOTES_EXPECTED_RESULT = "This has too many quotes in it";
	
	private static final String STRING_WITH_BACKSLASHES = "This \\has\\ many\\ slashes in\\ it\\\\";
	private static final String STRING_WITH_BACKSLASHES_EXPECTED_RESULT = "This has many slashes in it";
	
	private static final String STRING_WITH_COMMAS_BETWEEN_QUOTES = "Outside, Quotes \"Between, Quotes, Here\", Outside, again";
	private static final String STRING_WITH_COMMAS_BETWEEN_QUOTES_EXPECTED_RESULT = "Outside, Quotes Between Quotes Here, Outside, again";
	
	@Test
	void testRemovesQuotesFromString() {
		String actual = CsvImportData.cleanLine(STRING_WITH_QUOTES);
		assertEquals(STRING_WITH_QUOTES_EXPECTED_RESULT, actual);		
	}
	
	@Test
	void testRemoveBackslashesFromString() {
		String actual = CsvImportData.cleanLine(STRING_WITH_BACKSLASHES);
		assertEquals(STRING_WITH_BACKSLASHES_EXPECTED_RESULT, actual);		
	}
	
	@Test
	void testRemovesCommasOnlyFromBetweenQuotes() {
		String actual = CsvImportData.cleanLine(STRING_WITH_COMMAS_BETWEEN_QUOTES);
		assertEquals(STRING_WITH_COMMAS_BETWEEN_QUOTES_EXPECTED_RESULT, actual);		
	}

}
