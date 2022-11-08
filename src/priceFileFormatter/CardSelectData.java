/* Creates the GUI to show the new supplier file and give user a chance
 * to select the data they want to use from it.  Allows the user to 
 * restart the process.
 * 
 */

package priceFileFormatter;

import java.awt.BorderLayout;
import java.awt.FlowLayout;
import java.awt.GridBagLayout;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.HashMap;

import javax.swing.*;


public class CardSelectData extends JPanel {
	private static final long serialVersionUID = 1L;
	private String[] columnNames;
	JTextArea importsTextArea;
	JButton restartButton;
	JButton nextButton;
	JComboBox<String> selectSKU;
	JComboBox<String> selectDescription;
	JComboBox<String> selectNetCost;
	PriceFileFormatter priceFileFormatter;
	Connection conn;
		
	
	public CardSelectData(PriceFileFormatter priceFileFormatter) {
		this.priceFileFormatter = priceFileFormatter;
		this.conn = priceFileFormatter.getConnection();
		columnNames = priceFileFormatter.getModelImport().getColumnNames();
		buildSwingComponents();
		setListeners();
	}
	
	public void buildSwingComponents() {
		// Main Panel
		JPanel mainPanel = new JPanel(new BorderLayout());
		this.add(BorderLayout.CENTER, mainPanel);
		
		// Data field
		JPanel outputPanel = new JPanel(new BorderLayout());
		importsTextArea = new JTextArea(30,90);
		JScrollPane scroll = new JScrollPane(importsTextArea, JScrollPane.VERTICAL_SCROLLBAR_ALWAYS, JScrollPane.HORIZONTAL_SCROLLBAR_ALWAYS);
		outputPanel.add(BorderLayout.CENTER, scroll);
		mainPanel.add(BorderLayout.CENTER, outputPanel);
		
		// Buttons
		JPanel buttonPanel = new JPanel();
		nextButton = new JButton("Next");
		buttonPanel.add(nextButton);
		restartButton = new JButton("Restart");
		buttonPanel.add(restartButton);
		mainPanel.add(BorderLayout.NORTH, buttonPanel);
		
		// Data/col selection
		JPanel selectPanel = new JPanel(new BorderLayout());
		JPanel selectNorthPanel = new JPanel();
		selectNorthPanel.setLayout(new BoxLayout(selectNorthPanel, BoxLayout.Y_AXIS));
		// SKU select
		JLabel selectSKULabel = new JLabel("Select SKU column"); 
		selectSKU = new JComboBox<>(columnNames);
		selectNorthPanel.add(selectSKULabel);
		selectNorthPanel.add(selectSKU);
		// Description select
		JLabel selectDescriptionLabel = new JLabel("Select Description column"); 
		selectDescription = new JComboBox<>(columnNames);
		selectNorthPanel.add(selectDescriptionLabel);
		selectNorthPanel.add(selectDescription);
		// Net Cost select
		JLabel selectNetCostLabel = new JLabel("Select Net Cost column"); 
		selectNetCost = new JComboBox<>(columnNames);
		selectNorthPanel.add(selectNetCostLabel);
		selectNorthPanel.add(selectNetCost);
		
		selectPanel.add(BorderLayout.NORTH, selectNorthPanel);
		mainPanel.add(BorderLayout.EAST, selectPanel);

	}
	
	
	public void setListeners() {
		restartButton.addActionListener(new RestartButtonListener());
		nextButton.addActionListener(new NextButtonListener());
	}
	
	
	private class NextButtonListener implements ActionListener {
		public void actionPerformed(ActionEvent e) {
			HashMap<Integer, String> order = new HashMap<>();
			order.put(0, selectSKU.getSelectedItem().toString());
			order.put(1, selectDescription.getSelectedItem().toString());
			order.put(2, selectNetCost.getSelectedItem().toString());
			priceFileFormatter.setDataLocations(order);
			
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
				importsTextArea.append(ResultToCSV.convert(rs));
			}
			importsTextArea.setCaretPosition(0);
		} catch (SQLException e) {
			importsTextArea.setText("Error reading results");
			e.printStackTrace();
		}
	}
	
	
	public void setColumnNames(String[] columnNames) {
		this.columnNames = columnNames;
	}
}
