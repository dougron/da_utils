package main.java.da_utils.udp.udp_utils;
import java.util.ArrayList;
/*
 * OSC message maker for upd message from java to max4live UPD in port
 * 
 * caveat: the double is cast to a float cos i can't get max to read a double. Not 
 * convinced this is a problem as the bulk of my doubles are a) not big and b) not big on the decimal front
 */

public class OSCMessMaker {

	private ArrayList<OSCAtom> atList = new ArrayList<OSCAtom>();
	private static final String address = "/";
	
	public OSCMessMaker(){
		
	}
	
	
	
	public void addItem(int i)
	{
		atList.add(new OSCAtom(i));
	}
	
	
	
	public void addItem(double d)
	{
		atList.add(new OSCAtom((float)d));
	}
	
	
	
	public void addItem(String s)
	{
		atList.add(new OSCAtom(s));
	}
	
	
	
	public void addItem(float f)
	{
		atList.add(new OSCAtom(f));
	}
	
	
	
	public void addItem(int i, int index)
	{
		atList.add(index ,new OSCAtom(i));
	}
	
	
	
	public void addItem(double d, int index)
	{
		atList.add(index ,new OSCAtom((float)d));
	}
	
	
	
	public void addItem(String s, int index)
	{
		atList.add(index ,new OSCAtom(s));
	}
	
	
	
	public void addItem(float f, int index)
	{
		atList.add(index ,new OSCAtom(f));
	}
	
	
	
	public byte[] getOSCMess()
	{
		String typeString = ",";			// header for type string in OSC format
		for (OSCAtom at: atList){
			typeString += at.stringFormat();
		}
		atList.add(0, new OSCAtom(typeString));
		atList.add(0, new OSCAtom(address));
		ArrayList<Byte> byteList = new ArrayList<Byte>();
		for (OSCAtom at: atList){
			for (byte b: at.getByteArray()){
				byteList.add(b);
			}
		}
		byte[] byteArr = new byte[byteList.size()];
		for (int i = 0; i < byteList.size(); i++){
			byteArr[i] = (byte)byteList.get(i);
		}
		return byteArr;
	}
	
	
	
	public String toString()
	{
		String ret = "";
		for (OSCAtom at: atList){
			ret = ret + at.toString() + "\n";
		}
		return ret;
	}
	
	
	
	public int length()
	{
		return atList.size();
	}
	
	
	/*
	 * this is used in junit testing, so expect something to break if you change it
	 */
	public String toShortString()
	{
		StringBuilder sb = new StringBuilder();
		for (OSCAtom at: atList)
		{
			sb.append(at.itemAsString() + " ");
		}
		return sb.toString();
	}
	
}
