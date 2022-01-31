package main.java.da_utils.algorithmic_models.pipeline.plugins.drums;


import main.java.da_utils.algorithmic_models.pipeline.pipeline_note_list.PipelineNoteList;
import main.java.da_utils.algorithmic_models.pipeline.plugins.Pluggable;
import main.java.da_utils.algorithmic_models.pipeline.plugins.embellishments.ED;
import main.java.da_utils.algorithmic_models.pipeline.plugins.embellishments.PlugInAddEmbellishmentRhythm;
import main.java.da_utils.algorithmic_models.pipeline.plugins.embellishments.PlugInAssignEmbellishment;
import main.java.da_utils.algorithmic_models.pipeline.plugins.pipeline_plugin.PipelinePlugIn;
import main.java.da_utils.algorithmic_models.pipeline.plugins.pipeline_plugin.PlayPlugArgument;
import main.java.da_utils.algorithmic_models.pipeline.utilities.NoteRange;


public class PlugInDrumEmbellish extends Pluggable implements PipelinePlugIn{
	
	private PipelinePlugIn[] plugArr; 
	

	
	private void doParams(){
		setName("KikEmbOne");
		setSortIndex(0);
		setInputSort(0);
		setZone(1);
		setCanDouble(true);
		setActive(1);
	}
	
	public PlugInDrumEmbellish(int drumNote){
		doParams();
		plugArr = new PipelinePlugIn[]{
				new PlugInAddEmbellishmentRhythm(new double[]{-0.25, -0.5},
						new double[]{1.0, 3.0},
							1.0,
							new NoteRange(drumNote, drumNote)),
				new PlugInAssignEmbellishment(new ED[]{new ED("s", 0)},
						new double[]{1.0})			
		};
	}
	public void process(PipelineNoteList pnl, PlayPlugArgument ppa){
//		MaxObject.post("PlugInBassAddEmbellishmentOne.process() with PipelineNoteList of size: " + pnl.pnoList.size());
//		postPNL(pnl);
		for (PipelinePlugIn ppi: plugArr){
			ppi.process(pnl, ppa);
//			MaxObject.post(ppi.name() + " just been run in PlugInBassAddEmbellishmentOne. results follow.....");
//			postPNL(pnl);
//			MaxObject.post("-x-x-x-x-x-x-x-x-x-x-x-x-x-x-x-x-x-x-x-x-x-x-x-x-x-x-");
		}
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
