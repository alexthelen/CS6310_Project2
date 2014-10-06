package SimulationEngine;

public class SimulationEngine implements Runnable 
{
	@Override
	public void run() 
	{
		// TODO Auto-generated method stub
		
	}

	//Attributes--------------------------
	private Planet earth;

	//Accessors---------------------------

	//Constructors------------------------
	public SimulationEngine()
	{
		
	}
	
	//Public Methods----------------------
	public void RunSimulation(int hours)
	{
		try 
		{
			this.earth = new Planet();
			for(int i = 0; i <= hours; i++)
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
