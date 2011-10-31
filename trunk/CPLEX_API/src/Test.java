
public class Test {
	public static void main(String[] args) {
		String s = "((((";
		s=s.replace("\u0028", "");
		System.out.println(s);
	}
}
