package main.java.da_utils.test_utils;
import java.util.ArrayList;

import LegacyStuff.ChordProgrammer2;
import LegacyStuff.ChordProgrammer2Parent;
import LegacyStuff.ControllerInfo_DAPlayAlong;
import LegacyStuff.DeviceParamInfo_DAPlayAlong;
import LegacyStuff.PanInfo_DAPlayAlong;
import LegacyStuff.SendInfo_DAPlayAlong;
import main.java.da_utils.ableton_live.ableton_device_control_utils.SendInfo;
import main.java.da_utils.ableton_live.ableton_device_control_utils.controller.ControllerInfo;
import main.java.da_utils.ableton_live.ableton_live_clip.LiveClip;
import main.java.da_utils.ableton_live.ableton_live_clip.clip_info_object.ClipInfoObject;
import main.java.da_utils.ableton_live.ableton_live_clip.controller_data_clips.ControllerClip;
import main.java.da_utils.ableton_live.ableton_live_clip.controller_data_clips.PitchBendClip;
import main.java.da_utils.resource_objects.ContourData;
import main.java.da_utils.resource_objects.TwoBarRhythmBuffer;


public class TestData {
	
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

	public static ArrayList<PitchBendClip> makePBList(){
		ArrayList<PitchBendClip> pbList = new ArrayList<PitchBendClip>();
		pbList.add(makeBassBender());
		pbList.add(makeKeysBender());
		pbList.add(makeMelodyBender());
		// etc.......
		return pbList;
	}
	public static ArrayList<ControllerClip> makeCCList(){
		ArrayList<ControllerClip> ccList = new ArrayList<ControllerClip>();
		ccList.add(makeDrumSend());
		ccList.add(makeBassSend());
		ccList.add(makeKeysSend());
		ccList.add(makeMelodySend());
		ccList.add(makeKeysFilterFreq());
		ccList.add(makeKeysTrem());
		ccList.add(makeMelodyPan());
		// etc.......
		return ccList;
	}
	
	public static ArrayList<LiveClip> makeLCList(){
		ArrayList<LiveClip> lcList = new ArrayList<LiveClip>();
		lcList.add(makeDrumClip());
		lcList.add(makeBassClip());
		lcList.add(makeKeysClip());
		lcList.add(makeMelodyClip());		
		return lcList;
	}
	
//public PitchBendClip(int clipObjInd, double length)
	
	private static PitchBendClip makeBassBender(){
		PitchBendClip pb = new PitchBendClip(bassClipObjectIndex, 8.0);
		pb.addBreakPoint(0.0, 0.5);
		pb.addBreakPoint(0.5, 1.0);
		pb.addBreakPoint(1.0, 0.5);
		return pb;
	}
	private static PitchBendClip makeKeysBender(){
		PitchBendClip pb = new PitchBendClip(keysClipObjectIndex, 8.0);
		pb.pitchBendRange = 12.0;
		pb.addBreakPoint(0.0, 0.5);
		pb.addBreakPoint(4.0, 0.5);
		pb.addBreakPoint(7.0, 1.0);
		pb.addBreakPoint(8.0, 0.0);
		return pb;
	}
	private static PitchBendClip makeMelodyBender(){
		PitchBendClip pb = new PitchBendClip(melodyClipObjectIndex, 4.0);
		pb.addBreakPoint(0.0, 0.5);
		pb.addBreakPoint(1.99, 1.0);
		pb.addBreakPoint(2.0, 0.5);
		pb.addBreakPoint(4.0, 0.0);
		return pb;
	}
	public static LiveClip liveClipForTestForm(){
		LiveClip lc = new LiveClip(0,0);
		lc.addNote(51, 0.0, 1.0, 100, 0);		// Eb major - using ebminor caused key identification prblems
		lc.addNote(55, 0.0, 1.0, 100, 0);
		lc.addNote(58, 0.0, 1.0, 100, 0);
		lc.addNote(48, 8.0, 1.0, 100, 0);		// cm
		lc.addNote(51, 8.0, 1.0, 100, 0);
		lc.addNote(55, 8.0, 1.0, 100, 0);
		
		lc.length = 16.0;
		lc.loopStart = 0.0;
		lc.loopEnd = 16.0;
		return lc;
	}
	public static LiveClip shortLiveClipForTestForm(){
		LiveClip lc = new LiveClip(0,0);
		lc.addNote(48, 0.0, 1.0, 100, 0);		// cm
		lc.addNote(51, 0.0, 1.0, 100, 0);
		lc.addNote(55, 0.0, 1.0, 100, 0);

		lc.length = 4.0;
		lc.loopStart = 0.0;
		lc.loopEnd = 4.0;
		return lc;
	}
	
