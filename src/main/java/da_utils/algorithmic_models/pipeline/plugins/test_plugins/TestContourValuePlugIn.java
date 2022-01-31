package main.java.da_utils.algorithmic_models.pipeline.plugins.test_plugins;
import java.util.ArrayList;

import main.java.da_utils.algorithmic_models.pipeline.pipeline_note_list.PipelineNoteList;
import main.java.da_utils.algorithmic_models.pipeline.pipeline_note_list.PipelineNoteObject;
import main.java.da_utils.algorithmic_models.pipeline.plugins.Pluggable;
import main.java.da_utils.algorithmic_models.pipeline.plugins.filter_sort.FilterObject;
import main.java.da_utils.algorithmic_models.pipeline.plugins.pipeline_plugin.PipelinePlugIn;
import main.java.da_utils.algorithmic_models.pipeline.plugins.pipeline_plugin.PlayPlugArgument;

/*
 * this plugin iterates through the pnoList and assigns a contourValue from a preset list of 
 * values. The preset list will be cycled through if the length of the pnoList exceeds the length of
 * the preset list
 */

public class TestContourValuePlugIn extends Pluggable implements PipelinePlugIn{
	

	
	private void doParams(){
		setName("TestContourValuePlugIn()");
		setSortIndex(20);
		setInputSort(0);
		setZone(1);
		setCanDouble(false);
		setActive(1);
	}
	
	public TestContourValuePlugIn(){
		doParams();
	}
	public void process(PipelineNoteList pnl, PlayPlugArgument ppa){
		ArrayList<Integer> contourList = makeContourOptions();
		int index = 0;
		for (PipelineNoteObject pno: pnl.pnoList){
			pno.contourValue = contourList.get(index);
			index++;
			if (index == contourList.size()) index = 0;
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
	
// privates -------------------------------------------------------------------------
	private ArrayList<Integer> makeContourOptions(){
		ArrayList<Integer> iList = new ArrayList<Integer>();
		iList.add(0);
		iList.add(5);
		iList.add(9);
		iList.add(3);
		return iList;
	}


}
