package LegacyStuff;
import java.util.ArrayList;

import com.cycling74.max.Atom;

import DataObjects.note_buffer.NoteOnBuffer;
import main.java.da_utils.ableton_live.ableton_live_clip.LiveClip;
import main.java.da_utils.ableton_live.ableton_live_clip.LiveMidiNote;
import main.java.da_utils.chord_scale_dictionary.chord_analysis.ChordAnalysisObject;
import main.java.da_utils.resource_objects.ChordForm;


/*
 * attempt to debug the ChordProgrammer for use in DAPlay
 */
public class ChordProgrammer2 {
	
	public NoteOnBuffer nob  = new NoteOnBuffer();
	public ChordAnalysisObject[][] caoArr = new ChordAnalysisObject[16][2];	// [bar][beat]
	public int formLength = 4;
	public ChordForm form;
	
	public ChordProgrammer2Parent parent;

//	public int messageOutlet;
	
	public ChordProgrammer2(){
		// for debugging, will not work correctly
	}
	public ChordProgrammer2(ChordProgrammer2Parent cp2p){
		parent = cp2p;
	}

	public void clearAllButtons(){
		for (int i = 0; i < caoArr.length; i ++){
			for (int j = 0; j < caoArr[0].length; j++){
				clearButtonName(i, j);
			}
		}
	}
	public void setFormLength(int length){
		formLength = length;
		makeNewForm();
	}
	public void setFormLengthFromIndex(int lengthIndex){
		// 0 = 2 bars, 1 = 4 bars, 2 = 8 bars, 3 = 16bars
		formLength = (int)Math.pow(2.0, lengthIndex + 1.0);
		makeNewForm();
		outlet(new Atom[]{
				Atom.newAtom(setFormLengthMessage),
				Atom.newAtom(default_formLength)
		});
	}
	public void noteIn(int n, int v, int ch){
		nob.noteIn(n, v, ch);
	}
	public void sendActiveButtons(){
		// this can be optimized to send one big Atom[] with all the button data
		post("sending active buttons................");
		for (int i = 0; i < formLength; i++){
			ChordAnalysisObject[] caoTempArr = caoArr[i];
			//post("bar: " + i + " arraylength: " + caoTempArr.length);
			for (int j = 0; j < caoTempArr.length; j++){
				//post("doing " + i + "  " + j);
				if (caoTempArr[j] != null){
					sendButtonName(caoTempArr[j].chordToString(), i, j);
					//post("bar: " + i + " index " + j + " actually done.");
				}
			}
		}
	}
	public void chordButtonState(int bar, int beat, int state){		
		post("chordButtonState " + bar + " " + beat + " " + state);		
		if (state == 0){
			if (nob.size() == 0){
				clearChordInForm(bar, beat);
			} else {
				addNobToForm(bar, beat);
			}
		}
	}
	public void sendChordButtonText(int bar, int beat){
		if (caoArr[bar][beat] == null){
			clearButtonName(bar, beat);
		} else {
			sendButtonName(caoArr[bar][beat].chordToString(), bar, beat);
		}
	}
	public void postProgression(){
		post("====================================");
		postSplit(form.chunkListToString(), "\n");
		post(form.slashChordsToString());
	}
	public void postFormLength(){
		post("formLength: " + formLength);
	}
	public ChordForm getChordForm(){
		return form;
	}
	public double formLengthInQuarters(){
		return form.length();
	}
	public String toString(){
		return "ChprdProgrammer2 toString()\nform:\n" + form.toString();
	}
	
	
// privates --------------------------------------------------------------------
	private void makeNewForm(){
		LiveClip lc = new LiveClip(0, 0);
		lc.setLength((double)formLength * 4.0);		// magic 4.0: implies 4/4 time
		double[] buttonInc = new double[]{0.0, 2.0};		// offset distance in quarter notes of each button in relation to the bar.
		ArrayList<LiveMidiNote> lmnList = new ArrayList<LiveMidiNote>();
		for (int i = 0; i < formLength; i ++){
			for (int j = 0; j < caoArr[0].length; j++){
				if (caoArr[i][j] != null){
					lmnList.clear();
					for (Integer note: caoArr[i][j].noteList){
						double pos = i * 4.0 + buttonInc[j];			// 4.0 magic implies 4/4 time
						LiveMidiNote lmn = new LiveMidiNote(note, pos, default_note_length, 100, 0);
						lc.addNote(lmn);
						lmnList.add(lmn);
					}
				} else {
					for (LiveMidiNote lmn: lmnList){
						lmn.length += 2.0;			// magic number make the note a half bar longer
					}
				}
			}
		}
		postSplit(lc.toString(), "\n");
		form = new ChordForm(lc);
	}
	private void setChordButtonName(int bar, int beat, String str){
		outlet(new Atom[]{
//				Atom.newAtom("toChordButtons"),
				Atom.newAtom(bar),
				Atom.newAtom(beat),
				Atom.newAtom("setButtonName"),				
				Atom.newAtom(str)
		});
	}
	
