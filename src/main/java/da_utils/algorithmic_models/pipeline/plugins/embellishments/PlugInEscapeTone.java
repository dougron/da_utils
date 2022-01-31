package main.java.da_utils.algorithmic_models.pipeline.plugins.embellishments;

import main.java.da_utils.algorithmic_models.pipeline.pipeline_note_list.PipelineNoteList;
import main.java.da_utils.algorithmic_models.pipeline.plugins.Pluggable;
import main.java.da_utils.algorithmic_models.pipeline.plugins.filter_sort.FilterObject;
import main.java.da_utils.algorithmic_models.pipeline.plugins.pipeline_plugin.PipelinePlugIn;
import main.java.da_utils.algorithmic_models.pipeline.plugins.pipeline_plugin.PlayPlugArgument;


public class PlugInEscapeTone extends Pluggable implements PipelinePlugIn{
	
	private PipelinePlugIn[] plugArr; 
	
	private void doParams(){
		setName("Emb_Esc");
		setSortIndex(0);
		setInputSort(0);
		setZone(1);
		setCanDouble(true);
		setActive(1);
	}
	
	public PlugInEscapeTone(double[] rhythmOptions, 
			double[] weightingOptions, 
			double chance, 
			ED embForUpContour, 
			ED embForDownContour){
		doParams();
//		this.ro = ro;
		plugArr = new PipelinePlugIn[]{
				new PlugInAddEmbellishmentRhythm(rhythmOptions, weightingOptions, chance),
				new PlugInEscapeNote(embForUpContour, embForDownContour)			
		};
	}
	public void process(PipelineNoteList pnl, PlayPlugArgument ppa){
		for (PipelinePlugIn ppi: plugArr){
			ppi.process(pnl, ppa);
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

// privates -----------------------------------------------------------------------------
	

}
