package EarthSim.SimulationEngine;
import EarthSim.GUI.DataBuffer;

public class SimulationEngine implements Runnable 
{
	//Attributes--------------------------
	private Planet earth;
	private int _gridSize;
	private int _hours;
	private int _minutesPerRotation;
	private DataBuffer _buffer;
	private boolean produce;

	//Accessors---------------------------
	public int GetGridSize() { return this._gridSize; }
	public void SetGridSize(int value) { this._gridSize = value; }
	public int GetRunHours() { return this._hours; }
	public void SetRunHours(int value) { this._hours = value; }
	public int GetMinutesPerRotation() { return this._minutesPerRotation; }
	public void SetMinutesPerRotation(int value) { this._minutesPerRotation = value; }

	//Constructors------------------------
	public SimulationEngine(DataBuffer buffer, int cellSize, int hours, int minutesPerRotation)
	{
		this._gridSize = cellSize;
		this._hours = hours;
		this._minutesPerRotation = minutesPerRotation;
		this.produce = false;
		
		try 
		{
			this.earth = new Planet(this._gridSize);
		} 
		catch (Exception e) 
		{
			e.printStackTrace();
		}
	}
	
	//Public Methods----------------------
	@Override
	public void run() 
	{
		
	}

	//Private Methods---------------------
	private void RunSimulation()
	{
		try 
		{
			while(produce)
			{
				this.earth.ApplyHeatChange();
				this._buffer.Put(new PlanetGrid(this.earth));
				this.earth.RotatePlanet(this._minutesPerRotation);
			}
		} 
		catch (Exception e) 
		{
			e.printStackTrace();
		}
	}
}
