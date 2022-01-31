package main.java.da_utils.udp.udp_receiver;

public class InstructionItem {

	
	public static final int isString = 0;
	public static final int isDouble = 1;
	public static final int isInt = 2;
	private static String[] typeName = new String[]{"isString", "isDouble", "isInt"};
	public static String[] typeShortName = new String[]{"s", "d", "i"};
	
	private double d;
	private int type = isString;
	private int i;
	private String str;

	
	public InstructionItem(String str){
		this.str = str;
		try {
			d = Double.parseDouble(str);
			type = isDouble;
		} catch (NumberFormatException nfe) {

		}
		try {
			i = Integer.parseInt(str);
			type = isInt;
		} catch (NumberFormatException nfe) {

		}
	}
	
	public InstructionItem(Object o){
		if (o instanceof String){
			str = (String)o;
		} else if (o instanceof Integer){
			type = isInt;
			i = (int)o;
		} else if (o instanceof Float){
			type = isDouble;
			d = (double)(float)o;
		} 
	}
	
	public int type(){
		return type;
	}
	
	public int getInt(){
		return i;
	}
	public double getDouble(){
		return d;
	}
	public String getString(){
		return str;
	}
	
	public String toString(){
		return typeName[type] + " " + toShortString();

	}
	
	public String toShortString(){
		String s = "";
		switch (type){
		case 0: s += this.str;
				break;
		case 1: s += d;
				break;
		case 2: s += i;
				break;
		}
		return s;
	}

	public boolean isString() {
		if (type == isString) return true;
		return false;
	}

	public boolean isInt() {
		if (type == isInt) return true;
		return false;
	}
}
