package main.java.da_utils.algorithmic_models.pipeline.plugins.embellishments;
import java.io.File;
import java.text.DecimalFormat;
import java.util.ArrayList;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerConfigurationException;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.TransformerFactoryConfigurationError;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;
import javax.xml.xpath.XPath;
import javax.xml.xpath.XPathConstants;
import javax.xml.xpath.XPathExpressionException;
import javax.xml.xpath.XPathFactory;

import com.cycling74.max.MaxObject;

import main.java.da_utils.algorithmic_models.pipeline.pipeline_note_list.PipelineNoteList;
import main.java.da_utils.algorithmic_models.pipeline.pipeline_note_list.PipelineNoteObject;
import main.java.da_utils.algorithmic_models.pipeline.plugins.Pluggable;
import main.java.da_utils.algorithmic_models.pipeline.plugins.pipeline_plugin.PipelinePlugIn;
import main.java.da_utils.algorithmic_models.pipeline.plugins.pipeline_plugin.PlayPlugArgument;
import main.java.da_utils.algorithmic_models.pipeline.utilities.NoteRange;

import org.w3c.dom.Attr;
import org.w3c.dom.DOMException;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;


public class PlugInAddEmbellishmentRhythm extends Pluggable implements PipelinePlugIn{
	
	
	public double[] options; 
	public double[] weighting;
	public double weightingSum;
	public double chance;					// this is the chance of an embellishment being inserted	
	private NoteRange noteRange = null;
//	private boolean hasNoteRange = false;
	private static DecimalFormat df2 = new DecimalFormat("#.##");
	
	private void doParams(){
		setName("EmbRhy");
		setSortIndex(10);
		setInputSort(0);
		setZone(1);
		setCanDouble(true);
		setActive(1);
	}
	public PlugInAddEmbellishmentRhythm(double[] options, double[] weighting, double chance){
		doParams();
		sortOutWeighting(options, weighting);
		weightingSum = makeWeightingSum();
		this.options = options;
		for (double d: options){
			name = name + d + ",";
		}
		this.chance = chance;
		noteRange = new NoteRange();		// NoteRange allowing every note
	}
	public PlugInAddEmbellishmentRhythm(double[] options, double[] weighting, double chance, NoteRange nr){
		doParams();
		if (weighting.length == 0) weighting = new double[]{1.0};	// to cater for blank PlugInOptioneer in CombReComb_Console3
		sortOutWeighting(options, weighting);
		weightingSum = makeWeightingSum();
		this.options = options;
		for (double d: options){
			name = name + d + ",";
		}
		this.chance = chance;
		this.noteRange = nr;
//		hasNoteRange = true;
	}
	
	public void process(PipelineNoteList pnl, PlayPlugArgument ppa){
//		MaxObject.post("PlugInAddEmbellishmentRhythm.process called with pnl:");
//		postPNL(pnl);
		if (active > 0){
			ArrayList<PipelineNoteObject> pnoTempList = new ArrayList<PipelineNoteObject>();
			for (PipelineNoteObject pno: pnl.pnoList){
				if (noteRange.pnoInNoteRange(pno) && pno.isEmbellishable && ppa.rnd.next() < chance && options.length > 0){
					double position = pno.position + options[getWeightedIndex(ppa)];
					while (position < 0.0) position += pnl.length;
					position = position % pnl.length;
					if (!positionAlreadyTaken(position, pnl.pnoList, pnoTempList, true)){
						//System.out.println("adding noteposition at pos " + position);
						PipelineNoteObject newPno = new PipelineNoteObject(position, false, true, pno);
						pnoTempList.add(newPno);
					} 
					pno.isEmbellishable = false;
				}
			}
			
			pnl.add(pnoTempList);
//			System.out.println("PipelineNoteList contents:-----------------");
//			for (PipelineNoteObject p: pnl.pnoList){
//				System.out.println(p.position + " has " + p.noteList.size() + " notes  embellishable = " + p.isEmbellishable + " is GuideTone " + p.isGuideTone);
//			}
			pnl.dealWithOverlaps();
		}		
	}
	public void setRhythmChance(double d) {
		chance = d;		
	}
	
