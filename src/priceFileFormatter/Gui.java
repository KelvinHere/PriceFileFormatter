/* A tabbed GUI to house all cards in each step of the program
 * 
 */

package priceFileFormatter;

import java.awt.*;
import javax.swing.*;


public class Gui {
	final static int FRAME_WIDTH = 1400;
	final static int FRAME_HEIGHT = 720;
	
	JFrame frame;
	private CardSelectFiles cardSelectFiles;
	private CardOutput cardOutput;
	private CardSelectData cardSelectData;
	private JTabbedPane tabbedPane;

	// Files
	final static String FILE_SELECT = "1, Select Files";
    final static String SELECT_DATA = "2, Select Data";
    final static String PROCESSED_DATA = "3, Output";
    
    PriceFileFormatter priceFileFormatter;
    
    
    public Gui(PriceFileFormatter priceFileFormatter) {
    	this.priceFileFormatter = priceFileFormatter;
    	priceFileFormatter.setGui(this);
    }
    
    
    public void addComponentToPane(Container pane) {
        tabbedPane = new JTabbedPane();
        // Card 1 - Select Files
        cardSelectFiles = new CardSelectFiles(priceFileFormatter);
        // Card 2 - Select Files
        cardSelectData = new CardSelectData(priceFileFormatter);
        // Card 3 - Output
        cardOutput = new CardOutput(priceFileFormatter);

        // Add cards to pane
        tabbedPane.addTab(FILE_SELECT, cardSelectFiles);
        tabbedPane.addTab(SELECT_DATA, cardSelectData);
        tabbedPane.addTab(PROCESSED_DATA, cardOutput);
        
        //tabbedPane.setEnabledAt(1, false);
        pane.add(tabbedPane, BorderLayout.CENTER);
    }
 
    
    private void createAndShowGUI() {
        //Create and set up the window.
        frame = new JFrame("Price File Formatter");
        frame.setSize(FRAME_WIDTH, FRAME_HEIGHT);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        this.addComponentToPane(frame.getContentPane());
        frame.setVisible(true);
    }
    
    
    public void switchToCardSelectFiles() {
    	tabbedPane.setEnabledAt(1, false);
    	tabbedPane.setSelectedComponent(cardSelectFiles);
    }
    
    
    public void switchToCardOutput() {
    	tabbedPane.setEnabledAt(0, false);
    	tabbedPane.setSelectedComponent(cardOutput);
    }
    
    
    public void switchToCardSelectData() {
    	tabbedPane.setSelectedComponent(cardSelectData);
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
    
    
    public CardOutput getCardOutput() {
    	return cardOutput;
    }
    
    
    public CardSelectData getCardSelectData() {
    	return cardSelectData;
    }
    
    
    public void closeGUI() {
    	frame.dispose();
    }
}
