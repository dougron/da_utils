package main.java.da_utils.algorithmic_models.pipeline.plugins.embellishments;
//import com.cycling74.max.MaxObject;


import java.io.File;
import java.util.Collection;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;
import javax.xml.xpath.XPath;
import javax.xml.xpath.XPathConstants;
import javax.xml.xpath.XPathExpressionException;
import javax.xml.xpath.XPathFactory;

import org.apache.commons.io.FileUtils;
import org.apache.commons.io.filefilter.WildcardFileFilter;
import org.w3c.dom.Attr;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;

import main.java.da_utils.ableton_live.ableton_live_clip.LiveClip;
import main.java.da_utils.algorithmic_models.pipeline.Pipe;
import main.java.da_utils.algorithmic_models.pipeline.pipeline_note_list.PipelineNoteList;
import main.java.da_utils.algorithmic_models.pipeline.plugins.Pluggable;
import main.java.da_utils.algorithmic_models.pipeline.plugins.melody.PlugInMelodyContourGuideToneEveryBar;
import main.java.da_utils.algorithmic_models.pipeline.plugins.pipeline_plugin.PipelinePlugIn;
import main.java.da_utils.algorithmic_models.pipeline.plugins.pipeline_plugin.PlayPlugArgument;
import main.java.da_utils.algorithmic_models.pipeline.utilities.NoteRange;
import main.java.da_utils.resource_objects.ChordForm;
import main.java.da_utils.resource_objects.ContourData;
import main.java.da_utils.resource_objects.RandomNumberSequence;
import main.java.da_utils.test_utils.TestData;



public class PlugInEmbellishmentOptioneer extends Pluggable implements PipelinePlugIn{
	
	private PipelinePlugIn[] plugArr; 
	private int cycle = 1;				// # of times process method will cycle
	private PlugInAddEmbellishmentRhythm rhythmPlug;
	private PlugInAssignEmbellishment embPlug;
	private static int instanceCounter = 0;
	private int instance;
	static String presetPath = "D:/DougzJavaz/DAUtils/src/PlugInPresets/PlugInEmbellishmentOptioneer/";
	
	private void doParams(){
		setName("Emb_Rand");
		setSortIndex(0);
		setInputSort(0);
		setZone(1);
		setCanDouble(true);
		setActive(1);
	}
	
	public PlugInEmbellishmentOptioneer(double[] rhythmOptions, 
			double[] rhythmOptionChance, 
			double rhythmChance, 
			ED[] embellishmentOptions, 
			double[] embellishmentOptionChance){

		doParams();
		makePlugArr(rhythmOptions, rhythmOptionChance, rhythmChance, embellishmentOptions, embellishmentOptionChance);
		
		makeInstanceNumber();
	}
	
	

	public PlugInEmbellishmentOptioneer(double[] rhythmOptions, 
			double[] rhythmOptionChance, 
			double rhythmChance, 
			ED[] embellishmentOptions, 
			double[] embellishmentOptionChance, int cycle){
		
		doParams();
		makePlugArr(rhythmOptions, rhythmOptionChance, rhythmChance, embellishmentOptions, embellishmentOptionChance);
		this.cycle = cycle;
		makeInstanceNumber();
	}
	
	public PlugInEmbellishmentOptioneer(double[] rhythmOptions, 
			double[] rhythmOptionChance, 
			double rhythmChance, 
			ED[] embellishmentOptions, 
			double[] embellishmentOptionChance, int cycle, NoteRange nr){
		
		doParams();
		makePlugArr(rhythmOptions, rhythmOptionChance, rhythmChance, embellishmentOptions, embellishmentOptionChance, nr);
		this.cycle = cycle;
		makeInstanceNumber();
	}
	
	public PlugInEmbellishmentOptioneer(String presetName){
		doParams();
		makePlugArr(new double[]{}, new double[]{}, 0.0, new ED[]{}, new double[]{});
		makeInstanceNumber();
		loadPreset(presetName);
	}
	
	private void makePlugArr(double[] rhythmOptions, double[] rhythmOptionChance, double rhythmChance, ED[] embellishmentOptions, double[] embellishmentOptionChance){
		rhythmPlug = new PlugInAddEmbellishmentRhythm(rhythmOptions, rhythmOptionChance, rhythmChance);
		embPlug = new PlugInAssignEmbellishment(embellishmentOptions, embellishmentOptionChance);
		
		plugArr = new PipelinePlugIn[]{rhythmPlug, embPlug};
	}
	
	private void makePlugArr(double[] rhythmOptions, double[] rhythmOptionChance, double rhythmChance, ED[] embellishmentOptions, double[] embellishmentOptionChance, NoteRange nr){
		rhythmPlug = new PlugInAddEmbellishmentRhythm(rhythmOptions, rhythmOptionChance, rhythmChance, nr);
		embPlug = new PlugInAssignEmbellishment(embellishmentOptions, embellishmentOptionChance);
		
		plugArr = new PipelinePlugIn[]{rhythmPlug, embPlug};
	}
	
