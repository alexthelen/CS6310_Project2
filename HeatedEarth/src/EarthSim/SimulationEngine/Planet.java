package EarthSim.SimulationEngine;
import java.util.Calendar;
import java.util.GregorianCalendar;
import java.lang.Math;

import EarthSim.Presentation.earth.TemperatureGrid;

/**
 * A representation of the temperatures of the planet
 * 
 * @author Alexander Thelen
 * @version 1
 *
 */
public class Planet implements TemperatureGrid
{
	//Attributes--------------------------
	private Calendar IDLDateTime;
	private GridCell[][] planetGrid;
	private double noonLongitude;
	private int rows;
	private int columns;
	private int gridSize;
	
	//Accessors---------------------------
	public Calendar GetIDLDateTime(){ return this.IDLDateTime; }
	public int GetGridSize(){return this.gridSize; }
	
	//Constructors------------------------
	/**
	 * <CTOR>
	 * @param cellSize		an {@code int} specifying the degrees of amplitude of each cell
	 * @throws Exception	thrown when the parameter passed is not valid.
	 */
	public Planet(int cellSize) throws Exception
	{
		this.gridSize = cellSize;
		this.IDLDateTime = new GregorianCalendar(2000, 1, 1, 0, 0, 0);
		this.noonLongitude = 0;
		this.rows = 180 / this.gridSize;
		this.columns = 360 / this.gridSize;
		this.planetGrid = new GridCell[this.columns][this.rows];
		
		this.InitializeGrid();
	}	
	
