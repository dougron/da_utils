package main.java.da_utils.ableton_live.ableton_live_clip;


import acm.program.ConsoleProgram;


public class LiveMidiNoteTest extends ConsoleProgram{
	

	public void run(){
		setSize(700, 700);

		LiveMidiNote n = new LiveMidiNote(25, 1.4f, 2.6f, 69, "8n");
		println(n.toString());
//		for (double d = 0.0; d < 5.0; d = d + 0.2){
//			println(d + ": " + n.makeQuantizedPosition(d, "4n"));
//		}
	}
}
