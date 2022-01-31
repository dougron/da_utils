package main.java.da_utils.algorithmic_models.pipeline.pipeline_note_list;
import java.util.ArrayList;

import main.java.da_utils.algorithmic_models.pipeline.plugins.embellishments.ED;
import main.java.da_utils.algorithmic_models.pipeline.plugins.pipeline_plugin.PlayPlugArgument;


public interface Embellishment {

	public ArrayList<Integer> getNote(PipelineNoteObject pno, PlayPlugArgument ppa);
	public String name();
	public String shortName();
	public ED ed();
	public String getType();
	public int getOffset();
}
