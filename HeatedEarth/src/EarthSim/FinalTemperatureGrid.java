/**
 * 
 */
package EarthSim;

import java.util.Random;

import cs6310.gui.widget.earth.TemperatureGrid;

/**
 * @author pablo
 *
 */
public class FinalTemperatureGrid implements TemperatureGrid {

	@Override
	public double getTemperature(int x, int y) {
		// TODO Auto-generated method stub
		return (new Random()).nextInt(100);
	}

	@Override
	public float getCellHeight(int x, int y) {
		// TODO Auto-generated method stub
		switch (y) {
		case 0:
		case 11:
			return 7;
		case 1:
		case 10:
			return 20;
		case 2:
		case 9:
			return 31;
		case 3:
		case 8:
			return 41;
		case 4:
		case 7:
			return 48;
		case 5:
		case 6:
			return 51;
		}
		
		return 10;
	}

}
