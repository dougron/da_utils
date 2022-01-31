package main.java.da_utils.chord_scale_dictionary.datatypes;
/*
 * wrapper for chord type information
 */
public class ChordType {
	
	public String chordName;
	//public NotePattern notePattern;
	public int index;
	public int numberOfNotes;
	
	//public ChordScaleDictionary csd = new ChordScaleDictionary();
	
	public ChordType(int ind, String name, int nonotes){
		if (ind == -1){
			setNullChordType();
		} else {
			index = ind;
			chordName = name;
			numberOfNotes = nonotes;
			//notePattern = csd.getNotePatternFormIndex();
		}
	}
	public ChordType (int ind){
		setNullChordType();
	}
	public String toString(){
		if (index == -1){
			return "null ChordType";
		} else {
			return "index: " + index + "  chordName: " + chordName + "  numberOfNotes: " + numberOfNotes;
		}	
	}

	private void setNullChordType(){
		index = -1;
		chordName = "null chordType";
	}
}

