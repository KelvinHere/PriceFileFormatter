package priceFileFormatter;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

public class CardSelectFiles extends JPanel {
	
	private static final long serialVersionUID = 1L;
	private JButton priceFileButton;
	private JButton reportButton;
	private JButton processButton;
	private JComboBox<String> supplierListDropdown;
	private JLabel instructions = new JLabel("<html><br><br>1. Select a New Price File<br> 2. (Optional) Select a Report File<br> 3. Press the NEXT button</html>");
	private JLabel suppliersMessageLabel = new JLabel("Select Supplier");
	private JLabel priceFileMessageLabel = new JLabel("Select Price File");
	private JLabel reportMessageLabel = new JLabel("Select Report File");
	private JFileChooser chooser;
	private PriceFileFormatter priceFileFormatter;
	
	private String priceFileLocation;
	private String reportFileLocation;
	
	public CardSelectFiles(PriceFileFormatter priceFileFormatter) {
		this.priceFileFormatter = priceFileFormatter;
		makeSuppliersDropdownMenu();
		buildSwingComponents();
		setListeners();		
		
		// Set file chooser to relative data directory
		String location = getClass().getProtectionDomain().getCodeSource().getLocation().toString();
		String formattedLocation = location.substring(6, (location.length() - 4)) + "data\\";
		formattedLocation = formattedLocation.replace("/", "\\");
		chooser = new JFileChooser(formattedLocation);
	}
	
	
	private void makeSuppliersDropdownMenu() {
		// Make JComboBox with all suppliers listed
		String sql = String.format("SELECT COUNT(*) AS total FROM %s", Tables.SUPPLIER);
		ResultSet rs = SqlHelper.query(priceFileFormatter.getConnection(), sql);
		int numOfSuppliers = 0;
		try {
			rs.next();
			numOfSuppliers = rs.getInt("total");
		} catch (SQLException e1) {
			e1.printStackTrace();
		}
		
		String[] supplierList = new String[numOfSuppliers];
		sql = String.format("SELECT * FROM %s", Tables.SUPPLIER);
		rs = SqlHelper.query(priceFileFormatter.getConnection(), sql);
		try {
			int count = 0;
			while (rs.next()) {
				supplierList[count] = (rs.getString("supplier_name"));
				count++;
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
		supplierListDropdown = new JComboBox<String>(supplierList);
	}

	
	private void buildSwingComponents() {
		// Main Card Panel
		this.setLayout(new BorderLayout());
		
		// Instructions Panel
		JPanel instructionsPanel = new JPanel();
		instructions.setHorizontalAlignment(JLabel.CENTER);
		instructionsPanel.setLayout(new BorderLayout());
		instructionsPanel.add(instructions);
		this.add(BorderLayout.NORTH, instructionsPanel);
		
		// Process button Panel
		JPanel processButtonPanel = new JPanel();
		processButton = new JButton("NEXT");
		processButtonPanel.add(processButton);
		this.add(BorderLayout.SOUTH, processButtonPanel);
		
		// Selection Panel
		JPanel selectionPanel = new JPanel();
		selectionPanel.setLayout(new GridBagLayout());
		GridBagConstraints gridBagConstraints = new GridBagConstraints();
		gridBagConstraints.gridx = 0;
		gridBagConstraints.ipady = 40;
		
		this.add(BorderLayout.CENTER, selectionPanel);
		
		// Suppliers panel
		JPanel suppliersPanel = new JPanel();
		suppliersPanel.setLayout(new BoxLayout(suppliersPanel, BoxLayout.PAGE_AXIS));
		suppliersMessageLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
		supplierListDropdown.setAlignmentX(Component.CENTER_ALIGNMENT);
		suppliersPanel.add(suppliersMessageLabel);
		suppliersPanel.add(supplierListDropdown);
		selectionPanel.add(suppliersPanel);
		
		// Price file Panel child of Selection Panel
		JPanel priceFilePanel = new JPanel();
		priceFilePanel.setLayout(new BoxLayout(priceFilePanel, BoxLayout.PAGE_AXIS));
		priceFileButton = new JButton("Select New Pricefile");
		priceFileButton.setAlignmentX(Component.CENTER_ALIGNMENT);
		priceFileMessageLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
		priceFilePanel.add(priceFileMessageLabel);
		priceFilePanel.add(priceFileButton);
		selectionPanel.add(priceFilePanel, gridBagConstraints);
		
		// Price report Panel child of Selection Panel
		JPanel priceReportPanel = new JPanel();
		priceReportPanel.setLayout(new BoxLayout(priceReportPanel, BoxLayout.PAGE_AXIS));
		reportButton = new JButton("Select Existing Report");
		reportButton.setAlignmentX(Component.CENTER_ALIGNMENT);
		reportMessageLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
		priceReportPanel.add(reportMessageLabel);
		priceReportPanel.add(reportButton);
		selectionPanel.add(priceReportPanel, gridBagConstraints);

	}
	
	
	public void setListeners() {
		priceFileButton.addActionListener(new PriceFileButtonListener());
		reportButton.addActionListener(new ReportButtonListener());
		processButton.addActionListener(new NextButtonListener());
	}
	
	
	private class ReportButtonListener implements ActionListener {
		public void actionPerformed(ActionEvent e) {
			if (chooser.showOpenDialog(null) == JFileChooser.APPROVE_OPTION) {
				String fileLocation = chooser.getSelectedFile().toString();
				File selectedFile = new File(fileLocation);
				if (selectedFile.exists()) {
					setReaderReportSheetFromFile(selectedFile);
					reportMessageLabel.setText("Selected File : " + selectedFile.getName());
				} else {
					reportMessageLabel.setText("File does not exist, try again.");
				}
			}
		}
	}
	
	
	private class PriceFileButtonListener implements ActionListener {
		public void actionPerformed(ActionEvent e) {
			if (chooser.showOpenDialog(null) == JFileChooser.APPROVE_OPTION) {
				String fileLocation = chooser.getSelectedFile().toString();
				File selectedFile = new File(fileLocation);
				if (selectedFile.exists()) {
					priceFileLocation = fileLocation;
					priceFileMessageLabel.setText("Selected File : " + selectedFile.getName());
				} else {
					reportMessageLabel.setText("File does not exist, try again.");
				}
			}
		}
	}
	
	
	private class NextButtonListener implements ActionListener {
		public void actionPerformed(ActionEvent e) {
			priceFileFormatter.processFiles(supplierListDropdown.getSelectedItem().toString());
		}
	}

	
	public void setReaderReportSheetFromFile(File selectedFile) {
		priceFileFormatter.setCsvReportFile(selectedFile.toString());
	}
	
	
	public void setReaderPriceFileSheetFromFile(File selectedFile) {
		priceFileFormatter.setCsvImportFile(selectedFile.toString());
	}
	
			
	public JPanel getCard() {
		return this;
	}
}



