package main.java.da_utils.oddball_utils.audio_buffer_utils;

import java.util.ArrayList;
import java.util.Collections;

import com.cycling74.max.Atom;
import com.cycling74.max.DataTypes;
import com.cycling74.max.MaxObject;

import main.java.da_utils.combo_variables.IntAndDouble;

public class AudioBufferListSorter extends MaxObject {

	ArrayList<IntAndDouble> list = new ArrayList<IntAndDouble>();
	
	public AudioBufferListSorter(){
		System.out.println("AudioBufferListSorter instantiating......");
		setMaxInlets();

		System.out.println("AudioBufferListSorter instantiating complete.");
	}
	
	public void add(int index, double value){
		list.add(new IntAndDouble(index, value));
		//System.out.println("new item added i=" + index + " d=" + value + "  list.size()=" + list.size());
	}
	
	public void bang(){
		if (list.size() > 0){
			Collections.sort(list, IntAndDouble.doubleComparator);
			Atom[] atArr = new Atom[list.size()];
			for (int i = 0; i < list.size(); i++){
				atArr[i] = Atom.newAtom(list.get(i).i);
			}
			list.clear();
			outlet(0, atArr);
		}
		
	}
	public void clear(){
		list.clear();
	}
	
	
	
	private void setMaxInlets(){

		declareInlets(new int[]{
				DataTypes.ALL,  
				DataTypes.INT}
		);
		declareOutlets(new int[]{ 
				DataTypes.ALL, 
				DataTypes.ALL,
				DataTypes.ALL}
		);
		setInletAssist(new String[]{
				"most input",
				"other input"}
		);
		setOutletAssist(new String[]{
				"messages out",
				"clip inject out",
				"comment out"}
		);
	}
}
