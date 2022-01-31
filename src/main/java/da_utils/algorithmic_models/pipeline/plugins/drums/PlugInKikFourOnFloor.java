package main.java.da_utils.algorithmic_models.pipeline.plugins.drums;
import java.util.ArrayList;

import main.java.da_utils.algorithmic_models.pipeline.pipeline_note_list.PipelineNoteList;
import main.java.da_utils.algorithmic_models.pipeline.pipeline_note_list.PipelineNoteObject;
import main.java.da_utils.algorithmic_models.pipeline.plugins.Pluggable;
import main.java.da_utils.algorithmic_models.pipeline.plugins.pipeline_plugin.PipelinePlugIn;
import main.java.da_utils.algorithmic_models.pipeline.plugins.pipeline_plugin.PlayPlugArgument;



public class PlugInKikFourOnFloor extends Pluggable implements PipelinePlugIn{
	

	
	private void doParams(){
		setName("Kik_4on4");
		setSortIndex(0);
		setInputSort(0);
		setZone(0);
		setCanDouble(false);
		setActive(1);
	}
	
	public PlugInKikFourOnFloor(){
		doParams();
//		this.ro = ro;
	}

	public void process(PipelineNoteList pnl, PlayPlugArgument ppa){
		if (active > 0){
//			MaxObject.post("PlugInBassFromRhythmBuffer processed as active");
			pnl.length = ppa.cf.length();			
//			double formLength = p.getFormLength();	// seems redundant......	

			ArrayList<PipelineNoteObject> pnoTempList = addRhythmPositions(pnl);
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
	
	private ArrayList<PipelineNoteObject> addRhythmPositions(PipelineNoteList pnl){
		ArrayList<PipelineNoteObject> pnoList = new ArrayList<PipelineNoteObject>();
		for (double pos = 0.0; pos < pnl.length; pos++){
			if (kikPosNotTaken(pnl, pos)){
				pnoList.add(new PipelineNoteObject(pos, true, true, DrumStaticVariables.kikDescriptor));
			}			
		}
		return pnoList;
	}
	private boolean kikPosNotTaken(PipelineNoteList pnl, double pos){
		for (PipelineNoteObject pno: pnl.pnoList){
			if (pno.position == pos && pno.descriptor == DrumStaticVariables.kikDescriptor){
				return false;
			}
		}
		return true;
	}

}
