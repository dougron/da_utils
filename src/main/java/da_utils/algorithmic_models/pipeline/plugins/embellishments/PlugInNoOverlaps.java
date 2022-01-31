package main.java.da_utils.algorithmic_models.pipeline.plugins.embellishments;


import java.util.Collections;

import main.java.da_utils.algorithmic_models.pipeline.pipeline_note_list.PipelineNoteList;
import main.java.da_utils.algorithmic_models.pipeline.pipeline_note_list.PipelineNoteObject;
import main.java.da_utils.algorithmic_models.pipeline.plugins.Pluggable;
import main.java.da_utils.algorithmic_models.pipeline.plugins.filter_sort.FilterObject;
import main.java.da_utils.algorithmic_models.pipeline.plugins.pipeline_plugin.PipelinePlugIn;
import main.java.da_utils.algorithmic_models.pipeline.plugins.pipeline_plugin.PlayPlugArgument;

public class PlugInNoOverlaps extends Pluggable implements PipelinePlugIn{
	
	private String name = "NoOvers";			// abrreviated name for GUI space issue
	private int sortIndex = 0;
	private int inputSort = 0;
	private int zone = 3;			// must come after everything
	private boolean canDouble = false;
	private int active = 1;

	
	public PlugInNoOverlaps(){

	}
	public void process(PipelineNoteList pnl, PlayPlugArgument ppa){
		if (active == 1) {
			// oldProcess(pnl, ppa);
			Collections.sort(pnl.pnoList, PipelineNoteObject.positionComparator);
			double nextPnoPos;
			for (int i = 0; i < pnl.pnoList.size(); i++){
				int nextIndex = i + 1;
				if (nextIndex >= pnl.pnoList.size()){
					nextIndex = 0;
				}
				PipelineNoteObject pno = pnl.pnoList.get(i);
				PipelineNoteObject nextPno = pnl.pnoList.get(nextIndex);
				nextPnoPos = nextPno.position;
				while (nextPnoPos < pno.position) nextPnoPos += pnl.length;
				pno.length = nextPnoPos - pno.position;
				pno.setLengthsToLegatoModel();
			}
		}
	}
	private void oldProcess(PipelineNoteList pnl, PlayPlugArgument ppa){
		Collections.sort(pnl.pnoList, PipelineNoteObject.positionComparator);
		double endpos;
		double overlap;
		boolean useModulo = false;
		double nextPnoPos;
		for (int i = 0; i < pnl.pnoList.size(); i++){
			int nextIndex = i + 1;
			if (nextIndex >= pnl.pnoList.size()){
				nextIndex = 0;
//				useModulo = true;
			}
			PipelineNoteObject pno = pnl.pnoList.get(i);
			PipelineNoteObject nextPno = pnl.pnoList.get(nextIndex);
			nextPnoPos = nextPno.position;
			while (nextPnoPos < pno.position) nextPnoPos += pnl.length;
			if (useModulo){
				endpos = (pno.position + pno.length) % pnl.length;
			} else {
				endpos = (pno.position + pno.length);// % pnl.length;
			}
			//endpos = (pno.position + pno.length);// % pnl.length;
			overlap = endpos - nextPnoPos;
			if (overlap > 0){
				pno.length -= overlap;	
				pno.setLengthsToLegatoModel();
			}
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
	
}

