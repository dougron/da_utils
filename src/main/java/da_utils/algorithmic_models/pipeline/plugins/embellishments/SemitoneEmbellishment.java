package main.java.da_utils.algorithmic_models.pipeline.plugins.embellishments;
import java.util.ArrayList;

import main.java.da_utils.ableton_live.ableton_live_clip.LiveMidiNote;
import main.java.da_utils.algorithmic_models.pipeline.pipeline_note_list.Embellishment;
import main.java.da_utils.algorithmic_models.pipeline.pipeline_note_list.PipelineNoteObject;
import main.java.da_utils.algorithmic_models.pipeline.plugins.pipeline_plugin.PlayPlugArgument;


public class SemitoneEmbellishment implements Embellishment{
	
	public int offset;
	public static final String name = "SemitoneEmbellishment";
//	private ResourceObject ro;
	
	public SemitoneEmbellishment(int i){
		offset = i;
//		this.ro = ro;
	}

	public ArrayList<Integer> getNote(PipelineNoteObject pno, PlayPlugArgument ppa){
//		MaxObject.post("SemitoneEmbellishment getNote: ---- called with PipelineNoteObject:");
//		MaxObject.post(pno.toString());
		ArrayList<Integer> tempNoteList = new ArrayList<Integer>();
		for(LiveMidiNote lmn: pno.noteList){
			tempNoteList.add(lmn.note + offset);
			
		}
		return tempNoteList;
	}
	public String name(){
		return name + ": " + offset;
	}
	
	@Override
	public String shortName() {
		return "s" + offset;
	}
	
	@Override
	public ED ed() {
		
		return new ED("s", offset);
	}
	
	@Override
	public String getType() {
		return "s";
	}
	
	@Override
	public int getOffset() {
		return offset;
	}

}
