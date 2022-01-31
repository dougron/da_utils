package main.java.da_utils.algorithmic_models.pipeline.plugins.embellishments;

import java.util.ArrayList;

import acm.program.ConsoleProgram;
import main.java.da_utils.ableton_live.ableton_live_clip.LiveClip;
import main.java.da_utils.algorithmic_models.pipeline.pipeline_note_list.PipelineNoteObject;
import main.java.da_utils.algorithmic_models.pipeline.plugins.pipeline_plugin.PlayPlugArgument;
import main.java.da_utils.resource_objects.ChordForm;

public class EmbellishmentConsoleTest extends ConsoleProgram{

	
	public void run(){
		setSize(700, 700);
		ChordForm cf = new ChordForm(getCFClip());
		println(cf.toString() + "\n\n\n\n");
		PipelineNoteObject pno = new PipelineNoteObject(4.0, true, true);
		pno.addNote(52);
		pno.addNote(48);
		pno.addNote(57);
		
		println(pno.toString() + "\n\n\n\n");
		DiatonicEmbellishment de = new DiatonicEmbellishment(1);
		PlayPlugArgument ppa = new PlayPlugArgument();
		ppa.cf = cf;
		ArrayList<Integer> list = de.getNote(pno, ppa);
		for (Integer i: list){
			print(i + ", ");
		}
	}

	private LiveClip getCFClip() {
		LiveClip lc = new LiveClip(0, 0);
		lc.loopEnd = 16.0;
		lc.length = 16.0;
		
		lc.addNote(48, 0.0, 8.0, 100, 0);
		lc.addNote(52, 0.0, 8.0, 100, 0);
		lc.addNote(55, 0.0, 8.0, 100, 0);
		
		lc.addNote(47, 8.0, 8.0, 100, 0);
		lc.addNote(50, 8.0, 8.0, 100, 0);
		lc.addNote(55, 8.0, 8.0, 100, 0);
		return lc;
	}
}
