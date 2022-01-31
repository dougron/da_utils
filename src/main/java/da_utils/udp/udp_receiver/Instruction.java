package main.java.da_utils.udp.udp_receiver;

import java.util.ArrayList;
import java.util.HashMap;

/*
 * copied from CombReComb as an intermediate class for the UPDReceiver messages
 */


public class Instruction {
	
	
	private static final int INDENTITY_INDEX_WHEN_NULL = -100;
	InstructionItem[] arr;
	private ArrayList<String> tempInstructionsList;
	private boolean isLoaded = true;
	int parameterImprint;			// e.g.  123 = 3 parameters (excluding initial) 1st is String, 2nd is double 3rd is int
									// has a smallish limit to the number of commands you can have without losing it
	String parameterIdentity;
	Integer identityIndex;
	
	public Instruction(String[] arr){
		this.arr = makeArr(arr);
		
		parameterImprint = makeTestValue();
		parameterIdentity = makeParameterIdentity();
	}
	
	public Instruction(ArrayList<Object> oList){
		this.arr = makeArrFromObjects(oList);
		
		parameterImprint = makeTestValue();
		parameterIdentity = makeParameterIdentity();
	}
	
	public Instruction(){
		this.tempInstructionsList = new ArrayList<String>();
		isLoaded = false;
	}
	
	public Instruction(boolean b) {
		// makes loaded empty Instruction
		this.arr = new InstructionItem[0];
		isLoaded = true;
	}
	
	public int identityIndex(){
		if (identityIndex == null) {
			return INDENTITY_INDEX_WHEN_NULL;
		} else {
			return identityIndex;
		}
	}


	private String makeParameterIdentity() {
		String str = "";
		for (InstructionItem ii: arr){
			str += InstructionItem.typeShortName[ii.type()];
		}
		return str;
	}
	
	
	public void addInstructionElement(String s){
		tempInstructionsList.add(s);
	}
	
	public void addInstructionElement(InstructionItem ii) {
		if (ii.type() == InstructionItem.isString){
			tempInstructionsList.add(ii.getString());
		} else if (ii.type() == InstructionItem.isDouble){
			tempInstructionsList.add("" + ii.getDouble());
		} else if (ii.type() == InstructionItem.isInt){
			tempInstructionsList.add("" + ii.getInt());
		}
		
	}
	
	public void finalizeInstruction(){
		
		if (tempInstructionsList.size() > 0) {
			this.arr = makeArr(tempInstructionsList);
			isLoaded = true;
			parameterImprint = makeTestValue();
			parameterIdentity = makeParameterIdentity();
		}
	}
	
	public String identifier(){
		if (length() > 0) {
			return arr[0].getString();
		} else {
			return "null";
		}
	}
	
	

	public boolean isLoaded(){
		return isLoaded ;
	}
	
	public String toString(){
		String str;
		if (isLoaded) {
			str = "Instruction: " + parameterImprint + " " + parameterIdentity + " ";
			for (InstructionItem dsci : arr) {
				str += dsci.toString() + ", ";
			} 
			if (identityIndex !=null){
				str += "\nidentityIndex=" + identityIndex;
			}
		} else {
			str = "Instruction isLoaded=false";
		}
		return str;
	}
	
	public String toShortString(){
		String str = "";
		if (isLoaded) {
			for (InstructionItem dsci : arr) {
				str += dsci.toShortString() + " ";
			} 
		} else {
			str = "Instruction isLoaded=false";
		}
		return str;
	}
	
	public int parameterImprint(){
		return parameterImprint;
	}
	
	public int parameterImprint(int length) {
		// from beginning to end of length
		if (arr.length >= length) {
			int pwr = length - 1;
			int imprint = 0;
			for (int i = 0; i < length; i++) {
				imprint += (arr[i].type() + 1) * Math.pow(10, pwr);
				pwr--;
			}
			return imprint;
		} else {
			return parameterImprint;
		}
		
	}
	
	public int parameterImprintFromIndex(int index){
		// from index to end of arr
		if (arr.length <= index){
			return 0;
		} else {
			int pwr = arr.length - index - 1;
			int imprint = 0;
			for (int i = index; i < arr.length; i++){
				imprint += (arr[i].type() + 1) * Math.pow(10, pwr);
				pwr--;
			}
			return imprint;
		}
	}
	
	public InstructionItem getItem(int index){
		return arr[index];
	}
	
