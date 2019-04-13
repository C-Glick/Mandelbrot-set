import javax.swing.SwingWorker;

public class Tester{
	double threshold=Launcher.threshold;
	int limit=Launcher.limit;		//number of iterations to run before stopping
	
	
	public double test1(Complex c) {		//test for just one complex number, returns the number of iterations it took to reach the threshold. will be 0 if it surpass the limit
											//it is in the set if the number stays below the threshold until the iteration limit is reached
		Complex z = new Complex(0,0);
		double result=0;
		
		for (int i=1; i<=limit;	i++) {
			z.sqr();
			z.add(c);
			double value = z.abs();
			if (value>=threshold) {
				result = i;
				break;
			}
		}
		return result;
	}
	
	public double[][] test2(Complex topLeft, Complex bottomRight,int width,int height){			//takes in a range of complex numbers and returns a 2D array of the number of iterations to reach the threshold
		double[][] result = new double[width][height];
		
		limit = Launcher.limit;
		
		//finds the complex numbers based on the pixel numbers
		//then tests those numbers 
		for(int x=0; x<width; x++) {
			double real= ((bottomRight.getReal()-topLeft.getReal())/width)*x+topLeft.getReal();
			
			for(int y=0;y<height;y++) {
				double imag= -(((bottomRight.getImag()-topLeft.getImag())/height)*y+topLeft.getImag());
				result[x][y] = test1(new Complex(real,imag));
			}
			
		}
		
		
		return result;
	}
	

}
