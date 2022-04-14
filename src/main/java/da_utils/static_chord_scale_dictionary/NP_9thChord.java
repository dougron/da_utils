package main.java.da_utils.static_chord_scale_dictionary;

public class NP_9thChord implements NotePatternAnalysis {
	
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
	
	private boolean isDominantType = false;
	private boolean isMinorType = false;
	private boolean isMajorType = false;
	private boolean isDiminishedType = false;		// diminished triad
	private boolean isHalfDiminishedType = false;	// obviously the quartad
	private boolean isDiminishedSeventhType = false;
	private boolean isFlatNinth = false;
	private int[] extendeChordToneIntervals;

	@Override
	public int lowestNote() 
	{
		return lowestNote;
	}

	
	
	@Override
	public int rootNote() 
	{
		return rootNote;
	}

	@Override
	public int[] intervals() {
		return intervals;
	}

	
	
	@Override
	public String name() 
	{
		return name;
	}

	
	
	@Override
	public void setLowestNote(int lowestNote) 
	{
		this.lowestNote = lowestNote;
		hasLowestNote = true;
	}

	
	
	@Override
	public void setRootNote(int rootNote) 
	{
		this.rootNote = rootNote;
		hasRootNote = true;
	}

	
	
	@Override
	public void setIntervals(int[] arr) 
	{
		this.intervals = arr;
		hasIntervals = true;
		doTypeTest();
	}

	

	@Override
	public void setName(String name) 
	{
		this.name = name;
		hasName = true;
	}


	
	@Override
	public int rootNoteIndex() 
	{
		return rootNoteIndex;
	}

	
	
	@Override
	public void setRootNoteIndex(int rootNoteIndex) 
	{
		this.rootNoteIndex = rootNoteIndex;
		this.hasRootNoteIndex = true;
	}

	
	
	@Override
	public void setIntervals(Integer[] arr) 
	{
		intervals = new int[arr.length];
		// this only ever has 5 notes....
		intervals[0] = arr[0];
		intervals[1] = arr[2];
		intervals[2] = arr[3];
		intervals[3] = arr[4];
		intervals[4] = arr[1] + 12;
		hasIntervals = true;
		doTypeTest();
	}
	
	
	
	@Override
	public int inversionIndex() 
	{
		return inversionIndex;
	}

	
	
	@Override
	public String inversionName() 
	{
		return CSD.inversionName(inversionIndex);
	}

	
	
	@Override
	public void setInversionIndex(int i) 
	{
		inversionIndex = i;
		hasInversionIndex = true;
	}
	
	
	
	public String toString()
	{
		StringBuilder sb = new StringBuilder();
		sb.append("---\nNP_Triad instance of NotePatternAnalysis");
		sb.append(getLowestNoteToString());
		sb.append(getRootNoteIndexToString());
		sb.append(getRootNoteToString());
		sb.append(getInversionIndexToString());
		sb.append(getNameToString());
		sb.append(getIntervalsToString());
		
		return sb.toString();
	}



	private String getIntervalsToString()
	{
		StringBuilder sb = new StringBuilder();
		if (hasIntervals){
			sb.append("\nintervals: ");
			for (int i: intervals) sb.append(i + ", ");
			sb.append("\nisDominantType=" + isDominantType);
			sb.append("\nisMinorType=" + isMinorType);
			sb.append("\nisMajorType=" + isMajorType);
			sb.append("\nisDiminishedType=" + isDiminishedType);
			sb.append("\nisHalfDiminishedType=" + isHalfDiminishedType);
			sb.append("\nisDiminishedSeventhType=" + isDiminishedSeventhType);
		} 
		else 
		{
			sb.append("\nno intervals set");
		}
		return sb.toString();
	}



	private String getNameToString()
	{
//		String str;
//		if (hasName){
//			str = "\nname=" + name;
//		} else {
//			str = "\nname not set";
//		}
//		return str;
		return hasName ? "\nname=" + name : "\nname not set";
	}



