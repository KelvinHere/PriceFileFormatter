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
	JButton saveButton;
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
		
		// Buttons
		JPanel buttonPanel = new JPanel();
		saveButton = new JButton("Save");
		buttonPanel.add(saveButton);
		restartButton = new JButton("Restart");
		buttonPanel.add(restartButton);
		mainPanel.add(BorderLayout.NORTH, buttonPanel);
		

	}
	
	
	public void setListeners() {
		restartButton.addActionListener(new ResetButtonListener());
		saveButton.addActionListener(new SaveButtonListener());
	}
	
	
	private class SaveButtonListener implements ActionListener {
		public void actionPerformed(ActionEvent e) {
			priceFileFormatter.createOutputCsv();
		}
		
	}
	
	
	private class ResetButtonListener implements ActionListener {
		public void actionPerformed(ActionEvent e) {
			priceFileFormatter.resetSelf();
		}
		
	}

	
	public void updateOutputField(ResultSet rs) {
		outputField.setText("");
		try {
			outputField.append(ResultLineToCSV.getHeaders(rs));
			while (rs.next() ) {
				outputField.append(ResultLineToCSV.convert(rs));
			}
		} catch (SQLException e) {
			outputField.setText("Error reading results");
			e.printStackTrace();
		}
	}
}