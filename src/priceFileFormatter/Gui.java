package priceFileFormatter;

import java.awt.*;
import javax.swing.*;


public class Gui {
	final static int FRAME_WIDTH = 1280;
	final static int FRAME_HEIGHT = 720;
	
	private CardSelectFiles CardSelectFiles;
	private JTabbedPane tabbedPane;

	// Files
	final static String FILE_SELECT = "Select Files";
    final static String DATA_SELECT = "Select Data";
    final static String PROCESSED_DATA = "Processed Data";
    
    PriceFileFormatter priceFileFormatter;
    
    
    public Gui(PriceFileFormatter priceFileFormatter) {
    	this.priceFileFormatter = priceFileFormatter;
    }
    
    
    public void addComponentToPane(Container pane) {
        tabbedPane = new JTabbedPane();
        // Card 1
        CardSelectFiles = new CardSelectFiles(priceFileFormatter);

        // Add cards to pane
        tabbedPane.addTab(FILE_SELECT, CardSelectFiles);
        
        pane.add(tabbedPane, BorderLayout.CENTER);
    }
 
    
    private void createAndShowGUI() {
        //Create and set up the window.
        JFrame frame = new JFrame("Price File Formatter");
        frame.setSize(FRAME_WIDTH, FRAME_HEIGHT);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        this.addComponentToPane(frame.getContentPane());
        frame.setVisible(true);
    }
    
    
    public void switchToCardSelectFiles() {
    	tabbedPane.setSelectedComponent(CardSelectFiles);
    }
 
    
    public void go() {
        try {
            UIManager.setLookAndFeel("javax.swing.plaf.metal.MetalLookAndFeel");
        } catch (UnsupportedLookAndFeelException ex) {
            ex.printStackTrace();
        } catch (IllegalAccessException ex) {
            ex.printStackTrace();
        } catch (InstantiationException ex) {
            ex.printStackTrace();
        } catch (ClassNotFoundException ex) {
            ex.printStackTrace();
        }
        UIManager.put("swing.boldMetal", Boolean.FALSE);
         
        //creating and showing this application's GUI.
        javax.swing.SwingUtilities.invokeLater(new Runnable() {
            public void run() {
                createAndShowGUI();
            }
        });
    }
    
    
}
