package main.java.da_utils.four_point_contour;
import acm.program.ConsoleProgram;

public class FourPointContourConsoleTest
{

	
	public FourPointContourConsoleTest()
	{
		FourPointContour fpc = new FourPointContour(0.4, -1.0, 0.7, 1.0, -1.0);
		System.out.println(fpc.toString());
		fpc.sort();
		System.out.println(fpc.toString());
		for (double d = 0.; d < 1.5; d = d + 0.1){
			System.out.println(d + ": " + fpc.getValue(d));
		}
	}
	
	
	public static void main(String[] args)
	{
		new FourPointContourConsoleTest();
	}
}
