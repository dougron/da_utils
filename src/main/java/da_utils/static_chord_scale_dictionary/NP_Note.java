package main.java.da_utils.static_chord_scale_dictionary;

public class NP_Note implements NotePatternAnalysis {
	
	private int lowestNote;
	private boolean hasLowestNote = true;
	private int rootNoteIndex;
	private boolean hasRootNoteIndex = true;
	private int rootNote;
	private boolean hasRootNote = true;
	private int[] intervals = new int[]{0};
	private boolean hasIntervals = true;
	private int inversionIndex = -1;
	private boolean hasInversionIndex = true;
	private String name = "note";
	private boolean hasName = true;

	
	public NP_Note(){
		
	}
	@Override
	public int lowestNote() {
		// TODO Auto-generated method stub
		return lowestNote;
	}

	@Override
	public int rootNote() {
		// TODO Auto-generated method stub
		return rootNote;
	}

	@Override
	public int rootNoteIndex() {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public int[] intervals() {
		// TODO Auto-generated method stub
		return intervals;
	}

	@Override
	public String name() {
		// TODO Auto-generated method stub
		return name;
	}

	@Override
	public void setLowestNote(int note) {
		lowestNote = note;
		rootNote = note % 12;

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
	public int inversionIndex() {
		return inversionIndex;
	}

	@Override
	public String inversionName() {
		return CSD.inversionName(inversionIndex);
	}

	@Override
	public void setInversionIndex(int i) {
		
		
	}
	public String toString(){
		String str = "---\nNP_Note instance of NotePatternAnalysis";
		if (hasLowestNote){
			str += "\nlowestNote=" + lowestNote + ", " + CSD.noteName(lowestNote);
		} else {		
			str += "\nlowestNote not set";
		}
		if (hasRootNoteIndex){
			str += "\nrootNoteIndex=" + rootNoteIndex;
		} else {
			str += "\nrootNoteIndex not set";
		}
		if (hasRootNote){
			str += "\nrootNote=" + rootNote + ", " + CSD.noteName(rootNote);
		} else {
			str += "\nrootNote not set";
		}
		if (hasInversionIndex){
			str += "\ninversionIndex=" + inversionIndex + ", " + inversionName();
		} else {
			str += "\ninversionIndex not set";
		}
		if (hasName){
			str += "\nname=" + name;
		} else {
			str += "\nname not set";
		}
		if (hasIntervals){
			str += "\nintervals: ";
			for (int i: intervals){
				str += i + ", ";
			}
		} else {
			str += "\nno intervals set";
		}
		
		
		return str;
	}
	@Override
	public String chordSymbolToString() {
		return CSD.noteName(rootNote) + name;
	}
	@Override
	public String chordSymbolAndInversionToString() {
		return CSD.noteName(rootNote) + name + " " + inversionName();
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
	{}
	
}
