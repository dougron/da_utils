package main.java.da_utils.resource_objects;
import com.cycling74.max.Atom;


public interface ResourceObjectParent {
	
	public void sendRhythmBufferMessage(Atom[] atArr);	
	public void sendChordProgrammerMessage(Atom[] atArr);
	
	public void consolePrint(String str);
	public void postSplit(String str, String splitter);
}
