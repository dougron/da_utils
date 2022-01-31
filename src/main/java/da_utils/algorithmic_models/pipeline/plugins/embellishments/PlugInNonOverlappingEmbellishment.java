package main.java.da_utils.algorithmic_models.pipeline.plugins.embellishments;
import java.util.ArrayList;
import com.cycling74.max.MaxObject;

import main.java.da_utils.algorithmic_models.pipeline.pipeline_note_list.PipelineNoteList;
import main.java.da_utils.algorithmic_models.pipeline.pipeline_note_list.PipelineNoteObject;
import main.java.da_utils.algorithmic_models.pipeline.plugins.Pluggable;
import main.java.da_utils.algorithmic_models.pipeline.plugins.pipeline_plugin.PipelinePlugIn;
import main.java.da_utils.algorithmic_models.pipeline.plugins.pipeline_plugin.PlayPlugArgument;

/*
 * pre-embellisher that will not overlap a previous PipelineNoteObject
 * also going to try make a viable system for favouring certain embellishments on
 * certain beats of the bar and for certain distances from the embellished note.
 * 
 * also may be the time to present a way of heirarchicalizing embellishments based on their 
 * relationship to the prevailing key, but maybe this is could be a) complicated, and
 * b) me outhinking myself and that this wont be of any use.
 * 
 * also going to try not put an embellishment on a bass note
 */

public class PlugInNonOverlappingEmbellishment extends Pluggable implements PipelinePlugIn{
	
	public double[] options; 
	public double[] weighting;
	public double weightingSum;
	public double chance;					// this is the chance of an embellishment being inserted

	
	private void doParams(){
		setName("Emb-over");
		setSortIndex(10);
		setInputSort(0);
		setZone(1);
		setCanDouble(true);
		setActive(1);
	}
	
	public PlugInNonOverlappingEmbellishment(){
		doParams();
	}
	
	public void process(PipelineNoteList pnl, PlayPlugArgument ppa){
		if (active > 0){
			// mmmmm..... looks like a work in progress...........
		}		
	}


	
// privates ----------------------------------------------------------------------------
	
	private double makeWeightingSum(){
		double sum = 0.0;
		for (double dd: weighting){
			sum += dd;
		}
		return sum;
	}
	private void sortOutWeighting(double[] options, double[] weighting){
		if (weighting.length != options.length){
			double[] newWeighting = new double[options.length];
			for (int i = 0; i < newWeighting.length; i++){
				if (i < weighting.length){
					newWeighting[i] = weighting[i];
				} else {
					newWeighting[i] = 0.0;
				}
			}
			this.weighting = newWeighting;
		} else {
			this.weighting = weighting;
		}
	}
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
	private int getWeightedIndex(PlayPlugArgument ppa){
		double r = ppa.rnd.next() * weightingSum;
		int index = -1;
		while (r > 0){
			index++;
			r -= weighting[index];								
		}
		return index;
	}
	private void postPNL(PipelineNoteList pnl){
		MaxObject.post("PlugInBassAddEmbellishmentOne PipelineNoteList.toString -----------------");
		String[] splitPost = pnl.toString().split("\n");
		for (String str: splitPost){
			MaxObject.post(str);
		}
		
	}

}
