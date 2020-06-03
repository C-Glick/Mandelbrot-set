package glick;

import java.math.BigDecimal;
import java.math.MathContext;

import glick.gui.Display;
import glick.lib.BigComplex;
import glick.lib.Complex;
import glick.lib.DoubleDoubleComplex;
import glick.lib.Precision;
import resources.DoubleDouble;

public class Launcher {
	static GlobalHandler globalExceptionHandler = new GlobalHandler();
	static String title = "The MandelBrot Set";
	static private int width = 800;
	static private int height = 400;
	static KeyManager keyManager;
	static MouseManager mouseManager;
	static Tester tester;
	static Display display;
	static double[][] resultsArray = new double[width][height];
	static Complex topLeft;
	static Complex bottomRight;
	static boolean firstBoot = true;
	static boolean isVideoExporting = false;

	static boolean enableGrid = false;

	static double threshold = 2; // threshold for the test, stops once the value grows higher than this (default
									// is 2)
	static int limit = 150; // think of this as the resolution of the graph, the more the function is
							// allowed to iterate, the more detailed the result. This is the maximum number
							// of iterations
	static double scale = 1; // the scale of the graph, a higher number is more zoomed in
	static Complex center = new Complex(0, 0); // the center of the window, determines the bounds of the window from
												// this number,

	public Precision precision = Precision.LOW_PRECISION; // define the precision state and set it's default value

	// high precision variables
	static DoubleDoubleComplex topLeftHP;
	static DoubleDoubleComplex bottomRightHP;
	static DoubleDoubleComplex centerHP;
	static DoubleDouble widthHP;
	static DoubleDouble heightHP;
	static DoubleDouble scaleHP;

	// infinite precision variables
	static BigComplex topLeftIP;
	static BigComplex bottomRightIP;
	static BigComplex centerIP = new BigComplex(new BigDecimal(Double.toString(center.getReal())),
			new BigDecimal(Double.toString(center.getImag())));
	static BigDecimal widthIP = new BigDecimal(width);
	static BigDecimal heightIP = new BigDecimal(height);
	static BigDecimal scaleIP = new BigDecimal(scale);
	static MathContext rMode = BigComplex.getRMode();

	public static void main(String[] args) {
		Thread.setDefaultUncaughtExceptionHandler(globalExceptionHandler);

		Launcher launcher = new Launcher(); // creates a new launcher object
		tester = new Tester(); // creates a new tester
		keyManager = new KeyManager(launcher); // creates a the key listener
		mouseManager = new MouseManager(launcher);
		display = new Display(title, width, height, launcher); // create a new display

		launcher.calculate(launcher); // calculate the default values

		display.start(); // run setup on display and make it visible
		firstBoot = false;
	}

	/**
	 * Calculate the graph to be drawn from the values set in the launcher class.
	 * 
	 * @param launcher The launcher with the desired values.
	 */
	public void calculate(Launcher launcher) {
		switch (precision) {
		case LOW_PRECISION:
			width = display.getWidth();
			height = display.getHeight();
			double halfWidth = ((double) width / (scale * (double) 512));
			double halfHeight = ((double) height / (scale * (double) 512));
			// scale is multiplied by 512 to give a good initial size

			topLeft = new Complex(center.getReal() - halfWidth, center.getImag() + halfHeight); // Determine the complex
																								// numbers that
																								// correspond to the
																								// topLeft and bottom
																								// right pixels of the
																								// window
			bottomRight = new Complex(center.getReal() + halfWidth, center.getImag() - halfHeight);

			tester.test2(topLeft, bottomRight, width, height); // tell the tester object to run test #2 given the
																// details about the graph
			break; // the results will be saved into the resultsArray as each thread completes a
					// calculation
		case HIGH_PRECISION:
			widthHP = new DoubleDouble(display.getWidth());
			heightHP = new DoubleDouble(display.getHeight());
			DoubleDouble halfWidthHP = widthHP.divide(scaleHP.multiply(new DoubleDouble(512)));
			DoubleDouble halfHeightHP = heightHP.divide(scaleHP.multiply(new DoubleDouble(512)));

			topLeftHP = new DoubleDoubleComplex(centerHP.getReal().subtract(halfWidthHP),
					centerHP.getImag().add(halfHeightHP));
			bottomRightHP = new DoubleDoubleComplex(centerHP.getReal().add(halfWidthHP),
					centerHP.getImag().subtract(halfHeightHP));

			tester.test5(topLeftHP, bottomRightHP, widthHP, heightHP);
			break;
		case INFINITE_PRECISION:
			widthIP = new BigDecimal(display.getWidth());
			heightIP = new BigDecimal(display.getHeight());
			rMode = BigComplex.getRMode();
			BigDecimal correctedScale = scaleIP.multiply(new BigDecimal("512"));
			topLeftIP = new BigComplex(centerIP.getReal().subtract(widthIP.divide(correctedScale, rMode), rMode),
					centerIP.getImag().add(heightIP.divide(correctedScale, rMode), rMode));
			bottomRightIP = new BigComplex(centerIP.getReal().add(widthIP.divide(correctedScale, rMode), rMode),
					centerIP.getImag().subtract(heightIP.divide(correctedScale, rMode), rMode));

			tester.test4(topLeftIP, bottomRightIP, widthIP, heightIP);
			break;
		}
	}

	public void setResultsArray(double[][] input) {
		resultsArray = input;
	}

	public void setScale(double input) {
		scale = input;
		System.out.println("new scale: " + Launcher.scale);
	}

