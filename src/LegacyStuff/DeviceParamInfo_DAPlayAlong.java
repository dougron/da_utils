package LegacyStuff;
import java.util.ArrayList;

import com.cycling74.max.Atom;


public class DeviceParamInfo_DAPlayAlong implements ControllerInfo_DAPlayAlong{

	private int controllerIndex;
	private int track;
	private int device;
	private int parameter;
	private double minimum;
	private double maximum;
	private static String messageString = "deviceparam";	// should be refered to by all things making controller messages
	
	
	
	public DeviceParamInfo_DAPlayAlong(int controllerIndex, int track, int device, int param, double min, double max){
		this.controllerIndex = controllerIndex;
		this.track = track;
		this.device = device;
		parameter = param;
		minimum = min;
		maximum = max;
	}
	public Atom[] initAtomArray(){
		Atom[] atArr = new Atom[]{
				Atom.newAtom(messageString),
				Atom.newAtom(controllerIndex),
				Atom.newAtom(track),
				Atom.newAtom(device),
				Atom.newAtom(parameter),
				Atom.newAtom(minimum),
				Atom.newAtom(maximum)
		};
		return atArr;
	}
	public ArrayList<Atom> initAtomList(){
		ArrayList<Atom> atList = new ArrayList<Atom>();
		for (Atom atom: initAtomArray()){
			atList.add(atom);
		}
		return atList;
	}
}
