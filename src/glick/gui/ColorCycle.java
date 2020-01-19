package glick.gui;
import java.util.Date;

/**
 * A class to create a new thread and modify the colorOffset variable 
 * in the display class in order to create color cycling.
 *
 */
public class ColorCycle extends Thread {
	double speed;		
	Display display;
	boolean running;
	ColorCycle child;
	
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
		running=false;
	}
	
	/**
	 * Start the color cycle, create a new child thread and begin execution of child.
	 * This allows way only one ColorCycle object is required else where.
	 */
	public void startCycle() {
		running=true;
		child = new ColorCycle(this.speed, this.display);
		child.start();
	}
	
	/**
	 * The actual execution, increments the display color offset
	 * by the speed amount passed in at object initialization. 
	 * limits the FPS to 30.
	 */
	public void run() {
		running=true;
		Date lastUpdate = new Date();
		Date currentTime = new Date();
		double i=0;
		
		while(running) {
			currentTime = new Date();
			if(currentTime.getTime() - lastUpdate.getTime() >= 16) {		//if it has been more than 33 milliseconds since last update (30fps)
				lastUpdate = currentTime;
				if(i>=1) {i=0;}
				display.setColorOffset(i);
				display.repaint();
				i+=speed;
			}
			

		}
	}
	
	/**
	 * Stop the color cycling.
	 */
	public void stopCycle() {
		child.stopChildCycle();
		this.running = false;
		
		display.setColorOffset(0);
		display.repaint();
	}
	
	/**
	 * intended to only be run by children object.
	 */
	private void stopChildCycle() {
		running=false;
	}

}