	public void setScaleHP(DoubleDouble input) {
		scaleHP = input;
		System.out.println("new scale: " + Launcher.scaleHP);
	}

	public void setScaleIP(BigDecimal input) {
		scaleIP = input;
		System.out.println("new scale: " + Launcher.scaleIP);
	}

	/**
	 * Set center based on the X and Y values of the image *NOT the graph* 0,0 is
	 * the upper right hand corner.
	 * 
	 * @param x the images x value to be the center
	 * @param y the images y value to be the center
	 */
	public void setCenter(int x, int y) {
		Complex complex = new Complex(((bottomRight.getReal() - topLeft.getReal()) / width) * x + topLeft.getReal(),
				(((bottomRight.getImag() - topLeft.getImag()) / height) * y + topLeft.getImag()));
		Launcher.center = complex;
	}

	public void setCenter(Complex input) {
		Launcher.center = input;
	}

	public void setCenterHP(int x, int y) {
		DoubleDoubleComplex complexHP = new DoubleDoubleComplex(
				bottomRightHP.getReal().subtract(topLeftHP.getReal()).divide(widthHP).multiply(new DoubleDouble(x))
						.add(topLeftHP.getReal()),
				bottomRightHP.getImag().subtract(topLeftHP.getImag()).divide(heightHP).multiply(new DoubleDouble(y))
						.add(topLeftHP.getImag()));
		Launcher.centerHP = complexHP;
	}

	public void setCenterHP(DoubleDoubleComplex input) {
		Launcher.centerHP = input;
	}

	public void setCenterIP(int x, int y) {
		BigComplex bigComplex = new BigComplex(
				((bottomRightIP.getReal().subtract(topLeftIP.getReal())).divide(widthIP)).multiply(new BigDecimal(x))
						.add(topLeftIP.getReal()),
				((bottomRightIP.getImag().subtract(topLeftIP.getImag())).divide(heightIP)).multiply(new BigDecimal(y))
						.add(topLeftIP.getImag()));

		Launcher.centerIP = bigComplex;
	}

	public void setCenterIP(BigComplex input) {
		Launcher.centerIP = input;
	}

	public void setLimit(int input) {
		Launcher.limit = input;
		System.out.println("New limit: " + Launcher.limit);
	}

	/**
	 * For low precision pass in "LP", high precision = "HP", infinite precision =
	 * "IP"
	 * 
	 * @param newPrecision
	 */
	public void setPrecision(Precision newPrecision) {
		// check current precision then check new precision value
		switch (precision) {
		case LOW_PRECISION:
			switch (newPrecision) {
			case HIGH_PRECISION:
				precision = Precision.HIGH_PRECISION;
				centerHP = new DoubleDoubleComplex(center);
				widthHP = new DoubleDouble(width);
				heightHP = new DoubleDouble(height);
				scaleHP = new DoubleDouble(scale);
				break;
			case INFINITE_PRECISION:
				precision = Precision.INFINITE_PRECISION;
				centerIP = new BigComplex(center);
				widthIP = new BigDecimal(width);
				heightIP = new BigDecimal(height);
				scaleIP = new BigDecimal(scale);
				break;
			default:
				// do nothing
			}
			break;
		case HIGH_PRECISION:
			switch (newPrecision) {
			case LOW_PRECISION:
				precision = Precision.LOW_PRECISION;
				center = new Complex(centerHP);
				width = widthHP.intValue();
				height = heightHP.intValue();
				scale = scaleHP.doubleValue();
				break;
			case INFINITE_PRECISION:
				precision = Precision.INFINITE_PRECISION;
				centerIP = new BigComplex(centerHP);
				widthIP = new BigDecimal(widthHP.doubleValue());
				heightIP = new BigDecimal(heightHP.doubleValue());
				scaleIP = new BigDecimal(scaleHP.toString());
				break;
			default:
				// do nothing
			}
			break;
		case INFINITE_PRECISION:
			switch (newPrecision) {
			case LOW_PRECISION:
				precision = Precision.LOW_PRECISION;
				center = new Complex(centerIP);
				width = widthIP.intValue();
				height = heightIP.intValue();
				scale = scaleIP.doubleValue();
				break;
			case HIGH_PRECISION:
				centerHP = new DoubleDoubleComplex(centerIP);
				widthHP = new DoubleDouble(widthIP.toPlainString());
				heightHP = new DoubleDouble(heightIP.toPlainString());
				scaleHP = new DoubleDouble(scaleIP.toPlainString());
				break;
			default:
				// do nothing
			}

		}
		System.out.println("Precision set to " + newPrecision);
	}

	public KeyManager getKeyManager() {
		return keyManager;
	}

	public MouseManager getMouseManager() {
		return mouseManager;
	}

	public Complex getCenter() {
		return center;
	}

	public DoubleDoubleComplex getCenterHP() {
		return centerHP;
	}

	public BigComplex getCenterIP() {
		return centerIP;
	}

	public double getScale() {
		return scale;
	}

	public DoubleDouble getScaleHP() {
		return scaleHP;
	}

	public BigDecimal getScaleIP() {
		return scaleIP;
	}

	public int getLimit() {
		return limit;
	}

	public double getThreshold() {
		return threshold;
	}

	public double[][] getResultsArray() {
		return resultsArray;
	}

	public Display getDisplay() {
		return display;
	}

	public Tester getTester() {
		return tester;
	}

	public void setIsVideoExporting(boolean value) {
		isVideoExporting = value;
	}

	public boolean isVideoExporting() {
		return isVideoExporting;
	}
}
