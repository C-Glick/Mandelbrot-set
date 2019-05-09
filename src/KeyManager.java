import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.math.BigDecimal;
import java.math.BigInteger;

public class KeyManager implements KeyListener{
	
	private boolean[] keys;							//an array of all keys
	public boolean up,down,pUp,pDown;			//all the keys this program cares about
									//up= zoom in, down = zoom out
	private Launcher launcher;
	public KeyManager(Launcher launcher) {
		keys = new boolean[256];					//sets the size of the keys array
		this.launcher = launcher;
	}

	
	@Override
	public void keyPressed(KeyEvent e) {			//sets the key code of that key pressed to true in the keys array
		keys[e.getKeyCode()] =true;
		up = keys[KeyEvent.VK_UP];					//zoom in
		down = keys[KeyEvent.VK_DOWN];				//zoom out
		pUp = keys[KeyEvent.VK_PAGE_UP];			//limit up
		pDown = keys[KeyEvent.VK_PAGE_DOWN];		//limit down
		
		if(launcher.highPrecision) {
			if(up) {zoomInHP();}
				else if(down) {zoomOutHP();}
			
			if(pUp) {limitUp();}
				else if (pDown) {limitDown();}	
		}
		
		else {
			if(up) {zoomIn();}
				else if (down) {zoomOut();}
			
			if(pUp) {limitUp();}
				else if (pDown) {limitDown();}
		}
		
	}

	@Override
	public void keyReleased(KeyEvent e) {			//sets it to false 
		keys[e.getKeyCode()] =false;

	}

	@Override
	public void keyTyped(KeyEvent e) {
		
	}
	
	public void zoomIn() {
		launcher.setScale(launcher.getScale()*2);
		launcher.refresh();
	}
	
	public void zoomInHP() {
		launcher.setScaleHP(launcher.getScaleHP().multiply(new BigDecimal(2),Launcher.rMode));
		launcher.refresh();
	}
	
	public void zoomOut() {
		launcher.setScale(launcher.getScale()/2);
		launcher.refresh();
	}
	public void zoomOutHP() {
		launcher.setScaleHP(launcher.getScaleHP().divide(new BigDecimal(2),Launcher.rMode));
		launcher.refresh();
	}
	
	public void limitUp() {
		launcher.setLimit(launcher.getLimit()+50);
		launcher.refresh();
	}
	
	public void limitDown() {
		launcher.setLimit(launcher.getLimit()-50);
		launcher.refresh();
	}
}
