package main.java.da_utils.algorithmic_models.pipeline.plugins.drums;
import java.util.ArrayList;

import main.java.da_utils.ableton_live.ableton_live_clip.LiveMidiNote;
import main.java.da_utils.algorithmic_models.pipeline.pipeline_note_list.PipelineNoteList;
import main.java.da_utils.algorithmic_models.pipeline.pipeline_note_list.PipelineNoteObject;
import main.java.da_utils.algorithmic_models.pipeline.plugins.Pluggable;
import main.java.da_utils.algorithmic_models.pipeline.plugins.pipeline_plugin.PipelinePlugIn;
import main.java.da_utils.algorithmic_models.pipeline.plugins.pipeline_plugin.PlayPlugArgument;
import main.java.da_utils.resource_objects.AccentTemplate;

public class PlugInKikFromAccentTemplate extends Pluggable implements PipelinePlugIn{
	

	
	private void doParams(){
		setName("Kik_RB");
		setSortIndex(0);
		setInputSort(0);
		setZone(0);
		setCanDouble(false);
		setActive(1);
	}
	
	public PlugInKikFromAccentTemplate(){
		doParams();
//		this.ro = ro;
	}

	public void process(PipelineNoteList pnl, PlayPlugArgument ppa){
		if (active > 0){
			pnl.length = ppa.at.length;
			
			ArrayList<PipelineNoteObject> pnoTempList = addRhythmPositions(ppa.at, pnl);
			addNotes(pnoTempList);
			doVelocity(pnoTempList, ppa);
			
			pnl.add(pnoTempList);
		}
		
	}

	
// privates -----------------------------------------------------------------------------
	private void doVelocity(ArrayList<PipelineNoteObject> pnoList, PlayPlugArgument ppa){		
		for (PipelineNoteObject pno: pnoList){
			pno.setFixedVelocity(DrumStaticVariables.kikDynamic[ppa.dynamic]);			
		}
	}
	private void addNotes(ArrayList<PipelineNoteObject> pnoList){
		for (PipelineNoteObject pno: pnoList){
			pno.addNote(DrumStaticVariables.kikNote);
		}
	}	
	private ArrayList<PipelineNoteObject> addRhythmPositions(AccentTemplate at, PipelineNoteList pnl){
		ArrayList<PipelineNoteObject> pnoList = new ArrayList<PipelineNoteObject>();
		for (LiveMidiNote lmn: at.clip.noteList){
			PipelineNoteObject pno = new PipelineNoteObject(lmn.position, true, true);
			pnoList.add(pno);
		}
		return pnoList;
	}
//	private boolean kikPosNotTaken(PipelineNoteList pnl, double pos){
//		for (PipelineNoteObject pno: pnl.pnoList){
//			if (pno.position == pos && pno.descriptor == DrumStaticVariables.kikDescriptor){
//				return false;
//			}
//		}
//		return true;
//	}
}
