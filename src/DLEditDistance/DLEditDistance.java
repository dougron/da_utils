package DLEditDistance;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

public class DLEditDistance {
	
	private ArrayList<Integer> startIndexList = new ArrayList<Integer>();
	private ArrayList<Integer> endIndexList = new ArrayList<Integer>();
	Map<Integer, String> sourceMap = new HashMap<Integer, String>();
	int nextMapKey = 0;
	public static String SEPARATOR = ",";

	public DLEditDistance(){
		
	}
	public void setString(String start, String end){
		reset();
		addToSource(start, startIndexList);
		addToSource(end, endIndexList);
	}
	public int getShortestEditDistance(String start, String end, String seperator){
		SEPARATOR = seperator;
		setString(start, end);
		return getShortestEditDistance();
	}
	public int getShortestEditDistance(){
		ArrayList<Integer> scoreList = new ArrayList<Integer>();
		int[] currentArr = new int[startIndexList.size() + 1];
		int[] previousArr = new int[startIndexList.size() + 1];
		int[][] testArr = new int[startIndexList.size() + 1][endIndexList.size() + 1];
		for (int endIndex = 0; endIndex < endIndexList.size() + 1; endIndex++){	
			for (int startIndex = 0; startIndex < startIndexList.size() + 1; startIndex++){			
				//System.out.println();
				scoreList.clear();
				if (endIndex == 0){
					if (startIndex == 0){
						scoreList.add(0);
					} else {
						scoreList.add(addScoreFromAbove(startIndex, currentArr));
					}
				} else {
					scoreList.add(addScoreFromLeft(startIndex, previousArr));
					if (startIndex > 0){
						scoreList.add(addScoreFromDiagnol(startIndex, endIndex, previousArr));
						scoreList.add(addScoreFromAbove(startIndex, currentArr));
					}
				}
				Collections.sort(scoreList);
				currentArr[startIndex] = scoreList.get(0);
				testArr[startIndex][endIndex] = scoreList.get(0);
			}
			for (int i = 0; i < previousArr.length; i++){
				previousArr[i] = currentArr[i];
			}
		}
		//System.out.println(testArrToString(testArr));
		return scoreList.get(0);
	}
	public String toString(){
		String ret = "start:\n";
		ret += listToString(startIndexList);
		ret += "end:\n";
		ret += listToString(endIndexList);
		return ret;
	}
	public String listToString(ArrayList<Integer> list){
		String ret = "";
		for (int index: list){
			ret += index + ": " + sourceMap.get(index) + "\n";
		}
		return ret;
	}
	
	
// privates =============================================================================
	private int addScoreFromDiagnol(int startIndex, int endIndex, int[] previousCol){
		if (endIndexList.get(endIndex - 1) == startIndexList.get(startIndex - 1)){
			return previousCol[startIndex - 1];
		} else {
			return previousCol[startIndex - 1] + 1;
		}
	}
	private int addScoreFromLeft(int endIndex, int[] previousCol){
		return previousCol[endIndex] + 1;
	}
	private int addScoreFromAbove(int endIndex, int[] currentCol){
		return currentCol[endIndex - 1] + 1;
	}
	private void addToSource(String str, ArrayList<Integer> list){
		String[] strArr = str.split(SEPARATOR);
		for (String s: strArr){
			//System.out.println(s);
			int key = getSourceKey(s);
			//System.out.println("key=" + key);
			if (key == -1){
				sourceMap.put(nextMapKey, s);
				list.add(nextMapKey);
				nextMapKey++;
			} else {
				list.add(key);
			}
		}
	}
	private int getSourceKey(String str){
		for (int key: sourceMap.keySet()){
			if (sourceMap.get(key).equals(str)){
				return key;
			}
		}
		return -1;
	}
	private void reset(){
		nextMapKey = 0;
		sourceMap.clear();
		startIndexList.clear();
		endIndexList.clear();
	}
	private String testArrToString(int[][] arr){
		String ret = "testArr: \n";
		for (int y = 0; y < arr.length; y++){
			for (int x = 0; x < arr[y].length; x++){
				if (y == 0){
					if (x == 0){
						ret += getFixedLengthString("", 5);
					} else {
						ret += getFixedLengthString(sourceMap.get(endIndexList.get(x - 1)), 5);
					}
				} else {
					if (x == 0){
						ret += getFixedLengthString(sourceMap.get(startIndexList.get(y - 1)), 5);
					} else {
						getFixedLengthString("" + arr[y - 1][x - 1], 5);
					}
				}
			}
			ret += "\n";
		}
		return ret;
	}
	private String getFixedLengthString(String str, int length){
		String ret = str;
		int len = str.length();
		for (int i = 0; i < length - str.length(); i++){
			ret += " ";
		}
		return ret;
	}
}
