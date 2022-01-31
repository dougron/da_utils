package main.java.da_utils.ableton_live.ableton_live_clip.clip_info_object;
import java.util.ArrayList;

import com.cycling74.max.Atom;

import LegacyStuff.ControllerInfo_DAPlayAlong;
import main.java.da_utils.ableton_live.ableton_device_control_utils.controller.ControllerInfo;




public class ClipInfoObject {
	
	public int objectIndex;
	public int trackIndex;
	public int clipIndex;
	public String name;
	public ControllerInfo[] ccioArr;
	public static String initMessage = "init";

	
	public ClipInfoObject(int objectIndex, int trackIndex, int clipIndex, String name, ControllerInfo[] ccioArr){
		this.objectIndex = objectIndex;
		this.trackIndex = trackIndex;
		this.clipIndex = clipIndex;
		this.name = name;
		this.ccioArr = ccioArr;
	}

	public Atom[] clipInitAtomArray(){
		ArrayList<Atom> atList = new ArrayList<Atom>();
		atList.add(Atom.newAtom(initMessage));
		atList.add(Atom.newAtom(objectIndex));
		atList.add(Atom.newAtom(trackIndex));
		atList.add(Atom.newAtom(clipIndex));
		atList.add(Atom.newAtom(name));
		for (ControllerInfo coninf: ccioArr){
			atList.addAll(coninf.initAtomList());
		}
		return (Atom[])atList.toArray();		
	}
	public String toString(){
		return "ClipInfoObject: " + name + ", " + trackIndex + ", " + clipIndex;
	}
}
