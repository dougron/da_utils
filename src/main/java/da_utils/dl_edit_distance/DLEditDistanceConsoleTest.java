package main.java.da_utils.dl_edit_distance;

import acm.program.ConsoleProgram;

public class DLEditDistanceConsoleTest{

	public DLEditDistanceConsoleTest(){
//		setSize(700, 900);
//		String start = "A,B,C,D";
//		String end = "A,B,C,C,D";
		String start = "31,32,33,34,35";
		String end = "31,1,33,72,35";
		DLEditDistance dl = new DLEditDistance();
		dl.setString(start, end);
		System.out.println(dl.toString());
		System.out.println(dl.getShortestEditDistance());
	}
	
	
	public static void main(String[] args)
	{
		DLEditDistanceConsoleTest x = new DLEditDistanceConsoleTest();
	}
}
