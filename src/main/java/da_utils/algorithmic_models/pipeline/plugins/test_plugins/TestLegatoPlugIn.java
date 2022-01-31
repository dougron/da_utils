package main.java.da_utils.algorithmic_models.pipeline.plugins.test_plugins;
import main.java.da_utils.algorithmic_models.pipeline.pipeline_note_list.PipelineNoteList;
import main.java.da_utils.algorithmic_models.pipeline.pipeline_note_list.PipelineNoteObject;
import main.java.da_utils.algorithmic_models.pipeline.plugins.Pluggable;
import main.java.da_utils.algorithmic_models.pipeline.plugins.pipeline_plugin.PipelinePlugIn;
import main.java.da_utils.algorithmic_models.pipeline.plugins.pipeline_plugin.PlayPlugArgument;

/*
 * applies legato. legatoModel represents a set of percentages that are cycled through for each 
 * PipelineNoteObject. 0.0 means default length
 */
public class TestLegatoPlugIn extends Pluggable implements PipelinePlugIn{
	

	public double[] legatoModel;

	
	private void doParams(){
		setName("TestLegatoPlugIn");
		setSortIndex(10);
		setInputSort(0);
		setZone(1);
		setCanDouble(false);
		setActive(1);
	}
	
	public TestLegatoPlugIn(double[] d){
		doParams();
		legatoModel = d;
	}
	
	
	public void process(PipelineNoteList pnl, PlayPlugArgument ppa){
		int modelIndex = 0;
		for (int i = 0; i < pnl.pnoList.size() - 1; i++){
			PipelineNoteObject thisNote = pnl.pnoList.get(i);
			PipelineNoteObject nextNote = pnl.pnoList.get(i + 1);
			double legato = legatoModel[modelIndex];
			if (legato > 0.0){							// each note is default_length by default.
				thisNote.setFixedLength(legato, (nextNote.position - thisNote.position) * legato);
			}
			modelIndex++;
			if (modelIndex >= legatoModel.length) modelIndex = 0;
		}
	}


}
