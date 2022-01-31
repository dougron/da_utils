package main.java.da_utils.resource_objects;
import java.util.ArrayList;
import java.util.Iterator;

import com.cycling74.max.MaxObject;

import DataObjects.incomplete_note_utils.FinalListNote;
import main.java.da_utils.ableton_live.ableton_live_clip.LiveClip;
import main.java.da_utils.ableton_live.ableton_live_clip.LiveMidiNote;
import main.java.da_utils.algorithmic_models.pipeline.pipeline_note_list.PipelineNoteList;
import main.java.da_utils.algorithmic_models.pipeline.pipeline_note_list.PipelineNoteObject;
import main.java.da_utils.algorithmic_models.pipeline.plugins.filter_sort.FilterObject;
import main.java.da_utils.algorithmic_models.pipeline.plugins.filter_sort.FilterSortInterface;
import main.java.da_utils.chord_progression_analyzer.ChordInKeyObject;
import main.java.da_utils.chord_progression_analyzer.ProgressionAnalyzer;
import main.java.da_utils.chord_progression_analyzer.chord_chunk.ChordChunk;
import main.java.da_utils.chord_progression_analyzer.chord_chunk.ChordChunkList;
import main.java.da_utils.chord_scale_dictionary.ChordScaleDictionary;
import main.java.da_utils.combo_variables.DoubleAndString;
import main.java.da_utils.combo_variables.IntAndString;
import main.java.da_utils.static_chord_scale_dictionary.NotePatternAnalysis;



/*
 * same as Form from Beebmachine but renamed to solve cannot sresolve type error.
 * edit :- as of 19May2017, no longer the same as BeebMachine 
 */
		
public class ChordForm implements FilterSortInterface{
	
	private LiveClip lc;
	private ChordChunkList ccl;
	private String resolution = "32n";		// default resolution
	private ProgressionAnalyzer pa;
	private static final IntAndString NULL_KEY = new IntAndString(-1, "");
	private int numberOfForms = 1;
	
	public ChordForm(LiveClip lc){
		this.lc = lc;
		ccl = new ChordChunkList(resolution, lc);
		pa = new ProgressionAnalyzer(ccl);

	}
	
	public NotePatternAnalysis getPrevailingNotePatternAnalysis(double barPos){
		return ccl.getPrevailingChord(barPos);
	}
	
	public String getPrevailingChordSymbol(double pos){
		return ccl.getPrevailingChord(pos).chordSymbolToString();
	}
	
	public String getPrevailingChordSymbol(double pos, LiveClip lc){
		// returns the prevailing chord symbol with syncopation taken into account:
		// any note will take the chord from the following 8th note position unless there is a note there
		// 
		pos = (double)Math.round(pos * 2) / 2; // round to nearest 0.5 

		if (thereIsANotePosPlusHalf(pos, lc)){		// thereIsANotePosPlusHalf does not yet do 16th syncopation
			return getPrevailingChordSymbol(pos);
		} else {
			return getPrevailingChordSymbol(pos + 0.5);
		}
	}
	
	public IntAndString getPrevailingChord(double pos, LiveClip lc){
		// returns the prevailing chord with syncopation taken into account:
		// any note will take the chord from the following 8th note position unless there is a note there
		// 
		pos = (double)Math.round(pos * 2) / 2; // round to nearest 0.5 

		if (thereIsANotePosPlusHalf(pos, lc)){ 		// thereIsANotePosPlusHalf does not yet do 16th syncopation
			return getPrevailingChord(pos);
		} else {
			return getPrevailingChord(pos + 0.5);
		}		
	}
	
	public IntAndString getPrevailingChord(FinalListNote fln) {
		double pos = (double)Math.round(fln.position() * 2) / 2; // round to nearest 0.5 
		double nextPos = (double)Math.round(fln.next().position() * 2) / 2;

		if (nextPos == pos + 0.5){
			return getPrevailingChord(pos);
		} else {
			return getPrevailingChord(pos + 0.5);
		}	
	}
	
	private IntAndString getPrevailingChord(double pos) {
		ChordInKeyObject ciko = pa.getPrevailingCIKO(pos);		
		return new IntAndString(ciko.rootIndex, ciko.chordType);
	}
	
	public ChordInKeyObject getPrevailingCIKO(double pos, LiveClip lc){
		// returns the prevailing CHordInKeyObject with syncopation taken into account:
		// any note will take the chord from the following 8th note position unless there is a note there
		// 
		pos = (double)Math.round(pos * 2) / 2; // round to nearest 0.5 

		if (thereIsANotePosPlusHalf(pos, lc)){
			return getPrevailingCIKO(pos);
		} else {
			return getPrevailingCIKO(pos + 0.5);
		}		
	}
	
	public ChordInKeyObject getPrevailingCIKO(double pos, PipelineNoteList pnl){
		// returns the prevailing CHordInKeyObject with syncopation taken into account:
		// any note will take the chord from the following 8th note position unless there is a note there
		// 
		pos = (double)Math.round(pos * 2) / 2; // round to nearest 0.5 

		if (thereIsANotePosPlusHalf(pos, pnl)){
			return getPrevailingCIKO(pos);
		} else {
			return getPrevailingCIKO(pos + 0.5);
		}		
	}
	
