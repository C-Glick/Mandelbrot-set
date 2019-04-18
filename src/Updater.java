
public class Updater extends Thread {
	Launcher launcher;
	
	public Updater(Launcher launcher) {
		this.launcher = launcher;
	}
	
	public void run() {
		launcher.isUpdaterWorking = true;
		//launcher.display.progressBar.setIndeterminate(true);
		launcher.display.updateUI();
		System.out.println("clac");
		launcher.calculate(launcher);
		System.out.println("paint");
		launcher.getDisplay().repaint();
		//launcher.display.progressBar.setIndeterminate(false);
		launcher.isUpdaterWorking = false;
	}

}
