package EarthSim.SimulationEngine;
import java.util.concurrent.BlockingQueue;

import EarthSim.GUI.DataBuffer;
import EarthSim.Presentation.earth.TemperatureGrid;
import EarthSim.ProcessingComponent;

public class SimulationEngine extends ProcessingComponent
{
	//Attributes--------------------------
	private Planet earth;
	private int _gridSize;
	private int _minutesPerRotation;
	private DataBuffer<TemperatureGrid> _buffer;
	private boolean produce;

	//public BlockingQueue<TemperatureGrid> temperatureGrid;	
	public DataBuffer<TemperatureGrid> temperatureGrid;

	//Accessors---------------------------
	public int GetGridSize() { return this._gridSize; }
	public void SetGridSize(int value) { this._gridSize = value; }
	public int GetMinutesPerRotation() { return this._minutesPerRotation; }
	public void SetMinutesPerRotation(int value) { this._minutesPerRotation = value; }
	public boolean GetProduce(){ return this.produce; }
	public void SetProduce(boolean value){ this.produce = value; }

	//Constructors------------------------
	public SimulationEngine(DataBuffer buffer, int cellSize, int minutesPerRotation, boolean dedicatedThread)
	{
		this._gridSize = cellSize;
		this._minutesPerRotation = minutesPerRotation;
		this.produce = false;
		threadName = "SimulationThread";
		setRunningInOwnThread(dedicatedThread);

		try 
		{
			this.earth = new Planet(this._gridSize);
		} 
		catch (Exception e) 
		{
			e.printStackTrace();
		}
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

		produce = true;
		RunSimulation();
	}

	//Public Methods----------------------
	public void RunSimulation()
	{
		try 
		{
			while(produce)
			{
				this.RunSimulationOnce();
			}
		} 
		catch (Exception e) 
		{
			e.printStackTrace();
		}
	}

	public void RunSimulationOnce() throws Exception
	{
		this.earth.ApplyHeatChange();
		//		this._buffer.Put(new PlanetGrid(this.earth));
		PlanetGrid planetGrid = new PlanetGrid(this.earth);
		
		while(this.temperatureGrid.Put(planetGrid)) {
			//System.out.println("Simulation: Waiting for buffer");
		}
		
		this.processingComplete();
		this.earth.RotatePlanet(this._minutesPerRotation);
	}
	//Private Methods---------------------

}
