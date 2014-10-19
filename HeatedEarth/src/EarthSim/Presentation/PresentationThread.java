/**
 * 
 */
package EarthSim.Presentation;

import java.awt.Dimension;
import java.util.concurrent.BlockingQueue;

import EarthSim.Presentation.earth.TemperatureGrid;

/**
 * @author pablo
 *
 */
public class PresentationThread extends Thread {
	public static Presentation presentation = new Presentation(new Dimension(800, 600), new Dimension(800, 600), new Dimension(800, 600));
	public BlockingQueue<TemperatureGrid> temperatureGrid;
	
	/**
	 * 
	 */
	public PresentationThread() {
		super(presentation);
	}

	/**
	 * @param target
	 */
	public PresentationThread(Runnable target) {
		super(target);
		// TODO Auto-generated constructor stub
	}

	/**
	 * @param name
	 */
	public PresentationThread(String name) {
		super(name);
		// TODO Auto-generated constructor stub
	}

	/**
	 * @param group
	 * @param target
	 */
	public PresentationThread(ThreadGroup group, Runnable target) {
		super(group, target);
		// TODO Auto-generated constructor stub
	}

	/**
	 * @param group
	 * @param name
	 */
	public PresentationThread(ThreadGroup group, String name) {
		super(group, name);
		// TODO Auto-generated constructor stub
	}

	/**
	 * @param target
	 * @param name
	 */
	public PresentationThread(Runnable target, String name) {
		super(target, name);
		// TODO Auto-generated constructor stub
	}

	/**
	 * @param group
	 * @param target
	 * @param name
	 */
	public PresentationThread(ThreadGroup group, Runnable target, String name) {
		super(group, target, name);
		// TODO Auto-generated constructor stub
	}

	/**
	 * @param group
	 * @param target
	 * @param name
	 * @param stackSize
	 */
	public PresentationThread(ThreadGroup group, Runnable target, String name,
			long stackSize) {
		super(group, target, name, stackSize);
		// TODO Auto-generated constructor stub
	}

	
	@Override
	public void run() {
		TemperatureGrid newGrid;
		try {
			while ((newGrid = temperatureGrid.take()) != null) {
				presentation.updateGrid(newGrid);
			}
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
	}
}
