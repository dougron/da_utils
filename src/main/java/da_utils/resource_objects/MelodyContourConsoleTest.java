package main.java.da_utils.resource_objects;
import com.cycling74.max.Atom;

import LegacyStuff.ChordProgrammer2;
import LegacyStuff.ChordProgrammer2Parent;
import acm.program.ConsoleProgram;
import main.java.da_utils.algorithmic_models.pipeline.plugins.pipeline_plugin.PlayPlugArgument;


public class MelodyContourConsoleTest extends ConsoleProgram implements ChordProgrammer2Parent{
	
	
	public void run(){
		setSize(900, 700);
		ChordProgrammer2 cp = makeProgression();
		ContourData cd = new ContourData();
		MelodyContour mc = new MelodyContour();
		PlayPlugArgument ppa = new PlayPlugArgument();
		ppa.cd = cd;
		ppa.cf = cp.form;
		cd.newData(0.4, 0.0, 0, 1);
		println(cp.form.length());
//		println(mc.toString());
		for (double d = 0.0; d < 30.0; d++){
			println(d + ": " + mc.getContourValue(d, ppa));
		}
	}
	
// ChordProgrammer2Parent methods ----------------------------------------
	
	public void sendChordProgrammerMessage(Atom[] atArr){}
	public void consolePrint(String str){}
	
// privates -------------------------------------------------------------	
	private ChordProgrammer2 makeProgression(){
		ChordProgrammer2 cp = new ChordProgrammer2(this);
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
