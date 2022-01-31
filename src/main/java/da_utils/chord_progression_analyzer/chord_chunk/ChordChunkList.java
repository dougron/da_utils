package main.java.da_utils.chord_progression_analyzer.chord_chunk;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.Iterator;

import main.java.da_utils.ableton_live.ableton_live_clip.LiveClip;
import main.java.da_utils.ableton_live.ableton_live_clip.LiveMidiNote;
import main.java.da_utils.static_chord_scale_dictionary.NP_NullChord;
import main.java.da_utils.static_chord_scale_dictionary.NotePatternAnalysis;

/*
 * takes contents of LiveClip and converts into chunks based on start position
 * (length not considered at this point, but the distance to the next chunk
 * will be considered the chunk length as this is for chord analysis)
 */



public class ChordChunkList 
{

	private String resolution;
	private double length;
	private int default_channel = 1;

	
	public ArrayList<ChordChunk> chunkList = new ArrayList<ChordChunk>();
	public LiveClip lc;
	public boolean liveClipIsSameAsChunkList;

	
	// for sub classing
	public ChordChunkList()
	{}
	
	
	public ChordChunkList(String rez, LiveClip lc)
	{

		resolution = rez;
		this.lc = lc;
		cleanUpLiveClip();		// any LiveMidiNote outside the loopStart and loopEnd points is either removed or trimmed
		makeChunkList(rez);
		liveClipIsSameAsChunkList = true;
	}
	
	
	
	private void cleanUpLiveClip() 
	{
		// any LiveMidiNote outside the loopStart and loopEnd points is either removed or trimmed
		ArrayList<LiveMidiNote> removeList = new ArrayList<LiveMidiNote>();
		for (LiveMidiNote lmn: lc.noteList)
		{
			if (lmn.quantizedPosition(resolution) < lc.loopStart || lmn.quantizedPosition(resolution) >= lc.loopEnd)
			{
				removeList.add(lmn);
			} 
			else 
			{
				if (lmn.quantizedEndPoint(resolution) > lc.loopEnd)
				{
					lmn.length -= lmn.quantizedEndPoint(resolution) - lc.loopEnd;
				}
			}
		}
		if (removeList.size() > 0)
		{
			lc.noteList.removeAll(removeList);
		}
		// also, the length value is checked
		lc.length = lc.loopEnd - lc.loopStart;		
	}
	
	
	
	public ChordChunkList (String rez, String name)
	{
		// this instantiator does not have correct loopStart and loopEnd points in the LiveClip
		// really it just carries the name, for now
		liveClipIsSameAsChunkList = false;
		lc = new LiveClip(0, 0);
		lc.name = name;
	}
	
	
	
	public double getLength()
	{
		return lc.length;
	}
	

	public void addChunk(ChordChunk cc)
	{
		// this can only be used properly if you are adding ChordChunks from an already existing
		// ChordChunkList where lengths and next() and previous() have already been set
		// (as in the ProgressionAnalyzer where it is significant to keep these values the same)
		chunkList.add(cc);
		Collections.sort(chunkList, ChordChunk.positionComparator);
		liveClipIsSameAsChunkList = false;
	}
	
	
	
	public int signatureNumerator()
	{
		if (lc.signatureNumerator == -1)
		{
			return 4;		// 4/4 default...... to deal with ChordChunk slash notation
		} 
		else 
		{
			return lc.signatureNumerator;
		}	
	}
	
	
	
	public int signatureDenominator()
	{
		if (lc.signatureDenominator == -1)
		{
			return 4;		// 4/4 default...... to deal with ChordChunk slash notation
		} 
		else 
		{
			return lc.signatureDenominator;
		}
	}
	
	
	
	public LiveClip getLiveClip()
	{
		return lc;
	}

	
	
	private void makeChunkList(String rez)
	{
		populateChunkList(rez);
		Collections.sort(chunkList, ChordChunk.positionComparator);
		setNextAndPrevious();
		setChunkLengths();
	}
	
	
	
