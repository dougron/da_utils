package main.java.da_utils.algorithmic_models.pipeline.plugins.test_plugins;
import main.java.da_utils.algorithmic_models.pipeline.pipeline_note_list.Embellishment;
import main.java.da_utils.algorithmic_models.pipeline.pipeline_note_list.PipelineNoteList;
import main.java.da_utils.algorithmic_models.pipeline.pipeline_note_list.PipelineNoteObject;
import main.java.da_utils.algorithmic_models.pipeline.plugins.Pluggable;
import main.java.da_utils.algorithmic_models.pipeline.plugins.embellishments.SemitoneEmbellishment;
import main.java.da_utils.algorithmic_models.pipeline.plugins.pipeline_plugin.PipelinePlugIn;
import main.java.da_utils.algorithmic_models.pipeline.plugins.pipeline_plugin.PlayPlugArgument;

public class TestAssignEmbellishmentPlugIn extends Pluggable implements PipelinePlugIn{
	

	public Embellishment[] embOptionArr = new Embellishment[]{
			new SemitoneEmbellishment(-1),
			new SemitoneEmbellishment(1)
	};

	
	private void doParams(){
		setName("TestAssignEmbellishmentPlugIn");
		setSortIndex(20);
		setInputSort(0);
		setZone(1);
		setCanDouble(true);
		setActive(1);
	}
	
	public TestAssignEmbellishmentPlugIn(){
		doParams();
	}
	
	public void process(PipelineNoteList pnl, PlayPlugArgument ppa){
		for (PipelineNoteObject pno: pnl.pnoList){
			if (pno.isEmbellishable){
				pno.embellishmentType = embOptionArr[(int)(ppa.rnd.next() * embOptionArr.length)];
				pno.addNote(pno.embellishmentType.getNote(pno.pnoEmbellishing, ppa));
			}
		}
	}
	

}
