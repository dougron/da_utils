package main.java.da_utils.chord_scale_dictionary;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

import main.java.da_utils.ableton_live.ableton_live_clip.LiveClip;
import main.java.da_utils.chord_scale_dictionary.datatypes.ChordFunction;
import main.java.da_utils.chord_scale_dictionary.datatypes.ChordType;
import main.java.da_utils.chord_scale_dictionary.datatypes.Inversion;
import main.java.da_utils.chord_scale_dictionary.datatypes.NotePattern;
import main.java.da_utils.chord_scale_dictionary.datatypes.NotePatternChord;
import main.java.da_utils.chord_scale_dictionary.datatypes.NotePatternScale;
import main.java.da_utils.chord_scale_dictionary.datatypes.ScaleType;
import main.java.da_utils.chord_scale_dictionary.key_utils.Key;
import main.java.da_utils.chord_scale_dictionary.test.ChordScaleDictionaryTest;
import main.java.da_utils.chord_scale_dictionary.text_file_reader.TextFileReader;

/*
 * class that manages the basic data......
 */
public class ChordScaleDictionary {
	
	public static String loadFile = "D:/Documents/DougzJavaz/DAUtils/src/ChordScaleDictionary/resources/csd.data";
	public static String saveFile = "D:/Documents/DougzJavaz/DAUtils/src/ChordScaleDictionary/resources/csd_new.data";
	
	public static Boolean debugMode = true;
	public ChordScaleDictionaryTest parent;
	public static TextFileReader tfr;
	public static HashMap<String,Map> qmodelByTimeFormat = new HashMap<String,Map>();
	public static HashMap<Integer[],NotePattern> notePattern = new HashMap<Integer[],NotePattern>();
	public static HashMap<String, NotePattern> notePatternStringIndex = new HashMap<String,NotePattern>();
	//public static HashMap<Integer,ArrayList<Chord>> chordByNotePatternIndex = new HashMap<Integer,ArrayList<Chord>>();
	public static ArrayList<ChordType> chordType = new ArrayList<ChordType>();
	public static HashMap<String, ChordType> chordTypeByName = new HashMap<String, ChordType>();
	public static ArrayList<ScaleType> scaleType = new ArrayList<ScaleType>();
	public static ArrayList<Inversion> inversion = new ArrayList<Inversion>();
	public static ArrayList<NotePatternChord> chord = new ArrayList<NotePatternChord>();
	public static ArrayList<NotePatternScale> scale = new ArrayList<NotePatternScale>();
	public static ArrayList<String> noteName = new ArrayList<String>();			// A, Bb etc
	public static ArrayList<String> degreeName = new ArrayList<String>();			// root, b9 etc
	public static ArrayList<Key> keyList = new ArrayList<Key>();
	public static ArrayList<ChordFunction> chordFunctionList = new ArrayList<ChordFunction>();
	
	public static boolean loaded = false;
	
