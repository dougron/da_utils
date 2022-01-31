package main.java.da_utils.udp.udp_receiver;

import java.nio.ByteBuffer;
import java.util.ArrayList;

/*
 * wraps an ArrayList<Byte> with an int indicating the type according to the very obvious static variables
 */

public class TypedByteList {

	
	public static final int isString = 0;
	public static final int isInt = 1;
	public static final int isFloat = 2;
	
	public static String[] typeName = new String[] {"isString", "isInt", "isFloat"};
	
	ArrayList<Byte> list = new ArrayList<Byte>();
	int type;
	
	public void add(byte b){
		list.add(b);
	}
	
	public String toString(){
		String str = typeName[type] + "\t";
		for (Byte b: list){
			str += b + "\t";
		}
		return str;
	}
	
	public Object getAsObject(){
		if (type == isInt){
			return getInt();
		} else if (type == isFloat){
			return getFloat();
		} else {
			return getString();
		}
	}

	private Object getString() {
		return new String(listAsArray());
	}

	private byte[] listAsArray() {
		byte[] arr = new byte[list.size()];
		for (int i = 0; i < list.size(); i++){
			arr[i] = list.get(i);
		}
		return arr;
	}

	private Object getFloat() {
		return ByteBuffer.wrap(listAsArray()).getFloat();
	}

	private Object getInt() {

		return ByteBuffer.wrap(listAsArray()).getInt();
	}
	
	
// ===== main =================================================
	
	public static void main(String[] args){
//		String ex = "1234";
		int ex = 1234;
//		byte[] barr = ex.getBytes();
		TypedByteList tbl = new TypedByteList();
		tbl.type = isInt;
//		for (byte b: barr){
//			tbl.add(b);
//		}
		System.out.println(tbl.getAsObject());
	}
}
