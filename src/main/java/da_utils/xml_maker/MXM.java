package main.java.da_utils.xml_maker;

import java.util.ArrayList;
import java.util.HashMap;

import main.java.da_utils.combo_variables.IntAndString;
import main.java.da_utils.combo_variables.StringPair;
import main.java.da_utils.xml_maker.clef.XMLClef;
import main.java.da_utils.xml_maker.clef.XMLClefOption;
import main.java.da_utils.xml_maker.key.XMLKey;
import main.java.da_utils.xml_maker.note.XMLNoteAppearance;

/* 
 * class to hold all static methiods and constants. MXM to keep typing to a minimum
 */

public class MXM {
	
	public static final String SCORE_PARTWISE = "score-partwise";
	public static final String LB = "<";
	public static final String RB = ">";
	public static final int indentIncrement = 2;
	public static final String PART_LIST = "part-list";
	public static final String BS = "/";
	public static final String SCORE_PART = "score-part";
	public static final String ID_EQUALS = " id=";
	public static final String INVC = "\"";
	public static final String PART_NAME = "part-name";
	public static final String PART = "part";
	public static final String MEASURE = "measure";
	public static final String NUMBER_EQUALS = " number=";
	public static final String CLEF = "clef";
	public static final String SIGN = "sign";
	public static final String LINE = "line";
	public static final String BEATS = "beats";
	public static final String BEAT_TYPE = "beat-type";
	public static final String TIME = "time";
	public static final String KEY = "key";
	public static final String FIFTHS = "fifths";
	public static final String ATTRIBUTES = "attributes";
	public static final String DIVISIONS = "divisions";
	public static final String VERSION_3 = " version=\"3.0\"";
	public static final String DURATION = "duration";
	public static final String BACKUP = "backup";
	
	public static final int PLACEMENT_ABOVE = 0;
	public static final int PLACEMENT_BELOW = 1;
	public static final String DIRECTION = "direction";
	public static final String DIRECTION_TYPE = "direction-type";
	public static final String WORDS = "words";
	public static final String ABOVE = "above";
	public static final String BELOW = "below";
	public static final String PLACEMENT = "placement";
	public static final String BEGIN = "begin";
	public static final String BEAM = "beam";
	public static final String NUMBER = "number";
	
	public static final String BARLINE = "barline";
	public static final String LOCATION = "location";
	public static final String LEFT = "left";
	public static final String RIGHT = "right";
	public static final String BAR_STYLE = "bar-style";
	public static final String LIGHT_LIGHT = "light-light";
	public static final String LIGHT_HEAVY = "light-heavy";
	public static final String HEAVY_LIGHT = "heavy-light";
	public static final String DASHED = "dashed";
	public static final String DOTTED = "dotted";
	public static final String REPEAT = "repeat";
//	public static final String DIRECTION = "backup";
	public static final String FORWARD = "forward";
	public static final String BACKWARD = "backward";
	
	public static final String ARTICULATIONS = "articulations";
	public static final String STACCATO = "staccato";
	public static final String ACCENT = "accent";
	public static final String FERMATA = "fermata";
	public static final String TENUTO = "tenuto";
	public static final String DETACHED_LEGATO = "detached-legato";
	public static final String STRONG_ACCENT = "strong-accent";
	
	public static final String FONT_SIZE = "font-size";
	public static final String DEFAULT_Y = "default-y";
	public static final String DEFAULT_X = "default-x";
	public static final String RELATIVE_X = "relative-x";
	
	public static final String PAGE_LAYOUT = "page-layout";
	public static final String PAGE_HEIGHT = "page-height";
	public static final String PAGE_WIDTH = "page-width";
	
	
	public static final int DEFAULT_ANNOTATION_TEXT_SIZE = 7;
	public static final int LAYERED_ANNOTATION_INCREMENT = -22;
	public static final int ANNOTATION_BASELINE = -60;
	
	public static final String[] articluationArray = new String[]{
			"", STACCATO, ACCENT, DETACHED_LEGATO, TENUTO, FERMATA, STRONG_ACCENT};

	public static final int PORTRAIT = 0;
	public static final int LANDSCAPE = 1;
	
