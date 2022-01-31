package main.java.da_utils.algorithmic_models.pipeline.plugins.embellishments;

import main.java.da_utils.algorithmic_models.pipeline.pipeline_note_list.PipelineNoteList;
import main.java.da_utils.algorithmic_models.pipeline.pipeline_note_list.PipelineNoteObject;
import main.java.da_utils.algorithmic_models.pipeline.plugins.Pluggable;
import main.java.da_utils.algorithmic_models.pipeline.plugins.pipeline_plugin.PipelinePlugIn;
import main.java.da_utils.algorithmic_models.pipeline.plugins.pipeline_plugin.PlayPlugArgument;

/*
 * applies legato. legatoModel represents a set of percentages that are cycled through for each 
 * PipelineNoteObject. 0.0 means default length
 */
public class PlugInLegato extends Pluggable implements PipelinePlugIn{
	

	public double[] legatoModel;

	
	private void doParams(){
		setName("Leg");
		setSortIndex(0);
		setInputSort(0);
		setZone(2);
		setCanDouble(true);
		setActive(1);
	}
	
	public PlugInLegato(double[] d){
		doParams();
		legatoModel = d;
		for (double dd: legatoModel){
			name = name + dd + ",";
		}
	}
	
	
	public void process(PipelineNoteList pnl, PlayPlugArgument ppa){
		if (active > 0 && pnl.pnoList.size() > 0){
//			p.parent.parent.post("PlugInLegato processed as active");
			int modelIndex = 0;
			for (int i = 0; i < pnl.pnoList.size() - 1; i++){
				PipelineNoteObject thisNote = pnl.pnoList.get(i);
				PipelineNoteObject nextNote = pnl.pnoList.get(i + 1);
				double legato = legatoModel[modelIndex];
				adjustLegatoParameter(thisNote, nextNote.position, legato);
				modelIndex++;
				if (modelIndex >= legatoModel.length) modelIndex = 0;
			}
			PipelineNoteObject thisNote = pnl.pnoList.get(pnl.pnoList.size() - 1);
			double legato = legatoModel[modelIndex];
			adjustLegatoParameter(thisNote, pnl.length, legato);
		}		
	}

	
// privates ------------------------------------------------------------------------------------------
	
	private void adjustLegatoParameter(PipelineNoteObject thisNote, double nextNotePos, double model){
		if (model > 0.0){							// each note is default_length by default.
			thisNote.setFixedLength(model, (nextNotePos - thisNote.position) * model);
		} else {
			thisNote.setLengthsToLegatoModel(0.0);
		}
	}

}
