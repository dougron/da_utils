package main.java.da_utils.chord_scale_dictionary.test;
import java.util.Iterator;
import java.util.Random;

import acm.program.ConsoleProgram;
import main.java.da_utils.ableton_live.ableton_live_clip.LiveClip;
import main.java.da_utils.chord_scale_dictionary.ChordScaleDictionary;
import main.java.da_utils.chord_scale_dictionary.chord_analysis.ChordAnalysisObject;
import main.java.da_utils.chord_scale_dictionary.key_utils.Key;
import main.java.da_utils.chord_scale_dictionary.key_utils.KeyScorerArray;


public class KeyScorerArrayRandomProgressionTest{
	
	public int numberOfProgressions = 100;
	public int numberOfChords = 8;
	public double chordLength = 4.0;
	public Random rnd = new Random();
	
	public KeyScorerArrayRandomProgressionTest(){
		ChordScaleDictionary csd = new ChordScaleDictionary();
		for (int i = 0; i < numberOfProgressions; i++){
			KeyScorerArray ksa = new KeyScorerArray(new Key[]{csd.keyList.get(0)});	
			LiveClip lc = makeLiveClip();
//			lc.makeChordAnalysis("8n");		// this is the problematic line as this has been updated to use the StaticChordScaleDictionary
//			Iterator<ChordAnalysisObject> it = lc.chunkArray.chunkArrayIterator();
//			while (it.hasNext()){
//				ksa.newChord(it.next());
//			}
			ksa.makeKeyAreas();
			System.out.println("--------------------------------------------------------------");
//			System.out.println(ksa.toString());
//			System.out.println("--------------------------------------------------------------\nFinal Chord Progression Analysis:");
//			for (KeyArea ka: ksa.makeChordProgressionAnalysis()){
//				System.out.println(ka.toString());
//			}
			System.out.println(ksa.chordAnalysisToString());
		}
	}
	
	private LiveClip makeLiveClip(){
		LiveClip lc = new LiveClip(0, 0);
		double pos = 0.0;
		for (int i = 0; i < numberOfChords; i++){
			int root = rnd.nextInt(12);
			int chordStructOptionIndex = rnd.nextInt(chordStructOptions.length);
			for (int offset: chordStructs[chordStructOptions[chordStructOptionIndex]]){
				lc.addNote(root + offset, pos, chordLength, default_velocity, 0);
			}
			pos += chordLength;
		}
		return lc;
	}
	
	private int default_velocity = 100;
	private int[][] chordStructs = new int[][]{
			new int[]{0, 4, 7},						// major
			new int[]{0, 3, 7},						// minor
			new int[]{0, 3, 6}						// diminished
	};
	private int[] chordStructOptions = new int[]{0,0,0,0,1,1,1,1,2};

	public static void main (String[] args)
	{
		new KeyScorerArrayRandomProgressionTest();
	}
}