	public static final double A4_LONG = 1583.9;		// not sure what unit these are
	public static final double A4_SHORT = 1223.92;
	
	
	public static String getIndent(int i) {
//		System.out.println("indent i=" + i);
		String ret = "";
		for (int x = 0; x < i; x++){
			ret += " ";
		}
//		System.out.println("'" + ret + "'");
		return ret;
	}
	public static void makeQuickEntry(ArrayList<String> strList, String name, String[][] contentArr, String[][] paramArr, int indent){
		
		strList.add(makeHeader(indent, name, paramArr));
		for (String[] str: contentArr){
//			strList.add(getIndent(indent + indentIncrement) + LB + str[0] + RB + str[1] + LB + BS + str[0] + RB);
			addOneLiner(strList, str[0], str[1], indent + indentIncrement);
		}
		strList.add(getIndent(indent + indentIncrement) + LB + BS + name + RB);
	}
	public static void addOneLiner(ArrayList<String> strList, String name, String value, int indent){
		//___indent___<name>value<\name>
		strList.add(getIndent(indent) + LB + name + RB + value + LB + BS + name + RB);

	}
	public static void addOneLinerWithAttributes(ArrayList<String> strList, String name, String value, int indent, String[][] attribArr){
		//___indent___<name attrib1="attrib1value" etc....>value<\name>
		String str = getIndent(indent) + LB + name; // + " " + attribName + "=\"" + attribValue + "\"" + BS + RB;
		for (String[] strArr: attribArr){
			str += " " + strArr[0] + "=\"" + strArr[1] + "\"";
		}
		str += RB + value + LB + BS + name + RB;
		
		strList.add(str);
	}
	public static void addSingleEntry(ArrayList<String> strList, String name, int indent, boolean hasBackslash){
		// adds simple entry with possible slash i.e.  <name> or </name> (yes its not actually a backslash, need to change that)
		// ___indent___<(/)name>
		String str = getIndent(indent) + LB;
		if (hasBackslash) str += BS;
		str += name + RB;
		strList.add(str);
	}
//	public static void addSingleEntry(ArrayList<String> strList, String name, int indent, String[][] attribArr, boolean hasBackslash){
//		// adds simple entry with possible slash i.e.  <name> or </name> (yes its not actually a backslash, need to change that)
//		String str = getIndent(indent) + LB;
//		if (hasBackslash) str += BS;
//		str += name + RB;
//		strList.add(str);
//	}
	public static String makeHeader(int indent, String name, String[][] paramArr) {
		String ret = getIndent(indent);
		ret += LB + name;
		for (String[] str: paramArr){
			ret += " " + str[0] + "=\"" + str[1] +"\"";					 
		}
		ret += RB;
		return ret;
	}
	public static void addSingleEntryWithAttributesAndEndSlash(ArrayList<String> strList, String name, String[][] attribArr, int indent) {
		// ___indent___<name attrib1name="attrib1value" ..etc../>
		String str = getIndent(indent) + LB + name; // + " " + attribName + "=\"" + attribValue + "\"" + BS + RB;
		for (String[] strArr: attribArr){
			str += " " + strArr[0] + "=\"" + strArr[1] + "\"";
		}
		str += BS + RB;
		strList.add(str);
	}
	public static void addSingleEntryWithAttributes(ArrayList<String> strList, String name, String[][] attribArr, int indent) {
		// ___indent___<name attrib1name="attrib1value" ..etc..>
		String str = getIndent(indent) + LB + name; // + " " + attribName + "=\"" + attribValue + "\"" + BS + RB;
		for (String[] strArr: attribArr){
			str += " " + strArr[0] + "=\"" + strArr[1] + "\"";
		}
		str += RB;
		strList.add(str);
	}
	public static void addSingleEntryWithEndSlash(ArrayList<String> strList, String name, int indent) {
		// ___indent___<name/>
		String str = getIndent(indent) + LB + name + BS + RB;
		strList.add(str);
	}
	public static void addArticulationTypeEntry(ArrayList<String> strList, String name, String param, int indent){
		//___indent___<name>
		//___indent___indentincrement___<param/>
		//___indent___indentincrement___</name>
		addSingleEntry(strList, name, indent, false);
		addSingleEntryWithEndSlash(strList, param, indent + indentIncrement);
		addSingleEntry(strList, name, indent + indentIncrement, true);
	}
	public static final XMLNoteAppearance WHOLE = new XMLNoteAppearance("whole", 0);
	public static final XMLNoteAppearance DOTTED_HALF = new XMLNoteAppearance("half", 1);
	public static final XMLNoteAppearance HALF = new XMLNoteAppearance("half", 0);
	public static final XMLNoteAppearance DOTTED_QUARTER = new XMLNoteAppearance("quarter", 1);
	public static final XMLNoteAppearance QUARTER = new XMLNoteAppearance("quarter", 0);
	public static final XMLNoteAppearance DOTTED_EIGHTH = new XMLNoteAppearance("eighth", 01);
	public static final XMLNoteAppearance EIGHTH = new XMLNoteAppearance("eighth", 0);
	public static final XMLNoteAppearance DOTTED_SIXTEENTH = new XMLNoteAppearance("16th", 1);
	public static final XMLNoteAppearance SIXTEENTH = new XMLNoteAppearance("16th", 0);
	public static final String NOTE = "note";
	public static final String CHORD = "chord";
	public static final String REST = "rest";
	public static final String VOICE = "voice";
	public static final String TYPE = "type";

//	public static final XMLKey KEY_OF_C = new XMLKey("C", 0, 
//			new IntAndString[]{
//					new IntAndString(0, "C"),
//					new IntAndString(1, "C"),
//					new IntAndString(0, "D"),
//					new IntAndString(-1, "E"),
//					new IntAndString(0, "E"),
//					new IntAndString(0, "F"),
//					new IntAndString(1, "F"),
//					new IntAndString(0, "G"),
//					new IntAndString(1, "G"),
//					new IntAndString(0, "A"),
//					new IntAndString(-1, "B"),
//					new IntAndString(0, "B"),					
//					}
//			);

