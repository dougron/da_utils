package main.java.da_utils.algorithmic_models.pipeline.plugins.drums;
import main.java.da_utils.ableton_live.ableton_live_clip.LiveMidiNote;
import main.java.da_utils.algorithmic_models.pipeline.pipeline_note_list.PipelineNoteList;
import main.java.da_utils.algorithmic_models.pipeline.pipeline_note_list.PipelineNoteObject;
import main.java.da_utils.algorithmic_models.pipeline.plugins.Pluggable;
import main.java.da_utils.algorithmic_models.pipeline.plugins.filter_sort.FilterObject;
import main.java.da_utils.algorithmic_models.pipeline.plugins.pipeline_plugin.PipelinePlugIn;
import main.java.da_utils.algorithmic_models.pipeline.plugins.pipeline_plugin.PlayPlugArgument;


public class PlugInSnrToRim extends Pluggable implements PipelinePlugIn{
	

	
	private void doParams(){
		setName("Snr_Rim");
		setSortIndex(5);
		setInputSort(0);
		setZone(0);
		setCanDouble(false);
		setActive(1);
	}
	
	public PlugInSnrToRim(){
		doParams();
	}

	public void process(PipelineNoteList pnl, PlayPlugArgument ppa){
		if (active > 0){			
			setSnareNote(pnl, DrumStaticVariables.rimNote);
		} else {
			setSnareNote(pnl, DrumStaticVariables.snrNote);
		}
		
	}
	
	public String name(){
		return name;
	}
	public String originalName(){
		return name();
	}
	public int sortIndex(){
		return sortIndex;
	}
	public int inputSort(){
		return inputSort;
	}
	public int zone(){
		return zone;
	}
	public boolean canDouble(){
		return canDouble;
	}
	public void setInputSort(int i){
		inputSort = i;
	}
	public int active(){
		return active;
	}
	public void setActive(int i){
		active = i;
	}
	public FilterObject getFilterObject(){
		return new FilterObject(originalName(), this);
	}
	
// privates -----------------------------------------------------------------------------
	private void setSnareNote(PipelineNoteList pnl, int note){
		for (PipelineNoteObject pno: pnl.pnoList){
			if (pno.descriptor == DrumStaticVariables.snrDescriptor){
				for (LiveMidiNote lmn: pno.noteList){
					lmn.note = note;
				}
			}
		}		
	}
}


