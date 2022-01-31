package main.java.da_utils.algorithmic_models.pipeline.pipeline_note_list;
import java.util.ArrayList;
import java.util.Collections;

import main.java.da_utils.ableton_live.ableton_live_clip.LiveClip;
import main.java.da_utils.ableton_live.ableton_live_clip.LiveMidiNote;
import main.java.da_utils.ableton_live.ableton_live_clip.controller_data_clips.ControllerClip;
import main.java.da_utils.ableton_live.ableton_live_clip.controller_data_clips.PitchBendClip;
import main.java.da_utils.algorithmic_models.pipeline.utilities.RenderNumber;



/*
 * wrapper for data that accumulates in the algorithm pipeline
 * 
 * this is the object that is passed to each item in an ArrayList<PipelinePlugIn> and
 * at the end can return an ArrayList<LiveClip>
 * 
 * as of 2019_04_12 I am going to assume that PipelineNoteObjects in pnoList do not overlap, but this 
 * will be settable on the noOverlaps flag
 */

public class PipelineNoteList {
	
	public ArrayList<PipelineNoteObject> pnoList = new ArrayList<PipelineNoteObject>();
	
	public ArrayList<ControllerClip> cList = new ArrayList<ControllerClip>();
	public int centreOfGravity;
	public double length;
	public PitchBendClip pb = null;
	public int clipObjectIndex = 0;
	public String trackName = "";
	public RenderNumber rn = new RenderNumber();
	
	private boolean noOverlaps = true;

	private boolean couldBeTieOverEndOfBar = false;

	
	public PipelineNoteList(int clipObjectIndex){
		this.clipObjectIndex = clipObjectIndex;
		//pb = new PitchBendClip(clipObjectIndex, length);		// pbClip is allowed to be null
	}
	
	public PipelineNoteList(){
		
	}
	
	public PipelineNoteList deepCopy() {
		PipelineNoteList pnl = this.cloneWithoutPNOList();
		for (PipelineNoteObject pno: pnoList){
			pnl.pnoList.add(pno);
		}
		for (ControllerClip cc: cList){
			pnl.cList.add(cc);
		}
		return pnl;
	}
	
	public void addNoteObject(double pos, boolean gt, boolean emb){
		pnoList.add(new PipelineNoteObject(pos, gt, emb));
		Collections.sort(pnoList, PipelineNoteObject.positionComparator);
	}
	
	public void addNoteObject(double pos, boolean gt, boolean emb, String descriptor){
		pnoList.add(new PipelineNoteObject(pos, gt, emb, descriptor));
		Collections.sort(pnoList, PipelineNoteObject.positionComparator);
	}
	
	public void addNoteObject(PipelineNoteObject pno){
		pnoList.add(pno);
		Collections.sort(pnoList, PipelineNoteObject.positionComparator);
	}
	
	public void add(ArrayList<PipelineNoteObject> pList){
		pnoList.addAll(pList);
		Collections.sort(pnoList, PipelineNoteObject.positionComparator);
	}
	
	public void addControllerClip(ControllerClip cc){
		cList.add(cc);
	}
	
	public String toString(){
		String ret = "PipelineNoteList:----------------------------------------\n";
		for (PipelineNoteObject pno: pnoList){
			ret = ret + pno.toString() + "\n";
		}	
		ret = ret + "Controller -----------------------------------------------\n";
		for (ControllerClip cc: cList){
			ret = ret + cc.toString();
		}
		ret += "PitchBend-------------------------------------------------\n";
		if (pb != null){
			ret += pb.toString();
		}
		return ret;
	}
//	public PipelineNoteList copy(){
//		PipelineNoteList copy = new PipelineNoteList(clipObjectIndex);
//		
//	}
	public void generatePrecedingSpaceValues(){
		for (int i = 0; i < pnoList.size(); i++){
			PipelineNoteObject pno = pnoList.get(i);
			PipelineNoteObject lastPNO;
			if (i == 0){
				lastPNO = pnoList.get(pnoList.size() - 1);
			} else {
				lastPNO = pnoList.get(i - 1);
			}
			pno.precedingSpaceInNoteList = pno.position - lastPNO.position;
			while (pno.precedingSpaceInNoteList < 0) pno.precedingSpaceInNoteList += length;
		}
	}
	
