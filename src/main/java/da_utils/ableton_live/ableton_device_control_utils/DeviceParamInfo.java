package main.java.da_utils.ableton_live.ableton_device_control_utils;
import java.util.ArrayList;

import com.cycling74.max.Atom;

import main.java.da_utils.ableton_live.ableton_device_control_utils.controller.Controller;
import main.java.da_utils.ableton_live.ableton_device_control_utils.controller.ControllerInfo;
import main.java.da_utils.ableton_live.ableton_device_control_utils.device_parameter_utils.DefaultDeviceParameter;
import main.java.da_utils.udp.udp_utils.OSCMessMaker;


public class DeviceParamInfo extends Controller implements ControllerInfo{

//	private String messageName;		// name that the controller object will respond to. implemented in Controller superclass
//	private int track;
	private boolean trackDefined = false;
	private int device;
	private boolean deviceDefined = false;
	private int parameter;
	private double minimum;
	private double maximum;
	private boolean minMaxDefined = false;
//	private static String messageString = "deviceparam";	// should be refered to by all things making controller messages
//	private double offValue;

	
	
//	public DeviceParamInfo(String messageName, int track, int device, int param, double min, double max, double offValue){
//		this.messageName = messageName;
//		this.track = track;
//		this.device = device;
//		parameter = param;
//		minimum = min;
//		maximum = max;
//		this.offValue = offValue;
//	}
//	public DeviceParamInfo(String messageName, int param, double offValue){
//		this.messageName = messageName;
//		this.track = track;
//		this.device = device;
//		parameter = param;
//		minimum = min;
//		maximum = max;
//		this.offValue = offValue;
//	}
	public DeviceParamInfo(DefaultDeviceParameter ddp, int track, int trackType){		// will need other variations of instantiators to cater for extended options like other tracks, other device indices and or other min max requirements
		this.messageName = ddp.name;
		this.track = track;
		trackDefined = true;
//		this.device = device;
		parameter = ddp.index;
		minimum = ddp.min;
		maximum = ddp.max;
		this.offValue = ddp.offValue;
		this.trackType = trackType;
	}
	public Atom[] initAtomArray(){
		Atom[] atArr = new Atom[]{
//				Atom.newAtom(messageString),
//				Atom.newAtom(controllerIndex),
//				Atom.newAtom(track),
//				Atom.newAtom(device),
//				Atom.newAtom(parameter),
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
		mess.addItem(deviceString);
		if (deviceDefined){
			mess.addItem(device);
		} else {
			mess.addItem(defaultDeviceIndex);
		}		
		mess.addItem(parameter);
		if (trackDefined){
			mess.addItem(trackString);
			mess.addItem(track);
		}
		
		mess.addItem(messageName);
		if (minMaxDefined){
			mess.addItem(minimum);
			mess.addItem(maximum);
		}
		
	}
	public OSCMessMaker makeOSCMessage(){				// initialization message. this caters for the format that attempts to not use recursive listbreakers in max for live
		// format is <controller> 'device' deviceindex(i) parameterindex(i) trackType(s) trackindex(i) name(s) min(double) max(double)
		OSCMessMaker mess = new OSCMessMaker();
		mess.addItem(trackTypeArr[trackType]);
		if (trackType == ofMasterTrackType){
			mess.addItem(trackTypeArr[trackType]);
		} else {
			mess.addItem(track);
		}
		mess.addItem(deviceString);
		if (deviceDefined){
			mess.addItem(device);
		} else {
			mess.addItem(defaultDeviceIndex);
		}		
		mess.addItem(parameter);
		
		
		mess.addItem(messageName);
		mess.addItem(minimum);
		mess.addItem(maximum);
		for (int i = mess.length(); i < this.default_message_length; i++){
			mess.addItem("x");
		}
		
		return mess;
	}
//	public OSCMessMaker makeOffOSCMessage(){
//		OSCMessMaker mess = new OSCMessMaker();
//		mess.addItem(PlayletObject.controllerMessage);
//		mess.addItem(cc.controllerIndex);
//		mess.addItem(0);			// magic number 0 - clip is OFF
//		mess.addItem(offValue);
//		
//		mess.addItem(PlayletObject.trackTypeArr[trackType]);
//	/	mess.addItem(track);
//		mess.addItem(messageName);
//		return mess;
//	}
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
	
	// values for variable 'type'
	public static final int CHORD_PROGRESSION = 0;
	public static final int RHYTHM_BUFFER = 1;
	public static final int CONTOUR = 2;
	
	public static final int DRUM_GENERATOR = 3;
	public static final int KIK_GENERATOR = 4;
	public static final int SNR_GENERATOR = 5;
	public static final int HAT_GENERATOR = 6;
	public static final int TOMS_GENERATOR = 7;
	public static final int CYMBAL_GENERATOR = 8;
	public static final int BASS_GENERATOR = 9;
	public static final int KEYS_GENERATOR = 10;
	public static final int LEAD_GENERATOR = 11;
	public static final int MULTI_GENERATOR = 12;
	
	public static final int DRUM_PROCESSOR = 13;
	public static final int KIK_PROCESSOR = 14;
	public static final int SNR_PROCESSOR = 15;
	public static final int HAT_PROCESSOR = 16;
	public static final int TOMS_PROCESSOR = 17;
	public static final int CYMBAL_PROCESSOR = 18;
	public static final int BASS_PROCESSOR = 19;
	public static final int KEYS_PROCESSOR = 20;
	public static final int LEAD_PROCESSOR = 21;
	public static final int MULTI_PROCESSOR = 22;
	
	public static final int DRUM_INSTRUMENT = 23;
	public static final int KIK_INSTRUMENT = 24;
	public static final int SNR_INSTRUMENT = 25;
	public static final int HAT_INSTRUMENT = 26;
	public static final int TOMS_INSTRUMENT = 27;
	public static final int CYMBAL_INSTRUMENT = 28;
	public static final int BASS_INSTRUMENT = 29;
	public static final int KEYS_INSTRUMENT = 30;
	public static final int LEAD_INSTRUMENT = 31;
	public static final int MULTI_INSTRUMENT = 32;
	
	public static final int RETURN_GENERATOR = 33;
	public static final int RETURN_PROCESSOR = 34;
	public static final int RETURN_INSTRUMENT = 35;
	
	public static final int MASTER_GENERATOR = 36;
	public static final int MASTER_PROCESSOR = 37;
	public static final int MASTER_INSTRUMENT = 38;
	
	
	public static final String trackString = "track";
	public static final String returnTrackString = "returntrack";
	public static final String masterTrackString = "master";
	public static final int ofTrackType = 0;
	public static final int ofReturnTrackType = 1;
	public static final int ofMasterTrackType = 2;
	public static final String[] trackTypeArr = new String[]{trackString, returnTrackString, masterTrackString};
	public static final String newClipObjectString = "newClipObject";
	public static final String initString = "init";
	public static final String controllerinitString = "controllerinit";
	public static final String controllerString = "controller";
	public static final String volString = "vol";
	public static final String panString = "pan";
	public static final String deviceString = "device";
	public static final String sendString = "send";
	public static final String resetString = "reset";
	public static final String globalString = "global";
	public static final String playString = "play";
	public static final String stopString = "stop";
	public static final String toggleTransportString = "toggletransport";
	public static final String adjustString = "adjust";
	
	public static final DefaultDeviceParameter default_HP = new DefaultDeviceParameter("HP", 1, 0.5);
	public static final DefaultDeviceParameter default_FB = new DefaultDeviceParameter("FB", 2, 0.0);
	public static final DefaultDeviceParameter default_LP = new DefaultDeviceParameter("LP", 3, 1.0);
	public static final DefaultDeviceParameter default_rez = new DefaultDeviceParameter("rez", 4, 0.0); // these four are based on the setup for the dubDelay
	public static final DefaultDeviceParameter default_TREM = new DefaultDeviceParameter("trem", 5, 0.0);
	
//	public static final int delaySendControllerIndex = 0;
//	public static final int delaySend = 0;
	public static final int defaultDelaySendIndex = 1;
//	public static final int bassFreqControllerIndex = 1;  
//	public static final int bassProcessorDeviceIndex = 1; 
//	public static final int bassLPFreqParameterIndex = 1;
//	public static final int bassPanControllerIndex = 2;
	public static final double defaultSendOffValue = 0.0;
	public static final double defaultDelayOffValue = 0.0;
	public static final double defaultPanOffValue = 0.5;
	public static final double defaultVolOffValue = 0.75;
	public static final double defaultWahOffValue = 1.0;
	public static final double defaultPitchBendOffValue = 0.5;
	
	public static final int defaultDeviceIndex = 1;		// gives room for the pitchbend device at index 0
	public static final int defaultHPFreqControllerIndex = 1;
	public static final int defaultHPLPResonanceControllerIndex = 2;
	public static final int defaultLPFreqControllerIndex = 3;

	// inject message strings
		public static final String injectMessage = "inject";
		public static final String notesMessage = "notes";
		public static final String paramMessage = "param";
		public static final String controllerMessage = "controller";
		public static final String pitchbendMessage = "pitchbend";
		public static final String clipObjInitMessage = "clipObjInit";
}
