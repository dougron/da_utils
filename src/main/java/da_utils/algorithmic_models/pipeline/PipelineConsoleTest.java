package main.java.da_utils.algorithmic_models.pipeline;
import com.cycling74.max.Atom;

import LegacyStuff.ChordProgrammer2;
import main.java.da_utils.ableton_live.ableton_live_clip.LiveClip;
import main.java.da_utils.ableton_live.ableton_live_clip.controller_data_clips.ControllerClip;
import main.java.da_utils.ableton_live.ableton_live_clip.controller_data_clips.PitchBendClip;
import main.java.da_utils.ableton_live.clip_injector.ClipInjector;
import main.java.da_utils.ableton_live.clip_injector.ClipInjectorParent;
import main.java.da_utils.ableton_live.max_atom_utils.DougzAtomUtilities;
import main.java.da_utils.algorithmic_models.pipeline.pipeline_note_list.PipelineNoteList;
import main.java.da_utils.algorithmic_models.pipeline.pipeline_note_list.PipelineNoteObject;
import main.java.da_utils.algorithmic_models.pipeline.pipeline_parent.PipelineParent;
import main.java.da_utils.algorithmic_models.pipeline.plugins.test_plugins.TestAddEmbellishment;
import main.java.da_utils.algorithmic_models.pipeline.plugins.test_plugins.TestAddTwoMoreVoicesPlugIn;
import main.java.da_utils.algorithmic_models.pipeline.plugins.test_plugins.TestLegatoPlugIn;
import main.java.da_utils.algorithmic_models.pipeline.plugins.test_plugins.TestMakeGuideTone;
import main.java.da_utils.algorithmic_models.pipeline.plugins.test_plugins.TestVelocityPlugIn;
import main.java.da_utils.resource_objects.ChordForm;
import main.java.da_utils.resource_objects.ResourceObject;
import main.java.da_utils.resource_objects.TwoBarRhythmBuffer;


public class PipelineConsoleTest implements PipelineParent, ClipInjectorParent{
	
	ClipInjector ci = new ClipInjector(this);
	ResourceObject ro = new ResourceObject();		// placeholder for PipelienParent compatibility................
	
	public PipelineConsoleTest(){

		Pipeline pl = new Pipeline(0, this);
		makePipeline(pl);
		System.out.println(pl.toString());
//		doPlugInTest(pl);
		doSwingPanelTest(pl);
	}
	public void postit(String str){
		System.out.println(str);
	}
	public void out(Atom[] atomArr){
		System.out.println("ouput to outlet.........");
		System.out.println(DougzAtomUtilities.atomArrToString(atomArr));
	}
	public TwoBarRhythmBuffer getRhythmBuffer(){
		return new TwoBarRhythmBuffer();		// placeholder, not intended to be used meaningfully
	}
	public TwoBarRhythmBuffer getInterlockBuffer(){
		return new TwoBarRhythmBuffer();		// placeholder, not intended to be used meaningfully
	}
	public ChordForm getChordForm(){
		return new ChordForm(new LiveClip(0, 0)); // placeholder, not intended to be used meaningfully
	}
	public ChordProgrammer2 getChordProgrammer(){
		return new ChordProgrammer2();			// placeholder, not intended to be used meaningfully
	}
	public double getFormLength(){
		return 0.0;								// placeholder, not intended to be used meaningfully				
	}
	public int getDynamicIndex(){
		return 0;								// placeholder, not intended to be used meaningfully
	}
	public void sendClipObjectMessage(Atom[] atArr){
		System.out.println(DougzAtomUtilities.atomArrToString(atArr));
	}
	public void consolePrint(String str){
		System.out.println(str);
	}
	
// PipelineParent methids --------------------------------------------------------------
	
	public void sendPipelineMessage(Atom[] atArr){}
	public void postSplit(String str, String splitter){}
	public ResourceObject ro(){
		return ro;
	}


// privates ---------------------------------------------------------------------------
	
	private void doSwingPanelTest(Pipeline pl){
		pl.openPanel();
	}
	private void doPlugInTest(Pipeline pl){
		pl.addPlugIn(1);
		pl.addPlugIn(2);
		pl.addPlugIn(2);
		pl.addPlugIn(4);
		pl.addPlugIn(0);
		pl.addPlugIn(3);
		System.out.println(pl.toString());
//		pl.removePlugIn(1);
//		pl.removePlugIn(1);
//		System.out.println(pl.toString());
		System.out.println(stringFromAtom(pl.plugOptionListNameAtomArray(new Atom[]{
				Atom.newAtom(3), 
				Atom.newAtom("optionlist")
				})));
		System.out.println(stringFromAtom(pl.plugListNameAtomArray(new Atom[]{
				Atom.newAtom(3), 
				Atom.newAtom("pipelist")
				})));
		doit(pl);
//		pl.removePlugIn(1);
//		doit(pl);
	}
	private void doit(Pipeline pl){
		PipelineNoteList pnl = pl.makeNoteList();
		System.out.println(pnl.toString());
		LiveClip lc = makeLiveClip(pnl);
		System.out.println(lc.toString());		
		ci.inject(new LiveClip[]{lc}, new ControllerClip[]{}, new PitchBendClip[]{});		
	}
	private LiveClip makeLiveClip(PipelineNoteList pnl){
		LiveClip lc = new LiveClip(0, 0);
		lc.length = pnl.length;
		lc.loopEnd = pnl.length;
		for (PipelineNoteObject pno: pnl.pnoList){
			lc.addNoteList(pno.noteList);
		}
		return lc;
	}
	private void makePipeline(Pipeline pl){
		pl.addPlugInOption(new TestMakeGuideTone());
		pl.addPlugInOption(new TestAddTwoMoreVoicesPlugIn());
		pl.addPlugInOption(new TestAddEmbellishment());
		pl.addPlugInOption(new TestLegatoPlugIn(new double[]{0.0, 1.0, 0.5}));
		pl.addPlugInOption(new TestVelocityPlugIn());
		
	}
	public String convertAtomToString(Atom atom){
		if (atom.isString()){
			return atom.getString();
		} else if (atom.isInt()){
			return Integer.toString(atom.getInt());
		} else if (atom.isFloat()){
			return Float.toString(atom.getFloat());
		}
		return "fail";
	}
	private String stringFromAtom(Atom[] atomArr){
		String str = "";
		for (int i = 0; i < atomArr.length; i++){
			str = str + convertAtomToString(atomArr[i]) + " ";
		}		
		return str;
	}
	
	public static void main (String[] args)
	{
		new PipelineConsoleTest();
	}

}
