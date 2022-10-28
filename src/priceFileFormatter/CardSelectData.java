package priceFileFormatter;

import java.awt.BorderLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;

import javax.swing.*;


public class CardSelectData extends JPanel {
	private static final long serialVersionUID = 1L;
	private String[] columnNames;
	JTextArea importsTextArea;
	JButton restartButton;
	JButton nextButton;
	PriceFileFormatter priceFileFormatter;
	Connection conn;
		
	
	public CardSelectData(PriceFileFormatter priceFileFormatter) {
		this.priceFileFormatter = priceFileFormatter;
		this.conn = priceFileFormatter.getConnection();
		buildSwingComponents();
		setListeners();
	}
	
	public void buildSwingComponents() {
		// Main Panel
		JPanel mainPanel = new JPanel(new BorderLayout());
		this.add(mainPanel);
		
		// Data field
		JPanel outputPanel = new JPanel();
		importsTextArea = new JTextArea("Results Here");
		JScrollPane scroll = new JScrollPane(importsTextArea, JScrollPane.VERTICAL_SCROLLBAR_ALWAYS, JScrollPane.HORIZONTAL_SCROLLBAR_ALWAYS);
		outputPanel.add(scroll);
		mainPanel.add(BorderLayout.CENTER, outputPanel);
		
		// Buttons
		JPanel buttonPanel = new JPanel();
		nextButton = new JButton("Next");
		buttonPanel.add(nextButton);
		restartButton = new JButton("Restart");
		buttonPanel.add(restartButton);
		mainPanel.add(BorderLayout.NORTH, buttonPanel);
		

	}
	
	
	public void setListeners() {
		restartButton.addActionListener(new RestartButtonListener());
		nextButton.addActionListener(new NextButtonListener());
	}
	
	
	private class NextButtonListener implements ActionListener {
		public void actionPerformed(ActionEvent e) {
			priceFileFormatter.processFiles();
			priceFileFormatter.getGui().switchToCardOutput();
		}
		
	}
	
	
	private class RestartButtonListener implements ActionListener {
		public void actionPerformed(ActionEvent e) {
			priceFileFormatter.resetSelf();
		}
		
	}

	
	public void updateDataField(ResultSet rs) {
		importsTextArea.setText("");
		try {
			importsTextArea.append(ResultToCSV.getHeaders(rs, true));
			while (rs.next() ) {
				importsTextArea.append(ResultToCSV.resultFromImportTable(rs));
			}
		} catch (SQLException e) {
			importsTextArea.setText("Error reading results");
			e.printStackTrace();
		}
	}
	
	
	public void setColumnNames(String[] columnNames) {
		this.columnNames = columnNames;
	}
}
