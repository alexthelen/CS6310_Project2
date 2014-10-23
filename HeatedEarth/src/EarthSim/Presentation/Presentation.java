/**
 * 
 */
package EarthSim.Presentation;

import java.awt.Dimension;

import EarthSim.ComponentType;
import EarthSim.FinalTemperatureGrid;
import EarthSim.ProcessingComponent;
import EarthSim.ProcessingComponentListener;
import EarthSim.GUI.DataBuffer;
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
public class Presentation extends ProcessingComponent implements ProcessingComponentListener {
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
	public Presentation(DataBuffer<TemperatureGrid> buffer, Dimension minSize, Dimension maxSize, Dimension prefSize, boolean dedicatedThread) {
		super();
		_componentType = ComponentType.Presentation;
		_buffer = buffer;
		_earthPanel = new EarthPanel(minSize, maxSize, prefSize);
		threadName = "PresentationThread";
		setRunningInOwnThread(dedicatedThread);
		_isRunning = false;
	}

	@Override
	public void Stop() {
		_isPaused = true;
		this.updateGrid(new FinalTemperatureGrid());		
	}

	@Override
	public void run() {		
		_isRunning = true;
		if(hasInitiative()) {			
			RunPresentation();
		}
		else {
			idle();
		}
	}

	public void RunPresentation() {
		while(_isRunning) {
			Present();
		}		
	}	

	public void Present() {
		if(!_isPaused) {
			TemperatureGrid newGrid = null;

			if(_buffer != null) {
				try {
					newGrid = _buffer.Pull();
					System.out.println("Presentation: Pulling data from buffer");
					this.updateGrid(newGrid);
				} catch (Exception ex) {
					System.out.println("Presentation Error: " + ex.getMessage());
				}
			}

			System.out.println("Presentation: Processing Complete");
			this.processingComplete();			
		}
	}

	/**
	 * Updates the temperature grid begin displayed
	 * 
	 * @param grid 	the {@link TemperatureGrid} to be displayed in the map
	 */
	public void updateGrid(TemperatureGrid grid) {		
		_earthPanel.updateGrid(grid);
		_earthPanel.moveSunPosition((float)0.25);				
	}

	@Override
	public void onProcessComplete(ComponentType origin) {
		System.out.println("Presentation: No Initiative");
		Present();
	}
}
