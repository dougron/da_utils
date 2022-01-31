package main.java.da_utils.algorithmic_models.pipeline.plugins.pipeline_plugin;

import main.java.da_utils.algorithmic_models.pipeline.Pipeline;
import main.java.da_utils.algorithmic_models.pipeline.pipeline_note_list.PipelineNoteList;

public interface PipelinePlugInOLD {

	public void process(PipelineNoteList pnl, Pipeline p);
	public String name();
	public int sortIndex();
	public int inputSort();				// sort index based on when the plug was loaded. only really taken into account when canDouble = true
	public int zone();					// 0 - header, 1 - body, 2 - tail
	public boolean canDouble();			// no point in putting two guidetone generators in, for instance
	public void setInputSort(int i);
	public int active();			// yes - plug operates, no - plug don't
	public void setActive(int i);
}