	public ChordScaleDictionary(){
		debugMode = false;		// has to be false otherwise would be trying to consolePrint to non existent parent
		if (!loaded){
			loaded = true;
			initialize();
		} 			
	}
	public ChordScaleDictionary(ChordScaleDictionaryTest p){
		parent = p;
		if (!loaded){
			loaded = true;
			initialize();			
		} else {
			if (debugMode){parent.consolePrint("ChordScaleDictionary already loaded.)");}
		}
	}
	public static void analyzeChordProgression(LiveClip lc, String rez){
//		lc.makeChordAnalysis(rez);
		
	}
	public static int[] getScalePattern(String scaleName){
		// brute force search. make premade hashmap for quicker searching(maybe, don't really understand if this makes a difference or not)
		for (NotePatternScale nps: scale){
			if (nps.scaleType.scaleName.equals(scaleName)){
				return nps.scalePatternArr();
			}
		}
		return new int[0];
	}
	public static int[] getNotePattern(String chordName, int inversion){
		// brute force search. should use a pre made HashMap for time critical application
		for (NotePatternChord npc: chord){
			if (npc.chordType.chordName.equals(chordName)){
				return npc.notePatternArrInversion(inversion);				
			}
		}
		return new int[0];		
	}
	public static NotePattern getNotePattern(Integer[] np){
		// gets NotePattern from notePattern in Integer[] format
		Iterator<NotePattern> it = notePattern.values().iterator();
		while (it.hasNext()){
			NotePattern temp = it.next();
//			System.out.println(temp.notePatternString);
			if (Arrays.deepEquals(np, temp.notePatternArr)){
				return temp;
			}
		}
		return new NotePattern(-1);
	}
	public static NotePattern getNotePatternFromIndex(int index){
		Iterator<NotePattern> it = notePattern.values().iterator();
		while (it.hasNext()){
			NotePattern temp = it.next();
			if (temp.index == index){
				return temp;
			}
		}
		return new NotePattern(-1);
	}
	public static ChordType getChordType(String ct){
		if (chordTypeByName.keySet().contains(ct)){
			return chordTypeByName.get(ct);
		} else {
			return new ChordType(-1);
		}		
	}
	public static ScaleType getScaleType(String st){
		Iterator<ScaleType> it = scaleType.iterator();
		while (it.hasNext()){
			ScaleType temp = it.next();
			if (temp.scaleName.equals(st)){
				return temp;
			}
		}
		return new ScaleType(-1);
	}
	public static NotePatternChord getChordFromNotePattern(NotePattern np){
		Iterator<NotePatternChord> it = chord.iterator();
		while (it.hasNext()){
			NotePatternChord cd = it.next();
			if (cd.notePattern == np){
				return cd;
			}
		}
		return new NotePatternChord(-1);
	}
	public static Inversion getInversionFromIndex(int index){
		if (index >= inversion.size()){
			return new Inversion(-1);
		} else {
			return inversion.get(index);
		}
	}
	public static String getInversionNameFromIndex(int index){
		return getInversionFromIndex(index).name;
	}
	
	
	public void initialize(){
		tfr = new TextFileReader(loadFile);
		if (debugMode){parent.consolePrint(tfr.toString());}
		makeSpecialData();
	}
	public void addToRawData(String key, String item){
		//use this to add data to rawdata. dictdata gets rebuilt and the new data will be saved if
		// the ChordScaleDictionary is written to file......
		tfr.data.get(key).addToRawDataAndRedo(item);
		doSpecialDataKey(key);
	}
	public static String noteName(int index){
		return (String) tfr.data.get("note").dictdata.get(index % 12).get("name");
	}
	public static String noteIndexName(int index){
		return (String) tfr.data.get("note").dictdata.get(index % 12).get("indexName");
	}
	public static int qmodelOnTickValue(String q){
		if (qmodelByTimeFormat.keySet().contains(q)){
			return (Integer)qmodelByTimeFormat.get(q).get("ontick");
		} else {
			return -1;
		}	
	}
	public static int qmodelTickValue(String q){
		if (qmodelByTimeFormat.keySet().contains(q)){
			return (Integer)qmodelByTimeFormat.get(q).get("ontick") / (Integer)qmodelByTimeFormat.get(q).get("pulses");
		} else {
			return -1;
		}		
	}
	public static int chordIndexFromName(String chordName){
		for (int i = 0; i < chordType.size(); i++){
			String cn = chordType.get(i).chordName;
			if (chordName.equals(cn)){
				return i;
			}
		}
		return -1;
	}
	public void writeToSaveFile(){
		tfr.writeToDataFile(saveFile);
	}

// toString methods =====================================================================
	