	//Public Methods----------------------
	@Override
	public double getTemperature(int x, int y) 
	{	
		GridCell cell = null;
		
		try 
		{
			cell = this.planetGrid[x][y];
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
		return this.rows;
	}

	@Override
	public int getLongitudeLength() 
	{
		return this.columns;
	}
	
	/**
	 * Change the longitude receiving direct sunlight, based on the amount of simulation minutes passed
	 * @param minutes	an {@code int} specifying the amount of simulation minutes passed
	 */
	public void RotatePlanet(int minutes)
	{
		this.IDLDateTime.add(Calendar.MINUTE, minutes);
		if(this.noonLongitude > -180)
			this.noonLongitude -= (Constants.degreesPerMinute * minutes);
		else
			this.noonLongitude = 180;
		
	}
	
	/**
	 * Clean the grid to start a new simulation
	 */
	public void ResetGrid() {
		this.planetGrid = null;
		this.planetGrid = new GridCell[this.columns][this.rows];
		try {
			this.InitializeGrid();
		} catch (Exception e) {
			
		}		
	}
	
	/**
	 * Modify the temperature of each cell of the grid based on sun radiation, dissipation, and cooling
	 * @throws Exception	thrown when the coordinates specified for a cell are invalid.
	 */
	public void ApplyHeatChange() throws Exception
	{
		GridCell operationCell;
		double avgGridTemp = 0;
		double solarTemp;
		double coolTemp;
		double neighborTemp;
		double newTemp;
		
		for(int i = -90; i < 90; i += this.gridSize)
		{
			for(int j = -180; j < 180; j += this.gridSize)
			{
				operationCell = this.GetGridCell(i, j);
				operationCell.SetOldTemp(operationCell.GetTemp());
				avgGridTemp += operationCell.GetTemp();
			}
		}
		
		avgGridTemp /= (this.rows * this.columns);
		
		for(int i = -90; i < 90; i += this.gridSize)
		{
			for(int j = -180; j < 180; j += this.gridSize)
			{
				operationCell = this.GetGridCell(i, j);
				
				coolTemp = operationCell.GetOldTemp() + this.LoseHeatToSpace(operationCell, avgGridTemp);
				neighborTemp = this.DiffuseHeat(operationCell);			
				
				// calculate solar temp
				solarTemp = this.RadiateSun(operationCell);
				
				newTemp = (operationCell.GetOldTemp() + solarTemp + neighborTemp + coolTemp) / 4;
				operationCell.SetTemp(newTemp);
				
			}
		}
	}
	
	/**
	 * Retrieve a cell at a particular latitude and longitude
	 * @param latitude 		an {@code int} specifying the latitude of the cell to be retrieved
	 * @param longitude		an {@code int} specifying the longitude of the cell to be retrieved
	 * @return				the {@link GridCell} at the specified latitude and longitude
	 * @throws Exception	thrown when the coordinates specified for a cell are invalid
	 */
	public GridCell GetGridCell(int latitude, int longitude) throws Exception
	{
		if(!(latitude <= 90 && latitude >= -90))
			throw new Exception(Constants.invalidLatitudeMessage);
		
		if(!(longitude <= 180 && longitude >= -180))
			throw new Exception(Constants.invalidLongitudeMessage);
		
		int y = this.ConvertToArrayIndex(latitude, true);
		int x = this.ConvertToArrayIndex(longitude, false);
		return this.planetGrid[x][y];
	}
	
	//Private Methods---------------------
	/**
	 * Initialize the grid with default values
	 * @throws Exception thrown when the coordinates specified are not valid
	 */
	private void InitializeGrid() throws Exception
	{
		int latitude = -90;
		int longitude = -180;
		
		for(int j = 0; j < this.rows; j++)
		{
			for(int i = 0; i < this.columns; i++)
			{
				GridCell newCell = new GridCell(latitude, longitude, this.gridSize);
				this.planetGrid[i][j] = newCell;
				longitude += this.gridSize;
			}
			longitude = -180;
			latitude += this.gridSize;
		}
	}
	
	/**
	 * Calculate the value of the heat diffused by the cell passed as a parameter and its neighbor cells
	 * @param cell	the {@link GridCell} whose temperature change by diffusion is being calculated
	 * @return	a {@code double} containing the new temperature of the cell as affected by diffusion
	 */
	private double DiffuseHeat(GridCell cell)
	{
		int[] neighbor = cell.GetNorthNeighborCoordinates();
		neighbor[1] = this.ConvertToArrayIndex(neighbor[0], true);
		neighbor[0] = this.ConvertToArrayIndex(neighbor[1], false);
		double northWeight = this.CalculateHeatWeight(this.planetGrid[neighbor[0]][neighbor[1]].GetOldTemp(), cell.GetNorthBaseLength(), cell);
		
		neighbor = cell.GetSouthNeighborCoordinates();
		neighbor[1] = this.ConvertToArrayIndex(neighbor[0], true);
		neighbor[0] = this.ConvertToArrayIndex(neighbor[1], false);
		double southWeight = this.CalculateHeatWeight(this.planetGrid[neighbor[0]][neighbor[1]].GetOldTemp(), cell.GetSouthBaseLength(), cell);
		
		neighbor = cell.GetEastNeighborCoordinates();
		neighbor[1] = this.ConvertToArrayIndex(neighbor[0], true);
		neighbor[0] = this.ConvertToArrayIndex(neighbor[1], false);
		double eastWeight = this.CalculateHeatWeight(this.planetGrid[neighbor[0]][neighbor[1]].GetOldTemp(), cell.GetEastLength(), cell);
		
		neighbor = cell.GetWestNeighborCoordinates();
		neighbor[1] = this.ConvertToArrayIndex(neighbor[0], true);
		neighbor[0] = this.ConvertToArrayIndex(neighbor[1], false);
		double westWeight = this.CalculateHeatWeight(this.planetGrid[neighbor[0]][neighbor[1]].GetOldTemp(), cell.GetWestLength(), cell);
		
		return northWeight + southWeight + eastWeight + westWeight;
	}

	/**
	 * Calculate the value of the heat received by the sun in a cell
	 * @param cell	the {@link GridCell} whose temperature change by solar radiation is being calculated
	 * @return	a {@code double} containing the new temperature of the cell as affected by solar radiation
	 */
	private double RadiateSun(GridCell cell)
	{
		double longitudeFactor;
		int latitudeAbs;
		double latitudeFactor;
		double solarTemp = Math.pow((1 - Constants.aldebo) * Constants.solarEnergy / (4 * Constants.emissivity * Constants.stefanBoltzmannConstant), 1.0/4);
		double heatFactor = 0;
		double longitudeDifference = Math.abs(cell.GetLongitude() - this.noonLongitude);
		
		if(longitudeDifference < 90)
		{
			longitudeFactor = Math.cos(Math.toRadians(longitudeDifference));
			latitudeAbs = Math.abs(cell.GetLatitude());
			latitudeFactor = Math.cos(Math.toRadians(latitudeAbs));
			heatFactor = Math.abs(longitudeFactor * latitudeFactor);
		}
		
		return heatFactor * solarTemp;
	}

	/**
	 * Calculate the value of the heat lost by the cell passed as a parameter based on average planet temperature
	 * @param cell			the {@link GridCell} whose temperature change by heat loss is being calculated
	 * @param avgGridTemp	a {@code double} representing the average temperature of earth
	 * @return	a {@code double} containing the new temperature of the cell as affected by heat loss
	 */
	private double LoseHeatToSpace(GridCell cell, double avgGridTemp)
	{		
		double avgGridCellSize = (Constants.surfaceAreaOfEarth / (this.rows * this.columns)) * 1000000;	//Convert to square meters
		double relativeSizeFactor = cell.GetSurfaceArea() / avgGridCellSize;
		double relativeTempFactor = cell.GetTemp() / avgGridTemp;
		return -relativeSizeFactor * relativeTempFactor * Constants.tempSun;
	}
	
	/**
	 * Calculate the weight of the effects of temperature on a cell by a neighboring
	 * cell based on the lenght of the side shared
	 * @param temp		a {@code double} representing the temperature of the cell
	 * @param length	a {@code double} representing the lenght of the side shared by both cells
	 * @param cell		the {@link GridCell} for which the impact of the neighboring cell is being calculated
	 * @return			a {@code double} with the coefficient of impact of the neighboring cell
	 */
	private double CalculateHeatWeight(double temp, double length, GridCell cell)
	{
		return length / cell.CalculatePerimeter() * temp;
	}
	
	/**
	 * Convert latitude and longitude to grid indexes
	 * @param coordinate		an {@code int} containing the latitude or longitude value
	 * @param latitude			a {@code boolean} specifying if the parameter passed is the latitude
	 * @return					an {@code int} with the index of the array
	 */
	private int ConvertToArrayIndex(int coordinate, boolean latitude)
	{
		int total;
		
		if(latitude)
			total = 180;
		else
			total = 360;
		
		return (int)Math.floor(coordinate / this.gridSize) + (total / this.gridSize / 2);		
	}
}
