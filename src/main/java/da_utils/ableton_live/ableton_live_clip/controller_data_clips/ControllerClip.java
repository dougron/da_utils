package main.java.da_utils.ableton_live.ableton_live_clip.controller_data_clips;
import java.util.ArrayList;
/*
 * class to hold data for the controller loops in DAPlayAlong
 */
import java.util.Collections;



public class ControllerClip {
	
	public ArrayList<FunctionBreakPoint> fbpList = new ArrayList<FunctionBreakPoint>();
	public int clipObjectIndex;
	public int controllerIndex;
	public double length;
	public double offset = 0.0;
	public int onOff = 1;
	public String resolution = "128n";
	public double offValue = -1.0;
	public String name = "xxx";				//in the new playlet this identifies the contrller by its fuinctional name eg HP, pan, etc which is defined during initialization
	
	public ControllerClip(int clipObjInd, int contInd, double length){
		clipObjectIndex = clipObjInd;
		controllerIndex = contInd;
		this.length = length;
	}
	
	public void addBreakPoint(double pos, double value){
		fbpList.add(new FunctionBreakPoint(pos, value));
		
	}
	public void sortBreakPointList(){
		Collections.sort(fbpList, FunctionBreakPoint.positionComparator);
	}
	public void addBreakPoint(ArrayList<FunctionBreakPoint> bpList){
		fbpList.addAll(bpList);
		sortBreakPointList();
	}
	public void setOff(double value){
		onOff = 0;
		offValue = value;
	}
	public String toString(){
		String str = "ControllerClip: " + name + " length=" + length + " clipObjectIndex=" + clipObjectIndex + " controllerIndex=" + controllerIndex + "\n";
		for (FunctionBreakPoint fbp: fbpList){
			str += "  " + fbp.toString() + "\n";
		}
		return str;
	}
}
