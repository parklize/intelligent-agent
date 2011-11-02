
public class StringBufferTest {
	public static void change(StringBuffer sb){
		sb.append("aaa");
	}
	public static void main(String[] args) {
		StringBuffer sb = new StringBuffer("dddd");
		
		change(sb);
		System.out.println(sb);
	}
}
