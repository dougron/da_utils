package main.java.da_utils.combo_variables;

public class IntAndString {
	
	public int i;
	public String str;

	public IntAndString(int i, String s){
		this.i = i;
		this.str = s;
	}
	public String toString(){
		return "IntAndString: " + i + ", " + str;
	}
}
