package main.java.da_utils.algorithmic_models.pipeline.plugins.drums;
import java.util.ArrayList;

import main.java.da_utils.algorithmic_models.pipeline.pipeline_note_list.PipelineNoteList;
import main.java.da_utils.algorithmic_models.pipeline.pipeline_note_list.PipelineNoteObject;
import main.java.da_utils.algorithmic_models.pipeline.plugins.Pluggable;
import main.java.da_utils.algorithmic_models.pipeline.plugins.pipeline_plugin.PipelinePlugIn;
import main.java.da_utils.algorithmic_models.pipeline.plugins.pipeline_plugin.PlayPlugArgument;



public class PlugInKik58Euclidean extends Pluggable implements PipelinePlugIn{
	

	
	private void doParams(){
		setName("Kik_58Euc");
		setSortIndex(0);
		setInputSort(0);
		setZone(0);
		setCanDouble(true);
		setActive(1);
	}
	
	public PlugInKik58Euclidean(){
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
		double repLength = 8.0;			// uses Euclidean calculation which repeats every 8.0 beats
		double itemCount = 5.0;			// there will be 5 items in repLength
		double quant = 0.5;				// ...quantized to the nearest 1/8 note 
		double inc = repLength / itemCount;
		double offset = 3.0;
		ArrayList<PipelineNoteObject> pnoList = new ArrayList<PipelineNoteObject>();
		for (double pos = 0.0; pos < pnl.length; pos += repLength){
			for (double pos2 = 0.0; pos2 < repLength; pos2 += inc){
				double actualpos = ((((int)(pos2 / quant + quant) * quant) + offset) % repLength) + pos;
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
