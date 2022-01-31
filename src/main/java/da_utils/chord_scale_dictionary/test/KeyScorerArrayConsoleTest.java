package main.java.da_utils.chord_scale_dictionary.test;
import java.util.Iterator;

import main.java.da_utils.ableton_live.ableton_live_clip.LiveClip;
import main.java.da_utils.chord_scale_dictionary.ChordScaleDictionary;
import main.java.da_utils.chord_scale_dictionary.chord_analysis.ChordAnalysisObject;
import main.java.da_utils.chord_scale_dictionary.key_utils.Key;
import main.java.da_utils.chord_scale_dictionary.key_utils.KeyScorerArray;


public class KeyScorerArrayConsoleTest{

	
	public KeyScorerArrayConsoleTest(){
		ChordScaleDictionary csd = new ChordScaleDictionary();
		KeyScorerArray ksa = new KeyScorerArray(new Key[]{csd.keyList.get(0)});	
		LiveClip lc = makeLiveClip();
//		lc.makeChordAnalysis("8n");
//		Iterator<ChordAnalysisObject> it = lc.chunkArray.chunkArrayIterator();
//		while (it.hasNext()){
//			ksa.newChord(it.next());
//		}
		ksa.makeKeyAreas();
		System.out.println("--------------------------------------------------------------");
//		System.out.println(ksa.toString());
		System.out.println("--------------------------------------------------------------\nFinal Chord Progression Analysis:");
//		for (KeyArea ka: ksa.makeChordProgressionAnalysis()){
//			System.out.println(ka.toString());
//		}
		System.out.println(ksa.chordAnalysisToString());
	}
//	private LiveClip makeLiveClip(){
//		return TestDataTemp.liveClipForTestForm();
//	}
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
		lc.addNote(45, 8.0, 4.0, 100, 0);
		lc.addNote(48, 8.0, 4.0, 100, 0);
		lc.addNote(52, 8.0, 4.0, 100, 0);
		return lc;
	}	
	public static void main (String[] args)
	{
		new KeyScorerArrayConsoleTest();
	}
}
