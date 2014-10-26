package EarthSim.SimulationEngine;

/**
 * Representation of a portion of the earth's surface
 * 
 * @author Alexander Thelen
 * @version 1
 *
 */
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
	private int gridSize;
	
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
	/**
	 * <CTOR>
	 * @param latitudeValue		an {@code int} with the latitude of the cell
	 * @param longitudeValue	an {@code int} with the longitude of the cell
	 * @param cellSize			an {@code int} with the degrees of amplitude of the cell
	 * @throws Exception		When parameters are not valid
	 */
	public GridCell(int latitudeValue, int longitudeValue, int cellSize) throws Exception
	{
		this.gridSize = cellSize;
		
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
		
		this.southBaseLength = this.CalculateBase(this.latitude);
		this.northBaseLength = this.CalculateBase(this.latitude + this.gridSize);
		
		this.eastWestLength = this.CalculateLegs();
		this.surfaceArea = this.CalculateSurfaceArea();
	}
	
	//Public Methods----------------------
	/**
	 * @return an array of 2 {@code int} values specifying the coordinates of the cell neighboring the north bound of the cell.
	 */
	public int[] GetNorthNeighborCoordinates()
	{
		int northLatitude;
		int northLongitude;
		
		if(this.latitude + this.gridSize == 90)
		{
			northLatitude = this.latitude;
			
			if(this.longitude >= 0)
				northLongitude = this.longitude - 180;
			else
				northLongitude = this.longitude + 180;

		}
		else
		{
			northLatitude = this.latitude + this.gridSize;
			northLongitude = this.longitude;
		}
		
		return new int[] {northLatitude, northLongitude};
	}

	/**
	 * @return an array of 2 {@code int} values specifying the coordinates of the cell neighboring the south bound of the cell.
	 */
	public int[] GetSouthNeighborCoordinates()
	{
		int southLatitude;
		int southLongitude;
		
		if(this.latitude - this.gridSize < -90)
		{
			southLatitude = this.latitude;
			
			if(this.longitude >= 0)
				southLongitude = this.longitude - 180;
			else
				southLongitude = this.longitude + 180;

		}
		else
		{
			southLatitude = this.latitude - this.gridSize;
			southLongitude = this.longitude;
		}
		
		return new int[] {southLatitude, southLongitude};
	}

	/**
	 * @return an array of 2 {@code int} values specifying the coordinates of the cell neighboring the east bound of the cell.
	 */
	public int[] GetEastNeighborCoordinates()
	{
		int eastLatitude= this.latitude;
		int eastLongitude;
		
		if(this.longitude + this.gridSize >= 180)
			eastLongitude = -180;
		else
			eastLongitude = this.longitude + this.gridSize;
		
		return new int[] {eastLatitude, eastLongitude};
	}

	/**
	 * @return an array of 2 {@code int} values specifying the coordinates of the cell neighboring the west bound of the cell.
	 */
	public int[] GetWestNeighborCoordinates()
	{
		int westLatitude= this.latitude;
		int westLongitude;
		
		if(this.longitude - this.gridSize < -180)
			westLongitude = 170;
		else
			westLongitude = this.longitude - this.gridSize;
		
		return new int[] {westLatitude, westLongitude};
	}

	/**
	 * @return a {@code double} specifying the perimeter of the cell
	 */
	public double CalculatePerimeter()
	{
		return this.northBaseLength + this.southBaseLength + 2 * this.eastWestLength;
	}
	
	//Private Methods---------------------
	/**
	 * @return a {@code double} specifying the area of the cell
	 */
	private double CalculateSurfaceArea()
	{	
		return this.CalculateHeight() * (this.southBaseLength + this.northBaseLength) / 2;
	}

	/**
	 * @param lat	an {@code int} containing the latitude of the cell
	 * @return a {@code double} specifying the length of the base of the cell
	 */
	private double CalculateBase(int lat)
	{
		lat = Math.abs(lat);
		double radians = lat * Math.PI / 180;
		double circumference = 2 * Math.PI * Constants.earthRadius * Math.cos(radians);
		return circumference / (360 / this.gridSize);
	}

	/**
	 * @return a {@code double} specifying the height of the cell
	 */
	private double CalculateHeight()
	{
		return Constants.distanceBetweenLatitudeLines * this.gridSize;
	}

	/**
	 * @return a {@code double} specifying the legs of the cell
	 */
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
