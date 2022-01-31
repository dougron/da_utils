package main.java.da_utils.algorithmic_models.pipeline.plugins.test_plugins;

import java.util.ArrayList;


import LegacyStuff.ChordProgrammer2;
import LegacyStuff.ChordProgrammer2Parent;
import LegacyStuff.ControllerInfo_DAPlayAlong;
import LegacyStuff.DeviceParamInfo_DAPlayAlong;
import LegacyStuff.PanInfo_DAPlayAlong;
import LegacyStuff.SendInfo_DAPlayAlong;
import main.java.da_utils.resource_objects.ContourData;




public class TestData_DAPlayAlong {
	
	private static int drumsTrack = 1;
	private static int bassTrack = 3;
	private static int keysTrack = 4;
	private static int melodyTrack = 5;
	
	private static int drumsClipObjectIndex = 0;
	private static int bassClipObjectIndex = 1;
	private static int keysClipObjectIndex = 2;
	private static int melodyClipObjectIndex = 3;
	
	private static int delaySendControllerIndex = 0;
	private static int keysTremControllerIndex = 1;
	private static int keysLPFreqControllerIndex = 2;
	private static int melodyProcessorDeviceIndex = 2;
	private static int melodyLPFreqParameter = 1;
	private static int melodyPanControllerIndex = 1;
	private static int melodyLPFreqControllerIndex = 2;
	
	private static int delaySend = 0;
	private static int keysProcessorDeviceIndex = 2;
	private static int keysTremAmountParameter = 1;
	private static int keysLPFreqParameter = 2;



	
// stuff for making initialization messages========================================================
	
//	public static ClipInfoObject[] makeClipInfoObjects(){
//		ClipInfoObject[] cioArr = new ClipInfoObject[]{
//				new ClipInfoObject(0, 1, 0, "drums", drumsControllers()),
//				new ClipInfoObject(1, 3, 0, "bass", bassControllers()),
//				new ClipInfoObject(2, 4, 0, "keys", keysControllers()),
//				new ClipInfoObject(3, 5, 0, "melody", melodyControllers()),
//		};
//		return cioArr;
//	}
	private static ControllerInfo_DAPlayAlong[] drumsControllers(){
		return new ControllerInfo_DAPlayAlong[]{
				new SendInfo_DAPlayAlong(delaySendControllerIndex, drumsTrack, delaySend)
		};
	}
	private static ControllerInfo_DAPlayAlong[] bassControllers(){
		return new ControllerInfo_DAPlayAlong[]{
				new SendInfo_DAPlayAlong(delaySendControllerIndex, bassTrack, delaySend)
		};
	}
	private static ControllerInfo_DAPlayAlong[] keysControllers(){
		return new ControllerInfo_DAPlayAlong[]{
				new SendInfo_DAPlayAlong(delaySendControllerIndex, keysTrack, delaySend),
				new DeviceParamInfo_DAPlayAlong(keysTremControllerIndex, keysTrack, keysProcessorDeviceIndex, keysTremAmountParameter, 0., 127.),
				new DeviceParamInfo_DAPlayAlong(keysLPFreqControllerIndex, keysTrack, keysProcessorDeviceIndex, keysLPFreqParameter, 0., 127.)
				
		};
	}
	private static ControllerInfo_DAPlayAlong[] melodyControllers(){
		return new ControllerInfo_DAPlayAlong[]{
				new SendInfo_DAPlayAlong(delaySendControllerIndex, melodyTrack, delaySend),
				new PanInfo_DAPlayAlong(melodyPanControllerIndex, melodyTrack),
				new DeviceParamInfo_DAPlayAlong(melodyLPFreqControllerIndex, melodyTrack, melodyProcessorDeviceIndex, melodyLPFreqParameter, 0., 127.)
		};
		
	}
	public static ChordProgrammer2 makeProgression(ChordProgrammer2Parent p){
		ChordProgrammer2 cp = new ChordProgrammer2(p);
		cp.formLength = 4;
		cp.noteIn(50, 100, 1); // Dm, i think
		cp.noteIn(53, 100, 1);
		cp.noteIn(57, 100, 1);
		cp.chordButtonState(0, 0, 1);
		cp.chordButtonState(0, 0, 0);
		cp.noteIn(50, 0, 1); // Dm, i think
		cp.noteIn(53, 0, 1);
		cp.noteIn(57, 0, 1);
		
		cp.noteIn(52, 100, 1); // Em, i think
		cp.noteIn(55, 100, 1);
		cp.noteIn(59, 100, 1);
		cp.chordButtonState(2, 0, 1);
		cp.chordButtonState(2, 0, 0);
		cp.noteIn(52, 0, 1); // Dm, i think
		cp.noteIn(55, 0, 1);
		cp.noteIn(59, 0, 1);
		
		return cp;
		
	}
	public static ContourData upContour(){
		ContourData cd = new ContourData();
		cd.newData(1.0, 1.0, 1, ContourData.HIGH_MIDPOINT);
		return cd;
	}
	public static ContourData downContour(){
		ContourData cd = new ContourData();
		cd.newData(1.0, -1.0, 1, ContourData.LOW_MIDPOINT);
		return cd;
	}
	public static ContourData updownContour(){
		ContourData cd = new ContourData();
		cd.newData(0.5, 0.0, 1, ContourData.HIGH_MIDPOINT);
		return cd;
	}
	public static ContourData downupContour(){
		ContourData cd = new ContourData();
		cd.newData(0.5, 0.0, 1, ContourData.LOW_MIDPOINT);
		return cd;
	}

}
