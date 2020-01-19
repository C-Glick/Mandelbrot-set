package glick.gui;
import java.awt.Color;
import java.util.ArrayList;
import java.util.Collections;

/**
 * A class made up of a list of GradientPoint objects. A linear gradient is
 * generated between each defined point. It is recommend to always define a
 * point at position 0.0 and 1.0
 *
 */
public class Gradient {
	ArrayList<GradientPoint> listOfPoints;

	/**
	 * Create an empty gradient
	 */
	public Gradient() {
		listOfPoints = new ArrayList<GradientPoint>();
	}

	/**
	 * Create a Gradient with two points, likely at position 0, and 1
	 * 
	 * @param point1 The left most point
	 * @param point2 The right most point
	 */
	public Gradient(GradientPoint point1, GradientPoint point2) {
		listOfPoints = new ArrayList<GradientPoint>();
		listOfPoints.add(point1);
		listOfPoints.add(point2);
		Collections.sort(listOfPoints);
	}

	/**
	 * Add a point to the gradient. It is not necessary to add points in the order
	 * in which they appear in the gradient. The list of gradient points will be
	 * sorted when a new point is added.
	 * 
	 * @param point
	 */
	public void addPoint(GradientPoint point) {
		listOfPoints.add(point);
		Collections.sort(listOfPoints);
	}

	/**
	 * Get the color of the gradient at a specific position.
	 * 
	 * @param position The position along the gradient, range from 0.0 to 1.0)
	 */
	public Color getColor(double position) {
		Color result = Color.BLACK;

		// normalize the position, if it is not within 0-1 make it
		position = normalize(position);

		// find which points the asked position is between
		GradientPoint pointA;
		GradientPoint pointB;
		for (int i = 0; i < listOfPoints.size() - 1; i++) {
			GradientPoint currentPoint = listOfPoints.get(i);
			if ((currentPoint.getPosition() <= position && listOfPoints.get(i + 1).getPosition() >= position)) {
				pointA = currentPoint;
				pointB = listOfPoints.get(i + 1);

				// get the local position (range 0-1) between the two points
				double localPos = (position - pointA.getPosition()) / (pointB.getPosition() - pointA.getPosition());

				// calculate the value of the RGB channels at the local position between the two
				// points
				int r = (int) (pointA.getR() + localPos * (pointB.getR() - pointA.getR()));
				int g = (int) (pointA.getG() + localPos * (pointB.getG() - pointA.getG()));
				int b = (int) (pointA.getB() + localPos * (pointB.getB() - pointA.getB()));
				result = new Color(r, g, b);
				break;
			}
		}
		return result;
	}

	private double normalize(double position) {
		if (position < 0) {
			position++;
			position = normalize(position);
		} else if (position > 1) {
			position--;
			position = normalize(position);
		}
		return position;
	}

}