	public void process(PipelineNoteList pnl, PlayPlugArgument ppa){

		for (int i = 0; i < cycle; i++) {
			for (PipelinePlugIn ppi : plugArr) {
				ppi.process(pnl, ppa);
			} 
		}
	}
	
	public void setChance(double d){
		rhythmPlug.setRhythmChance(d);
	}
	
	public void setRhythmOptions(double[] arr) {
		rhythmPlug.setRhythmOptions(arr);
		
	}
	
	public void setEmbellishmentOptions(ED[] arr) {
		embPlug.setEmbellishmentOptions(arr);
		
	}
	
	public void setCycle(int cycle) {
		this.cycle = cycle;
		
	}
	
	public void setNoteRange(NoteRange nr){
		rhythmPlug.setNoteRange(nr);
	}
	
	public void addRhythms(double[] darr) {
		rhythmPlug.addRhythms(darr);
		
	}
	
	public void addEmellishmentOptions(ED[] arr) {
		embPlug.addEmbellishmentOptions(arr);
		
	}
	
	public boolean removeRhythm(double[] list) {
		return rhythmPlug.removeRhythm(list);

	}
	
	public boolean removeEmbellishment(ED[] list) {
		return embPlug.removeEmbellishment(list);
	}
	
	public void loadPreset(String name){
		if (presetListContainsFile(name)){
			try {

				File file = new File(presetPath + name + "." + Pluggable.presetXMLExtension);

				DocumentBuilder dBuilder = DocumentBuilderFactory.newInstance().newDocumentBuilder();

				Document doc = dBuilder.parse(file);
				
				Element root = doc.getDocumentElement();
				
				System.out.println(root.getNodeName());
				System.out.println(root.getAttribute("name"));
				
				
				if (root.getNodeName().equals("plugin_preset") && root.getAttribute("name").equals("PlugInEmbellishmentOptioneer")){
					try {
						XPathFactory xfact = XPathFactory.newInstance();
						XPath xpath = xfact.newXPath();
						
						String xpathStr = "/plugin_preset/plugin_preset[@name='PlugInAddEmbellishmentRhythm']";
						Object res = xpath.evaluate(xpathStr, doc, XPathConstants.NODE);
						rhythmPlug.setParametersFromXMLNode((Node)res, xpath, dBuilder);
//						System.out.println((Node)res);
						
						xpathStr = "/plugin_preset/plugin_preset[@name='PlugInAssignEmbellishment']";
						res = xpath.evaluate(xpathStr, doc, XPathConstants.NODE);
						embPlug.setParametersFromXMLNode((Node)res, xpath);
						
						xpathStr = "/plugin_preset/cycle";
						String cycStr = (String)xpath.evaluate(xpathStr, doc, XPathConstants.STRING);
						System.out.println("cycle set to " + cycStr);
						cycle = Integer.parseInt(cycStr);
						
					} catch (XPathExpressionException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
					
//					NodeList nList = root.getChildNodes();
//					for (int i = 0 ; i < nList.getLength() ; i++) {
//					    Node child = nList.item(i);
//					    System.out.println(child.getNodeName());
//					    NamedNodeMap map = child.getAttributes();
//					   
//					    System.out.println(map.getNamedItem("name"));
//					    String childName = map.getNamedItem("name").getNodeValue();
//					    if (childName.equals("PlugInAddEmbellishmentRhythm")){
//					    	rhythmPlug.setParametersFromXMLNode(child);
//					    } else if (childName.equals("PlugInAssignEmbellishment")){
//					    	embPlug.setParametersFromXMLNode(child);
//					    }
//					    					    
//					}
					
				}
//				if (root.getNodeName())

//				System.out.println("Root element :" + doc.getDocumentElement().getNodeName());
//
//				if (doc.hasChildNodes()) {
//
//					printNote(doc.getChildNodes());
//
//				}

			    } catch (Exception e) {
			    	System.out.println(e.getMessage());
			    }
		}
	}
	
	public void savePresetXMLFile(String name){
		if (!presetListContainsFile(name)){
			try {
				DocumentBuilderFactory docFactory = DocumentBuilderFactory.newInstance();
				DocumentBuilder docBuilder = docFactory.newDocumentBuilder();

				// root elements
				Document doc = docBuilder.newDocument();
				
				Element rootElement = doc.createElement("plugin_preset");
				Attr attr = doc.createAttribute("name");
				attr.setValue("PlugInEmbellishmentOptioneer");
				rootElement.setAttributeNode(attr);
				doc.appendChild(rootElement);
				
				rootElement.appendChild(getCycleXMLElement(doc));
				rootElement.appendChild(rhythmPlug.getSettingsAsXMLElement(doc));
				rootElement.appendChild(embPlug.getSettingsAsXMLElement(doc));

				
				// write the content into xml file
				TransformerFactory transformerFactory = TransformerFactory.newInstance();
				Transformer transformer = transformerFactory.newTransformer();
				DOMSource source = new DOMSource(doc);
				String path = presetPath + name + "." + Pluggable.presetXMLExtension; 
				StreamResult result = new StreamResult(new File(path));

				// Output to console for testing
				// StreamResult result = new StreamResult(System.out);

				transformer.transform(source, result);

				System.out.println("File saved!");
				
			} catch (Exception ex){
				System.out.println("ooops");
			}
		}
	}
	
	private Node getCycleXMLElement(Document doc) {
		Element rootElement = doc.createElement("cycle");
		rootElement.appendChild(doc.createTextNode("" + cycle));
		return rootElement;
	}

	public static boolean presetListContainsFile(String name) {
		for (Object o: getListOfAllPresets()){
			if (o instanceof File){
				File f = (File)o;
				if (name.equals(getFileNameWithoutExtension(f))) return true;
			}
		}
		return false;
	}

	public static Collection getListOfAllPresets(){
	    File directory = new File(presetPath);
	    return FileUtils.listFiles(directory, new WildcardFileFilter("*." + Pluggable.presetXMLExtension), null);
	}
	
	public static String getFileNameWithoutExtension(File file) {
        String fileName = "";
 
        try {
            if (file != null && file.exists()) {
                String name = file.getName();
                fileName = name.replaceFirst("[.][^.]+$", "");
            }
        } catch (Exception e) {
            e.printStackTrace();
            fileName = "";
        }
 
        return fileName;
 
    }
	

	
	public String toString(){
		String str = "Optioneer: instance=" + instance + " cycle=" + cycle + " ---------\n";
		str += rhythmPlug.toString() + "\n" + embPlug.toString();
		return str;
	}

// privates -----------------------------------------------------------------------------
	
	private void makeInstanceNumber() {
		instance = instanceCounter;
		instanceCounter++;
		
	}
	
	private void postPNL(PipelineNoteList pnl){
//		MaxObject.post("PlugInBassAddEmbellishmentOne PipelineNoteList.toString -----------------");
		String[] splitPost = pnl.toString().split("\n");
		for (String str: splitPost){
//			MaxObject.post(str);
		}
		
	}
	
	
// ===== main ------------------------------------------------------------------------------------
	
	public static void main(String[] args){
		ChordForm cf = new ChordForm(TestData.chordProgressionAmGFE7());
		Pipe p = new Pipe("test");
		
//		ED[] embOptions = new ED[]{new ED("d", 1)};
//		Pluggable plug = new PlugInEmbellishmentOptioneer(
//				new double[]{-1.0},
//				new double[]{1.0},
//				1.0,
//				embOptions,
//				new double[]{1.0});
		
		p.addPlugIn(new PlugInMelodyContourGuideToneEveryBar());
		ED[] embOptions = new ED[]{new ED("d", 1)};
		PlugInEmbellishmentOptioneer plug = new PlugInEmbellishmentOptioneer(
				new double[]{-1.0},
				new double[]{1.0},
				1.0,
				embOptions,
				new double[]{});

		// cycle tests
		PlayPlugArgument ppa = new PlayPlugArgument();
		ppa.cf = cf;
		ppa.rnd = new RandomNumberSequence(16, 24);
		ppa.cd = new ContourData();
		System.out.println("---------------------------------------\n" + plug.toString());
		LiveClip lc = p.makeNoteList(ppa).makeLiveClip();
		System.out.println(lc.toString());
		
		p.addPlugIn(plug);
		System.out.println("---------------------------------------\n" + plug.toString());
		lc = p.makeNoteList(ppa).makeLiveClip();
		System.out.println(lc.toString());
		
		plug.setCycle(2);
		System.out.println("---------------------------------------\n" + plug.toString());
		lc = p.makeNoteList(ppa).makeLiveClip();
		System.out.println(lc.toString());
		
		// xml tests
//		plug.savePresetXMLFile("poopy");
//		System.out.println("---------------------------------------\n" + plug.toString());
//		plug.loadPreset("poopy");
//		System.out.println("---------------------------------------\n" + plug.toString());
//		plug.loadPreset("poopyNoteRangeArray");
//		System.out.println("---------------------------------------\n" + plug.toString());
//		plug.loadPreset("poopyNoteRangeList");
//		System.out.println("---------------------------------------\n" + plug.toString());
//		
		
		
//		Pluggable plugGen = new PlugInBassRootEveryBar();
//		p.addPlugIn(plugGen);
//		p.addPlugIn(plug);
		
//		PlayPlugArgument ppa = new PlayPlugArgument();
//		ppa.cf = cf;
//		ppa.rnd = new RandomNumberSequence(16, 24);
//		
//		LiveClip lc = p.makeNoteList(ppa).makeLiveClip();
//		System.out.println(lc.toString());
		
//		for (Object f: plug.getListOfAllPresets()){
//			File ff = null;
//			if (f instanceof File){
//				ff = (File)f;
//				System.out.println(ff.getName());
////				System.out.println(getFileNameWithoutExtension(ff));
//			}
////			System.out.println(f.toString());
////			System.out.println(f.getClass());
//			
//		}
		
		
	}

	




	




	

	
}
