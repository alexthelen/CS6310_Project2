/**
 * 
 */
package EarthSim.Presentation;

import java.awt.Dimension;

import org.omg.PortableInterceptor.SYSTEM_EXCEPTION;

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
	private static final float DEGREES_PER_MINUTE = (float)0.25;
	
	private final EarthPanel _earthPanel;		
	private int _simulationTimeStep;
	private float _degreesPerIteration;
	private float _displayRate = (float)0.01;
	private float _displayRateMillis = 10;
	private long _millisecondsOnLastRefresh;

	public int getSimulationTimeStep() {
		return _simulationTimeStep;
	}

	public void setSimulationTimeStep(int _simulationTimeStep) {
		this._simulationTimeStep = _simulationTimeStep;
		_degreesPerIteration = DEGREES_PER_MINUTE * (float)_simulationTimeStep;
	}

	public float getDisplayRate() {
		return _displayRate;
	}

	public void setDisplayRate(float _displayRate) {
		this._displayRate = _displayRate;
		this._displayRateMillis = _displayRate * 1000;
	}

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

					long difference = System.currentTimeMillis() - _millisecondsOnLastRefresh;
					
					if (_displayRateMillis > difference) {
						Thread.sleep((long) (_displayRateMillis - difference));
					} else {
						System.out.println(difference);
					}
					
					this.updateGrid(newGrid);
					_millisecondsOnLastRefresh = System.currentTimeMillis();
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
		_earthPanel.moveSunPosition(_degreesPerIteration);				
	}

	@Override
	public void onProcessComplete(ComponentType origin) {
		System.out.println("Presentation: No Initiative");
		Present();
	}
}
