package main.java.da_utils.algorithmic_models.pipeline.plugins.keys;
import java.util.Iterator;

import LegacyStuff.DAGlobals;
import main.java.da_utils.algorithmic_models.pipeline.pipeline_note_list.PipelineNoteList;
import main.java.da_utils.algorithmic_models.pipeline.pipeline_note_list.PipelineNoteObject;
import main.java.da_utils.algorithmic_models.pipeline.plugins.Pluggable;
import main.java.da_utils.algorithmic_models.pipeline.plugins.centre_adjust.CentreAdjust;
import main.java.da_utils.algorithmic_models.pipeline.plugins.pipeline_plugin.PipelinePlugIn;
import main.java.da_utils.algorithmic_models.pipeline.plugins.pipeline_plugin.PlayPlugArgument;
import main.java.da_utils.chord_progression_analyzer.chord_chunk.ChordChunk;
import main.java.da_utils.resource_objects.ChordForm;


public class PlugInKeysPad extends Pluggable implements PipelinePlugIn, CentreAdjust{
	


	private static final int default_velocity = 100;
	private static final double default_noteLength = 0.25;

	private int centreOfGravityNote = 48;
//	private TwoBarRhythmBuffer rb;
//	private ChordForm form;
	
	private void doParams(){
		setName("Keys_Pad");
		setSortIndex(0);
		setInputSort(0);
		setZone(0);
		setCanDouble(false);
		setActive(1);
	}
//	
	public PlugInKeysPad(){
		doParams();
//		this.ro = ro;
	}
	
	public PlugInKeysPad(int centreOfGravity){
		centreOfGravityNote = centreOfGravity;
		doParams();

	}

	public void process(PipelineNoteList pnl, PlayPlugArgument ppa){
		if (active > 0){
			pnl.length = ppa.cf.length();
//			TwoBarRhythmBuffer rb = p.getRhythmBuffer();	
			ChordForm form = ppa.cf;


			addRhythmPositions(pnl, form);
			doLengths(pnl);
			addNotes(pnl);
			doVelocity(pnl, ppa);
		}
		
	}
	
// CentreAdjust method(s) ----------------------------------------------------
	
	@Override
	public void addjustCentre(int semitones) {
		centreOfGravityNote += semitones;
			
	}

	
// privates -----------------------------------------------------------------------------
	private void doVelocity(PipelineNoteList pnl, PlayPlugArgument ppa){		
		for (PipelineNoteObject pno: pnl.pnoList){
			pno.setFixedVelocity(DAGlobals.keysDynamic[ppa.dynamic]);
			pno.setLengthsToLegatoModel(0.99);
		}
	}
	private void addNotes(PipelineNoteList pnl){
		for (PipelineNoteObject pno: pnl.pnoList){
			addClosestNotesToCentre(pno, centreOfGravityNote);
		}
	}
	private void addClosestNotesToCentre(PipelineNoteObject pno, int centre){
		for (int n: pno.ciko.getModChordTones()){
			int note = n % 12;
			int dist = 1000;
			for (int i = 0; i < 12; i++){		// 12 octaves.......
				if (Math.abs(centre - note) < dist){
					dist = Math.abs(centre - note);
					note += 12;
				} else {
					pno.addNote(note);
					break;
				}
			}
		}
	}
	private void doLengths(PipelineNoteList pnl){
		for (int i = 0; i < pnl.pnoList.size() - 1; i++){
			pnl.pnoList.get(i).length = pnl.pnoList.get(i + 1).position - pnl.pnoList.get(i).position;
		}
		if (pnl.pnoList.size() > 0){
			pnl.pnoList.get(pnl.pnoList.size() - 1).length = pnl.length - pnl.pnoList.get(pnl.pnoList.size() - 1).position;
		}		
	}
		
	private void addRhythmPositions(PipelineNoteList pnl, ChordForm form){
		Iterator<ChordChunk> ccIt = form.getChordAnalysisIterator();
		while (ccIt.hasNext()){
			ChordChunk cc = ccIt.next();
			PipelineNoteObject pno = new PipelineNoteObject(cc.position(), true, true);
			pno.cc = cc;
			pno.ciko = form.getPrevailingCIKO(cc.position());
			pnl.addNoteObject(pno);
		}
	}

}
