package main.java.da_utils.chord_scale_dictionary.chord_analysis;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;
import java.util.TreeSet;

import DataObjects.note_buffer.PlayedMidiNote;
import main.java.da_utils.ableton_live.ableton_live_clip.LiveMidiNote;
import main.java.da_utils.chord_scale_dictionary.ChordScaleDictionary;
import main.java.da_utils.chord_scale_dictionary.datatypes.NotePattern;
import main.java.da_utils.chord_scale_dictionary.datatypes.NotePatternChord;
import main.java.da_utils.static_chord_scale_dictionary.CSD;

/*
 * ChordAnalysisObject makes structural analysis of a group of notes as a chord. The KeyAnalyzer does chord function analysis,
 * and resolves things like whether it is a m7 or Maj6, or a m7b5 or min6. Therefore, any chord progression
 * should undergo key analysis if, for instance, you have instances of the above chords and you are 
 * doing a bass generation algorithm (repeated in KeyAnalysis class header)
 * 
 * This class is depreciated from 19 May 2017. Its activities have been taken over by the ChordChunk
 * and its relationship with the ChordChunkList and the CSD. 
 */

public class ChordAnalysisObject {
	
	public ArrayList<Integer> noteList = new ArrayList<Integer>();
	public TreeSet<Integer> noteListNoDuplicates = new TreeSet<Integer>();
	public ArrayList<Integer> modNoteList = new ArrayList<Integer>();
	public ArrayList<String> inversionList = new ArrayList<String>();
	public ArrayList<Integer[]> intInversionList = new ArrayList<Integer[]>();
	public NotePattern matchingNotePattern;
	public ChordScaleDictionary csd = new ChordScaleDictionary();
	
	public int lowestNote;
	public int lowestModNote;
	public int patternMatchIndex;
	public ArrayList<Integer> rootList = new ArrayList<Integer>();	// arraylist of roots for each item in matchingNotePattern.chordOptions

	public double simpleLength;
	public boolean hasLength = false;
	public double startPoint;
	
	public double beatLength = 1.0;
//	public boolean debug = false;
//	public ChordAnalysisObjectTest parent;
	
	public double lengthInChunkArray;
	private ChordAnalysisObject next = null;
	private ChordAnalysisObject previous = null;
	public boolean isDominantType = false;
	public boolean isMinorType = false;
	public boolean isMajorType = false;
	public boolean isDiminishedType = false;		// diminished triad
	public boolean isHalfDiminishedType = false;	// obviously the quartad
	public boolean isDiminishedSeventhType = false;

	public ChordAnalysisObject(ArrayList<PlayedMidiNote> nob){
		init(nob);
	}
	public ChordAnalysisObject(List<Integer> noteList){
		// takes a list of original notes values
		init(noteList);
	}


	public ChordAnalysisObject(ArrayList<PlayedMidiNote> nob, ArrayList<LiveMidiNote> lmList){
		// this instantiator allows the chord chunk to generate an length value by averaging the length of all items in lmList
		// also makes an average startpoint
		init(nob);
		makeSimpleLength(lmList);
		makeStartPoint(lmList);
		doTypeTest();

	}
	public void setNext(ChordAnalysisObject cao){
		this.next = cao;
	}
	public void setPrevious(ChordAnalysisObject cao){
		this.previous = cao;
	}
	public ChordAnalysisObject next(){
		return next;
	}
	public ChordAnalysisObject previous(){
		return previous;
	}

	public int[] easyChordData(){			// [root, chordType]
		if (rootList.size() == 0){
			return new int[]{-1, -1};
		} else {
			int[] chord = new int[2];
			chord[0] = rootList.get(0);
			chord[1] = matchingNotePattern.chordOptions.get(0).chordType.index;
			return chord;
		}		
	}
	public int[] rootPositionIntervals(){
		int[] iarr = csd.getNotePattern(chordTypeName(), 0);
//		int i = 
		return iarr;
	}


	private void doTypeTest() {
		int[] iarr = csd.getNotePattern(chordTypeName(), 0);
		boolean third = false;
		boolean minorThird = false;
		boolean minorSeventh = false;
		boolean perfectFifth = false;
		boolean flatFifth = false;
		boolean dimSeventh = false;
		for (int i: iarr){
			if (i == 4) third = true;
			if (i == 10) minorSeventh = true;
			if (i == 3) minorThird = true;
			if (i == 7) perfectFifth = true;
			if (i == 6) flatFifth = true;
			if (i == 9) dimSeventh = true;
		}
		if (third && minorSeventh)isDominantType = true;
		if (third && perfectFifth) isMajorType = true;
		if (minorThird && perfectFifth) isMinorType = true;
		
		if (minorThird && flatFifth && minorSeventh) isHalfDiminishedType = true;	// obviously the quartad
		if (minorThird && flatFifth && dimSeventh) isDiminishedSeventhType = true;
		if (minorThird && flatFifth && !dimSeventh) isDiminishedType = true;		// diminished triad (not part of diminished7th)
		
	}
	private void init(List<Integer> noteList2) {
		createNoteList(noteList2);
		init();
	}

