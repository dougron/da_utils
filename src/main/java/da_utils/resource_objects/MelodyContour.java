package main.java.da_utils.resource_objects;
import main.java.da_utils.algorithmic_models.pipeline.plugins.pipeline_plugin.PlayPlugArgument;

/*
 * returns as contour position value for a contour
 */
public class MelodyContour {
	
//	public ContourData cd;
//	public ChordProgrammer2 cp;
//	public ResourceObject ro;

	public MelodyContour(){
//		this.ro = ro;
	}
	public double getContourValue(double pos, PlayPlugArgument ppa){
		double length = ppa.cf.length() * (ppa.cd.phraseLength + 1);
		pos = pos % length;
//		double posPercent = pos / length;
		if (pos / length <= ppa.cd.midPoint){
			return getFirstBitValue(pos, length, ppa);
		} else {
			return getSecondBitValue(pos, length, ppa);
		}
	}
	
	public String toString(){
		String ret = "MelodyContour ---------------------------\n";
//		ret += ro.cd.toString() + "\n";
//		ret += ro.cp.toString();
		return ret;
	}
// privates --------------------------------------------------------------------------
	private double getSecondBitValue(double pos, double length, PlayPlugArgument ppa){
		double pos2 = (pos / length - ppa.cd.midPoint) / (1.0 - ppa.cd.midPoint);
		if (ppa.cd.highOrLow == 0){
			return 1.0 - (pos2 * (1 - ppa.cd.endPoint));
		} else {
			return -1.0 + (pos2 * (1 + ppa.cd.endPoint));
		}
	}
	private double getFirstBitValue(double pos, double length, PlayPlugArgument ppa){
		if (ppa.cd.highOrLow == 0){
			return pos / length / ppa.cd.midPoint;
		} else {
			return pos / length / ppa.cd.midPoint * -1.0;
		}
	}
	
	
}
