
public class Complex {
	double real;
	double imag;
	
	Complex(double real,double imag) {	
		this.real = real;
		this.imag = imag;
	}
	
	public void add(Complex num) {		//adds the provided value (num) to this object
		real = real + num.getReal();
		imag = imag + num.getImag();
	}
	
	public void multiply(Complex num) {		//multiples this object by the value provided
		real = real * num.getReal();
		imag = imag * num.getImag();
	}
	
	public void sqr() {		//squares this object
		real = real *real;
		imag = imag *imag;
	}
	
	public double abs() {			//returns the absolute value of this complex number using the pythagorean theorem
		double x = Math.abs(this.getReal());
		double y = Math.abs(this.getImag());
		
		double result = Math.sqrt(Math.pow(x,2) + Math.pow(y,2));
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
