package main.java.da_utils.xml_maker.voice_item;

import acm.program.ConsoleProgram;

public class VoiceItemConsoleTest extends ConsoleProgram {

	
	public void run(){
		setSize(700, 700);
		VoiceItem vi = new VoiceItem(0.0, 3.97, 1);
		for (double d = 0.0; d < 4.0; d += 0.1){
//			vi.start = d;
			println(d + ":- " + vi.start());
		}
	}
}
