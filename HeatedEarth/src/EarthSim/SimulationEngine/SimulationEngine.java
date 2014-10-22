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
	public SimulationEngine(DataBuffer<TemperatureGrid> buffer, int cellSize, int minutesPerRotation, boolean dedicatedThread)
	{
		_componentType = ComponentType.Simulation;
		this._buffer = buffer;
		this._gridSize = cellSize;
		this._minutesPerRotation = minutesPerRotation;
		this._isRunning = false;
		threadName = "SimulationThread";
		setRunningInOwnThread(dedicatedThread);

		try 
		{
			this.earth = new Planet(this._gridSize);
		} 
		catch (Exception e) 
		{
			System.out.println("Simulation Error: " + e.getMessage());
		}
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
			PlanetGrid planetGrid = new PlanetGrid(this.earth);		
			if(this._buffer.isFull()) {
				System.out.println("Simulation: Buffer full");
			}
			else {
				this._buffer.Put(planetGrid);
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
