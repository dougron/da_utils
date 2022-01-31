package main.java.da_utils.ableton_live.ableton_device_control_utils.device_parameter_utils;

import acm.program.ConsoleProgram;
import main.java.da_utils.ableton_live.ableton_device_control_utils.DeviceParamInfo;
import main.java.da_utils.ableton_live.ableton_device_control_utils.VolumeInfo;
import main.java.da_utils.ableton_live.ableton_device_control_utils.controller.ControllerInfo;
import main.java.da_utils.udp.udp_utils.OSCMessMaker;

public class DeviceParamInfoConsoleTest extends ConsoleProgram{

	public void run(){
		setSize(500, 500);
		ControllerInfo dpi = new VolumeInfo("panny", 2, 0.0, DeviceParamInfo.ofTrackType);
		OSCMessMaker mess = dpi.makeOSCMessage();
		mess.addItem(DeviceParamInfo.controllerString, 0);
		println(mess.toString());
	}
}
