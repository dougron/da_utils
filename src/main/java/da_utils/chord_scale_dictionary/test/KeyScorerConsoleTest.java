package main.java.da_utils.chord_scale_dictionary.test;
import java.util.ArrayList;
import java.util.Iterator;

import acm.program.ConsoleProgram;
import main.java.da_utils.ableton_live.ableton_live_clip.LiveClip;
import main.java.da_utils.chord_scale_dictionary.ChordScaleDictionary;
import main.java.da_utils.chord_scale_dictionary.chord_analysis.ChordAnalysisObject;
import main.java.da_utils.chord_scale_dictionary.key_utils.KeyArea;
import main.java.da_utils.chord_scale_dictionary.key_utils.KeyScorer;


public class KeyScorerConsoleTest{
	
	public KeyScorerConsoleTest(){

		ChordScaleDictionary csd = new ChordScaleDictionary();
		KeyScorer ks = new KeyScorer(4, csd.keyList.get(0), this);	// c, major
		LiveClip lc = makeLiveClip();
//		lc.makeChordAnalysis("8n");		// depreciated for LiveClip, commented out to remove problem list item
		System.out.println(lc.toString());
		
		// depreciated for LiveClip, commented out to remove problem list item
		
//		System.out.println(lc.chunkArrayToString());
//		Iterator<ChordAnalysisObject> it = lc.chunkArray.chunkArrayIterator();
//		while (it.hasNext()){
//			ks.addChordAnalysisObject(it.next());
//		}
		
		System.out.println("--------------------------------------------------------------");
		System.out.println(ks.toString());
		ArrayList<KeyArea> kaList = ks.makeKeyAreas();
		for (KeyArea ka: kaList){
			System.out.println(ka.toString());
		}
 	}
	
	private LiveClip makeLiveClip(){
		LiveClip lc = new LiveClip(0, 0);
		lc.length = 12.0;
		lc.startMarker = 0.0;
		lc.endMarker = 12.0;
		lc.addNote(40, 0.0, 4.0, 100, 0);
		lc.addNote(44, 0.0, 4.0, 100, 0);
		lc.addNote(47, 0.0, 4.0, 100, 0);
		lc.addNote(45, 4.0, 4.0, 100, 0);
		lc.addNote(49, 4.0, 4.0, 100, 0);
		lc.addNote(52, 4.0, 4.0, 100, 0);
		lc.addNote(41, 8.0, 4.0, 100, 0);
		lc.addNote(45, 8.0, 4.0, 100, 0);
		lc.addNote(48, 8.0, 4.0, 100, 0);
		return lc;
	}
	public void consolePrint(String str){
		System.out.println(str);
	}
	public static void main (String[] args)
	{
		new KeyScorerConsoleTest();
	}
}
