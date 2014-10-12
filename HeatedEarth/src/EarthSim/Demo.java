/**
 * 
 */
package EarthSim;

import java.awt.Container;
import java.awt.Dimension;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Random;

import javax.swing.JButton;
import javax.swing.JFrame;

import EarthSim.Presentation.earth.EarthPanel;
import EarthSim.Presentation.earth.TemperatureGrid;

/**
 * @author pablo
 *
 */
public class Demo {

	/**
	 * @param args
	 */
	public static void main(String[] args) {
        //Schedule a job for the event-dispatching thread:
        //creating and showing this application's GUI.
        javax.swing.SwingUtilities.invokeLater(new Runnable() {
            public void run() {
                createAndShowGUI();
            }
        });
	}


    /**
     * Create the GUI and show it.  For thread safety,
     * this method should be invoked from the
     * event-dispatching thread.
     */
    private static void createAndShowGUI() {
        //Create and set up the window.
        final JFrame frame = new JFrame("Heated Plate");
        frame.setResizable(false);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        
        // for window layout
        Container container = frame.getContentPane();
        container.setLayout(new GridBagLayout());
        GridBagConstraints c = new GridBagConstraints();
        
        final EarthPanel earth = new EarthPanel(new Dimension(800, 500), new Dimension(800, 500), new Dimension(800, 500));

        c.fill = GridBagConstraints.HORIZONTAL;
        c.gridx = 0;
        c.gridy = 0;
        container.add(earth, c);

        
        final JButton runButton = new JButton("Randomize");
        
        c.gridx = 1;
        c.gridy = 0;
        container.add(runButton, c);

        // wire button action
        runButton.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent arg0) {
				FinalTemperatureGrid grid = new FinalTemperatureGrid();
		        earth.updateGrid(grid);
			}
        	
        });

        
        //Display the window.
        frame.pack();
        frame.setVisible(true);
    }
}
