package main.java.da_utils.algorithmic_models.pipeline.plugins.drums;
import java.util.ArrayList;

import main.java.da_utils.algorithmic_models.pipeline.pipeline_note_list.PipelineNoteList;
import main.java.da_utils.algorithmic_models.pipeline.pipeline_note_list.PipelineNoteObject;
import main.java.da_utils.algorithmic_models.pipeline.plugins.Pluggable;
import main.java.da_utils.algorithmic_models.pipeline.plugins.pipeline_plugin.PipelinePlugIn;
import main.java.da_utils.algorithmic_models.pipeline.plugins.pipeline_plugin.PlayPlugArgument;


public class PlugInKikFunkOne extends Pluggable implements PipelinePlugIn{
	

	
	private void doParams(){
		setName("KikFunk");
		setSortIndex(0);
		setInputSort(0);
		setZone(0);
		setCanDouble(false);
		setActive(1);
	}
	
	public PlugInKikFunkOne(){
		doParams();
//		this.ro = ro;
	}

	public void process(PipelineNoteList pnl, PlayPlugArgument ppa){
		if (active > 0){
//			MaxObject.post("PlugInBassFromRhythmBuffer processed as active");
			pnl.length = ppa.cf.length();			
//			double formLength = p.getFormLength();	// seems redundant......	

			ArrayList<PipelineNoteObject> pnoTempList = addRhythmPositions(pnl);
			addNotes(pnoTempList);
			doVelocity(pnoTempList, ppa);
			
			pnl.add(pnoTempList);
		}
		
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
	
	private ArrayList<PipelineNoteObject> addRhythmPositions(PipelineNoteList pnl){
		double[] posList = new double[]{0.0, 1.75, 2.5, 4.5, 5.5, 6.5, 7.25};
		double repLength = 8.0;				// repeats every this number of beats
		ArrayList<PipelineNoteObject> pnoList = new ArrayList<PipelineNoteObject>();
		for (double pos = 0.0; pos < pnl.length; pos += repLength){
			for (double pos2: posList){
				double actualpos = pos + pos2;
				if (actualpos < pnl.length && kikPosNotTaken(pnl, actualpos)){
					pnoList.add(new PipelineNoteObject(actualpos, true, true, DrumStaticVariables.kikDescriptor));
				}
			}
						
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
