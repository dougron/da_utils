package main.java.da_utils.oddball_utils.option_generator;

import java.util.ArrayList;

/*
 * generates all variations of options
 */
public class OptionGenerator {
	
	private static String[] hexArr = new String[] {
			"0", "1", "2", "3", "4", "5", "6", "7", "8", "9", "A", "B", "C", "D", "E", "F"
	};

	public static ArrayList<Integer[]> getOptions(int[] optionArr, int finalLength){
		ArrayList<Integer[]> list = new ArrayList<Integer[]>();
		ArrayList<Integer[]> tempList = new ArrayList<Integer[]>();
		tempList.add(new Integer[] {});
		while(true) {
			if (tempList.get(0).length == finalLength) {
				list = tempList;
				break;
			} else {
				tempList = addMore(tempList, optionArr);
			}
		}
		
		
		return list;
	}
	
	
	
	
	
	private static ArrayList<Integer[]> addMore(ArrayList<Integer[]> tempList, int[] optionArr) {
		ArrayList<Integer[]> list = new ArrayList<Integer[]>();
		for (Integer[] iarr: tempList) {
			for (int option: optionArr) {
				list.add(makeNewArr(iarr, option));
			}
		}
		return list;
	}





	private static Integer[] makeNewArr(Integer[] iarr, int option) {
		Integer[] arr = new Integer[iarr.length + 1];
		for (int i = 0; i < iarr.length; i++) {
			arr[i] = iarr[i];
		}
		arr[iarr.length] = option;
		return arr;
	}





	public static void main(String[] args) {
		ArrayList<Integer[]> list = OptionGenerator.getOptions(new int[] {0, 1, 2, 3},  4);
		for (Integer[] iarr: list) {
			System.out.println(makeString(iarr));
		}
		System.out.println(list.size() + " options");
	}





	public static String makeString(Integer[] iarr) {
		String str = "";
		for (Integer i: iarr) {
			str += "" + getHex(i);
		}
		return str;
	}





	private static String getHex(Integer i) {
		if (i > 15) {
			return "X";
			
		} else {
			return hexArr[i];
		}
		
	}
}
