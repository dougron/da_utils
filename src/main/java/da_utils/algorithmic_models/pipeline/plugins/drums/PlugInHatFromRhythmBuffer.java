package main.java.da_utils.algorithmic_models.pipeline.plugins.drums;
import java.util.ArrayList;

import main.java.da_utils.ableton_live.ableton_live_clip.LiveMidiNote;
import main.java.da_utils.algorithmic_models.pipeline.pipeline_note_list.PipelineNoteList;
import main.java.da_utils.algorithmic_models.pipeline.pipeline_note_list.PipelineNoteObject;
import main.java.da_utils.algorithmic_models.pipeline.plugins.Pluggable;
import main.java.da_utils.algorithmic_models.pipeline.plugins.pipeline_plugin.PipelinePlugIn;
import main.java.da_utils.algorithmic_models.pipeline.plugins.pipeline_plugin.PlayPlugArgument;
import main.java.da_utils.resource_objects.TwoBarRhythmBuffer;


public class PlugInHatFromRhythmBuffer extends Pluggable implements PipelinePlugIn{
	

//	private double default_rhythmBuffer_loopLength = 32.0;
//	private static final int[] loBassRegister = new int[]{36, 55};
//	private static final int default_velocity = 100;
//	private static final double default_noteLength = 0.25;
//	private static final int[] dynamicVelocity = new int[]{40, 65, 90, 127}; 
//	private TwoBarRhythmBuffer rb;
//	private ChordForm form;
//	private ResourceObject ro;
	
	private void doParams(){
		setName("Hat_RB");
		setSortIndex(0);
		setInputSort(0);
		setZone(0);
		setCanDouble(false);
		setActive(1);
	}
	
	public PlugInHatFromRhythmBuffer(){
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
//			MaxObject.post("PlugInBassFromRhythmBuffer processed as active");
			pnl.length = ppa.cf.length();
			TwoBarRhythmBuffer rb = ppa.rb;
			
			ArrayList<PipelineNoteObject> pnoTempList = addRhythmPositions(pnl, ppa);
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
	private ArrayList<PipelineNoteObject> addRhythmPositions(PipelineNoteList pnl, PlayPlugArgument ppa){
		if (ppa.at == null){
			return addRhythmPositionsRB(pnl, ppa);
		} else {
			return addRhythmPositionsAT(pnl, ppa);
		}
	}
	private ArrayList<PipelineNoteObject> addRhythmPositionsAT(PipelineNoteList pnl, PlayPlugArgument ppa){
		ArrayList<PipelineNoteObject> pnoList = new ArrayList<PipelineNoteObject>();
		double inc = 0.0;
		boolean flag = true;
		while (flag){
			for (LiveMidiNote lmn: ppa.at.clip.noteList){
				double pos = lmn.position + inc;
				if (pos >= ppa.cf.length()){
					flag = false;
				} else {
					if (hatPosNotTaken(pnl, pos + inc)){
						pnoList.add(new PipelineNoteObject(pos, true, true, DrumStaticVariables.hatDescriptor));
					}

				}
			}
			inc += ppa.at.length;
		}
		return pnoList;
	}
	private ArrayList<PipelineNoteObject> addRhythmPositionsRB(PipelineNoteList pnl, PlayPlugArgument ppa){
		ArrayList<PipelineNoteObject> pnoList = new ArrayList<PipelineNoteObject>();
		double inc = 0.0;
		boolean flag = true;
		while (flag){
			for (int i = 0; i < ppa.rb.buffy.length; i++){
				double pos = (double)i * 0.25;				// magic 0.25 = length of 16th note.....
				if (pos + inc >= pnl.length) {
					flag = false;
					break;
				}
				if (ppa.rb.buffy[i] == 1 && flag && hatPosNotTaken(pnl, pos + inc)){
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

