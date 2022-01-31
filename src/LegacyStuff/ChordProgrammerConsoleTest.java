package LegacyStuff;
import com.cycling74.max.Atom;

//import ChordScaleDictionary.ChordScaleDictionary;
import acm.program.ConsoleProgram;
import main.java.da_utils.ableton_live.max_atom_utils.DougzAtomUtilities;


public class ChordProgrammerConsoleTest extends ConsoleProgram implements ChordProgrammer2Parent{
	
	
	public void run(){
		setSize(700, 900);
//		ChordScaleDictionary csd = new ChordScaleDictionary();
		ChordProgrammer2 cp = new ChordProgrammer2(this);
		cp.setFormLength(8);
		makeChordProgression(cp);
		println(cp.toString());
		println(cp.formLength);
		println(cp.form.length());
	}
	public void sendChordProgrammerMessage(Atom[] atArr){
		println("ChordProgrammer message to GUI:");
		println(DougzAtomUtilities.atomArrToString(atArr));
	}
	public void consolePrint(String str){
//		println("ChordProgrammer message to console:");
		println(str);
	}
// privates ---------------------------------------------------------------------
	private void makeChordProgression(ChordProgrammer2 cp){
		cp.noteIn(100, 100, 1);
		cp.noteIn(104, 100, 1);
		cp.noteIn(107, 100, 1);
		cp.chordButtonState(0, 0, 1);
		cp.chordButtonState(0, 0, 0);
	}

}
