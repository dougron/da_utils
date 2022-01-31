package LegacyStuff;
import java.util.ArrayList;

import com.cycling74.max.Atom;


public class SendInfo_DAPlayAlong implements ControllerInfo_DAPlayAlong{
	
	private int controllerIndex;
	private int track;
	private int sendIndex;
	private double minimum = 0.0;		// default value
	private double maximum = 1.0;
	private static String messageString = "send";	// should be refered to by all things making controller messages

	
	public SendInfo_DAPlayAlong(int controllerIndex, int track, int send){
		this.controllerIndex = controllerIndex;
		this.track = track;
		this.sendIndex = send;
	}
	public SendInfo_DAPlayAlong(int controllerIndex, int track, int send, double min, double max){
		this.controllerIndex = controllerIndex;
		this.track = track;
		this.sendIndex = send;
		minimum = min;
		maximum = max;
	}
	public Atom[] initAtomArray(){
		Atom[] atArr = new Atom[]{
				Atom.newAtom(messageString),
				Atom.newAtom(controllerIndex),
				Atom.newAtom(track),
				Atom.newAtom(sendIndex),
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
