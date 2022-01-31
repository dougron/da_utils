package main.java.da_utils.ableton_live.ableton_device_control_utils;
import java.util.ArrayList;

import com.cycling74.max.Atom;
/*
 * needs enough info to generate an init message to send to the clipObjects
 */

import main.java.da_utils.ableton_live.ableton_device_control_utils.controller.Controller;
import main.java.da_utils.ableton_live.ableton_device_control_utils.controller.ControllerInfo;
import main.java.da_utils.udp.udp_utils.OSCMessMaker;

public class PanInfo extends Controller implements ControllerInfo{
	
//	private String messageName;		// name that the controller object will respond to
//	private int track;
	private double minimum = -1.0;		// default value
	private double maximum = 1.0;
//	private static String messageString = "pan";	// should be refered to by all things making controller messages
//	private double offValue;
	
	public PanInfo(String name, int track, double offValue, int trackType){	// use this if you want to not use the default name 'pan' for this control
		this.messageName = name;
		this.track = track;
		this.offValue = offValue;
		this.trackType = trackType;
	}
	public PanInfo(int track, double offValue, int trackType){		// uses default name 'pan'
		this.track = track;
		this.offValue = offValue;
		this.trackType = trackType;
		messageName = DeviceParamInfo.panString;
	}

	public Atom[] initAtomArray(){			// may be redundant
		Atom[] atArr = new Atom[]{
//				Atom.newAtom(messageString),
//				Atom.newAtom(controllerIndex),
//				Atom.newAtom(track),
//				Atom.newAtom(minimum),
//				Atom.newAtom(maximum)
		};
		return atArr;
	}
	public ArrayList<Atom> initAtomList(){
		ArrayList<Atom> atList = new ArrayList<Atom>();
		for (Atom atom: initAtomArray()){
			atList.add(atom);
		}
		return atList;
	}
	public void addToOscMessage(OSCMessMaker mess){		// remember, this is an initialization message......
		mess.addItem(DeviceParamInfo.panString);
		if (messageName != null){
			mess.addItem(messageName);
		}
	}
	public OSCMessMaker makeOSCMessage(){				// this caters for the format that attempts to not use recursive listbreakers in max for live
		// format is <controller> 'pan' trackType(s) trackindex(i) name(s) min(double) max(double)
		OSCMessMaker mess = new OSCMessMaker();
		mess.addItem(DeviceParamInfo.trackTypeArr[trackType]);
		if (trackType == DeviceParamInfo.ofMasterTrackType){
			mess.addItem(DeviceParamInfo.trackTypeArr[trackType]);
		} else {
			mess.addItem(track);
		}
		mess.addItem(DeviceParamInfo.panString);
		
		mess.addItem(messageName);
		mess.addItem(minimum);
		mess.addItem(maximum);
		for (int i = mess.length(); i < this.default_message_length; i++){
			mess.addItem("x");
		}
		
		return mess;
	}

	public void addOffMessageToOSC(OSCMessMaker mess, int clipObjectIndex){ // and this is a control message......
//		mess.addItem(PlayletObject.controllerMessage);
//		mess.addItem(controllerIndex);
//		mess.addItem(0);			// this is the on/off parameter for controllers, and hence 0 cos we are switching it off
//		mess.addItem(offValue);
//		mess.addItem(clipObjectIndex);
	}
	public void setTrackIndex(int i){
		track = i;
	}
	public String name(){
		return messageName;
	}


}
