package main.java.da_utils.algorithmic_models.pipeline.plugins.drums;
import java.util.ArrayList;

import main.java.da_utils.algorithmic_models.pipeline.pipeline_note_list.PipelineNoteList;
import main.java.da_utils.algorithmic_models.pipeline.pipeline_note_list.PipelineNoteObject;
import main.java.da_utils.algorithmic_models.pipeline.plugins.Pluggable;
import main.java.da_utils.algorithmic_models.pipeline.plugins.filter_sort.FilterObject;
import main.java.da_utils.algorithmic_models.pipeline.plugins.pipeline_plugin.PipelinePlugIn;
import main.java.da_utils.algorithmic_models.pipeline.plugins.pipeline_plugin.PlayPlugArgument;
import main.java.da_utils.resource_objects.ChordForm;
import main.java.da_utils.resource_objects.TwoBarRhythmBuffer;



public class PlugInKikFromRhythmBuffer extends Pluggable implements PipelinePlugIn{
	

	
	private void doParams(){
		setName("Kik_RB");
		setSortIndex(0);
		setInputSort(0);
		setZone(0);
		setCanDouble(false);
		setActive(1);
	}
	
	public PlugInKikFromRhythmBuffer(){
		doParams();
//		this.ro = ro;
	}
	public PlugInKikFromRhythmBuffer(TwoBarRhythmBuffer rb, ChordForm form){
		
	}
	public void process(PipelineNoteList pnl, PlayPlugArgument ppa){
		if (active > 0){
//			MaxObject.post("PlugInBassFromRhythmBuffer processed as active");
			pnl.length = ppa.cf.length();
//			TwoBarRhythmBuffer rb = ro.rbgc.getRhythmBuffer();
			
			ArrayList<PipelineNoteObject> pnoTempList = addRhythmPositions(ppa.rb, pnl);
			addNotes(pnoTempList);
			doVelocity(pnoTempList, ppa);
			
			pnl.add(pnoTempList);
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
	private void doVelocity(ArrayList<PipelineNoteObject> pnoList, PlayPlugArgument ppa){		
		for (PipelineNoteObject pno: pnoList){
			pno.setFixedVelocity(DrumStaticVariables.kikDynamic[ppa.dynamic]);			
		}
	}
	private void addNotes(ArrayList<PipelineNoteObject> pnoList){
		for (PipelineNoteObject pno: pnoList){
			pno.addNote(DrumStaticVariables.kikNote);
		}
	}	
	private ArrayList<PipelineNoteObject> addRhythmPositions(TwoBarRhythmBuffer rb, PipelineNoteList pnl){
		ArrayList<PipelineNoteObject> pnoList = new ArrayList<PipelineNoteObject>();
		double inc = 0.0;
		boolean flag = true;
		while (flag){
			for (int i = 0; i < rb.buffy.length; i++){
				double pos = (double)i * 0.25;				// magic 0.25 = length of 16th note.....
				if (pos + inc >= pnl.length) {
					flag = false;
					break;
				}
				if (rb.buffy[i] == 1 && flag && kikPosNotTaken(pnl, pos + inc)){
					pnoList.add(new PipelineNoteObject(pos + inc, true, true, DrumStaticVariables.kikDescriptor));
				}
			}
			inc += 8.0;				// magic number = 2 bars, need to chenge when refactoring for other time signatures
		}
		return pnoList;
	}
	private boolean kikPosNotTaken(PipelineNoteList pnl, double pos){
		for (PipelineNoteObject pno: pnl.pnoList){
			if (pno.position == pos && pno.descriptor == DrumStaticVariables.kikDescriptor){
				return false;
			}
		}
		return true;
	}

}
