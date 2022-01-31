package main.java.da_utils.algorithmic_models.pipeline.plugins.test_plugins;
import java.util.ArrayList;

import DataObjects.note_buffer.NoteOnBuffer;
import main.java.da_utils.algorithmic_models.pipeline.pipeline_note_list.PipelineNoteList;
import main.java.da_utils.algorithmic_models.pipeline.pipeline_note_list.PipelineNoteObject;
import main.java.da_utils.algorithmic_models.pipeline.plugins.Pluggable;
import main.java.da_utils.algorithmic_models.pipeline.plugins.pipeline_plugin.PipelinePlugIn;
import main.java.da_utils.algorithmic_models.pipeline.plugins.pipeline_plugin.PlayPlugArgument;
import main.java.da_utils.chord_scale_dictionary.chord_analysis.ChordAnalysisObject;


/*
 * this plug iterates through the pnoList and assigns a ChordAnalysisObject from a 
 * preset list of ChordAnalysisObjects, which gets cycled through if the length of the pnoList
 * exceeds the length of the preset list.
 */

public class TestPrevailingChordPlugIn extends Pluggable implements PipelinePlugIn{
	

	
	private void doParams(){
		setName("TestPrevailingChordPlugIn()");
		setSortIndex(10);
		setInputSort(0);
		setZone(1);
		setCanDouble(false);
		setActive(1);
	}
	
	
	public TestPrevailingChordPlugIn(){
		doParams();
	}
	public void process(PipelineNoteList pnl, PlayPlugArgument ppa){
		ArrayList<ChordAnalysisObject> caoList = makeChordOptions();
		int index = 0;
		for (PipelineNoteObject pno: pnl.pnoList){
			pno.cao = caoList.get(index);
			index++;
			if (index == caoList.size()) index = 0;
		}
	}

	
// privates --------------------------------------------------------------------
	private ArrayList<ChordAnalysisObject> makeChordOptions(){
		ArrayList<ChordAnalysisObject> caoList = new ArrayList<ChordAnalysisObject>();
		int[][] chordArr = new int[][]{
				{34, 38, 41},
				{37, 41, 44}
		};
		NoteOnBuffer nob = new NoteOnBuffer();
		for (int[] arr: chordArr){
			for (int i: arr){
				nob.noteIn(i, 100, 1);		// 100 = arbitrary non zero velocity, 1 = arbitrary channel
			}
			caoList.add(new ChordAnalysisObject(nob.noteBuffer));
			nob.clear();
		}
		return caoList;
	}


}
