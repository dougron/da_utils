package main.java.da_utils.algorithmic_models.pipeline.plugins.embellishments;
import java.util.ArrayList;
import java.util.Collections;

import com.cycling74.max.MaxObject;

import main.java.da_utils.ableton_live.ableton_live_clip.LiveMidiNote;
import main.java.da_utils.algorithmic_models.pipeline.pipeline_note_list.Embellishment;
import main.java.da_utils.algorithmic_models.pipeline.pipeline_note_list.PipelineNoteObject;
import main.java.da_utils.algorithmic_models.pipeline.plugins.pipeline_plugin.PlayPlugArgument;
import main.java.da_utils.combo_variables.IntAndString;
import main.java.da_utils.static_chord_scale_dictionary.CSD;
import main.java.da_utils.static_chord_scale_dictionary.ModeObject;


public class DiatonicEmbellishment implements Embellishment{

	public int offset;
	public static final String name = "DiatonicEmbellishment";
//	private ResourceObject ro;
	
	public DiatonicEmbellishment(int i){
		offset = i;
//		this.ro = ro;
	}
	public ArrayList<Integer> getNote(PipelineNoteObject pno, PlayPlugArgument ppa){
//		MaxObject.post("DiatonicEmbellishment getNote: ---- called with PipelineNoteObject:");
//		MaxObject.post(pno.toString());
		ArrayList<Integer> tempNoteList = new ArrayList<Integer>();
		for(LiveMidiNote lmn: pno.noteList){
			tempNoteList.add(getDiatonicEmbellishment(lmn.note, ppa, pno.position));
			
		}
		return tempNoteList;
	}
	public String name(){
		return name + ": " + offset;
	}
	
	@Override
	public String shortName() {
		return "d" + offset;
	}
	
	@Override
	public ED ed() {
		
		return new ED("d", offset);
	}
	
	@Override
	public String getType() {
		return "d";
	}
	
	@Override
	public int getOffset() {
		return offset;
	}
	
// privates ------------------------------------------------------------------
	
	private int getDiatonicEmbellishment(int noteToEmbellish, PlayPlugArgument ppa, double pos){
		IntAndString ka = getKeyArea(pos, ppa);
		int[] modNoteArr = getDiatonicNotesOfKey(ka);
		int diatonicCount = Math.abs(offset);
		int inc; 
		if (offset == 0){
			inc = 0;
		} else {
			inc = offset / Math.abs(offset);
		}
		
		int testNote = noteToEmbellish;
		while (diatonicCount > 0){
			testNote += inc;
			if (arrayContains(modNoteArr, testNote % 12)){
				diatonicCount--;
			}			
		}
		return testNote;
	}
	private boolean arrayContains(int[] arr, int testval){
		for (int i: arr){
			if (i == testval){
				return true;
			}
		}
		return false;
	}
	private int[] getDiatonicNotesOfKey(IntAndString ka){
//		System.out.println(ka.toString());
		ModeObject mo = CSD.IONIAN_MODE;
		if (ka.str == CSD.MINOR){
			mo = CSD.AOELIAN_MODE;
		}
		int[] scalePattern = CSD.getDiatonicModeDegrees(mo);
		ArrayList<Integer> list = new ArrayList<Integer>();
		for (int i: scalePattern){
			list.add((i + ka.i) % 12);
		}
		Collections.sort(list);
//		list.add(list.get(0) + 12);				// gives
		scalePattern = new int[list.size()];
		for (int i = 0; i < list.size(); i++){
			scalePattern[i] = list.get(i);
		}
		return scalePattern;
	}
	private IntAndString getKeyArea(double pos, PlayPlugArgument ppa){

		return ppa.cf.getPrevailingKey(pos);
	}
}
