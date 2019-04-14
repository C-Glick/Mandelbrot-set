import java.math.BigDecimal;
import java.math.MathContext;

/**
 * The same as the complex class but uses the BigDecimal class to create high precision complex numbers.
 * @author Colton Glick
 *
 */
public class ComplexLong {
	BigDecimal real;
	BigDecimal imag;
	
	/**
	 * Complex number example: (2+5i), real = 2, imag = 5.
	 * Constructor for a ComplexLong object. Uses BigDecimal objects to create high Precision objects.
	 * when creating new BigDecimal objects, use strings rather than doubles for the best results, 
	 * 'new BigDecimal("48.02761");' not 'new BigDecimal(someDouble);'
	 * @param real	A BigDecimal object to represent the real part of the complex number
	 * @param imag	A BigDecimal object to represent the imaginary part of the complex number
	 */
	ComplexLong(BigDecimal real,BigDecimal imag) {	
		this.real = real;
		this.imag = imag;
	}
	
	/**
	 * Add the provided ComplexLong number to this object.
	 * @param num A ComplexLong number to add to this object.
	 */
	public void add(ComplexLong num) {
		real = getReal().add(num.getReal());
		imag = getImag().add(num.getImag());

	}
	
	/**
	 * Multiplies this BigDecimal object by the BigDecimal value provided.
	 * 
	 * @param num the BigDecimal value to multiply by
	 */
	public void multiply(ComplexLong num) {
		//(2+5i)*(3+8i) need to use the FOIL method
		
				BigDecimal F = getReal().multiply(num.getReal());		//will give some real number
				BigDecimal O = getReal().multiply(num.getImag());		//will give some number*i
				BigDecimal I = getImag().multiply(num.getReal());
				BigDecimal L = getImag().multiply(num.getImag());		//will give some number*i^2, i^2 is the same as -1 so this number should be inverted
				L = L.negate();
				
				//combine and simplify
				real = F.add(L);
				imag = O.add(I);
	}
	/**
	 * Squares this object.
	 */
	public void sqr() {
		this.multiply(this);
	}
	
	/**
	 * Returns the absolute value of this complex number using the pythagorean theorem.
	 * @return The absolute value of this ComplexLong number.
	 */
	public BigDecimal abs() {
		
		BigDecimal radicand = (getReal().pow(2)).add((getImag()).pow(2));
		BigDecimal result = radicand.sqrt(MathContext.DECIMAL64);							//TODO: what should the scale be for this sqrt method?
		
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


}
