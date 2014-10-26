package EarthSim.SimulationEngine;
import EarthSim.ComponentType;
import EarthSim.FinalTemperatureGrid;
import EarthSim.ProcessingComponent;
import EarthSim.ProcessingComponentListener;
import EarthSim.GUI.DataBuffer;
import EarthSim.Presentation.earth.TemperatureGrid;

/**
 * Runs a simulation of the distribution of heat on earth
 * 
 * @author Alexander Thelen
 * @version 1
 *
 */
public class SimulationEngine extends ProcessingComponent implements ProcessingComponentListener
{
	//Attributes--------------------------
	private Planet earth;
	private int _gridSize;
	private int _minutesPerRotation;

	//Accessors---------------------------
	public int GetGridSize() { return this._gridSize; }
	public void SetGridSize(int gridSize) {
		if(gridSize > 180)
			gridSize = 180;

		if(gridSize < 0)
			gridSize = 1;

		while(180 % gridSize != 0)
		{
			gridSize--;
		}
		this._gridSize = gridSize;

		try 
		{
			this.earth = new Planet(this._gridSize);
		} 
		catch (Exception e) 
		{
			System.out.println("Simulation Error: " + e.getMessage());
		}
	}
	public int GetMinutesPerRotation() { return this._minutesPerRotation; }
	public void SetMinutesPerRotation(int value) { this._minutesPerRotation = value; }	

	//Constructors------------------------
	/**
	 * <CTOR>
	 * @param buffer 			a {@link DataBuffer} to place the output of the simulation
	 * @param dedicatedThread	a {@code boolean} specifying if the simulation runs in its own thread
	 */
	public SimulationEngine(DataBuffer<TemperatureGrid> buffer, boolean dedicatedThread)
	{
		_componentType = ComponentType.Simulation;
		this._buffer = buffer;
		this._minutesPerRotation = 1;
		this._isRunning = false;
		threadName = "SimulationThread";
		setRunningInOwnThread(dedicatedThread);
		this.SetGridSize(15);

	}	

	@Override
	public void Stop() {
		_isPaused = true;		
		this.earth = null;
		try {
			this.earth = new Planet(this._gridSize);		
			this.earth.ResetGrid();			
			_buffer.Clear();			
			_buffer.Put(new FinalTemperatureGrid());
		} catch (Exception e) {
			System.out.println("Simulation Error: Resetting planet");
		}
	}

	@Override
	public void run() {		

		_isRunning = true;
		if(hasInitiative()) {				
			RunSimulation();
		}
		else {
			idle();
		}
	}

	//Public Methods----------------------
	/**
	 * Start the simulation
	 */
	public void RunSimulation()
	{
		try 
		{
			while(_isRunning)
			{				
				Thread.sleep(1);
				Simulate();
			}
		} 
		catch (Exception e) 
		{
			System.out.println("Simulation Error: " + e.getMessage());
		}
	}	

	/**
	 * Perform one iteration of the simulation
	 * @throws Exception thrown when the coordinates of a cell are not valid
	 */
	public void Simulate() throws Exception
	{		
		if(!_isPaused) {				
			this.earth.ApplyHeatChange();		
			if(!this._buffer.isFull()) {
				this._buffer.Put(this.earth);
			}							
			this.earth.RotatePlanet(this._minutesPerRotation);			
			this.processingComplete();			
		}
	}
	//Private Methods---------------------
	@Override
	public void onProcessComplete(ComponentType origin) {
		try {
			this.Simulate();
		} catch (Exception e) {
			System.out.println("Simulation Error: " + e.getMessage());
		}

	}
}
