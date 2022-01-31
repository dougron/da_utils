package main.java.da_utils.xml_maker.note;

import java.util.ArrayList;

import main.java.da_utils.xml_maker.MXM;

public class XMLNoteAppearance {
	
	private String xmlName;
	private int dotCount;

	

	public XMLNoteAppearance(String xmlName, int dotCount) {
		this.xmlName = xmlName;
		this.dotCount = dotCount;
	}
	
	public String toString(){
		return "XMLNoteAppearance name=" + xmlName + " dotCount=" + dotCount;
	}

	public void addTypeToStringList(ArrayList<String> strList, int indent) {
		MXM.addOneLiner(strList, MXM.TYPE, xmlName, indent);
		
	}
	public void addDotCountToStringList(ArrayList<String> strList, int indent){
		for (int i = 0; i < dotCount; i++){
			strList.add(MXM.getIndent(indent) + MXM.LB + MXM.DOT + MXM.BS + MXM.RB);
		}
	}

}
