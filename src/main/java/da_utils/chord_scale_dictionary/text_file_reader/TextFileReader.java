package main.java.da_utils.chord_scale_dictionary.text_file_reader;
/**
 * generic textfile reader. 
 * key words:
 * 	table: - precedes name of new hash map key
 * 	format:poopy(int),doggy(str),..... - indicates name and type of data in text file
 * 
 * 1,somestring,27,9.9999 - line of data, becomes data
 * 
 * HashMap data format in Python syntax:
 * {<tablename> : {'format': (('poopy','str'),....),
 * 					'rawdata':(('1','somestring','27','9.9999'),....
 * 					'dictdata'((HashMap: {'format[0][0]' = 1,
 * 											'format[1][0]' = somestring,........}))
 * 
 * Still need to complete the error testing stuff in TextFileReaderItem
 * 
 * 
 * by doug
 *
 */

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;

public class TextFileReader {
	public HashMap<String,TextFileReaderItem> data;
	public ArrayList<String> errorLog;
	
	public TextFileReader(String filepath){
		data = new HashMap<String,TextFileReaderItem>();
		errorLog = new ArrayList<String>();
		
		ArrayList<String> lineList = getLines(filepath);
		doRawData(lineList);
		doDictData();
		
		
	}
	
	public HashMap<String,TextFileReaderItem> hashdata(){
		return data;
	}
	
	public TextFileReaderItem dataItem(String key){
		return data.get(key);
	}
	
	public String errorLogToString(){
		String ret = "Error log for Reader:\n";
		if (errorLog.size() == 0){
			ret = ret + "No errors.";
		} else {
			Iterator<String> it = errorLog.iterator();
			while (it.hasNext()){
				ret = ret + it.next() + "\n";
			}
		}
		Iterator<String> it = data.keySet().iterator();
		while (it.hasNext()){
			String key = it.next();
			ret = "Errors for " + key + "\n" + data.get(key).errorLogToString(4);
		}
		return ret;
 	}
	public String toString(){
		String ret = "TextFileReader data........................\n";
		Iterator<String> it = data.keySet().iterator();
		while (it.hasNext()){
			String key = it.next();
			ret = ret + "\n------------------------------------------------\n";
			ret = ret + "table: " + key +"\n";
			ret = ret + data.get(key).toString(4);
		}
		ret = ret + "TextFileReader data complete!";
		return ret;
	}
	public void writeToDataFile(String file){
		try {
			BufferedWriter bw = new BufferedWriter(new FileWriter(file));
			Iterator<String> it = data.keySet().iterator();
			while (it.hasNext()){
				String key = it.next();
				//----------------------------------------------------------------
				bw.write("table:" + key);
				bw.newLine();
				//----------------------------------------------------------------
				bw.write(data.get(key).formatStringForDataFile());
				bw.newLine();
				bw.newLine();
				//----------------------------------------------------------------
				Iterator<String[]> rd = data.get(key).rawdataIterator();
				while (rd.hasNext()){
					String fline = "";
					String[] rdline = rd.next();
					for (int i = 0; i < rdline.length; i++){
						fline = fline + rdline[i];
						if (i != rdline.length - 1){
							fline += ",";
						}
					}
					bw.write(fline);
					bw.newLine();
				}
				bw.newLine();
			}
			bw.close();
		} catch (Exception e){
			
		}
		
	}

	private void doDictData(){
		Iterator<TextFileReaderItem> it = data.values().iterator();
		while (it.hasNext()){
			it.next().doDictData();
		}
	}	
	private ArrayList<String> getLines(String filepath){
		ArrayList<String> lineList = new ArrayList<String>();
		try{
			BufferedReader reader = new BufferedReader(new FileReader(filepath)); 
			String line = null;
			
			while ((line = reader.readLine()) != null){
				if (line.length() > 0){
					lineList.add(line);
				}
			}
			reader.close();
		} 
		catch (IOException x){
			lineList.add("An error was encountered and the reader did not complete");			
		}
		return lineList;		
	}			
	private void doRawData(ArrayList<String> llist){
		String key = "";
		Iterator<String> it = llist.iterator();
		while (it.hasNext()){
			String line = it.next();
			if (line.charAt(0) != '/'){		// ignore comment
				String[] splat = line.split(":");
				if (splat.length == 2){
					key = dealWithKeyWordLine(splat, key);					
				} else if (splat.length == 1){
					dealWithDataLine(splat[0], key);
				}
			}
		}
	}	
	private String dealWithKeyWordLine(String[] splat, String key){
		if (splat[0].equals("table")){
			key = splat[1];
			data.put(key, new TextFileReaderItem());
		} else if (splat[0].equals("format")){
			data.get(key).getFormatFromString(splat[1]);
		} else {
			errorLog.add("Unknown item in keyword position: " + splat[0]);
		}
		return key;
	}
	private void dealWithDataLine(String splat, String key){
		String error = data.get(key).addToRawData(splat);
		if (error != ""){
			errorLog.add(error);
		}
	}
	
}
