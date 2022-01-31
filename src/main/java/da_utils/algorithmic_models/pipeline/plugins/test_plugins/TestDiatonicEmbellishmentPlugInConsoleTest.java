package main.java.da_utils.algorithmic_models.pipeline.plugins.test_plugins;
import com.cycling74.max.Atom;

import acm.program.ConsoleProgram;
import main.java.da_utils.ableton_live.max_atom_utils.DougzAtomUtilities;
import main.java.da_utils.algorithmic_models.pipeline.Pipeline;
import main.java.da_utils.algorithmic_models.pipeline.pipeline_parent.PipelineParent;
import main.java.da_utils.resource_objects.ChordForm;
import main.java.da_utils.resource_objects.ResourceObject;
import main.java.da_utils.resource_objects.ResourceObjectParent;
import main.java.da_utils.test_utils.TestData;


public class TestDiatonicEmbellishmentPlugInConsoleTest extends ConsoleProgram 
implements PipelineParent, ResourceObjectParent{
	
	public ResourceObject ro = new ResourceObject();	//placeholder for PipelineParent compatibility
	public ResourceObject ro(){
		return ro;
	}	
	
	public void run(){
		setSize(700, 900);
		Pipeline p = new Pipeline(0, this);
		ResourceObject ro = new ResourceObject(this);
//		TwoBarRhythmBuffer rb = new TwoBarRhythmBuffer();
		TestData.fourOnFloorForRhythmBuffer(ro.rbgc.getRhythmBuffer());
		makeProgression(ro);
		
		println(ro.cp.toString());
//		println(ro.rbgc.getRhythmBuffer().buffyToString());
//		p.addPlugInOption(new PlugInBassFromRhythmBuffer(ro));
//		p.addPlugInOption(new TestDiatonicEmbellishmentPlugIn(ro));
//		p.addPlugIn(0);
//		p.addPlugIn(1);
//		println(p.toString());

//		formTest(ro.cp.form);

//		PipelineNoteList pnl = p.makeNoteList();
//		println(pnl);
		
//		ClipInjector ci = new ClipInjector(this);
//		ci.inject(pnl);
	}
	
	// various parent methods------------------------------------------------------------
		public void consolePrint(String str){
			println(str);
		}
		public void postSplit(String str, String splitter){
			String[] strArr = str.split(splitter);
			for (String s: strArr){
				println(s);
			}
		}
		public void sendRhythmBufferMessage(Atom[] atArr){
			println("rhythmBufferOutlet: " + DougzAtomUtilities.atomArrToString(atArr));
		}
		public void sendChordProgrammerMessage(Atom[] atArr){
			println("chordProgrammerOutlet: " + DougzAtomUtilities.atomArrToString(atArr));
		}
		public void sendPipelineMessage(Atom[] atArr){
			println("PipelineMessage: " + DougzAtomUtilities.atomArrToString(atArr));
		}

// privates ----------------------------------------------------------------=================
		
	private void formTest(ChordForm form){
		for (double d = 0.0; d < 24.0; d = d +0.5){
			println(d + ": " + form.getPrevailingChordSymbol(d));
		}
	}
	
	private void makeProgression(ResourceObject ro){
//		ChordProgrammer2 cp = new ChordProgrammer2(this);
		ro.cp.formLength = 4;
		ro.cp.noteIn(50, 100, 1); // Dm, i think
		ro.cp.noteIn(53, 100, 1);
		ro.cp.noteIn(57, 100, 1);
		ro.cp.chordButtonState(0, 0, 1);
		ro.cp.chordButtonState(0, 0, 0);
		ro.cp.noteIn(50, 0, 1); // Dm, i think
		ro.cp.noteIn(53, 0, 1);
		ro.cp.noteIn(57, 0, 1);
		
		ro.cp.noteIn(52, 100, 1); // Em, i think
		ro.cp.noteIn(55, 100, 1);
		ro.cp.noteIn(59, 100, 1);
		ro.cp.chordButtonState(2, 0, 1);
		ro.cp.chordButtonState(2, 0, 0);
		ro.cp.noteIn(52, 0, 1); // Dm, i think
		ro.cp.noteIn(55, 0, 1);
		ro.cp.noteIn(59, 0, 1);
		
	}

}

