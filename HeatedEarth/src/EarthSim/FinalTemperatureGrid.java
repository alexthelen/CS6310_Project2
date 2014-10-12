/**
 * 
 */
package EarthSim;

import EarthSim.Presentation.earth.TemperatureGrid;

/**
 * @author pablo
 *
 */
public class FinalTemperatureGrid implements TemperatureGrid {

	@Override
	public double getTemperature(int x, int y) {
		return (y * x);
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