	public ChordInKeyObject getPrevailingCIKO(FinalListNote fln) {
		double pos = (double)Math.round(fln.position() * 2) / 2; // round to nearest 0.5 
		double nextPos = (double)Math.round(fln.next().position() * 2) / 2;

		if (nextPos == pos + 0.5){
			return getPrevailingCIKO(shmoozeIntoStartEndRange(pos));
		} else {
			return getPrevailingCIKO(shmoozeIntoStartEndRange(pos + 0.5));
		}	
	}
	
	private double shmoozeIntoStartEndRange(double actualpos) {
		while (actualpos < lc.loopStart) actualpos += lc.loopEnd - lc.loopStart;		// this deals with notes outside of the start and end points of the chordForm
		while (actualpos > lc.loopEnd) actualpos -= lc.loopEnd - lc.loopStart;
		return actualpos;
	}
	
	public ChordInKeyObject getPrevailingCIKO(double pos) {
		return pa.getPrevailingCIKO(pos);		
	}
	
	private boolean thereIsANotePosPlusHalf(double pos, LiveClip lc) {
		for (LiveMidiNote lmn: lc.noteList){
			if (lmn.position > pos){
				double testPos = (double)Math.round(lmn.position * 2) / 2;
				if (testPos == pos + 0.5){
					return true;
				}
			}	
		}
		return false;
	}
	
	private boolean thereIsANotePosPlusHalf(double pos, PipelineNoteList pnl) {
		for (PipelineNoteObject pno: pnl.pnoList){
			if (pno.position > pos){
				double testPos = (double)Math.round(pno.position * 2) / 2;
				if (testPos == pos + 0.5){
					return true;
				}
			}	
		}
		return false;
	}
	
	public double length(){
		return lc.length;
	}
	
	public double totalLength(){
		return length() * numberOfForms;
	}
	
	public String chunkListToString(){
		return ccl.toString();
	}
	
	public String slashChordsToString(){
		return ccl.slashChordsToString();
	}
	
	public String minusChordsToString(){
		return ccl.minusChordsToString();
	}

	public Iterator<ChordChunk> getChordAnalysisIterator(){
		return ccl.chunkListIterator();
	}
	
	public String name(){
		return lc.name;
	}
	
	public String toString(){
		String str = "ChordForm ----------\n" + "barCount=" + lc.barCount();
		str += "\nnumberOfForms=" + numberOfForms + "\n" + slashChordsToString();
		str += "\nLiveClip:---\n" + lc.toString();
		str += "\nChordChunkList:---\n" + ccl.toString();
		return str;
	}
	
	public String shortToString() {
		String str = "ChordForm ----------\nnumberOfForms=" + numberOfForms;
		str += "\ntimeSignature: " + timeSignatureToString();
		str += "\n" + slashChordsToString();
		return str;
	}
	
	public IntAndString getPrevailingKey(double pos){
		ChordInKeyObject ciko = pa.getPrevailingCIKO(pos);
		if (ciko != null){
			return new IntAndString(ciko.keyIndex, ciko.keyQuality());
		} else {
			return NULL_KEY;
		}
	}
	
	public String timeSignatureToString(){
		int[] arr = getTimeSignature();
		return arr[0] + "/" + arr[1];
	}
	
	public int[] getTimeSignature(){
		return new int[]{lc.signatureNumerator, lc.signatureDenominator};
	}
	
	public void setNumberOfForms(int i){
		if (i > 0){
			numberOfForms = i;
		}
	}
	
	public int numberOfForms(){
		return numberOfForms;
	}
	
	public boolean hasNoteListContent(){	// ##################################
		if (lc.noteList.size() == 0){
			return false;
		} else {
			return true;
		}
	}
	
	public double barLength(){
		return lc.getBarLengthInQuarters();
	}
	
	public int barCount(){
		return lc.barCount();
	}
	
	public int totalBarCount() {
		return barCount() * numberOfForms;
	}
	
	public LiveClip lc(){
		return lc;
	}
	
	public ArrayList<DoubleAndString> getListOfChords(){
		return pa.getListOfChords();
	}
	
	public ArrayList<DoubleAndString> getListOfKeyChordAnalysis(){
		return pa.getListOfKeyChordSymbols();
	}
	
	public ArrayList<DoubleAndString> getListOfSimpleKeyChordAnalysis(){
		return pa.getListOfSimpleKeyChordSymbols();
	}
	
//	public String shortProgression(){
	// wont be replaced.......
//		return chordProgression.minusChordsToString();
//	}
//	public FilterObject getFilterObject(){
	// no idea what this does. will leave it here in case it becomes apparent while i am fixing everything i have broken
	// while depreciating ChordAnalysisObject and ChordScaleDictionary
	// this is actually used in SortaSelekta.Node.......
	// no need to return one of these, as ChordForm  now implements FilterSortInterface
//		return new FilterObject(minusChordsToString(), this);
//	}
// FilterSortMethod --------------------------------------------------------
	public FilterSortInterface getFilteSortObject(){
		return this;
	}
	
	

}
