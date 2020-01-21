package glick;

import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;

public class MouseManager implements MouseListener {
	Launcher launcher;

	public MouseManager(Launcher launcher) {
		this.launcher = launcher;

	}

	@Override
	public void mouseClicked(MouseEvent event) {
		System.out.println("Mouse clicked at x=" + event.getX() + " y=" + event.getY());

		switch (launcher.precision) {
		case LOW_PRECISION:
			launcher.setCenter(event.getX(), event.getY());
			launcher.getDisplay().refresh();
			break;
		case HIGH_PRECISION:
			launcher.setCenterHP(event.getX(), event.getY());
			launcher.getDisplay().refresh();
			break;
		case INFINITE_PRECISION:
			launcher.setCenterIP(event.getX(), event.getY());
			launcher.getDisplay().refresh();
			break;
		}
	}

	@Override
	public void mouseEntered(MouseEvent event) {
	}

	@Override
	public void mouseExited(MouseEvent event) {

	}

	@Override
	public void mousePressed(MouseEvent event) {
		System.out.println("mouse pressed");
		// TODO Auto-generated method stub

	}

	@Override
	public void mouseReleased(MouseEvent event) {
		// TODO Auto-generated method stub

	}

}
