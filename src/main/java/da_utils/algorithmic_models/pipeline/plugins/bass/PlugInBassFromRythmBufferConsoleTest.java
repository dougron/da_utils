package main.java.da_utils.algorithmic_models.pipeline.plugins.bass;
import com.cycling74.max.Atom;

import acm.program.ConsoleProgram;
import main.java.da_utils.ableton_live.clip_injector.ClipInjector;
import main.java.da_utils.ableton_live.clip_injector.ClipInjectorParent;
import main.java.da_utils.ableton_live.max_atom_utils.DougzAtomUtilities;
import main.java.da_utils.algorithmic_models.pipeline.Pipeline;
import main.java.da_utils.algorithmic_models.pipeline.pipeline_note_list.PipelineNoteList;
import main.java.da_utils.algorithmic_models.pipeline.pipeline_parent.PipelineParent;
import main.java.da_utils.resource_objects.ChordForm;
import main.java.da_utils.resource_objects.ResourceObject;
import main.java.da_utils.resource_objects.ResourceObjectParent;
import main.java.da_utils.test_utils.TestData;
import LegacyStuff.ChordProgrammer2;


/*
 * This class uses the ResourceObject which as of 3 July 2018, I cannot remember the significance of 
 * and I suspect it is not going to be used anymore. So this class does not work, but does not 
 * neccesarily reflect on the functionality of the PlugInBassFromRythmBuffer
 */


public class PlugInBassFromRythmBufferConsoleTest extends ConsoleProgram 
implements PipelineParent, ClipInjectorParent, ResourceObjectParent{
	
//	TwoBarRhythmBuffer rb;
//	ChordProgrammer2 cp;
//	public ResourceObject ro;
	public ResourceObject ro = new ResourceObject();	//placeholder for PipelineParent compatibility
	public ResourceObject ro(){
		return ro;
	}	
	
	public void run(){
		setSize(700, 900);
		Pipeline p = new Pipeline(0, this);
//		rb = new TwoBarRhythmBuffer();
		TestData.fourOnFloorForRhythmBuffer(ro.rbgc.getRhythmBuffer());
		makeProgression(ro);
		
		println(ro.cp.toString());
		println(ro.rbgc.getRhythmBuffer().buffyToString());
		p.addPlugInOption(new PlugInBassFromRhythmBuffer());
		p.addPlugIn(0);
		println(p.toString());

		PipelineNoteList pnl = p.makeNoteList();
		println(pnl);
		
		ClipInjector ci = new ClipInjector(this);
		ci.inject(pnl);
	}
	
// ResourceObjectParentMethods
	public void sendRhythmBufferMessage(Atom[] atArr){
		println("RhythmBufferMessage-----------------------------");
		println(DougzAtomUtilities.atomArrToString(atArr));
	}
	public void sendChordProgrammerMessage(Atom[] atArr){
		println("ChordProgrammerMessage-----------------------------");
		println(DougzAtomUtilities.atomArrToString(atArr));
	}
	
	public void postSplit(String str, String splitter){
		String[] strArr = str.split(splitter);
		for (String s: strArr){
			println(s);
		}
	}
	
// ClipInjectorParent methods ----------------------------------------------------------------
	
	public void sendClipObjectMessage(Atom[] atArr){
		println(DougzAtomUtilities.atomArrToString(atArr));
	}
	public void consolePrint(String str){
		println(str);
	}
	
// PipelineParent methods -------------------------------------------------------------------
	public void sendPipelineMessage(Atom[] atArr){
		println(DougzAtomUtilities.atomArrToString(atArr));
	}
//	public void out(Atom[] atArr){
//		println(DougzAtomUtilities.atomArrToString(atArr));
//	}
//	public void postit(String str){
//		println(str);
//	}
//	public TwoBarRhythmBuffer getRhythmBuffer(){
//		return ro.rbgc.getRhythmBuffer();
//	}
//	public TwoBarRhythmBuffer getInterlockBuffer(){
//		return ro.rbgc.getInterlockBuffer();			// placeholder. there is no relevant InterlockBuffer in this test program
//	}
//	public ChordForm getChordForm(){
//		return ro.cp.form;
//	}
//	public ChordProgrammer2 getChordProgrammer(){
//		return ro.cp;
//	}
//	public double getFormLength(){
//		return ro.cp.form.length();
//	}
//	public int getDynamicIndex(){
//		return 1;			// plceholdr, not used
//	}
	
// privates ---------------------------------------------------------------------------------
	
	private void formTest(ChordForm form){
		for (double d = 0.0; d < 24.0; d = d +0.5){
			println(d + ": " + form.getPrevailingChordSymbol(d));
		}
	}
	
	private void makeProgression(ResourceObject ro){
		ChordProgrammer2 cp = ro.cp;
		cp.formLength = 2;
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
		cp.chordButtonState(1, 0, 1);
		cp.chordButtonState(1, 0, 0);
		cp.noteIn(52, 0, 1); // Em, i think
		cp.noteIn(55, 0, 1);
		cp.noteIn(59, 0, 1);

	}


}
