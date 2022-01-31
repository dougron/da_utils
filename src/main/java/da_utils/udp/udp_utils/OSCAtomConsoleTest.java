package main.java.da_utils.udp.udp_utils;
import acm.program.ConsoleProgram;


public class OSCAtomConsoleTest extends ConsoleProgram{
	
	public void run(){
		setSize(700, 700);
		int i = 256;
		double d = 100.0;
		String s = "aaaa";
		OSCAtom at = new OSCAtom(s);
		int index = 0;
		for (byte b: at.getByteArray()){
			println(index + " - " + b);
			index++;
		}
		println(at.byteFormat());
	}
	
// privates -------------------------------------------------------
	public void getByte(){
		String poopy = "ifds";
		byte[] bbb = poopy.getBytes();
		for (byte b: bbb){
			println(b);
		}
	}

}
