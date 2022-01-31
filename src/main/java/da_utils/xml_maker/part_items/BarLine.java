package main.java.da_utils.xml_maker.part_items;

import java.util.ArrayList;

import main.java.da_utils.xml_maker.MXM;

public class BarLine implements PartItem {
	
	
	public static final int LIGHT_LIGHT = 0;
	public static final int DASHED = 1;
	public static final int DOTTED = 2;
	public static final int LIGHT_HEAVY = 3;
	public static final int START_REPEAT = 4;
	public static final int END_REPEAT = 5;
//	public static final int LIGHT_LIGHT = 0;
//	public static final int LIGHT_LIGHT = 0;
	private double position;
	private int barlineType;
	private String location;
	private static final int type = Part.barLineType;


	public BarLine(double pos, int type){
		this.position = pos;
		this.barlineType = type;
	}
	@Override
	public double position() {
		return position;
	}

	@Override
	public double length() {
		return 0;
	}

	@Override
	public double actualPos() {
		return position;
	}

	@Override
	public int note() {
		return 128;
	}

	@Override
	public int getType() {
		return type;
	}
	public String toString(){
		String ret = "Barline: " + location + " barlineType: " + barlineType;
		return ret;
	}

	@Override
	public void addToStrList(ArrayList<String> strList, int indent) {
		//strList.add(MXM.LB + MXM.BARLINE + " " + MXM.LOCATION + "=" + MXM.INVC + location + MXM.INVC + MXM.RB);
		MXM.addSingleEntryWithAttributes(strList, MXM.BARLINE, new String[][]{
			new String[]{MXM.LOCATION, location}
		}, indent);
		switch(barlineType){
		case 0: makeLightLight(strList, indent + MXM.indentIncrement);
				break;
		case 1: makeDashed(strList, indent + MXM.indentIncrement);
				break;
		case 2: makeDotted(strList, indent + MXM.indentIncrement);
				break;
		case 3: makeLightHeavy(strList, indent + MXM.indentIncrement);
				break;
		case 4: makeStartRepeat(strList, indent + MXM.indentIncrement);
				break;
		case 5: makeEndRepeat(strList, indent + MXM.indentIncrement);
				break;

		}
		MXM.addSingleEntry(strList, MXM.BARLINE, indent + MXM.indentIncrement, true);

	}
	private void makeEndRepeat(ArrayList<String> strList, int indent) {
		MXM.addOneLiner(strList, MXM.BAR_STYLE, MXM.LIGHT_HEAVY, indent);
		MXM.addSingleEntryWithAttributesAndEndSlash(strList, MXM.REPEAT, new String[][]{
			new String[]{MXM.DIRECTION, MXM.BACKWARD}
		}, indent);
		
	}
	private void makeStartRepeat(ArrayList<String> strList, int indent) {
		MXM.addOneLiner(strList, MXM.BAR_STYLE, MXM.HEAVY_LIGHT, indent);
		MXM.addSingleEntryWithAttributesAndEndSlash(strList, MXM.REPEAT, new String[][]{
			new String[]{MXM.DIRECTION, MXM.FORWARD}
		}, indent);
	}
	private void makeLightHeavy(ArrayList<String> strList, int indent) {
		MXM.addOneLiner(strList, MXM.BAR_STYLE, MXM.LIGHT_HEAVY, indent);
		
	}
	private void makeDotted(ArrayList<String> strList, int indent) {
		MXM.addOneLiner(strList, MXM.BAR_STYLE, MXM.DOTTED, indent);
		
	}
	private void makeDashed(ArrayList<String> strList, int indent) {
		MXM.addOneLiner(strList, MXM.BAR_STYLE, MXM.DASHED, indent);
		
	}
	private void makeLightLight(ArrayList<String> strList, int indent) {
		MXM.addOneLiner(strList, MXM.BAR_STYLE, MXM.LIGHT_LIGHT, indent);		
	}
	@Override
	public void customSetting(String settingName, int setting) {
		// TODO Auto-generated method stub
		
	}
	@Override
	public void customSetting(String settingName, String setting) {
		if (settingName.equals(MXM.LOCATION)){
			this.location = setting;
		}
		
	}
	@Override
	public int articulation() {
		return 0;		// no articulation for barlines pls...:)
	}

}
