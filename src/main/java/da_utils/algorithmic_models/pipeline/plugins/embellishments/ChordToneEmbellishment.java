package main.java.da_utils.algorithmic_models.pipeline.plugins.embellishments;
import java.util.ArrayList;

import main.java.da_utils.ableton_live.ableton_live_clip.LiveMidiNote;
import main.java.da_utils.algorithmic_models.pipeline.pipeline_note_list.Embellishment;
import main.java.da_utils.algorithmic_models.pipeline.pipeline_note_list.PipelineNoteObject;
import main.java.da_utils.algorithmic_models.pipeline.plugins.pipeline_plugin.PlayPlugArgument;
import main.java.da_utils.chord_progression_analyzer.ChordInKeyObject;
import main.java.da_utils.chord_scale_dictionary.chord_analysis.ChordAnalysisObject;


public class ChordToneEmbellishment implements Embellishment{

	public int offset;
	public static final String name = "ChordToneEmbellishment";
//	private ResourceObject ro;
	
	public ChordToneEmbellishment(int i){
		offset = i;
//		this.ro = ro;
	}
	public ArrayList<Integer> getNote(PipelineNoteObject pno, PlayPlugArgument ppa){
//		MaxObject.post("ChordToneEmbellishment getNote: ---- called with PipelineNoteObject:");
//		MaxObject.post(pno.toString());
		ArrayList<Integer> tempNoteList = new ArrayList<Integer>();
		for(LiveMidiNote lmn: pno.noteList){
			tempNoteList.add(getChordToneEmbellishment(lmn.note, ppa, pno.position));
			
		}
		return tempNoteList;
	}
	public String name(){
		return name + ": " + offset;
	}
	
	@Override
	public String shortName() {
		return "c" + offset;
	}
	
	@Override
	public ED ed() {
		
		return new ED("c", offset);
	}
	
	@Override
	public String getType() {
		return "c";
	}
	
	@Override
	public int getOffset() {
		return offset;
	}

	
	
// privates ------------------------------------------------------------------
	
	private int getChordToneEmbellishment(int noteToEmbellish, PlayPlugArgument ppa, double pos){
//		ResourceObject tempro = ro;
//		MaxObject.post("%%%%%%%%%%%%" + tempro.toString());
//		ChordProgrammer2 tempcp = tempro.cp;
//		ChordForm cf = ro.cp.getChordForm();
		ChordInKeyObject ciko = ppa.cf.getPrevailingCIKO(pos, ppa.cf.lc());
		int[] modNoteList = ciko.getModChordTones();
		int count = Math.abs(offset);
		int inc = offset / Math.abs(offset);
		int testNote = noteToEmbellish;
		while (count > 0){
			testNote += inc;
			if (intArrContains(modNoteList, testNote % 12)){
				count--;
			}			
		}
		return testNote;
	}

	private boolean intArrContains(int[] arr, int i) {
		for (int x: arr){
			if (x == i){
				return true;
			}
		}
		return false;
	}
	private ArrayList<Integer> getNotesOfChord(ChordAnalysisObject cao){
		ArrayList<Integer> tempList = new ArrayList<Integer>();
		for (Integer i: cao.modNoteList){
			tempList.add(i + cao.simpleRoot());
		}
		return tempList;
	}


}
