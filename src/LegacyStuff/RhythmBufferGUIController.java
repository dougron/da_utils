package LegacyStuff;
import java.util.ArrayList;

import com.cycling74.max.Atom;

import main.java.da_utils.resource_objects.TwoBarRhythmBuffer;


public class RhythmBufferGUIController {
	
	public int barDisplay = 0;			// which bar is being displayed on the LaunchPad
	public int bufferIndex = 0;			// which is the selected buffer
	public int duplicate = 0;			// 0 - no duplicates 1 - duplicates bar 1 input in bar 2 and vice versa
	private static final int shift_button_initial_value = 0;		// for initialization
	
	public TwoBarRhythmBuffer[] rb = new TwoBarRhythmBuffer[4];	// 0,1 is rhythmbuffer, 2,3 is related interlock buffer
	public String[] bufferName = new String[]{
			"RhythmBuffer 0    : ", 
			"RhythmBuffer 1    : ", 
			"InterlockBuffer 0 : ",
			"InterlockBuffer 1 : "
	};
	private static final String controlButtonMessage = "controlButton";
	private RBRandomInterlock ri = new RBRandomInterlock();
	
	public RhythmBufferGUIController(){
		makeBuffers();
	}
	public Atom[] controlButtonInitAtomArray(){
		ArrayList<Atom> atList = new ArrayList<Atom>();
		atList.add(Atom.newAtom(controlButtonMessage));
		atList.add(Atom.newAtom(duplicate));
		atList.add(Atom.newAtom(barDisplay));
		atList.add(Atom.newAtom(bufferIndex));
		atList.add(Atom.newAtom(shift_button_initial_value));
		return atList.toArray(new Atom[atList.size()]);
	}
	public void setBarDisplay(int i){
		barDisplay = i;
	}
	public void setBufferIndex(int i){
		bufferIndex = i;
	}
	public void duplicate(int i){
		duplicate = i;
	}
	public TwoBarRhythmBuffer getRhythmBuffer(){
		return rb[bufferIndex];
	}
	public TwoBarRhythmBuffer getInterlockBuffer(){
		return rb[bufferIndex + 2];
	}
	public Atom[] button(int button, int shift){
		return setBuffyItem(button, shift, (buffyItemFromButton(button, shift) + 1) % 2);		
	}
	public void generateActiveInterlockBuffer(){
		rb[this.bufferIndex + 2] = ri.makeInterlock(rb[this.bufferIndex]);
	}
	public void generateInactiveInterlockBuffer(){
		int index = (bufferIndex + 1) % 2;
		rb[index + 2] = ri.makeInterlock(rb[index]);
	}
	public void generateInterlockBuffer(int index){
		if (index >= 0 || index < 2){
			rb[index + 2] = ri.makeInterlock(rb[index]);
		}
	}
	public Atom[] bigSettingArray(){
		Atom[] atomArr = new Atom[rb[0].buffy.length * 2 + 3];
		for (int i = 0; i < rb[0].buffy.length; i++){
			for (int j = 0; j < 2; j++){
				atomArr[i + (j * rb[0].buffy.length) + 1] = Atom.newAtom(getValueForGUIButton(j, i));
			}
		}
		atomArr[0] = Atom.newAtom("bigsetting");
		atomArr[65] = Atom.newAtom(barDisplay);
		atomArr[66] = Atom.newAtom(bufferIndex);
		return atomArr;
	}
	
	public String toString(){
		String ret = "RhythmBufferGUIController:\n";
		for (int i = 0; i < rb.length; i++){
			ret = ret + bufferName[i] + rb[i].buffyToString() + "\n";
		}
		ret = ret + "barDisplay: " + barDisplay + "\n";
		ret = ret + "bufferIndex: " + bufferIndex + "\n";
		ret = ret + "duplicate: " + duplicate + "\n";
		return ret;
	}
	
	
	
// privates ===============================================================================
	private int getValueForGUIButton(int rbIndex, int buffyIndex){
		rbIndex = rbIndex % 2;			// just in case we get sent an interlock rhythm buffer index
		int temp = 0;
		if (rb[rbIndex].buffy[buffyIndex] == 1 && rb[rbIndex+2].buffy[buffyIndex] == 0){
			temp = 1;
		} else if (rb[rbIndex].buffy[buffyIndex] == 0 && rb[rbIndex+2].buffy[buffyIndex] == 1){
			temp = 2;
		} else if (rb[rbIndex].buffy[buffyIndex] == 1 && rb[rbIndex+2].buffy[buffyIndex] == 1){
			temp = 3;
		}
		return temp;
	}
	
	private Atom[] setBuffyItem(int button, int shift ,int value){
		int rbIndex = button / 16 + shift * 2;
		int buffyIndex = barDisplay * 16 + button % 16;
		rb[rbIndex].buffy[buffyIndex] = value;
		ArrayList<Atom> atList = new ArrayList<Atom>();
		atList.add(Atom.newAtom("setbuttons"));
		atList.addAll(makeButtonMessage(rbIndex, buffyIndex));
		atList.addAll(doDuplicate(rbIndex, buffyIndex));
		return atList.toArray(new Atom[atList.size()]);
	}
	private ArrayList<Atom> makeButtonMessage(int rbIndex, int buffyIndex){
		ArrayList<Atom> atList = new ArrayList<Atom>();
		int bar = buffyIndex / 16;
		int button = (rbIndex % 2 * 16) + (buffyIndex % 16);
		atList.add(Atom.newAtom(button));
		atList.add(Atom.newAtom(bar));
		atList.add(Atom.newAtom(getValueForGUIButton(rbIndex, buffyIndex)));
		
		return atList;
	}
	private ArrayList<Atom> doDuplicate(int rbIndex, int buffyIndex){
		ArrayList<Atom> atList = new ArrayList<Atom>();
		if (duplicate > 0){
			int duplicateBuffyIndex = (buffyIndex + 16) % 32;
			rb[rbIndex].buffy[duplicateBuffyIndex] = rb[rbIndex].buffy[buffyIndex];
			atList.addAll(makeButtonMessage(rbIndex, duplicateBuffyIndex));
		}
		return atList;
	}
	private int buffyItemFromButton(int button, int shift){
		int rbIndex = button / 16 + shift * 2;
		int buffyIndex = barDisplay * 16 + button % 16;
		return rb[rbIndex].buffy[buffyIndex];
	}
	private void makeBuffers(){
		for (int i = 0; i < rb.length; i++){
			rb[i] = new TwoBarRhythmBuffer();
		}
	}
	

}
