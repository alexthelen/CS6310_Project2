package EarthSim;

/**
 * Interface for listeners of the {@code ProcessingComponent} class
 * 
 * @author Pablo Gallastegui
 * @author TJ Baxter
 * @version 1
 *
 */
public interface ProcessingComponentListener {
	public void onProcessComplete(ComponentType origin);
}
