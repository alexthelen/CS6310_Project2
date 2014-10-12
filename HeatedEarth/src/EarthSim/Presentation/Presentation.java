/**
 * 
 */
package EarthSim.Presentation;

import java.awt.Dimension;
import java.util.ArrayList;
import java.util.List;

import cs6310.gui.widget.earth.EarthPanel;
import cs6310.gui.widget.earth.TemperatureGrid;

/**
 * @author pablo
 *
 */
public class Presentation implements Runnable {
	private List<PresentationListener> _listeners;
	private boolean _stayIdle = false;
	private final static int IDLE_TIME = 1000;
	private final EarthPanel _earthPanel;
	
	/**
	 * @return the EarthPanel
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
	 * 
	 */
	public void updateGrid(TemperatureGrid grid) {
		_stayIdle = false;
		double height = this.getEarthPanel().getSize().getHeight();
		
		double angleIncrement = 180.0 / grid.getLatitudeLength();
		
		double previousHeightCos = 0;
		
		for (int i = 1; i < grid.getLatitudeLength() / 2; i++) {
			double currentAngle = angleIncrement * i;
			double cos = Math.cos(Math.toRadians(currentAngle));
			double currentHeightCos = 1 - cos;
			double currentHeightCoefficient = previousHeightCos - currentHeightCos;

			System.out.println("current angle: " + currentAngle + ", Cos: " + cos + ", Current height coefficient: " + currentHeightCoefficient * 500);
			
			previousHeightCos = currentHeightCos;
		}
		
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