	public ArrayList<Instruction> split(String[] keyWords, String identifier) {
		// adds the identifier to beginning of each instruction
		// includes all elements from beginning of intruction
		ArrayList<Instruction> list = new ArrayList<Instruction>();
		Instruction instruction = new Instruction();
//		instruction.addInstructionElement(identifier);
		boolean loadable = true;
		for (InstructionItem ii: arr){
			if (ii.type() == InstructionItem.isString){
				if (arrContains(keyWords, ii.getString())){
					instruction.finalizeInstruction();
					if (instruction.isLoaded()) list.add(instruction);
					instruction = new Instruction();
					instruction.addInstructionElement(identifier);
					loadable = true;
					instruction.addInstructionElement(ii);
				} else if (loadable){
					instruction.addInstructionElement(ii);
				}
			} else {
				instruction.addInstructionElement(ii);
			}
			
			
		}
		instruction.finalizeInstruction();
		if (instruction.isLoaded()) list.add(instruction);
		return list;
	}
	
	public ArrayList<Instruction> split(String[] keyWords) {
		ArrayList<Instruction> list = new ArrayList<Instruction>();
		Instruction instruction = new Instruction();
		boolean loadable = false;
		for (InstructionItem ii: arr){
			if (ii.type() == InstructionItem.isString){
				if (arrContains(keyWords, ii.getString())){
					instruction.finalizeInstruction();
					if (instruction.isLoaded()) list.add(instruction);
					instruction = new Instruction();
					loadable = true;
					instruction.addInstructionElement(ii);
				} else if (loadable){
					instruction.addInstructionElement(ii);
				}
			} else {
				instruction.addInstructionElement(ii);
			}
			
			
		}
		instruction.finalizeInstruction();
		if (instruction.isLoaded()) list.add(instruction);
		return list;
	}
	
	public ArrayList<Instruction> splitAndKeepID(String[] keyWords) {
		ArrayList<Instruction> list = new ArrayList<Instruction>();
		Instruction instruction = new Instruction();
		boolean loadable = false;
		for (InstructionItem ii: arr){
			if (ii.type() == InstructionItem.isString){
				if (arrContains(keyWords, ii.getString())){
					instruction.finalizeInstruction();
					if (instruction.isLoaded()) list.add(instruction);
					instruction = new Instruction();
					loadable = true;
					instruction.addInstructionElement(ii);
				} else if (loadable){
					instruction.addInstructionElement(ii);
				}
			} else {
				instruction.addInstructionElement(ii);
			}
			
			
		}
		instruction.finalizeInstruction();
		if (instruction.isLoaded()) list.add(instruction);
		return list;
	}
	
	public ArrayList<Instruction> split(String[] keyWords, String[][] exceptions) {
		ArrayList<Instruction> list = new ArrayList<Instruction>();
		if (length() == 0) {
			list.add(new Instruction());
		} else {
			Instruction instruction = new Instruction();
			boolean loadable = false;
			InstructionItem previousItem = null;
			for (InstructionItem ii: arr){
				if (ii.type() == InstructionItem.isString){
					if (arrContains(keyWords, ii.getString()) && !isException(ii.getString(), previousItem, exceptions)){
						instruction.finalizeInstruction();
						if (instruction.isLoaded()) list.add(instruction);
						instruction = new Instruction();
						loadable = true;
						instruction.addInstructionElement(ii);
					} else if (loadable){
						instruction.addInstructionElement(ii);
					}
					previousItem = ii;
				} else {
					instruction.addInstructionElement(ii);
					previousItem = null;
				}
				
				
			}
			instruction.finalizeInstruction();
			if (instruction.isLoaded()) list.add(instruction);
		}
		
		return list;
	}
	
	private boolean isException(String string, InstructionItem previousItem, String[][] exceptions) {
		if (previousItem != null){			
			for (String[] arr: exceptions){
				if (previousItem.getString().equals(arr[0]) && string.equals(arr[1])){
					return true;
				} 				
			}
			return false;
		} else {
			return false;
		}
		
	}

	public ArrayList<Instruction> split(HashMap<String, Integer> map) {
		ArrayList<Instruction> list = new ArrayList<Instruction>();
		Instruction instruction = new Instruction();
		boolean loadable = false;
		for (InstructionItem ii: arr){
			if (ii.type() == InstructionItem.isString){
				if (map.containsKey(ii.getString())){
					instruction.finalizeInstruction();
					
					if (instruction.isLoaded()) list.add(instruction);
					instruction = new Instruction();
					instruction.identityIndex = map.get(ii.getString());
					loadable = true;
					instruction.addInstructionElement(ii);
				} else if (loadable){
					instruction.addInstructionElement(ii);
				}
			} else {
				instruction.addInstructionElement(ii);
			}
			
			
		}
		instruction.finalizeInstruction();
		if (instruction.isLoaded()) list.add(instruction);
		return list;
	}

