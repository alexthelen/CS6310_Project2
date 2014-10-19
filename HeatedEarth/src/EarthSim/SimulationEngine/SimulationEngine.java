package EarthSim.SimulationEngine;
import EarthSim.GUI.DataBuffer;
import EarthSim.ProcessingComponent;

public class SimulationEngine extends ProcessingComponent
{
	//Attributes--------------------------
	private Planet earth;
	private int _gridSize;
	private int _minutesPerRotation;
	private DataBuffer _buffer;
	private boolean produce;

	//Accessors---------------------------
	public int GetGridSize() { return this._gridSize; }
	public void SetGridSize(int value) { this._gridSize = value; }
	public int GetMinutesPerRotation() { return this._minutesPerRotation; }
	public void SetMinutesPerRotation(int value) { this._minutesPerRotation = value; }
	public boolean GetProduce(){ return this.produce; }
	public void SetProduce(boolean value){ this.produce = value; }

	//Constructors------------------------
	public SimulationEngine(DataBuffer buffer, int cellSize, int minutesPerRotation)
	{
		this._gridSize = cellSize;
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
	public void RunSimulation()
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
	
	//Private Methods---------------------

}
