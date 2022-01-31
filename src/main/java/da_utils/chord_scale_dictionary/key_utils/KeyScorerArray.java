package main.java.da_utils.chord_scale_dictionary.key_utils;
import java.util.ArrayList;

import main.java.da_utils.ableton_live.ableton_live_clip.LiveClip;
import main.java.da_utils.chord_scale_dictionary.chord_analysis.ChordAnalysisObject;


public class KeyScorerArray {
	
	public KeyScorer[] ksArr;
	public ArrayList<ChordAnalysisObject> caoList = new ArrayList<ChordAnalysisObject>();
	public ArrayList<KeyArea> keyAreaList = new ArrayList<KeyArea>();
	
	private boolean takeFirst = false;	// if true, the first available keyArea is returned from getPrevailing Key, otherwise the last.....
	private String[] functionPriorityArray = new String[]{"I", "vi"};	// first choice functions
		
// instantiation ------------------------------------------------------------------------
	public KeyScorerArray(Key[] keyArr){
		init(keyArr);
	}
	public KeyScorerArray(Key[] keyArr, LiveClip lc){
		init(keyArr);
		if (lc.noteList.size() > 0){
//			if (lc.chunkArray.size() == 0){
////				lc.makeChordAnalysis("2n");					// 2n - magic for now, could become default_analysis_resolution late
//			}
//			Iterator<ChordAnalysisObject> caoIt = lc.chunkArray.chunkArrayIterator();
//			while (caoIt.hasNext()){
//				newChord(caoIt.next());
//			}
		}
	}
	private void init(Key[] keyArr){
		ksArr = new KeyScorer[keyArr.length * 12];		// 12 = 12 keys
		for (int i = 0; i < keyArr.length; i++){
			for (int j = 0; j < 12; j++){
				int index = j + i * 12;
				ksArr[index] = new KeyScorer(j, keyArr[i]);
			}
		}
	}
	
// publics ----------------------------------------------------------------------------------
	public void newChord(ChordAnalysisObject cao){
		caoList.add(cao);
		for (KeyScorer ks: ksArr){
			ks.addChordAnalysisObject(cao);
		}
	}
	
	public String toString(){
		String ret = "KeyScorerArray:\n";
		ArrayList<String[]> strList = new ArrayList<String[]>();
		addPrimaryChordToStringArrayList(strList);
		addFunctionNameToStringArray(strList);
		ret += makeSpacedStringOutput(strList);
		for (KeyArea ka: keyAreaList){
			ret = ret + ka.toString() + "\n";
		}
		return ret;
	}
	public String chordAnalysisToString(){
		
		return makeSpacedStringOutput(getChordAnalysisStringArrayList());
	}
	public ArrayList<String[]> getChordAnalysisStringArrayList(){
		ArrayList<String[]> strList = new ArrayList<String[]>();
		strList.add(chordStringArray());
		for (KeyArea ka: makeChordProgressionAnalysis()){
			strList.add(ka.toStringArray(caoList.size()));
		}
		return strList;
	}
	public void makeKeyAreas(){
		for (KeyScorer ks: ksArr){
			int keyIndex = ks.keyIndex;
			ArrayList<KeyArea> temp = ks.makeKeyAreas();
 			keyAreaList.addAll(ks.makeKeyAreas());
		}
	}
	public ArrayList<KeyArea> makeChordProgressionAnalysis(){
		int index = 0;
		ArrayList<KeyArea> kaList = new ArrayList<KeyArea>();
		while (index < caoList.size()){
			ArrayList<KeyArea> tempList = chooseLongestKeyFromIndex(index);
			if (tempList.size() > 0){
				index = tempList.get(0).endIndex + 1;
			}
			if (tempList.size() > 1){
				tempList = chooseFirstOccurenceOfFunction(tempList, functionPriorityArray);  
			}
			kaList.addAll(tempList);
		}		
		return kaList;
	}
	public KeyArea getPrevailingKey(double pos){
		KeyArea chosenKeyArea = new KeyArea();
		boolean hasKeyArea = false;
		if (caoList.size() > 0){
			ChordAnalysisObject lastCao = caoList.get(caoList.size() - 1);
			double modPos = pos % Math.round(lastCao.startPoint + lastCao.lengthInChunkArray);
			for (KeyArea ka: makeChordProgressionAnalysis()){
				for (ChordAnalysisObject cao: ka.caoList){
					double start = cao.startPoint;
					double end = cao.startPoint + cao.lengthInChunkArray;
					if (modPos >= cao.startPoint && modPos < cao.startPoint + cao.lengthInChunkArray){
						chosenKeyArea = ka;
						if (takeFirst){
							return chosenKeyArea;
						}
						hasKeyArea = true;
					}
				}
			}
		}		
		return chosenKeyArea;
	}
	
// privates -----------------------------------------------------------------------------------
	