	public LiveClip makeLiveClip(){
		Collections.sort(pnoList, PipelineNoteObject.positionComparator);
		LiveClip lc = new LiveClip(0, 0);
		lc.length = length;
		if (pnoList.size() > 0){
			PipelineNoteObject pno1 = pnoList.get(0);
			if (pno1.position > 0){
				lc.setLoopStart(pno1.position);
				lc.setLoopEnd(pno1.position + length);
				lc.setStartMarker(length);
			} else {
				lc.setLoopStart(0.0);
				lc.setLoopEnd(length);
				lc.setStartMarker(0.0);
			}
		} else {
			lc.setLoopStart(0.0);
			lc.setLoopEnd(4.0);			// default bar length for 4/4 time
			lc.setStartMarker(0.0);
		}
		
		if (pb != null){
			lc.pb = pb;
		}
		
		lc.cList = cList;
	
		
		lc.name = trackName + rn.getRenderCount();
		lc.clipObjectIndex = clipObjectIndex;
		for (PipelineNoteObject pno: pnoList){
			lc.addNoteList(pno.noteList);
		}
		return lc;
	}
	
	public PipelineNoteList cloneWithoutPNOList(){
		PipelineNoteList pnl = new PipelineNoteList(clipObjectIndex);
		pnl.cList = cList;
		pnl.centreOfGravity = centreOfGravity;
		pnl.length = length;
		pnl.pb = pb;
		pnl.trackName = trackName;
		return pnl;
	}
	
	public String posListToString(){
		String ret = "PipelineNoteList posList:----\n";
		for (PipelineNoteObject pno: pnoList){
			ret += pno.position + ", " + pno.noteList.size() + " items ";
			for (LiveMidiNote lmn: pno.noteList){
				ret += lmn.note + ",";
			}
			ret += "\n";
		} 
		return ret;
	}
	
	public String posAndLengthToString(){
		String ret = "PipelineNoteList: length=" + length + "----------------------------------------\n";
		for (PipelineNoteObject pno: pnoList){
			ret = ret + pno.posAndLengthToString() + "\n";
		}	
		ret = ret + "Controller -----------------------------------------------\n";
		for (ControllerClip cc: cList){
			ret = ret + cc.toString();
		}
		return ret;
	}
	
	public boolean isSameAs(PipelineNoteList pnl){
		// test notes ------
		if (pnl.pnoList.size() != pnoList.size()){
			return false;
		} else {
			for (int i = 0; i < pnoList.size(); i++){
				if (!pnoList.get(i).isSameAs(pnl.pnoList.get(i))){
					return false;
				}
			}
		}
		// test pitchbend -------
		if (pb == null){
			if (pnl.pb != null){
				return false;
			}
		} else {
			if (pnl.pb != null){
				if (!pb.isSameAs(pnl.pb)){
					return false;
				}
			}
		}
		// test controllers ---------
		// still to code
		return true;
	}
	
	public boolean isSameAsIncludingVelocity(PipelineNoteList pnl){
		// test notes ------
		if (pnl.pnoList.size() != pnoList.size()){
			return false;
		} else {
			for (int i = 0; i < pnoList.size(); i++){
				if (!pnoList.get(i).isSameAsIncludingVelocity(pnl.pnoList.get(i))){
					return false;
				}
			}
		}
		// test pitchbend -------
		if (pb == null){
			if (pnl.pb != null){
				return false;
			}
		} else {
			if (pnl.pb != null){
				if (!pb.isSameAs(pnl.pb)){
					return false;
				}
			}
		}
		// test controllers ---------
		// still to code
		return true;
	}
	
	public void setNoOverlaps(boolean b){
		noOverlaps = b;
	}
	
	public boolean getNoOverlaps(){
		return noOverlaps;
	}

	public void dealWithOverlaps(){
		if (noOverlaps && pnoList.size() > 0){
			double firstPos = pnoList.get(0).position;
			if (firstPos > 0.0) couldBeTieOverEndOfBar  = true;
			for (int i = 0; i < pnoList.size(); i++){
				PipelineNoteObject thisNote = pnoList.get(i);
				if (i < pnoList.size() - 1){
					
					PipelineNoteObject nextNote = pnoList.get(i + 1);
					thisNote.length = nextNote.position - thisNote.position;
				} else {
					
					thisNote.length = length + firstPos - thisNote.position;
				}
				thisNote.setLengthsToLegatoModel();
			}
		}
	}
}
