package main.java.da_utils.algorithmic_models.pipeline.plugins.embellishments;
import java.io.File;
import java.text.DecimalFormat;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;
import javax.xml.xpath.XPath;
import javax.xml.xpath.XPathConstants;
import javax.xml.xpath.XPathExpressionException;

import org.w3c.dom.Attr;
import org.w3c.dom.DOMException;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import com.cycling74.max.MaxObject;

import main.java.da_utils.algorithmic_models.pipeline.pipeline_note_list.Embellishment;
import main.java.da_utils.algorithmic_models.pipeline.pipeline_note_list.PipelineNoteList;
import main.java.da_utils.algorithmic_models.pipeline.pipeline_note_list.PipelineNoteObject;
import main.java.da_utils.algorithmic_models.pipeline.plugins.Pluggable;
import main.java.da_utils.algorithmic_models.pipeline.plugins.pipeline_plugin.PipelinePlugIn;
import main.java.da_utils.algorithmic_models.pipeline.plugins.pipeline_plugin.PlayPlugArgument;


public class PlugInAssignEmbellishment extends Pluggable implements PipelinePlugIn{

//	public static final String name = "PlugInAssignEmbellishment";
	public Embellishment[] embOptionArr; 
	private double[] weighting;
	private double weightingSum;
	private double percentageOfEmbellishingVelocity = 0.8;		// if embellished note is a Guide tone
//	private ResourceObject ro;
	private static DecimalFormat df2 = new DecimalFormat("#.##");
	
	
	
	private void doParams(){
		setName("PlugInAssignEmbellishment");
		setSortIndex(20);
		setInputSort(0);
		setZone(1);
		setCanDouble(true);
		setActive(1);
	}
	
	public PlugInAssignEmbellishment(ED[] edArr, double[] weighting){
		doParams();
//		this.ro = ro;
		if (weighting.length == 0) weighting = new double[]{1.0};	// to cater for blank PlugInOptioneer in CombReComb_Console3
		
		setEmbellishmentsAndWeighting(edArr, weighting);
	}
	
	private void setEmbellishmentsAndWeighting(ED[] edArr, double[] weighting){
		sortOutWeighting(edArr.length, weighting);
		weightingSum = makeWeightingSum();
		embOptionArr = makeEmbellishmentOptions(edArr);
	}
	
	public void process(PipelineNoteList pnl, PlayPlugArgument ppa){
//		MaxObject.post("PlugInAssignEmbellishment.process() called");
		for (PipelineNoteObject pno: pnl.pnoList){
//			splitPost(pno.toString(), "\n");
			if (!pno.notesAreUpToDate && !pno.isGuideTone && embOptionArr.length > 0){
//				System.out.println("adding note to position " + pno.position);
				pno.embellishmentType = embOptionArr[getWeightedIndex(ppa)];
				pno.addNote(pno.embellishmentType.getNote(pno.pnoEmbellishing, ppa));
				if (pno.pnoEmbellishing.isGuideTone){
					pno.setFixedVelocity((int)(pno.pnoEmbellishing.velocity * percentageOfEmbellishingVelocity));
				} else {
					pno.setFixedVelocity((int)(pno.pnoEmbellishing.velocity));
				}
				pno.notesAreUpToDate = true;
			}
		}
	}
	
	public void setEmbellishmentOptions(ED[] arr) {
		setEmbellishmentsAndWeighting(arr, weighting);
		
	}
	
	public void addEmbellishmentOptions(ED[] arr) {
		// TODO Auto-generated method stub
		ED[] embArr = new ED[embOptionArr.length + arr.length];
		
		for (int i = 0; i < embOptionArr.length; i++){
			embArr[i] = embOptionArr[i].ed();
		}
		for (int i = 0; i < arr.length; i++){
			embArr[embOptionArr.length + i] = arr[i];
		}
		setEmbellishmentOptions(embArr);
	}
	
	public boolean removeEmbellishment(ED[] list) {
		boolean b = false;
		ED[] edArr = makeEDCopy(embOptionArr);
		for (ED ed: list){
			int index = getIndexof(ed, edArr);
			if (index < edArr.length){
				edArr = removeIndex(edArr, index);
				b = true;
			}
		}
		setEmbellishmentOptions(edArr);
		return b;
	}
	
