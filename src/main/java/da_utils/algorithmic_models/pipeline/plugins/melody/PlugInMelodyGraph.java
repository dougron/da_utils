package main.java.da_utils.algorithmic_models.pipeline.plugins.melody;

import main.java.da_utils.algorithmic_models.pipeline.pipeline_note_list.PipelineNoteList;
import main.java.da_utils.algorithmic_models.pipeline.plugins.Pluggable;
import main.java.da_utils.algorithmic_models.pipeline.plugins.pipeline_plugin.PipelinePlugIn;
import main.java.da_utils.algorithmic_models.pipeline.plugins.pipeline_plugin.PlayPlugArgument;


public class PlugInMelodyGraph extends Pluggable implements PipelinePlugIn{


	
	private double resolution = 1.0;	// default quarter note
	
	private void doParams(){
		setName("Mel_Graph");
		setSortIndex(10);
		setInputSort(0);
		setZone(1);
		setCanDouble(false);
		setActive(1);
	}
	public PlugInMelodyGraph(){
		doParams();
	}
	
	public void process(PipelineNoteList pnl, PlayPlugArgument ppa){
		if (active > 0){

		}		
	}


}
