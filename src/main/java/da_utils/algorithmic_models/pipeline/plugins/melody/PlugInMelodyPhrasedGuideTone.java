package main.java.da_utils.algorithmic_models.pipeline.plugins.melody;
import java.util.ArrayList;

import main.java.da_utils.ableton_live.ableton_live_clip.LiveMidiNote;
import main.java.da_utils.algorithmic_models.pipeline.pipeline_note_list.PipelineNoteList;
import main.java.da_utils.algorithmic_models.pipeline.pipeline_note_list.PipelineNoteObject;
import main.java.da_utils.algorithmic_models.pipeline.plugins.Pluggable;
import main.java.da_utils.algorithmic_models.pipeline.plugins.pipeline_plugin.PipelinePlugIn;
import main.java.da_utils.algorithmic_models.pipeline.plugins.pipeline_plugin.PlayPlugArgument;
import main.java.da_utils.chord_progression_analyzer.ChordInKeyObject;
import main.java.da_utils.chord_scale_dictionary.chord_analysis.ChordAnalysisObject;
import main.java.da_utils.resource_objects.ChordForm;
import DataObjects.contour.FourPointContour;
import LegacyStuff.DAGlobals;
/*
 * generates guide tone melody.
 * acquires contours from the connected contour unit
 * parameters are:
 * 		number of phrases
 * 		phrase lengths
 * 		phrase percentage - how much of the allocated phrase will be music and silence
 * 		ending note strength pattern
 * 		note density
 */

public class PlugInMelodyPhrasedGuideTone extends Pluggable implements PipelinePlugIn{
	
	
	private int centreNote = 60;
	private int contourRange = 15;
	private int[] phraseRange = new int[]{5, 10};
	private int phraseRangeIndex = 0;
	private int phraseCount = 4;
	private double[] phraseLength = new double[]{16};		// cycles through this. remember phrase length in beats
	private int phraseLengthIndex = 0;
	private double phrasePercent = .55;
	private int[] endStrength = new int[]{WEAK, STRONG};
	private double default_note_length = 0.25;
	private double noteDensity = WHOLE_NOTE;		

//	private ResourceObject ro;
//	private ContourData cd;
//	private MelodyContour mc;
	
