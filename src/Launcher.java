
public class Launcher {

	public static void main(String[] args) {
		Complex a = new Complex(5,1);
		a.sqr();
		Complex b = new Complex(-32,-5);
		a.add(b);
		System.out.println(a.getReal()+""+a.getImag());
	}

}
