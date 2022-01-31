package main.java.da_utils.chord_progression_analyzer.chord_chunk;

import java.util.ArrayList;
import java.util.Comparator;

import main.java.da_utils.ableton_live.ableton_live_clip.LiveMidiNote;
import main.java.da_utils.chord_progression_analyzer.ChordInKeyObject;
import main.java.da_utils.static_chord_scale_dictionary.CSD;
import main.java.da_utils.static_chord_scale_dictionary.NotePatternAnalysis;

/*
 * contains:-
 * 1. a group of notes which can be considered a chord due to their similar start point
 * 2. an list of NotePatternAnalysis instances which would contain a list of possible descriptions 
 * 		of the chord (i.e. would not differentiate between a C6 or an Am7 at this point)
 * 3. position information (all the same)
 * 4. duration info - the distance to the next ChordChunk in the ChordChunkArray
 * 5. next and previous ChordChunks
 * 
 */
public class ChordChunk 
{
	
	
	
	private static final int DEFAULT_VELOCITY = 64;
	public ArrayList<LiveMidiNote> noteList = new ArrayList<LiveMidiNote>();
	private ArrayList<NotePatternAnalysis> chordOptionList;
	private int bestChordOptionIndex = -1;
	private double position;
	private double length;
	private ChordChunk next = null;
	private ChordChunk previous = null;
	private ChordChunkList parent;
	private ChordInKeyObject associatedCIKO;
//	private ChordAnalysisObject cao;

	
	
//	public ChordChunk(LiveMidiNote lmn)
//	{
//		noteList.add(lmn);
//		position = lmn.position;
//		cao = new ChordAnalysisObject(makeNoteValueList(noteList));
//	}
	
	
	
	public ChordChunk()
	{
		// for subclassing......
	}
	
	
	
	public ChordChunk(double pos, ArrayList<LiveMidiNote> noteList, ChordChunkList ccl)
	{
		this.parent = ccl;
		this.noteList = noteList;
		position = pos;
		//cao = new ChordAnalysisObject(makeNoteValueList(noteList));
		chordOptionList = CSD.getChordOptions(makeNoteValueList(noteList));
		if (chordOptionList.size() > 0)
		{
			bestChordOptionIndex = 0;		// temporary setting until further context is established
											// the sequence of NotePatterns in the CSD.npArr would dictate which 
											// possibility would be favoured at this stage of the analysis
		}
	}
	
	
	
	public ChordChunk(double pos, double length, int[] noteArr)
	{	
		this.parent = null;
		this.length = length;
		this.noteList = makeNoteList(pos, noteArr, length);
		position = pos;
		//cao = new ChordAnalysisObject(makeNoteValueList(noteList));
		chordOptionList = CSD.getChordOptions(makeNoteValueList(noteList));
		if (chordOptionList.size() > 0)
		{
			bestChordOptionIndex = 0;		// temporary setting until further context is established
											// the sequence of NotePatterns in the CSD.npArr would dictate which 
											// possibility would be favoured at this stage of the analysis
		}
	}
	
	
	
	protected ArrayList<LiveMidiNote> makeNoteList(double pos, int[] noteArr, double length) 
	{
		ArrayList<LiveMidiNote> list = new ArrayList<LiveMidiNote>();
		for (int i: noteArr)
		{
			list.add(new LiveMidiNote(i, pos, length, DEFAULT_VELOCITY, 0));
		}
		return list;
	}
	
	
	
	public void setParent(ChordChunkList aChordChunkList)
	{
		parent = aChordChunkList;
	}
	
	
	
	public void setChordOptionList(ArrayList<NotePatternAnalysis> chordOptions)
	{
		chordOptionList = chordOptions;		
	}
	
	
	
	public double position()
	{
		return position;
	}
	
	
	
	public void setPosition(double aPosition)
	{
		position = aPosition;
	}
	
	
	public double length()
	{
		return length;
	}
	
	
	
	public void setLength(double len)
	{
		//System.out.println("ChordChunk length changed to " + len);
		length = len;
	}
	
	
	
	public ArrayList<NotePatternAnalysis> getChordOptionList()
	{
		return chordOptionList;
	}
	
	
	
	public void setBestChordOptionIndex(int aIndex)
	{
		bestChordOptionIndex = aIndex;
	}
	
	
	
	public void setNext(ChordChunk cc)
	{
		next = cc;
	}
	
	
	
	public void setPrevious(ChordChunk cc)
	{
		previous = cc;
	}
	
	
	public ChordChunk next()
	{
		return next;
	}
	
	
	public ChordChunk previous()
	{
		return previous;
	}
	
	
	
	public int size()
	{
		return noteList.size();
	}
	
	
	
	public static Comparator<ChordChunk> positionComparator = new Comparator<ChordChunk>()
	{
		public int compare(ChordChunk chunk1, ChordChunk chunk2)
		{
			if (chunk1.position() < chunk2.position) return -1;
			if (chunk1.position() > chunk2.position) return 1;
			return 0;
		}
	};
	
	
	
