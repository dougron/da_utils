package main.java.da_utils.xml_maker.key;

import java.util.ArrayList;

import main.java.da_utils.xml_maker.MXM;

public class KeyMap {

	public ArrayList<XMLKeyZone> xkZoneList = new ArrayList<XMLKeyZone>();
	
	
	public KeyMap(){
		
	}
	public void addNewKeyZone(XMLKeyZone xkz){
		xkZoneList.add(xkz);
	}
//	public double endOfFile() {
//		double count = 0.0;
//		for (TimeSignatureZone tsz: xkZoneList){
//			count += ((double)tsz.beats / 4.0) * tsz.beats * tsz.count;
//		}
//		return count;
//	}
	public XMLKey getXMLKeyZone(int barPos) {
		int pos = 1;
		for (XMLKeyZone xkz: xkZoneList){
			if (barPos >= pos && barPos < pos + xkz.barCount){
				return xkz.xmlKey;
			}
			pos += xkz.barCount;
		}
		return MXM.KEY_OF_C;
	}
	
//	public boolean restateKey(int barCount) {
//		if (barCount == 1){
//			return true;
//		} else {
//			return false;
//		}		
//	}
	public boolean restateKey(int barCount){
		if (barCount == 1){
			return true;
		} else {
			int bc = barCount;
			for (XMLKeyZone kz: xkZoneList){
				bc -= kz.barCount;
				if (bc == 1) return true;
			}
			return false;
		}
	}
}