	public static LiveClip chordProgressionAmGFE7(){
		LiveClip lc = new LiveClip(0,0);
		lc.addNote(45, 0.0, 4.0, 100, 0);		// Am
		lc.addNote(48, 0.0, 4.0, 100, 0);
		lc.addNote(52, 0.0, 4.0, 100, 0);
		
		lc.addNote(43, 4.0, 2.0, 100, 0);		// G
		lc.addNote(47, 4.0, 2.0, 100, 0);
		lc.addNote(50, 4.0, 2.0, 100, 0);
		
		lc.addNote(41, 6.0, 2.0, 100, 0);		// F
		lc.addNote(45, 6.0, 2.0, 100, 0);
		lc.addNote(48, 6.0, 2.0, 100, 0);
		
		lc.addNote(40, 8.0, 8.0, 100, 0);		// E7
		lc.addNote(44, 8.0, 8.0, 100, 0);
		lc.addNote(47, 8.0, 8.0, 100, 0);
		lc.addNote(50, 8.0, 8.0, 100, 0);

		lc.length = 16.0;
		lc.loopStart = 0.0;
		lc.loopEnd = 16.0;
		return lc;
	}
	public static void fourOnFloorForRhythmBuffer(TwoBarRhythmBuffer rb){
		int[] hits = new int[]{0, 4, 8, 12, 16, 20, 24, 28};
		for (int i: hits){
			rb.set(i, 1);
		}
	}
	public static void oneOnFloorForRhythmBuffer(TwoBarRhythmBuffer rb){
		int[] hits = new int[]{0, 16};
		for (int i: hits){
			rb.set(i, 1);
		}
	}
	public static void tangoForRhythmBuffer(TwoBarRhythmBuffer rb){
		int[] hits = new int[]{0, 6, 12, 16, 22, 28};
		for (int i: hits){
			rb.set(i, 1);
		}
	}
	
	
//public ControllerClip(int clipObjInd, int contInd, double length)
	
	private static ControllerClip makeDrumSend(){
		ControllerClip cc = new ControllerClip(drumsClipObjectIndex, delaySend, 8.0);
		cc.addBreakPoint(0.0, 1.0);
		cc.addBreakPoint(1.0, 1.0);
		cc.addBreakPoint(1.1, 0.0);
		return cc;
	}
	private static ControllerClip makeBassSend(){
		ControllerClip cc = new ControllerClip(bassClipObjectIndex, delaySend, 8.0);
		cc.addBreakPoint(0.0, 0.0);
		cc.addBreakPoint(1.9, 0.0);
		cc.addBreakPoint(2.0, 1.0);
		cc.addBreakPoint(3.0, 1.0);
		cc.addBreakPoint(3.1, 0.0);
		return cc;
	}
	private static ControllerClip makeKeysSend(){
		ControllerClip cc = new ControllerClip(keysClipObjectIndex, delaySend, 8.0);
		cc.addBreakPoint(0.0, 0.0);
		cc.addBreakPoint(3.9, 0.0);
		cc.addBreakPoint(4.0, 1.0);
		cc.addBreakPoint(5.0, 1.0);
		cc.addBreakPoint(5.1, 0.0);
		return cc;
	}
	private static ControllerClip makeMelodySend(){
		ControllerClip cc = new ControllerClip(melodyClipObjectIndex, delaySend, 8.0);
		cc.addBreakPoint(0.0, 0.0);
		cc.addBreakPoint(5.9, 0.0);
		cc.addBreakPoint(6.0, 1.0);
		cc.addBreakPoint(7.0, 1.0);
		cc.addBreakPoint(7.1, 0.0);
		return cc;
	}
	private static ControllerClip makeKeysFilterFreq(){
		ControllerClip cc = new ControllerClip(keysClipObjectIndex, keysLPFreqControllerIndex, 4.0);
		cc.addBreakPoint(0.0, 0.5);
		cc.addBreakPoint(2.0, 0.8);
		cc.addBreakPoint(4.0, 0.5);
		return cc;
	}
	private static ControllerClip makeKeysTrem(){
		ControllerClip cc = new ControllerClip(keysClipObjectIndex, keysTremControllerIndex, 16.0);
		cc.addBreakPoint(0.0, 0.0);
		cc.addBreakPoint(7.9, 0.0);
		cc.addBreakPoint(8.0, 1.0);
		return cc;
	}
	private static ControllerClip makeMelodyPan(){
		ControllerClip cc = new ControllerClip(melodyClipObjectIndex, melodyPanControllerIndex, 6.0);
		cc.addBreakPoint(0.0, 0.5);
		cc.addBreakPoint(2.0, 1.0);
		cc.addBreakPoint(4.0, 0.0);
		cc.addBreakPoint(6.0, 0.5);
		return cc;
	}
	private static ControllerClip makeMelodyFreq(){
		ControllerClip cc = new ControllerClip(melodyClipObjectIndex, melodyLPFreqControllerIndex, 4.0);
		cc.setOff(0.7);
		return cc;
	}
// LiveClip data format
//(double len, double lStart, double lEnd, double startMark, double endMark, 
// int sigNum, int sigDenom, double offset, int clip, int track, String name, int clipObjectIndex)
	
