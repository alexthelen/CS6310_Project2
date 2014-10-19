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
			cell = this._planet.planetGrid[x][y];
		} 
		catch (Exception e) 
		{
			e.printStackTrace();
		}
		if (cell == null) {
			return 0;
		}
		return cell.GetTemp();
	}

	@Override
	public int getLatitudeLength() 
	{
		return this._planet.getRows();
	}

	@Override
	public int getLongitudeLength() 
	{
		return this._planet.getColumns();
	}

}
