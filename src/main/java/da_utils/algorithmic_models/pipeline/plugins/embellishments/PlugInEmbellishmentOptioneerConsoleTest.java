package main.java.da_utils.algorithmic_models.pipeline.plugins.embellishments;
import com.cycling74.max.Atom;

import acm.program.ConsoleProgram;
import main.java.da_utils.ableton_live.max_atom_utils.DougzAtomUtilities;
import main.java.da_utils.algorithmic_models.pipeline.Pipeline;
import main.java.da_utils.algorithmic_models.pipeline.pipeline_note_list.PipelineNoteList;
import main.java.da_utils.algorithmic_models.pipeline.pipeline_note_list.PipelineNoteObject;
import main.java.da_utils.algorithmic_models.pipeline.pipeline_parent.PipelineParent;
import main.java.da_utils.algorithmic_models.pipeline.plugins.keys.PlugInKeysPad;
import main.java.da_utils.algorithmic_models.pipeline.plugins.pipeline_plugin.PlayPlugArgument;
import main.java.da_utils.chord_scale_dictionary.ChordScaleDictionary;
import main.java.da_utils.resource_objects.ResourceObject;
import main.java.da_utils.resource_objects.ResourceObjectParent;
import main.java.da_utils.resource_objects.TwoBarRhythmBuffer;
import main.java.da_utils.test_utils.TestData;


public class PlugInEmbellishmentOptioneerConsoleTest extends ConsoleProgram implements PipelineParent, ResourceObjectParent{
	
	public ResourceObject ro = new ResourceObject();	//placeholder for PipelineParent compatibility
	public ResourceObject ro(){
		return ro;
	}	
	
	public void run(){
		setSize(900, 700);
		ro = new ResourceObject(this);
		Pipeline p = new Pipeline(0, this);
		TwoBarRhythmBuffer rb = new TwoBarRhythmBuffer();
//		TestData.fourOnFloorForRhythmBuffer(rb);
		TestData.fourOnFloorForRhythmBuffer(ro.rbgc.getRhythmBuffer());
		ro.cd.newData(0.5, 1, 0, 1);
		
		p.addPlugInOption(new PlugInKeysPad());
//		p.addPlugInOption(new PlugInKikFourOnFloor());
//		p.addPlugInOption(new PlugInMelodyContourGuideToneEveryBar(cp, cd));
		double[] rhythmOptions = new double[]{-0.5, -1.0, - 1.5};
		double[] rhythmOptionChance = new double[]{1.0, 1.0, 1.0};
		double rhythmChance = 1.0;
		
//		ED[] embellishmentOptions = new ED[]{new ED("s", -1), new ED("d", -1), new ED("d", 1), new ED("c", -1), new ED("c", 1)};
//		double[] embellishmentOptionChance = new double[]{1.0, 1.0, 1.0, 1.0, 1.0};

		ED[] embellishmentOptions = new ED[]{new ED("s", 0)};
		double[] embellishmentOptionChance = new double[]{1.0};

		p.addPlugInOption(new PlugInEmbellishmentOptioneer(rhythmOptions, rhythmOptionChance, rhythmChance, embellishmentOptions, embellishmentOptionChance));

		p.addPlugIn(0);
//		p.addPlugIn(1);
//		p.addPlugIn(1);
		
		PipelineNoteList pnl = p.makeNoteList();
	
		println(p.makeNoteList().toString());
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
	
	private void makeProgression(){
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
