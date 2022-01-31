package main.java.da_utils.static_chord_scale_dictionary;

public class NP_Interval implements NotePatternAnalysis {

	private int lowestNote;
	private boolean hasLowestNote = false;
	private int rootNoteIndex;
	private boolean hasRootNoteIndex = false;
	private int rootNote;
	private boolean hasRootNote = false;
	private int[] intervals;						// interval structure that, together with the lowestNote, will recreate the correct inversion
	private boolean hasIntervals = false;
	private int inversionIndex;
	private boolean hasInversionIndex = false;
	private String name;
	private boolean hasName = false;

	@Override
	public int lowestNote() {
		return lowestNote;
	}

	@Override
	public int rootNote() {
		return rootNote;
	}

	@Override
	public int[] intervals() {
		return intervals;
	}

	@Override
	public String name() {
		return name;
	}

	@Override
	public void setLowestNote(int lowestNote) {
		this.lowestNote = lowestNote;
		hasLowestNote = true;
	}

	@Override
	public void setRootNote(int rootNote) {
		this.rootNote = rootNote;
		hasRootNote = true;
	}

	@Override
	public void setIntervals(int[] arr) {
		this.intervals = arr;
		hasIntervals = true;
	}

	@Override
	public void setName(String name) {
		this.name = name;
		hasName = true;
	}


	@Override
	public int rootNoteIndex() {
		return rootNoteIndex;
	}

	@Override
	public void setRootNoteIndex(int rootNoteIndex) {
		this.rootNoteIndex = rootNoteIndex;
		this.hasRootNoteIndex = true;
	}

	@Override
	public void setIntervals(Integer[] arr) {
		intervals = new int[arr.length];
		for (int i = 0; i < arr.length; i++){
			intervals[i] = arr[i];
		}
		hasIntervals = true;
	}
	public String toString(){
		String str = "---\nNP_Interval instance of NotePatternAnalysis";
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
	public int inversionIndex() {
		return inversionIndex;
	}

	@Override
	public String inversionName() {
		return CSD.inversionName(inversionIndex);
	}

	@Override
	public void setInversionIndex(int i) {
		inversionIndex = i;
		hasInversionIndex = true;
		
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
