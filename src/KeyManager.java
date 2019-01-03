import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;

public class KeyManager extends Thread implements KeyListener{
	
	private boolean[] keys;							//an array of all keys
	public boolean up,down;			//all the keys this program cares about
									//up= zoom in, down = zoom out
	private Launcher launcher;
	public KeyManager(Launcher launcher) {
		keys = new boolean[256];					//sets the size of the keys array
		this.launcher = launcher;
	}

	public void tick() {													//this method is run many times per second (dependent on the fps)
		up = keys[KeyEvent.VK_UP];					//sets the up boolean to whatever the entery in keys is at the "up arrow" key code
		down = keys[KeyEvent.VK_DOWN];
	}
	@Override
	public void keyPressed(KeyEvent e) {			//sets the key code of that key pressed to true in the keys array
		keys[e.getKeyCode()] =true;
		System.out.println("key pressed");
	}

	@Override
	public void keyReleased(KeyEvent e) {			//sets it to false 
		keys[e.getKeyCode()] =false;

	}

	@Override
	public void keyTyped(KeyEvent e) {
		
	}
	
	public void run() {
		System.out.println("thread is running");
		while(true) {
			tick();
			if(up) {
				System.out.println("up pressed");
				launcher.setScale(launcher.getScale()+1);
				launcher.calculate(launcher);
				launcher.getDisplay().repaint();
			}
			if(down) {
				System.out.println("down pressed");
				launcher.setScale(launcher.getScale()-1);
				launcher.getDisplay().repaint();
			}
		}
	}

}
