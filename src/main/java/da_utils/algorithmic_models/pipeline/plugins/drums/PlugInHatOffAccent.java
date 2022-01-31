package main.java.da_utils.algorithmic_models.pipeline.plugins.drums;

import main.java.da_utils.ableton_live.ableton_live_clip.LiveMidiNote;
import main.java.da_utils.algorithmic_models.pipeline.pipeline_note_list.PipelineNoteList;
import main.java.da_utils.algorithmic_models.pipeline.pipeline_note_list.PipelineNoteObject;
import main.java.da_utils.algorithmic_models.pipeline.plugins.Pluggable;
import main.java.da_utils.algorithmic_models.pipeline.plugins.pipeline_plugin.PipelinePlugIn;
import main.java.da_utils.algorithmic_models.pipeline.plugins.pipeline_plugin.PlayPlugArgument;


public class PlugInHatOffAccent extends Pluggable implements PipelinePlugIn{
	


	private int velocity_decrement = 20;
	
	private void doParams(){
		setName("Hat_AcOff");
		setSortIndex(0);
		setInputSort(0);
		setZone(2);
		setCanDouble(false);
		setActive(1);
	}
	
	public PlugInHatOffAccent(){
		doParams();
		
	}

	public void process(PipelineNoteList pnl, PlayPlugArgument ppa){
		if (active > 0){
			for (PipelineNoteObject pno: pnl.pnoList){
				if (pno.descriptor == DrumStaticVariables.hatDescriptor){
					double x = pno.position - (int)(pno.position);
					if (pno.position - (int)(pno.position) != 0.5){		//magic 0.5: if 0.5 remains, its an off eigth note
						pno.velocity -= velocity_decrement;
						if (pno.velocity < 0){
							pno.velocity = 1;
						}
						doNotes(pno);
					}
				}
			}
		}
		
	}

	
// privates -----------------------------------------------------------------------------

	private void doNotes(PipelineNoteObject pno){
		for (LiveMidiNote lmn: pno.noteList){
			lmn.velocity = pno.velocity;
		}
	}
}

