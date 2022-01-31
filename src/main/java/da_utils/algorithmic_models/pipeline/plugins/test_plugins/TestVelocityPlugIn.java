package main.java.da_utils.algorithmic_models.pipeline.plugins.test_plugins;
import main.java.da_utils.algorithmic_models.pipeline.pipeline_note_list.PipelineNoteList;
import main.java.da_utils.algorithmic_models.pipeline.pipeline_note_list.PipelineNoteObject;
import main.java.da_utils.algorithmic_models.pipeline.plugins.Pluggable;
import main.java.da_utils.algorithmic_models.pipeline.plugins.pipeline_plugin.PipelinePlugIn;
import main.java.da_utils.algorithmic_models.pipeline.plugins.pipeline_plugin.PlayPlugArgument;

/*
 * assigns a velocity of 100 to all guide tones and 80 to any others
 */
public class TestVelocityPlugIn extends Pluggable implements PipelinePlugIn{
	

	private int guide_tone_velocity = 100;
	private int embellishment_tone_velocity = 80;

	
	private void doParams(){
		setName("TestVelocityPlugIn()");
		setSortIndex(10);
		setInputSort(0);
		setZone(1);
		setCanDouble(false);
		setActive(1);
	}
	
	public TestVelocityPlugIn(){
		doParams();
	}
	public void process(PipelineNoteList pnl, PlayPlugArgument ppa){
		for (PipelineNoteObject pno: pnl.pnoList){
			if (pno.isGuideTone){
				pno.setFixedVelocity(guide_tone_velocity);
			} else {
				pno.setFixedVelocity(embellishment_tone_velocity);
			}
		}
	}
	


}
