package main.java.da_utils.ableton_live.ableton_device_control_utils.controller;

import main.java.da_utils.ableton_live.ableton_device_control_utils.DeviceParamInfo;
import main.java.da_utils.udp.udp_utils.OSCMessMaker;

public class Controller {

	public static int default_message_length = 10;		// all controller init messages are made this length to avoid recursive list breaking
	public int trackType = 0;
	
	public double offValue;
	public int track;
	public String messageName;
	
	public OSCMessMaker makeOffOSCMessage(){
		OSCMessMaker mess = new OSCMessMaker();
		mess.addItem(DeviceParamInfo.injectMessage);
		mess.addItem(DeviceParamInfo.trackTypeArr[trackType]);
		if (trackType == DeviceParamInfo.ofMasterTrackType){
			mess.addItem(DeviceParamInfo.trackTypeArr[trackType]);
		} else {
			mess.addItem(track);
		}
		mess.addItem(DeviceParamInfo.controllerMessage);
		mess.addItem(messageName);
//		mess.addItem(cc.controllerIndex);
		mess.addItem(0);			// magic number 0 - clip is OFF
		mess.addItem(offValue);
		
		
		return mess;
	}
}
