package main.java.da_utils.algorithmic_models.pipeline.plugins.pipeline_plugin;

import main.java.da_utils.algorithmic_models.pipeline.pipeline_note_list.PipelineNoteList;
import main.java.da_utils.algorithmic_models.pipeline.plugins.Pluggable;
import main.java.da_utils.algorithmic_models.pipeline.plugins.filter_sort.FilterObject;
import main.java.da_utils.algorithmic_models.pipeline.plugins.filter_sort.FilterSortInterface;

/*
 * class to deal with duplicates of plugins in the pipeline, and handle the sorting properly
 * 
 * These plugins cannot take outside parameters, as duplicates may recieve the same set of parameters and, 
 * whilst all hell may not break loose, things may not go as planned.........
 */
		
public class PipelinePlugInSortWrapper extends Pluggable implements PipelinePlugIn{
	
	private Pluggable plug;
	private int inputSort = 0;
	private int active = 1;


	private void doParams(){
		setName("HiBass");
		setSortIndex(0);
		setInputSort(0);
		setZone(0);
		setCanDouble(true);
		setActive(1);
	}
	
	public PipelinePlugInSortWrapper(Pluggable ppi, int inputIndex){
		doParams();
		plug = (Pluggable)ppi;
		inputSort = inputIndex;
	}
	public void process(PipelineNoteList pnl, PlayPlugArgument ppa){
		if (active > 0){
			plug.process(pnl, ppa);
		}		
	}
	
	public String name(){
		return plug.name() + " " + inputSort + " in wrapper.";
	}
	public String originalName(){
		return plug.originalName();
	}
//	@Override
	public FilterObject getFilterObject(){
		return new FilterObject(originalName(), getFilteSortObject());
	}
//	public FilterObject getFilterObject(){
//		return new FilterObject(originalName(), getFilteSortObject());
//	}
	public FilterSortInterface getFilteSortObject(){
		return plug.getFilteSortObject();
	}
}
