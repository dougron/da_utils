package main.java.da_utils.chord_scale_dictionary.test;
//import java.util.Iterator;

import java.nio.file.Path;
import java.nio.file.Paths;

import main.java.da_utils.ableton_live.ableton_live_clip.LiveClip;
import main.java.da_utils.chord_scale_dictionary.ChordScaleDictionary;
import main.java.da_utils.chord_scale_dictionary.datatypes.ChordFunction;
import main.java.da_utils.chord_scale_dictionary.datatypes.NotePattern;
import main.java.da_utils.chord_scale_dictionary.datatypes.NotePatternScale;
import main.java.da_utils.chord_scale_dictionary.key_utils.Key;



public class ChordScaleDictionaryTest{

	public ChordScaleDictionary csd;
	
	public ChordScaleDictionaryTest(){

		bigTest();
		chordScaleDictMultiLoadTest();
		chordTest();
		scaleTest();
		chordIndexTest();
		keyTest();
		chordAnalysisTest();
		keyList();
		chordIDTest();
//		pathTest();
	}
	private void pathTest (){
		Path currentDir = Paths.get("."); // currentDir = "."
		Path fullPath = currentDir.toAbsolutePath(); // fullPath =
														// "/Users/guest/workspace"
		System.out.println(fullPath);
//		Path one = currentDir.resolve("file.txt"); // one = "./file.txt"
//		Path fileName = one.getFileName(); // fileName = "file.txt"
		
	}
	private void chordIDTest() {
		csd = new ChordScaleDictionary(this);
		LiveClip lc = new LiveClip(0, 0);
		lc.addNote(60, 0.0, 4.0, 100, 0);
		lc.addNote(64, 0.0, 4.0, 100, 0);
		lc.addNote(67, 0.0, 4.0, 100, 0);
		
	}
	public void keyList(){
		csd = new ChordScaleDictionary(this);
		for (Key key: csd.keyList){
			System.out.println(key.toString());
			for (ChordFunction cf: key.cfList){
				System.out.println("    " + cf.toString());
			}
		}
	}
	public void chordAnalysisTest(){
		csd = new ChordScaleDictionary();
		LiveClip lc = makeLiveClip();
		System.out.println(lc.toString());

		//		System.out.println(lc.slashChordsToString());
//		lc.makeChordAnalysis("8n");
//		System.out.println(lc.chunkArrayToString());
//		csd.analyzeChordProgression(lc, "8n");

		// the statements commented out are no longer relevant to the LiveClip
//		Iterator<ChordAnalysisObject> it = lc.chunkArray.chunkArrayIterator();
//		int[][] primaryScore = new int[12][lc.chunkArray.size()];
//		int index = 0;
//		while (it.hasNext()){
//			System.out.println("--------------------------------------------");
//			ChordAnalysisObject cao = it.next();
//			System.out.println(cao.chordToString() + " " + cao.simpleRoot() + " " + cao.matchingNotePattern.simpleNotePatternChord().chordType.chordName);
//			for (int root = 0; root < 12; root++){				// all instances of 12 refer to the 12 tone system
//				System.out.println(csd.noteName(root));
//				Key key = csd.keyList.get(0);
//				for (ChordFunction cf: key.cfList){
//					int degree = (root + cf.degree) % 12;
//					System.out.println("   " + csd.noteName(degree) + cf.chordType);
//					if (degree == cao.simpleRoot() && cf.chordType.equals(cao.simpleChord())){
//						System.out.println(cf.name + " in " + csd.noteName(root) + " " + key.name);
//						primaryScore[root][index] = cf.primaryScore;
//					}
//				}
//			}
//			index++;
//		}
//		System.out.println(makeArrayOut(primaryScore));
//		System.out.println(bestOptions(primaryScore));
	}
	private String bestOptions(int[][] arr){
		int [] totalArr = new int[arr.length];
		int best = 0;
		for (int i = 0; i < totalArr.length; i++){
			int x = total(arr[i]);
			if (x > best){
				best = x;
			}
			totalArr[i] = x;
 		}
		String ret = "";
		for (int i = 0; i < totalArr.length; i++){
			if (totalArr[i] == best){
				ret = ret + csd.noteName(i);
			}
		}
		return ret;
	}
	private int total(int[] arr){
		int total = 0;
		for (int i: arr){
			total += i;
		}
		return total;
	}
	private String makeArrayOut(int[][] arr){
		String ret = "";
		for (int i = 0; i < arr.length; i++){
			for (int j = 0; j < arr[0].length; j++){
				ret = ret + arr[i][j] + "";
			}
			ret = ret + "\n";
		}
		return ret;
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
	public void keyTest(){
		// test new key and chordFunction data
		csd = new ChordScaleDictionary(this);
		System.out.println(csd.toString());
	}
	
	public void chordIndexTest(){
		csd = new ChordScaleDictionary();
		System.out.println("Maj " + csd.chordIndexFromName("Maj"));
		System.out.println("dim " + csd.chordIndexFromName("dim"));
	}
	public void scaleTest(){
		csd = new ChordScaleDictionary(this);
//		System.out.println(csd.getNotePatternFromIndex(280).toString());
		for (NotePatternScale nps: csd.scale){
			System.out.println(nps.toString());
		}
		int[] arr = csd.getScalePattern("NatMinor");
		for (int i: arr){
			System.out.println(i);
		}
	}
	public void chordTest(){
		csd = new ChordScaleDictionary();
//		System.out.println(csd.chord.get(0).toString());
//		System.out.println(csd.getNotePatternFromIndex(23));
		NotePattern np = csd.getNotePatternFromIndex(23);
		System.out.println(np.toString());
		Integer[] noteArr = np.notePatternArr;
		for (Integer i: noteArr){
			System.out.println(i);
		}
		csd.getNotePattern("7", 3);
		
	}
	public void chordScaleDictMultiLoadTest(){
		System.out.println("chordScaleDictMultiLoadTest()");
		ChordScaleDictionary c1 = new ChordScaleDictionary(this);
		System.out.println("first one instantiated....");
		ChordScaleDictionary c2 = new ChordScaleDictionary(this);
		System.out.println("second one instantiated....");
	}
	public void bigTest(){
		// this tests the ability of the ChordScaleDictionary to update its information...
		csd = new ChordScaleDictionary(this);
//		System.out.println("qmodel highestIndex: " + csd.tfr.data.get("qmodel").getHighestIndex());
//		csd.addToRawData("qmodel", "4,24n,240,3");		
//		System.out.println(csd.tfr.toString());

//		System.out.println("qmodel highestIndex: " + csd.tfr.data.get("qmodel").getHighestIndex());
//		System.out.println("noteIndex: 21 is " + csd.noteName(21));
//		System.out.println("noteIndexName: 7 is " + csd.noteIndexName(7));

//		System.out.println(csd.toString());
//		System.out.println("16n onTickValue: " + csd.qmodelOnTickValue("16n"));
//		System.out.println("8t tickValue: " + csd.qmodelTickValue("8t"));
//		csd.writeToSaveFile();
		System.out.println("Done!");
	}
	public void consolePrint(String str){
		System.out.println(str);
	}
	
	
	public static void main (String[] args)
	{
		new ChordScaleDictionaryTest();
	}
}
