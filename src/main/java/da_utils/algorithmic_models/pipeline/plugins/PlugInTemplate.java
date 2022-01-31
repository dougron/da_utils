package main.java.da_utils.algorithmic_models.pipeline.plugins;


import main.java.da_utils.algorithmic_models.pipeline.pipeline_note_list.PipelineNoteList;
import main.java.da_utils.algorithmic_models.pipeline.plugins.filter_sort.FilterObject;
import main.java.da_utils.algorithmic_models.pipeline.plugins.pipeline_plugin.PipelinePlugIn;
import main.java.da_utils.algorithmic_models.pipeline.plugins.pipeline_plugin.PlayPlugArgument;

public class PlugInTemplate extends Pluggable implements PipelinePlugIn{
	
	private String name = "XXXXXX";			// abrreviated name for GUI space issue
	private int sortIndex = 0;
	private int inputSort = 0;
	private int zone = 2;
	private boolean canDouble = false;
	private int active = 1;

	
	public PlugInTemplate(){

	}
	public void process(PipelineNoteList pnl, PlayPlugArgument ppa){
		if (active == 1) {
			
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
	
}

