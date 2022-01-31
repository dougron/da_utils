package main.java.da_utils.algorithmic_models.pipeline.plugins.test_plugins;
import java.util.ArrayList;

import main.java.da_utils.algorithmic_models.pipeline.pipeline_note_list.PipelineNoteList;
import main.java.da_utils.algorithmic_models.pipeline.pipeline_note_list.PipelineNoteObject;
import main.java.da_utils.algorithmic_models.pipeline.plugins.Pluggable;
import main.java.da_utils.algorithmic_models.pipeline.plugins.filter_sort.FilterObject;
import main.java.da_utils.algorithmic_models.pipeline.plugins.pipeline_plugin.PipelinePlugIn;
import main.java.da_utils.algorithmic_models.pipeline.plugins.pipeline_plugin.PlayPlugArgument;

/*
 * using the contour value for each PipelineNoteObject and the centreOfGravity value, a single 
 * item is added to the pno.noteList which is the closest chord tone for the prevailing 
 * ChordAnalysisObject.
 * 
 */


public class TestGetNotePlugIn extends Pluggable implements PipelinePlugIn{
	
	private int centreOfGravity = 48;

	
	private void doParams(){
		setName("TestGetNotePlugIn()");
		setSortIndex(30);
		setInputSort(0);
		setZone(1);
		setCanDouble(false);
		setActive(1);
	}
	
	public TestGetNotePlugIn(){
		doParams();
	}
	public void process(PipelineNoteList pnl, PlayPlugArgument ppa){
		for (PipelineNoteObject pno: pnl.pnoList){
			ArrayList<Integer> noteList = pno.cao.modNoteList;
			int root = pno.cao.simpleRoot();
			int notePrecursor = pno.contourValue + centreOfGravity;
			int closestNote = getClosestNote(noteList, notePrecursor, root);
			pno.addNote(closestNote);
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
	
// privates ^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^
	
	private int getClosestNote(ArrayList<Integer> optList, int preNote, int root){
		int distance = 1000;
		int note = -1;
		for (int i = 0; i < 12; i++){
			for (Integer chordTone: optList){
				int testNote = chordTone + (i * 12) + root;
				int tempDist = Math.abs(testNote - preNote);
				if (tempDist < distance){
					note = testNote;
					distance = tempDist;
				}
			}
		}
		return note;
	}

}
