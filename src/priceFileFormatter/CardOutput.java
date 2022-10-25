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
	JTextArea outputTextArea;
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
		outputTextArea = new JTextArea("Results Here");
		JScrollPane scroll = new JScrollPane(outputTextArea, JScrollPane.VERTICAL_SCROLLBAR_ALWAYS, JScrollPane.HORIZONTAL_SCROLLBAR_ALWAYS);
		outputPanel.add(scroll);
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
		outputTextArea.setText("");
		try {
			outputTextArea.append(ResultLineToCSV.getHeaders(rs));
			while (rs.next() ) {
				outputTextArea.append(ResultLineToCSV.convert(rs));
			}
		} catch (SQLException e) {
			outputTextArea.setText("Error reading results");
			e.printStackTrace();
		}
	}
}