	private ArrayList<KeyArea> chooseFirstOccurenceOfFunction(ArrayList<KeyArea> kaList, String[] optArr){
		// chooses the keyArea which shows the first appearance of chordFunction in optArr. if no
		// good choices can be made, the original kaList is returned.
		ArrayList<KeyArea> newList = new ArrayList<KeyArea>();
		for (String opt: optArr){
			int occurenceIndex = -1;
			for (KeyArea ka: kaList){
				for (int i = 0; i < ka.cfList.size(); i++){
					if (occurenceIndex == -1){
						if (ka.cfList.get(i).name.equals(opt)){
							occurenceIndex = i;
							newList.add(ka);
						}
					} else {
						if (ka.cfList.get(i).name.equals(opt)){
							if (i < occurenceIndex){
								newList.clear();
								newList.add(ka);
							} else if (i == occurenceIndex){
								newList.add(ka);
							}
						}
					}
				}
			}
			if (newList.size() > 0){
				break;
			}
		}
		if (newList.size() == 0){
			return kaList;
		} else {
			return newList;
		}
		
	}
	private ArrayList<KeyArea> chooseLongestKeyFromIndex(int index){	// does not work........
		ArrayList<KeyArea> newKAList = new ArrayList<KeyArea>();
		int tempLength = 0;
		for (KeyArea ka: keyAreaList){
			if (ka.startIndex <= index){
				if (ka.endIndex - index > tempLength){
					newKAList.clear();
					newKAList.add(ka);
					tempLength = ka.endIndex - index;
				} else if (ka.endIndex - index == tempLength){
					newKAList.add(ka);
				}
			}			
		}
		return newKAList;
	}
	private ArrayList<KeyArea> chooseFirstOccurenceOfTonic(ArrayList<KeyArea> kaList){
		ArrayList<KeyArea> newKAList = new ArrayList<KeyArea>();
		int indexOfTonic = kaList.get(0).cfList.size();
		for (KeyArea ka: kaList){
			int currentTonicIndex = ka.indexOfTonic();
			if(currentTonicIndex < indexOfTonic){
				newKAList.clear();
				newKAList.add(ka);
				indexOfTonic = currentTonicIndex;
			} else if (currentTonicIndex == indexOfTonic){
				newKAList.add(ka);
				indexOfTonic = currentTonicIndex;
			}
		}
		return newKAList;
	}
	private ArrayList<KeyArea> checkForAllInOneKey(){
		ArrayList<KeyArea> kaList = new ArrayList<KeyArea>();
		for (KeyArea ka: keyAreaList){
			if (ka.startIndex == 0 && ka.endIndex == caoList.size() - 1){
				kaList.add(ka);
			}
		}
		return kaList;
	}
	private void addFunctionNameToStringArray(ArrayList<String[]> strList){
		strList.add(chordStringArray());
		for (KeyScorer ks: ksArr){
			strList.add(ks.functionStringArray());
		}
	}
	private void addPrimaryChordToStringArrayList(ArrayList<String[]> strList){
		strList.add(chordStringArray());
		for (KeyScorer ks: ksArr){
			strList.add(ks.primaryScoreStringArray());
		}
	}
// makes a columnized String output out of an ArrayList of String[] items
	private String makeSpacedStringOutput(ArrayList<String[]> strList){
		int[] lengthArr = makeLengthArray(strList);
		String ret = "";
		for (String[] strArr: strList){			
				for (int i = 0; i < lengthArr.length; i++){
				if (strArr.length > i){
					ret = ret + spacedChordName(strArr[i], lengthArr[i]);
				} else {
					ret = ret + spacedChordName("", lengthArr[i]);
				}
			}
			ret = ret + "\n";
		}
		return ret;
	}
	private String spacedChordName(String name, int length){
		int extra = length - name.length();
		String space = "";
		if (extra > 0){
			for (int i = 0; i < extra; i++){
				space += " ";
			}
		}
		return name + space;
	}
	private int[] makeLengthArray(ArrayList<String[]> strList){
		int[] lengthArr = new int[strList.get(0).length];
		for (int i = 0; i < lengthArr.length; i++){
			int longest = 0;
			for (String[] strArr: strList){
				if (strArr.length > i){
//					int testlen = strArr[i].length();
					if (strArr[i].length() > longest){
						longest = strArr[i].length();
					}
				}
			}
			lengthArr[i] = longest + 1;
		}
		return lengthArr;
	}
	private String[] chordStringArray(){
		String [] ret = new String[caoList.size() + 1];
		ret[0] = "";
		int index = 1;
		for (ChordAnalysisObject cao: caoList){
			ret[index] = cao.chordToString();
			index++;
		}
		return ret;
	}
}
