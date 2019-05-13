import java.util.concurrent.RecursiveAction;

public class BasicTest extends RecursiveAction{
	
	Complex c;
	int limit;
	double threshold;
	int x;
	int y;
	
	
	BasicTest (Complex c, int limit, double threshold, int x, int y){
		this.limit = limit;
		this.threshold = threshold;
		this.c = c;
		this.x = x;
		this.y = y;
	}

	@Override
	protected void compute() {
		Complex z = new Complex(0,0);
		int result=0;
		
		for (int i=1; i<=limit;	i++) {
			z.sqr();
			z.add(c);
			double value = z.abs();
			if (value>=threshold) {
				result = i;
				break;
			}
		}
		Launcher.resultsArray[x][y] = result;
		
	}

}
