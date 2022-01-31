package main.java.da_utils.algorithmic_models.pipeline;

import java.awt.Container;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;

import javax.swing.DefaultListModel;
import javax.swing.GroupLayout;
import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JList;
import javax.swing.JScrollPane;
import javax.swing.ListSelectionModel;

import com.cycling74.max.Atom;

import main.java.da_utils.algorithmic_models.pipeline.pipeline_note_list.PipelineNoteList;
import main.java.da_utils.algorithmic_models.pipeline.plugins.Pluggable;
import main.java.da_utils.algorithmic_models.pipeline.plugins.PluggableList;
import main.java.da_utils.algorithmic_models.pipeline.plugins.drums.PlugInKik58Euclidean;
import main.java.da_utils.algorithmic_models.pipeline.plugins.drums.PlugInKikTwoOnFloor;
import main.java.da_utils.algorithmic_models.pipeline.plugins.keys.PlugInKeysPad;
import main.java.da_utils.algorithmic_models.pipeline.plugins.pipeline_plugin.PlayPlugArgument;
import main.java.da_utils.resource_objects.ChordForm;
import main.java.da_utils.resource_objects.RandomNumberSequence;
import main.java.da_utils.test_utils.TestData;

/*
 * Pipe is a simple version of the Pipeline without all the stuff used for the original project
 * for which it was designed which was all about a GUI to build your Pipelines (may have been 
 * DALooperLive9)
 * In this version there is a plugList which gets populated with Pluggables. There is no idiot
 * checking or sorting of PlugIns or whatever. You get it wrong, it wont work
 */

public class Pipe {
	
	public ArrayList<Pluggable> plugList = new ArrayList<Pluggable>();
//	public ArrayList<Pluggable> plugOptionList = new ArrayList<Pluggable>();
//	public RandomNumberSequence rnd = new RandomNumberSequence(16, 2);		// 16 length of preset random sequence. 2 = arbitrary seed value 
//	public static int inputSortCounter = 0;
	public String name = "";
	private ArrayList<PluggableList> undoList = new ArrayList<PluggableList>()
			{{add(new PluggableList(0));		// 0 - first undoList entry, undoCount = 0
				
			}};
	int undoIndex = 0;			// for clarity, this is the index of the current playing Pipe
	int undoCount = 1;
	
	public Pipe(String name){			
		this.name = name;
//		undoList.add(new PluggableList());
	}
	
	public Pipe deepCopy() {
		Pipe p = new Pipe(name);
		for (Pluggable plug: plugList){
			p.plugList.add(plug);
		}
		for (PluggableList pluglist: undoList){
			p.undoList.add(pluglist);
		}
		p.undoIndex = undoIndex;
		p.undoCount = undoCount;
		return p;
	}
	
	public void addPlugIn(Pluggable ppi){		
		plugList.add(ppi);
		loadNewUndoEntry();
	}
	
	public void removePlugIn(int index){	
		if (index >= 0 && index < plugList.size()){
			
			int keyLocation = plugList.indexOf(plugList.get(index));  
			plugList.remove(keyLocation);
			loadNewUndoEntry();
		}
	}
	
	public void removePlugIn(int[] indexArr){	
		ArrayList<Pluggable> list = new ArrayList<Pluggable>();
		boolean changed = false;
		for (int index: indexArr) {
			if (index >= 0 && index < plugList.size()) {
				list.add(plugList.get(index));				
				changed = true;
			} 
		}
		for (Pluggable p: list) {
			plugList.remove(p);
		}
		if (changed) loadNewUndoEntry();
	}
	
	public boolean removePlugIn(String plugInName) {
		for (int i = 0; i < plugList.size(); i++){
			String s = plugList.get(i).idName();
			if (s.equals(plugInName)){
				removePlugIn(i);
				return true;
			}
		}
		return false;
	}
	
	public PipelineNoteList makeNoteList(PlayPlugArgument ppa){
		//parent.consolePrint("Pipeline.makeNoteList(PlayPlugArgument ppa) called for: " + name);
//		rnd.reset();
		PipelineNoteList pnl = new PipelineNoteList();
		pnl.trackName = name;
		for (Pluggable ppi: plugList){
//			System.out.println("=================================================");
//			System.out.println(ppi.name() + " called: active = " + ppi.active());
			ppi.process(pnl, ppa);	
//			System.out.println(pnl.posAndLengthToString());
		}
		return pnl;
	}
	
//	public void newRandomSequence(){
//		rnd.generateNewSequence();
//	}
	
