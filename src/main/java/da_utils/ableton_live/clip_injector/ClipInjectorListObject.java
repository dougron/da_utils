package main.java.da_utils.ableton_live.clip_injector;

import com.cycling74.max.Atom;

public class ClipInjectorListObject {

	
	int i;
	double d;
	String str;
	boolean isInt = false;
	boolean isDouble = false;
	boolean isString = false;

	public ClipInjectorListObject(int i){
		this.i = i;
		isInt = true;
	}
	public ClipInjectorListObject(double d){
		this.d = d;
		isDouble = true;
	}
	public ClipInjectorListObject(String str){
		this.str = str;
		isString = true;
	}
	public Atom getAtom(){
		if (isInt){
			return Atom.newAtom(i);
		} else if (isDouble){
			return Atom.newAtom(d);
		} else if (isString){
			return Atom.newAtom(str);
		}
		return Atom.newAtom("poopy");
	}
	public int getI (){
		return i;
	}
	public void setI (int i){
		this.i = i;
	}
	public double getD (){
		return d;
	}
	public void setD (double d){
		this.d = d;
	}
	public String getStr (){
		return str;
	}
	public void setStr (String str){
		this.str = str;
	}
	public boolean isInt (){
		return isInt;
	}
	public void setInt (boolean isInt){
		this.isInt = isInt;
	}
	public boolean isDouble (){
		return isDouble;
	}
	public void setDouble (boolean isDouble){
		this.isDouble = isDouble;
	}
	public boolean isString (){
		return isString;
	}
	public void setString (boolean isString){
		this.isString = isString;
	}

}