	public Element getSettingsAsXMLElement(Document doc){
		Element rootElement = doc.createElement("plugin_preset");
		
		Attr attr = doc.createAttribute("name");
		attr.setValue("PlugInAssignEmbellishment");
		rootElement.setAttributeNode(attr);
				
		Element elOptions = doc.createElement("embellishment_options");
		for (Embellishment emb: embOptionArr){
			Element item = doc.createElement("item");
			
			Element type = doc.createElement("type");
			type.appendChild(doc.createTextNode(emb.getType()));
			item.appendChild(type);
			
			Element offset = doc.createElement("offset");
			offset.appendChild(doc.createTextNode("" + emb.getOffset()));
			item.appendChild(offset);
			
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
		
		return rootElement;
	}
	
	

	public String toString(){
		String str = "PlugInAssignEmbellishment: ";
		str += "embellishments=[";
		if (embOptionArr.length > 0) {
			str += embOptionArr[0].shortName();
			for (int i = 1; i < embOptionArr.length; i++) {
				str += ", " + embOptionArr[i].shortName();
			} 
		}
		str += "] weighting=[" + makeString(weighting) + "]";
		return str;
	}
	
	
// privates ----------------------------------------------------------------------------------
	
	private ED[] removeIndex(ED[] edArr, int removeIndex) {
		ED[] newArr = new ED[edArr.length - 1];
		int index;
		for (int i = 0; i < newArr.length; i++){
			if (i >= removeIndex){
				index = i + 1;
			} else {
				index = i;
			}
			newArr[i] = edArr[index];
		}
		return newArr;
	}
	private int getIndexof(ED ed, ED[] edArr) {
		for (int i = 0; i < edArr.length; i++){
			if (edArr[i].isSameAs(ed)) return i;
		}
		return edArr.length;
	}
	
	private ED[] makeEDCopy(Embellishment[] arr) {
		ED[] edArr = new ED[arr.length];
		for (int i = 0; i < arr.length; i++){
			edArr[i] = arr[i].ed();
		}
		return edArr;
	}
//	private double[] copyArr(double[] arr) {
//		double[] newArr = new double[arr.length];
//		for (int i = 0; i < arr.length; i++){
//			newArr[i] = arr[i];
//		}
//		return newArr;
//	}
	
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
	
	private Embellishment[] makeEmbellishmentOptions(ED[] edArr){
		Embellishment[] embArr = new Embellishment[edArr.length];
		for (int i = 0; i < edArr.length; i++){
			if (edArr[i].type.equals("s")){
				embArr[i] = new SemitoneEmbellishment(edArr[i].value);
			} else if (edArr[i].type.equals("d")){
				embArr[i] = new DiatonicEmbellishment(edArr[i].value);
			} else if (edArr[i].type.equals("c")){
				embArr[i] = new ChordToneEmbellishment(edArr[i].value);
			}
				
		}
		return embArr;
	}
	private double makeWeightingSum(){
		double sum = 0.0;
		for (double dd: weighting){
			sum += dd;
		}
		return sum;
	}
	private void sortOutWeighting(int length, double[] weighting){
		if (weighting.length == 0) weighting = new double[]{1.0}; 
		if (weighting.length != length){
			double[] newWeighting = new double[length];
			for (int i = 0; i < newWeighting.length; i++){
				newWeighting[i] = weighting[i % weighting.length];
			}
			this.weighting = newWeighting;
		} else {
			this.weighting = weighting;
		}
	}
	private int getWeightedIndex(PlayPlugArgument ppa){
		double r = ppa.rnd.next() * weightingSum;
		int index = -1;
		while (r > 0){
			index++;
			r -= weighting[index];								
		}
		return index;
	}
	private void splitPost(String str, String splitString){
		String[] strArr = str.split(splitString);
		for (String s: strArr){
			MaxObject.post(s);
		}
	}
	private void postPNL(PipelineNoteList pnl){
		MaxObject.post("PlugInBassAddEmbellishmentOne PipelineNoteList.toString -----------------");
		String[] splitPost = pnl.toString().split("\n");
		for (String str: splitPost){
			MaxObject.post(str);
		}
		
	}

	
// ============= main ====================================================
	
	public static void main(String[] args){
		PlugInAssignEmbellishment plug = new PlugInAssignEmbellishment(
				new ED[]{new ED("d", 1), new ED("d", 2), new ED("d", 1), new ED("s", 4)},
				new double[]{1.0});
//		System.out.println(plug.toString());
//		System.out.println(plug.removeEmbellishment(new ED[]{new ED("d", 1), new ED("d", 1), new ED("d", 2), new ED("s", 4), new ED("d", 1)}));
//		System.out.println("---------------------------\n" + plug.toString());
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
			StreamResult result = new StreamResult(new File("D:/DougzJavaz/snadbox/src/dom_xml_test/pluginassignembellishment.xml"));

			// Output to console for testing
			// StreamResult result = new StreamResult(System.out);

			transformer.transform(source, result);

			System.out.println("File saved!");
			
		} catch (Exception ex){
			System.out.println("ooops");
		}
	
	}


	
	
	
	
	public void setParametersFromXMLNode(Node preset, XPath xpath) {
		ED[] edarray = setEmbellishmentArrayFromXMLNode(preset, xpath);
		double[] warr = setWeightingFromXMLNode(preset, xpath);
		setEmbellishmentsAndWeighting(edarray, warr);
	}
	
	private ED[] setEmbellishmentArrayFromXMLNode(Node preset, XPath xpath) {
		try {
			String xpathStr = ".//embellishment_options/item";
			NodeList nList = (NodeList)xpath.evaluate(xpathStr, preset, XPathConstants.NODESET);
			ED[] edarr = new ED[nList.getLength()];
			System.out.println("length=" + nList.getLength());
			for (int i = 0; i < nList.getLength(); i++){
				Node item = nList.item(i);
				xpathStr = ".//type";
//				NodeList itemChildList = item.getChildNodes();
				
				String type = (String)xpath.evaluate(xpathStr, item, XPathConstants.STRING);
//				Node t2 = (Node)xpath.evaluate(xpathStr, item, XPathConstants.NODE);
//				String t3 = t2.getFirstChild().getNodeValue(); 
				System.out.println("type=" + type);
			
				xpathStr = ".//offset";
				int offset = Integer.parseInt((String)xpath.evaluate(xpathStr, item, XPathConstants.STRING));
//				
				System.out.println("type=" + type + " offset=" + offset);
				edarr[i] = new ED(type, offset);
//			System.out.println("..." + item.getFirstChild().getNodeValue());
//				weightArr[i] = Double.parseDouble(item.getFirstChild().getNodeValue());
			}
//			weighting = weightArr;
//			System.out.println(toString());
			return edarr;
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
		return new ED[]{};
	}

	private double[] setWeightingFromXMLNode(Node preset, XPath xpath) {
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
			return weightArr;
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
		return new double[]{};
	}



}
