package main.java.da_utils.static_chord_scale_dictionary;

public interface NotePatternAnalysis {

	public int lowestNote();
	public int rootNote();
	public int rootNoteIndex();
	public int[] intervals();
	public int[] extendedChordToneIntervals();
	public String name();
	public int inversionIndex();
	public String inversionName();
	public void setLowestNote(int lowestNote);
	public void setRootNote(int rootNote);
	public void setRootNoteIndex(int rootNoteIndex);
	public void setIntervals(int[] arr);
	public void setIntervals(Integer[] arr);
	public void setExtendedChordToneIntervals(int[] arr);
	public void setName(String name);
	public void setInversionIndex(int i);
	public String toString();
	public String chordSymbolToString();
	public String chordSymbolAndInversionToString();
	
	public boolean isDominantType();
	public boolean isMinorType();
	public boolean isMajorType();
	public boolean isDiminishedType();		// diminished triad
	public boolean isHalfDiminishedType();	// obviously the quartad
	public boolean isDiminishedSeventhType();
	public boolean isNinthChord();
	public boolean isFlatNinth();
}
