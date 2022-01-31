package main.java.da_utils.algorithmic_models.pipeline.plugins.test_plugins;
import java.util.ArrayList;
/*
 * takes the pnoList of the PipelineNoteList and adds a new item either -0.25 or - 0.5 away, 
 * unless that position is already taken, and keeping the value within the length constraints (0.0 >= pos < pnl.length)
 * Embellished notes are flagged as isEmbellishable = false and new items are true.
 * 
 */

import main.java.da_utils.algorithmic_models.pipeline.pipeline_note_list.PipelineNoteList;
import main.java.da_utils.algorithmic_models.pipeline.pipeline_note_list.PipelineNoteObject;
import main.java.da_utils.algorithmic_models.pipeline.plugins.Pluggable;
import main.java.da_utils.algorithmic_models.pipeline.plugins.filter_sort.FilterObject;
import main.java.da_utils.algorithmic_models.pipeline.plugins.pipeline_plugin.PipelinePlugIn;
import main.java.da_utils.algorithmic_models.pipeline.plugins.pipeline_plugin.PlayPlugArgument;

public class TestAddEmbellishmentRhythmPlugIn extends Pluggable implements PipelinePlugIn{
	
	public double[] options; 

	
	private void doParams(){
		setName("TestAddEmbellishmentRhythmPlugIn");
		setSortIndex(10);
		setInputSort(0);
		setZone(1);
		setCanDouble(true);
		setActive(1);
	}
	
	
	public TestAddEmbellishmentRhythmPlugIn(){
		doParams();
		options = new double[]{-0.25, -0.5, -0.75, -1.0};
	}
	
	public void process(PipelineNoteList pnl, PlayPlugArgument ppa){
		
		ArrayList<PipelineNoteObject> pnoTempList = new ArrayList<PipelineNoteObject>();
		for (PipelineNoteObject pno: pnl.pnoList){
			if (pno.isEmbellishable){
				double position = pno.position + options[(int)(ppa.rnd.next() * options.length)];
				while (position < 0.0) position += pnl.length;
				position = position % pnl.length;
				if (!positionAlreadyTaken(position, pnl.pnoList, pnoTempList)){
					pnoTempList.add(new PipelineNoteObject(position, false, true, pno));
					pno.isEmbellishable = false;
				}
			}
		}
		pnl.add(pnoTempList);
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

// privates -------------------------------------------------------------------------------------
	
	private boolean positionAlreadyTaken(double pos, ArrayList<PipelineNoteObject> list1, ArrayList<PipelineNoteObject> list2){
		if (containsPosition(pos, list1) || containsPosition(pos, list2)){
			return true;
		} else {
			return false;
		}
	}
	private boolean containsPosition(double pos, ArrayList<PipelineNoteObject> list){
		for (PipelineNoteObject pno: list){
			if (pno.position == pos){
				return true;
			}
		}
		return false;
	}
}
