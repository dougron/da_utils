package main.java.da_utils.algorithmic_models.pipeline.plugins.controllers;

import main.java.da_utils.ableton_live.ableton_live_clip.LiveMidiNote;
import main.java.da_utils.ableton_live.ableton_live_clip.controller_data_clips.PitchBendClip;
import main.java.da_utils.algorithmic_models.pipeline.pipeline_note_list.PipelineNoteList;
import main.java.da_utils.algorithmic_models.pipeline.pipeline_note_list.PipelineNoteObject;
import main.java.da_utils.algorithmic_models.pipeline.plugins.Pluggable;
import main.java.da_utils.algorithmic_models.pipeline.plugins.pipeline_plugin.PipelinePlugIn;
import main.java.da_utils.algorithmic_models.pipeline.plugins.pipeline_plugin.PlayPlugArgument;
import main.java.da_utils.chord_scale_dictionary.chord_analysis.ChordAnalysisObject;
import main.java.da_utils.resource_objects.ChordForm;

public class PlugInRandomBendOnLongNote extends Pluggable implements PipelinePlugIn{
	
	private double minimumLength;
	private double bendChance;

	private static final double pitchBendOff = 0.5;	
	private double pitchBendRange = 7.0;
	private double returnToZeroLength = 0.01;			// the length given for a bend to return to zero, so that points do not get mixed up by falling on the same position
	
	
	private void doParams(){
		setName("PBLong");
		setSortIndex(50);
		setInputSort(0);
		setZone(2);
		setCanDouble(false);
		setActive(1);
	}
	
	public PlugInRandomBendOnLongNote(double minimumLength, double bendChance){
		doParams();
		this.minimumLength = minimumLength;
		this.bendChance = bendChance;
		name = name + minimumLength + "," + bendChance;
	}
	public void process(PipelineNoteList pnl, PlayPlugArgument ppa){
		if (active > 0 && pnl.pnoList.size() > 0){
//			p.parent.parent.post("PlugInRandomBendOnLongNote processed as active: minimum length: " + minimumLength);
			PitchBendClip pbc = new PitchBendClip(ppa.clipObjectIndex, pnl.length);
			pbc.pitchBendRange = pitchBendRange;
			addStartAndEnd(pbc);
			for (PipelineNoteObject pno: pnl.pnoList){
//				p.parent.parent.post("PipelineNoteObject pos: " + pno.position + " length: " + pno.length);
				if (pno.length * pno.legatoModel >= minimumLength){
					if (ppa.rnd.next() <= bendChance){
						makeBend(pbc, pno.position, pno.length, ppa);
					}
				}
			}
//			p.parent.postSplit(pbc.toString(), "\n");
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
}
