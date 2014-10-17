package EarthSim.SimulationEngine;

public class SimulationEngine implements Runnable 
{
	//Attributes--------------------------
	private Planet earth;
	private int gridSize;
	private int runHours;
	private int minutesRotation;

	//Accessors---------------------------
	public int GetGridSize() { return this.gridSize; }
	public void SetGridSize(int value) { this.gridSize = value; }
	public int GetRunHours() { return this.runHours; }
	public void SetRunHours(int value) { this.runHours = value; }
	public int GetMinutesPerRotation() { return this.minutesRotation; }
	public void SetMinutesPerRotation(int value) { this.minutesRotation = value; }

	//Constructors------------------------
	public SimulationEngine(int cellSize, int hours, int minutesPerRotation)
	{
		this.gridSize = cellSize;
		this.runHours = hours;
		this.minutesRotation = minutesPerRotation;
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
				this.earth.RotatePlanet(this.minutesRotation);
			}
		} 
		catch (Exception e) 
		{
			e.printStackTrace();
		}
	}

	//Private Methods---------------------
}
