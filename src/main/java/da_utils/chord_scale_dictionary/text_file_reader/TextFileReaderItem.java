package main.java.da_utils.chord_scale_dictionary.text_file_reader;
/**
 * wrapper for the value item  in the TextFileReader data HashMap
 * 
 * 
 * STILL TO COMPLETE:
 * 		floats in doDictData
 * 		testForInteger
 * 		testForString
 * 
 * 30 december 2012 - due to oversight, there are now two seperate toString paths
 * there are the format/rawdat/dictdata/errorLog-ToString and the toString which
 * does the whole batch in a slightly different format.(slightly better indentation, imo)
 * 
 */

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;


public class TextFileReaderItem {
	public String[][] format;
	public ArrayList<String[]> rawdata;
	public ArrayList<String> errorLog;
	public ArrayList<Map> dictdata;
	
	
	
	public void getFormatFromString(String fstr){
		String[] fplat = fstr.split(",");
		format = new String[fplat.length][2];
		resetAllData();				// new format implies new data
		for (int i = 0; i < fplat.length; i ++){
			String depar = fplat[i].replaceAll("\\)","");
			String[] tsp = depar.split("\\(");
			format[i][0] = tsp[0];
			format[i][1] = tsp[1];
		}
	}
	public String formatStringForDataFile(){
		String ret = "format:";
		for(int i = 0; i < format.length; i++){
			ret = ret + format[i][0] + "(" + format[i][1] + ")";
			if (i != format.length -1){
				ret += ",";
			}
		}
		return ret;
	}
	public Iterator<String[]> rawdataIterator(){
		return rawdata.iterator();
	}
	
	public String formatToString(int indent){
		return formatTextString(makeIndentString(indent));	
	}
	public String formatToString(){
		return formatTextString("");
	}
	public String rawdataToString(int indent){
		return rawDataTextString(makeIndentString(indent));
	}
	public String rawdataToString(){
		return rawDataTextString("");
	}
	public String dictDataToString(int indent){
		return dictDataTextString(makeIndentString(indent));
	}
	public String dictDataToString(){
		return dictDataTextString("");
	}
	
	public int formatLength(){
		return format.length;
	}
	public String addToRawData(String str){
		String[] splt = str.split(",");
		if (splt.length != formatLength()){
			return "Data Line length error - too many or too few items: " + str;
		} else {
			rawdata.add(splt);
			return "";
		}
	}
	public String addToRawDataAndRedo(String str){
		String errorMessage = addToRawData(str);
		if (errorMessage == ""){
			redoDictData();
		}
		return errorMessage;
	}
	public String errorLogToString(int indent){
		String ret = "";
		if (errorLog.size() == 0){
			return ret + makeIndentString(indent) + "No errors.";
		} else {
			Iterator<String> it = errorLog.iterator();
			while (it.hasNext()){
				ret = ret + makeIndentString(indent) + it.next() + "\n";
			}
			return ret;
		}
 	}
	public void redoDictData(){
		dictdata = new ArrayList<Map>();
		doDictData();
	}
	public int getHighestIndex(){
		int index = 0;
		Iterator<Map> it = dictdata.iterator();
		while (it.hasNext()){
			Integer tempIndex = (Integer) it.next().get("index");
			if (tempIndex > index){
				index = tempIndex;
			}
		}
		return index;
	}
	
	public void doDictData(){
		Iterator<String[]> it = rawdata.iterator();
		while (it.hasNext()){
			String[] rdline = it.next();
			Map tempHash = new HashMap<String,Object>();
			for (int i = 0; i < format.length; i++){
				if (format[i][1].equals("int")){
					if (testForInteger(rdline[i])){
						tempHash.put(format[i][0], Integer.parseInt(rdline[i]));
					} else {
						errorLog.add("Error parsing " + rdline[i] + " as int");
					}
				} else if (format[i][1].equals("str")){
					tempHash.put(format[i][0], rdline[i]);
				} else if (format[i][1].equals("float")){
					if (testForFloat(rdline[i])){
						// do float stuff
					} else {
						errorLog.add("Error parsing " + rdline[i] + " as float");
					}
					
				}
			}
			dictdata.add(tempHash);
		}
	}
	public String toString(int indent){
		//return formatToString(indent) + rawdataToString(indent) + dictDataToString(indent) + errorLogToString(indent);
		return toStringMaker(indent);
	}
	public String toString(){
		return toStringMaker(0);
	}

	
 	private String toStringMaker(int indent){
		String indentStr = makeIndentString(indent);
		//----------------------------------------------------
		String ret = indentStr + "format:\n";
		for (int i = 0; i < format.length; i++){
			ret = ret + indentStr + "    " + format[i][0] + ", " + format [i][1] + "\n";
		}
		//----------------------------------------------------
		ret = ret + indentStr + "rawdata:\n";
		Iterator<String[]> it = rawdata.iterator();
		while (it.hasNext()){
			String[] temp = it.next();
			for (int i = 0; i < temp.length; i++){
				if (i == 0){
					ret = ret + indentStr + "    " + temp[i];
				} else {
					ret = ret + ", " + temp[i];
				}
				
			}
			ret += "\n";
		}
		//------------------------------------------------------
		ret = ret + indentStr + "dictdata:\n";
		Iterator<Map> it1 = dictdata.iterator();
		while (it1.hasNext()){
			Map temp = it1.next();
			Iterator<String> it2 = temp.keySet().iterator();
			ret = ret + indentStr + "    ";
			while (it2.hasNext()){
				String key = it2.next();
				ret = ret + key + ": " + temp.get(key) + ", ";
			}
			ret += "\n";
		}
		//---------------------------------------------------------
		ret = ret + indentStr + "errorLog:\n";
		if (errorLog.size() == 0){
			ret = ret + indentStr + "    no errors.\n";
		} else {
			Iterator<String> it3 = errorLog.iterator();
			while (it3.hasNext()){
				ret = ret + indentStr + "    " + it3.next();
			}
		}		
		return ret;
	}
	
	
	
	private Boolean testForInteger(String str){
		// should return true for positive or negative integers
		return true;
	}
	private Boolean testForFloat(String str){
		// returns true if str can be parsed into float. still to code
		return true;
	}
	
	private String makeIndentString(int indent){
		String indentStr = "";
		for (int i = 0; i < indent; i++){
			indentStr += " ";
		}
		return indentStr;
	}
	
 	private String rawDataTextString(String indentStr){
		String ret = "rawdata:\n";
		for (int i = 0; i < rawdata.size(); i++){
			String line = indentStr;
			for (int j = 0; j < rawdata.get(i).length; j++){
				line = line + rawdata.get(i)[j] + ", ";
			}
			ret = ret + line + "\n";
		}		
		return ret;
	}
 	
 	private String dictDataTextString(String indentStr){
		String ret = "dictdata:\n";
		for (int i = 0; i < dictdata.size(); i++){
			
			ret = ret + indentStr + dictdata.get(i) + "\n";
		}		
		return ret;
	}
	
	private String formatTextString(String indentStr){
		String ret = "Format data:\n";
		for (int i = 0; i < format.length; i++){
			ret = ret + indentStr + format[i][0] + ": " + format[i][0] + "\n";
		}
		return ret;
	}
	
	private void resetAllData(){
		rawdata = new ArrayList<String[]>();
		errorLog = new ArrayList<String>();
		dictdata = new ArrayList<Map>();
	}

}
