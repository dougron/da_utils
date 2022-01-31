package main.java.da_utils.algorithmic_models.pipeline.plugins.controllers;

import main.java.da_utils.ableton_live.ableton_live_clip.controller_data_clips.ControllerClip;
import main.java.da_utils.algorithmic_models.pipeline.pipeline_note_list.PipelineNoteList;
import main.java.da_utils.algorithmic_models.pipeline.plugins.Pluggable;
import main.java.da_utils.algorithmic_models.pipeline.plugins.pipeline_plugin.PipelinePlugIn;
import main.java.da_utils.algorithmic_models.pipeline.plugins.pipeline_plugin.PlayPlugArgument;
import main.java.da_utils.resource_objects.ChordForm;
import main.java.da_utils.test_utils.TestData;

/*
 * switches delay send on and off every quarter note for the duration of the loop
 */

public class PlugInDelaySendOnOffTest extends Pluggable implements PipelinePlugIn{
	
//	private double minimumLength;
//	private double bendChance;

//	private static final double pitchBendOff = 0.5;	
//	private double pitchBendRange = 7.0;
	private double returnToZeroLength = 0.01;			// the length given for a bend to return to zero, so that points do not get mixed up by falling on the same position
//	private int bendRange;
	
	
	private void doParams(){
		setName("DelayOnOff");
		setSortIndex(50);
		setInputSort(0);
		setZone(2);
		setCanDouble(false);
		setActive(1);
	}
	
	public PlugInDelaySendOnOffTest(){
		doParams();
		
//		name = name;
	}
	
	public void process(PipelineNoteList pnl, PlayPlugArgument ppa){
		if (active > 0){
			int contInd = 0;			// this is problematic. delay send controllers (or indeed any controllers)
										// should not be bound to what appears to be a position in a list
			ControllerClip cc = new ControllerClip(pnl.clipObjectIndex, contInd, ppa.cf.length());
			for (double pos = 0.0; pos < ppa.cf.length(); pos += 2.0){
				cc.addBreakPoint(pos, 1.0);
				cc.addBreakPoint(pos + 1.0 - returnToZeroLength, 1.0);
				cc.addBreakPoint(pos + 1.0, 0.0);
				cc.addBreakPoint(pos + 2.0 - returnToZeroLength, 0.0);
			}
			cc.name = "delaySend";
			pnl.cList.add(cc);
		}
		
	}

// privates ------------------------------------------------------------------------------
	
	
	
	
// ===== main ================================================================================
	
	public static void main(String[] args){
		ChordForm cf = new ChordForm(TestData.chordProgressionAmGFE7());
		PlayPlugArgument ppa = new PlayPlugArgument();
		ppa.cf = cf;
		PlugInDelaySendOnOffTest plug = new PlugInDelaySendOnOffTest();
		PipelineNoteList pnl = new PipelineNoteList();
		plug.process(pnl, ppa);
		
		System.out.println(pnl.toString());
		
		
		
	}
	
	
	
	
}