	public void removeFirstItem() {
		InstructionItem[] newArr = new InstructionItem[arr.length - 1];
		for (int i = 1; i < arr.length; i++){
			newArr[i -1] = arr[i];
		}
		arr = newArr;
		parameterImprint = makeTestValue();
		parameterIdentity = makeParameterIdentity();
		
	}
	
	public int length(){
		return arr.length;
	}
	
	public double[] getDoubleEndList(int index) {
		// creates a list of the end InstructionItems starting at index i. 
		// doubles remain doubles, ints convert to doubles, Strings are discarded
		ArrayList<Double> list = new ArrayList<Double>();
		for (int i = index; i < arr.length; i++){
			InstructionItem ii = arr[i];
			switch (ii.type()){
			case InstructionItem.isDouble: 	list.add(ii.getDouble());
											break;
			case InstructionItem.isInt: 	list.add((double)ii.getInt());
											break;			
			}
		}
		double[] darr = new double[list.size()];
		for (int i = 0; i < list.size(); i++){
			darr[i] = list.get(i);
		}
		return darr;
	}
	
	public int[] getIntEndList(int index) {
		// creates a list of the end InstructionItems starting at index i. 
		// ints remain ints, doubles truncated to ints, Strings are discarded
		ArrayList<Integer> list = new ArrayList<Integer>();
		for (int i = index; i < arr.length; i++){
			InstructionItem ii = arr[i];
			switch (ii.type()){
			case InstructionItem.isDouble: 	list.add((int)ii.getDouble());
											break;
			case InstructionItem.isInt: 	list.add(ii.getInt());
											break;			
			}
		}
		int[] iarr = new int[list.size()];
		for (int i = 0; i < list.size(); i++){
			iarr[i] = list.get(i);
		}
		return iarr;
	}

	
	
// privates -------------------------------------------------------------------------------
	
	
	
	private boolean arrContains(String[] keyWords, String string) {
		for (String keyWord: keyWords){
			if (string.equals(keyWord)) return true;
		}
		return false;
	}


	private int makeTestValue() {
		int pwr = 0;
		int value = 0;
		for (int i = arr.length - 1; i >= 0; i--){
			value += (arr[i].type() + 1) * Math.pow(10, pwr);
			
			pwr++;
		}
		return value;
	}
	
	private InstructionItem[] makeArr(String[] arr2) {
		InstructionItem[] newArr = new InstructionItem[arr2.length];
		for (int i = 0; i < arr2.length; i++){
			newArr[i] = new InstructionItem(arr2[i]);
		}
		return newArr;
	}
	
	private InstructionItem[] makeArr(ArrayList<String> list) {
		InstructionItem[] newArr = new InstructionItem[list.size()];
		for (int i = 0; i < list.size(); i++){
			newArr[i] = new InstructionItem(list.get(i));
		}
		return newArr;
	}
	
	private InstructionItem[] makeArrFromObjects(ArrayList<Object> list) {
		InstructionItem[] newArr = new InstructionItem[list.size()];
		for (int i = 0; i < list.size(); i++){
			newArr[i] = new InstructionItem(list.get(i));
		}
		return newArr;
	}


// === main --------------------------------------------------
	
	public static void main(String[] args){
//		Instruction i = new Instruction("bass add poopy".split(" "));
//		System.out.println(i.toString());
//		
//		Instruction i2 = new Instruction();
//		i2.addInstructionElement("bass");
//		i2.addInstructionElement("play");
//		i2.addInstructionElement("nice");
//		i2.finalizeInstruction();
//		
//		System.out.println(i2.toString());
		
//		String i1 = "this is a split test but could just test someting";
//		String[] keyWords = new String[]{"this", "split", "test"};
//		String[][] exceps = new String[][]{new String[]{"split", "test"}};
//		
//		Instruction ins = new Instruction(i1.split(" "));
//		System.out.println(ins.toShortString());
//		ArrayList<Instruction> list = ins.split(keyWords, exceps);
//		for (Instruction i: list){
//			System.out.println(i.toShortString());
//		}
		
		Instruction ins = new Instruction(false);
		System.out.println(ins.toString());
		System.out.println(ins.length());
		System.out.println(ins.identifier());
		
	}

	

	











}
