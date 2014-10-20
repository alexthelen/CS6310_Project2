/**
 * 
 */
package EarthSim.Presentation;

import java.awt.Dimension;
import java.util.concurrent.BlockingQueue;

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
	public BlockingQueue<TemperatureGrid> temperatureGrid;
	public TemperatureGrid newGrid;
	
	/**
	 * @return the {@link EarthPanel} being displayed
	 */
	public EarthPanel getEarthPanel() {
		return _earthPanel;
	}
	
	/**
	 * <CTOR>
	 */
	public Presentation(Dimension minSize, Dimension maxSize, Dimension prefSize, boolean dedicatedThread) {
		super();
        _earthPanel = new EarthPanel(minSize, maxSize, prefSize);
        threadName = "PresentationThread";
        setRunningInOwnThread(dedicatedThread);
	}
	
	public void startThread() {
		System.out.println("Starting " +  threadName );
		if (thread == null)
		{
			thread = new Thread(this, threadName);
			thread.start();
		}
	}
	
	public void startNoThread() {
		run();
	}
	
	public void run() {		
		if (temperatureGrid != null) {
			TemperatureGrid newGrid;
			try {
				while ((newGrid = temperatureGrid.take()) != null) {
					this.updateGrid(newGrid);
					Thread.sleep(1);
				}
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}
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
		processingComplete();
		_stayIdle = true;
		idle();
	}
}
