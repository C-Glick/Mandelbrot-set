package glick.gui.videoExport;

public class KeyPoint {
	private double value;
	private int frame;

	public KeyPoint(double value, int frame) {
		this.frame = frame;
		this.value = value;
	}

	public double getValue() {
		return value;
	}

	public int getFrame() {
		return frame;
	}

	public void setValue(double value) {
		this.value = value;
	}

	public void setFrame(int frame) {
		this.frame = frame;
	}

	@Override
	public String toString() {
		return "Frame: " + frame + " Value: " + value;
	}
}
