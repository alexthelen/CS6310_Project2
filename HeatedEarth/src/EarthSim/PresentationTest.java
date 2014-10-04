package EarthSim;

import java.util.ArrayList;
import java.util.List;

public class PresentationTest implements Runnable {
	private List<PresentationListener> _listeners;
	
	/**
	 * Add a listener to the presentation
	 * 
	 * @param listener a {@code PresentationListener} containing the functionality to execute when the Presentation has events to dispatch
	 */
	public void setPresentationListener(PresentationListener listener) {
		_listeners.add(listener);
	}
	
	/**
	 * Fire the presentation complete event on all listeners
	 */
	private void presentationComplete() {
		for (PresentationListener listener : _listeners) {
			listener.onPresentationComplete();
		}
	}
	
	public PresentationTest() {
		_listeners = new ArrayList<PresentationListener>();
	}

	@Override
	public void run() {
		for (int i = 0; i < 1000; i++) {
			System.out.println("From Presentation - Presentation complete");
			this.presentationComplete();
			try {
				Thread.sleep(4000);
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
	}

	
	public interface PresentationListener {
		public void onPresentationComplete();
	}
}
