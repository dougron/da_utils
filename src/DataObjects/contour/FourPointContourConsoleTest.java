package DataObjects.contour;
import acm.program.ConsoleProgram;

public class FourPointContourConsoleTest extends ConsoleProgram{

	
	public void run(){
		setSize(900, 700);
		FourPointContour fpc = new FourPointContour(0.4, -1.0, 0.7, 1.0, -1.0);
		println(fpc.toString());
		fpc.sort();
		println(fpc.toString());
		for (double d = 0.; d < 1.5; d = d + 0.1){
			println(d + ": " + fpc.getValue(d));
		}
	}
}