	public static final HashMap<Integer, XMLKey> xmlKeyMap = new HashMap<Integer, XMLKey>(){{
		put(0, new XMLKey("C", 0, 
				new IntAndString[]{
						new IntAndString(0, "C"),
						new IntAndString(1, "C"),
						new IntAndString(0, "D"),
						new IntAndString(-1, "E"),
						new IntAndString(0, "E"),
						new IntAndString(0, "F"),
						new IntAndString(1, "F"),
						new IntAndString(0, "G"),
						new IntAndString(1, "G"),
						new IntAndString(0, "A"),
						new IntAndString(-1, "B"),
						new IntAndString(0, "B"),					
						}
				));
		put(-5, new XMLKey("Db", -5, 
				new IntAndString[]{
						new IntAndString(0, "C"),
						new IntAndString(-1, "D"),
						new IntAndString(0, "D"),
						new IntAndString(-1, "E"),
						new IntAndString(0, "E"),
						new IntAndString(0, "F"),
						new IntAndString(-1, "G"),
						new IntAndString(0, "G"),
						new IntAndString(-1, "A"),
						new IntAndString(0, "A"),
						new IntAndString(-1, "B"),
						new IntAndString(0, "B"),					
						}
				));
		put(2, new XMLKey("D", 2, 
				new IntAndString[]{
						new IntAndString(0, "C"),
						new IntAndString(1, "C"),
						new IntAndString(0, "D"),
						new IntAndString(1, "D"),
						new IntAndString(0, "E"),
						new IntAndString(0, "F"),
						new IntAndString(1, "F"),
						new IntAndString(0, "G"),
						new IntAndString(1, "G"),
						new IntAndString(0, "A"),
						new IntAndString(-1, "B"),
						new IntAndString(0, "B"),					
						}
				));
		put(-3, new XMLKey("Eb", -3, 
				new IntAndString[]{
						new IntAndString(0, "C"),
						new IntAndString(-1, "D"),
						new IntAndString(0, "D"),
						new IntAndString(-1, "E"),
						new IntAndString(0, "E"),
						new IntAndString(0, "F"),
						new IntAndString(-1, "G"),
						new IntAndString(0, "G"),
						new IntAndString(-1, "A"),
						new IntAndString(0, "A"),
						new IntAndString(-1, "B"),
						new IntAndString(0, "B"),					
						}
				));
		put(4, new XMLKey("E", 4, 
				new IntAndString[]{
						new IntAndString(0, "C"),
						new IntAndString(1, "C"),
						new IntAndString(0, "D"),
						new IntAndString(1, "D"),
						new IntAndString(0, "E"),
						new IntAndString(1, "E"),
						new IntAndString(1, "F"),
						new IntAndString(0, "G"),
						new IntAndString(1, "G"),
						new IntAndString(0, "A"),
						new IntAndString(1, "A"),
						new IntAndString(0, "B"),					
						}
				));
		put(-1, new XMLKey("F", -1, 
				new IntAndString[]{
						new IntAndString(0, "C"),
						new IntAndString(1, "C"),
						new IntAndString(0, "D"),
						new IntAndString(-1, "E"),
						new IntAndString(0, "E"),
						new IntAndString(0, "F"),
						new IntAndString(1, "F"),
						new IntAndString(0, "G"),
						new IntAndString(1, "G"),
						new IntAndString(0, "A"),
						new IntAndString(-1, "B"),
						new IntAndString(0, "B"),					
						}
				));
		put(6, new XMLKey("F#", 6, 
				new IntAndString[]{
						new IntAndString(0, "C"),
						new IntAndString(1, "C"),
						new IntAndString(0, "D"),
						new IntAndString(1, "D"),
						new IntAndString(0, "E"),
						new IntAndString(1, "E"),
						new IntAndString(1, "F"),
						new IntAndString(0, "G"),
						new IntAndString(1, "G"),
						new IntAndString(0, "A"),
						new IntAndString(1, "A"),
						new IntAndString(0, "B"),					
						}
				));
		put(-6, new XMLKey("Gb", -6, 
				new IntAndString[]{
						new IntAndString(0, "C"),
						new IntAndString(-1, "D"),
						new IntAndString(0, "D"),
						new IntAndString(-1, "E"),
						new IntAndString(0, "E"),
						new IntAndString(0, "F"),
						new IntAndString(-1, "G"),
						new IntAndString(0, "G"),
						new IntAndString(-1, "A"),
						new IntAndString(0, "A"),
						new IntAndString(-1, "B"),
						new IntAndString(-1, "C"),					
						}
				));
		put(1, new XMLKey("G", 1, 
				new IntAndString[]{
						new IntAndString(0, "C"),
						new IntAndString(1, "C"),
						new IntAndString(0, "D"),
						new IntAndString(1, "D"),
						new IntAndString(0, "E"),
						new IntAndString(0, "F"),
						new IntAndString(1, "F"),
						new IntAndString(0, "G"),
						new IntAndString(1, "G"),
						new IntAndString(0, "A"),
						new IntAndString(-1, "B"),
						new IntAndString(0, "B"),					
						}
				));
		put(-4, new XMLKey("Ab", -4, 
				new IntAndString[]{
						new IntAndString(0, "C"),
						new IntAndString(-1, "D"),
						new IntAndString(0, "D"),
						new IntAndString(-1, "E"),
						new IntAndString(0, "E"),
						new IntAndString(0, "F"),
						new IntAndString(-1, "G"),
						new IntAndString(0, "G"),
						new IntAndString(-1, "A"),
						new IntAndString(0, "A"),
						new IntAndString(-1, "B"),
						new IntAndString(1, "C"),					
						}
				));
		put(3, new XMLKey("A", 3, 
				new IntAndString[]{
						new IntAndString(0, "C"),
						new IntAndString(1, "C"),
						new IntAndString(0, "D"),
						new IntAndString(1, "D"),
						new IntAndString(0, "E"),
						new IntAndString(1, "E"),
						new IntAndString(1, "F"),
						new IntAndString(0, "G"),
						new IntAndString(1, "G"),
						new IntAndString(0, "A"),
						new IntAndString(1, "A"),
						new IntAndString(0, "B"),					
						}
				));
		put(-2, new XMLKey("Bb", -2, 
				new IntAndString[]{
						new IntAndString(0, "C"),
						new IntAndString(-1, "D"),
						new IntAndString(0, "D"),
						new IntAndString(-1, "E"),
						new IntAndString(0, "E"),
						new IntAndString(0, "F"),
						new IntAndString(1, "F"),
						new IntAndString(0, "G"),
						new IntAndString(-1, "A"),
						new IntAndString(0, "A"),
						new IntAndString(-1, "B"),
						new IntAndString(0, "B"),					
						}
				));
		put(5, new XMLKey("B", 5, 
				new IntAndString[]{
						new IntAndString(1, "B"),
						new IntAndString(1, "C"),
						new IntAndString(0, "D"),
						new IntAndString(1, "D"),
						new IntAndString(0, "E"),
						new IntAndString(1, "E"),
						new IntAndString(1, "F"),
						new IntAndString(2, "F"),
						new IntAndString(1, "G"),
						new IntAndString(0, "A"),
						new IntAndString(1, "A"),
						new IntAndString(0, "B"),					
						}
				));
	}};
	public static final XMLKey KEY_OF_C = xmlKeyMap.get(0);
	public static final XMLKey KEY_OF_Db = xmlKeyMap.get(-5);
	public static final XMLKey KEY_OF_D = xmlKeyMap.get(2);
	public static final XMLKey KEY_OF_Eb = xmlKeyMap.get(-3);
	public static final XMLKey KEY_OF_E = xmlKeyMap.get(4);
	public static final XMLKey KEY_OF_F = xmlKeyMap.get(-1);
	public static final XMLKey KEY_OF_Fsharp = xmlKeyMap.get(6);
	public static final XMLKey KEY_OF_Gb = xmlKeyMap.get(-6);
	public static final XMLKey KEY_OF_G = xmlKeyMap.get(1);
	public static final XMLKey KEY_OF_Ab = xmlKeyMap.get(-4);
	public static final XMLKey KEY_OF_A = xmlKeyMap.get(3);
	public static final XMLKey KEY_OF_Bb = xmlKeyMap.get(-2);
	public static final XMLKey KEY_OF_B = xmlKeyMap.get(5);
	
	
	public static final String PITCH = "pitch";
	public static final String STEP = "step";
	public static final String ALTER = "alter";
	public static final String OCTAVE = "octave";
	public static final String DOT = "dot";
	public static final String YES = "yes";
	public static final String TIE = "tie";
	public static final String START = "start";
	public static final String END = "end";
	public static final String CONTINUE = "continue";
	public static final String NOTATIONS = "notations";
	public static final String STOP = "stop";
	public static final String TIED = "tied";


