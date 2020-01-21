package glick;

import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.math.BigDecimal;
import resources.DoubleDouble;

public class KeyManager implements KeyListener {

	private boolean[] keys; // an array of all keys
	public boolean up, down, pUp, pDown; // all the keys this program cares about
	// up= zoom in, down = zoom out
	private Launcher launcher;

	public KeyManager(Launcher launcher) {
		keys = new boolean[256]; // sets the size of the keys array
		this.launcher = launcher;
	}

	@Override
	public void keyPressed(KeyEvent e) { // sets the key code of that key pressed to true in the keys array
		keys[e.getKeyCode()] = true;
		up = keys[KeyEvent.VK_UP]; // zoom in
		down = keys[KeyEvent.VK_DOWN]; // zoom out
		pUp = keys[KeyEvent.VK_PAGE_UP]; // limit up
		pDown = keys[KeyEvent.VK_PAGE_DOWN]; // limit down

		switch (launcher.precision) {
		case LOW_PRECISION:
			if (up) {
				zoomIn();
			} else if (down) {
				zoomOut();
			}

			if (pUp) {
				limitUp();
			} else if (pDown) {
				limitDown();
			}
			break;
		case HIGH_PRECISION:
			if (up) {
				zoomInHP();
			} else if (down) {
				zoomOutHP();
			}

			if (pUp) {
				limitUp();
			} else if (pDown) {
				limitDown();
			}
			break;
		case INFINITE_PRECISION:
			if (up) {
				zoomInIP();
			} else if (down) {
				zoomOutIP();
			}

			if (pUp) {
				limitUp();
			} else if (pDown) {
				limitDown();
			}
			break;
		}

	}

	@Override
	public void keyReleased(KeyEvent e) { // sets it to false
		keys[e.getKeyCode()] = false;

	}

	@Override
	public void keyTyped(KeyEvent e) {

	}

	public void zoomIn() {
		launcher.setScale(launcher.getScale() * 2);
		launcher.getDisplay().refresh();
	}

	public void zoomInHP() {
		launcher.setScaleHP(launcher.getScaleHP().multiply(DoubleDouble.valueOf(2)));
		launcher.getDisplay().refresh();
	}

	public void zoomInIP() {
		launcher.setScaleIP(launcher.getScaleIP().multiply(new BigDecimal(2), Launcher.rMode));
		launcher.getDisplay().refresh();
	}

	public void zoomOut() {
		launcher.setScale(launcher.getScale() / 2);
		launcher.getDisplay().refresh();
	}

	public void zoomOutHP() {
		launcher.setScaleHP(launcher.getScaleHP().divide(DoubleDouble.valueOf(2)));
		launcher.getDisplay().refresh();
	}

	public void zoomOutIP() {
		launcher.setScaleIP(launcher.getScaleIP().divide(new BigDecimal(2), Launcher.rMode));
		launcher.getDisplay().refresh();
	}

	public void limitUp() {
		launcher.setLimit(launcher.getLimit() + 50);
		launcher.getDisplay().refresh();
	}

	public void limitDown() {
		launcher.setLimit(launcher.getLimit() - 50);
		launcher.getDisplay().refresh();
	}
}