	public int rootModNote()
	{
		if (bestChordOptionIndex > -1)
		{
			return getNotePatternAnalysis().rootNote();
		} 
		else 
		{
			return -1;
		}	
	}
	
	
	
	public String rootNoteName()
	{
		if (bestChordOptionIndex > -1)
		{
			return CSD.noteName(rootModNote());
		} 
		else 
		{
			return "noRoot";
		}		
	}
	
	
	
	public String chordTypeName()
	{
		if (bestChordOptionIndex > -1)
		{
			return getNotePatternAnalysis().name();
		} 
		else 
		{
			return "noChord";
		}
	}
	
	
	
	public String chordSymbol()
	{
		return rootNoteName() + chordTypeName();
	}
	
	
	
	public NotePatternAnalysis getNotePatternAnalysis()
	{
		if (bestChordOptionIndex > -1)
		{
			return chordOptionList.get(bestChordOptionIndex);
		} 
		else 
		{
			return null;
		}		
	}
	
	
	
	public String toString()
	{
		StringBuilder sb = new StringBuilder();
		sb.append("ChordChunk: position=" + position + " length=" + length + "\n");
		if (previous != null)
		{
			sb.append("previous().position=" + previous().position() + " previous().length=" + previous.length + "\n");
		}
		if (next != null)
		{
			sb.append("next().position=" + next().position() + " next().length=" + next.length + "\n");	
		}
		for (LiveMidiNote lmn: noteList)
		{
			sb.append(lmn.oneLineToString() + "\n");
		}
		sb.append("ChordOptionList: size=" + chordOptionList.size() + "------------\n");
		for (NotePatternAnalysis npa: chordOptionList)
		{
			sb.append(npa.toString() + "\n");
		}
		return sb.toString();
	}
	
	
	
	public boolean isDominantType()
	{
		return getNotePatternAnalysis().isDominantType();
	}
	
	
	
	public boolean isMinorType()
	{
		return getNotePatternAnalysis().isMinorType();
	}
	
	
	
	public boolean isMajorType()
	{
		return getNotePatternAnalysis().isMajorType();
	}
	
	
	
	public boolean isDiminishedType()
	{
		return getNotePatternAnalysis().isDiminishedType();
	}		// diminished triad
	
	
	
	public boolean isHalfDiminishedType()
	{
		return getNotePatternAnalysis().isHalfDiminishedType();
	}	// obviously the quartad
	
	
	
	public boolean isDiminishedSeventhType()
	{
		return getNotePatternAnalysis().isDiminishedSeventhType();
	}
	
	
	
	public boolean isNinthChord()
	{
		return getNotePatternAnalysis().isNinthChord();
	}
	
	
	
	public boolean isFlatNinth()
	{
		return getNotePatternAnalysis().isFlatNinth();
	}
	
	
	
	public boolean isSameTypeAndRootAs(ChordChunk cc)
	{
//		String thisChord = this.chordSymbol();
//		String lastChord = cc.chordSymbol();
		if (cc.rootModNote() == this.rootModNote())
		{
			if (	
					cc.isDiminishedSeventhType() == this.isDiminishedSeventhType() 
					&& cc.isDiminishedType() == this.isDiminishedType()
					&& cc.isDominantType() == this.isDominantType()
					&& cc.isHalfDiminishedType() == this.isHalfDiminishedType()
					&& cc.isMajorType() == this.isMajorType()
					&& cc.isMinorType() == this.isMinorType()){
				return true;
			} 
		}
		return false;
	}
	
	
	
	protected ArrayList<Integer> makeNoteValueList(ArrayList<LiveMidiNote> noteList2) 
	{
		ArrayList<Integer> list = new ArrayList<Integer>();
		for (LiveMidiNote lmn: noteList2){
			list.add(lmn.note);
		}
		return list;
	}
	
	

	public String slashChordToString() 
	{
		StringBuilder sb = new StringBuilder();
		if (parent == null)
		{
			sb.append(chordSymbol() + ",");
		} 
		else 
		{
			int sigdenom = parent.signatureDenominator();
			double beatLength = CSD.getBeatLength(parent.signatureDenominator());
			int slashCount = (int)(length / CSD.getBeatLength(parent.signatureDenominator())) - 1;	// this may not be entirely accurate but whatever
			//System.out.println("ChordChunk.slashChordsToString().slashCount=" + slashCount + " beatLength=" + beatLength + " length=" + length + " sigdenom=" + sigdenom);
			sb.append(chordSymbol() + " ");
			for (int i = 0; i < slashCount; i++)
			{
				sb.append("/ ");
			}
		}		
		return sb.toString();
	}



	public void setAssociatedChordInKeyObject(ChordInKeyObject aChordInKeyObject)
	{
		associatedCIKO = aChordInKeyObject;	
	}
	
	
	
	public ChordInKeyObject getAssociatedCIKO()
	{
		return associatedCIKO;
	}


}
