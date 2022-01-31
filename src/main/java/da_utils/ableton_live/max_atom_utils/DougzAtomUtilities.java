package main.java.da_utils.ableton_live.max_atom_utils;
import java.util.ArrayList;

import com.cycling74.max.Atom;

import main.java.da_utils.ableton_live.ableton_live_clip.controller_data_clips.FunctionBreakPoint;
import main.java.da_utils.ableton_live.clip_injector.ClipInjectorListObject;

/*
 * Various conversion methods relating to Atoms for use in MaxObjects
 */
public class DougzAtomUtilities {

	
	public static String atomArrToString(Atom[] atArr){
		String str = "";
		for (Atom atom: atArr){
			if (atom.isString()) str = str + atom.getString();
			if (atom.isFloat()) str = str + atom.getFloat();
			if (atom.isInt()) str = str + atom.getInt();
			str += " ";
		}
		return str;
	}
	public static Atom[] atomArrFromFunctionBreakPointList(ArrayList<FunctionBreakPoint> bpList){
		Atom[] atomArr = new Atom[bpList.size() * 2];
		for (int i = 0; i < bpList.size(); i++){
			atomArr[i * 2] = Atom.newAtom(bpList.get(i).position);
			atomArr[i * 2 + 1] = Atom.newAtom(bpList.get(i).value);
		}
		return atomArr;
	}
	public static Atom[] atomArrFromCILOList(ArrayList<ClipInjectorListObject> list){
		Atom[] atArr = new Atom[list.size()];
		int index = 0;
		for (ClipInjectorListObject cilo: list){
			atArr[index] = cilo.getAtom();
			index++;
		}
		return atArr;
	}
}