	private void doParams(){
		setName("Mel_Phrase");
		setSortIndex(10);
		setInputSort(0);
		setZone(1);
		setCanDouble(false);
		setActive(1);
	}
	
	
	public PlugInMelodyPhrasedGuideTone(){
		doParams();
//		this.ro = ro;
		
	}
	public void process(PipelineNoteList pnl, PlayPlugArgument ppa){
		if (active > 0){
//			System.out.println(ppa.cp.toString());
//			System.out.println(ppa.cf.toString());
			
			setLength(pnl, ppa);
//			System.out.println("pnl length is: " + pnl.length);
			
			addChordTones(pnl, ppa);
//			System.out.println(pnl.toString());
//			pnl.length = ppa.cf.length() * (ppa.cd.phraseLength + 1);

//			addGuideTone(pnl, ppa);
//			addChordAnalysis(pnl, form);
//			addNotes(pnl);
//			doVelocity(pnl, p);
		}
		
	}
	public void setNoteDensity(double d){
		noteDensity = d;
	}

	
// privates ------------------------------------------------------------------
	private void addChordTones(PipelineNoteList pnl, PlayPlugArgument ppa){
		resetPhraseLengthIndex();
		resetPhraseRangeIndex();
		double phraseStartPos = 0;
		double contourPosLength = makeContourPosLength();
		ppa.cp.resetReadIndex();
		for (int i = 0; i < phraseCount; i++){
//			System.out.println(ppa.toString());
//			double  x = phraseStartPos / contourPosLength;
//			System.out.println(x);
//			ContourPack xxxz = ppa.cp;
//			FourPointContour xxxxx = xxxz.contour;
//			double zzz = xxxxx.getValue(x);
			int phraseCentreNote = centreNote + (int)(ppa.cp.contour.getValue(phraseStartPos / contourPosLength) * contourRange);
			int phraseNoteRange = nextPhraseRange();
			double currentPhraseLength = nextPhraseLength();
			
//			System.out.println("doing phrase: " + i);
//			System.out.println("phrse % pos: " + phraseStartPos / contourPosLength + ", phraseCentreNote " + phraseCentreNote);
			
			
			addPhrase(pnl, ppa, phraseStartPos, currentPhraseLength, phraseCentreNote, phraseNoteRange);
				
			phraseStartPos += currentPhraseLength;	
		}
	}
	private void addPhrase(
			PipelineNoteList pnl, 
			PlayPlugArgument ppa, 
			double phraseStartPos, 
			double currentPhraseLength, 
			int phraseCentreNote,
			int phraseNoteRange
			){
		
		ArrayList<PipelineNoteObject> pnoList = new ArrayList<PipelineNoteObject>();
		FourPointContour fpc = ppa.cp.nextContour();
//		System.out.println(fpc.toString());
//		System.out.println("phraseStartPos: " + phraseStartPos + " currentPhraseLength: " + currentPhraseLength);
		for (double pos = 0; pos < currentPhraseLength; pos += noteDensity){
//			System.out.println("doing pos: " + pos);
			if (pos / currentPhraseLength < phrasePercent){
				double absolutePos = phraseStartPos + pos;
				PipelineNoteObject pno = new PipelineNoteObject(absolutePos, true, true);
				double contourPos = pos / (currentPhraseLength * phrasePercent);
				double contourValue = fpc.getValue(contourPos);
				double preNote = phraseCentreNote + contourValue * phraseNoteRange;
				pno.ciko = ppa.cf.getPrevailingCIKO(absolutePos);
				int note = getClosestChordTone((int)(preNote + 0.5), pno.ciko);
//				System.out.println("preNote: " + preNote + " contourPos: " + contourPos + " contourValue: " + contourValue);				
				pno.addNote(note);			
				pno.setFixedLength(1.0, default_note_length);		// 1.0 = % of full length: 
				pno.setFixedVelocity(DAGlobals.melodyDynamic[ppa.dynamic]);
				pnoList.add(pno);
				// still need to implement end note strength and set last note isEndNote = true;
			}	
		}
		pnl.add(pnoList);
	}
	private double makeContourPosLength(){
		// makes a length amounting to all the phrase lengths except the last one, so that the 
		// last phrase centre note will be the very last point on the graph
		double length = 0;
		int index = 0;
		for (int i = 0; i < phraseCount - 1; i++){
			length += phraseLength[index];
			index++;
			if (index >= phraseLength.length) index = 0;
		}
		return length;
	}
	private void setLength(PipelineNoteList pnl, PlayPlugArgument ppa){
		double length = 0;
		int index = 0;
		for (int i = 0; i < phraseCount; i++){
			length += phraseLength[index];
			index++;
			if (index >= phraseLength.length) index = 0;
		}
		pnl.length = length;
	}
	private void addGuideTone(PipelineNoteList pnl, PlayPlugArgument ppa){
		ArrayList<PipelineNoteObject> pnoList = new ArrayList<PipelineNoteObject>();
		for (double pos = 0.0; pos < pnl.length; pos = pos + 4.0){			// 4.0 - assumes 4/4 time for a note every bar						
			PipelineNoteObject pno = new PipelineNoteObject(pos, true, true);
			pno.ciko = ppa.cf.getPrevailingCIKO(pos);
//			pno.contourDoubleValue = mc.getContourValue(pos, ppa);
			int contourNote = (int)(pno.contourDoubleValue * contourRange) + centreNote;
			int note = getClosestChordTone(contourNote, pno.ciko);			
			pno.addNote(note);			
			pno.setFixedLength(1.0, default_note_length);		// 1.0 = % of full length: 
			pno.setFixedVelocity(DAGlobals.melodyDynamic[ppa.dynamic]);
			pnoList.add(pno);
		}
		pnl.add(pnoList);
	}
	private int getClosestChordTone(int contourNote, ChordInKeyObject ciko){
		int offset = 0;
		while (true){
			if (isChordTone(contourNote + offset, ciko)) return contourNote + offset;
			if (isChordTone(contourNote - offset, ciko)) return contourNote - offset;
			offset++;
		}
	}
	private boolean isChordTone(int note, ChordInKeyObject ciko){
		for (int testNote: ciko.getModChordTones()){
			if (note % 12 == testNote % 12) return true;		// magic 12 - 12 notes in octave.....
		}
		return false;
	}
	private int nextPhraseRange(){
		int temp = phraseRange[phraseRangeIndex];
		phraseRangeIndex++;
		if (phraseRangeIndex >= phraseRange.length) phraseRangeIndex = 0;
		return temp;
	}
	private void resetPhraseRangeIndex(){
		phraseRangeIndex = 0;
	}
	private double nextPhraseLength(){
		double temp = phraseLength[phraseLengthIndex];
		phraseLengthIndex++;
		if (phraseLengthIndex >= phraseLength.length) phraseLengthIndex = 0;
		return temp;
	}
	private void resetPhraseLengthIndex(){
		phraseLengthIndex = 0;
	}
	
	private static final int WEAK = 0;
	private static final int STRONG = 1;
	private static final double WHOLE_NOTE = 4.0;
	private static final double HALF_NOTE = 2.0;
	private static final double QUARTER_NOTE = 1.0;
}
