package main.java.da_utils.ableton_live.ableton_live_clip.controller_data_clips;
import java.util.Comparator;

/*
 * item for the ControllerClip to store breakpoint data
 */
public class FunctionBreakPoint {
	
	public double position;
	public double value;
	
	public FunctionBreakPoint(double pos, double value){
		position = pos;
		this.value = value;
	}
	public static Comparator<FunctionBreakPoint> positionComparator = new Comparator<FunctionBreakPoint>(){
		public int compare(FunctionBreakPoint point1, FunctionBreakPoint point2){
			if (point1.position < point2.position) return -1;
			if (point1.position > point2.position) return 1;
			return 0;
		}
	};

	public String toString(){
		return "position: " + position + " value: " + value;
	}
	public boolean isSameAs(FunctionBreakPoint fbp){
		if (position == fbp.position && value == fbp.position){
			return true;
		} else {
			return false;
		}
	}
}
