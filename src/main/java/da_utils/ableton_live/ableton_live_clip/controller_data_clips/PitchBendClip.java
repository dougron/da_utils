package main.java.da_utils.ableton_live.ableton_live_clip.controller_data_clips;
import java.util.ArrayList;
import java.util.Collections;




public class PitchBendClip {
//	pitchbend message format from Max4Live
//	1: i clipObjectIndex
//	2: i on/off
//	3: f off value (range 0. - 1.)
//	4: f length
//	5: f offset
//	6: s resolution
//	7: f depth (0. to 12. (semitones))
//	8: break point f f x y pairs
	
	public ArrayList<FunctionBreakPoint> fbpList = new ArrayList<FunctionBreakPoint>();
	public int clipObjectIndex;
	public double length;
	public double offset = 0.0;
	public String resolution = "128n";
	public double pitchBendRange = 2.0;
	public int onOff = 1;
	public double offValue = -1.0;

	public PitchBendClip(int clipObjInd, double length){
		this.length = length;
		clipObjectIndex = clipObjInd;
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
	public String toString(){
		String ret = "PitchBendClip:....\n";
		ret = ret + "clipObjectIndex:       " + clipObjectIndex + "\n";
		ret = ret + "length:                " + length + "\n";
		ret = ret + "resolution     :       " + resolution + "\n";
		for (FunctionBreakPoint fbp: fbpList){
			ret = ret + "    " + fbp.toString() + "\n";
		}
		return ret;
	}
	public boolean isSameAs(PitchBendClip pbc){
		if (fbpList.size() == pbc.fbpList.size()){
			for (int i = 0; i < fbpList.size(); i++){
				if (!fbpList.get(i).isSameAs(pbc.fbpList.get(i))){
					return false;
				}
			}
		} else {
			return false;
		}
		return true;
	}
}
