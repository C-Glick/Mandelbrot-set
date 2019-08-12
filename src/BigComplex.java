import java.math.BigDecimal;
import java.math.MathContext;
import java.math.RoundingMode;

/**
 * The same as the complex class but uses the BigDecimal class to create high precision complex numbers.
 * @author Colton Glick
 *
 */
public class BigComplex {
	BigDecimal real;
	BigDecimal imag;
	static MathContext rMode = new MathContext(32, RoundingMode.HALF_UP);	     //Rounding mode to round towards "nearest neighbor" unless both neighbors are equidistant, in which case round up.
	
	/**
	 * Complex number example: (2+5i), real = 2, imag = 5.
	 * Constructor for a BigComplex object. Uses BigDecimal objects to create high Precision objects.
	 * when creating new BigDecimal objects, use strings rather than doubles for the best results, 
	 * 'new BigDecimal("48.02761");' not 'new BigDecimal(someDouble);'
	 * @param real	A BigDecimal object to represent the real part of the complex number
	 * @param imag	A BigDecimal object to represent the imaginary part of the complex number
	 */
	BigComplex(BigDecimal real,BigDecimal imag) {	
		this.real = real;
		this.imag = imag;
	}
	
	/**
	 * Add the provided BigComplex number to this object.
	 * @param num A BigComplex number to add to this object.
	 */
	public void add(BigComplex num) {
		real = getReal().add(num.getReal(), rMode);
		imag = getImag().add(num.getImag(), rMode);

	}
	
	/**
	 * Multiplies this BigDecimal object by the BigDecimal value provided.
	 * 
	 * @param num the BigDecimal value to multiply by
	 */
	public void multiply(BigComplex num) {
		//(2+5i)*(3+8i) need to use the FOIL method
		
				BigDecimal F = getReal().multiply(num.getReal(), rMode);		//will give some real number
				BigDecimal O = getReal().multiply(num.getImag(), rMode);		//will give some number*i
				BigDecimal I = getImag().multiply(num.getReal(), rMode);
				BigDecimal L = getImag().multiply(num.getImag(), rMode);		//will give some number*i^2, i^2 is the same as -1 so this number should be inverted
				
				//combine and simplify
				real = F.subtract(L, rMode);
				imag = O.add(I, rMode);
	}
	/**
	 * Squares this object.
	 */
	public void sqr() {
		BigDecimal r = getReal().pow(2, rMode).subtract(getImag().pow(2,rMode),rMode);				//F - L
		BigDecimal i = getImag().multiply(getReal(),rMode).multiply(BigDecimal.valueOf(2));			//(O or I) *2
		real=r;
		imag=i;
		
	}
	
	/**
	 * Returns the absolute value of this complex number using the pythagorean theorem.
	 * @return The absolute value of this BigComplex number.
	 */
	public BigDecimal abs() {
		
		BigDecimal radicand = (getReal().pow(2, rMode)).add((getImag()).pow(2, rMode), rMode);
		BigDecimal result = radicand.sqrt(rMode);
		
		return result;
	}
	
	public BigDecimal getReal() {
		return real;
	}
	
	public BigDecimal getImag() {
		return imag;
	}
	
	public void setReal(BigDecimal value) {
		real = value;
	}
	
	public void setImag(BigDecimal value) {
		imag = value;
	}
	
	/**
	 * Set the maximum number of digits used for all BigComplex calculations.
	 * (default is 128 digits)
	 * @param value an Integer, the maximum number of digits to use.
	 */
	public void setCalcPrecision(int value) {
		rMode = new MathContext(value, RoundingMode.HALF_UP);	    //Rounding mode to round towards "nearest neighbor" unless both neighbors are equidistant, in which case round up.
																	//Updates the rMode object (rounding mode).
																	//rMode is static so it changes for all BigComplex objects
		Launcher.rMode = rMode;			//also update rMode in the launcher as some math is also done there
		Launcher.tester.rMode = rMode;
	}
}
