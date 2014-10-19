/**
 * 
 */
package EarthSim.Presentation;

import java.awt.Dimension;

import EarthSim.ProcessingComponent;
import EarthSim.Presentation.earth.EarthPanel;
import EarthSim.Presentation.earth.TemperatureGrid;

/**
 * Class to display and update an {@link EarthPanel} in its own thread.
 * 
 * @author Pablo Gallastegui
 *
 * @version 1.0
 *
 */
public class Presentation extends ProcessingComponent {
	private final EarthPanel _earthPanel;
	
	/**
	 * @return the {@link EarthPanel} being displayed
	 */
	public EarthPanel getEarthPanel() {
		return _earthPanel;
	}
	
	/**
	 * <CTOR>
	 */
	public Presentation(Dimension minSize, Dimension maxSize, Dimension prefSize) {
		super();
        _earthPanel = new EarthPanel(minSize, maxSize, prefSize);
	}
	
	/**
	 * Updates the temperature grid begin displayed
	 * 
	 * @param grid 	the {@link TemperatureGrid} to be displayed in the map
	 */
	public void updateGrid(TemperatureGrid grid) {
		_stayIdle = false;		
		_earthPanel.updateGrid(grid);
		_earthPanel.moveSunPosition((float)0.25);
		presentationComplete();
		_stayIdle = true;
		idle();
	}
}
