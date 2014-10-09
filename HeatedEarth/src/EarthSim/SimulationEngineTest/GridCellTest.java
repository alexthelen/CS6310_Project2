package EarthSim.SimulationEngineTest;

import static org.junit.Assert.*;
import org.junit.Test;
import EarthSim.SimulationEngine.*;

public class GridCellTest {

	@Test
	public void testNorthLatitude() 
	{
		 try
		 {
			 GridCell cell = new GridCell(100, 0);
			 fail("Exception should be thrown if latitude is greater than 90");
		 }
		 catch (Exception ex)
		 {
			 assertEquals(Constants.invalidLatitudeMessage, ex.getMessage());
		 }
	}
	
	@Test
	public void testSouthLatitude() 
	{
		 try
		 {
			 GridCell cell = new GridCell(-100, 0);
			 fail("Exception should be thrown if latitude is less than -90");
		 }
		 catch (Exception ex)
		 {
			 assertEquals(Constants.invalidLatitudeMessage, ex.getMessage());
		 }
	}
	
	@Test
	public void tesEastLongitude() 
	{
		 try
		 {
			 GridCell cell = new GridCell(0, 190);
			 fail("Exception should be thrown if longitude is greater than 180");
		 }
		 catch (Exception ex)
		 {
			 assertEquals(Constants.invalidLongitudeMessage, ex.getMessage());
		 }
	}

	@Test
	public void testWestLongitude() 
	{
		 try
		 {
			 GridCell cell = new GridCell(0, -190);
			 fail("Exception should be thrown if longitude is less than -180");
		 }
		 catch (Exception ex)
		 {
			 assertEquals(Constants.invalidLongitudeMessage, ex.getMessage());
		 }
	}

	@Test
	public void tesNorthHemisphereCalculations() 
	{
		try
		{
			GridCell cell = new GridCell(20, 0);
			
			assertEquals(20, cell.GetLatitude());
			assertEquals(0, cell.GetLongitude());
			assertEquals(288, cell.GetTemp(), 0.01);
			assertEquals(1110, cell.GetHeight(), 0.01);
			assertEquals(1046.038, cell.GetSouthBaseLength(), 0.01);
			assertEquals(964.034, cell.GetNorthBaseLength(), 0.01);
			assertEquals(1110.757, cell.GetEastLength(),0.01);		
			assertEquals(1110.757, cell.GetWestLength(), 0.01);
			assertEquals(1115590.478, cell.GetSurfaceArea(),0.01);
			assertEquals(4231.587, cell.CalculatePerimeter(), 0.01);
		}
		catch (Exception ex)
		{
			fail(ex.getMessage());
		}
	}
	
	@Test
	public void testSouthHemisphereCalculations() 
	{
		try
		{
			GridCell cell = new GridCell(-20, 0);
			
			assertEquals(1046.038, cell.GetNorthBaseLength(), 0.01);
			assertEquals(964.034, cell.GetSouthBaseLength(), 0.01);
			assertEquals(1110.757, cell.GetEastLength(),0.01);		
			assertEquals(1110.757, cell.GetWestLength(), 0.01);
			assertEquals(1115590.478, cell.GetSurfaceArea(),0.01);
			assertEquals(4231.587, cell.CalculatePerimeter(), 0.01);
		}
		catch (Exception ex)
		{
			fail(ex.getMessage());
		}
	}
	
	@Test
	public void tesNorthHemispherePoleCalculations() 
	{
		try
		{
			GridCell cell = new GridCell(80, 0);
			
			assertEquals(0.0, cell.GetNorthBaseLength(), 0.01);
			assertEquals(193.300, cell.GetSouthBaseLength(), 0.01);
			assertEquals(1114.199, cell.GetEastLength(),0.01);		
			assertEquals(1114.199, cell.GetWestLength(), 0.01);
			assertEquals(107281.564, cell.GetSurfaceArea(),0.01);
			assertEquals(2421.699, cell.CalculatePerimeter(), 0.01);
		}
		catch (Exception ex)
		{
			fail(ex.getMessage());
		}
	}
	
	@Test
	public void testSouthHemispherePoleCalculations() 
	{
		try
		{
			GridCell cell = new GridCell(-80, 0);
			
			assertEquals(193.300, cell.GetNorthBaseLength(), 0.01);
			assertEquals(0.0, cell.GetSouthBaseLength(), 0.01);
			assertEquals(1114.199, cell.GetEastLength(),0.01);		
			assertEquals(1114.199, cell.GetWestLength(), 0.01);
			assertEquals(107281.564, cell.GetSurfaceArea(),0.01);
			assertEquals(2421.699, cell.CalculatePerimeter(), 0.01);
		}
		catch (Exception ex)
		{
			fail(ex.getMessage());
		}
	}

	@Test
	public void testNorthHemisphereGetNeighbors() 
	{
		try
		{
			GridCell cell = new GridCell(20, 0);
			
			int[] north = cell.GetNorthNeighborCoordinates();
			int[] south = cell.GetSouthNeighborCoordinates();
			int[] east = cell.GetEastNeighborCoordinates();
			int[] west = cell.GetWestNeighborCoordinates();
			
			assertArrayEquals(new int[] {30,0}, north);
			assertArrayEquals(new int[] {10,0}, south);
			assertArrayEquals(new int[] {20,10}, east);
			assertArrayEquals(new int[] {20,-10}, west);
		}
		catch (Exception ex)
		{
			fail(ex.getMessage());
		}
	}
	
