package main.java.da_utils.algorithmic_models.pipeline.plugins.drums;


import main.java.da_utils.ableton_live.ableton_live_clip.LiveMidiNote;
import main.java.da_utils.algorithmic_models.pipeline.pipeline_note_list.PipelineNoteList;
import main.java.da_utils.algorithmic_models.pipeline.pipeline_note_list.PipelineNoteObject;
import main.java.da_utils.algorithmic_models.pipeline.plugins.Pluggable;
import main.java.da_utils.algorithmic_models.pipeline.plugins.pipeline_plugin.PipelinePlugIn;
import main.java.da_utils.algorithmic_models.pipeline.plugins.pipeline_plugin.PlayPlugArgument;

public class PlugInHiBassLoBass extends Pluggable implements PipelinePlugIn{
	
	private int lowestNote = 60;
	private int highestNote = 79;
	
	private void doParams(){
		setName("HiBass");
		setSortIndex(10);
		setInputSort(0);
		setZone(0);
		setCanDouble(false);
		setActive(1);
	}
 	
	
	public PlugInHiBassLoBass(){
		doParams();
		
	}
	public void process(PipelineNoteList pnl, PlayPlugArgument ppa){
		if (active > 0 && pnl.pnoList.size() > 0){
//			p.parent.parent.post("PlugInHiBass processed as active"); bad type of call for console debugging
			for (PipelineNoteObject pno: pnl.pnoList){
				for (LiveMidiNote lmn: pno.noteList){
					while (lmn.note < lowestNote){
						lmn.note += 12;
					}
					while (lmn.note > highestNote){
						lmn.note -= 12;
					}
				}
			}
		}		
	}


}
