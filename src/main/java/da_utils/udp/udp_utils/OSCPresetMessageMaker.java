package main.java.da_utils.udp.udp_utils;

public class OSCPresetMessageMaker {

	
	public static OSCMessMaker makePitchBendOffMessage(int track){
		OSCMessMaker mess = new OSCMessMaker();
		mess.addItem(INJECT);
		mess.addItem(PITCHBEND);
		mess.addItem(track);
		mess.addItem(PITCHBEND_OFF);
		mess.addItem(PITCHBEND_OFF_VALUE);
		
		return mess;
	}
	public static OSCMessMaker makePitchBendMessage(
			int track, double length, 
			double offset, String resolution, 
			double depth, double[] breakpointArray){
		OSCMessMaker mess = new OSCMessMaker();
		mess.addItem(INJECT);
		mess.addItem(PITCHBEND);
		mess.addItem(track);
		mess.addItem(PITCHBEND_ON);
		mess.addItem(PITCHBEND_OFF_VALUE);
		mess.addItem(length);
		mess.addItem(offset);
		mess.addItem(resolution);
		mess.addItem(depth);
		for (double d: breakpointArray){
			mess.addItem(d);
		}
		
		return mess;
	}
//	1: i clipObjectIndex
//	2: i on/off
//	3: f off value (range 0. - 1.)
//	4: f length
//	5: f offset
//	6: s resolution
//	7: f depth
//	8: break point f f x y pairs
	
	
	public static final String INJECT = "inject";
	public static final String PITCHBEND = "pitchbend";
	private static final int PITCHBEND_OFF = 0;
	private static final int PITCHBEND_ON = 1;
	private static final double PITCHBEND_OFF_VALUE = 0.5;
}
