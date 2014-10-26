/**
 * 
 */
package EarthSim;

/**
 * Interface for listeners of event fired when the simulation is
 * complete and should stop.
 * 
 * @author Pablo Gallastegui
 * @version 1
 *
 */
public interface SimulationTimeListener {
	public void onSimulationComplete(ComponentType origin);
}
