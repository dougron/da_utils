package ConsoleTests;

import java.math.RoundingMode;
import java.text.DecimalFormat;

import acm.program.ConsoleProgram;
import main.java.da_utils.ableton_live.ableton_live_clip.LiveClip;
import main.java.da_utils.ableton_live.ableton_live_clip.LiveMidiNote;
import main.java.da_utils.resource_objects.ChordForm;

public class CHordFormConsoleTest extends ConsoleProgram{

	public void run(){
		setSize(700, 900);
//		actualTest();
//		decimalFormatTest();
//		newChordFormTest();
//		noChordsChordFormTest();
		timeSignatureChangeTest();
	}
	private void timeSignatureChangeTest() {
		LiveClip lc = makeLiveClip();
		println(lc.toString());
		ChordForm cf = new ChordForm(lc);
//		println("\n\nChordForm:-----------------\n" + cf.toString());
		println("\n----" + cf.slashChordsToString());
		LiveClip newlc = lc.cloneIntoNewTimeSignature(new int[]{7, 16});
		println(newlc.toString());
		ChordForm newcf = new ChordForm(newlc);
		println("\n\nnew ChordForm:-----------------\n" + newcf.toString());
		println("\n----" + newcf.slashChordsToString());
		//ChordForm newcf = cf.ne
		newlc = newlc.cloneIntoNewTimeSignature(new int[]{5, 4});
		println(newlc.toString());
		ChordForm newnewcf = new ChordForm(newlc);
		println("\n\nnew ChordForm:-----------------\n" + newnewcf.toString());
		println("\n----" + newnewcf.slashChordsToString());
	}
	private void noChordsChordFormTest() {
		LiveClip lc = new LiveClip(0, 0);
		lc.loopEnd = 8.0;
		ChordForm cf = new ChordForm(lc);
		println("ChordForm:-----------------\n" + cf.toString());
		println("\n----" + cf.slashChordsToString());
		println("\n----" + cf.minusChordsToString());
		for (double pos = 0.0; pos < 10; pos += 0.3){
			println(pos + "-" + cf.getPrevailingChordSymbol(pos));
		}
		
	}
	private void decimalFormatTest() {
		DecimalFormat df = new DecimalFormat("#.#");  // actually not useful as ouput is a string
		df.setRoundingMode(RoundingMode.CEILING);
		for (double d = 0.0; d < 2.0; d += 0.14){
			println(d + ": " + (double)(Math.round(d * 2)) / 2);
			//println(d + ": " + df.format(d));
		}
	}
	private void actualTest(){
		LiveClip lc = makeLiveClip();
		println(lc.toString());
		ChordForm cf = new ChordForm(lc);
		println("\n----" + cf.slashChordsToString());
		println("\n----" + cf.minusChordsToString());
//		for (double d = -6.0; d < 19.0; d++){
//			println(d + ": " + cf.getPrevailingChord(d));
//		}
		LiveClip testMelody = makeTestMelody();
		for (LiveMidiNote lmn: testMelody.noteList){
			println(lmn.toString());
			println(cf.getPrevailingChordSymbol(lmn.position, testMelody));
		}
	}
	private void newChordFormTest(){
		LiveClip lc = makeLiveClip();
//		println(lc.toString());
		ChordForm cf = new ChordForm(lc);
		println("ChordForm:-----------------\n" + cf.toString());
		println("\n----" + cf.slashChordsToString());
		println("\n----" + cf.minusChordsToString());
		for (double pos = 0.0; pos < 10; pos += 0.3){
			println(pos + "-" + cf.getPrevailingChordSymbol(pos));
		}
	}

	private LiveClip makeTestMelody() {
		LiveClip lc = new LiveClip(16.0, 0.0, 16.0, 0.0, 16.0, 4, 4, 0.0, 0, 1, "Melody", 0);
		lc.addNote(72, 0.0, 3.5, 100, 0);
		lc.addNote(72, 3.5, 0.5, 100, 0);
		lc.addNote(72, 4.0, 3.5, 100, 0);
		lc.addNote(60, 7.5, 4.5, 100, 0);
		return lc;
	}

	private LiveClip makeLiveClip() {
		LiveClip lc = new LiveClip(0, 0);
		lc.loopStart = 4.0;
		lc.loopEnd = 12.0;
		lc.name= "Progression";
		lc.addNote(60, 4.0, 4.0, 100, 0);
		lc.addNote(64, 4.0, 4.0, 100, 0);
		lc.addNote(67, 4.0, 4.0, 100, 0);
		lc.addNote(71, 4.0, 4.0, 100, 0);
		lc.addNote(74, 4.0, 4.0, 100, 0);
		
		lc.addNote(60, 8.0, 4.0, 100, 0);
		lc.addNote(65, 8.0, 4.0, 100, 0);
		lc.addNote(68, 8.0, 4.0, 100, 0);
		lc.addNote(63, 8.0, 4.0, 100, 0);
		lc.addNote(67, 8.0, 4.0, 100, 0);
		lc.length = 8.0;
		return lc;
	}
}
