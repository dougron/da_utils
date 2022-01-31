package LegacyStuff;
import com.cycling74.max.Atom;

import acm.program.ConsoleProgram;


public class RhythmBufferGUIControllerTest extends ConsoleProgram{

	
	public void run(){
		setSize(700, 700);
		RhythmBufferGUIController rbgc = new RhythmBufferGUIController();
		println(rbgc.toString());
//		rbgc.setBarDisplay(1);
//		testBigSetting(rbgc);
		testButtonInput(rbgc, 0, 1, 0);
		testButtonInput(rbgc, 0, 0, 1);
		rbgc.button(1, 0);
		println(rbgc.toString());
	}
	
	
// privates ================================================================================
	private void testButtonInput(RhythmBufferGUIController rbgc, int shift, int bd, int dup){
		rbgc.setBarDisplay(bd);
		rbgc.duplicate(dup);
		for (int i = 0; i < 32; i ++){
			println("button: " + i + " " + shift);
			println(stringFromAtomArray(rbgc.button(i, shift)));
			println(rbgc.toString());
		}
	}
	private String stringFromAtomArray(Atom[] atArr){
		String[] strArr = stringArrFromAtom(atArr);
		String str = stringFromStringArr(strArr);
		return str;
	}
	private void testBigSetting(RhythmBufferGUIController rbgc){
		println("bisetting test -----------------------------------------------------");
		Atom[] atArr = rbgc.bigSettingArray();
		String[] strArr = stringArrFromAtom(atArr);
		String str = stringFromStringArr(strArr);
		println(str);
	}
	private String stringFromStringArr(String[] strArr){
		String str = "";
		for (int i = 0; i < strArr.length; i++){
			str = str + strArr[i] + " ";
		}		
		return str;
	}
	private String[] stringArrFromAtom(Atom[] atomArr){
		String[] strArr = new String[atomArr.length];
		for (int i = 0; i < atomArr.length; i++){
			strArr[i] = convertAtomToString(atomArr[i]);			
		}		
		return strArr;
	}
	public String convertAtomToString(Atom atom){
		if (atom.isString()){
			return atom.getString();
		} else if (atom.isInt()){
			return Integer.toString(atom.getInt());
		} else if (atom.isFloat()){
			return Float.toString(atom.getFloat());
		}
		return "fail";
	}
}
