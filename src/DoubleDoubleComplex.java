import resources.DoubleDouble;

/**
 * The same as the complex class but uses the DoubleDouble class to create high precision complex numbers.
 * @author Colton Glick
 *
 */
public class DoubleDoubleComplex {
	DoubleDouble real;
	DoubleDouble imag;
	
	/**
	 * Complex number example: (2+5i), real = 2, imag = 5.
	 * Constructor for a DoubleDoubleComplex object. Uses DoubleDouble objects to create high Precision objects.
	 * when creating new DoubleDouble objects, use strings rather than doubles for the best results, 
	 * 'new DoubleDouble("48.02761");' not 'new DoubleDouble(someDouble);'
	 * @param real	A DoubleDouble object to represent the real part of the complex number
	 * @param imag	A DoubleDouble object to represent the imaginary part of the complex number
	 */
	DoubleDoubleComplex(DoubleDouble real,DoubleDouble imag) {	
		this.real = real;
		this.imag = imag;
	}
	
	/**
	 * Add the provided DoubleDoubleComplex number to this object.
	 * @param num A DoubleDoubleComplex number to add to this object.
	 */
	public void add(DoubleDoubleComplex num) {
		real = getReal().add(num.getReal());
		imag = getImag().add(num.getImag());

	}
	
	/**
	 * Multiplies this DoubleDouble object by the DoubleDouble value provided.
	 * 
	 * @param num the DoubleDouble value to multiply by
	 */
	public void multiply(DoubleDoubleComplex num) {
		//(2+5i)*(3+8i) need to use the FOIL method
		
				
				DoubleDouble F = getReal().multiply(num.getReal());		//will give some real number
				DoubleDouble O = getReal().multiply(num.getImag());		//will give some number*i
				DoubleDouble I = getImag().multiply(num.getReal());
				DoubleDouble L = getImag().multiply(num.getImag());		//will give some number*i^2, i^2 is the same as -1 so this number should be inverted
				
				//combine and simplify
				real = F.subtract(L);
				imag = O.add(I);
	}
	/**
	 * Squares this object.
	 */
	public void sqr() {
		DoubleDouble r = getReal().sqr().subtract(getImag().sqr());				//F - L
		DoubleDouble i = getImag().multiply(getReal()).multiply(DoubleDouble.valueOf(2));			//(O or I) *2
		real=r;
		imag=i;
		
	}
	
	/**
	 * Returns the absolute value of this complex number using the pythagorean theorem.
	 * @return The absolute value of this DoubleDoubleComplex number.
	 */
	public DoubleDouble abs() {
		
		DoubleDouble radicand = getReal().sqr().add((getImag()).sqr());
		DoubleDouble result = radicand.sqrt();
		
		return result;
	}
	
	public DoubleDouble getReal() {
		return real;
	}
	
	public DoubleDouble getImag() {
		return imag;
	}
	
	public void setReal(DoubleDouble value) {
		real = value;
	}
	
	public void setImag(DoubleDouble value) {
		imag = value;
	}
}
