package priceFileFormatter;

import java.awt.BorderLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;

import javax.swing.*;


public class CardOutput extends JPanel {
	
	private static final long serialVersionUID = 1L;
	JTextArea outputField;
	JButton restartButton;
	PriceFileFormatter priceFileFormatter;
	Connection conn;
		
	
	public CardOutput(PriceFileFormatter priceFileFormatter) {
		this.priceFileFormatter = priceFileFormatter;
		this.conn = priceFileFormatter.getConnection();
		buildSwingComponents();
		setListeners();
	}
	
	public void buildSwingComponents() {
		// Main Panel
		JPanel mainPanel = new JPanel(new BorderLayout());
		this.add(mainPanel);
		
		// Output field
		JPanel outputPanel = new JPanel();
		outputField = new JTextArea("Results Here");
		outputPanel.add(outputField);
		mainPanel.add(BorderLayout.CENTER, outputPanel);
		
		// Restart button
		JPanel buttonPanel = new JPanel();
		restartButton = new JButton("Restart");
		buttonPanel.add(restartButton);
		mainPanel.add(BorderLayout.NORTH, buttonPanel);
	}
	
	
	public void setListeners() {
		restartButton.addActionListener(new ResetButtonListener());
	}
	
	
	private class ResetButtonListener implements ActionListener {
		public void actionPerformed(ActionEvent e) {
			System.out.println("df");
			priceFileFormatter.resetSelf();
		}
		
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