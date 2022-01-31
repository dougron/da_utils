package main.java.da_utils.xml_maker.key;

import main.java.da_utils.xml_maker.MXM;

public class XMLKeyZone {

	
	public XMLKey xmlKey;
	public int barCount;

	public XMLKeyZone(XMLKey xmlKey, int barCount){
		this.xmlKey = xmlKey;
		this.barCount = barCount;
	}
	public XMLKeyZone(int keySig, int barCount){
		this.xmlKey = MXM.xmlKeyMap.get(keySig);
		this.barCount = barCount;
	}
}