	private String getInversionIndexToString()
	{
//		String str;
//		if (hasInversionIndex){
//			str = "\ninversionIndex=" + inversionIndex + ", " + inversionName();
//		} else {
//			str = "\ninversionIndex not set";
//		}
//		return str;
		return hasInversionIndex 
				? "\ninversionIndex=" + inversionIndex + ", " + inversionName() 
				: "\ninversionIndex not set";
	}



	private String getRootNoteToString()
	{
//		String str;
//		if (hasRootNote){
//			str = "\nrootNote=" + rootNote + ", " + CSD.noteName(rootNote);
//		} else {
//			str = "\nrootNote not set";
//		}
//		return str;
		return hasRootNote 
				? "\nrootNote=" + rootNote + ", " + CSD.noteName(rootNote)
				: "\nrootNote not set";
	}



	private String getRootNoteIndexToString()
	{
//		String str;
//		if (hasRootNoteIndex){
//			str = "\nrootNoteIndex=" + rootNoteIndex;
//		} else {
//			str = "\nrootNoteIndex not set";
//		}
//		return str;
		return hasRootNoteIndex 
				? "\nrootNoteIndex=" + rootNoteIndex
				: "\nrootNoteIndex not set";
	}



	private String getLowestNoteToString()
	{
//		String str;
//		if (hasLowestNote){
//			str = "\nlowestNote=" + lowestNote + ", " + CSD.noteName(lowestNote);
//		} else {		
//			str = "\nlowestNote not set";
//		}
//		return str;
		return hasLowestNote 
				? "\nlowestNote=" + lowestNote + ", " + CSD.noteName(lowestNote) 
				: "\nlowestNote not set";
	}
	
	
	
	@Override
	public String chordSymbolToString() 
	{
		return CSD.noteName(rootNote) + name;
	}
	
	
	
	@Override
	public String chordSymbolAndInversionToString() 
	{
		return CSD.noteName(rootNote) + name + " " + inversionName();
	}

	
	
	@Override
	public boolean isDominantType() 
	{
		return isDominantType;
	}

	
	
	@Override
	public boolean isMinorType() 
	{
		return isMinorType;
	}

	
	
	@Override
	public boolean isMajorType() 
	{
		return isMajorType;
	}

	
	
	@Override
	public boolean isDiminishedType() 
	{
		return isDiminishedType;
	}

	
	
	@Override
	public boolean isHalfDiminishedType() 
	{
		return isHalfDiminishedType;
	}

	
	
	@Override
	public boolean isDiminishedSeventhType() 
	{
		return isDiminishedSeventhType;
	}
	
	
	
	@Override
	public boolean isNinthChord() 
	{
		return true;
	}

	
	
	@Override
	public boolean isFlatNinth() 
	{
		return isFlatNinth;
	}
	
	
// privates ============================================================================
	private void doTypeTest() 
	{
		boolean third = false;
		boolean minorThird = false;
		boolean minorSeventh = false;
		boolean perfectFifth = false;
		boolean flatFifth = false;
		boolean dimSeventh = false;
		boolean flatNine = false;
		for (int i: intervals)
		{
			if (i == 4) third = true;
			if (i == 10) minorSeventh = true;
			if (i == 3) minorThird = true;
			if (i == 7) perfectFifth = true;
			if (i == 6) flatFifth = true;
			if (i == 9) dimSeventh = true;
			if (i == 13) flatNine = true;
		}
		if (third && minorSeventh)isDominantType = true;
		if (third && perfectFifth) isMajorType = true;
		if (minorThird && perfectFifth) isMinorType = true;
		
		if (minorThird && flatFifth && minorSeventh) isHalfDiminishedType = true;	// obviously the quartad
		if (minorThird && flatFifth && dimSeventh) isDiminishedSeventhType = true;
		if (minorThird && flatFifth && !dimSeventh) isDiminishedType = true;		// diminished triad (not part of diminished7th)
		if (flatNine) isFlatNinth = true;
	}

	
	
	@Override
	public int[] extendedChordToneIntervals()
	{
		return extendeChordToneIntervals;
	}



	@Override
	public void setExtendedChordToneIntervals(int[] arr)
	{
		extendeChordToneIntervals = arr;		
	}
}
