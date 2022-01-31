package main.java.da_utils.algorithmic_models.pipeline.plugins.test_plugins;
import main.java.da_utils.algorithmic_models.pipeline.pipeline_note_list.PipelineNoteList;
import main.java.da_utils.algorithmic_models.pipeline.plugins.Pluggable;
import main.java.da_utils.algorithmic_models.pipeline.plugins.filter_sort.FilterObject;
import main.java.da_utils.algorithmic_models.pipeline.plugins.pipeline_plugin.PipelinePlugIn;
import main.java.da_utils.algorithmic_models.pipeline.plugins.pipeline_plugin.PlayPlugArgument;

/*
 * assigns a PipelineNoteObject to the beginning of each bar in a four bar (4/4 time) PipelineNoteList
 * 
 * very much for testing......
 */
public class TestRhythmPlugIn extends Pluggable implements PipelinePlugIn{
	
	
	
	private void doParams(){
		setName("TestRhythmPlugIn()");
		setSortIndex(0);
		setInputSort(0);
		setZone(1);
		setCanDouble(false);
		setActive(1);
	}
	
	public TestRhythmPlugIn(){
		doParams();
	}
	public void process(PipelineNoteList pnl, PlayPlugArgument ppa){
		pnl.addNoteObject(0.0, true, true);
		pnl.addNoteObject(4.0, true, true);
		pnl.addNoteObject(8.0, true, true);
		pnl.addNoteObject(12.0, true, true);
		pnl.length = 16.0;
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
