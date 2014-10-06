package SimulationEngine;
import java.util.Calendar;
import java.util.Dictionary;
import java.util.GregorianCalendar;
import java.lang.Math;

public class Planet 
{
	//Attributes--------------------------
	private Calendar IDLDateTime;
	private Dictionary<int [], GridCell> planetGrid;
	private int noonLongitude;
	
	//Accessors---------------------------
	public Calendar GetIDLDateTime(){ return this.IDLDateTime; }
	
	//Constructors------------------------
	public Planet() throws Exception
	{
		this.IDLDateTime = new GregorianCalendar(2000, 1, 1, 0, 0, 0);
		this.noonLongitude = 0;
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
	
	//Private Methods---------------------
	private void InitializeGrid() throws Exception
	{
		for(int i = -90; i <= 90; i+=Constants.gridLatitudeSize)
		{
			for(int j = -180; j <= 180; i+=Constants.gridLongitudeSize)
			{
				GridCell newCell = new GridCell(i, j);
				this.planetGrid.put(new int[] {newCell.GetLatitude(), newCell.GetLongitude()}, newCell);
			}
		}
	}
	
	private GridCell GetGridCell(int latitude, int longitude) throws Exception
	{
		if(latitude > 90 || latitude < -90)
			throw new Exception(Constants.invalidLatitudeMessage);
		
		if(longitude > 180 || latitude < -180)
			throw new Exception(Constants.invalidLongitudeMessage);
		
		return this.planetGrid.get(new int[]{latitude, longitude});
	}
	
	private void DiffuseHeat(GridCell cell)
	{
		double northWeight = this.CalculateHeatWeight(this.planetGrid.get(cell.GetNorthNeighborCoordinates()).GetOldTemp(), cell.GetNorthBaseLength(), cell);
		double southWeight = this.CalculateHeatWeight(this.planetGrid.get(cell.GetSouthNeighborCoordinates()).GetOldTemp(), cell.GetNorthBaseLength(), cell);
		double eastWeight = this.CalculateHeatWeight(this.planetGrid.get(cell.GetEastNeighborCoordinates()).GetOldTemp(), cell.GetNorthBaseLength(), cell);
		double westWeight = this.CalculateHeatWeight(this.planetGrid.get(cell.GetWestNeighborCoordinates()).GetOldTemp(), cell.GetNorthBaseLength(), cell);
		
		double newTemp = northWeight + southWeight + eastWeight + westWeight;
		cell.SetTemp(newTemp);
	}
	
	private void RadiateSun(GridCell cell)
	{
		double heatFactor = this.CalculateHeatFactor(cell);
		double tempChange = heatFactor * this.CalculateSolarTemperature(cell);
		cell.SetTemp(cell.GetTemp() + tempChange);
	}
	
	private void LoseHeatToSpace(GridCell cell)
	{
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
}
