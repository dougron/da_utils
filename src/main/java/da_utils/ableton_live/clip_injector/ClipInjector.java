package main.java.da_utils.ableton_live.clip_injector;
/*
 * handles the clipObject outlet........
 */

import java.util.ArrayList;

import com.cycling74.max.Atom;

import LegacyStuff.ControllerInfo_DAPlayAlong;
import main.java.da_utils.ableton_live.ableton_device_control_utils.controller.ControllerInfo;
import main.java.da_utils.ableton_live.ableton_live_clip.LiveClip;
import main.java.da_utils.ableton_live.ableton_live_clip.LiveMidiNote;
import main.java.da_utils.ableton_live.ableton_live_clip.clip_info_object.ClipInfoObject;
import main.java.da_utils.ableton_live.ableton_live_clip.controller_data_clips.ControllerClip;
import main.java.da_utils.ableton_live.ableton_live_clip.controller_data_clips.FunctionBreakPoint;
import main.java.da_utils.ableton_live.ableton_live_clip.controller_data_clips.PitchBendClip;
import main.java.da_utils.ableton_live.max_atom_utils.DougzAtomUtilities;
import main.java.da_utils.algorithmic_models.pipeline.PipelineConsoleTest;
import main.java.da_utils.algorithmic_models.pipeline.max_objects.PipelineMaxTest;
import main.java.da_utils.algorithmic_models.pipeline.pipeline_note_list.PipelineNoteList;


public class ClipInjector {
	
//	public ClipInfoObject[] cioArr; 
	public ClipInjectorParent cParent;
//	public ClipInjectorMaxWrapper parent;
	public ClipInjectorConsoleTest parent1;
	public PipelineConsoleTest parent2;
	public PipelineMaxTest parent3;
//	public DAPlay parent4;
//	public PlugInBassFromRythmBufferConsoleTest parent5;
//	public PlayWithMeMaxTest parent6;
	public int parentIndex;

	public int clipObjectOutlet = 0;


	public ClipInjector(ClipInjectorParent p){
		cParent = p;
	}

	public void initializeClipObjects(ClipInfoObject[] cioArr){
		for (ClipInfoObject cio: cioArr){
			post(cio.toString());
			if (parentIndex == 3){
				parent3.post(DougzAtomUtilities.atomArrToString(makeInitAtomArray(cio)));
			}			
			sendMessage(makeInitAtomArray(cio));
		}
	}
	public void inject(LiveClip lc){
		ArrayList<Atom> atomList = new ArrayList<Atom>();
		atomList.add(Atom.newAtom(injectMessage));
		atomList.addAll(getLiveClipAtomList(lc));
		sendMessage(atomList.toArray(new Atom[atomList.size()]));
	}
	public void inject(ArrayList<LiveClip> lcList, ArrayList<ControllerClip> ccList, ArrayList<PitchBendClip> pbList){
		ArrayList<Atom> atomList = new ArrayList<Atom>();
		atomList.add(Atom.newAtom(injectMessage));
		for (LiveClip lc: lcList){
			atomList.addAll(getLiveClipAtomList(lc));
		}
		for (ControllerClip cc: ccList){
			atomList.addAll(getControllerClipAtomList(cc));
		}
		for (PitchBendClip pb: pbList){
			atomList.addAll(getPitchBendClipAtomList(pb));
		}
		sendMessage(atomList.toArray(new Atom[atomList.size()]));
	}
	public void inject(PipelineNoteList pnl){
//		post("ClipInjector.inject(PipelineNoteList pnl) called");
//		postSplit(pnl.toString(), "\n");
		
		ArrayList<Atom> atomList = new ArrayList<Atom>();
		atomList.add(Atom.newAtom(injectMessage));
		
//		LiveClip lc = pnl.makeLiveClip();
//		postSplit(lc.toString(), "\n");
		atomList.addAll(getLiveClipAtomList(pnl.makeLiveClip()));
		
		for (ControllerClip cc: pnl.cList){
			atomList.addAll(getControllerClipAtomList(cc));
		}
		if (pnl.pb == null){
			atomList.addAll(pitchBendOff(pnl.clipObjectIndex));			
		} else {
			atomList.addAll(getPitchBendClipAtomList(pnl.pb));
		}
		sendMessage(atomList.toArray(new Atom[atomList.size()]));
	}
	public void inject(LiveClip[] lcArr, ControllerClip[] ccArr, PitchBendClip[] pbArr){
		ArrayList<Atom> atomList = new ArrayList<Atom>();
		atomList.add(Atom.newAtom(injectMessage));
		for (LiveClip lc: lcArr){
			atomList.addAll(getLiveClipAtomList(lc));
		}
		for (ControllerClip cc: ccArr){
			atomList.addAll(getControllerClipAtomList(cc));
		}
		for (PitchBendClip pb: pbArr){
			atomList.addAll(getPitchBendClipAtomList(pb));
		}
		sendMessage(atomList.toArray(new Atom[atomList.size()]));
	}
	
//	notes message format: (from max4live)
//		1: i f f i i - x times for each note
//		2: i number of notes
//		3: i clipObjectIndex