	public String toString(){
		String ret = "ChordScaleDictionary special data structures:\n";
		ret = ret + qmodelToString();
		ret = ret + notePatternToString();
		ret = ret + chordTypeToString();
		ret = ret + chordTypeByNameToString();
		ret = ret + inversionToString();
		ret = ret + chordToString();
		ret = ret + notePatternStringIndexToString();
		ret = ret + keyToString();
		return ret;
	}
	private String keyToString(){
		String ret = "key:\n";
		for (Key k: keyList){
			ret = ret + "    " + k.toString() + "\n";
			for (ChordFunction cf: k.cfList){
				ret = ret + "        " + cf.toString() + "\n";
			}
		}
		return ret;
	}
	private String chordToString(){
		String ret = "chord:\n";
		Iterator<NotePatternChord> it = chord.iterator();
		while (it.hasNext()){
			ret = ret + "    " + it.next().toString() + "\n";
		}
		return ret;
	}
	private String inversionToString(){
		String ret = "inversion:\n";
		Iterator<Inversion> it = inversion.iterator();
		while (it.hasNext()){
			ret = ret + "    " + it.next().toString() + "\n";
		}
		return ret;
	}
	private String chordTypeToString(){
		String ret = "chordType:\n";
		Iterator<ChordType> it = chordType.iterator();
		while (it.hasNext()){
			ret = ret + "    " + it.next().toString() + "\n";
		}
		return ret;
	}
	private String chordTypeByNameToString(){
		String ret = "chordTypeByName:\n";
		for (String index: chordTypeByName.keySet()){
			ret = ret + "    " + index + stringSpacer(index.length(), 20) + chordTypeByName.get(index).toString() + "\n";
		}
		return ret;
	}
	private String stringSpacer(int subtract, int length){
		if (subtract >= length){
			return "";
		} else {
			String ret = "";
			for (int i = 0; i < length - subtract; i++){
				ret = ret + " ";
			}
			return ret;
		}
	}
	private String qmodelToString(){
		String ret = "qmodelByTimeFormat:\n";
		Iterator<String> it = qmodelByTimeFormat.keySet().iterator();
		while (it.hasNext()){
			String key = it.next();
			ret = ret + "    " + key + ":  ";
			Iterator<String> it1 = qmodelByTimeFormat.get(key).keySet().iterator();
			while (it1.hasNext()){
				String temp = it1.next();
				ret = ret + temp + ": " + qmodelByTimeFormat.get(key).get(temp) + ", ";
			}
			ret = ret + "\n";
		}
		return ret;
 	}
	private String notePatternToString(){
		Iterator<NotePattern> it = notePattern.values().iterator();
		String ret = "notePattern:\n";
		while (it.hasNext()){
			ret = ret + "    " + it.next().toString() + "\n";
		}
		return ret;
	}
	private String notePatternStringIndexToString(){
		Iterator<String> it = notePatternStringIndex.keySet().iterator();
		String ret = "notePatternStringIndex:\n";
		while (it.hasNext()){
			String key = it.next();
			ret = ret + "    " + key + ":  " + notePatternStringIndex.get(key).toString() + "\n";
		}
		return ret;
	}

// data organization ============================================================================
	
	private void makeSpecialData(){
		// makes extra data structures to make the info easy to get to
		String[] keySequence = new String[]{				// can't rely on the sequence of events from a keySet iterator....
			"note", "qmodel", "inversion", "notePattern", "chordType", "chord", "scaleType", "scale", "chordFunction", "key" 
		};
		for (String key: keySequence){
			doSpecialDataKey(key);				
		}
		attachNotePatternChordsToNotePattern();
	}
	private void attachNotePatternChordsToNotePattern(){
		Iterator<NotePattern> it = notePatternStringIndex.values().iterator();
		while (it.hasNext()){
			NotePattern np = it.next();
			Iterator<NotePatternChord> npcit = chord.iterator();
			while (npcit.hasNext()){
				NotePatternChord npc = npcit.next();
				if (np.index == npc.notePattern.index){
					np.chordOptions.add(npc);
				}
			}				
		}		
	}
	