	private void setChunkLengths() 
	{
		for (int i = 0; i < chunkList.size(); i++)
		{
			ChordChunk cc = chunkList.get(i);
			if (i == chunkList.size() - 1)
			{
				double lll = 0.0;
				if (cc == cc.next())
				{
					lll = lc.loopEnd - cc.position();
				} 
				else 
				{
					lll = cc.next().position() + lc.loopEnd - lc.loopStart - cc.position();
				}				
				cc.setLength(lll);
			} 
			else 
			{
				double lll = cc.next().position() - cc.position();
				cc.setLength(lll);
			}
		}		
	}

	

	private void populateChunkList(String rez) 
	{
		HashMap<Double, ArrayList<LiveMidiNote>> map = new HashMap<Double, ArrayList<LiveMidiNote>>();
		for (LiveMidiNote lmn: lc.noteList(rez))
		{
			double posInLoop = lmn.position - lc.loopStart;
			if (!map.containsKey(posInLoop))
			{
				ArrayList<LiveMidiNote> list = new ArrayList<LiveMidiNote>();
				map.put(posInLoop, list);
			}
			map.get(posInLoop).add(lmn);
		}
		for (Double pos: map.keySet())
		{
			//System.out.println("ChordChunkList.populteChunkList(): array at key=" + pos + " has size=" + map.get(pos).size());
			ChordChunk cxc = new ChordChunk(pos, map.get(pos), this);
			chunkList.add(cxc);
		}	
		//System.out.println("done");
	}
	


	protected void setNextAndPrevious() 
	{
		for (int i = 0; i < chunkList.size(); i++)
		{
			ChordChunk cc = chunkList.get(i);
			if (i == chunkList.size() - 1)
			{
				cc.setNext(chunkList.get(0));
			} 
			else 
			{
				cc.setNext(chunkList.get(i + 1));
			}
			if (i == 0)
			{
				cc.setPrevious(chunkList.get(chunkList.size() - 1));
			} 
			else 
			{
				cc.setPrevious(chunkList.get(i - 1));
			}
		}		
	}
	
	
	
//	public ArrayList<ChordAnalysisObject> getNonChordList()
//	{
//		// gets chords from the chunkArray that cannot be indentified
//		ArrayList<ChordAnalysisObject> tempList = new ArrayList<ChordAnalysisObject>();
//		for (ChordAnalysisObject cao: chunkArray)
//		{
//			if (cao.chordToString() == ChordAnalysisObject.NO_CHORD)
//			{
//				tempList.add(cao);
//			}
//		}
//		return tempList;
//	}


	
	public String toString()
	{
		String ret = "ChordChunkArray contents:\n";
		for (ChordChunk cao: chunkList)
		{
			ret = ret + "---------------------\n" + cao.toString() + "\n";
		}
		return ret;
	}
	
	
	
	public String slashChordsToStringOLD()
	{
		String str = "";
		for (ChordChunk cc: chunkList)
		{
			str += cc.slashChordToString();
		}
		return str;
	}
	
	
	
	public String slashChordsToString()
	{
		String str = "";
		double pos = 0.0;
		double lineLength = lc.getBarLengthInQuarters() * 4;	// puts in a line break every 4 bars
		for (ChordChunk cc: chunkList)
		{
			str += cc.slashChordToString();
			pos += cc.length();
			if (pos >= lineLength)
			{
				str += "\n";
				pos = 0.0;
			}
		}
		return str;
	}
	
	
	
	public String minusChordsToString()
	{
		String str = "";
		for (ChordChunk cc: chunkList)
		{
			str += cc.chordSymbol() + "-";
		}
		return str;
	}
	
	

	public NotePatternAnalysis getPrevailingChord(double pos)
	{
		double relativePos = pos % lc.length;
		for (ChordChunk cc: chunkList)
		{
			if (relativePos >= cc.position() && relativePos < cc.position() + cc.length()){
				return cc.getNotePatternAnalysis();
			}
		}
		return NULL_CHORD;
	}
	
	
	
	public Iterator<ChordChunk> chunkListIterator()
	{
		return chunkList.iterator();
	}
	
	
	
	public int size()
	{
		return chunkList.size();
	}
	
	
	
	public String name()
	{
		return lc.name;
	}
	
	
	
	private static final NotePatternAnalysis NULL_CHORD = new NP_NullChord();


	
	
}
