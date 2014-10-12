/**
 * 
 */
package EarthSim.Presentation;

import java.awt.Dimension;
import java.util.ArrayList;
import java.util.List;

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
public class Presentation implements Runnable {
	private List<PresentationListener> _listeners;
	private boolean _stayIdle = false;
	private final static int IDLE_TIME = 1000;
	private final EarthPanel _earthPanel;
	
	/**
	 * @return the {@link EarthPanel} being displayed
	 */
	public EarthPanel getEarthPanel() {
		return _earthPanel;
	}

	/**
	 * Add a listener to the presentation
	 * 
	 * @param listener a {@code PresentationListener} containing the functionality to execute when the Presentation has events to dispatch
	 */
	public void addPresentationListener(PresentationListener listener) {
		_listeners.add(listener);
	}
	
	/**
	 * Remove all listeners
	 */
	public void removeListeners() {
		_listeners.clear();
	}
	
	/**
	 * Fire the presentation complete event on all listeners
	 */
	private void presentationComplete() {
		for (PresentationListener listener : _listeners) {
			listener.onPresentationComplete();
		}
	}
	
	/**
	 * <CTOR>
	 */
	public Presentation(Dimension minSize, Dimension maxSize, Dimension prefSize) {
		_listeners = new ArrayList<PresentationListener>();
        _earthPanel = new EarthPanel(minSize, maxSize, prefSize);
	}

	@Override
	public void run() {
		_stayIdle = true;
		idle();
	}
	
	/**
	 * Updates the temperature grid begin displayed
	 * 
	 * @param grid 	the {@link TemperatureGrid} to be displayed in the map
	 */
	public void updateGrid(TemperatureGrid grid) {
		_stayIdle = false;		
		_earthPanel.updateGrid(grid);
		presentationComplete();
		_stayIdle = true;
		idle();
	}
	
	/**
	 * Stay idle as long as the there is no task at hand
	 */
	private void idle() {
		while (_stayIdle) {
			try {
				Thread.sleep(IDLE_TIME);
			} catch (InterruptedException e) {
			}
		}
	}
}
