package SimulationEngine;

public class Constants 
{
	public final static int rotationPerHour = 15;	//Degrees
	public final static int gridLatitudeSize = 1;	//Degrees
	public final static int gridLongitudeSize = 1;	//Degrees
	public final static int tempChangeAtNoonAndMidnight = 5;	//Kelvin
	public final static double earthRadius = 6378;	//Kilometers
	public final static double distanceBetweenLatitudeLines = 111;	//Kilometers
	public final static double surfaceAreaOfEarth = 510100000;	//Kilometers
	public final static double solarEnergy = 1366;	//Watts per square meter
	public final static double stefanBoltzmannConstant = 0.0000000567;
	public final static String invalidLatitudeMessage = "Invalid Latitude Value. Latitude Values must be between -90 and 90 degrees.";
	public final static String invalidLongitudeMessage = "Invalid Longitude Value. Longitude Values must be between -180 and 180 degrees.";
}
