package main.java.da_utils.algorithmic_models.pipeline;
import java.awt.Container;
//import java.awt.event.ActionEvent;
//import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;

import javax.swing.DefaultListModel;
import javax.swing.GroupLayout;
//import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JList;
import javax.swing.JScrollPane;
import javax.swing.ListSelectionModel;

import com.cycling74.max.Atom;

import main.java.da_utils.algorithmic_models.pipeline.pipeline_note_list.PipelineNoteList;
import main.java.da_utils.algorithmic_models.pipeline.pipeline_parent.PipelineParent;
import main.java.da_utils.algorithmic_models.pipeline.plugins.Pluggable;
import main.java.da_utils.algorithmic_models.pipeline.plugins.pipeline_plugin.PipelinePlugInSortWrapper;
import main.java.da_utils.algorithmic_models.pipeline.plugins.pipeline_plugin.PlayPlugArgument;
import main.java.da_utils.resource_objects.RandomNumberSequence;



/*
 * manages the generation of a PipeLineNoteList
 */
public class Pipeline {
	
	public ArrayList<Pluggable> plugList = new ArrayList<Pluggable>();
	public ArrayList<Pluggable> plugOptionList = new ArrayList<Pluggable>();
	public RandomNumberSequence rnd = new RandomNumberSequence(16, 2);		// 16 length of preset random sequence. 2 = arbitrary seed value 
	public static int inputSortCounter = 0;
	public String name = "";
	public double[] rgb;
	public PipelineParent parent;

	public int clipObjectIndex;				// is the same as panelIndex, should the question arise
	
//	public TwoBarRhythmBuffer rb;		// for testing, set these as objects and they will be used
//	public TwoBarRhythmBuffer ib;		// for testing, interlock buffer
//	public ChordForm form;			// if they are null, Pipeline will look for DAPlay related items
//	public ChordProgrammer2 cp;		// ditto
	
	private DefaultListModel optionModel;
	private DefaultListModel plugModel;
	


	
	private int test_dynamic_index = 2;		// for testing where there is no DAPlay
	
	public Pipeline(){
		// for use in Playlet where Pipeline is used only for random sequence, to avoid having to 
		// redo every PipelinePlugIn with a new process(....) method. Which will have to happen at some point
	}
	
//	public Pipeline(int clipObjectIndex, PipelineMaxTest p){
//		parent1 = p;
//		parentIndex = 1;
//		this.clipObjectIndex = clipObjectIndex;
//	}
//	public Pipeline(int clipObjectIndex, PipelineConsoleTest p){
//		parent2 = p;
//		parentIndex = 2;
//		this.clipObjectIndex = clipObjectIndex;
//	}
	public Pipeline(int clipObjectIndex, PipelineParent p){
		parent = p;
		this.clipObjectIndex = clipObjectIndex;
	}
	
	public Pipeline(String n, double[] rgb, int clipObjectIndex){			// rgb = red green blue alpha. must be 4 doubles
		name = n;
		this.rgb = rgb;
		this.clipObjectIndex = clipObjectIndex;
	}
	
	public Pipeline(String name){			
		this.name = name;
	}
	
	public void addPlugInOption(Pluggable ppi){
		plugOptionList.add(ppi);
	}
	
	public void addPlugIn(int index){
		if (index >= 0 && index < plugOptionList.size()){
			addToPlugList(index);
			Collections.sort(plugList, plugInSort);
		}
	}
	
	public void removePlugIn(int index){	
		if (index >= 0 && index < plugList.size()){
			post("pipeline removes index " + index);
			int keyLocation = plugList.indexOf(plugList.get(index));  
			plugList.remove(keyLocation);
		}
	}
//	public void updatePanel(){
//		
//	}
	public PipelineNoteList makeNoteList(){
		parent.consolePrint("Pipeline.makeNoteList() called for: " + name);
		rnd.reset();
		PlayPlugArgument ppa = makePlayPlugArgument();
		PipelineNoteList pnl = new PipelineNoteList(clipObjectIndex);
		pnl.trackName = name;
		for (Pluggable ppi: plugList){
//			parent.post(ppi.name() + " called: active = " + ppi.active());
			ppi.process(pnl, ppa);			
		}
		return pnl;
	}
	
	public PipelineNoteList makeNoteList(PlayPlugArgument ppa){
		//parent.consolePrint("Pipeline.makeNoteList(PlayPlugArgument ppa) called for: " + name);
		rnd.reset();
		PipelineNoteList pnl = new PipelineNoteList(clipObjectIndex);
		pnl.trackName = name;
		for (Pluggable ppi: plugList){
//			System.out.println("=================================================");
//			System.out.println(ppi.name() + " called: active = " + ppi.active());
			ppi.process(pnl, ppa);	
//			System.out.println(pnl.posAndLengthToString());
		}
		return pnl;
	}
	
