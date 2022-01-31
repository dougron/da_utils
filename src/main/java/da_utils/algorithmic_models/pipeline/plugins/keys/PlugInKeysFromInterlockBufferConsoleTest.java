package main.java.da_utils.algorithmic_models.pipeline.plugins.keys;
import com.cycling74.max.Atom;

import LegacyStuff.ChordProgrammer2;
import acm.program.ConsoleProgram;
import main.java.da_utils.ableton_live.max_atom_utils.DougzAtomUtilities;
import main.java.da_utils.algorithmic_models.pipeline.Pipeline;
import main.java.da_utils.algorithmic_models.pipeline.pipeline_note_list.PipelineNoteList;
import main.java.da_utils.algorithmic_models.pipeline.pipeline_parent.PipelineParent;
import main.java.da_utils.algorithmic_models.pipeline.plugins.embellishments.ED;
import main.java.da_utils.algorithmic_models.pipeline.plugins.embellishments.PlugInEmbellishmentOptioneer;
import main.java.da_utils.resource_objects.ChordForm;
import main.java.da_utils.resource_objects.ResourceObject;
import main.java.da_utils.resource_objects.ResourceObjectParent;
import main.java.da_utils.test_utils.TestData;


public class PlugInKeysFromInterlockBufferConsoleTest extends ConsoleProgram 
	implements ResourceObjectParent, PipelineParent{
	
	ResourceObject ro = new ResourceObject();
	public ResourceObject ro(){
		return ro;
	}
	public void run(){
		setSize(900, 700);
		Pipeline p = new Pipeline(0, this);
		
		
		TestData.fourOnFloorForRhythmBuffer(ro.rbgc.getRhythmBuffer());
		ro.rbgc.generateActiveInterlockBuffer();


		makeProgression(ro);
		
		println(ro.cp.toString());
		println(ro.rbgc.getRhythmBuffer().buffyToString());
		println(ro.rbgc.getInterlockBuffer().buffyToString());
		println(ro.cp.getChordForm().toString());
		
		p.addPlugInOption(new PlugInKeysFromInterlockBuffer());
		addPlugInOptioneer(p, ro);										// carry on debugging here. step through this to see where its all going to shitz
		
		p.addPlugIn(0);
		p.addPlugIn(1);
		p.addPlugIn(1);
////		p.addPlugIn(1);
////		p.addPlugIn(1);
		println("====================================================================");
		println(p.toString());

////		formTest(cp.form);
////		println(p.form.length());
		PipelineNoteList pnl = p.makeNoteList();
		println(pnl);
//		println(pnl.pnoList.size() + " items in pnoList......");
		
////		ClipInjector ci = new ClipInjector(this);
////		ci.inject(pnl);
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
// privates ----------------------------------------------------------------------
	private void addPlugInOptioneer(Pipeline p, ResourceObject ro){
		double[] rhythmOptions = new double[]{-0.5, -1.0, - 1.5};
		double[] rhythmOptionChance = new double[]{1.0, 1.0, 1.0};
		double rhythmChance = 1.0;
		ED[] embellishmentOptions = new ED[]{new ED("s", -1), new ED("d", -1), new ED("d", 1), new ED("c", -1), new ED("c", 1)};
		double[] embellishmentOptionChance = new double[]{1.0, 1.0, 1.0, 1.0, 1.0};
		
		p.addPlugInOption(new PlugInEmbellishmentOptioneer(rhythmOptions, rhythmOptionChance, rhythmChance, embellishmentOptions, embellishmentOptionChance));

	}
	
	private void formTest(ChordForm form){
		for (double d = 0.0; d < 24.0; d = d +0.5){
			println(d + ": " + form.getPrevailingChordSymbol(d));
		}
	}
	
	private void makeProgression(ResourceObject ro){
		ChordProgrammer2 cp = ro.cp;
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
		cp.noteIn(52, 0, 1); // Em, i think
		cp.noteIn(55, 0, 1);
		cp.noteIn(59, 0, 1);

	}

}