	public ArrayList<Atom> getLiveClipAtomList(LiveClip lc){
		// format for injection, makes 'notes' and 'param' messages.....
		ArrayList<Atom> atList = new ArrayList<Atom>();
		// notes message
		atList.add(Atom.newAtom(notesMessage));
		atList.addAll(noteListToAtomList(lc));
		atList.add(Atom.newAtom(lc.noteList.size()));
		atList.add(Atom.newAtom(lc.clipObjectIndex));
		// param message
		atList.add(Atom.newAtom(paramMessage));
		atList.addAll(paramListFromClip(lc));
		return atList;
	}
	public Atom[] makeInitAtomArray(ClipInfoObject cio){
		ArrayList<Atom> atList = new ArrayList<Atom>();
		atList.add(Atom.newAtom("init"));
		atList.add((Atom.newAtom(cio.objectIndex)));
		atList.add((Atom.newAtom(cio.trackIndex)));
		atList.add((Atom.newAtom(cio.clipIndex)));
		atList.add((Atom.newAtom(cio.name)));
		for (ControllerInfo ci: cio.ccioArr){
			atList.addAll(ci.initAtomList());
		}
		return atList.toArray(new Atom[atList.size()]);
	}

	
	
// privates =======================================================================
	
	private ArrayList<Atom> pitchBendOff(int clipObjectIndex){
		ArrayList<Atom> atList = new ArrayList<Atom>();
		atList.add(Atom.newAtom(pitchbendMessage));
		atList.add(Atom.newAtom(clipObjectIndex));
		atList.add(Atom.newAtom(0));
		atList.add(Atom.newAtom(0.5));
		
		return atList;
	}
	
//	picthbend message format from max4live
//	1: i clipObjectIndex
//	2: i on/off
//	3: f off value (range 0. - 1.)
//	4: f length
//	5: f offset
//	6: s resolution
//	7: f depth (0. to 12. (semitones))
//	8: break point f f x y pairs
	
	private ArrayList<Atom> getPitchBendClipAtomList(PitchBendClip pb){
		ArrayList<Atom> atList = new ArrayList<Atom>();
		atList.add(Atom.newAtom(pitchbendMessage));
		atList.add(Atom.newAtom(pb.clipObjectIndex));
		atList.add(Atom.newAtom(pb.onOff));
		atList.add(Atom.newAtom(pb.offValue));
		atList.add(Atom.newAtom(pb.length));
		atList.add(Atom.newAtom(pb.offset));
		atList.add(Atom.newAtom(pb.resolution));
		atList.add(Atom.newAtom(pb.pitchBendRange));
		atList.addAll(getBreakPointPairsAtomList(pb.fbpList));		
		return atList;
	}
	
//	controller message format from max4live
//	1: i controllerIndex
//	2: i on/off
//	3: f off value (range 0. - 1.)
//	4: f length
//	5: f offset
//	6: s resolution
//	7: break point f f x y pairs
//	8: clipObjectIndex
	
	private ArrayList<Atom> getControllerClipAtomList(ControllerClip cc){
		ArrayList<Atom> atList = new ArrayList<Atom>();
		atList.add(Atom.newAtom(controllerMessage));
		atList.add(Atom.newAtom(cc.controllerIndex));
		atList.add(Atom.newAtom(cc.onOff));
		atList.add(Atom.newAtom(cc.offValue));
		atList.add(Atom.newAtom(cc.length));
		atList.add(Atom.newAtom(cc.offset));
		atList.add(Atom.newAtom(cc.resolution));
		atList.addAll(getBreakPointPairsAtomList(cc.fbpList));
		atList.add(Atom.newAtom(cc.clipObjectIndex));
		return atList;
	}
	private ArrayList<Atom> getBreakPointPairsAtomList(ArrayList<FunctionBreakPoint> fbpList){
		ArrayList<Atom> atList = new ArrayList<Atom>();
		for (FunctionBreakPoint bp: fbpList){
			atList.add(Atom.newAtom(bp.position));
			atList.add(Atom.newAtom(bp.value));
		}
		return atList;
	}
	
//	param arguments: from max4live
//		1:  f length
//		2:  f loopStart
//		3:  f loopEnd
//		4:  f startMaker
//		5:  f endMarker
//		6:  i sigNum
//		7:  i sigDenom
//		8:  f offset
//		9:  i clip
//		10: i track
//		11: s name
//		12: clipObjectIndex
	private ArrayList<Atom> paramListFromClip(LiveClip lc){
		ArrayList<Atom> atList = new ArrayList<Atom>();
		atList.add(Atom.newAtom(lc.length));
		atList.add(Atom.newAtom(lc.loopStart));
		atList.add(Atom.newAtom(lc.loopEnd));
		atList.add(Atom.newAtom(lc.startMarker));
		atList.add(Atom.newAtom(lc.endMarker));
		atList.add(Atom.newAtom(lc.signatureNumerator));
		atList.add(Atom.newAtom(lc.signatureDenominator));
		atList.add(Atom.newAtom(lc.offset));
		atList.add(Atom.newAtom(lc.clip));
		atList.add(Atom.newAtom(lc.track));
		atList.add(Atom.newAtom(lc.name));
		atList.add(Atom.newAtom(lc.clipObjectIndex)); 
		return atList;
	}
	private ArrayList<Atom> noteListToAtomList(LiveClip lc){		// for inject
		ArrayList<Atom> atList = new ArrayList<Atom>();
		for (LiveMidiNote lmn: lc.noteList){
			atList.add(Atom.newAtom(lmn.note));
			atList.add(Atom.newAtom(lmn.position));
			atList.add(Atom.newAtom(lmn.length));
			atList.add(Atom.newAtom(lmn.velocity));
			atList.add(Atom.newAtom(lmn.mute));
		}
		return atList;
	}
	private void postSplit(String str, String splitString){
		for (String s: str.split(splitString)){
			post(s);
		}
	}
		
	private void post(String str){
		cParent.consolePrint(str);
	}
	
	private void sendMessage(Atom[] atArr){
		cParent.sendClipObjectMessage(atArr);
	}

	private String injectMessage = "inject";
	private String notesMessage = "notes";
	private String paramMessage = "param";
	private String controllerMessage = "controller";
	private String pitchbendMessage = "pitchbend";
}
