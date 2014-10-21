/**
 * 
 */
package EarthSim.Presentation;

import java.awt.Dimension;
import java.util.concurrent.BlockingQueue;

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
	private DataBuffer<TemperatureGrid> _buffer;
	private boolean _isRunning = false;
	private boolean _isPaused = false;

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
		_buffer = buffer;
		_earthPanel = new EarthPanel(minSize, maxSize, prefSize);
		threadName = "PresentationThread";
		setRunningInOwnThread(dedicatedThread);
		_isRunning = false;
	}

	public void Start() {
		_isPaused = false;
	}
	
	public void Pause() {
		_isPaused = true;
	}
	
	public void Resume() {
		_isPaused = false;
	}
	
	public void Stop() {
		_isPaused = true;
		_buffer.Clear();
		_buffer.Put(new FinalTemperatureGrid());
	}

	public void process() {

		_isPaused = true;
		if (this.isRunningInOwnThread()) startThread();		
		else startNoThread();
	}

	private void startThread() {
		System.out.println("Starting " +  threadName );
		if (thread == null)
		{
			thread = new Thread(this, threadName);
			thread.start();
		}
	}

	private void startNoThread() {
		run();
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

	private void Present() {
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

			if(hasInitiative()) {
				System.out.println("Presentation: Initiative");
				this.processingComplete();
			}
		}
	}

	/**
	 * Updates the temperature grid begin displayed
	 * 
	 * @param grid 	the {@link TemperatureGrid} to be displayed in the map
	 */
	public void updateGrid(TemperatureGrid grid) {
		//_stayIdle = false;		
		_earthPanel.updateGrid(grid);
		_earthPanel.moveSunPosition((float)0.25);		
		//_stayIdle = true;
		//idle();
	}

	@Override
	public void onProcessComplete() {
		System.out.println("Presentation: No Initiative");
		Present();
	}
}
