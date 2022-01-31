package LegacyStuff;
import com.cycling74.max.Atom;
import com.cycling74.max.DataTypes;
import com.cycling74.max.MaxObject;


public class RhythmBufferGUIControllerMAxWrapper extends MaxObject{
	
	public RhythmBufferGUIController rbgc = new RhythmBufferGUIController();
	public int rbPanelOutlet = 0;		// considering having seperate outlets for each panel
	public int rbPanelInlet = 0;
	
	public RhythmBufferGUIControllerMAxWrapper(){
		setMaxInlets();
	}
	public void initialize(){
		bigSetting();
	}
	public void bar(int i){
		int inlet = getInlet();
		if (inlet == rbPanelInlet) rbgc.setBarDisplay(i);
	}
	public void buffer(int i){
		int inlet = getInlet();
		if (inlet == rbPanelInlet) rbgc.setBufferIndex(i);
	}
	public void duplicate(int i){
		int inlet = getInlet();
		if (inlet == rbPanelInlet) rbgc.duplicate(i);
	}
	public void bigSetting(){
		sendMessage(rbPanelOutlet, rbgc.bigSettingArray());
	}
	public void button(int button, int shift){
		int inlet = getInlet();
		if (inlet == rbPanelInlet) sendMessage(rbPanelOutlet, rbgc.button(button, shift));
	}
	public void postRhythmBuffer(){
		postSplit(rbgc.toString(), "\n");
	}
	
// privates ===================================================================
	public void postSplit(String str, String splitter){
		String[] strArr = str.split(splitter);
		for (String s: strArr){
			post(s);
		}
	}
	private void sendMessage(int out, Atom[] message){
		outlet(out, message);
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
				"messages out",
				"dump out"}
		);
	}
}