	public String toString(){
		String ret = "Pipeline:" + name + "----------------------------\nplugList:--\n";
		for (Pluggable plug: plugList){
			ret = ret + "   " + plug.name() + " active = " + plug.active() + "\n";
		}
		return ret;
	}
	
	public void clearPlugList(){
		plugList.clear();
	}
	
	public String indexedPlugListToString() {
		String str = "";
		for (int i = 0; i < plugList.size(); i++){
			str += i + ")\t" + plugList.get(i).name() + "\n";
		}
		return str;
	}
	
	
	public boolean clearPlugListWithUndo(){
		if (plugList.size() == 0){
			return false;
		} else {
			plugList.clear();
			loadNewUndoEntry();
			return true;
		}
		
	}
	
	public boolean undo(){		// remember this replaces the plugList, input material not saved at this level and 
								// may result in different outputss
		if (undoIndex > 0){
			undoIndex--;
			loadPlugListFromUndoIndex();
			return true;
		} else {
			return false;
		}
	}
	
	public boolean redo() {
		if (undoIndex >= undoList.size() - 1){
			return false;
		} else {
			undoIndex++;
			loadPlugListFromUndoIndex();
			return true;
		}
		
	}
	
	public Pluggable getPlugIn(String type, int index) {
		// gets the indexth instance of Pluggable of type in plugList
		int count = 0;
		for (Pluggable p: plugList){
			if (p.name().equals(type)){
				count ++;
				if (count > index) return p;
			}
		}
		return null;
	}
	
	public boolean removePlugIn(String plugInName, int index) {
		// removes the indexth instance of Pluggable of type in plugList, or returns false
		int count = 0;
		Pluggable removeItem = null;
		boolean found = false;
		for (Pluggable p: plugList){
			if (p.name().equals(plugInName)){
				count ++;
				if (count > index) {
					found = true;
					removeItem = p;
					break;
				}
			}
		}
		if (found){
			plugList.remove(removeItem);
			return true;
		}
		return false;
	}
	
	public void removePlugIn(Pluggable plug) {
		plugList.remove(plug);
		
	}
	
	public boolean movePlugIn(int from, int to) {
		if (from < plugList.size() && from >= 0) {
			if (to < 0) {
				to = 0;
			} else if (to >= plugList.size()) {
				to = plugList.size() - 1;
			}
			if (from > to) {
				for (int i = from - 1; i >= to; i--) {
					Collections.swap(plugList, i, i + 1);
				}
			} else {
				for (int i = from; i < to; i++) {
					Collections.swap(plugList, i, i + 1);
				}
			}
			return true;
		} else {
			return false;
		}
	}
	
	public int getGeneratorCount() {
		int count = 0;
		for (Pluggable plug: plugList) {
			if (plug.zone() == 0) count++;
		}
		return count;
	}
	



// privates  ------------------------------------------------------------------
	
	private void loadPlugListFromUndoIndex() {
		clearPlugList();
		PluggableList list = undoList.get(undoIndex);
		plugList.addAll(list.plugList);
		
	}

	private void loadNewUndoEntry() {
		PluggableList list = new PluggableList(undoCount);
		for (Pluggable p: plugList){
			list.add(p);
		}
		
		undoIndex++;
		undoList.add(undoIndex, list);
		undoCount++;	
	}

	

	
// ==== main ========================================================
	
	public static void main(String[] args) {
		Pipe pipe = new Pipe("pipetypipe");
		ChordForm cf = new ChordForm(TestData.liveClipForTestForm());
		PlayPlugArgument ppa = new PlayPlugArgument();
		ppa.cf = cf;
		pipe.plugList.add(new PlugInKeysPad());
		pipe.plugList.add(new PlugInKeysPad(105));
		PipelineNoteList pnl = pipe.makeNoteList(ppa);
		System.out.println(pnl.makeLiveClip().toString());
	}






	


	
	

}
