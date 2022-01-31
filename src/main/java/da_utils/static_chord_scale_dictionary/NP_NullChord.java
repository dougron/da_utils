package main.java.da_utils.static_chord_scale_dictionary;

public class NP_NullChord implements NotePatternAnalysis {

	@Override
	public int lowestNote() {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public int rootNote() {
		// TODO Auto-generated method stub
		return -1;
	}

	@Override
	public int rootNoteIndex() {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public int[] intervals() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public String name() {
		// TODO Auto-generated method stub
		return "noChord";
	}

	@Override
	public int inversionIndex() {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public String inversionName() {
		// TODO Auto-generated method stub
		return "";
	}

	@Override
	public void setLowestNote(int lowestNote) {
		// TODO Auto-generated method stub

	}

	@Override
	public void setRootNote(int rootNote) {
		// TODO Auto-generated method stub

	}

	@Override
	public void setRootNoteIndex(int rootNoteIndex) {
		// TODO Auto-generated method stub

	}

	@Override
	public void setIntervals(int[] arr) {
		// TODO Auto-generated method stub

	}

	@Override
	public void setIntervals(Integer[] arr) {
		// TODO Auto-generated method stub

	}

	@Override
	public void setName(String name) {
		// TODO Auto-generated method stub

	}

	@Override
	public void setInversionIndex(int i) {
		// TODO Auto-generated method stub

	}

	@Override
	public String chordSymbolToString() {
		// TODO Auto-generated method stub
		return name();
	}

	@Override
	public String chordSymbolAndInversionToString() {
		// TODO Auto-generated method stub
		return name();
	}

	@Override
	public boolean isDominantType() {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean isMinorType() {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean isMajorType() {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean isDiminishedType() {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean isHalfDiminishedType() {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean isDiminishedSeventhType() {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean isNinthChord() {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean isFlatNinth() {
		// TODO Auto-generated method stub
		return false;
	}
	
	
	@Override
	public int[] extendedChordToneIntervals()
	{
		return new int[] {};
	}

	
	@Override
	public void setExtendedChordToneIntervals(int[] arr)
	{
		
	}
}
