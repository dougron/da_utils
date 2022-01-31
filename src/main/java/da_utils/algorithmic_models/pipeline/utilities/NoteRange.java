package main.java.da_utils.algorithmic_models.pipeline.utilities;
import java.io.File;
import java.util.ArrayList;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;

import org.w3c.dom.Document;
import org.w3c.dom.Element;

import main.java.da_utils.ableton_live.ableton_live_clip.LiveMidiNote;
import main.java.da_utils.algorithmic_models.pipeline.pipeline_note_list.PipelineNoteObject;

/*
 * class that encapsulates a note range, or any range of values, either as a list of discrete values or 
 *  or as a contiguous range with min and max value
 *  
 *  
 */
public class NoteRange {
	
	public int mode;
	int min;
	int max;
	ArrayList<Integer> list;
	int[] array;

	public NoteRange(int min, int max){
		mode = RANGE;
		this.min = min;
		this.max = max;
	}
	public NoteRange(int[] arr){
		mode = ARRAY;
		array = arr;
	}
	public NoteRange(ArrayList<Integer> list){
		mode = LIST;
		this.list = list;
	}
	public NoteRange(){
		mode = RANGE;
		this.min = 0;
		this.max = 127;
	}
	public boolean valueInNoteRange(int i){
		if (mode == LIST){
			return valueInList(i);
		} else if (mode == RANGE){
			return valueInRange(i);
		} else if (mode == ARRAY){
			return valueInArray(i);
		} else {
			return false;
		}
	}
	public boolean pnoInNoteRange(PipelineNoteObject pno){
		for (LiveMidiNote lmn: pno.noteList){
			if (!valueInNoteRange(lmn.note)){
				return false;
			}
		}
		return true;
	}
	
	public Element getXMLElement(Document doc) {
		Element elNoteRange = doc.createElement("note_range");
		
		Element nrMode = doc.createElement("mode");
		nrMode.appendChild(doc.createTextNode("" + mode));
		elNoteRange.appendChild(nrMode);
		
		switch (mode){
		case LIST: 	addListToXML(elNoteRange, doc);
		break;
		case RANGE:	addRangeToXML(elNoteRange, doc);
		break;
		case ARRAY:	addArrayToXML(elNoteRange, doc);
		break;
		}
		return elNoteRange;
	}

	public String toString(){
		String str = "";
		switch (mode){
		case 0:	str += listToString();
		break;
		case 1:	str += rangeToString();
		break;
		case 2: str += arrayToString();
		break;
		}
		return str;
	}
	
// privates ------------------------------------------------------------------------
	
	
	private String arrayToString() {
		String str = "noteRange using array:";
		for (int i: array){
			str += i + ",";
		}
		return str;
	}
	private String rangeToString() {
		String str = "noteRange using range min=" + min + " max=" + max;
		return str;
	}
	private String listToString() {
		String str = "noteRange using list:";
		for (Integer i: list){
			str += i + ",";
		}
		return str;
	}
	private void addRangeToXML(Element elNoteRange, Document doc) {
		Element elMin = doc.createElement("min");
		elMin.appendChild(doc.createTextNode("" + min));
		Element elMax = doc.createElement("max");
		elMax.appendChild(doc.createTextNode("" + max));
		elNoteRange.appendChild(elMin);
		elNoteRange.appendChild(elMax);
		
	}
	
	private void addArrayToXML(Element elNoteRange, Document doc) {
		Element elArray = doc.createElement("array");
		for (int i: array){
			Element item = doc.createElement("item");
			elArray.appendChild(item);
			item.appendChild(doc.createTextNode("" + i));
		}
		elNoteRange.appendChild(elArray);
		
	}
	
	private void addListToXML(Element elNoteRange, Document doc) {
		Element elList = doc.createElement("list");
		for (Integer i: list){
			Element item = doc.createElement("item");
			elList.appendChild(item);
			item.appendChild(doc.createTextNode("" + i));
		}
		elNoteRange.appendChild(elList);
		
	}
	
	private boolean valueInArray(int i){
		for (int x: array){
			if (x == i){
				return true;
			}
		}
		return false;
	}
	
	private boolean valueInList(int i){
		if (list.contains(i)){
			return true;
		} else {
			return false;
		}
	}
	
	private boolean valueInRange(int i){
		if (i >= min && i <= max){
			return true;
		} else {
			return false;
		}
	}
	
	private static final int LIST = 0;
	private static final int RANGE = 1;
	private static final int ARRAY = 2;


	
// ===== main ========================================================
	
	public static final void main(String[] args){
		
//		NoteRange nr = new NoteRange(new ArrayList<Integer>(){{
//			add(34);
//			add(35);
//			add(100);
//		}});
		
		NoteRange nr = new NoteRange(new int[]{67, 68, 69});
		
		try {
			DocumentBuilderFactory docFactory = DocumentBuilderFactory.newInstance();
			DocumentBuilder docBuilder = docFactory.newDocumentBuilder();

			// root elements
			Document doc = docBuilder.newDocument();
			
			Element rootElement = nr.getXMLElement(doc);
			doc.appendChild(rootElement);
			
			// write the content into xml file
			TransformerFactory transformerFactory = TransformerFactory.newInstance();
			Transformer transformer = transformerFactory.newTransformer();
			DOMSource source = new DOMSource(doc);
			StreamResult result = new StreamResult(new File("D:/DougzJavaz/snadbox/src/dom_xml_test/noterange.xml"));

			// Output to console for testing
			// StreamResult result = new StreamResult(System.out);

			transformer.transform(source, result);

			System.out.println("File saved!");
			
			
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
}
