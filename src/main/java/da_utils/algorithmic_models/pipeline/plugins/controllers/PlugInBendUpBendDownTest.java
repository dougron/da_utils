package main.java.da_utils.algorithmic_models.pipeline.plugins.controllers;

import main.java.da_utils.ableton_live.ableton_live_clip.controller_data_clips.PitchBendClip;
import main.java.da_utils.algorithmic_models.pipeline.pipeline_note_list.PipelineNoteList;
import main.java.da_utils.algorithmic_models.pipeline.plugins.Pluggable;
import main.java.da_utils.algorithmic_models.pipeline.plugins.pipeline_plugin.PipelinePlugIn;
import main.java.da_utils.algorithmic_models.pipeline.plugins.pipeline_plugin.PlayPlugArgument;
import main.java.da_utils.resource_objects.ChordForm;
import main.java.da_utils.test_utils.TestData;

public class PlugInBendUpBendDownTest extends Pluggable implements PipelinePlugIn{
	
//	private double minimumLength;
//	private double bendChance;

	private static final double pitchBendOff = 0.5;	
//	private double pitchBendRange = 7.0;
	private double returnToZeroLength = 0.01;			// the length given for a bend to return to zero, so that points do not get mixed up by falling on the same position
	private int bendRange;
	
	
	private void doParams(){
		setName("PBendUpDown");
		setSortIndex(50);
		setInputSort(0);
		setZone(2);
		setCanDouble(false);
		setActive(1);
	}
	
	public PlugInBendUpBendDownTest(int bendRange){
		doParams();
		this.bendRange = bendRange;
		name = name + bendRange;
	}
	public void process(PipelineNoteList pnl, PlayPlugArgument ppa){
		if (active > 0){
			PitchBendClip pbc = new PitchBendClip(ppa.clipObjectIndex, pnl.length);
			pbc.pitchBendRange = bendRange;
			addStartAndEnd(pbc);
			int loopLength = ppa.cf.barCount();
			double barLength = ppa.cf.barLength();
			boolean up = true;
			double pos = 0.0;
			for (int i = 0; i < loopLength; i++){
				if (up) {
					sawToothBendPoints(pbc, pos, barLength, 1.0);
					up = false;
				} else {
					sawToothBendPoints(pbc, pos, barLength, 0.0);
					up = true;
				}
				
				pos += barLength;
			}
			
			
			
			pnl.pb = pbc;
		}
		
	}

// privates ------------------------------------------------------------------------------
	
	private void addStartAndEnd(PitchBendClip pbc){
		pbc.addBreakPoint(0.0, pitchBendOff);
		pbc.addBreakPoint(pbc.length, pitchBendOff);
	}
	private void makeBend(PitchBendClip pbc, double pos, double len, PlayPlugArgument ppa){
		if (ppa.rnd.next() < 0.5){		// 50/50 chance bend up/down
			sawToothBendPoints(pbc, pos, len, 0.0);	// bend down
		} else {
			sawToothBendPoints(pbc, pos, len, 1.0);
		}
	}
	private void sawToothBendPoints(PitchBendClip pbc, double pos, double len, double value){
		// makes a sawtooth on the graph between pos + (len/2) and pos + len to peak at value
		pbc.addBreakPoint(pos + (len / 2), pitchBendOff);
		pbc.addBreakPoint(pos + len - returnToZeroLength, value);
		pbc.addBreakPoint(pos + len, pitchBendOff);
	}
	
	
// ===== main ================================================================================
	
	public static void main(String[] args){
		ChordForm cf = new ChordForm(TestData.chordProgressionAmGFE7());
		PlayPlugArgument ppa = new PlayPlugArgument();
		ppa.cf = cf;
		PlugInBendUpBendDownTest plug = new PlugInBendUpBendDownTest(12);
		PipelineNoteList pnl = new PipelineNoteList();
		plug.process(pnl, ppa);
		
		System.out.println(pnl.toString());
		
		
		
	}
	
	
	
	
}