	private void doSpecialDataKey(String key){
		if (key.equals("qmodel")){
			qmodelSpecialData();
		} else if (key.equals("notePattern")){
			notePatternSpecialData();
		} else if (key.equals("chordType")){
			doChordTypeSpecialData();
		} else if (key.equals("inversion")){
			doInversionSpecialData();
		} else if (key.equals("chord")){
			doChordSpecialData();
		} else if (key.equals("note")){
			doNoteSpecialData();
		} else if (key.equals("scaleType")){
			doScaleTypeSpecialData();
		} else if (key.equals("scale")){
			doScaleSpecialData();
		} else if (key.equals("key")){
			doKeySpecialData();
		} else if (key.equals("chordFunction")){
			doChordFunctionSpecialData();
		} 
	}
	private void doChordFunctionSpecialData(){
		Iterator<Map> it = tfr.data.get("chordFunction").dictdata.iterator();
		while (it.hasNext()){
			Map func = it.next();
			ChordFunction cf = new ChordFunction(
					(Integer)func.get("keyIndex"),
					(Integer)func.get("degree"),
					(String)func.get("chordType"),
					(String)func.get("name"),
					(Integer)func.get("prim_score"),
					(Integer)func.get("tonic"));
			chordFunctionList.add(cf);
		}
	}
	private void doKeySpecialData(){
		Iterator<Map> it = tfr.data.get("key").dictdata.iterator();
		while (it.hasNext()){
			Map key = it.next();
			Key k = new Key((Integer)key.get("index"), (String)key.get("name"), scale.get((Integer)key.get("scaleIndex")));
			keyList.add(k);
			addChordFunctions(k);
		}
	}
	private void addChordFunctions(Key k){
		for (ChordFunction cf: chordFunctionList){
			if (cf.keyIndex == k.index){
				k.addChordFunction(cf);
				cf.setKey(k);
			}
		}
	}
	private void doNoteSpecialData(){
		Iterator<Map> it = tfr.data.get("note").dictdata.iterator();
		while (it.hasNext()){
			Map np = it.next();
			noteName.add((String)np.get("name"));
			degreeName.add((String)np.get("indexName"));
			int i = 0;
		}		
	}
	private void doChordSpecialData(){
		Iterator<Map> it = tfr.data.get("chord").dictdata.iterator();
		while (it.hasNext()){
			Map inv = it.next();
			NotePatternChord npc = new NotePatternChord(
					(Integer)inv.get("index"), 
					(Integer)inv.get("notePatternIndex"), 
					(String)inv.get("chordType"), 
					(Integer)inv.get("inversion"));
			chord.add(npc);
		}
	}
	private void doScaleSpecialData(){
		Iterator<Map> it = tfr.data.get("scale").dictdata.iterator();
		while (it.hasNext()){
			Map inv = it.next();
//			Integer i1 = (Integer)inv.get("index");
//			Integer i2 = (Integer)inv.get("notePatternIndex");
//			String s1 = (String)inv.get("scaleType");
//			Integer i3 = (Integer)inv.get("startDegree");
//			NotePatternScale nps = new NotePatternScale(
//					(Integer)inv.get("index"), 
//					(Integer)inv.get("notePatternIndex"), 
//					(String)inv.get("scaleType"), 
//					(Integer)inv.get("startDegree"));
					
			scale.add(new NotePatternScale(
					(Integer)inv.get("index"), 
					(Integer)inv.get("notePatternIndex"), 
					(String)inv.get("scaleType"), 
					(Integer)inv.get("startDegree")));
			
		}
		int i = 1;
	}
	private void doInversionSpecialData(){
		Iterator<Map> it = tfr.data.get("inversion").dictdata.iterator();
		while (it.hasNext()){
			Map inv = it.next();
			inversion.add(new Inversion((Integer)inv.get("index"), (String)inv.get("name")));
		}
	}
	private void doChordTypeSpecialData(){
		Iterator<Map> it = tfr.data.get("chordType").dictdata.iterator();
		while (it.hasNext()){
			Map ct = it.next();
			ChordType c = new ChordType((Integer)ct.get("index"), (String)ct.get("name"), (Integer)ct.get("numberOfNotes"));
			chordType.add(c);
			chordTypeByName.put(c.chordName, c);
		}
	}
	private void doScaleTypeSpecialData(){
		Iterator<Map> it = tfr.data.get("scaleType").dictdata.iterator();
		while (it.hasNext()){
			Map st = it.next();
			scaleType.add(new ScaleType((Integer)st.get("index"), (String)st.get("name"), (Integer)st.get("numberOfNotes")));
		}
	}
	private void notePatternSpecialData(){
		Iterator<Map> it = tfr.data.get("notePattern").dictdata.iterator();
		while (it.hasNext()){
			Map np = it.next();
			NotePattern newnp = new NotePattern((Integer)np.get("index"), (String)np.get("notePattern"));
			notePattern.put(newnp.notePatternArr, newnp);
			notePatternStringIndex.put(newnp.notePatternString, newnp);
		}
	}
	private void qmodelSpecialData(){
		Iterator<Map> it = tfr.data.get("qmodel").dictdata.iterator();
		while (it.hasNext()){
			Map temp = it.next();
			qmodelByTimeFormat.put((String)temp.get("notevalue"), temp);
		}
	}
}
