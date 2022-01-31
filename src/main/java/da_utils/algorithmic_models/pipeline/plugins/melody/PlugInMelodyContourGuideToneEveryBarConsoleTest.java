package main.java.da_utils.algorithmic_models.pipeline.plugins.melody;
import com.cycling74.max.Atom;

import LegacyStuff.ChordProgrammer2;
import acm.program.ConsoleProgram;
import main.java.da_utils.ableton_live.max_atom_utils.DougzAtomUtilities;
import main.java.da_utils.algorithmic_models.pipeline.Pipeline;
import main.java.da_utils.algorithmic_models.pipeline.pipeline_parent.PipelineParent;
import main.java.da_utils.resource_objects.ResourceObject;
import main.java.da_utils.resource_objects.ResourceObjectParent;
import main.java.da_utils.resource_objects.TwoBarRhythmBuffer;
import acm.program.ConsoleProgram;


public class PlugInMelodyContourGuideToneEveryBarConsoleTest extends ConsoleProgram 
implements ResourceObjectParent, PipelineParent{

	public ResourceObject ro = new ResourceObject();
	public ResourceObject ro(){
		return ro;
	}
	
	public void run(){
		setSize(900, 700);
		Pipeline p = new Pipeline(0, this);
	
		TwoBarRhythmBuffer rb = new TwoBarRhythmBuffer();
//		TestData.fourOnFloorForRhythmBuffer(rb);
		makeProgression(ro);

//		ContourData cd = new ContourData();
		ro.cd.newData(0.5, 1, 3, 1);
		
		p.addPlugInOption(new PlugInMelodyContourGuideToneEveryBar());
		p.addPlugIn(0);
	
		println(p.makeNoteList().toString());
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
// privates ------------------------------------------------------------------------------
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
