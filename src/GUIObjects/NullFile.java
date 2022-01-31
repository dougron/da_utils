package GUIObjects;

import java.io.File;

public class NullFile extends File {

	public NullFile(){
		super("xyz");
		
	}
	public String getName(){
		return "...";
	}
	public String getPath(){
		return "";
	}
}
