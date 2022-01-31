package main.java.da_utils.ableton_live.clip_injector;
import java.util.ArrayList;

import com.cycling74.max.Atom;
import com.cycling74.max.DataTypes;
import com.cycling74.max.MaxObject;

import main.java.da_utils.test_utils.TestData;


public class ClipInjectorMaxWrapper extends MaxObject implements ClipInjectorParent{
	
//	public ChordScaleDictionary csd = new ChordScaleDictionary();
	public int clipObjectOutlet = 0;
	public ClipInjector ci = new ClipInjector(this);
	
	
	public ClipInjectorMaxWrapper(){
		post ("initializing........");
		setMaxInlets();
		post ("done!");
	}
	public void initClipObjects(){
		ci.initializeClipObjects(TestData.makeClipInfoObjects());
	}
	public void testInject(){
		ci.inject(TestData.makeLCList(), TestData.makeCCList(), TestData.makePBList());
	}
	public void sendClipObjectMessage(Atom[] atArr){
		outlet(clipObjectOutlet, atArr);
	}
	public void consolePrint(String str){
		post(str);
	}

	
// privates ===================================================================
	

	private void postSplit(String str, String splitter){
		String[] strArr = str.split(splitter);
		for (String s: strArr){
			post(s);
		}
	}

	private void setMaxInlets(){
		declareInlets(new int[]{
				DataTypes.ALL,  
				DataTypes.INT}
		);
		declareOutlets(new int[]{ 
				DataTypes.ALL, 
				DataTypes.ALL}
		);
		setInletAssist(new String[]{
			"messages in",
			"erm...."}
		);
		setOutletAssist(new String[]{
			"clipObject messages out",
			"dump out"}
		);
	}
}