	public void newRandomSequence(){
		rnd.generateNewSequence();
	}
	
	public String toString(){
		String ret = "Pipeline:----------------------------\nplugOptionList:--\n";
		for (Pluggable plug: plugOptionList){
			ret = ret + "   " + plug.name() + "\n";
		}
		ret = ret + "plugList:--\n";
		for (Pluggable plug: plugList){
			ret = ret + "   " + plug.name() + " active = " + plug.active() + "\n";
		}
		return ret;
	}

	public Atom[] plugOptionListNameAtomArray(Atom[] prepend){
		return atomArrayFromList(plugOptionList, prepend);
	}
	
	public Atom[] plugListNameAtomArray(Atom[] prepend){		
		Atom[] atArr = new Atom[plugList.size() * 2 + prepend.length];
		for (int i = 0; i < prepend.length; i++){
			atArr[i] = prepend[i];
		}
		for (int i = 0; i < plugList.size(); i++){
			atArr[i * 2 + prepend.length] = Atom.newAtom(plugList.get(i).name());
			atArr[i * 2 + prepend.length + 1] = Atom.newAtom(plugList.get(i).active());
		}
		return atArr;
	}
	
	public void setPlugActive(int index, int onoff){
		if (index >= 0 && index < plugList.size()){
			plugList.get(index).setActive(onoff);
		}
	}
	
	public static Comparator<Pluggable> plugInSort = new Comparator<Pluggable>(){
		public int compare(Pluggable plug1, Pluggable plug2){
			int plug1sort = plug1.zone() * 1000;							// 1000 for each zone. arbitrarily large
			int plug2sort = plug2.zone() * 1000;
			if (plug1.canDouble()) {
				plug1sort += plug1.inputSort();
			} else {
				plug1sort += plug1.sortIndex();
			}
			if (plug2.canDouble()) {
				plug2sort += plug2.inputSort();
			} else {
				plug2sort += plug2.sortIndex();
			}
			if (plug1sort < plug2sort) return -1;
			if (plug1sort > plug2sort) return 1;
			return 0;
			
		}
	};
//	public TwoBarRhythmBuffer getRhythmBuffer(){
//		if (rb == null){
//			return parent.getRhythmBuffer();
//		} else {
//			return rb;
//		}
//	}
//	public TwoBarRhythmBuffer getInterlockBuffer(){
//		if (ib == null){
//			return parent.getInterlockBuffer();
//		} else {
//			return ib;
//		}
//	}
//	public ChordForm getChordForm(){
//		if (form == null){
//			return parent.getChordForm();
//		} else {
//			return form;
//		}
//	}
//	public ChordProgrammer2 getChordProgrammer(){
//		if (form == null){
//			return parent.getChordProgrammer();
//		} else {
//			return cp;
//		}
//	}
//	public double getFormLength(){
//		if (form == null){		
//			parent.postit("Pipeline.getFormLength() thinks form == null. ");
//			parent.postit("using DAPlay.PipelineArray.ChordProgrammer.formLength: " + parent.parent.cp.formLength);
//			return parent.getFormLength();
//		} else {		// will go here if debugging outside of DAPlay......
//			parent.post("Pipeline.getFormLength() using form.length = " + form.length());
//			return form.length(); 
//		}
//	}
//	public int getDynamicIndex(){
//		if (parent == null){
//			return test_dynamic_index;
//		} else {
//			return parent.getDynamicIndex();
//		}
//	}
	public String[] optionNameArray(){
		String[] strArr = new String[plugOptionList.size()];
		for (int i = 0; i < plugOptionList.size(); i++){
			strArr[i] = plugOptionList.get(i).name();
		}
		return strArr;
	}
	
	public String[] plugNameArray(){
		String[] strArr = new String[plugList.size()];
		for (int i = 0; i < plugList.size(); i++){
			strArr[i] = plugList.get(i).name();
		}
		return strArr;
	}
	
	public String plugNameOneLineToString(){
		String ret = "plugList: ";
		for (Pluggable plu: plugList){
			ret += plu.originalName() + ",";
		}
		return ret;
	}
	
	public void openPanel(){
		makeDialog();
	}
	
	public void updatePipeList(){
		outlet(plugListNameAtomArray(new Atom[]{
				Atom.newAtom(clipObjectIndex),
				Atom.newAtom("pipelist")
		}));
	}
	
	public void turnOffAllPlugIns(){
		for (Pluggable ppi: plugList){
			ppi.setActive(0);
		}
	}
	
// privates ----------------------------------------------------------------------------
	private PlayPlugArgument makePlayPlugArgument(){
		PlayPlugArgument ppa = new PlayPlugArgument();
		ppa.cf = parent.ro().cp.form;
		ppa.rb = parent.ro().rbgc.getRhythmBuffer();
		ppa.ib = parent.ro().rbgc.getInterlockBuffer();
		ppa.cd = parent.ro().cd;
		ppa.rnd = rnd;
		ppa.dynamic = parent.ro().dynamic;
		ppa.clipObjectIndex = clipObjectIndex;
		return ppa;
	}
	
