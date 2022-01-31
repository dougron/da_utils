package LegacyStuff;
import acm.program.ConsoleProgram;
import main.java.da_utils.resource_objects.TwoBarRhythmBuffer;


public class RBRandomInterlockConsoleTest extends ConsoleProgram{
	
	public void run(){
		setSize(900, 700);
		TwoBarRhythmBuffer rb = new TwoBarRhythmBuffer();
		makeRhythm(new int[] {}, rb);
		println(rb.buffyToString());
		RBRandomInterlock ri = new RBRandomInterlock();
		println(ri.makeInterlock(rb).buffyToString());
	}
	private void makeRhythm(int[] arr, TwoBarRhythmBuffer rb){
		for (int i: arr){
			rb.set(i, 1);
		}
	}

}
