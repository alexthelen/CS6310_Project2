package EarthSim;

public class SimulationTest implements Runnable {

	boolean flag = false;
	
	@Override
	public void run() {
		for (int i = 0; i < 1000; i++) {
			if (flag) {
				System.out.println("From Simulation - Flag is true");
			} else {
				System.out.println("From Simulation - Flag is false");
			}
			try {
				Thread.sleep(5000);
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
	}
	
	public void respondToPresentationComplete() {
		flag = !flag;
		System.out.println("From Simulation - Presentation complete");
	}
	
}
