package priceFileFormatter;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;

import javax.swing.*;


public class CardOutput extends JPanel {
	
	private static final long serialVersionUID = 1L;
	JTextArea outputField;
	Connection conn;
	
	
	
	public CardOutput(PriceFileFormatter priceFileFormatter) {
		this.conn = priceFileFormatter.getConnection();
		buildSwingComponents();
	}
	
	public void buildSwingComponents() {
		// Output field
		JPanel outputPanel = new JPanel();
		outputField = new JTextArea("Results Here");
		outputPanel.add(outputField);
		this.add(outputPanel);
	}
	
	public void updateOutputField(ResultSet rs) {
		outputField.setText("");
		try {
			while (rs.next() ) {
				outputField.append(rs.getString("abbrev_description") + "," +
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
									rs.getString("vat_switch") + "\n"
									);
			}
		} catch (SQLException e) {
			outputField.setText("Error reading results");
			e.printStackTrace();
		}
	}
}
