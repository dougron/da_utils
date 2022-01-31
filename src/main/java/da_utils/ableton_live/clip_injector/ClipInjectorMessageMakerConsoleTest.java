package main.java.da_utils.ableton_live.clip_injector;

import java.util.ArrayList;

import com.cycling74.max.Atom;

import acm.program.ConsoleProgram;
import main.java.da_utils.ableton_live.ableton_live_clip.LiveClip;

public class ClipInjectorMessageMakerConsoleTest{

	
	public ClipInjectorMessageMakerConsoleTest(){

//		testOne();
		
		testTwo();
	}

	private void testTwo ()
	{
		LiveClip lc = makeLiveClip();
		int trackIndex = 1;
		ArrayList<ClipInjectorListObject> ciloList = ClipInjectorMessageMaker.getInjectLiveClipMessage(lc, trackIndex);
		for (ClipInjectorListObject cilo: ciloList)
		{
			System.out.println(cilo.toString());
			
		}
		
	}

	public void testOne ()
	{
		LiveClip lc = makeLiveClip();
		int trackIndex = 1;
		ArrayList<ClipInjectorListObject> ciloList = ClipInjectorMessageMaker.getInjectLiveClipMessage(lc, trackIndex);
		Atom[] atArr = new Atom[ciloList.size()];
		for (int i = 0; i < ciloList.size(); i++){
			ClipInjectorListObject cilo = ciloList.get(i);
			if (cilo.isInt) atArr[i] = Atom.newAtom(cilo.i);
			if (cilo.isDouble) atArr[i] = Atom.newAtom(cilo.d);
			if (cilo.isString) atArr[i] = Atom.newAtom(cilo.str);
		}
	}

	private LiveClip makeLiveClip() {
		LiveClip lc = new LiveClip(16.0, 0.0, 16.0, 0.0, 16.0, 4, 4, 0.0, 0, 1, "Melody", 0);
		lc.addNote(72, 0.0, 3.5, 100, 0);
		lc.addNote(72, 3.5, 0.5, 100, 0);
		lc.addNote(72, 4.0, 3.5, 100, 0);
		lc.addNote(60, 7.5, 4.5, 100, 0);
		return lc;
	}
	
	
	public static void main (String[] args)
	{
		new ClipInjectorMessageMakerConsoleTest();
	}
}
