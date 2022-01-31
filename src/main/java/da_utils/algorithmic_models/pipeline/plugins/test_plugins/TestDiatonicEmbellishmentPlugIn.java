package main.java.da_utils.algorithmic_models.pipeline.plugins.test_plugins;

import main.java.da_utils.algorithmic_models.pipeline.pipeline_note_list.PipelineNoteList;
import main.java.da_utils.algorithmic_models.pipeline.plugins.Pluggable;
import main.java.da_utils.algorithmic_models.pipeline.plugins.embellishments.ED;
import main.java.da_utils.algorithmic_models.pipeline.plugins.embellishments.PlugInAddEmbellishmentRhythm;
import main.java.da_utils.algorithmic_models.pipeline.plugins.embellishments.PlugInAssignEmbellishment;
import main.java.da_utils.algorithmic_models.pipeline.plugins.filter_sort.FilterObject;
import main.java.da_utils.algorithmic_models.pipeline.plugins.pipeline_plugin.PipelinePlugIn;
import main.java.da_utils.algorithmic_models.pipeline.plugins.pipeline_plugin.PlayPlugArgument;

public class TestDiatonicEmbellishmentPlugIn extends Pluggable implements PipelinePlugIn{
	
	private PipelinePlugIn[] plugArr;
	
	
	private void doParams(){
		setName("TestDiatonicEmb");
		setSortIndex(0);
		setInputSort(0);
		setZone(1);
		setCanDouble(true);
		setActive(1);
	}
	
	public TestDiatonicEmbellishmentPlugIn(){
		doParams();
		plugArr = new PipelinePlugIn[]{
				new PlugInAddEmbellishmentRhythm(new double[]{-0.25, -0.5},
												new double[]{1.0, 3.0},
													1.0),
				new PlugInAssignEmbellishment(new ED[]{new ED("c", 1)},
						new double[]{1.0})			
		};
	}
	public void process(PipelineNoteList pnl, PlayPlugArgument ppa){
		postPNL(pnl);
		for (PipelinePlugIn ppi: plugArr){
			ppi.process(pnl, ppa);
			postPNL(pnl);
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
	
	private void postPNL(PipelineNoteList pnl){
//		MaxObject.post("PlugInBassAddEmbellishmentOne PipelineNoteList.toString -----------------");
		String[] splitPost = pnl.toString().split("\n");
		for (String str: splitPost){
//			MaxObject.post(str);
		}
		
	}
}
