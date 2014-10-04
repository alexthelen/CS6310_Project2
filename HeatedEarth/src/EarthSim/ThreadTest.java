package EarthSim;

import java.util.List;

import EarthSim.PresentationTest.PresentationListener;

public class ThreadTest {

	public static void main(String[] args) {
		// TODO Auto-generated method stub
		
		PresentationTest presentation = new PresentationTest();
		final SimulationTest simulation = new SimulationTest();
		
		Thread presentationThread = new Thread(presentation);
		Thread simulationThread = new Thread(simulation);
		
		presentation.setPresentationListener(new PresentationListener() {

			@Override
			public void onPresentationComplete() {
				simulation.respondToPresentationComplete();
			}
			
		});
		
		presentationThread.start();
		simulationThread.start();
	}
	
}
