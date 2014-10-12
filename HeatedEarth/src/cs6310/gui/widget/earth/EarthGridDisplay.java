package cs6310.gui.widget.earth;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.image.BufferedImage;
import java.awt.image.RescaleOp;

import javax.swing.JPanel;

/**
 * Use this class to display an image of the earth with a grid drawn on top. All the methods that could
 * be used to update the grid are given package level access - these methods should be interacted with 
 * from the {@link EarthPanel}.
 * 
 * 
 * 
 * @author Andrew Bernard
 * @author Pablo Gallastegui
 * 
 * @version 1.1
 * 
 */
public class EarthGridDisplay extends JPanel {
  private static final long serialVersionUID = -1108120968981962997L;
  private static final float OPACITY = 0.75f;  
  private static final int DEFAULT_CELL_TEMP = 15; //degrees in celsius
  private TemperatureColorPicker colorPicker = TemperatureColorPicker.getInstance();
  
  /**
   * Deprecated properties storing display data originated from
   * the previous TemperatureGrid interface
   */
  @SuppressWarnings("unused")
  @Deprecated
  private int degreeSeparation;
  @SuppressWarnings("unused")
  @Deprecated
  private int pixelsPerCellX; //number of pixels per latitudal division
  @SuppressWarnings("unused")
  @Deprecated
  private int pixelsPerCellY; //number of pixels per longitudal division
  @SuppressWarnings("unused")
  @Deprecated
  private int numCellsX;
  @SuppressWarnings("unused")
  @Deprecated
  private int numCellsY;

  
  private BufferedImage imgTransparent;
  private BufferedImage earthImage;
  private float[] scales = { 1f, 1f, 1f, OPACITY }; //last index controls the transparency
  private float[] offsets = new float[4];
  private int imgWidth; // in pixels
  private int imgHeight; // in pixels
  private int radius; // in pixels
  private boolean paintInitialColors = true;
  private TemperatureGrid grid;
  
  /**
 * @return the radius
 */
public int getRadius() {
	return radius;
}

/**
 * <CTOR>
   * Constructs a display grid with a default grid spacing.
   * 
   * @param defaultGridPacing in degrees
   */
  EarthGridDisplay(int defaultGridPacing) {
    earthImage = new EarthImage().getBufferedImage();    
    setGranularity(defaultGridPacing);
    setIgnoreRepaint(true);
    radius = imgHeight/2;
  }
  
  /**
   * Sets the granularity of the grid.
   * 
   * @param degreeSeparation the latitude and longitude degree separations 
   * between the cells in the grid
   */
  void setGranularity(int degreeSeparation) {
    imgWidth = earthImage.getWidth();
    imgHeight = earthImage.getHeight();
    
    //create an image capable of transparency; then draw our image into it
    imgTransparent = new BufferedImage(imgWidth, imgHeight, BufferedImage.TYPE_INT_ARGB);
    Graphics g = imgTransparent.getGraphics();
    g.drawImage(earthImage, 0, 0, imgWidth, imgHeight, null);
  }
  
  public void paint(Graphics g) {
    //the order in which these are called does matter
    if(paintInitialColors)
      initCellColors(g);
    else 
      fillCellColors(g);
    drawTransparentImage(g);
    drawGrid(g);
  }
  
  private void initCellColors(Graphics g) {
    g.setColor(colorPicker.getColor(DEFAULT_CELL_TEMP));
    g.fillRect(0, 0, imgWidth, imgHeight);
  }
  
  /**
   * Updates the display with the values from the temperature grid.
   * 
   * @param grid the grid to get the new temperature values from
   */
  void updateGrid(TemperatureGrid grid) {
    this.grid = grid;
    paintInitialColors = false;    
    this.repaint();
  }

  /**
   * This is used implicitly by swing to do it's layout job properly
   */
  public int getWidth() {
    return imgWidth;
  }
  
  private void fillCellColors(Graphics g) {
	float cellX=0, cellY=0;
    float cellWidth = earthImage.getWidth() / (float)this.grid.getLongitudeLength();
    float degreeSeparation = 180 / this.grid.getLatitudeLength();
    
    int latitude = 0;
    float previousHeight = 0;
    
    int[] lineY = new int[this.grid.getLatitudeLength()];
    boolean first = true;
    
    for (int x = 0; x < this.grid.getLongitudeLength(); x++) {
      previousHeight = 0; 
      for (int y = 0; y < this.grid.getLatitudeLength(); y++) {
    	latitude = 90 - (int)(degreeSeparation * (y + 1));
    	  
        double newTemp = grid.getTemperature(x, y);
        int colorValue = new Double(newTemp).intValue();
        
        float distToEquator = Util.getDistToEquator(latitude, this.imgHeight / 2);
        
        float cellHeight = this.radius - distToEquator - previousHeight;
        
        cellHeight = Math.abs(cellHeight);
        
        previousHeight += cellHeight;
                
        g.setColor(colorPicker.getColor(colorValue));
        g.fillRect(Math.round(cellX), Math.round(cellY), Math.round(cellWidth), Math.round(cellHeight));
        cellY += cellHeight;
        
        // Store the vertical coordinate for the latitude lines
        if (first) {
        	lineY[y] = this.radius - (int)distToEquator;
        }
      }      
      if (first) {
      	first = !first;
      }
      // draw the longitude line
      g.setColor(Color.black);
      g.drawLine((int)cellX, 0, (int)cellX, imgHeight); 
      cellX += cellWidth;
      cellY = 0;
    }

    // draw the latitude lines
    g.setColor(Color.black);
    for (int line : lineY) {
        g.drawLine(0, line, imgWidth, line);
    }
  }
  
  private void drawTransparentImage(Graphics g) {    
    RescaleOp rop = new RescaleOp(scales, offsets, null);
    Graphics2D g2d = (Graphics2D)g;
    g2d.drawImage(imgTransparent, rop, 0, 0);
  }
  
  private void drawGrid(Graphics g) {
    g.setColor(Color.black);
    g.setColor(Color.blue);
    g.drawLine(imgWidth/2, 0, imgWidth/2, imgHeight); //prime meridian
    g.drawLine(0, imgHeight/2, imgWidth, imgHeight/2); // equator
  }
  
  /**
   * Sets the opacity of the map image on a scale of 0 to 1, with 0 being 
   * completely transparent.
   * 
   * @param value the opacity value
   */
  void setMapOpacity(float value) {
    scales[3] = value;
  }

  void reset() {
    paintInitialColors = true;
  }
  
}
