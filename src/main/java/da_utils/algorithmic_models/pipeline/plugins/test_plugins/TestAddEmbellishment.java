package main.java.da_utils.algorithmic_models.pipeline.plugins.test_plugins;
import main.java.da_utils.algorithmic_models.pipeline.pipeline_note_list.PipelineNoteList;
import main.java.da_utils.algorithmic_models.pipeline.plugins.Pluggable;
import main.java.da_utils.algorithmic_models.pipeline.plugins.pipeline_plugin.PipelinePlugIn;
import main.java.da_utils.algorithmic_models.pipeline.plugins.pipeline_plugin.PlayPlugArgument;

/*
 * this is a wrapper for other plugins
 */
public class TestAddEmbellishment extends Pluggable implements PipelinePlugIn{
	
	public PipelinePlugIn[] plugArr = new PipelinePlugIn[]{
			new TestAddEmbellishmentRhythmPlugIn(),
			new TestAssignEmbellishmentPlugIn()
		};

	
		private void doParams(){
			setName("TestAddEmbellishment");
			setSortIndex(10);
			setInputSort(0);
			setZone(1);
			setCanDouble(true);
			setActive(1);
		}
		
	public TestAddEmbellishment(){
		doParams();
	}
	public void process(PipelineNoteList pnl, PlayPlugArgument ppa){
		for (PipelinePlugIn ppi: plugArr){
			ppi.process(pnl, ppa);
		}
	}


}
