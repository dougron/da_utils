package main.java.da_utils.ableton_live.max_time_format_database;
import java.util.Hashtable;


public class Database {
	
	public static Hashtable<String, Integer[]> maxTimeFormat = new Hashtable<String, Integer[]>();
	
	
	public Database(){
		makeMaxTimeFormat();
	}
	public int getTickValue(String rez){
		return maxTimeFormat.get(rez)[1];
	}
	public int getDenomValue(String rez){
		return maxTimeFormat.get(rez)[0];
	}
	
	private void makeMaxTimeFormat(){
		String[] keys = new String[]{"1n", "2n", "4n", "8n", "16n"};
		Integer[][] items = new Integer[][]{{1, 1920},{2, 960},{4, 480},{8, 240},{16, 120}};
		for (int i = 0; i < keys.length; i++){
			maxTimeFormat.put(keys[i], items[i]);
		}
	}

}
