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
	public int[] intervals() 
	{
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
		for (int i = 0; i < arr.length; i++) intervals[i] = arr[i];
		hasIntervals = true;
	}
	
	
	
	public String toString()
	{
		StringBuilder sb = new StringBuilder();
		sb.append("---\nNP_Interval instance of NotePatternAnalysis");
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
		if (hasIntervals)
		{
			sb.append("\nintervals: ");
			for (int i: intervals)sb.append(i + ", ");
		} 
		else 
		{
			sb.append("\nno intervals set");
		}
		return sb.toString();
	}



	private String getNameToString()
	{
		return hasName ? "\nname=" + name : "\nname not set";
	}



	private String getInversionIndexToString()
	{
		return hasInversionIndex 
				? "\ninversionIndex=" + inversionIndex + ", " + inversionName() 
				: "\ninversionIndex not set";
	}



	private String getRootNoteToString()
	{
		return hasRootNote 
				? "\nrootNote=" + rootNote + ", " + CSD.noteName(rootNote)
				: "\nrootNote not set";
	}



	private String getRootNoteIndexToString()
	{
		return hasRootNoteIndex 
				? "\nrootNoteIndex=" + rootNoteIndex
				: "\nrootNoteIndex not set";
	}



	private String getLowestNoteToString()
	{
		return hasLowestNote 
				? "\nlowestNote=" + lowestNote + ", " + CSD.noteName(lowestNote) 
				: "\nlowestNote not set";
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
		return false;
	}

	
	
	@Override
	public boolean isMinorType() 
	{
		return false;
	}

	
	
	@Override
	public boolean isMajorType() 
	{
		return false;
	}

	
	
	@Override
	public boolean isDiminishedType() 
	{
		return false;
	}

	@Override
	public boolean isHalfDiminishedType() 
	{
		return false;
	}

	
	
	@Override
	public boolean isDiminishedSeventhType() 
	{
		return false;
	}

	
	
	@Override
	public boolean isNinthChord() 
	{
		return false;
	}

	
	
	@Override
	public boolean isFlatNinth() 
	{
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
