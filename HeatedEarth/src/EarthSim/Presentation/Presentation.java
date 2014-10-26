/**
 * 
 */
package EarthSim.Presentation;

import java.awt.Dimension;
import java.util.ArrayList;
import java.util.List;

import EarthSim.ComponentType;
import EarthSim.ProcessingComponent;
import EarthSim.ProcessingComponentListener;
import EarthSim.SimulationTimeListener;
import EarthSim.GUI.DataBuffer;
import EarthSim.Presentation.earth.EarthPanel;
import EarthSim.Presentation.earth.TemperatureGrid;

/**
 * Class to display and update an {@link EarthPanel}.
 * 
 * @author Pablo Gallastegui
 *
 * @version 1
 *
 */
public class Presentation extends ProcessingComponent implements ProcessingComponentListener {
	private static final float DEGREES_PER_MINUTE = (float)0.25;
	private static final float MAX_RUN_MINUTES = 14400; // 10 days

	private List<SimulationTimeListener> _simulationListeners;
	private final EarthPanel _earthPanel;		
	private int _simulationTimeStep = 1;
	private float _degreesPerIteration = DEGREES_PER_MINUTE;
	private float _displayRate = (float)0.01;
	private float _displayRateMillis = 10;
	private long _millisecondsOnLastRefresh;

	/**
	 * 
	 * @return an {@code int} specifying the amount of simulation minutes elapsed between iterations
	 */
	public int getSimulationTimeStep() {
		return _simulationTimeStep;
	}

	/**
	 * Set the amount of simulation minutes elapsed between iterations.
	 * @param _simulationTimeStep	an {@code int} specifying the amount of simulation minutes elapsed between iterations
	 */
	public void setSimulationTimeStep(int _simulationTimeStep) {
		this._simulationTimeStep = _simulationTimeStep;
		_degreesPerIteration = DEGREES_PER_MINUTE * (float)_simulationTimeStep;
	}

	/**
	 * 
	 * @return a {@code int} float with the amount of seconds in between display refreshes
	 */
	public float getDisplayRate() {
		return _displayRate;
	}

	/**
	 * Set the amount of seconds in between display refreshes
	 * @param _displayRate a {@code int} float with the amount of seconds in between display refreshes
	 */
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
	 * Add a listener to the presentation
	 * 
	 * @param listener a {@code PresentationListener} containing the functionality to execute when the Presentation has events to dispatch
	 */
	public void addSimulationListener(SimulationTimeListener listener) {
		_simulationListeners.add(listener);
	}
	
	/**
	 * Remove all listeners
	 */
	public void removeSimulationListeners() {
		_simulationListeners.clear();
	}

	/**
	 * <CTOR>
	 */
	public Presentation(DataBuffer<TemperatureGrid> buffer, Dimension minSize, Dimension maxSize, Dimension prefSize, boolean dedicatedThread) {
		super();
		_simulationListeners = new ArrayList<SimulationTimeListener>();
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
		_earthPanel.reset();	
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

	/**
	 * Start running the presentation
	 */
	public void RunPresentation() {
		while(_isRunning) {
			Present();
		}		
	}	

	/**
	 * Display an update to the grid
	 */
	public void Present() {
		if(!_isPaused) {
			TemperatureGrid newGrid = null;

			if(_buffer != null) {
				try {
					newGrid = _buffer.Pull();
					long difference = System.currentTimeMillis() - _millisecondsOnLastRefresh;
					
					if (_displayRateMillis > difference) {
						Thread.sleep((long) (_displayRateMillis - difference));
					}
					
					this.updateGrid(newGrid);
					_millisecondsOnLastRefresh = System.currentTimeMillis();
				} catch (Exception ex) {
					System.out.println("Presentation Error: " + ex.getMessage());
				}
			}

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
		_earthPanel.increaseTimeElapsed(getSimulationTimeStep());
		
		if (_earthPanel.getMinutesElapsed() > MAX_RUN_MINUTES) {
			this.simulationComplete();
		}
	}

	@Override
	public void onProcessComplete(ComponentType origin) {
		Present();
	}
	
	/**
	 * Fire the simulation complete event on all listeners
	 */
	protected void simulationComplete() {
		for (SimulationTimeListener listener : _simulationListeners) {
			listener.onSimulationComplete(_componentType);
		}
	}
}
