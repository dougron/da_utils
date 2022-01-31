package main.java.da_utils.algorithmic_models.pipeline.plugins.embellishments;
import acm.program.ConsoleProgram;
import main.java.da_utils.algorithmic_models.pipeline.Pipeline;
import main.java.da_utils.algorithmic_models.pipeline.pipeline_note_list.PipelineNoteList;
import main.java.da_utils.algorithmic_models.pipeline.plugins.melody.PlugInMelodyContourGuideToneEveryBar;
import main.java.da_utils.algorithmic_models.pipeline.plugins.pipeline_plugin.PlayPlugArgument;
import main.java.da_utils.chord_scale_dictionary.ChordScaleDictionary;
import main.java.da_utils.resource_objects.ContourData;
import main.java.da_utils.resource_objects.TwoBarRhythmBuffer;
import LegacyStuff.ChordProgrammer2;



public class PlugInEscapeNoteConsoleTest extends ConsoleProgram{

	public void run(){
		setSize(900, 700);
		ChordScaleDictionary csd = new ChordScaleDictionary();
		Pipeline p = new Pipeline();
		PlayPlugArgument ppa = new PlayPlugArgument();
		TwoBarRhythmBuffer rb = new TwoBarRhythmBuffer();
		ChordProgrammer2 cp = makeProgression();

		ppa.cf = cp.form;
		ContourData cd = new ContourData();
		cd.newData(0.5, 1, 0, 1);
	
		p.addPlugInOption(new PlugInMelodyContourGuideToneEveryBar());
		p.addPlugInOption(new PlugInEscapeTone(new double[]{-0.25, -0.5, -0.75, -1.0}, new double[]{1.0, 1.0, 1.0, 1.0}, 1.0, new ED("d", 1), new ED("s", -1)));
		p.addPlugIn(0);
		p.addPlugIn(1);
		println(p.makeNoteList());
//		PipelineNoteList pnl = makePNL();
//		println(pnl.toString());
//		PlugInEscapeTone pesc = new PlugInEscapeTone(new double[]{-0.25, -0.5, -0.75, -1.0}, new double[]{1.0, 1.0, 1.0, 1.0}, 1.0, new ED("s", 1), new ED("s", -1));
		
	}
	
	private PipelineNoteList makePNL(){
		PipelineNoteList pnl = new PipelineNoteList(0);
		pnl.addNoteObject(0.0, true, true);
		pnl.addNoteObject(4.0, true, true);
		int[] notes = new int[]{45, 50};
		for (int i = 0; i < pnl.pnoList.size(); i++){
			pnl.pnoList.get(i).addNote(notes[i]);
			pnl.pnoList.get(i).setFixedVelocity(100);
		}		
		return pnl;
	}
	private ChordProgrammer2 makeProgression(){
		ChordProgrammer2 cp = new ChordProgrammer2();
		cp.formLength = 4;
		cp.noteIn(50, 100, 1); // Dm, i think
		cp.noteIn(53, 100, 1);
		cp.noteIn(57, 100, 1);
		cp.chordButtonState(0, 0, 1);
		cp.chordButtonState(0, 0, 0);
		cp.noteIn(50, 0, 1); // Dm, i think
		cp.noteIn(53, 0, 1);
		cp.noteIn(57, 0, 1);
		
		cp.noteIn(52, 100, 1); // Em, i think
		cp.noteIn(55, 100, 1);
		cp.noteIn(59, 100, 1);
		cp.chordButtonState(2, 0, 1);
		cp.chordButtonState(2, 0, 0);
		cp.noteIn(52, 0, 1); // Dm, i think
		cp.noteIn(55, 0, 1);
		cp.noteIn(59, 0, 1);
		
		return cp;
		
	}
}
