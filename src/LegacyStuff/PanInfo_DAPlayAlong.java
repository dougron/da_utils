package LegacyStuff;
import java.util.ArrayList;

import com.cycling74.max.Atom;


public class PanInfo_DAPlayAlong implements ControllerInfo_DAPlayAlong{
	
	private int controllerIndex;
	private int track;
	private double minimum = -1.0;		// default value
	private double maximum = 1.0;
	private static String messageString = "pan";	// should be refered to by all things making controller messages

	public PanInfo_DAPlayAlong(int controllerIndex, int track){
		this.controllerIndex = controllerIndex;
		this.track = track;
	}
	public PanInfo_DAPlayAlong(int controllerIndex, int track, double min, double max){
		this.controllerIndex = controllerIndex;
		this.track = track;
		minimum = min;
		maximum = max;
	}
	public Atom[] initAtomArray(){
		Atom[] atArr = new Atom[]{
				Atom.newAtom(messageString),
				Atom.newAtom(controllerIndex),
				Atom.newAtom(track),
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
