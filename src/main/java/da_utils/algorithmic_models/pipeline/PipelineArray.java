package main.java.da_utils.algorithmic_models.pipeline;
import com.cycling74.max.Atom;

import main.java.da_utils.algorithmic_models.pipeline.pipeline_note_list.PipelineNoteList;
import main.java.da_utils.algorithmic_models.pipeline.pipeline_parent.PipelineParent;
import main.java.da_utils.algorithmic_models.pipeline.plugins.Pluggable;
import main.java.da_utils.resource_objects.ResourceObject;




public class PipelineArray implements PipelineParent{
	
	public Pipeline[] plArray;
	public PipelineArrayParent parent;
	public int programmerOutlet;
	
	private int test_dynamic_index = 2;		// for testing where there is no DAPlay
	
	
	public PipelineArray(PipelineArrayParent p, int pipelineProgrammerOutlet, Pipeline[] arr){
		plArray = arr;
		parent = p;
		for (Pipeline pl: plArray){
			pl.parent = this;
		}
		programmerOutlet = pipelineProgrammerOutlet;
		consolePrint("PipelineArray initialized.");
	}
	public void addPlugInOption(int index, Pluggable ppi){
		if (index >= 0 && index < plArray.length){
			plArray[index].addPlugInOption(ppi);
		}
	}
	public void addPlugInOption(String str, Pluggable ppi){
		for (Pipeline pl: plArray){
			if (pl.name == str){
				pl.addPlugInOption(ppi);
			}
		}
	}
	public void openPanel(int index){
		plArray[index].openPanel();
	}
	
	public void postContents(){
		for (Pipeline pl: plArray){
			postSplit(pl.toString(), "\n");
		}
	}
	public void setupPanels(){
		clearAllPanels();
		sendPipelineNames();
		sendOptionLists();
		for (int i = 0; i < plArray.length; i++){
			sendPipelineMessage(pipelineNameMessage(i));
			sendPipelineMessage(plArray[i].plugOptionListNameAtomArray(new Atom[]{
					Atom.newAtom(i),
					Atom.newAtom(optionListMessage)
			}));			
		}
		updateAllPipeLists();
	}
	public void updateAllPipeLists(){
		for (Pipeline p: plArray){
			p.updatePipeList();
		}
	}
	public void clearPanel(int index){
		clearPanelMessage(index);
	}
	public void clearAllPanels(){
		for (int i = 0; i < plArray.length; i++){
			clearPanelMessage(i);
		}
	}
	public void sendOptionList(int index){
		sendPipelineMessage(plArray[index].plugOptionListNameAtomArray(new Atom[]{
				Atom.newAtom(index),
				Atom.newAtom(optionListMessage)
		}));
	}
	public void newRandomSequence(int index){
		plArray[index].newRandomSequence();
	}
	public void addPlugIn(int panelIndex, int optionIndex){
		plArray[panelIndex].addPlugIn(optionIndex);
		updatePipeList(panelIndex);		
	}
	public void removePlugIn(int panelIndex, int plugIndex){
		plArray[panelIndex].removePlugIn(plugIndex);
		updatePipeList(panelIndex);
	}
	public void setPlugActive(int panelIndex, int pipeIndex, int active){
		plArray[panelIndex].setPlugActive(pipeIndex, active);
	}
	public PipelineNoteList renderPipeline(int index){
		consolePrint("PipelineArray.renderPipeline(int index): index = " + index);
		return plArray[index].makeNoteList();
	}
//	public int getDynamicIndex(){
//		if (parent == null){
//			return test_dynamic_index;
//		} else {
//			return parent.dynamic;
//		}
//	}
	public Pipeline[] plArray(){
		return plArray;
	}
//	public TwoBarRhythmBuffer getRhythmBuffer(){
//		return parent.rbgc.getRhythmBuffer();
//	}
//	public TwoBarRhythmBuffer getInterlockBuffer(){
//		return parent.rbgc.getInterlockBuffer();
//	}
//	public ChordForm getChordForm(){
//		return parent.cp.form;
//	}
//	public ChordProgrammer2 getChordProgrammer(){
//		return parent.cp;
//	}
//	public double getFormLength(){
//		return parent.cp.form.length();
//	}
	public void turnOffAllPlugIns(){
		for (Pipeline p: plArray){
			p.turnOffAllPlugIns();
		}
	}
	public void clearAllPipelines(){
		for (Pipeline p: plArray){
			p.plugList.clear();
		}
	}
	public ResourceObject ro(){
		return parent.ro();
	}
//	public int dynamic(){
//		return parent.dynamic();
//	}

// privates --------------------------------------------------------------
	
	private void updatePipeList(int panelIndex){
//		out(plArray[panelIndex].plugListNameAtomArray(new Atom[]{
//				Atom.newAtom(panelIndex),
//				Atom.newAtom("pipelist")
//		}));
		plArray[panelIndex].updatePipeList();
	}
	private void sendOptionLists(){
		for (int i = 0; i < plArray.length; i++){
			sendOptionList(i);
		}
	}
	private void sendPipelineNames(){
		for (int i = 0; i < plArray.length; i++){
			sendPipelineMessage(pipelineNameMessage(i));
			
			
		}
	}
	private void clearPanelMessage(int programmerPanelIndex){
		sendPipelineMessage(new Atom[]{
				Atom.newAtom(programmerPanelIndex),
				Atom.newAtom("clearoptionlist")
		});
		sendPipelineMessage(new Atom[]{
				Atom.newAtom(programmerPanelIndex),
				Atom.newAtom("clearpipelist")
		});
	}
	
	private Atom[] pipelineNameMessage(int index){
		return new Atom[]{
				Atom.newAtom(index),
				Atom.newAtom("name"),
				Atom.newAtom(plArray[index].name),
				Atom.newAtom(plArray[index].rgb[0]),
				Atom.newAtom(plArray[index].rgb[1]),
				Atom.newAtom(plArray[index].rgb[2]),
				Atom.newAtom(plArray[index].rgb[3])
		};
	}
	
	public void postSplit(String str, String splitter){
		String[] strArr = str.split(splitter);
		for (String s: strArr){
			consolePrint(s);
		}
	}
	public void consolePrint(String str){
		parent.consolePrint("PipelineArray: " + str);
	}
	public void sendPipelineMessage(Atom[] atomArr){
		parent.sendPipelineMessage(atomArr);
	}

	private static String optionListMessage = "optionlist";
}
