package main.java.da_utils.ableton_live.clip_injector;

import java.util.ArrayList;
import java.util.Collection;

import com.cycling74.max.Atom;

import main.java.da_utils.ableton_live.ableton_device_control_utils.DeviceParamInfo;
import main.java.da_utils.ableton_live.ableton_live_clip.LiveClip;
import main.java.da_utils.ableton_live.ableton_live_clip.LiveMidiNote;
import main.java.da_utils.udp.udp_utils.OSCMessMaker;

/*
 * a version of the ClipInjectorObject which just makes a simple message 
 * as an ArrayList<ClipInjectorIstObject>. Ultmately this should be the
 * heart of a clip injection system that can be done via UDP or from a MaxObject
 */

public class ClipInjectorMessageMaker {

	
	public static ArrayList<ClipInjectorListObject> getInjectLiveClipMessage(LiveClip lc, int trackIndex){
		ArrayList<ClipInjectorListObject> list = new ArrayList<ClipInjectorListObject>();
		list.add(new ClipInjectorListObject(ClipInjectorObject.injectMessage));
		list.addAll(liveClipMessage(lc, trackIndex));
		return list;
	}
	public static ArrayList<ClipInjectorListObject> getInstrumentInitalizaionMessage(int trackIndex, int clipIndex, int trackType){
		ArrayList<ClipInjectorListObject> list = new ArrayList<ClipInjectorListObject>();
		list.add(new ClipInjectorListObject(ClipInjectorObject.initMessage));
		list.addAll(instrumentInitializationMessage(trackIndex, clipIndex, trackType));
		return list;
	}
	public static ArrayList<ClipInjectorListObject> getResetInitializationMessage(){
		// will reset the entire batch of ClipObjects in Live
		ArrayList<ClipInjectorListObject> list = new ArrayList<ClipInjectorListObject>();
    	list.add(new ClipInjectorListObject(DeviceParamInfo.initString));
    	list.add(new ClipInjectorListObject(DeviceParamInfo.resetString));
    	return list;
	}
	public static Atom[] makeAtomArray(ArrayList<ClipInjectorListObject> ciloList){
		Atom[] atArr = new Atom[ciloList.size()];
		for (int i = 0; i < ciloList.size(); i++){
			ClipInjectorListObject cilo = ciloList.get(i);
			if (cilo.isInt) atArr[i] = Atom.newAtom(cilo.i);
			if (cilo.isDouble) atArr[i] = Atom.newAtom(cilo.d);
			if (cilo.isString) atArr[i] = Atom.newAtom(cilo.str);
		}
		return atArr;
	}
	public static ArrayList<ClipInjectorListObject> getPlayMessage(){
		ArrayList<ClipInjectorListObject> list = new ArrayList<ClipInjectorListObject>();
		list.add(new ClipInjectorListObject(ClipInjectorObject.GLOBAL));
		list.add(new ClipInjectorListObject(ClipInjectorObject.PLAY));
		return list;
	}
	public static ArrayList<ClipInjectorListObject> getStopMessage(){
		ArrayList<ClipInjectorListObject> list = new ArrayList<ClipInjectorListObject>();
		list.add(new ClipInjectorListObject(ClipInjectorObject.GLOBAL));
		list.add(new ClipInjectorListObject(ClipInjectorObject.STOP));
		return list;
	}
	public static ArrayList<ClipInjectorListObject> getTempoMessage(double d){
		ArrayList<ClipInjectorListObject> list = new ArrayList<ClipInjectorListObject>();
		list.add(new ClipInjectorListObject(ClipInjectorObject.GLOBAL));
		list.add(new ClipInjectorListObject(ClipInjectorObject.TEMPO));
		list.add(new ClipInjectorListObject(d));
		return list;
	}
	
	
// privates

	private static ArrayList<ClipInjectorListObject> instrumentInitializationMessage(int trackIndex, int clipIndex, int trackType) {
		ArrayList<ClipInjectorListObject> list = new ArrayList<ClipInjectorListObject>();
		list.add(new ClipInjectorListObject(ClipInjectorObject.newClipObjectString));
		list.add(new ClipInjectorListObject(ClipInjectorObject.trackTypeArr[trackType]));
		if (trackType == ClipInjectorObject.ofMasterTrackType){
			list.add(new ClipInjectorListObject(ClipInjectorObject.masterTrackString));
		} else {
			list.add(new ClipInjectorListObject(trackIndex));			
		}
		list.add(new ClipInjectorListObject(clipIndex));
		return list;
	}
	
	private static ArrayList<ClipInjectorListObject> liveClipMessage(LiveClip lc, int trackIndex) {
		ArrayList<ClipInjectorListObject> list = new ArrayList<ClipInjectorListObject>();
		
		list.add(new ClipInjectorListObject(DeviceParamInfo.trackTypeArr[DeviceParamInfo.ofTrackType]));		// assuming liveclip is going into a track
		list.add(new ClipInjectorListObject(trackIndex));
		list.add(new ClipInjectorListObject(ClipInjectorObject.notesMessage));
		list.addAll(lcNotesListt(lc));
		list.add(new ClipInjectorListObject(lc.noteList.size()));		
//		// param message
		list.add(new ClipInjectorListObject(ClipInjectorObject.paramMessage));
		list.addAll(lcParamList(lc));
		return list;
	}

	private static Collection<? extends ClipInjectorListObject> lcParamList(LiveClip lc) {
		ArrayList<ClipInjectorListObject> list = new ArrayList<ClipInjectorListObject>();
		list.add(new ClipInjectorListObject(lc.length));
		list.add(new ClipInjectorListObject(lc.loopStart));
		list.add(new ClipInjectorListObject(lc.loopEnd));
		list.add(new ClipInjectorListObject(lc.startMarker));
		list.add(new ClipInjectorListObject(lc.endMarker));
		list.add(new ClipInjectorListObject(lc.signatureNumerator));
		list.add(new ClipInjectorListObject(lc.signatureDenominator));
		list.add(new ClipInjectorListObject(lc.offset));
		list.add(new ClipInjectorListObject(lc.clip));
		list.add(new ClipInjectorListObject(lc.track));
		list.add(new ClipInjectorListObject(lc.name));
		return list;
	}

	private static Collection<? extends ClipInjectorListObject> lcNotesListt(LiveClip lc) {
		ArrayList<ClipInjectorListObject> list = new ArrayList<ClipInjectorListObject>();
		for (LiveMidiNote lmn: lc.noteList){
			list.add(new ClipInjectorListObject(lmn.note));
			list.add(new ClipInjectorListObject(lmn.position));
			list.add(new ClipInjectorListObject(lmn.length));
			list.add(new ClipInjectorListObject(lmn.velocity));
			list.add(new ClipInjectorListObject(lmn.mute));
		}
		return list;
	}

	private static void addLCNotesToLst(LiveClip lc, ArrayList<ClipInjectorListObject> list) {
		
		
	}

	
	
}
