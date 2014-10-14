package EarthSim.SimulationEngine;

public class SimulationEngine implements Runnable 
{
	//Attributes--------------------------
	private Planet earth;
	private int gridSize;
	private int runHours;

	//Accessors---------------------------
	public int GetGridSize() { return this.gridSize; }
	public void SetGridSize(int value) { this.gridSize = value; }
	public int GetRunHours() { return this.runHours; }
	public void SetRunHours(int value) { this.runHours = value; }

	//Constructors------------------------
	public SimulationEngine(int cellSize, int hours)
	{
		this.gridSize = cellSize;
		this.runHours = hours;
	}
	
	//Public Methods----------------------
	@Override
	public void run() 
	{
		try 
		{
			this.earth = new Planet(this.gridSize);
			for(int i = 0; i <= this.runHours; i++)
			{
				this.earth.ApplyHeatChange();
				this.earth.RotatePlanet();
			}
		} 
		catch (Exception e) 
		{
			e.printStackTrace();
		}
	}

	//Private Methods---------------------
}
