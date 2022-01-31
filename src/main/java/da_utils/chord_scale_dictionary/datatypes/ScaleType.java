package main.java.da_utils.chord_scale_dictionary.datatypes;
/*
 * wrapper for chord type information
 */
public class ScaleType {
	
	public String scaleName;
	//public NotePattern notePattern;
	public int index;
	public int numberOfNotes;
	
	//public ChordScaleDictionary csd = new ChordScaleDictionary();
	
	public ScaleType(int ind, String name, int nonotes){
		if (ind == -1){
			setNullChordType();
		} else {
			index = ind;
			scaleName = name;
			numberOfNotes = nonotes;
			//notePattern = csd.getNotePatternFormIndex();
		}
	}
	public ScaleType (int ind){
		setNullChordType();
	}
	public String toString(){
		if (index == -1){
			return "null ScaleType";
		} else {
			return "index: " + index + "  chordName: " + scaleName + "  numberOfNotes: " + numberOfNotes;
		}	
	}

	private void setNullChordType(){
		index = -1;
		scaleName = "null ScaleType";
	}
}
