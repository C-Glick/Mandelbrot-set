import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;

public class MouseManager implements MouseListener{
	Launcher launcher;

	public MouseManager(Launcher launcher) {
		this.launcher = launcher;
	}
	@Override
	public void mouseClicked(MouseEvent event) {
		System.out.println("Mouse clicked at x="+event.getX()+" y="+event.getY());
		
		if(launcher.highPrecision) {
			launcher.setCenterHP(event.getX(),event.getY());
			launcher.refresh();
		}else {
			launcher.setCenter(event.getX(),event.getY());
			launcher.refresh();
		}
		
	}

	@Override
	public void mouseEntered(MouseEvent event) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void mouseExited(MouseEvent event) {
		// TODO Auto-generated method stub
		
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
