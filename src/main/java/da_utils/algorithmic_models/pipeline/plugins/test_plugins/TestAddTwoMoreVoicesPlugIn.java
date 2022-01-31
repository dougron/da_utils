package main.java.da_utils.algorithmic_models.pipeline.plugins.test_plugins;
import java.util.ArrayList;
/*
 * adds two chord tones below the first note in the pno.noteList
 */

import main.java.da_utils.ableton_live.ableton_live_clip.LiveMidiNote;
import main.java.da_utils.algorithmic_models.pipeline.pipeline_note_list.PipelineNoteList;
import main.java.da_utils.algorithmic_models.pipeline.pipeline_note_list.PipelineNoteObject;
import main.java.da_utils.algorithmic_models.pipeline.plugins.Pluggable;
import main.java.da_utils.algorithmic_models.pipeline.plugins.pipeline_plugin.PipelinePlugIn;
import main.java.da_utils.algorithmic_models.pipeline.plugins.pipeline_plugin.PlayPlugArgument;
import main.java.da_utils.chord_scale_dictionary.chord_analysis.ChordAnalysisObject;

public class TestAddTwoMoreVoicesPlugIn extends Pluggable implements PipelinePlugIn{
	

	
	private void doParams(){
		setName("TestAddTwoMoreVoicesPlugIn");
		setSortIndex(10);
		setInputSort(0);
		setZone(1);
		setCanDouble(true);
		setActive(1);
	}
	
	public TestAddTwoMoreVoicesPlugIn(){
		doParams();
	}
	public void process(PipelineNoteList pnl, PlayPlugArgument ppa){
		for (PipelineNoteObject pno: pnl.pnoList){
			int noteCount = 2;
			LiveMidiNote note = pno.noteList.get(0);
			int testNote = note.note - 1;
			while (noteCount > 0){
				if (noteInChord(testNote, pno.cao)){
					pno.addNote(testNote);
					noteCount--;
				}
				testNote--;
			}
			
		}
	}

	
// privates ----------------------------------------------------------------------------
	private boolean noteInChord(int testNote, ChordAnalysisObject cao){
		int root = cao.simpleRoot();
		for (Integer i: cao.modNoteList){
			if (testNote % 12 == (i + root) % 12){
				return true;
			}
		}
		return false;
	}

}
