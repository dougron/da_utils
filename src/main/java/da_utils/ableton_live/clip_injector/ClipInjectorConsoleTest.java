package main.java.da_utils.ableton_live.clip_injector;
import java.util.ArrayList;

import com.cycling74.max.Atom;

import acm.program.ConsoleProgram;
import main.java.da_utils.ableton_live.ableton_live_clip.clip_info_object.ClipInfoObject;
import main.java.da_utils.ableton_live.max_atom_utils.DougzAtomUtilities;
import main.java.da_utils.test_utils.TestData;


public class ClipInjectorConsoleTest extends ConsoleProgram implements ClipInjectorParent{
	
	
	
	public void run(){
		setSize(700,700);
		ClipInjector ci = new ClipInjector(this);
		ClipInfoObject[] cioArr = TestData.makeClipInfoObjects();
		ci.initializeClipObjects(cioArr);					// initialization of clipObjects and controllers are working
		ci.inject(TestData.makeLCList(), TestData.makeCCList(), TestData.makePBList());
	}
	public void sendClipObjectMessage(Atom[] atArr){
		println(DougzAtomUtilities.atomArrToString(atArr));
	}
	public void consolePrint(String str){
		println(str);
	}
	
// privates ------------------------------------------------------------------------------	
	

}


































































