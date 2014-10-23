package EarthSim.SimulationEngine;
import EarthSim.ComponentType;
import EarthSim.FinalTemperatureGrid;
import EarthSim.ProcessingComponent;
import EarthSim.ProcessingComponentListener;
import EarthSim.GUI.DataBuffer;
import EarthSim.Presentation.earth.TemperatureGrid;

public class SimulationEngine extends ProcessingComponent implements ProcessingComponentListener
{
	//Attributes--------------------------
	private Planet earth;
	private int _gridSize;
	private int _minutesPerRotation;
	private DataBuffer<TemperatureGrid> _buffer;
	private boolean _isRunning;
	private boolean _isPaused = false;

	//Accessors---------------------------
	public int GetGridSize() { return this._gridSize; }
	public void SetGridSize(int value) { this._gridSize = value; }
	public int GetMinutesPerRotation() { return this._minutesPerRotation; }
	public void SetMinutesPerRotation(int value) { this._minutesPerRotation = value; }	

	//Constructors------------------------
	public SimulationEngine(DataBuffer<TemperatureGrid> buffer, int gridSize, int minutesPerRotation, boolean dedicatedThread)
	{
		_componentType = ComponentType.Simulation;
		this._buffer = buffer;
		this._minutesPerRotation = minutesPerRotation;
		this._isRunning = false;
		threadName = "SimulationThread";
		setRunningInOwnThread(dedicatedThread);
		
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

	public void Simulate() throws Exception
	{		
		if(!_isPaused) {				
			this.earth.ApplyHeatChange();		
			if(this._buffer.isFull()) {
				System.out.println("Simulation: Buffer full");
			}
			else {
				this._buffer.Put(this.earth);
				System.out.println("Simulation: Pushing to buffer");
			}							
			this.earth.RotatePlanet(this._minutesPerRotation);			
			System.out.println("Simulation: Processing Complete");
			this.processingComplete();			
		}
	}
	//Private Methods---------------------
	@Override
	public void onProcessComplete(ComponentType origin) {
		System.out.println("Simulation: No Initiative");
		try {
			this.Simulate();
		} catch (Exception e) {
			System.out.println("Simulation Error: " + e.getMessage());
		}

	}
}
