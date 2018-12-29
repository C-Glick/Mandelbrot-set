
public class Tester {
	double threshold=2;
	int limit=50;		//number of iterations to run before stopping
	
	
	public boolean test1(Complex c) {		//test for just one complex number, returns true if it is in the Mandelbrot set.
											//it is in the set if the number stays below the threshold until the iteration limit is reached
		Complex z = new Complex(0,0);
		boolean result=true;
		
		for (int i=0; i<=limit;	i++) {
			z.sqr();
			z.add(c);
			double value = z.abs();
			System.out.println(value);
			if (value>=threshold) {
				result = false;
				break;
			}
		}
		return result;
	}
	
	public double[][] test2(Complex topLeft, Complex bottomRight,int width,int height){			//takes in a range of complex numbers and returns a 2D array of the number of iterations to reach the threshold
		double[][] result = new double[width][height];
		double stepX = (double) (bottomRight.getReal()-topLeft.getReal())/width;
		double stepY = (double) (topLeft.getImag() - bottomRight.getImag())/height;
		
		for(int x=0; x<width; x++) {
			for(int y=0;y<height;y++) {
				
			}
			
		}
		
		
		return result;
	}

}
