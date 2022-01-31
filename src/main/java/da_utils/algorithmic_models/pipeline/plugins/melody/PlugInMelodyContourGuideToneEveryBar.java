package main.java.da_utils.algorithmic_models.pipeline.plugins.melody;
import java.util.ArrayList;

import main.java.da_utils.ableton_live.ableton_live_clip.LiveMidiNote;
import main.java.da_utils.algorithmic_models.pipeline.pipeline_note_list.PipelineNoteList;
import main.java.da_utils.algorithmic_models.pipeline.pipeline_note_list.PipelineNoteObject;
import main.java.da_utils.algorithmic_models.pipeline.plugins.Pluggable;
import main.java.da_utils.algorithmic_models.pipeline.plugins.centre_adjust.CentreAdjust;
import main.java.da_utils.algorithmic_models.pipeline.plugins.pipeline_plugin.PipelinePlugIn;
import main.java.da_utils.algorithmic_models.pipeline.plugins.pipeline_plugin.PlayPlugArgument;
import main.java.da_utils.chord_progression_analyzer.ChordInKeyObject;
import main.java.da_utils.chord_scale_dictionary.chord_analysis.ChordAnalysisObject;
import main.java.da_utils.resource_objects.ChordForm;
import main.java.da_utils.resource_objects.MelodyContour;
import LegacyStuff.DAGlobals;


public class PlugInMelodyContourGuideToneEveryBar extends Pluggable implements PipelinePlugIn, CentreAdjust{
	

	
	private static final double DEFAULT_LEGATO_MODEL = 0.0;
	private int centreNote = 60;
	private int contourRange = 15;

//	private ResourceObject ro;
//	private ContourData cd;
	private MelodyContour mc;
	private double DEFAULT_NOTE_LENGTH = 0.25;
	
	private void doParams(){
		setName("Mel_GT1n");
		setSortIndex(0);
		setInputSort(0);
		setZone(0);
		setCanDouble(false);
		setActive(1);
	}
	
	public PlugInMelodyContourGuideToneEveryBar(){
		doParams();
//		this.ro = ro;
		
	}
	public PlugInMelodyContourGuideToneEveryBar(int range){
		doParams();
		contourRange = range;
//		this.ro = ro;
		
	}
	public PlugInMelodyContourGuideToneEveryBar(int centreNote, int range){
		doParams();
		contourRange = range;
		this.centreNote = centreNote;
//		this.ro = ro;
		
	}
	public void process(PipelineNoteList pnl, PlayPlugArgument ppa){
		if (active > 0){
			mc = new MelodyContour();
			pnl.length = ppa.cf.length() * (ppa.cd.phraseLength + 1);

			addGuideTone(pnl, ppa);
//			addChordAnalysis(pnl, form);
//			addNotes(pnl);
//			doVelocity(pnl, p);
		}
		
	}

	
	// CentreAdjust method(s) ----------------------------------------------------
	
	@Override
	public void addjustCentre(int semitones) {
		centreNote += semitones;
				
	}
	
// privates ------------------------------------------------------------------
	private void addGuideTone(PipelineNoteList pnl, PlayPlugArgument ppa){
		ArrayList<PipelineNoteObject> pnoList = new ArrayList<PipelineNoteObject>();
		for (double pos = 0.0; pos < pnl.length; pos = pos + 4.0){			// 4.0 - assumes 4/4 time for a note every bar						
			PipelineNoteObject pno = new PipelineNoteObject(pos, true, true);
			pno.ciko = ppa.cf.getPrevailingCIKO(pos);
			pno.contourDoubleValue = mc.getContourValue(pos, ppa);
			int contourNote = (int)(pno.contourDoubleValue * contourRange) + centreNote;
			int note = getClosestChordTone(contourNote, pno.ciko);			
			pno.addNote(note);			
			pno.setFixedLength(DEFAULT_LEGATO_MODEL, DEFAULT_NOTE_LENGTH);		// 1.0 = % of full length: 
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

}
