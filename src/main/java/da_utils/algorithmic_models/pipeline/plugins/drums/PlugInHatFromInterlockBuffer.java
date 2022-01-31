package main.java.da_utils.algorithmic_models.pipeline.plugins.drums;
import java.util.ArrayList;

import main.java.da_utils.ableton_live.ableton_live_clip.LiveMidiNote;
import main.java.da_utils.algorithmic_models.pipeline.pipeline_note_list.Embellishment;
import main.java.da_utils.algorithmic_models.pipeline.pipeline_note_list.PipelineNoteList;
import main.java.da_utils.algorithmic_models.pipeline.pipeline_note_list.PipelineNoteObject;
import main.java.da_utils.algorithmic_models.pipeline.plugins.Pluggable;
import main.java.da_utils.algorithmic_models.pipeline.plugins.pipeline_plugin.PipelinePlugIn;
import main.java.da_utils.algorithmic_models.pipeline.plugins.pipeline_plugin.PlayPlugArgument;
import main.java.da_utils.resource_objects.AccentTemplate;
import main.java.da_utils.resource_objects.ChordForm;
import main.java.da_utils.resource_objects.TwoBarRhythmBuffer;


public class PlugInHatFromInterlockBuffer extends Pluggable implements PipelinePlugIn{
	
	
	private void doParams(){
		setName("Hat_IB");
		setSortIndex(10);
		setInputSort(0);
		setZone(0);
		setCanDouble(false);
		setActive(1);
	}
	
	public PlugInHatFromInterlockBuffer(){
		doParams();
//		this.ro = ro;
	}
//	public PlugInHatFromRhythmBuffer(TwoBarRhythmBuffer rb, String name){
	// not going to work. rhythm buffer changes.......
//		this.rb = rb;
//		this.name = name;
//	}
	public void process(PipelineNoteList pnl, PlayPlugArgument ppa){
		if (active > 0){

			pnl.length = ppa.cf.length();
			TwoBarRhythmBuffer rb = ppa.ib;
			
			ArrayList<PipelineNoteObject> pnoTempList = addRhythmPositions(rb, pnl, ppa);
			addNotes(pnoTempList);
			doVelocity(pnoTempList, ppa);
			
			pnl.add(pnoTempList);
		}
		
	}

	
// privates -----------------------------------------------------------------------------
	private void doVelocity(ArrayList<PipelineNoteObject> pnoList, PlayPlugArgument ppa){		
		for (PipelineNoteObject pno: pnoList){
			pno.setFixedVelocity(DrumStaticVariables.hatDynamic[ppa.dynamic]);			
		}
	}
	private void addNotes(ArrayList<PipelineNoteObject> pnoList){
		for (PipelineNoteObject pno: pnoList){
			pno.addNote(DrumStaticVariables.cHatNote);
		}
	}	
	private ArrayList<PipelineNoteObject> addRhythmPositions(TwoBarRhythmBuffer rb, PipelineNoteList pnl, PlayPlugArgument ppa){
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
				if (rb.buffy[i] == 1 && flag && hatPosNotTaken(pnl, pos + inc)){
					pnoList.add(new PipelineNoteObject(pos + inc, true, true, DrumStaticVariables.hatDescriptor));
				}
			}
			inc += 8.0;				// magic number = 2 bars, need to chenge when refactoring for other time signatures
		}
		return pnoList;
	}
	private boolean hatPosNotTaken(PipelineNoteList pnl, double pos){
		for (PipelineNoteObject pno: pnl.pnoList){
			if (pno.position == pos && pno.descriptor == DrumStaticVariables.hatDescriptor){
				return false;
			}
		}
		return true;
	}
}


