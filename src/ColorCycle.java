
/**
 * A class to create a new thread and modify the colorOffset variable 
 * in the display class in order to create color cycling.
 *
 */
public class ColorCycle extends Thread {
	double speed;		
	Display display;
	boolean running;
	
	/**
	 * create a new colorCycle thread with the given speed and controlling the given display.
	 * Call the start method to start cycling and the stopCycle method to stop it.
	 * @param speed	How much to increment the color offset each loop. Speed
	 * should be very low, ex: 0.005 because the color offset range is 0.0-1.0.
	 * @param display The display to control
	 */
	ColorCycle(double speed,Display display){
		this.speed = speed;
		this.display = display;
		running=true;
	}
	
	public void run() {
		running=true;
		for(double i=0; running; i+=speed) {
			if(i>=1) {i=0;}
			display.setColorOffset(i);
			display.repaint();

		}
	}
	
	/**
	 * Stop the color cycling.
	 */
	public void stopCycle() {
		running=false;
		display.setColorOffset(0);
		display.repaint();
	}

}
