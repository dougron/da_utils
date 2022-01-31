package main.java.da_utils.chord_scale_dictionary.datatypes;

import main.java.da_utils.chord_scale_dictionary.ChordScaleDictionary;

/*
 * this class connects a chordType with a NotePattern
 */
public class NotePatternScale {
	public int index;
	public NotePattern notePattern;
	public ScaleType scaleType;
	public int startDegree;
	public ChordScaleDictionary csd = new ChordScaleDictionary();
	
	public NotePatternScale(int ind, int npIndex, String sType, int sd){
		if (ind == -1){
			index = -1;
		} else {
			index = ind;
			notePattern = csd.getNotePatternFromIndex(npIndex);
			scaleType = csd.getScaleType(sType);
			startDegree = sd;
		}		
	}
	public NotePatternScale(int ind){
		index = -1;
	}

	public int[] scalePatternArr(){
		int[] arr = new int[notePattern.notePatternArr.length];
		Integer[] noteArr = notePattern.notePatternArr;
		int startIndex = this.startDegree;
		int dec = noteArr[startDegree];
		for (int i = 0; i < arr.length; i++){
			int writeIndex = startIndex % arr.length;
			int inc = 12 * (startIndex / arr.length);
			arr[i] = notePattern.notePatternArr[startIndex % arr.length] + (12 * (startIndex / arr.length)) - dec;
			startIndex++;					
		}
		return arr;
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
			String ret = "Scale:  index:" + index + "\n";		
			ret = ret + "        scaleType: " + scaleType.toString() + "\n";
			ret = ret + "        notePattern: " + notePattern.toStringShort() + "\n";
			ret = ret + "        startDegree: " + startDegree;
			return ret;
		}
		
	}

}