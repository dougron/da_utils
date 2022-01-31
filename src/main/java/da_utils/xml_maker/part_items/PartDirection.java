package main.java.da_utils.xml_maker.part_items;

import java.util.ArrayList;

import main.java.da_utils.xml_maker.MXM;

public class PartDirection implements PartItem {
	
	
	private double position;
	private String text;
	public static int type = Part.directionType;
	public int placement;
	private boolean addTextSizeAndYOffset = false;
	private int textSize;
	private int yOffset;
	private Object xOffset;
	private boolean addTextSizeAndBothOffsets = false;
	private boolean addTextSize = false;
	
	
	public PartDirection(String text, double pos, int placement){
		this.position = pos;
		this.text = text;
		this.placement = placement;
	}
	
	
	public PartDirection(String text, double pos, int placement, int textSize, int yoffset) {
		this.position = pos;
		this.text = text;
		this.placement = placement;
		this.addTextSizeAndYOffset  = true;
		this.textSize = textSize;
		this.yOffset = yoffset;
	}
	public PartDirection(String text, double pos, int placement, int textSize, int yoffset, int xoffset) {
		this.position = pos;
		this.text = text;
		this.placement = placement;
		this.addTextSizeAndBothOffsets   = true;
		this.textSize = textSize;
		this.yOffset = yoffset;
		this.xOffset = xoffset;
	}


	public PartDirection(String text, double pos, int placement, int textSize) {
		this.position = pos;
		this.text = text;
		this.placement = placement;
		this.textSize = textSize;
		this.addTextSize  = true;
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
		return 129;			// means that text gets precedence when sorting
	}

	@Override
	public int getType() {
		return type;
	}

	@Override
	public void addToStrList(ArrayList<String> strList, int indent) {
		String str = "";
		if (placement == MXM.PLACEMENT_ABOVE){
			str = MXM.ABOVE;
		} else if (placement == MXM.PLACEMENT_BELOW){
			str = MXM.BELOW;
		}
		
		MXM.addSingleEntryWithAttributes(strList, MXM.DIRECTION,
				new String[][]{
					new String[]{MXM.PLACEMENT, str}
				},
				indent);
		if (addTextSizeAndYOffset || addTextSizeAndBothOffsets || addTextSize){
			String[][] attribArr;
			if (addTextSizeAndYOffset){
				attribArr = new String[][]{
					new String[]{MXM.DEFAULT_Y, "" + yOffset},
					new String[]{MXM.FONT_SIZE, "" + textSize},
						
				};
			} else if (addTextSizeAndBothOffsets) {
				attribArr = new String[][]{
					new String[]{MXM.RELATIVE_X, "" + xOffset},
					new String[]{MXM.DEFAULT_Y, "" + yOffset},
					new String[]{MXM.FONT_SIZE, "" + textSize},
				};
			} else {
				attribArr = new String[][]{
					new String[]{MXM.FONT_SIZE, "" + textSize},
				};
			}
			
			MXM.addSingleEntry(strList, MXM.DIRECTION_TYPE, indent + MXM.indentIncrement, false);
			MXM.addOneLinerWithAttributes(
					strList, 
					MXM.WORDS, 
					text, 
					indent + MXM.indentIncrement + MXM.indentIncrement,
					attribArr);
			MXM.addSingleEntry(strList, MXM.DIRECTION_TYPE, indent + MXM.indentIncrement + MXM.indentIncrement, true);
		} else {
			MXM.makeQuickEntry(
					strList, 
					MXM.DIRECTION_TYPE, 
					new String[][]{
						new String[]{MXM.WORDS, text}
					}, 
					new String[][]{}, 
					indent + MXM.indentIncrement);
		}
		
		MXM.addSingleEntry(strList, MXM.DIRECTION, indent + MXM.indentIncrement, true);
	}
	@Override
	public void customSetting(String settingName, int setting) {
		// no custom settings as yet
		
	}

	@Override
	public void customSetting(String settingName, String setting) {
		// no custom settings as yet
		
	}


	@Override
	public int articulation() {
		return 0;		// does nothing......
	}

}
