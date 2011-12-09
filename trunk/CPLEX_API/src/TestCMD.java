import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;


public class TestCMD {

	public static void main(String[] args) {
		String s = null;
		 try {
//			 String[] cmds = {"cmd.exe","/k","cplex","read D:\\eclipse\\test.lp"};
//			 String[] cmds = {"cmd.exe","/k","ipconfig","/all"};
			 String[] cmds = {"cmd.exe","/k","start","cplex"};
			 Process p = Runtime.getRuntime().exec(cmds);
			
			 BufferedReader input = new BufferedReader(new InputStreamReader(p.getInputStream()));
			
			 while((s=input.readLine()) != null){
				 System.out.println(s);
			 }
			
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
}
