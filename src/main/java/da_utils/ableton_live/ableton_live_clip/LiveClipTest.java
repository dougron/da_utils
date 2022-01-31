package main.java.da_utils.ableton_live.ableton_live_clip;

import java.util.ArrayList;


import acm.program.ConsoleProgram;


public class LiveClipTest extends ConsoleProgram{
	
	
	public void run (){
		setSize(700, 700);
//		ChordScaleDictionary csd = new ChordScaleDictionary();
		liveClipTest();
//		syncopationTest();
	}
	
	private void liveClipTest() {
		LiveClip lc = makeLiveClip();
		println(lc.toString());
		lc.sortNoteList();
		println("============================================");
		println(lc.toString());
		println("============================================");
		ArrayList<LiveMidiNote> lmnList = lc.noteList("4n");
		for (LiveMidiNote lmn: lmnList){
			println(lmn.oneLineToString());
		}
	}

	private void syncopationTest(){
		LiveClip lc = makeSyncopatedLiveClip();
		println(lc.syncopationScore());
	}
	

	
	private LiveClip makeLiveClip(){
		LiveClip lc = new LiveClip(0, 0);
		
		lc.addNote(60, 0.1, 1.0, 100, 0);
		lc.addNote(64, 0.05, 1.0, 100, 0);
		lc.addNote(67, 0.2, 1.0, 100, 0);
		
		lc.addNote(55, 1.1, 1.0, 100, 0);
		lc.addNote(59, 1.2, 1.0, 100, 0);
		lc.addNote(62, 0.9, 1.0, 100, 0);
		lc.addNote(65, 1.002, 1.0, 100, 0);
		
		return lc;
	}
	private LiveClip makeSyncopatedLiveClip(){
		LiveClip lc = new LiveClip(0, 0);
		
		lc.addNote(60, 4.0, 1.5, 100, 0);
//		lc.addNote(64, 0.0, 1.0, 100, 0);
//		lc.addNote(67, 0.0, 1.0, 100, 0);
		
		lc.addNote(60, 5.33, 0.67, 100, 0);
//		lc.addNote(59, 6.0, 1.25, 100, 0);
//		lc.addNote(62, 7.25, 0.75, 100, 0);
//		lc.addNote(65, 8.2, 1.0, 100, 0);
		
		return lc;
	}
}
