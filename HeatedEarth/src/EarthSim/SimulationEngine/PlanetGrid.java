package EarthSim.SimulationEngine;
import EarthSim.Presentation.earth.*;

public class PlanetGrid implements TemperatureGrid
{
	//Attributes--------------------------
	Planet _planet;
	
	//Constructors------------------------
	public PlanetGrid(Planet planet)
	{
		this._planet = planet;
	}
	
	//Public Methods----------------------
	@Override
	public double getTemperature(int x, int y) 
	{	
		GridCell cell = null;
		
		try 
		{
			cell = this._planet.GetGridCell(x, y);
		} 
		catch (Exception e) 
		{
			e.printStackTrace();
		}
		
		return cell.GetTemp();
	}

	@Override
	public int getLatitudeLength() 
	{
		return this._planet.GetGridSize();
	}

	@Override
	public int getLongitudeLength() 
	{
		return this._planet.GetGridSize();
	}

}
