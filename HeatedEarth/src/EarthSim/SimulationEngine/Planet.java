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
	
	//Accessors---------------------------
	public Calendar GetIDLDateTime(){ return this.IDLDateTime; }
	
	//Constructors------------------------
	public Planet() throws Exception
	{
		this.IDLDateTime = new GregorianCalendar(2000, 1, 1, 0, 0, 0);
		this.noonLongitude = 0;
		this.planetGrid = new GridCell[this.CalculatePlanetGridX()][this.CalculatePlanetGridY()];
		this.InitializeGrid();
	}
	
	//Public Methods----------------------
	public void RotatePlanet()
	{
		this.IDLDateTime.add(Calendar.HOUR, 1);
		this.noonLongitude += 15;
	}
	
	public void ApplyHeatChange() throws Exception
	{
		GridCell operationCell;
		
		for(int i = -90; i <= 90; i+=Constants.gridLatitudeSize)
		{
			for(int j = -180; j <= 180; j+=Constants.gridLongitudeSize)
			{
				operationCell = this.GetGridCell(i, j);
				operationCell.SetOldTemp(operationCell.GetTemp());
			}
		}
		
		for(int i = -90; i <= 90; i+=Constants.gridLatitudeSize)
		{
			for(int j = -180; j <= 180; j+=Constants.gridLongitudeSize)
			{
				operationCell = this.GetGridCell(i, j);
				
				this.RadiateSun(operationCell);
				this.LoseHeatToSpace(operationCell);
				this.DiffuseHeat(operationCell);
			}
		}
	}
	
	public GridCell GetGridCell(int latitude, int longitude) throws Exception
	{
		if(!(latitude <= 90 && latitude >= -90))
			throw new Exception(Constants.invalidLatitudeMessage);
		
		if(!(longitude <= 180 && latitude >= -180))
			throw new Exception(Constants.invalidLongitudeMessage);
		
		int x = this.ConvertToArrayIndex(latitude, 180);
		int y = this.ConvertToArrayIndex(longitude, 360);
		return this.planetGrid[x][y];
	}
	
	//Private Methods---------------------
	private void InitializeGrid() throws Exception
	{
		int latitude = -90;
		int longitude = -180;
		int gridLength = this.CalculatePlanetGridX();
		int gridHeight = this.CalculatePlanetGridY();
		
		for(int i = 0; i < gridLength; i++)
		{
			for(int j = 0; j < gridHeight; j++)
			{
				GridCell newCell = new GridCell(latitude, longitude);
				this.planetGrid[i][j] = newCell;
				longitude += Constants.gridLongitudeSize;
			}
			longitude = -180;
			latitude += Constants.gridLatitudeSize;
		}
	}
	
	private void DiffuseHeat(GridCell cell)
	{
		int[] neighbor = cell.GetNorthNeighborCoordinates();
		neighbor[0] = this.ConvertToArrayIndex(neighbor[0], 180);
		neighbor[1] = this.ConvertToArrayIndex(neighbor[1], 360);
		double northWeight = this.CalculateHeatWeight(this.planetGrid[neighbor[0]][neighbor[1]].GetOldTemp(), cell.GetNorthBaseLength(), cell);
		
		neighbor = cell.GetSouthNeighborCoordinates();
		neighbor[0] = this.ConvertToArrayIndex(neighbor[0], 180);
		neighbor[1] = this.ConvertToArrayIndex(neighbor[1], 360);
		double southWeight = this.CalculateHeatWeight(this.planetGrid[neighbor[0]][neighbor[1]].GetOldTemp(), cell.GetNorthBaseLength(), cell);
		
		neighbor = cell.GetEastNeighborCoordinates();
		neighbor[0] = this.ConvertToArrayIndex(neighbor[0], 180);
		neighbor[1] = this.ConvertToArrayIndex(neighbor[1], 360);
		double eastWeight = this.CalculateHeatWeight(this.planetGrid[neighbor[0]][neighbor[1]].GetOldTemp(), cell.GetNorthBaseLength(), cell);
		
		neighbor = cell.GetWestNeighborCoordinates();
		neighbor[0] = this.ConvertToArrayIndex(neighbor[0], 180);
		neighbor[1] = this.ConvertToArrayIndex(neighbor[1], 360);
		double westWeight = this.CalculateHeatWeight(this.planetGrid[neighbor[0]][neighbor[1]].GetOldTemp(), cell.GetNorthBaseLength(), cell);
		
		double newTemp = northWeight + southWeight + eastWeight + westWeight;
		cell.SetTemp(newTemp);
	}
	
	private void RadiateSun(GridCell cell)
	{
		//Check that time of day is factored in. If the cell does not face the sun it does not receive any heat
		double heatFactor = this.CalculateHeatFactor(cell);
		double tempChange = heatFactor * this.CalculateSolarTemperature(cell);
		cell.SetTemp(cell.GetTemp() + tempChange);
	}
	
	private void LoseHeatToSpace(GridCell cell)
	{
		//Check that time of day is factored in. If the cell faces the sun it does not lose heat
		double heatFactor = this.CalculateHeatFactor(cell);
		double tempChange = heatFactor * this.CalculateSolarTemperature(cell);
		cell.SetTemp(cell.GetTemp() - tempChange);
	}
	
	private double CalculateHeatFactor(GridCell cell)
	{
		int longitudeDifference = Math.abs(cell.GetLongitude() - this.noonLongitude);
		double longitudeFactor = Math.cos(longitudeDifference);
		int latitudeAbs = Math.abs(cell.GetLatitude());
		double latitudeFactor = Math.cos(latitudeAbs);
		
		return longitudeFactor * latitudeFactor;
	}
	
	private double CalculateSolarTemperature(GridCell cell)
	{
		double sa = cell.GetSurfaceArea() * 1000000;	//surface area stored in square km we will do calculation with meters
		double temp = Math.pow(Constants.solarEnergy / sa * Constants.stefanBoltzmannConstant, -4);
		return temp;
	}
	
	private double CalculateHeatWeight(double temp, double length, GridCell cell)
	{
		return length / cell.CalculatePerimeter() * temp;
	}
	
	private int CalculatePlanetGridX()
	{
		return 180 / Constants.gridLatitudeSize;
	}
	
	private int CalculatePlanetGridY()
	{
		return 360 / Constants.gridLongitudeSize;
	}
	
	private int ConvertToArrayIndex(int coordinate, int total)
	{
		return (int)Math.floor(coordinate / Constants.gridLatitudeSize) + (total / Constants.gridLatitudeSize / 2);
	}
}
