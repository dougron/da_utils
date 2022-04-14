package main.java.da_utils.static_chord_scale_dictionary;

public class NP_Note implements NotePatternAnalysis 
{
	
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

	
	public NP_Note()
	{}
	
	
	
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
	public int rootNoteIndex() 
	{
		return 0;
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
	public void setLowestNote(int note) 
	{
		lowestNote = note;
		rootNote = note % 12;
	}

	
	
	@Override
	public void setRootNote(int rootNote) 
	{}

	
	
	@Override
	public void setRootNoteIndex(int rootNoteIndex) 
	{}

	
	
	@Override
	public void setIntervals(int[] arr) 
	{}

	
	
	@Override
	public void setIntervals(Integer[] arr) 
	{}

	
	
	@Override
	public void setName(String name) 
	{}
	
	
	
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
	
	
	
	public String toString()
	{
		StringBuilder sb = new StringBuilder();
		sb.append("---\nNP_Note instance of NotePatternAnalysis");
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
	public void setInversionIndex(int i) 
	{}
	
	
	
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
