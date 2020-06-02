package glick.gui.videoExport;

import glick.Launcher;
import glick.gui.Display;

public class AutomaticController {
	Display display;

	Timeline scale;
	Timeline center;
	Timeline limit;
	Timeline colorOffset;
	double FPS;
	double length; // length of video in seconds

	public AutomaticController(Display display) {
		this.display = display;

		scale = new Timeline();
		center = new Timeline();
		limit = new Timeline();
		colorOffset = new Timeline();
		FPS = 30;
		length = 1;
	}

	public void run() {
		Launcher launcher = display.getLauncher();
		for (int frame = 0; frame <= length * FPS; frame++) {
			launcher.setScale(Math.pow(2, scale.getValue(frame)));
			// TODO: implement translation launcher.setCenter(center.getValue(frame));
			launcher.setLimit((int) limit.getValue(frame));
			display.setColorOffset(colorOffset.getValue(frame));

			display.refresh();
			try {
				Thread.sleep(100);
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			display.exportImage();

		}
	}

	public void setLength(double length) {
		this.length = length;
	}

	public Timeline getScaleTimeline() {
		return scale;
	}

	public Timeline getCenterTimeline() {
		return center;
	}

	public Timeline getLimitTimeline() {
		return limit;
	}

	public Timeline getColorOffsetTimeline() {
		return colorOffset;
	}

}
