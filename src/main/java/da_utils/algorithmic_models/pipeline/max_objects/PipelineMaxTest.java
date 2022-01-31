package main.java.da_utils.algorithmic_models.pipeline.max_objects;
import java.util.ArrayList;

import com.cycling74.max.Atom;
import com.cycling74.max.DataTypes;
import com.cycling74.max.MaxObject;

import LegacyStuff.ChordProgrammer2;
import main.java.da_utils.ableton_live.ableton_live_clip.LiveClip;
import main.java.da_utils.ableton_live.clip_injector.ClipInjector;
import main.java.da_utils.ableton_live.clip_injector.ClipInjectorParent;
import main.java.da_utils.algorithmic_models.pipeline.Pipeline;
import main.java.da_utils.algorithmic_models.pipeline.pipeline_note_list.PipelineNoteList;
import main.java.da_utils.algorithmic_models.pipeline.pipeline_note_list.PipelineNoteObject;
import main.java.da_utils.algorithmic_models.pipeline.pipeline_parent.PipelineParent;
import main.java.da_utils.algorithmic_models.pipeline.plugins.Pluggable;
import main.java.da_utils.algorithmic_models.pipeline.plugins.test_plugins.TestAddEmbellishment;
import main.java.da_utils.algorithmic_models.pipeline.plugins.test_plugins.TestAddTwoMoreVoicesPlugIn;
import main.java.da_utils.algorithmic_models.pipeline.plugins.test_plugins.TestLegatoPlugIn;
import main.java.da_utils.algorithmic_models.pipeline.plugins.test_plugins.TestMakeGuideTone;
import main.java.da_utils.algorithmic_models.pipeline.plugins.test_plugins.TestVelocityPlugIn;
import main.java.da_utils.resource_objects.ChordForm;
import main.java.da_utils.resource_objects.ResourceObject;
import main.java.da_utils.resource_objects.TwoBarRhythmBuffer;
import main.java.da_utils.test_utils.TestData;




public class PipelineMaxTest extends MaxObject implements PipelineParent, ClipInjectorParent{
	
	public int clipObjectOutlet = 0;
	public ClipInjector ci = new ClipInjector(this);
	public Pipeline pl = new Pipeline(3, this);
	public ResourceObject ro = new ResourceObject();	//placeholder for PipelineParent compatibility
	
	public int programmerIndex = 3;				// artibrary for testing. matches pipelineProgrammer panel index in max4live
	public int programmerOutlet = 1;
	
	public PipelineMaxTest(){
		post("Pipeline initializing.....");
		setMaxInlets();
		makePipeline();
		post("done!");
	}
	public void init(){
		initClipObjects();
		clearPanel();
		loadOptionList();
	}
	public void initClipObjects(){
		post("clipObjectLength: " + TestData.makeClipInfoObjects().length);
		ci.initializeClipObjects(TestData.makeClipInfoObjects());
	}
	public void newRandomSequence(){
		pl.newRandomSequence();
	}
	public void generateClip(int clipIndex){
		// this does not work correctly as it does not load continous controllers or pitchbend from the pipeline output
		ci.inject(TestData.makeLCList(), TestData.makeCCList(), TestData.makePBList());
	}
	public void postPipeline(){
		for (Pluggable ppi: pl.plugList){
			post(ppi.name());
		}
	}
	public void addPlugIn(int clipIndex, int optionIndex){
		post("adding plugIn index " + optionIndex);
		pl.addPlugIn(optionIndex);
		loadPipelineGUI();
	}
	public void deletePlugIn(int clipIndex, int pipeIndex){
		post("deleting plugIn index " + pipeIndex);
		pl.removePlugIn(pipeIndex);
		loadPipelineGUI();
	}
	public void plugOnOff(int clipIndex, int pipeIndex, int active){
		pl.setPlugActive(pipeIndex, active);
	}
	public void newRandomSequence(int clipIndex){
		pl.newRandomSequence();
	}
	public void openPanel(){
		pl.openPanel();
	}
//	private void updatePipeList(int panelIndex){
//		outlet(plArray[panelIndex].plugListNameAtomArray(new Atom[]{
//				Atom.newAtom(panelIndex),
//				Atom.newAtom("pipelist")
//		}));
//	}
	public void postit(String str){
		post(str);
	}
	public void out(Atom[] atArr){
		outlet(programmerOutlet, atArr);
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
		outlet(clipObjectOutlet, atArr);
	}
	public void consolePrint(String str){
		post(str);
	}

// pipeline programmer calls -------------------------------------------------
	
	
	
// privates ===============================================================
	private ArrayList<LiveClip> makeLCList(){
		ArrayList<LiveClip> lcList = new ArrayList<LiveClip>();
		lcList.add(makeLiveClip(pl.makeNoteList()));
		return lcList;
	}
	private void loadPipelineGUI(){
//		sendMessage(programmerOutlet, pl.plugListNameAtomArray(new Atom[]{
//				Atom.newAtom(programmerIndex),
//				Atom.newAtom("pipelist")
//		}));
		pl.updatePipeList();
	}
	private void loadOptionList(){
		sendMessage(programmerOutlet, pl.plugOptionListNameAtomArray(new Atom[]{
				Atom.newAtom(programmerIndex),
				Atom.newAtom("optionlist")
		}));
	}
	private void clearPanel(){
		sendMessage(programmerOutlet, new Atom[]{
				Atom.newAtom(programmerIndex),
				Atom.newAtom("clearoptionlist")
		});
		sendMessage(programmerOutlet, new Atom[]{
				Atom.newAtom(programmerIndex),
				Atom.newAtom("clearpipelist")
		});
	}
	private LiveClip makeLiveClip(PipelineNoteList pnl){
		LiveClip lc = new LiveClip(0, 0);
		lc.length = pnl.length;
		lc.setLoopStart(0.0);
		lc.setLoopEnd(pnl.length);
		for (PipelineNoteObject pno: pnl.pnoList){
			lc.addNoteList(pno.noteList);
		}
		return lc;
	}
	private void makePipeline(){
		pl.addPlugInOption(new TestMakeGuideTone());
		pl.addPlugInOption(new TestAddTwoMoreVoicesPlugIn());
		pl.addPlugInOption(new TestAddEmbellishment());
		pl.addPlugInOption(new TestLegatoPlugIn(new double[]{0.0, 1.0, 0.5}));
		pl.addPlugInOption(new TestVelocityPlugIn());
	}
	public void postSplit(String str, String splitter){
		String[] strArr = str.split(splitter);
		for (String s: strArr){
			post(s);
		}
	}
	public void sendPipelineMessage(Atom[] atArr){
		
	}
	public ResourceObject ro(){
		return ro;
	}
	private void sendMessage(int out, Atom[] message){
		outlet(out, message);
	}
	private void setMaxInlets(){
		declareInlets(new int[]{
				DataTypes.ALL,  
				DataTypes.INT}
		);
		declareOutlets(new int[]{ 
				DataTypes.ALL, 
				DataTypes.ALL}
		);
		setInletAssist(new String[]{
			"messages in",
			"erm...."}
		);
		setOutletAssist(new String[]{
			"clipObject messages out",
			"dump out"}
		);
	}
}
