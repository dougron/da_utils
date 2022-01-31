package main.java.da_utils.algorithmic_models.pipeline.plugins.drums;
import com.cycling74.max.Atom;

import acm.program.ConsoleProgram;
import main.java.da_utils.ableton_live.max_atom_utils.DougzAtomUtilities;
import main.java.da_utils.algorithmic_models.pipeline.Pipeline;
import main.java.da_utils.algorithmic_models.pipeline.pipeline_note_list.PipelineNoteList;
import main.java.da_utils.algorithmic_models.pipeline.pipeline_note_list.PipelineNoteObject;
import main.java.da_utils.algorithmic_models.pipeline.pipeline_parent.PipelineParent;
import main.java.da_utils.algorithmic_models.pipeline.plugins.pipeline_plugin.PlayPlugArgument;
import main.java.da_utils.chord_scale_dictionary.ChordScaleDictionary;
import main.java.da_utils.resource_objects.ResourceObject;
import main.java.da_utils.resource_objects.ResourceObjectParent;
import main.java.da_utils.test_utils.TestData;
import acm.program.ConsoleProgram;


public class PlugInKikFourOnFloorConsoleTest extends ConsoleProgram implements PipelineParent, ResourceObjectParent{

	ResourceObject ro = new ResourceObject();
	public ResourceObject ro(){
		return ro;
	}
	
	public void run(){
		setSize(900, 700);
		ro = new ResourceObject(this);
		makeProgression();
		Pipeline p = new Pipeline(0, this);
//		TwoBarRhythmBuffer rb = new TwoBarRhythmBuffer();
		TestData.tangoForRhythmBuffer(ro.rbgc.getRhythmBuffer());

		
		println(ro.cp.toString());
		p.addPlugInOption(new PlugInKikFourOnFloor());
		p.addPlugInOption(new PlugInKikFromRhythmBuffer());
		p.addPlugIn(0);
		p.addPlugIn(1);
		println(p.toString());

		PipelineNoteList pnl = p.makeNoteList();
		println(pnl);

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
	
	private void makeProgression(){
		
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
