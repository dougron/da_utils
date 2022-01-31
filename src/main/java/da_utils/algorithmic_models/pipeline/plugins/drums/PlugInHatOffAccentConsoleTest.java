package main.java.da_utils.algorithmic_models.pipeline.plugins.drums;
import acm.program.ConsoleProgram;
import main.java.da_utils.algorithmic_models.pipeline.Pipeline;
import main.java.da_utils.algorithmic_models.pipeline.pipeline_note_list.PipelineNoteList;
import main.java.da_utils.algorithmic_models.pipeline.plugins.pipeline_plugin.PlayPlugArgument;
import LegacyStuff.ChordProgrammer2;




public class PlugInHatOffAccentConsoleTest extends ConsoleProgram{
	
	public void run(){
		setSize(700, 900);
		ChordProgrammer2 cp = makeProgression();
		Pipeline p = new Pipeline();
		PlayPlugArgument ppa = new PlayPlugArgument();

		ppa.cf = cp.form;
		p.addPlugInOption(new PlugInHatOn());
		p.addPlugInOption(new PlugInHatOff());
		p.addPlugInOption(new PlugInHatOffAccent());
		p.addPlugIn(0);
		p.addPlugIn(1);
		p.addPlugIn(2);
		PipelineNoteList pnl = p.makeNoteList();
		println(pnl);
	}
// privates -------------------------------------------------------------	
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
