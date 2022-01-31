package main.java.da_utils.algorithmic_models.pipeline.plugins;

import main.java.da_utils.algorithmic_models.pipeline.pipeline_note_list.PipelineNoteList;
import main.java.da_utils.algorithmic_models.pipeline.plugins.filter_sort.FilterObject;
import main.java.da_utils.algorithmic_models.pipeline.plugins.filter_sort.FilterSortInterface;
import main.java.da_utils.algorithmic_models.pipeline.plugins.pipeline_plugin.PlayPlugArgument;

public class Pluggable implements FilterSortInterface{
	
	public String name = "Pluggable";			// abrreviated name for GUI space issue.informational
	public String idName = "Pluggable";			// for plugIn class identification
	public int sortIndex = 0;
	public int inputSort = 0;
	public int zone = 2;					// in general 0 = generators, 1 = midi processors, 2 = audio processors, I think
	public boolean canDouble = false;
	public int active = 1;
	public String customName = "";
	
	public static final String presetXMLExtension = "pluginpreset";

	
	public Pluggable(){

	}
	public void process(PipelineNoteList pnl, PlayPlugArgument ppa){
		if (active == 1) {
			
		}
	}
	public String name(){
		return name;
	}
	public String idName(){
		return idName;
	}
	public void setName(String str){
		name = str;
		idName = str;
	}
	public String originalName(){
		return name();
	}
	public int sortIndex(){
		return sortIndex;
	}
	public void setSortIndex(int sortIndex){
		this.sortIndex = sortIndex;
	}
	public int inputSort(){
		return inputSort;
	}
	public void setInputSort(int i){
		inputSort = i;
	}
	public int zone(){
		return zone;
	}
	public void setZone(int zone){
		this.zone = zone;
	}
	public boolean canDouble(){
		return canDouble;
	}
	public void setCanDouble(boolean b){
		canDouble = b;
	}
	
	public int active(){
		return active;
	}
	public void setActive(int i){
		active = i;
	}
	public FilterObject getFilterObject(){
		return new FilterObject(originalName(), getFilteSortObject());
	}
	public FilterSortInterface getFilteSortObject(){
		return this;
	}
	
	
}
