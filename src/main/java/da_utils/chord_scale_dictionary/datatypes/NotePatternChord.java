package main.java.da_utils.chord_scale_dictionary.datatypes;

import main.java.da_utils.chord_scale_dictionary.ChordScaleDictionary;

/*
 * this class connects a chordType with a NotePattern
 */
public class NotePatternChord {
	public int index;
	public NotePattern notePattern;
	public ChordType chordType;
	public Inversion inversion;
	public ChordScaleDictionary csd = new ChordScaleDictionary();
	public static final ChordType nullChordType = new ChordType(0);	// 0 - arb value. not used in instantiating null ChordType
	
	public NotePatternChord(int ind, int npIndex, String cType, int invIndex){
		if (ind == -1){
			index = -1;
		} else {
			index = ind;
			notePattern = csd.getNotePatternFromIndex(npIndex);
			chordType = csd.getChordType(cType);
			inversion = csd.getInversionFromIndex(invIndex);
		}		
	}
	public NotePatternChord(int ind){
		index = -1;
		chordType = nullChordType;
	}
	public int[] notePatternArrInversion(int inversion){
		int[] arr = new int[notePattern.notePatternArr.length];
		int startIndex = inversion - this.inversion.index;
		for (int i = 0; i < arr.length; i++){
			if (startIndex < 0 || startIndex >= arr.length){
				if (startIndex < 0){
					arr[i] = notePattern.notePatternArr[startIndex + notePattern.notePatternArr.length] - 12;
				} else {
					arr[i] = notePattern.notePatternArr[startIndex - notePattern.notePatternArr.length] + 12;
				}
			} else {
				arr[i] = notePattern.notePatternArr[startIndex];
			}
			startIndex++;
		}
		int inc = arr[0];
		for (int i = 0; i < arr.length; i++){
			arr[i] -= inc;
		}
		return arr;
	}
	public int[] scalePatternArr(){
		return notePatternArrInversion(0);
	}
	private int[] copyOfNotePatternArr(){
		int[] arr = new int[notePattern.notePatternArr.length];
		int tempindex = 0;
		for (Integer i: notePattern.notePatternArr){
			arr[tempindex] = (int)i;
			tempindex++;
		}
		return arr;
	}
	public String toString(){
		if (index == -1){
			return "null NotePatternChord";
		} else {
			String ret = "Chord:  index:" + index + "\n";		
			ret = ret + "        chordType: " + chordType.toString() + "\n";
			ret = ret + "        notePattern: " + notePattern.toStringShort() + "\n";
			ret = ret + "        inversion: " + inversion.toString();
			return ret;
		}
		
	}

}
