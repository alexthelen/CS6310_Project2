/**
 * 
 */
package EarthSim;

import java.util.ArrayList;
import java.util.List;

/**
 * @author pablo
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

	/**
	 * @return the hasInitiative
	 */
	public boolean hasInitiative() {
		return _hasInitiative;
	}
	
	/**
	 * Sets the initiative property
	 * @param init determines if the initiative is set
	 */
	public void setInitiative(boolean init) {
		_hasInitiative = init;
	}
	
	/**
	 * @return the runningInOwnThread
	 */
	public boolean isRunningInOwnThread() {
		return runningInOwnThread;
	}

	/**
	 * @param runningInOwnThread the runningInOwnThread to set
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
			listener.onProcessComplete();
		}
	}
	
	public ProcessingComponent() {
		_listeners = new ArrayList<ProcessingComponentListener>();
	}

//	@Override
//	public void run() {
//		_stayIdle = true;
//		idle();
//	}
	
	/**
	 * Stay idle as long as the there is no task at hand
	 */
	protected void idle() {
		_stayIdle = true;
		while (_stayIdle && this.isRunningInOwnThread()) {
			System.out.println(threadName + ": idle");
			try {
				Thread.sleep(IDLE_TIME);
			} catch (InterruptedException e) {
			}
		}
	}
}