	public void setRhythmOptions(double[] arr) {
		options = arr;
		sortOutWeighting(options, weighting);
		weightingSum = makeWeightingSum();
	}
	
	public void setNoteRange(NoteRange nr){
		this.noteRange = nr;
	}
	
	public void addRhythms(double[] darr) {
		double[] arr = new double[options.length + darr.length];
		for (int i = 0; i < options.length; i++){
			arr[i] = options[i];
		}
		for (int i = 0; i < darr.length; i++){
			arr[options.length + i] = darr[i];
		}
		setRhythmOptions(arr);
		
	}
	
	public boolean removeRhythm(double[] list) {
		boolean b = false;
		double[] optcopy = copyArr(options);
		for (double d: list){
			int index = getIndexof(d, optcopy);
			if (index < optcopy.length){
				optcopy = removeIndex(optcopy, index);
				b = true;
			}
		}
		setRhythmOptions(optcopy);
		return b;
	}
	
	public Element getSettingsAsXMLElement(Document doc){
		Element rootElement = doc.createElement("plugin_preset");
		
		Attr attr = doc.createAttribute("name");
		attr.setValue("PlugInAddEmbellishmentRhythm");
		rootElement.setAttributeNode(attr);
		
		Element elChance = doc.createElement("chance");
		elChance.appendChild(doc.createTextNode("" + chance));
		rootElement.appendChild(elChance);
		
		Element elOptions = doc.createElement("rhythm_options");
		for (double d: options){
			Element item = doc.createElement("item");
			item.appendChild(doc.createTextNode("" + d));
			elOptions.appendChild(item);
		}
		rootElement.appendChild(elOptions);
		
		Element elWeighting = doc.createElement("weighting");
		for (double d: weighting){
			Element item = doc.createElement("item");
			item.appendChild(doc.createTextNode("" + d));
			elWeighting.appendChild(item);
		}
		rootElement.appendChild(elWeighting);
		
		if (noteRange != null){
			Element elNoteRange = noteRange.getXMLElement(doc);
		
			rootElement.appendChild(elNoteRange);
		}
		return rootElement;
	}
	
