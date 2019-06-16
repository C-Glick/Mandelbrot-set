import java.awt.Color;

/**
 * A class used to define points on a gradient.
 * A GradientPoint has an position, a float between 0.0 and 1.0, a red value
 * a green value, and a blue value.
 *
 */
public class GradientPoint implements Comparable<GradientPoint> {
	Float position;		//a value from 0.0 to 1.0 determining the position in the gradient
	int r;				//rgb values between 0 and 255
	int g;
	int b;
	
	GradientPoint(int r, int g, int b, float position){
		this.r = r;
		this.g = g;
		this.b = b;
		this.position = position;
	}
	
	GradientPoint(Color color, float position){
		this.r = color.getRed();
		this.g = color.getGreen();
		this.b = color.getBlue();
		this.position = position;
	}

	public Float getPosition() {
		return position;
	}

	public int getR() {
		return r;
	}

	public int getG() {
		return g;
	}

	public int getB() {
		return b;
	}

	public void setPosition(float position) {
		this.position = position;
	}

	public void setR(int r) {
		this.r = r;
	}

	public void setG(int g) {
		this.g = g;
	}

	public void setB(int b) {
		this.b = b;
	}

	/**
	 * Compares two GradientPoint objects to allow for sorting.
	 */
	public int compareTo(GradientPoint otherPoint) {
		return this.getPosition().compareTo(otherPoint.getPosition());
	}
	
	
}
