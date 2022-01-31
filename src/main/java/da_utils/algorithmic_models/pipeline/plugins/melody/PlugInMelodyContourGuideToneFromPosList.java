package main.java.da_utils.algorithmic_models.pipeline.plugins.melody;
import java.util.ArrayList;

import main.java.da_utils.ableton_live.ableton_live_clip.LiveMidiNote;
import main.java.da_utils.algorithmic_models.pipeline.Pipe;
import main.java.da_utils.algorithmic_models.pipeline.pipeline_note_list.PipelineNoteList;
import main.java.da_utils.algorithmic_models.pipeline.pipeline_note_list.PipelineNoteObject;
import main.java.da_utils.algorithmic_models.pipeline.plugins.Pluggable;
import main.java.da_utils.algorithmic_models.pipeline.plugins.pipeline_plugin.PipelinePlugIn;
import main.java.da_utils.algorithmic_models.pipeline.plugins.pipeline_plugin.PlayPlugArgument;
import main.java.da_utils.chord_progression_analyzer.ChordInKeyObject;
import main.java.da_utils.chord_scale_dictionary.chord_analysis.ChordAnalysisObject;
import main.java.da_utils.resource_objects.ChordForm;
import main.java.da_utils.resource_objects.ContourData;
import main.java.da_utils.resource_objects.MelodyContour;
import main.java.da_utils.resource_objects.RandomNumberSequence;
import main.java.da_utils.test_utils.TestData;
import LegacyStuff.DAGlobals;


public class PlugInMelodyContourGuideToneFromPosList extends Pluggable implements PipelinePlugIn{
	

	
	private int centreNote = 60;
	private int contourRange = 15;

//	private ResourceObject ro;
//	private ContourData cd;
	private MelodyContour mc;
	private double default_note_length = 0.25;
	private double[] posList;
	
	private void doParams(){
		setName("Mel_GTPos");
		setSortIndex(0);
		setInputSort(0);
		setZone(0);
		setCanDouble(false);
		setActive(1);
	}
	
	public PlugInMelodyContourGuideToneFromPosList(double[] posList){
		this.posList = posList;
		doParams();
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

	
// privates ------------------------------------------------------------------
	private void addGuideTone(PipelineNoteList pnl, PlayPlugArgument ppa){
		ArrayList<PipelineNoteObject> pnoList = new ArrayList<PipelineNoteObject>();
		int posListIndex = -1;
		
		for (double pos = 0.0; pos < pnl.length; pos = pos + posList[posListIndex]){			// 4.0 - assumes 4/4 time for a note every bar						
			PipelineNoteObject pno = new PipelineNoteObject(pos, true, true);
			pno.ciko = ppa.cf.getPrevailingCIKO(pos);
			pno.contourDoubleValue = mc.getContourValue(pos, ppa);
			int contourNote = (int)(pno.contourDoubleValue * contourRange) + centreNote;
			int note = getClosestChordTone(contourNote, pno.ciko);			
			pno.addNote(note);			
			pno.setFixedLength(1.0, default_note_length);		// 1.0 = % of full length: 
			pno.setFixedVelocity(DAGlobals.melodyDynamic[ppa.dynamic]);
			pnoList.add(pno);
			posListIndex++;
			if (posListIndex >= posList.length) posListIndex = 0;
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
	

	
// ========= main =====================================================
	public static void main(String[] args){
		Pipe p = new Pipe("test");
		ChordForm cf = new ChordForm(TestData.chordProgressionAmGFE7());
		PlayPlugArgument ppa = new PlayPlugArgument();
		RandomNumberSequence rnd = new RandomNumberSequence(16, 0);
		ContourData cd = new ContourData();
		ppa.cd = cd;
		ppa.cf = cf;
		ppa.rnd = rnd;
		double[] posList = new double[]{4.0};
		
		p.addPlugIn(new PlugInMelodyContourGuideToneFromPosList(posList));
		PipelineNoteList pnl = p.makeNoteList(ppa);
		System.out.println(pnl.toString());
	}

}
