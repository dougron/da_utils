package main.java.da_utils.algorithmic_models.pipeline.plugins.bass;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;

import LegacyStuff.DAGlobals;
import main.java.da_utils.ableton_live.ableton_live_clip.LiveClip;
import main.java.da_utils.algorithmic_models.pipeline.pipeline_note_list.PipelineNoteList;
import main.java.da_utils.algorithmic_models.pipeline.pipeline_note_list.PipelineNoteObject;
import main.java.da_utils.algorithmic_models.pipeline.plugins.Pluggable;
import main.java.da_utils.algorithmic_models.pipeline.plugins.centre_adjust.CentreAdjust;
import main.java.da_utils.algorithmic_models.pipeline.plugins.pipeline_plugin.PipelinePlugIn;
import main.java.da_utils.algorithmic_models.pipeline.plugins.pipeline_plugin.PlayPlugArgument;
import main.java.da_utils.chord_progression_analyzer.ChordInKeyObject;
import main.java.da_utils.chord_progression_analyzer.chord_chunk.ChordChunk;
import main.java.da_utils.resource_objects.ChordForm;
import main.java.da_utils.resource_objects.MelodyContour;
import main.java.da_utils.test_utils.TestData;

public class PlugInBassRootEveryBar  extends Pluggable implements PipelinePlugIn, CentreAdjust{
	
	private int centreNote = 36;
	private int range = 15;

//	private ResourceObject ro;
//	private ContourData cd;
	private MelodyContour mc;
	private double default_note_length = 0.25;
	
	private void doParams(){
		setName("Bass_root");
		setSortIndex(0);
		setInputSort(0);
		setZone(0);
		setCanDouble(false);
		setActive(1);
	}
	
	public PlugInBassRootEveryBar(){
		doParams();
//		this.ro = ro;
		
	}
	public PlugInBassRootEveryBar(int range){
		doParams();
		this.range = range;
//		this.ro = ro;
		
	}
	public PlugInBassRootEveryBar(int centreNote, int range){
		doParams();
		this.range = range;
		this.centreNote = centreNote;
//		this.ro = ro;
		
	}
	public void process(PipelineNoteList pnl, PlayPlugArgument ppa){
		if (active > 0 && ppa.cf != null){
//			mc = new MelodyContour();
			pnl.length = ppa.cf.length();

			addRoot(pnl, ppa);
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
	private void addRoot(PipelineNoteList pnl, PlayPlugArgument ppa){
		ArrayList<PipelineNoteObject> pnoList = new ArrayList<PipelineNoteObject>();
		Iterator<ChordChunk> cit = ppa.cf.getChordAnalysisIterator();
		double barLength = ppa.cf.lc().getBarLengthInQuarters();
		double pos = 0.0;
		double len;
		while (cit.hasNext()){
			ChordChunk cc = cit.next();
			double cclen = cc.length();
			pnoList.addAll(getPNOForChordChunk(cclen, barLength, pos));
			pos += cclen;
		}
		for (PipelineNoteObject pno: pnoList){
			ChordInKeyObject ciko = ppa.cf.getPrevailingCIKO(pno.position);
			pno.addNote(lowestNoteInRange(ciko.rootIndex));
			pno.setFixedVelocity(ppa.velocity);
		}
//		for (double pos = 0.0; pos < pnl.length; pos = pos + 4.0){			// 4.0 - assumes 4/4 time for a note every bar						
			
			
			
			//			PipelineNoteObject pno = new PipelineNoteObject(pos, true, true);
//			pno.ciko = ppa.cf.getPrevailingCIKO(pos);
//			pno.contourDoubleValue = mc.getContourValue(pos, ppa);
//			int contourNote = (int)(pno.contourDoubleValue * range) + centreNote;
//			int note = getClosestChordTone(contourNote, pno.ciko);			
//			pno.addNote(note);			
//			pno.setFixedLength(1.0, default_note_length);		// 1.0 = % of full length: 
//			pno.setFixedVelocity(DAGlobals.melodyDynamic[ppa.dynamic]);
//			pnoList.add(pno);
//		}
		pnl.add(pnoList);
	}
	
	public int lowestNoteInRange(int rootIndex){
		while (true){
			if (rootIndex > centreNote - range){
				break;
			}
			rootIndex += 12;
		}
		return rootIndex;
	}
	
	public int closestNoteToCentre(int rootIndex, int centre) {
		int rootNote = rootIndex;
		int distance = 128;		// largest possible distance
		int tempdist = 127;
		int bestNote = 0;
		while (true){
			tempdist = Math.abs(centre - rootNote);
			if (tempdist >= distance){
				break;
			}
			distance = tempdist;
			bestNote = rootNote;
			rootNote += 12;		// octave
		}
		return bestNote;
	}

	private ArrayList<PipelineNoteObject> getPNOForChordChunk(double cclen, double barLength, double pos) {
		ArrayList<PipelineNoteObject> pnoList = new ArrayList<PipelineNoteObject>();
		
		while (true) {	
			if (cclen <= 0.0) break;
			if (cclen < barLength && cclen > 0.0) {
				PipelineNoteObject pno = new PipelineNoteObject(pos, true, true);
				pno.length = cclen;
				pnoList.add(pno);
				pos += cclen;
				break;
			} else {
				cclen -= barLength;
				PipelineNoteObject pno = new PipelineNoteObject(pos, true, true);
				pno.length = barLength;
				pnoList.add(pno);
				pos += barLength;
			}
		}
		return pnoList;
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
	
	
// ------ main -----------------------------------------------------------------
	
	public static void main(String[] args){
		
		ChordForm cf = new ChordForm(TestData.chordProgressionAmGFE7());
		System.out.println(cf.toString());
		
		PlayPlugArgument ppa = new PlayPlugArgument();
		ppa.cf = cf;
		ppa.velocity = 96;
		
		PlugInBassRootEveryBar plug = new PlugInBassRootEveryBar();
		PipelineNoteList pnl = new PipelineNoteList();
		plug.process(pnl, ppa);
		System.out.println(pnl.toString());
		LiveClip lc = pnl.makeLiveClip();
		System.out.println(lc.toString());
		
		CentreAdjust ca = (CentreAdjust)plug;
		ca.addjustCentre(12);
		
		pnl = new PipelineNoteList();
		plug.process(pnl, ppa);
		System.out.println(pnl.toString());
		lc = pnl.makeLiveClip();
		System.out.println(lc.toString());
		
//		System.out.println(plug.closestNoteToCentre(0, 36));
//		System.out.println(plug.closestNoteToCentre(1, 36));
//		System.out.println(plug.closestNoteToCentre(6, 36));
	}

	

}
