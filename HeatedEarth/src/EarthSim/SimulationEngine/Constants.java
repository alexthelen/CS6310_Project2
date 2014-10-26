package EarthSim.SimulationEngine;

/**
 * Constants utilized in the simulation
 * 
 * @author Alexander Thelen
 * @version 1
 *
 */
public class Constants 
{
	public final static double degreesPerMinute = 0.25;	//Degrees
	public final static int tempChangeAtNoonAndMidnight = 5;	//Kelvin
	public final static double earthRadius = 6378;	//Kilometers
	public final static double distanceBetweenLatitudeLines = 111;	//Kilometers
	public final static double surfaceAreaOfEarth = 510072000;	//Kilometers
	public final static double solarEnergy = 1366;	//Watts per square meter
	public final static double emissivity = 1;	//Earth's average fraction of energy reradiated
	public final static double aldebo = 0;	//Earth's average fraction of energy reflected
	public final static double stefanBoltzmannConstant = 0.0000000567;
	public final static double tempSun = 278;	//Kelvin
	public final static String invalidLatitudeMessage = "Invalid Latitude Value. Latitude Values must be between -90 and 90 degrees.";
	public final static String invalidLongitudeMessage = "Invalid Longitude Value. Longitude Values must be between -180 and 180 degrees.";
}
