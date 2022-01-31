package main.java.da_utils.algorithmic_models.melody_segmenter;

import java.util.ArrayList;

import main.java.da_utils.ableton_live.ableton_live_clip.LiveClip;
import main.java.da_utils.ableton_live.ableton_live_clip.LiveMidiNote;
import main.java.da_utils.resource_objects.ChordForm;

/*
 * creates a version of a LiveClip that has a copy of theoteList content as Phantom notes 
 * preceding and following the main content of the noteList, offset by the loop parameters
 * do not use for injection into Live. 
 */

public class PhantomLiveClip extends LiveClip {

	public LiveClip lc;
	public int phantomStartIndex;
	public int phantomEndIndex;
//	public ArrayList<LiveMidiNote> phantomNoteList;
	public ArrayList<Double> interOnsetIntervalList;
	public ArrayList<Double> offsetToOnsetIntervalList;
	public ArrayList<Integer> pitchIntervalList;
	public ChordForm cf = null;				// chordform for the melody, duh.

	public PhantomLiveClip(LiveClip lc){
		super(lc.track, lc.clip);
		addNoteList(lc.noteList);
		this.lc = lc;
		this.length = lc.length;
		this.loopStart = lc.loopStart;					// negative values all mean 'not applicable'
		this.loopEnd = lc.loopEnd;
		this.startMarker = lc.startMarker;
		this.endMarker = lc.endMarker;
		this.signatureNumerator = lc.signatureNumerator;
		this.signatureDenominator = lc.signatureDenominator;
		this.offset = lc.offset;						// to synchronise the start times of various length clips
		this.name = lc.name;
		this.phantomStartIndex = lc.noteList.size();
		this.phantomEndIndex = phantomStartIndex + lc.noteList.size();
		makePhantomNoteList(lc);
	}

	private void makePhantomNoteList(LiveClip lc) {
		this.length = loopEnd - loopStart;
//		phantomNoteList = new ArrayList<LiveMidiNote>();
		double[] offsetArr = new double[]{-length, length};
		for (double offset: offsetArr){
			for (LiveMidiNote lmn: lc.noteList){
				addNote(new PhantomNote(lmn, offset));
			}
		}
		sortNoteList();
		makeInterOnsetGapToPreviousNoteList();
		makePreviousOffsetToOnsetGapList();
		makePitchIntervalList();
	}
	
	private void makePitchIntervalList() {
		pitchIntervalList = new ArrayList<Integer>();
		pitchIntervalList.add(0);		// first note has no previous. Hopefully this will not be an issue
		for (int i = 1; i < noteList.size(); i++){
			pitchIntervalList.add(noteList.get(i).note - noteList.get(i - 1).note);
		}
	}

	private void makePreviousOffsetToOnsetGapList() {
		offsetToOnsetIntervalList = new ArrayList<Double>();
		offsetToOnsetIntervalList.add(0.0);		// first note has no previous. Hopefully this will not be an issue
		for (int i = 1; i < noteList.size(); i++){
			offsetToOnsetIntervalList.add(noteList.get(i).position - (noteList.get(i - 1).position + noteList.get(i - 1).length));
		}
	}

	private void makeInterOnsetGapToPreviousNoteList() {
		interOnsetIntervalList = new ArrayList<Double>();
		interOnsetIntervalList.add(0.0);		// first note has no previous. Hopefully this will not be an issue
		for (int i = 1; i < noteList.size(); i++){
			interOnsetIntervalList.add(noteList.get(i).position - noteList.get(i - 1).position);
		}
	}
	
}