	private static LiveClip makeMelodyClip(){
		LiveClip lc = new LiveClip(16.0, 0.0, 16.0, -1.0, -1.0, -1, -1, 0.0, 0, 5, "melodyClip", 3);
		double pos = 0.0;
		for (int i = 0; i < 8; i++){
			lc.addNote((int)(60 + Math.sin(i) * 15), pos, 8.0, 100, 0);
			pos = pos + 2.0;
		}
		return lc;
	}
	private static LiveClip makeKeysClip(){
		LiveClip lc = new LiveClip(8.0, 0.0, 8.0, -1.0, -1.0, -1, -1, 0.0, 0, 4, "keysClip", 2);
		double pos = 0.0;
		for (int i = 0; i < 5; i++){
			lc.addNote(45 + i * 5, pos, 8.0, 100, 0);
		}
		return lc;
	}
	private static LiveClip makeBassClip(){
		LiveClip lc = new LiveClip(8.0, 0.0, 8.0, 1.0, -1.0, 3, 4, 0.0, 0, 3, "bassClip", 1);
		double pos = 0.0;
		for (int i = 40; i < 48; i++){
			lc.addNote(i, pos, 1.0, 100, 0);
			pos++;
		}
		return lc;
	}
	private static LiveClip makeDrumClip(){
		LiveClip lc = new LiveClip(8.0, 0.0, 8.0, -1.0, -1.0, -1, 4, 0.0, 0, 1, "drumClip", 0);
		for (double d = 0.0; d < 8.0; d++){
			lc.addNote
			(36, d, 0.5, 100, 0);
			lc.addNote(42, d + 0.5, 0.5, 100, 0);
		}
		return lc;
	}
	public static LiveClip accentTemplateOne(){
		LiveClip lc = new LiveClip(8.0, 0.0, 8.0, -1.0, -1.0, -1, 4, 0.0, 0, 1, "atClip", 0);
		lc.addNote(0, 0.0, 2.5, 100, 0);
		lc.addNote(0, 2.5, 2.5, 100, 0);
		lc.addNote(0, 5.0, 1.0, 100, 0);
		lc.addNote(0, 6.0, 2.0, 100, 0);
		lc.length = 8.0;
		return lc;
	}
	public static LiveClip accentTemplateTwo(){
		LiveClip lc = new LiveClip(4.0, 0.0, 4.0, -1.0, -1.0, -1, 4, 0.0, 0, 1, "atClip", 0);
		lc.addNote(0, 0.0, 2.0, 100, 0);
		lc.addNote(0, 2.0, 1.0, 100, 0);
		lc.addNote(0, 3.0, 1.0, 100, 0);
		lc.length = 4.0;
		return lc;
	}
	
// stuff for making initialization messages========================================================
	
	public static ClipInfoObject[] makeClipInfoObjects(){
		ClipInfoObject[] cioArr = new ClipInfoObject[]{
//				new ClipInfoObject(0, 1, 0, "drums", drumsControllers()),
//				new ClipInfoObject(1, 3, 0, "bass", bassControllers()),
//				new ClipInfoObject(2, 4, 0, "keys", keysControllers()),
//				new ClipInfoObject(3, 5, 0, "melody", melodyControllers()),
		};
		return cioArr;
	}
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
