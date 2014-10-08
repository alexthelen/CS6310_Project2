package EarthSim.SimulationEngine;

public class GridCell 
{
	//Attributes--------------------------
	private int latitude;
	private int longitude;
	private double temp;
	private double oldTemp;
	private double surfaceArea;
	private double northBaseLength;	
	private double southBaseLength;		
	private double eastWestLength;
	private double height;
	
	//Accessors---------------------------
	public int GetLatitude(){ return this.latitude; }
	public int GetLongitude(){ return this.longitude; }
	public double GetTemp(){ return this.temp; }
	public void SetTemp(double value){ this.temp = value; }
	public double GetOldTemp(){ return this.oldTemp; }
	public void SetOldTemp(double value){ this.oldTemp = value; }
	public double GetSurfaceArea(){ return this.surfaceArea; }
	public double GetNorthBaseLength(){ return this.northBaseLength; }
	public double GetSouthBaseLength(){ return this.southBaseLength; }
	public double GetHeight(){ return this.height; }
	public double GetEastLength(){ return this.eastWestLength; }
	public double GetWestLength(){ return this.eastWestLength; }
	
	//Constructors------------------------
	public GridCell(int latitudeValue, int longitudeValue) throws Exception
	{
		if(latitudeValue <= 90 && latitudeValue >= -90)
			this.latitude = latitudeValue;
		else
			throw new Exception(Constants.invalidLatitudeMessage);
		
		if(longitudeValue <= 180 && longitudeValue >= -180)
			this.longitude = longitudeValue;
		else
			throw new Exception(Constants.invalidLongitudeMessage);
	
		this.temp = 288;
		this.height = this.CalculateHeight();
		
		if(this.latitude >= 0)
		{
			this.southBaseLength = this.CalculateBase(this.latitude);
			this.northBaseLength = this.CalculateBase(this.latitude + Constants.gridLatitudeSize);
		}
		else
		{
			this.southBaseLength = this.CalculateBase(this.latitude - Constants.gridLatitudeSize);
			this.northBaseLength = this.CalculateBase(this.latitude);
		}
		
		this.eastWestLength = this.CalculateLegs();
		this.surfaceArea = this.CalculateSurfaceArea();
	}
	
	//Public Methods----------------------
	public int[] GetNorthNeighborCoordinates()
	{
		int northLatitude;
		int northLongitude;
		
		if(this.latitude + Constants.gridLatitudeSize == 90)
		{
			northLatitude = this.latitude;
			
			if(this.longitude >= 0)
				northLongitude = this.longitude - 180;
			else
				northLongitude = this.longitude + 180;

		}
		else
		{
			northLatitude = this.latitude + Constants.gridLatitudeSize;
			northLongitude = this.longitude;
		}
		
		return new int[] {northLatitude, northLongitude};
	}
	
	public int[] GetSouthNeighborCoordinates()
	{
		int southLatitude;
		int southLongitude;
		
		if(this.latitude - Constants.gridLatitudeSize == -90)
		{
			southLatitude = this.latitude;
			
			if(this.longitude >= 0)
				southLongitude = this.longitude - 180;
			else
				southLongitude = this.longitude + 180;

		}
		else
		{
			southLatitude = this.latitude - Constants.gridLatitudeSize;
			southLongitude = this.longitude;
		}
		
		return new int[] {southLatitude, southLongitude};
	}
	
	public int[] GetEastNeighborCoordinates()
	{
		int eastLatitude= this.latitude;
		int eastLongitude;
		
		if(this.longitude + Constants.gridLongitudeSize == 180)
			eastLongitude = -180;
		else
			eastLongitude = this.longitude + Constants.gridLongitudeSize;
		
		return new int[] {eastLatitude, eastLongitude};
	}
	
	public int[] GetWestNeighborCoordinates()
	{
		int westLatitude= this.latitude;
		int westLongitude;
		
		if(this.longitude - Constants.gridLongitudeSize == -180)
			westLongitude = 180;
		else
			westLongitude = this.longitude - Constants.gridLongitudeSize;
		
		return new int[] {westLatitude, westLongitude};
	}
	
	public double CalculatePerimeter()
	{
		return this.northBaseLength + this.southBaseLength + 2 * this.eastWestLength;
	}
	
	//Private Methods---------------------
	private double CalculateSurfaceArea()
	{	
		return this.CalculateHeight() * (this.southBaseLength + this.northBaseLength) / 2;
	}
	
	private double CalculateBase(int lat)
	{
		lat = Math.abs(lat);
		double radians = lat * Math.PI / 180;
		double circumference = 2 * Math.PI * Constants.earthRadius * Math.cos(radians);
		return circumference / (360 / Constants.gridLongitudeSize);
	}
	
	private double CalculateHeight()
	{
		return Constants.distanceBetweenLatitudeLines * Constants.gridLatitudeSize;
	}
	
	private double CalculateLegs()
	{
		double x;
		
		if(this.northBaseLength >= this.southBaseLength)
			x = (this.northBaseLength - this.southBaseLength) / 2;
		else
			x = (this.southBaseLength - this.northBaseLength) / 2;
		
		return Math.sqrt(Math.pow(x, 2) + Math.pow(this.height, 2));
	}
	
}
