package main.java.da_utils.resource_objects;
import com.cycling74.max.Atom;


public class TwoBarRhythmBuffer {

	public int[] buffy = new int[32];
	public int slotsPerBar = 16;
	
	
	public TwoBarRhythmBuffer(){
		
	}
	public void set(int index, int value){
		if (index < buffy.length){
			buffy[index] = value;
		}
	}
	public String buffyToString(){
		String ret = "";
		for (int i: buffy){
			ret = ret + "" + i;
		}
		return ret;
	}
	public Atom[] buffyToAtomArray(){
		Atom[] atomArr = new Atom[buffy.length];
		for (int i = 0; i < atomArr.length; i++){
			atomArr[i] = Atom.newAtom(i);
		}
		return atomArr;
	}
}
