package main.java.da_utils.udp.udp_utils;
import java.nio.ByteBuffer;


public class OSCAtom {

	public int type;
	public int intValue;
	public float floatValue;
	public String stringValue;
	public double doubleValue;
	
	public OSCAtom(int i){
		type = INTEGER;
		intValue = i;
	}
	public OSCAtom(double d){
		type = DOUBLE;
		doubleValue = d;
	}
	public OSCAtom(String s){
		type = STRING;
		stringValue = s;
	}
	public OSCAtom(float f){
		type = FLOAT;
		floatValue = f;
	}
	public byte[] getByteArray(){
		if (type == INTEGER){
			return intToByteArray(intValue);
		} else if (type == DOUBLE){
			return doubleToByteArray(doubleValue);
		} else if (type == STRING){
			return stringToByteArray(stringValue);
		} else if (type == FLOAT){
			return floatToByteArray(floatValue);
		} else {
			return new byte[]{};
		}
	}
	public byte byteFormat(){
		return byteFormat[type];
	}
	public String stringFormat(){
		return stringFormat[type];
	}
	public String toString(){
		String ret = "OSCAtom: " + stringFormat[type] + " ";
		if (type == INTEGER) ret += intValue;
		if (type == FLOAT) ret += floatValue;
		if (type == STRING) ret += stringValue;
		if (type == DOUBLE) ret += doubleValue;
		return ret;
	}
	public String itemAsString(){
		if (type == INTEGER){
			return "" + intValue;
		} else if (type == DOUBLE){
			return "" + doubleValue;
		} else if (type == STRING){
			return stringValue;
		} else if (type == FLOAT){
			return "" + floatValue;
		} else {
			return "";
		}
	}
	
// privates --------------------------------------------------------------
	private byte[] floatToByteArray(float f){
		return ByteBuffer.allocate(4).putFloat(f).array();
	}
	private byte[] doubleToByteArray(double d){
		byte[] bytes = new byte[8];
		ByteBuffer.wrap(bytes).putDouble(d);
		return bytes;
	}
	private byte[] doubleToByteArrayOLD(double d){		// does not work yet
		byte[] output = new byte[8];
		long lng = Double.doubleToLongBits(d);
		for (int i = 0; i < 8; i++){
			output[i] = (byte)((lng >> ((7 - i) * 8)) & 0xff);
		}
		return output;
	}
	private byte[] stringToByteArray(String str){
		int len = str.length();
		int remainder = len % 4;
		byte[] mess = str.getBytes();
		byte[] bArr = new byte[len + 4 - remainder];
//		bArr[0] = 47;
		for (int i = 0; i < mess.length; i++){
			bArr[i] = mess[i];
		}
		return bArr;
	}
	private byte[] intToByteArray(int value){
		return new byte[]{
				(byte)(value >>> 24),
				(byte)(value >>> 16),
				(byte)(value >>> 8),
				(byte)value};

	}
	
	private static final int INTEGER = 0;
	private static final int FLOAT = 1;
	private static final int STRING = 2;
	private static final int DOUBLE = 3;
	
	private static final byte[] byteFormat = new byte[]{105, 102, 115, 100};
	private static final String[] stringFormat = new String[]{"i", "f", "s", "d"};
	
	// byte i - 105, f - 102, d - 100, s - 115
}