	private void init(ArrayList<PlayedMidiNote> nob){
		createNoteList(nob);
		init();
	}


	private void init(){
		createModNoteList();
		createBothInversionLists();
		getInversionMatch();
		getRoot();
	}
	
	public String modNoteListToString(){		
		return makeNotePatternString(modNoteList);
	}
	public String inversionListToString(){
		String ret = "";
		for (String s: inversionList){
			ret = ret + s + "\n";
		}
		return ret;
	}
	
	public String matchingNotePatternToString(){
		String ret = "";
		if (matchingNotePattern == null){
			ret = "null matchingNotePattern";
		} else {
			ret = matchingNotePattern.toStringShort() + "\n";
			for (int i = 0; i < matchingNotePattern.chordOptions.size(); i++){
				String rootName = CSD.noteName(rootList.get(i));
				ret = ret + CSD.noteName(rootList.get(i)) + matchingNotePattern.chordOptions.get(i).chordType.chordName + "  ";				
				int inversion = ((patternMatchIndex * -1) + inversionList.size() + matchingNotePattern.chordOptions.get(i).inversion.index) % inversionList.size();
				ret = ret + CSD.inversionNameArr[inversion] + "\n";
				//				ret = ret + "inversionList.size(): " + inversionList.size() + " chordOption index: " + matchingNotePattern.chordOptions.get(i).inversion.index + "\n";
//				ret = ret + "patternMatchIndex: " + patternMatchIndex + "\n";
//				ret = ret + "inversion: " + csd.inversion.get(inversion).name + "\n";
			}
		}		
		return ret;			
	}
	public String noteListToString(){
		Iterator<Integer> it = noteList.iterator();
		String ret = "noteList: ";
		while(it.hasNext()){
			ret = ret + it.next() + ", ";
		}
		return ret;
	}
	public String noteListNoDuplicatesToString(){
		//Iterator<Integer> it = noteListNoDuplicates.iterator();
		String ret = "noteListNoDuplicates: ";
		for (Integer i: noteListNoDuplicates){
			ret += i + ", ";
		}
		return ret;
	}
	public String toString(){
		String ret = "";
		ret = ret + modNoteListToString() + "\n";
		ret = ret + inversionListToString() + "\n";
		ret = ret + noteListToString() + "\n";
		ret = ret + matchingNotePatternToString();
		if (hasLength){
			ret = ret + "length: " + simpleLength + " start: " + startPoint;
			ret = ret + "  " + slashChordToString();
		}
//		ret = ret + "pos: " + startPoint;						// reinventeth the wheel......
//		if (hasLength) ret = ret + " length: " + simpleLength;
		return ret;
	}
	public String rootListToString(){
		String ret = "rootList: ";
		for (int i: rootList){
			ret = ret + i + ", ";		
		}
		return ret;
	}
	public String slashChordToString(){
		// gives a string for representing chord progressions in slash notation
		if (hasLength){
			String ret = "";
			ret = ret + chordToString();
			for (double i = 1.0; i < simpleLength; i = i + beatLength){
				ret = ret + "/";
			}
			return ret;
		} else {
			return "not possible due to missing length value";
		}
		
	}
	public String chordToString(){
		if (matchingNotePattern.chordOptions.size() > 0){
//			System.out.println("root=" + rootList.get(0));
			return CSD.noteName(rootList.get(0)) + chordTypeName();				
		} else {
			return NO_CHORD;
		}
	}
	public String chordTypeName(){
		if (matchingNotePattern.chordOptions.size() == 0){
			return "noChord";
		} else {
			return matchingNotePattern.chordOptions.get(0).chordType.chordName;
		}
		
	}
 	public ArrayList<NotePatternChord> chordOptions(){
		return matchingNotePattern.chordOptions;
	}
	public int simpleRoot(){
		if (rootList.size() == 0){
			return -1;
		} else {
			return rootList.get(0);
		}
		
	}
	public String simpleChord(){
		return matchingNotePattern.simpleNotePatternChord().chordType.chordName;
	}
	public boolean isSameTypeAndRootAs(ChordAnalysisObject cao){
		String thisChord = this.chordToString();
		String lastChord = cao.chordToString();
		if (cao.simpleRoot() == this.simpleRoot()){
			if (	
					cao.isDiminishedSeventhType == this.isDiminishedSeventhType 
					&& cao.isDiminishedType == this.isDiminishedType
					&& cao.isDominantType == this.isDominantType
					&& cao.isHalfDiminishedType == this.isHalfDiminishedType
					&& cao.isMajorType == this.isMajorType
					&& cao.isMinorType == this.isMinorType){
				return true;
			} 
		}
		return false;
	}
	
	
// privates ########################################################################################
	
