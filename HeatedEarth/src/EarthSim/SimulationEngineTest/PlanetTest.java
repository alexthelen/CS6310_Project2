package EarthSim.SimulationEngineTest;

import static org.junit.Assert.*;

import java.util.GregorianCalendar;

import org.junit.Test;
import EarthSim.SimulationEngine.*;

public class PlanetTest {

	@Test
	public void testGetGridCellInvalidLatitude() 
	{
		try 
		{
			Planet earth = new Planet(10);
			earth.GetGridCell(100, 0);
			fail("Invalid latitude exception should be thrown.");
		} 
		catch (Exception e) 
		{
			assertEquals(Constants.invalidLatitudeMessage, e.getMessage());
		}
	}
	
	@Test
	public void testGetGridCellInvalidLongitude() 
	{
		try 
		{
			Planet earth = new Planet(10);
			earth.GetGridCell(0, 200);
			fail("Invalid longitude exception should be thrown.");
		} 
		catch (Exception e) 
		{
			assertEquals(Constants.invalidLongitudeMessage, e.getMessage());
		}
	}
	
	@Test
	public void testGetGridCellValidCoordinates() 
	{
		try 
		{
			Planet earth = new Planet(10);
			GridCell cell = earth.GetGridCell(0, 0);
			assertEquals(0, cell.GetLatitude());
			assertEquals(0, cell.GetLongitude());
		} 
		catch (Exception e) 
		{
			fail(e.getMessage());
		}
	}
	
	@Test
	public void testRotatePlanet() 
	{
		try 
		{
			Planet earth = new Planet(10);
			earth.RotatePlanet();
			assertEquals(new GregorianCalendar(2000, 1, 1, 1, 0, 0), earth.GetIDLDateTime());
		} 
		catch (Exception e) 
		{
			fail(e.getMessage());
		}
	}

	@Test
	public void testApplyHeatChange() 
	{
		try 
		{
			Planet earth = new Planet(10);
			GridCell cell;
			earth.ApplyHeatChange();
			
			cell = earth.GetGridCell(0, 0);		
			assertEquals(288.0, cell.GetTemp(), 0.01);
			assertEquals(0, cell.GetTemp(), 0.01);	
			
			cell = earth.GetGridCell(0, 180);
			assertEquals(288.0, cell.GetTemp(), 0.01);
			assertEquals(0, cell.GetTemp(), 0.01);
		} 
		catch (Exception e) 
		{
			fail(e.getMessage());
		}
	}
	
}