	private void addNobToForm(int bar, int beat){
		ChordAnalysisObject cao = new ChordAnalysisObject(nob.noteBuffer);
		if (!cao.chordToString().equals("noChord")){
			caoArr[bar][beat] = cao;
			setChordButtonName(bar, beat, cao.chordToString());
			makeNewForm();
			post(form.slashChordsToString());
		}		
	}
	
	private void clearChordInForm(int bar, int beat){
		caoArr[bar][beat] = null;
		clearButtonName(bar, beat);
		makeNewForm();
	}
	
	private void sendButtonName(String str, int bar, int button){
		//post("sendButtonName: " + str);
		outlet(new Atom[]{
//				Atom.newAtom("toChordButtons"),
				Atom.newAtom(bar),
				Atom.newAtom(button),
				Atom.newAtom("setButtonName"),
				Atom.newAtom(str)
		});
	}
	
	private void clearButtonName(int bar, int button){
		outlet(new Atom[]{
//				Atom.newAtom("toChordButtons"),
				Atom.newAtom(bar),
				Atom.newAtom(button),
				Atom.newAtom("clearButtonName")
		});
	}
	public void postSplit(String str, String splitter){
		String[] strArr = str.split(splitter);
		for (String s: strArr){
			post(s);
		}
	}
	
	private void post(String str){
		parent.consolePrint(str);
//		if (parentIndex == 0) parent.post("ChordProgrammer: " + str);
//		if (parentIndex == 1) parent1.println("ChordProgrammer: " + str);
//		if (parentIndex == 2) parent2.println("ChordProgrammer: " + str);
//		if (parentIndex == 3) parent3.println("ChordProgrammer: " + str);
//		if (parentIndex == 4) parent4.println("ChordProgrammer: " + str);
//		if (parentIndex == 5) parent5.println("ChordProgrammer: " + str);
//		if (parentIndex == 6) parent6.println("ChordProgrammer: " + str);
//		if (parentIndex == 7) parent7.println("ChordProgrammer: " + str);
//		if (parentIndex == 8) parent8.println("ChordProgrammer: " + str);
//		if (parentIndex == 9) parent9.println("ChordProgrammer: " + str);
//		if (parentIndex == 10) parent10.println("ChordProgrammer: " + str);
	}
	private void outlet(Atom[] atomArr){
		parent.sendChordProgrammerMessage(atomArr);
//		if (parentIndex == 0) parent.outlet(messageOutlet, atomArr);
//		if (parentIndex == 1) parent1.println("cp_outlet:" + DougzAtomUtilities.atomArrToString(atomArr));
//		if (parentIndex == 2) parent2.println("cp_outlet:" + DougzAtomUtilities.atomArrToString(atomArr));
//		if (parentIndex == 3) parent3.println("cp_outlet:" + DougzAtomUtilities.atomArrToString(atomArr));
//		if (parentIndex == 4) parent4.println("cp_outlet:" + DougzAtomUtilities.atomArrToString(atomArr));
//		if (parentIndex == 5) parent5.println("cp_outlet:" + DougzAtomUtilities.atomArrToString(atomArr));
//		if (parentIndex == 6) parent6.println("cp_outlet:" + DougzAtomUtilities.atomArrToString(atomArr));
//		if (parentIndex == 7) parent7.println("cp_outlet:" + DougzAtomUtilities.atomArrToString(atomArr));
//		if (parentIndex == 8) parent8.println("cp_outlet:" + DougzAtomUtilities.atomArrToString(atomArr));
//		if (parentIndex == 9) parent9.println("cp_outlet:" + DougzAtomUtilities.atomArrToString(atomArr));
//		if (parentIndex == 10) parent10.println("cp_outlet:" + DougzAtomUtilities.atomArrToString(atomArr));
	}
	private double default_note_length = 2.0;
	private String setFormLengthMessage = "setFormLength";
	private int default_formLength = 1;	// index 1 = 4 bars

}