	private void outlet(Atom[] atomArr){
		parent.sendPipelineMessage(atomArr);
	}
	
	private void post(String str){
		parent.consolePrint(str);
	}
	
	private void makeDialog(){
		JDialog jd = new JDialog(new JFrame(), "howdy doody", false);
		makeContentPane(jd);
		jd.pack();
        jd.setSize(500, 400);
		jd.setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);
		jd.setVisible(true);
	}
	
	private void makeContentPane(JDialog jd){
		JList optionList = getOptionList();
		JList plugInList = getPlugInList();
		
		JScrollPane optionScrollpane = new JScrollPane(optionList);
        JScrollPane plugScrollpane = new JScrollPane(plugInList);
        
		Container pane = jd.getContentPane();
		GroupLayout gl = new GroupLayout(pane);
        pane.setLayout(gl);

        gl.setAutoCreateContainerGaps(true);
        
        gl.setHorizontalGroup(gl.createSequentialGroup()
                .addComponent(optionScrollpane)
                .addComponent(plugScrollpane)
        );

        gl.setVerticalGroup(gl.createParallelGroup()
                .addComponent(optionScrollpane)
                .addComponent(plugScrollpane)
        );   
	}
	
	private JList getPlugInList(){
		plugModel = new DefaultListModel();
		loadPlugModel();
		JList plugList = new JList(plugModel);
		plugList.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
	    plugList.addMouseListener(new MouseAdapter(){
	        public void mouseClicked(MouseEvent evt){
	        	JList list = (JList)evt.getSource();
	        	if (evt.getClickCount() == 2){
	        		int index = list.locationToIndex(evt.getPoint());
	        		removePlugIn(index);
	        		updatePipeList();
	        		loadPlugModel();
	        		postPlugList();
	        	} 
	       	}
	    });
		return plugList;
	}
	
	private JList getOptionList(){
		optionModel = new DefaultListModel();
		loadOptionModel();
		JList optionList = new JList(optionModel);
        optionList.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        optionList.addMouseListener(new MouseAdapter(){
        	public void mouseClicked(MouseEvent evt){
        		JList list = (JList)evt.getSource();
        		if (evt.getClickCount() == 2){
        			int index = list.locationToIndex(evt.getPoint());
	        		addPlugIn(index);
	        		updatePipeList();
        			loadPlugModel();
        			postPlugList();
        		} 
        	}
        });
        return optionList;
    }
	
	private void populateLists(){
		loadPlugModel();
		loadOptionModel();
	}
	
	private void loadPlugModel(){
		plugModel.clear();
		for (Pluggable ppi: plugList){
			post("loaded " + ppi.name() + " to plugModel");
			plugModel.addElement(ppi.name());
		}
	}
	
	private void loadOptionModel(){
		optionModel.clear();
		for (Pluggable ppi: plugOptionList){
			optionModel.addElement(ppi.name());
		}
	}
	
	private Atom[] atomArrayFromList(ArrayList<Pluggable> ppList, Atom[] prepend){
		Atom[] atArr = new Atom[ppList.size() + prepend.length];
		for (int i = 0; i < prepend.length; i++){
			atArr[i] = prepend[i];
		}
		for (int i = 0; i < ppList.size(); i++){
			atArr[i + prepend.length] = Atom.newAtom(ppList.get(i).name());
		}
		return atArr;
	}
	
	private void addToPlugList(int index){
		Pluggable ppi = plugOptionList.get(index);
		if (ppi.canDouble()){
			PipelinePlugInSortWrapper ppisw = new PipelinePlugInSortWrapper(ppi, inputSortCounter);
			ppisw.setActive(1);
			plugList.add(ppisw);
			inputSortCounter++;
		} else {
			if (noDuplicateInPlugList(ppi)){
				plugList.add(ppi);	
				ppi.setActive(1);
			}
		}
	}
	
	private boolean noDuplicateInPlugList(Pluggable ppi){
		// this test assumes that items in the plugList are referencesw to items in the plugOptionsList.
		// if development gets to the point where this is not the case any longer, a new plan needs to be made
		for (Pluggable p: plugList){
			if (p == ppi){
				return false;
			}
		}
		return true;
	}
	
	private void postPlugList(){
		post("plugList...........");
		for (String str: plugNameArray()){
			post(str);
		}
	}
	
//	private void parentPrint(String str){
//		if (parentIndex == 0) parent.post(str);
//		if (parentIndex == 1) parent1.post(str);
//		if (parentIndex == 2) parent2.println(str);
//	}

	public double zone2Count() {
		int count = 0;
		for (Pluggable p: plugList){
			if (p.zone == 2) count ++;
		}
		return count;
	}

}
