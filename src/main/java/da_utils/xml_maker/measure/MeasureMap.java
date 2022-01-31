package main.java.da_utils.xml_maker.measure;

import java.util.ArrayList;

import main.java.da_utils.xml_maker.time_signature.XMLTimeSignatureZone;

public class MeasureMap {

	public ArrayList<XMLTimeSignatureZone> tsZoneList = new ArrayList<XMLTimeSignatureZone>();
	
	
	public MeasureMap(){
		
	}
	public void addNewTimeSignatureZone(XMLTimeSignatureZone tsz){
		tsZoneList.add(tsz);
	}
	public double endOfFile() {
		double count = 0.0;
		for (XMLTimeSignatureZone tsz: tsZoneList){
			count += tsz.zoneLength();
		}
		return count;
	}
}
