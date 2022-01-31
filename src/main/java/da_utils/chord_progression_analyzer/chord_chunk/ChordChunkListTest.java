package main.java.da_utils.chord_progression_analyzer.chord_chunk;

import acm.program.ConsoleProgram;
import main.java.da_utils.ableton_live.ableton_live_clip.LiveClip;





public class ChordChunkListTest extends ConsoleProgram{
	
	public void run(){
		setSize(700, 700);
		LiveClip lc = makeLiveClip();
		println (lc.toString());
		println("======================================");
		ChordChunkList cca = new ChordChunkList("2n", lc);
		println(cca.toString());
//		println (lc.chunkArrayToString());
//		for (double d = 0.0; d < 20.0; d++){
//			println("pos " + d + " chord " + lc.chunkArray.getPrevailingChord(d)); 
//		}
	}
	
	private LiveClip makeLiveClip(){
		LiveClip lc = new LiveClip(0, 0);
		lc.loopStart = 0.0;
		lc.loopEnd = 8.0;
		
		lc.addNote(60, 0.0, 2.0, 100, 0);
		lc.addNote(64, 0.0, 2.0, 100, 0);
		lc.addNote(67, 0.0, 2.0, 100, 0);
		lc.addNote(69, 0.0, 2.0, 100, 0);
		
		lc.addNote(55, 2.0, 2.0, 100, 0);
		lc.addNote(59, 2.0, 2.0, 100, 0);
		lc.addNote(62, 2.0, 2.0, 100, 0);
		lc.addNote(65, 2.0, 2.0, 100, 0);
		lc.addNote(69, 2.0, 2.0, 100, 0);
		
		return lc;
	}

}
