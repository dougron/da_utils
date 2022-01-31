package main.java.da_utils.combo_variables;

import java.util.Comparator;

public class IntAndDouble {

	public int i;
	public double d;

	public IntAndDouble(int i, double d){
		this.i = i;
		this.d = d;
	}
	public String toString(){
		return i + ", " + d;
	}
	public String toString(String intName, String doubleName){
		return intName + "=" + i + ", " + doubleName + "=" + d;
	}
	public static Comparator<IntAndDouble> doubleComparator = new Comparator<IntAndDouble>(){

		@Override
		public int compare(IntAndDouble arg0, IntAndDouble arg1) {
			if (arg0.d < arg1.d) return -1;
			if (arg0.d > arg1.d) return 1;
			return 0;
		}
		
	};
}