	public void setParametersFromXMLNode(Node preset, XPath xpath, DocumentBuilder dBuilder){
		setChanceFromXMLNode(preset, xpath);
		setOptionsFromXMLNode(preset, xpath);
		setWeightingFromXMLNode(preset, xpath);
		setNoteRangeFromXMLNode(preset, xpath);
	}
	

	
	public String toString(){
		String str = "PlugInAddEmbellishmentRhythm: chance=" + chance + " rhy=[" + makeString(options) + "] weighting=[" + makeString(weighting) + "]";
		str += "\n" + noteRange.toString();
		return str;
	}


	
	
// privates ----------------------------------------------------------------------------
	
	
	private void setNoteRangeFromXMLNode(Node preset, XPath xpath) {
		try {
			String xpathStr = "//plugin_preset[@name='PlugInAddEmbellishmentRhythm']/note_range/mode";
			String str = (String)xpath.evaluate(xpathStr, preset, XPathConstants.STRING);
			int mode = Integer.parseInt(str);
			switch (mode){
			case 0:	listNoteRange(preset, xpath);
			break;
			case 1:	rangeNoteRange(preset, xpath);
			break;
			case 2:	arrayNoteRange(preset, xpath);
			break;
			}
		} catch (XPathExpressionException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
	}
	private void arrayNoteRange(Node preset, XPath xpath) {
		try {
			String xpathStr = "//plugin_preset[@name='PlugInAddEmbellishmentRhythm']/note_range/array/item";
			NodeList nList = (NodeList)xpath.evaluate(xpathStr, preset, XPathConstants.NODESET);
			int[] arr = new int[nList.getLength()];
			for (int i = 0; i < nList.getLength(); i++){
				Node item = nList.item(i);

				arr[i] = Integer.parseInt(item.getFirstChild().getNodeValue());
			}
			noteRange = new NoteRange(arr);
		} catch (XPathExpressionException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
	}
	private void rangeNoteRange(Node preset, XPath xpath) {
		try {
			String xpathStr = ".//note_range/min";
			String str = (String)xpath.evaluate(xpathStr, preset, XPathConstants.STRING);
			int min = Integer.parseInt(str);
			
			xpathStr = "//plugin_preset/note_range/max";
			str = (String)xpath.evaluate(xpathStr, preset, XPathConstants.STRING);
			int max = Integer.parseInt(str);
			
			noteRange = new NoteRange(min, max);
		} catch (XPathExpressionException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
	}
	private void listNoteRange(Node preset, XPath xpath) {
		try {
			String xpathStr = ".//note_range/list/item";
			NodeList nList = (NodeList)xpath.evaluate(xpathStr, preset, XPathConstants.NODESET);
			ArrayList<Integer> list = new ArrayList<Integer>();
//			System.out.println("length=" + nList.getLength());
			for (int i = 0; i < nList.getLength(); i++){
				Node item = nList.item(i);
//			System.out.println("..." + item.getFirstChild().getNodeValue());
				list.add(Integer.parseInt(item.getFirstChild().getNodeValue()));
			}
			noteRange = new NoteRange(list);
		} catch (XPathExpressionException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
	}
	private void setWeightingFromXMLNode(Node preset, XPath xpath) {
		try {
			String xpathStr = ".//weighting/item";
			NodeList nList = (NodeList)xpath.evaluate(xpathStr, preset, XPathConstants.NODESET);
			double[] weightArr = new double[nList.getLength()];
//			System.out.println("length=" + nList.getLength());
			for (int i = 0; i < nList.getLength(); i++){
				Node item = nList.item(i);
//			System.out.println("..." + item.getFirstChild().getNodeValue());
				weightArr[i] = Double.parseDouble(item.getFirstChild().getNodeValue());
			}
			weighting = weightArr;
//			System.out.println(toString());
		} catch (NumberFormatException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (XPathExpressionException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (DOMException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
	}
	private void setOptionsFromXMLNode(Node preset, XPath xpath) {
		try {
			String xpathStr = ".//rhythm_options/item";
			NodeList nList = (NodeList)xpath.evaluate(xpathStr, preset, XPathConstants.NODESET);
			double[] optArr = new double[nList.getLength()];
//			System.out.println("length=" + nList.getLength());
			for (int i = 0; i < nList.getLength(); i++){
				Node item = nList.item(i);
//			System.out.println("..." + item.getFirstChild().getNodeValue());
				optArr[i] = Double.parseDouble(item.getFirstChild().getNodeValue());
			}
			options = optArr;
//			System.out.println(toString());
		} catch (NumberFormatException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (XPathExpressionException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (DOMException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
	}
	private void setChanceFromXMLNode(Node preset, XPath xpath) {
		try {
//			System.out.println(toString());
			String xpathStr = ".//chance";
			String ch = (String)xpath.evaluate(xpathStr, preset, XPathConstants.STRING);
//			System.out.println(ch);
			chance = Double.parseDouble(ch);			
//			System.out.println(toString());
		} catch (NumberFormatException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (XPathExpressionException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
	}
	
	private double[] removeIndex(double[] optcopy, int removeIndex) {
		double[] newArr = new double[optcopy.length - 1];
		int index;
		for (int i = 0; i < newArr.length; i++){
			if (i >= removeIndex){
				index = i + 1;
			} else {
				index = i;
			}
			newArr[i] = optcopy[index];
		}
		return newArr;
	}
	private int getIndexof(double d, double[] optcopy) {
		for (int i = 0; i < optcopy.length; i++){
			if (optcopy[i] == d) return i;
		}
		return optcopy.length;
	}
	
	private double[] copyArr(double[] arr) {
		double[] newArr = new double[arr.length];
		for (int i = 0; i < arr.length; i++){
			newArr[i] = arr[i];
		}
		return newArr;
	}
	
	private String makeString(double[] arr) {
		if (arr.length == 0){
			return "";
		} else {
			String str = "" + df2.format(arr[0]);
			for (int i = 1; i < arr.length; i++){
				str += "," + df2.format(arr[i]);
			}
			return str;
		}

	}
	private double makeWeightingSum(){
		double sum = 0.0;
		for (double dd: weighting){
			sum += dd;
		}
		return sum;
	}
	private void sortOutWeighting(double[] options, double[] weighting){
		if (weighting.length == 0) weighting = new double[]{1.0}; // for when the plug is blank and being instantiated bit by bit
		if (weighting.length != options.length){
			double[] newWeighting = new double[options.length];
			for (int i = 0; i < newWeighting.length; i++){
				newWeighting[i] = weighting[i % weighting.length];
			}
			this.weighting = newWeighting;
		} else {
			this.weighting = weighting;
		}
	}
	private boolean positionAlreadyTaken(double pos, ArrayList<PipelineNoteObject> list1, ArrayList<PipelineNoteObject> list2, boolean setToNotEmbellishableOnMatch){
		//System.out.println("TESTING FOR pos = " + pos + "-----------------------------------");
		if (containsPosition(pos, list1, setToNotEmbellishableOnMatch) || containsPosition(pos, list2, setToNotEmbellishableOnMatch)){
			return true;
		} else {
			return false;
		}
	}
	private boolean containsPosition(double pos, ArrayList<PipelineNoteObject> list, boolean setToNotEmbellishableOnMatch){
		for (PipelineNoteObject pno: list){
			//System.out.println("testing pos " + pos + " against pno.position " + pno.position);
			if (pno.position == pos){
				//System.out.println("MATCH on position " + pos + " !!!!!!!!!!!!!!!!!!!!!!!!");
				if (setToNotEmbellishableOnMatch) pno.isEmbellishable = false;			// this is to stop the PlugInAssignEmbellishment from doubling up notes on an already existingnote position
				return true;
			}
		}
		return false;
	}
	private int getWeightedIndex(PlayPlugArgument ppa){
		double r = ppa.rnd.next() * weightingSum;
		int index = -1;
		while (r >= 0){
			index++;
			r -= weighting[index];								
		}
		return index;
	}
	private void postPNL(PipelineNoteList pnl){
		MaxObject.post("PlugInBassAddEmbellishmentOne PipelineNoteList.toString -----------------");
		String[] splitPost = pnl.toString().split("\n");
		for (String str: splitPost){
			MaxObject.post(str);
		}
		
	}
	
	
// ========= main ================================================================
	// testing removeRhythm
	
	public static void main(String[] args){
		PlugInAddEmbellishmentRhythm plug = new PlugInAddEmbellishmentRhythm(new double[]{-0.5, -1.0, -1.5, -0.5}, new double[]{1.0}, 1.0);
		System.out.println(plug.toString());
		System.out.println(plug.removeRhythm(new double[]{-2.0}));
		System.out.println("----------------------------------------");
		System.out.println(plug.toString());
		
		try {
			DocumentBuilderFactory docFactory = DocumentBuilderFactory.newInstance();
			DocumentBuilder docBuilder = docFactory.newDocumentBuilder();

			// root elements
			Document doc = docBuilder.newDocument();
			
			Element rootElement = plug.getSettingsAsXMLElement(doc);
			doc.appendChild(rootElement);
			
			// write the content into xml file
			TransformerFactory transformerFactory = TransformerFactory.newInstance();
			Transformer transformer = transformerFactory.newTransformer();
			DOMSource source = new DOMSource(doc);
			StreamResult result = new StreamResult(new File("D:/DougzJavaz/snadbox/src/dom_xml_test/pluginaddembellishmentrhythm.xml"));

			// Output to console for testing
			// StreamResult result = new StreamResult(System.out);

			transformer.transform(source, result);

			System.out.println("File saved!");
			
		} catch (Exception ex){
			System.out.println("ooops");
		}
	}
	


	

}
