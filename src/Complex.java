
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
		//(2+5i)*(3+8i) need to use the FOIL method
		
				double F = this.getReal() * num.getReal();	 //will give some real number
				double O = this.getReal() * num.getImag();  //will give some number*i
				double I = this.getImag() * num.getReal(); 	
				double L = this.getImag() * num.getImag();  //will give some number*i^2, i^2 is the same as -1 so this number should be inverted
				L = L * -1;
				
				//combine and simplify
				real = F + L;
				imag = O + I;
	}
	
	public void sqr() {		//squares this object		
		this.multiply(this);
		
	}
	
	public double abs() {			//returns the absolute value of this complex number using the pythagorean theorem
		
		double result = Math.sqrt(Math.pow(getReal(),2) + Math.pow(getImag(),2));
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
