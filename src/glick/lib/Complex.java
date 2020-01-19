package glick.lib;

/**
 * A class to represent a complex number (2+3i) through a real and an imaginary
 * part.
 * 
 * @author Colton Glick
 *
 */
public class Complex {
	double real;
	double imag;

	/**
	 * Construct a new Complex object.
	 * 
	 * @param real A double to represent the real portion of the complex number.
	 * @param imag A double to represent the imaginary portion.
	 */
	public Complex(double real, double imag) {
		this.real = real;
		this.imag = imag;
	}

	/**
	 * Create a new low precision complex number from a high precision complex
	 * number
	 * 
	 * @param complexHP The high precision complex number to convert from
	 */
	public Complex(DoubleDoubleComplex complexHP) {
		this.real = complexHP.getReal().doubleValue();
		this.imag = complexHP.getImag().doubleValue();
	}

	/**
	 * Create a new low precision complex number from an infinite precision complex
	 * number
	 * 
	 * @param complexIP The infinite precision complex number to convert from
	 */
	public Complex(BigComplex complexIP) {
		this.real = complexIP.getReal().doubleValue();
		this.imag = complexIP.getImag().doubleValue();

	}

	/**
	 * Adds the provided complex number to this object
	 * 
	 * @param num The Complex number to add to this object.
	 */
	public void add(Complex num) {
		real = getReal() + num.getReal();
		imag = getImag() + num.getImag();
	}

	/**
	 * Multiples this object by the complex number provided.
	 * 
	 * @param num The complex number to multiply this by.
	 */
	public void multiply(Complex num) {
		// (2+5i)*(3+8i) need to use the FOIL method

		double F = this.getReal() * num.getReal(); // will give some real number
		double O = this.getReal() * num.getImag(); // will give some number*i
		double I = this.getImag() * num.getReal();
		double L = this.getImag() * num.getImag(); // will give some number*i^2, i^2 is the same as -1 so this number
													// should be inverted

		// combine and simplify
		real = F - L;
		imag = O + I;
	}

	/**
	 * Squares this object.
	 */
	public void sqr() {
		// multiply(this);

		double r = Math.pow(getReal(), 2) - Math.pow(getImag(), 2); // F - L
		double i = (getImag() * getReal()) * 2; // (O or I) *2

		real = r;
		imag = i;
	}

	/**
	 * Returns the absolute value of this complex number using the pythagorean
	 * theorem. The distance between the origin and this point.
	 * 
	 * @return A double giving the absolute value of the complex number
	 */
	public double abs() {

		double result = Math.sqrt(Math.pow(getReal(), 2) + Math.pow(getImag(), 2));
		return result;
	}

	public double getReal() {
		return real;
	}

	public double getImag() {
		return imag;
	}

	public void setReal(double value) {
		real = value;
	}

	public void setImag(double value) {
		imag = value;
	}

}
