package main.java.da_utils.ableton_live.ableton_device_control_utils;
import java.util.ArrayList;

import com.cycling74.max.Atom;

import main.java.da_utils.ableton_live.ableton_device_control_utils.controller.Controller;
import main.java.da_utils.ableton_live.ableton_device_control_utils.controller.ControllerInfo;
import main.java.da_utils.udp.udp_utils.OSCMessMaker;


public class SendInfo extends Controller implements ControllerInfo{
	
//	private String messageName;		// name that the controller object will respond to
//	private int track;
	private int sendIndex;
	private double minimum = 0.0;		// default value
	private double maximum = 1.0;
	private boolean minMaxDefined = false;
	private static String messageString = "send";	// should be refered to by all things making controller messages
//	private double offValue;
	
	public SendInfo(){
		// just for testing. do not use
	}
	public SendInfo(String messageName, int track,int send, double offValue, int trackType){
		this.messageName = messageName;
		this.track = track;
		this.sendIndex = send;
		this.offValue = offValue;
	}
	public SendInfo(String messageName, int track, int send, double offValue, int trackType, double min, double max){
		this.messageName = messageName;
		this.track = track;
		this.sendIndex = send;
		minimum = min;
		maximum = max;
		minMaxDefined = true;
	}
	public Atom[] initAtomArray(){
		Atom[] atArr = new Atom[]{
//				Atom.newAtom(PlayletObject.sendString),
//				Atom.newAtom(messageName),
//				Atom.newAtom(sendIndex),
//				Atom.newAtom(PlayletObject.trackString),
//				Atom.newAtom(track),				
//				Atom.newAtom(minimum),
//				Atom.newAtom(maximum)
		};
		return atArr;
	}
	public ArrayList<Atom> initAtomList(){
		ArrayList<Atom> atList = new ArrayList<Atom>();
//		for (Atom atom: initAtomArray()){
//			atList.add(atom);
//		}
		return atList;
	}
	public void addToOscMessage(OSCMessMaker mess){		// remember, this is an initialization message......
		mess.addItem(DeviceParamInfo.sendString);
		mess.addItem(sendIndex);
//		mess.addItem(PlayletObject.trackString);		// not neccesary, also not yet implemented that another tracks sends can be controlled
//		mess.addItem(track);
		mess.addItem(messageName);
		if (minMaxDefined){
			mess.addItem(minimum);
			mess.addItem(maximum);
		}
		
	}
	public OSCMessMaker makeOSCMessage(){				// this caters for the format that attempts to not use recursive listbreakers in max for live
		// format is <controller> 'send' sendindex(i) trackType(s) trackindex(i) name(s) min(double) max(double)
		OSCMessMaker mess = new OSCMessMaker();
		mess.addItem(DeviceParamInfo.trackTypeArr[trackType]);
		if (trackType == DeviceParamInfo.ofMasterTrackType){
			mess.addItem(DeviceParamInfo.trackTypeArr[trackType]);
		} else {
			mess.addItem(track);
		}
		mess.addItem(DeviceParamInfo.sendString);				
		mess.addItem(sendIndex);
		
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
