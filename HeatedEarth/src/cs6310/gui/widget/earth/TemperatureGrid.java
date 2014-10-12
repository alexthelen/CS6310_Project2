package cs6310.gui.widget.earth;

/**
 * Implement this interface to allow for the retrieval of information 
 * pertaining to a grid of temperatures. The top left corner of the grid is 
 * considered to have the coordinates (0, 0); the bottom right corner will have
 * coordinates (x, y) in an <code>x by y</code> grid with x columns and y rows.
 * 
 * @author Andrew Bernard
 * @author Pablo Gallastegui
 * 
 * @version 1.1
 */
public interface TemperatureGrid {
  
  /**
   * Gets the temperature in the cell specified by the x and y coordinates.
   */
  public double getTemperature(int x, int y);
  
  /**
   * Gets the length of the vertical dimension of the grid.
   */
  public int getLatitudeLength();
  
  /**
   * Gets the length of the vertical dimension of the grid.
   */
  public int getLongitudeLength();
  
  /**
   * Moved responsibility to calculate the height in pixels of the cell
   * to {@link EarthGridDisplay}
   * 
   * public float getCellHeight(int x, int y);
   */

}