	private void makeSimpleLength(ArrayList<LiveMidiNote> lmList){
		double total = 0.0;
		for (LiveMidiNote lm: lmList){
			total += lm.length;
		}
		simpleLength = total / lmList.size();
		hasLength = true;
	}
	
	private void makeStartPoint(ArrayList<LiveMidiNote> lmList){
		double total = 0.0;
		for (LiveMidiNote lm: lmList){
			total += lm.position;
		}
		startPoint = total / lmList.size();
	}
	
	private String makeNotePatternString(ArrayList<Integer> arr){
		String ret = "";
		for (int i = 0; i < arr.size() - 1; i++){
			ret = ret + arr.get(i) + "-";
		}
		ret = ret + arr.get(arr.size() - 1);
		return ret;
	}
	
	private void getInversionMatch(){
		for (int i = 0; i < inversionList.size(); i++){
			if (csd.notePatternStringIndex.containsKey(inversionList.get(i))){
				matchingNotePattern = csd.notePatternStringIndex.get(inversionList.get(i));
				patternMatchIndex = i;
				break;
			}
		}
	}
	
	private void getRoot(){
		for (int i = 0; i < matchingNotePattern.chordOptions.size(); i++){
			NotePatternChord npc = matchingNotePattern.chordOptions.get(i);
			int rootIndex = (modNoteList.size() - npc.inversion.index) % inversionList.size();
			int rootWithoutModIndex = (rootIndex + patternMatchIndex) % inversionList.size();
			rootList.add((intInversionList.get(0)[rootWithoutModIndex] + lowestModNote) % 12);
		}

	}
	private void createInversionList(){
		inversionList.add(makeNotePatternString(modNoteList));
		ArrayList<Integer> temp = new ArrayList<Integer>();
		for (int i: modNoteList){
			temp.add(i);
		}
		for (int i = 1; i < modNoteList.size(); i++){
			int m = temp.get(0) + 12;
			temp.remove(0);
			temp.add(m);
			int sub = temp.get(0);
			for (int j = 0; j < temp.size(); j++){
				int tt = temp.get(j) - sub;
				temp.remove(j);
				temp.add(j, tt);
			}
			inversionList.add(makeNotePatternString(temp));
		}		
	}
	private void createBothInversionLists(){
		Integer[] temp = new Integer[modNoteList.size()];
		for (int i = 0; i < modNoteList.size(); i++){
			temp[i] = modNoteList.get(i);
		}
		intInversionList.add(temp);
		for (int i = 1; i < modNoteList.size(); i++){
			int offset = intInversionList.get(i - 1)[1];
			Integer[] ttemp = new Integer[modNoteList.size()];
			for (int j = 1; j < modNoteList.size(); j++){
				ttemp[j - 1] = intInversionList.get(i - 1)[j] - offset;
			}
			ttemp[modNoteList.size() - 1] = intInversionList.get(i - 1)[0] + 12 - offset;
			intInversionList.add(ttemp);
		}
		for (int i = 0; i < intInversionList.size(); i++){
			inversionList.add(makeNotePatternStringFromArray(intInversionList.get(i)));
		}
	}
	private String makeNotePatternStringFromArray(Integer[] arr){
		String ret = "";
		for (int i = 0; i < arr.length - 1; i++){
			ret = ret + arr[i] + "-";
		}
		ret = ret + arr[arr.length - 1];
		return ret;
	}
	
	private void createModNoteList(){
		for (int i = 0; i < noteList.size(); i++){
			int n = noteList.get(i) - lowestNote;
			if (n > 11){
				n = n % 12;
			}
			if (!modNoteList.contains(n)){
				modNoteList.add(n);
			}
		}
		Collections.sort(modNoteList);
	}
	
	private void createNoteList(ArrayList<PlayedMidiNote> nob){
		for (int i = 0; i < nob.size(); i++){
			noteList.add(nob.get(i).note);
			noteListNoDuplicates.add(nob.get(i).note);
		}
		Collections.sort(noteList);
		lowestNote = noteList.get(0);
		lowestModNote = lowestNote % 12;
	}
	private void createNoteList(List<Integer> noteList2) {
		for (int i = 0; i < noteList2.size(); i++){
			noteList.add(noteList2.get(i));
			noteListNoDuplicates.add(noteList2.get(i));
		}
		Collections.sort(noteList);
		lowestNote = noteList.get(0);
		lowestModNote = lowestNote % 12;
		
	}
	public static final String NO_CHORD = "noChord";
}
