package main.java.da_utils.chord_progression_analyzer;

import java.util.ArrayList;

import main.java.da_utils.ableton_live.ableton_live_clip.LiveMidiNote;

/*
 * CURRENTLY NOT USED #################################################################
 * an advancement on the ChordChunk class 
 * 
 * for use with the ChordProgressionAnalyzer
 * 
 * caters for held notes. also keeps record of original notes so 
 * that velocity can be taken into account at a later date
 */

public class CPAChordChunk {

	ArrayList<LiveMidiNote> allNotes = new ArrayList<LiveMidiNote>();
	ArrayList<LiveMidiNote> heldNotes = new ArrayList<LiveMidiNote>();
	ArrayList<LiveMidiNote> newNotes = new ArrayList<LiveMidiNote>();
	double position;
	double length;
	private boolean isLastNote;
	private ArrayList<CPAChordChunk> masterChunkList;
	private CPAChordChunk previousChunk = null;
	private CPAChordChunk nextChunk = null;

	public CPAChordChunk(LiveMidiNote lmn, ArrayList<CPAChordChunk> mcl){
		init(lmn, mcl);
		isLastNote = true;
	}
	private void init(LiveMidiNote lmn, ArrayList<CPAChordChunk> mcl){
		newNotes.add(lmn);
		allNotes.add(lmn);
		position = lmn.position;
		length = lmn.length;
		this.masterChunkList = mcl;
	}
}
