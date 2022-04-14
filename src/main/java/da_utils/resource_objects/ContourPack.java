package main.java.da_utils.resource_objects;
import java.util.ArrayList;

import main.java.da_utils.four_point_contour.FourPointContour;



public class ContourPack {

	public FourPointContour contour;					//melody willuse this as overarching contour
	public ArrayList<FourPointContour> contourList = new ArrayList<FourPointContour>();		//melody will cycle through these for individual phrases
	public boolean isNullObject = true;
	private int readIndex = 0;
	
	public ContourPack(){
		
	}
	public ContourPack(FourPointContour fpc){
		contour = fpc;
		isNullObject = false;
	}

	public void setContour(FourPointContour fpc){
		contour = fpc;
	}
	public void addToContourList(FourPointContour fpc){
		contourList.add(fpc);
	}
	public void resetReadIndex(){
		readIndex = 0;
	}
	public FourPointContour nextContour(){
		FourPointContour temp = contourList.get(readIndex);
		readIndex++;
		if (readIndex >= contourList.size()) readIndex =0;
		return temp;
	}
	public String toString(){
		String ret = "ContourPack ----------\n";
		if (contour == null){
			ret = ret + "null contour object.";
		} else {
			ret = ret + "contour\n";
			ret = ret + contour.toString();
			for (FourPointContour fpc:contourList){
				ret = ret + "-----\n";
				ret = ret + fpc.toString();
			}
		}		
		return ret;
	}
}
