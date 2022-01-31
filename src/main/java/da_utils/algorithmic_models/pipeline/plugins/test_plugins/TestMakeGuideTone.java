package main.java.da_utils.algorithmic_models.pipeline.plugins.test_plugins;
import main.java.da_utils.algorithmic_models.pipeline.pipeline_note_list.PipelineNoteList;
import main.java.da_utils.algorithmic_models.pipeline.plugins.Pluggable;
import main.java.da_utils.algorithmic_models.pipeline.plugins.pipeline_plugin.PipelinePlugIn;
import main.java.da_utils.algorithmic_models.pipeline.plugins.pipeline_plugin.PlayPlugArgument;

public class TestMakeGuideTone extends Pluggable implements PipelinePlugIn{
	
	public PipelinePlugIn[] plugArr = new PipelinePlugIn[]{
		new TestRhythmPlugIn(),
		new TestPrevailingChordPlugIn(),
		new TestContourValuePlugIn(),
		new TestGetNotePlugIn()
	};

	
	private void doParams(){
		setName("TestMakeGuideTone()");
		setSortIndex(0);
		setInputSort(0);
		setZone(1);
		setCanDouble(false);
		setActive(1);
	}
	
	
	public TestMakeGuideTone (){
		doParams();
	}
	public void process(PipelineNoteList pnl, PlayPlugArgument ppa){
		for (PipelinePlugIn ppi: plugArr){
			ppi.process(pnl, ppa);
		}
	}

}
