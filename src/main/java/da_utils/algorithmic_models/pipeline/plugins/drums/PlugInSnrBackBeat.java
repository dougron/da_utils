package main.java.da_utils.algorithmic_models.pipeline.plugins.drums;
import java.util.ArrayList;

import main.java.da_utils.algorithmic_models.pipeline.pipeline_note_list.PipelineNoteList;
import main.java.da_utils.algorithmic_models.pipeline.pipeline_note_list.PipelineNoteObject;
import main.java.da_utils.algorithmic_models.pipeline.plugins.Pluggable;
import main.java.da_utils.algorithmic_models.pipeline.plugins.pipeline_plugin.PipelinePlugIn;
import main.java.da_utils.algorithmic_models.pipeline.plugins.pipeline_plugin.PlayPlugArgument;


public class PlugInSnrBackBeat extends Pluggable implements PipelinePlugIn{
	

	
	private void doParams(){
		setName("Snr_BkBt");
		setSortIndex(3);
		setInputSort(0);
		setZone(1);
		setCanDouble(false);
		setActive(1);
	}
	
	public PlugInSnrBackBeat(){
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
			pno.setFixedVelocity(DrumStaticVariables.snrDynamic[ppa.dynamic]);			
		}
	}
	private void addNotes(ArrayList<PipelineNoteObject> pnoList){
		for (PipelineNoteObject pno: pnoList){
			pno.addNote(DrumStaticVariables.snrNote);
		}
	}	
	
	private ArrayList<PipelineNoteObject> addRhythmPositions(PipelineNoteList pnl){
		ArrayList<PipelineNoteObject> pnoList = new ArrayList<PipelineNoteObject>();
		for (double pos = 1.0; pos < pnl.length; pos = pos + 2.0){
			if (snrPosNotTaken(pnl, pos)){
				pnoList.add(new PipelineNoteObject(pos, true, true, DrumStaticVariables.snrDescriptor));
			}			
		}
		return pnoList;
	}
	private boolean snrPosNotTaken(PipelineNoteList pnl, double pos){
		for (PipelineNoteObject pno: pnl.pnoList){
			if (pno.position == pos && pno.descriptor == DrumStaticVariables.snrDescriptor){
				return false;
			}
		}
		return true;
	}

}

