/**
 * 
 */
package EarthSim;

import java.util.ArrayList;
import java.util.List;

import EarthSim.GUI.DataBuffer;
import EarthSim.Presentation.earth.TemperatureGrid;

/**
 * Abstract class containing common functionality to run a process either in
 * a shared or its own thread, referencing data from a buffer and reporting
 * completed status through listeners
 * 
 * @author Pablo Gallastegui
 * @author TJ Baxter
 * @version 1
 *
 */
public abstract class ProcessingComponent implements Runnable {
	private List<ProcessingComponentListener> _listeners;
	protected boolean _stayIdle = false;
	private boolean runningInOwnThread = false;
	protected final static int IDLE_TIME = 1000;
	protected Thread thread;
	protected String threadName;
	protected boolean _hasInitiative = false;
	protected ComponentType _componentType;	
	protected DataBuffer<TemperatureGrid> _buffer;
	protected boolean _isRunning;
	protected boolean _isPaused = false;

	/**
	 * Start the process
	 */
	public void Start() {
		_isPaused = false;
	}

	/**
	 * Pause the process
	 */
	public void Pause() {
		_isPaused = true;
	}

	/**
	 * Resume the process from paused
	 */
	public void Resume() {
		_isPaused = false;
	}

	/**
	 * Stop the process
	 */
	public void Stop() {
		_isPaused = true;		
	}

	/**
	 * Start running the process
	 */
	public void process() {

		_isPaused = true;
		if (this.isRunningInOwnThread()) startThread();		
		else startNoThread();
	}

	/**
	 * Start running the process in its own thread
	 */
	private void startThread() {
		if (thread == null)
		{
			thread = new Thread(this, threadName);
			thread.start();
		}
	}

	/**
	 * Start running the process in a shared thread
	 */
	private void startNoThread() {
		run();
	}
	
	/**
	 * @return a {@code boolean} indicating if this process has the initiative
	 */
	public boolean hasInitiative() {
		return _hasInitiative;
	}
	
	/**
	 * Sets the initiative property
	 * @param init determines if this component has the initiative
	 */
	public void setInitiative(boolean init) {
		_hasInitiative = init;
	}

	/**
	 * @return a {@code boolean} indicating if this process is running in its own thread
	 */
	public boolean isRunningInOwnThread() {
		return runningInOwnThread;
	}

	/**
	 * Sets the value specifying if this process is running in its own thread
	 * @param runningInOwnThread	A {@code boolean} value indicating if the process is running in its own thread
	 */
	public void setRunningInOwnThread(boolean runningInOwnThread) {
		this.runningInOwnThread = runningInOwnThread;
	}

	/**
	 * Add a listener to the presentation
	 * 
	 * @param listener a {@code PresentationListener} containing the functionality to execute when the Presentation has events to dispatch
	 */
	public void addListener(ProcessingComponentListener listener) {
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
	protected void processingComplete() {
		for (ProcessingComponentListener listener : _listeners) {
			listener.onProcessComplete(_componentType);
		}
	}
	
	/**
	 * <CTOR>
	 */
	public ProcessingComponent() {
		_listeners = new ArrayList<ProcessingComponentListener>();
	}
	
	/**
	 * Stay idle as long as the there is no task at hand
	 */
	protected void idle() {
		_stayIdle = true;
		while (_stayIdle && this.isRunningInOwnThread()) {
			try {
				Thread.sleep(IDLE_TIME);
			} catch (InterruptedException e) {
			}
		}
	}
}
