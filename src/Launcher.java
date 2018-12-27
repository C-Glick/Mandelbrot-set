
public class Launcher {

	public static void main(String[] args) {
		Complex a = new Complex(0,0.27);
		Tester tester = new Tester();
		Display display = new Display();
		display.init();
		System.out.println(a.abs());
		
		System.out.println(tester.test1(a));
	}

}
