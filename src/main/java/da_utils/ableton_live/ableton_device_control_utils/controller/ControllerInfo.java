package main.java.da_utils.ableton_live.ableton_device_control_utils.controller;
import java.util.ArrayList;

import com.cycling74.max.Atom;

import main.java.da_utils.udp.udp_utils.OSCMessMaker;


public interface ControllerInfo {

	public Atom[] initAtomArray();
	public ArrayList<Atom> initAtomList();
	public void addToOscMessage(OSCMessMaker mess);
	public OSCMessMaker makeOSCMessage();				// this caters for the format that attempts to not use recursive listbreakers
	public void addOffMessageToOSC(OSCMessMaker mess, int clipObjectIndex);
	public OSCMessMaker makeOffOSCMessage();
	public void setTrackIndex(int i);
//	public int controllerIndex();
	public String name();
}