	public static XMLClef getPreferredClefFromName(String name){
		for (XMLClefOption xco: clefFromName){
			if (xco.isNameInOptionList(name)) return xco.clef();
		}
		return TREBLE_CLEF;
	}
	private static final XMLClef TREBLE_CLEF = new XMLClef("G", 2);
	private static final XMLClef BASS_CLEF = new XMLClef("F", 4);
	private static final XMLClefOption[] clefFromName = new XMLClefOption[]{
			new XMLClefOption(TREBLE_CLEF, 
					new String[]{"Keys", "Melody"}),
			new XMLClefOption(BASS_CLEF,
					new String[]{"Kik", "Snr", "Hat", "Bass"})
	};
	
	
	
	
	
	
	
	


	public static boolean closeEnough(double value1, double value2, double range) {
		if (value1 >= value2 - range && value1 <= value2 + range){
			return true;
		} else {
			return false;
		}
		
	}
	public static void addSingleEntryWithAttributesAndValue(ArrayList<String> strList, String name,
			StringPair[] stringPairs, String value, int indent) {
		String str = getIndent(indent) + LB;
		str += name;
		for (StringPair sp: stringPairs){
			str += " " + sp.str1 + "=\"" + sp.str2 + "\"";
		}
		str += RB + value + LB + BS + name + RB;
		strList.add(str);
	}
}
