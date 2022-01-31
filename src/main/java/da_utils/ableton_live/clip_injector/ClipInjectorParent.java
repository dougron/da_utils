package main.java.da_utils.ableton_live.clip_injector;
import com.cycling74.max.Atom;


public interface ClipInjectorParent {

	public void consolePrint(String str);
	public void sendClipObjectMessage(Atom[] atArr);
}
