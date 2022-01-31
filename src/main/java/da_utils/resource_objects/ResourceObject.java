package main.java.da_utils.resource_objects;
import com.cycling74.max.Atom;

import LegacyStuff.ChordProgrammer2;
import LegacyStuff.ChordProgrammer2Parent;
import LegacyStuff.RhythmBufferGUIController;
import main.java.da_utils.chord_scale_dictionary.ChordScaleDictionary;


public class ResourceObject implements ChordProgrammer2Parent{

	public RhythmBufferGUIController rbgc = new RhythmBufferGUIController();
	public ChordProgrammer2 cp; 
	public ContourData cd;
	public int dynamic = 0;		// 0-3 soft to loud default is soft
	public ResourceObjectParent parent;
	public ChordScaleDictionary csd = new ChordScaleDictionary();
	
	public ResourceObject(ResourceObjectParent rop){
		parent = rop;
		// ---------------------------------------------------contourData-------------
		cd = new ContourData();
		//-------------------------------------------------- rhythmBuffer ----
		rbBigSetting();			// rhythmBuffer
		controlButtonInitialize();
		//-------------------------------------------------- chordProgrammer -----
		cp = new ChordProgrammer2(this);
		cp.clearAllButtons();	// chordProgrammer
		cp.setFormLengthFromIndex(1);
	}
	public ResourceObject(){
		// for use with Playlet
		
	}
	
// rhythmBuffer stuff ------------------------------------------------------------
	public void bar(int i){
		rbgc.setBarDisplay(i);
	}
	public void buffer(int i){
		rbgc.setBufferIndex(i);
	}
	public void duplicate(int i){
		rbgc.duplicate(i);
	}
	public void rbBigSetting(){
		parent.sendRhythmBufferMessage(rbgc.bigSettingArray());
	}
	public void button(int button, int shift){
		parent.sendRhythmBufferMessage(rbgc.button(button, shift));
	}
	public void controlButtonInitialize(){
		parent.sendRhythmBufferMessage(rbgc.controlButtonInitAtomArray());
	}
	public void postRhythmBuffer(){
		parent.postSplit(rbgc.toString(), "\n");
	}
	public void generateInterlockBuffer(int i){
		rbgc.generateInterlockBuffer(i);
		rbBigSetting();
	}
	public void postContents(){
		parent.postSplit(cp.toString(), "\n");
	}
	
// chordProgrammer Parent methods ----------------------------------------------------
	
	public void sendChordProgrammerMessage(Atom[] atArr){
		parent.sendChordProgrammerMessage(atArr);
	}
	public void consolePrint(String str){
		parent.consolePrint(str);
	}
// toString stufff-------------------------------------------------------------------
//	public String toString(){
//		String ret = "ResourceObject to String ============================================\n";
//		ret = ret + cp.toString();
//		return ret;
//	}
}
