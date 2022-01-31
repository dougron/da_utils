package main.java.da_utils.algorithmic_models.pipeline.plugins.pipeline_plugin;

import main.java.da_utils.ableton_live.ableton_live_clip.LiveClip;
import main.java.da_utils.resource_objects.AccentTemplate;
import main.java.da_utils.resource_objects.ChordForm;
import main.java.da_utils.resource_objects.ContourData;
import main.java.da_utils.resource_objects.ContourPack;
import main.java.da_utils.resource_objects.RandomNumberSequence;
import main.java.da_utils.resource_objects.TwoBarRhythmBuffer;

/*
 * carries diverse data into a PlayletPlugIn object......
 * 
 * mostly connects all PlayletObjects in a rendering chain to the Resource Layer
 * modules that the Generator at the head of the chain in connected to
 */
public class PlayPlugArgument {
	
	public TwoBarRhythmBuffer rb;		// rhythm buffer
	public TwoBarRhythmBuffer ib;		// interlock buffer
	public ContourData cd;
	public ContourPack cp;
	public ChordForm cf;
	public AccentTemplate at;
	public RandomNumberSequence rnd; 
	public LiveClip lc;
	public int dynamic;			// currently range 0 - 3. accessed statically from PlayletObject
								// added for back compatibility with DAPlay......
	public int velocity;
	public int clipObjectIndex;
	public int numberOfEmbellishments;	// for use with the PlugInSizeAndLengthContextEmbellisher
	public double barlength = 4.0;		// assumes 4/4 time. this should be made the same as the static variable of the same name from PlayletObject class		

	public PlayPlugArgument(){
		
	}
	public PlayPlugArgument deepCopy() {
		PlayPlugArgument ppa = new PlayPlugArgument();
		ppa.rb = rb;
		ppa.ib = ib;
		ppa.cd = cd;
		ppa.cp = cp;
		ppa.cf = cf;
		ppa.at = at;
		ppa.rnd = rnd;
		ppa.lc = lc;
		ppa.dynamic = dynamic;
		ppa.velocity = velocity;
		ppa.clipObjectIndex = clipObjectIndex;
		ppa.numberOfEmbellishments = numberOfEmbellishments;
		ppa.barlength = barlength;
		return ppa;
	}
	
	public boolean hasContour(){
		if (cp != null) return true; else return false;
	}
	
	public boolean hasChordProgression(){
		if (cf != null) return true; else return false;
	}
	
	public boolean hasRhythmBuffer(){
		if (rb != null) return true; else return false;
	}
	
	public boolean hasInterlockBuffer(){
		if (rb != null) return true; else return false;
	}
	
	public String toString(){
		String ret = ("PlayPlugArgument------------------\n");
		ret += addRet(rb, "rb");
		ret += addRet(at, "at");
		ret += addRet(ib, "ib");
		ret += addRet(cd, "cd");
		ret += addRet(cp, "cp");
		ret += addRet(cf, "cf");
		ret += addRet(rnd, "rnd");
		return ret;
	}
	private String addRet(Object o, String name){
		String ret = "";
		if (o != null){
			ret += "active " + name + "\n";
		} else {
			ret += "null " + name + "\n";
		}
		return ret;
	}
	
	
	
}
