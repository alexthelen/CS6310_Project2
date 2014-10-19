package EarthSim.SimulationEngine;
import java.util.Calendar;
import java.util.GregorianCalendar;
import java.lang.Math;

public class Planet 
{
	//Attributes--------------------------
	private Calendar IDLDateTime;
	private GridCell[][] planetGrid;
	private int noonLongitude;
	private int rows;
	private int columns;
	private int gridSize;
	
	//Accessors---------------------------
	public Calendar GetIDLDateTime(){ return this.IDLDateTime; }
	public int GetGridSize(){return this.gridSize; }
	
	/**
	 * @return the rows
	 */
	public int getRows() {
		return rows;
	}
	/**
	 * @return the columns
	 */
	public int getColumns() {
		return columns;
	}
	//Constructors------------------------
	public Planet(int cellSize) throws Exception
	{
		this.gridSize = cellSize;
		this.IDLDateTime = new GregorianCalendar(2000, 1, 1, 0, 0, 0);
		this.noonLongitude = 0;
		this.rows = 180 / this.gridSize;
		this.columns = 360 / this.gridSize;
		this.planetGrid = new GridCell[this.rows][this.columns];
		
		this.InitializeGrid();
	}
	
	//Public Methods----------------------
	public void RotatePlanet(int minutes)
	{
		this.IDLDateTime.add(Calendar.MINUTE, minutes);
		if(this.noonLongitude < 180)
			this.noonLongitude += (Constants.degreesPerMinute * minutes);
		else
			this.noonLongitude = -180;
		
	}
	
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
				
				solarTemp = operationCell.GetOldTemp() + (this.RadiateSun(operationCell) / avgGridTemp);
				coolTemp = operationCell.GetOldTemp() + this.LoseHeatToSpace(operationCell, avgGridTemp);
				neighborTemp = this.DiffuseHeat(operationCell);			
				
				newTemp = (coolTemp + solarTemp + neighborTemp) / 3;
				operationCell.SetTemp(newTemp);
				
				System.out.println(newTemp);
			}
		}
	}
	
	public GridCell GetGridCell(int latitude, int longitude) throws Exception
	{
		if(!(latitude <= 90 && latitude >= -90))
			throw new Exception(Constants.invalidLatitudeMessage);
		
		if(!(longitude <= 180 && latitude >= -180))
			throw new Exception(Constants.invalidLongitudeMessage);
		
		int x = this.ConvertToArrayIndex(latitude, true);
		int y = this.ConvertToArrayIndex(longitude, false);
		return this.planetGrid[x][y];
	}
	
	//Private Methods---------------------
	private void InitializeGrid() throws Exception
	{
		int latitude = -90;
		int longitude = -180;
		
		for(int i = 0; i < this.rows; i++)
		{
			for(int j = 0; j < this.columns; j++)
			{
				GridCell newCell = new GridCell(latitude, longitude, this.gridSize);
				this.planetGrid[i][j] = newCell;
				longitude += this.gridSize;
			}
			longitude = -180;
			latitude += this.gridSize;
		}
	}
	
	private double DiffuseHeat(GridCell cell)
	{
		int[] neighbor = cell.GetNorthNeighborCoordinates();
		neighbor[0] = this.ConvertToArrayIndex(neighbor[0], true);
		neighbor[1] = this.ConvertToArrayIndex(neighbor[1], false);
		double northWeight = this.CalculateHeatWeight(this.planetGrid[neighbor[0]][neighbor[1]].GetOldTemp(), cell.GetNorthBaseLength(), cell);
		
		neighbor = cell.GetSouthNeighborCoordinates();
		neighbor[0] = this.ConvertToArrayIndex(neighbor[0], true);
		neighbor[1] = this.ConvertToArrayIndex(neighbor[1], false);
		double southWeight = this.CalculateHeatWeight(this.planetGrid[neighbor[0]][neighbor[1]].GetOldTemp(), cell.GetSouthBaseLength(), cell);
		
		neighbor = cell.GetEastNeighborCoordinates();
		neighbor[0] = this.ConvertToArrayIndex(neighbor[0], true);
		neighbor[1] = this.ConvertToArrayIndex(neighbor[1], false);
		double eastWeight = this.CalculateHeatWeight(this.planetGrid[neighbor[0]][neighbor[1]].GetOldTemp(), cell.GetEastLength(), cell);
		
		neighbor = cell.GetWestNeighborCoordinates();
		neighbor[0] = this.ConvertToArrayIndex(neighbor[0], true);
		neighbor[1] = this.ConvertToArrayIndex(neighbor[1], false);
		double westWeight = this.CalculateHeatWeight(this.planetGrid[neighbor[0]][neighbor[1]].GetOldTemp(), cell.GetWestLength(), cell);
		
		return northWeight + southWeight + eastWeight + westWeight;
	}
	
	private double RadiateSun(GridCell cell)
	{
		double longitudeFactor;
		int latitudeAbs;
		double latitudeFactor;
		double solarTemp = Math.pow((1 - Constants.aldebo) * Constants.solarEnergy / (4 * Constants.emissivity * Constants.stefanBoltzmannConstant), 1.0/4);
		double heatFactor = 0;
		int longitudeDifference = Math.abs(cell.GetLongitude() - this.noonLongitude);
		
		if(longitudeDifference < 90)
		{
			longitudeFactor = Math.cos(Math.toRadians(longitudeDifference));
			latitudeAbs = Math.abs(cell.GetLatitude());
			latitudeFactor = Math.cos(Math.toRadians(latitudeAbs));
			heatFactor = Math.abs(longitudeFactor * latitudeFactor);
		}
		
		return heatFactor * solarTemp;
	}
	
	private double LoseHeatToSpace(GridCell cell, double avgGridTemp)
	{		
		double avgGridCellSize = (Constants.surfaceAreaOfEarth / (this.rows * this.columns)) * 1000000;	//Convert to square meters
		double relativeSizeFactor = cell.GetSurfaceArea() / avgGridCellSize;
		double relativeTempFactor = cell.GetTemp() / avgGridTemp;
		return -relativeSizeFactor * relativeTempFactor * Constants.tempSun;
	}
	
	private double CalculateHeatWeight(double temp, double length, GridCell cell)
	{
		return length / cell.CalculatePerimeter() * temp;
	}
	
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
