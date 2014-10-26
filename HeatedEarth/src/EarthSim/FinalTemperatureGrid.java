/**
 * 
 */
package EarthSim;

import EarthSim.Presentation.earth.TemperatureGrid;

/**
 * Clean Temperature Grid used for reset
 * 
 * @author Pablo Gallastegui
 * @version 1
 *
 */
public class FinalTemperatureGrid implements TemperatureGrid {

	@Override
	public double getTemperature(int x, int y) {
		return 188;
	}

	@Override
	public int getLatitudeLength() {
		return 12;
	}

	@Override
	public int getLongitudeLength() {
		return 36;
	}

}