	@Test
	public void testSouthHemisphereGetNeighbors() 
	{
		try
		{
			GridCell cell = new GridCell(-20, 0);
			
			int[] north = cell.GetNorthNeighborCoordinates();
			int[] south = cell.GetSouthNeighborCoordinates();
			int[] east = cell.GetEastNeighborCoordinates();
			int[] west = cell.GetWestNeighborCoordinates();
			
			assertArrayEquals(new int[] {-10,0}, north);
			assertArrayEquals(new int[] {-30,0}, south);
			assertArrayEquals(new int[] {-20,10}, east);
			assertArrayEquals(new int[] {-20,-10}, west);
		}
		catch (Exception ex)
		{
			fail(ex.getMessage());
		}
	}
	
	@Test
	public void testEastHemisphereGetNeighbors() 
	{
		try
		{
			GridCell cell = new GridCell(0, 20);
			
			int[] north = cell.GetNorthNeighborCoordinates();
			int[] south = cell.GetSouthNeighborCoordinates();
			int[] east = cell.GetEastNeighborCoordinates();
			int[] west = cell.GetWestNeighborCoordinates();
			
			assertArrayEquals(new int[] {10,20}, north);
			assertArrayEquals(new int[] {-10,20}, south);
			assertArrayEquals(new int[] {0,30}, east);
			assertArrayEquals(new int[] {0,10}, west);
		}
		catch (Exception ex)
		{
			fail(ex.getMessage());
		}
	}
	
	@Test
	public void testWestHemisphereGetNeighbors() 
	{
		try
		{
			GridCell cell = new GridCell(0, -20);
			
			int[] north = cell.GetNorthNeighborCoordinates();
			int[] south = cell.GetSouthNeighborCoordinates();
			int[] east = cell.GetEastNeighborCoordinates();
			int[] west = cell.GetWestNeighborCoordinates();
			
			assertArrayEquals(new int[] {10,-20}, north);
			assertArrayEquals(new int[] {-10,-20}, south);
			assertArrayEquals(new int[] {0,-10}, east);
			assertArrayEquals(new int[] {0,-30}, west);
		}
		catch (Exception ex)
		{
			fail(ex.getMessage());
		}
	}
	
	@Test
	public void testNorthPoleGetNeighbors() 
	{
		try
		{
			GridCell cell = new GridCell(80, 0);
			
			int[] north = cell.GetNorthNeighborCoordinates();
			int[] south = cell.GetSouthNeighborCoordinates();
			int[] east = cell.GetEastNeighborCoordinates();
			int[] west = cell.GetWestNeighborCoordinates();
			
			assertArrayEquals(new int[] {80,-180}, north);
			assertArrayEquals(new int[] {70,0}, south);
			assertArrayEquals(new int[] {80,10}, east);
			assertArrayEquals(new int[] {80,-10}, west);
		}
		catch (Exception ex)
		{
			fail(ex.getMessage());
		}
	}
	
	@Test
	public void testSouthPoleGetNeighbors() 
	{
		try
		{
			GridCell cell = new GridCell(-80, 0);
			
			int[] north = cell.GetNorthNeighborCoordinates();
			int[] south = cell.GetSouthNeighborCoordinates();
			int[] east = cell.GetEastNeighborCoordinates();
			int[] west = cell.GetWestNeighborCoordinates();
			
			assertArrayEquals(new int[] {-70,0}, north);
			assertArrayEquals(new int[] {-80,-180}, south);
			assertArrayEquals(new int[] {-80,10}, east);
			assertArrayEquals(new int[] {-80,-10}, west);
		}
		catch (Exception ex)
		{
			fail(ex.getMessage());
		}
	}
	
	public void testEastIDLGetNeighbors() 
	{
		try
		{
			GridCell cell = new GridCell(20, 170);
			
			int[] north = cell.GetNorthNeighborCoordinates();
			int[] south = cell.GetSouthNeighborCoordinates();
			int[] east = cell.GetEastNeighborCoordinates();
			int[] west = cell.GetWestNeighborCoordinates();
			
			assertArrayEquals(new int[] {30,170}, north);
			assertArrayEquals(new int[] {10,170}, south);
			assertArrayEquals(new int[] {20,-180}, east);
			assertArrayEquals(new int[] {20,160}, west);
		}
		catch (Exception ex)
		{
			fail(ex.getMessage());
		}
	}
	
	@Test
	public void testWestIDLPoleGetNeighbors() 
	{
		try
		{
			GridCell cell = new GridCell(20, -170);
			
			int[] north = cell.GetNorthNeighborCoordinates();
			int[] south = cell.GetSouthNeighborCoordinates();
			int[] east = cell.GetEastNeighborCoordinates();
			int[] west = cell.GetWestNeighborCoordinates();
			
			assertArrayEquals(new int[] {30,-170}, north);
			assertArrayEquals(new int[] {10,-170}, south);
			assertArrayEquals(new int[] {20,-160}, east);
			assertArrayEquals(new int[] {20,180}, west);
		}
		catch (Exception ex)
		{
			fail(ex.getMessage());
		}
	}
}
