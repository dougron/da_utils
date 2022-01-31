package main.java.da_utils.xml_maker.part_items;

import java.util.ArrayList;

public interface PartItem {

	public double position();
	public double length();
	public double actualPos();
	public int note();		// only relevant for PartNotes, but important for sorting
	public int getType();
	public String toString();
	public void addToStrList(ArrayList<String> strList, int indent);
	public void customSetting(String settingName, int setting);
	public void customSetting(String settingName, String setting);
	public int articulation();
	
}
