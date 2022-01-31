package liveclipManager;

import java.awt.BorderLayout;
import java.awt.Component;
import java.awt.EventQueue;
import java.awt.FlowLayout;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.FilenameFilter;
import java.io.IOException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerConfigurationException;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;

import org.apache.commons.io.FilenameUtils;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.w3c.dom.Attr;
import org.w3c.dom.DOMException;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

import com.cycling74.max.Atom;

import GUIObjects.ClipAndChordBrowser;
import GUIObjects.FileBrowserPanel;
import GUIObjects.FileBrowserParent;
import main.java.da_utils.ableton_live.ableton_live_clip.LiveClip;
import main.java.da_utils.ableton_live.ableton_live_clip.LiveMidiNote;
import main.java.da_utils.ableton_live.clip_injector.ClipInjector;
import main.java.da_utils.ableton_live.clip_injector.ClipInjectorObject;
import main.java.da_utils.ableton_live.clip_injector.ClipInjectorParent;
import main.java.da_utils.algorithmic_models.melody_segmenter.BatchSegmenter;
import main.java.da_utils.algorithmic_models.melody_segmenter.MelodySegmenterExcelMaker;
import main.java.da_utils.algorithmic_models.melody_segmenter.PhantomLiveClip;
import main.java.da_utils.combo_variables.DoubleAndString;
import main.java.da_utils.resource_objects.ChordForm;
import main.java.da_utils.xml_maker.MXM;
import main.java.da_utils.xml_maker.MusicXMLMaker;
import main.java.da_utils.xml_maker.key.XMLKeyZone;
import main.java.da_utils.xml_maker.time_signature.XMLTimeSignatureZone;

public class LiveClipManager extends JFrame implements FileBrowserParent, ClipInjectorParent {

	private JPanel contentPane;
	private String settingsPath = "D:/DougzJavaz/DAUtils/src/liveclipManager/liveClipManagerSettings.xml";
	private String browserPath;
	private String clipSavePath = "D:/DougzJavaz/DAUtils/src/liveclipManager/contentClipList.data";
	private String chordsSavePath = "D:/DougzJavaz/DAUtils/src/liveclipManager/contentChordsList.data";
	private int listLength = 15;
	private String clipPath = "C:/Users/Doug/Documents/_MASTER OF UNIBERSE/Repetition text files/CorpusMelodyFiles";
	private String chordsPath = "C:/Users/Doug/Documents/_MASTER OF UNIBERSE/Repetition text files/ChordProgressionTestFiles";
	
	private JFrame mainFrame = this;
	private ClipAndChordBrowser cncBrowser;
	private String musicXMLFolderPath = "C:/Users/Doug/Documents/_MASTER OF UNIBERSE/Repetition text files/LiveClipManager output";
	private String batchSourcePath;
	private String batchDestinationPath;
	private ClipInjectorObject cio;
	private String segmentationDirPath;
	private int xmlBoundaryTextSize = 5;
	
	private static final DateFormat df = new SimpleDateFormat("yyyy_MMdd_HHmm_ss_SSS");
	

	public LiveClipManager() {
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setBounds(100, 100, 450, 300);
		readSettingsFile();
		contentPane = new JPanel();
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		contentPane.setLayout(new BorderLayout(0, 0));
		setContentPane(contentPane);
		cio = new ClipInjectorObject(2, 0, ClipInjectorObject.ofTrackType);
//		cio.setPort(7808);
		addTerminationCode();
		addContent(contentPane);
	}
	
	
	
	private void addContent(JPanel cp) {
		addClipAndChordBrowser(cp);
		addFunctionButtons(cp);
	}



	private void addFunctionButtons(JPanel cp) {
		
		JPanel buttonPanel = new JPanel(new GridBagLayout());
		GridBagConstraints gbc = new GridBagConstraints();
		gbc.gridx = 0;
		gbc.gridy = 0;
		buttonPanel.add(resaveWithChordsPathButton(), gbc);
		gbc.gridx = 1;
		buttonPanel.add(makeXMLOfMelodyAndChords(), gbc);
		gbc.gridx = 0;
		gbc.gridy = 1;
		buttonPanel.add(sendMelodyClipToLiveViaUDP(), gbc);
		gbc.gridx = 1;
		buttonPanel.add(sendChordClipToLiveViaUDP(), gbc);
		gbc.gridx = 0;
		gbc.gridy = 2;
		buttonPanel.add(makeSegmentationFilesButton(), gbc);
		gbc.gridx = 1;
		gbc.gridy = 2;
		buttonPanel.add(makeSegmentationBatchFilesButton(), gbc);
		cp.add(buttonPanel, BorderLayout.NORTH);
	}
	private Component makeSegmentationBatchFilesButton() {
		JButton butt = new JButton("make batch segmentation files");
		butt.addActionListener(new ActionListener(){

			@Override
			public void actionPerformed(ActionEvent e) {
				
				JFileChooser chooser = new JFileChooser();				
				chooser.setFileSelectionMode( JFileChooser.FILES_AND_DIRECTORIES ); 
			    chooser.setCurrentDirectory(new File(batchSourcePath));
			    chooser.setDialogTitle("Select source directory");
			    chooser.setAcceptAllFileFilterUsed(false);
			    int userSourceSelection = chooser.showSaveDialog(mainFrame);
				
			    JFileChooser dhooser = new JFileChooser();				
				dhooser.setFileSelectionMode( JFileChooser.FILES_AND_DIRECTORIES ); 
			    dhooser.setCurrentDirectory(new File(batchDestinationPath));
			    dhooser.setDialogTitle("Select destination directory");
			    dhooser.setAcceptAllFileFilterUsed(false);
			    int userDestinationSelection = dhooser.showSaveDialog(mainFrame);
				    
			    if (userSourceSelection == JFileChooser.APPROVE_OPTION && userDestinationSelection == JFileChooser.APPROVE_OPTION) {
//					System.out.println("getCurrentDirectory(): " 
//					         +  chooser.getCurrentDirectory());
//					      System.out.println("getSelectedFile() : " 
//					         +  chooser.getSelectedFile());
					File sFile = chooser.getSelectedFile();
					File sourcedir;
					if (sFile.isDirectory()){
						sourcedir = chooser.getSelectedFile();
					} else {
						sourcedir = chooser.getCurrentDirectory();
					}
					batchSourcePath = sourcedir.getAbsolutePath();
			        File[] fList = sourcedir.listFiles(new FilenameFilter(){

						@Override
						public boolean accept(File arg0, String filename) {
							return filename.endsWith(".liveclip");
						}
			        	
			        });
			        
			        File dFile = dhooser.getSelectedFile();
					File destinationdir;
					if (dFile.isDirectory()){
						destinationdir = dhooser.getSelectedFile();
					} else {
						destinationdir = dhooser.getCurrentDirectory();
					}
					batchDestinationPath = destinationdir.getAbsolutePath();
			        
			        String namePrefix = makeLatestRenderName();
			        BatchSegmenter[] bsArr = new BatchSegmenter[fList.length];
			        int index = 0;
			        
			        for (File f: fList){
			        	System.out.println("found " + f.getAbsolutePath());
			        	BatchSegmenter bs = makeBatchXMLfromFileAndSave(namePrefix, f);
			        	bsArr[index] = bs;
			        	index++;
			        }
			        XSSFWorkbook wb = MelodySegmenterExcelMaker.makeMelodySegmenterWorkBook(bsArr, false, 10);
			        try {
						FileOutputStream out = new FileOutputStream( 
								new File(batchDestinationPath + "/" + namePrefix + "_" + ".xlsx"));
						wb.write(out);
						out.close();
					} catch (Exception ex){
				    	JOptionPane.showMessageDialog(mainFrame, "There was a problem saving the .xlsx file");
				
					}
				}				
			}
		});
		return butt;
	}



	protected BatchSegmenter makeBatchXMLfromFileAndSave(String namePrefix, File f) {
		LiveClip lc = new LiveClip(0, 0);
		try {
			lc.instantiateClipFromBufferedReader(new BufferedReader(new FileReader(f)));
			//System.out.println(lc.toString() + "\nmaking XL and XML");
			//.....	
		} catch (FileNotFoundException e1) {
			e1.printStackTrace();
		}
		BatchSegmenter bs = new BatchSegmenter(
		    		BatchSegmenter.fullListOfSegmentationModels(), 
		    		new double[]{1.0},
		    		0.6,
		    		new PhantomLiveClip(lc));
		    
		MusicXMLMaker mxm = makeMusicXML(bs);
		String path = batchDestinationPath + "/" + namePrefix + "_" + lc.name + ".xml";
		mxm.makeXML(path); 
		System.out.println("saved file " + path);
		return bs;
	}



	private Component makeSegmentationFilesButton() {
		JButton butt = new JButton("make segmentation files");
		butt.addActionListener(new ActionListener(){

			@Override
			public void actionPerformed(ActionEvent e) {
				if (cncBrowser.hasSelectedClipFile()){
					LiveClip lc = new LiveClip(0, 0);
					try {
						lc.instantiateClipFromBufferedReader(new BufferedReader(new FileReader(cncBrowser.getSelectedClipFile())));
						//System.out.println(lc.toString() + "\nmaking XL and XML");
						//......
						
						
					} catch (FileNotFoundException e1) {
						e1.printStackTrace();
					}
					JFileChooser fileChooser = new JFileChooser();
					fileChooser.setDialogTitle("Specify a filename. A .xlsx and .xml file will be saved");  
					fileChooser.setCurrentDirectory(new File(segmentationDirPath));
					 
					int userSelection = fileChooser.showSaveDialog(mainFrame);
					 
					if (userSelection == JFileChooser.APPROVE_OPTION) {
					    File fileToSave = fileChooser.getSelectedFile();
					    File newPath = fileChooser.getCurrentDirectory();
					    String filename = FilenameUtils.getName(fileToSave.getAbsolutePath());
					    String filenameWithoutExt = FilenameUtils.removeExtension(filename);
					    String xmlFileName = newPath + "/" + filenameWithoutExt + ".xml";
					    String xlFileName = newPath + "/" + filenameWithoutExt + ".xlsx";
					    
					    BatchSegmenter bs = new BatchSegmenter(
					    		BatchSegmenter.fullListOfSegmentationModels(), 
					    		new double[]{1.0},
					    		0.6,
					    		new PhantomLiveClip(lc));
					    
					    MusicXMLMaker mxm = makeMusicXML(bs);
					    int problem = 0;
					    try {
					    	mxm.makeXML(xmlFileName);
					    } catch (Exception ex){
					    	JOptionPane.showMessageDialog(mainFrame, "There was a problem saving the .xml file");
					    	problem++;
					    }
					    
					    XSSFWorkbook wb = MelodySegmenterExcelMaker.makeMelodySegmenterWorkBook(new BatchSegmenter[]{bs}, false, 10);
					    
					    try {
							FileOutputStream out = new FileOutputStream( 
									new File(xlFileName));
							wb.write(out);
							out.close();
						} catch (Exception ex){
					    	JOptionPane.showMessageDialog(mainFrame, "There was a problem saving the .xlsx file");
					    	problem++;
						}
					    
//					    System.out.println("Save as file: " + fileToSave.getAbsolutePath());
//					    System.out.println("new segmentation path: " + newPath.getAbsolutePath());
//					    System.out.println("filename: " + filename);
//					    System.out.println("filenameWithoutExt: " + filenameWithoutExt);
					    if (problem == 0){
					    	JOptionPane.showMessageDialog(mainFrame, "All files saved");
					    	
					    } else if (problem == 1){
					    	JOptionPane.showMessageDialog(mainFrame, "Files saved, excluding aforementioned problem");
					    	
					    } else if (problem == 2){
					    	JOptionPane.showMessageDialog(mainFrame, "No files saved.");
					    	
					    }
					    
					}
					
				}
				
			}
			
		});
		return butt;
	}



	protected MusicXMLMaker makeMusicXML(BatchSegmenter bs) {
		MusicXMLMaker mxm = new MusicXMLMaker(MXM.KEY_OF_C);
		//mxm.setLandscapePageOrientation();	// does not work. need to tweak import settings in Sibelius to get landscape orientation

		mxm.measureMap.addNewTimeSignatureZone(new XMLTimeSignatureZone(bs.plc().lc.signatureNumerator, bs.plc().lc.signatureDenominator, bs.plc().lc.loopEndBarCount())); 
		mxm.keyMap.addNewKeyZone(new XMLKeyZone(MXM.KEY_OF_C, bs.plc().lc.barCount()));
		
		String partName = bs.plc().name;
		mxm.addPart(partName, bs.plc().lc); //, int signatureNumerator, int signatureDenominator, String fileName, int barCount);
		
		for (String key: bs.getBoundaryListMap().keySet()){
			for (Double d: bs.getBoundaryListMap().get(key)){
				mxm.addTextDirection(partName, key, d, MXM.PLACEMENT_ABOVE, xmlBoundaryTextSize );					
			}
		}
		for (Double d: bs.boundaryList()){
			mxm.addTextDirection(partName, "xxx", d, MXM.PLACEMENT_BELOW, xmlBoundaryTextSize);
			
		}
		return mxm;
	}



	private Component sendChordClipToLiveViaUDP() {
		JButton butt = new JButton("send chords via UDP");
		butt.addActionListener(new ActionListener(){

			@Override
			public void actionPerformed(ActionEvent e) {
				if (cncBrowser.hasSelectedChordsFile()){
					LiveClip lc = new LiveClip(0, 0);
					try {
						lc.instantiateClipFromBufferedReader(new BufferedReader(new FileReader(cncBrowser.getSelectedChordsFile())));
						System.out.println(lc.toString());
						cio.sendClip(lc);
						JOptionPane.showMessageDialog(mainFrame, "Chord clip sent on port " + cio.port);
					} catch (FileNotFoundException e1) {
						JOptionPane.showMessageDialog(mainFrame, "Chord .liveclip file not found. No clip sent");
						
						e1.printStackTrace();
					}
					
				}
				
			}
			
		});
		return butt;
	}



	private Component sendMelodyClipToLiveViaUDP() {
		JButton butt = new JButton("send melody via UDP");
		butt.addActionListener(new ActionListener(){

			@Override
			public void actionPerformed(ActionEvent e) {
				if (cncBrowser.hasSelectedClipFile()){
					LiveClip lc = new LiveClip(0, 0);
					try {
						lc.instantiateClipFromBufferedReader(new BufferedReader(new FileReader(cncBrowser.getSelectedClipFile())));
						cio.sendClip(lc);
						JOptionPane.showMessageDialog(mainFrame, "Melody clip sent on port " + cio.port);
						
					} catch (FileNotFoundException e1) {
						JOptionPane.showMessageDialog(mainFrame, "Melody .liveclip file not found. No clip sent");
						
						e1.printStackTrace();
					}
					
				}
				
			}
			
		});
		return butt;
	}



	private JButton makeXMLOfMelodyAndChords() {
		JButton butt = new JButton("chords and melody XML");
		butt.addActionListener(new ActionListener(){

			@Override
			public void actionPerformed(ActionEvent arg0) {
				if (cncBrowser.hasSelectedClipFile()){
					try {
						LiveClip lc = new LiveClip(0, 0);
						lc.instantiateClipFromBufferedReader(new BufferedReader(new FileReader(cncBrowser.getSelectedClipFile())));
						MusicXMLMaker mxm = makeMelodyMXM(lc);
						if (cncBrowser.hasSelectedChordsFile()){
							LiveClip chords = new LiveClip(0, 0);
							chords.instantiateClipFromBufferedReader(new BufferedReader(new FileReader(cncBrowser.getSelectedChordsFile())));
							addChordsToMelody(mxm, lc, chords);
						} else {
							JOptionPane.showMessageDialog(mainFrame, "No chords file selected. proceeding with only melody");
							//System.out.println("no chords file selected. proceeding with only melody");
						}
						String path = outputXML(mxm, lc);
						JOptionPane.showMessageDialog(mainFrame, "File saved to " + path);
						
					} catch (FileNotFoundException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
				} else {
					JOptionPane.showMessageDialog(mainFrame, "No clip file selected. no XML generated");
					//System.out.println("no clip file selected. no XML generated");
				}
				
			}
			
		});
		return butt;
	}



	protected String outputXML(MusicXMLMaker mxm, LiveClip lc) {
		String fileName = lc.name;
		String outputPath = musicXMLFolderPath + "/" + fileName + ".xml";
		System.out.println(outputPath + " - file created" );
		mxm.makeXML(outputPath);
		return outputPath;
	}



	protected void addChordsToMelody(MusicXMLMaker mxm, LiveClip lc, LiveClip chords) {
//		equalizeStartPoints(lc, chords);
		ChordForm cf = new ChordForm(chords);
		double diff = lc.loopStart - chords.loopStart;
		for (DoubleAndString das: cf.getListOfChords()){
			//println("placing " + das.str + " at " + das.d);
			//mxm.addTextDirection(chordPartName, das.str, das.d, MXM.PLACEMENT_ABOVE);
			mxm.addTextDirection(lc.name, cf.getPrevailingCIKO(das.d, lc).toStringKeyChordAndSimpleFunction(), das.d + diff, MXM.PLACEMENT_ABOVE);
			
		}
	}



//	private void equalizeStartPoints(LiveClip lc, LiveClip chords) {
//		if (lc.loopStart == chords.loopStart){
//			//...do nothing
//		} else {
//			double diff = lc.loopStart - chords.loopStart;
//			chords.loopStart = lc.loopStart;
//			chords.loopEnd = lc.loopEnd;
//			chords.length = lc.length;
//			for (LiveMidiNote lmn: chords.noteList){
//				lmn.position += diff;
//			}
//		}
//		
//	}



	protected MusicXMLMaker makeMelodyMXM(LiveClip lc) {
		
		MusicXMLMaker mxm = new MusicXMLMaker(lc.getNotationKeySignature());
		mxm.measureMap.addNewTimeSignatureZone(new XMLTimeSignatureZone(lc.signatureNumerator, lc.signatureDenominator, lc.loopEndBarCount())); 
		mxm.keyMap.addNewKeyZone(new XMLKeyZone(lc.getNotationKeySignature(), lc.loopEndBarCount()));
		String partName = lc.name;
		mxm.addPart(partName, lc); //, int signatureNumerator, int signatureDenominator, String fileName, int barCount);
		
		
		return mxm;
	}



	private JButton resaveWithChordsPathButton() {
		JButton butn = new JButton("Resave with chords path");
		//cp.add(butn, BorderLayout.NORTH);
		butn.addActionListener(new ActionListener(){
			public void actionPerformed(ActionEvent e){
				Object[] options = {"Yebo",
                        "Haibo",};
                        //"No eggs, no ham!"};

				int result = JOptionPane.showOptionDialog(mainFrame,
                        "Save chord file path with melody?",
                        "Yo",
                        JOptionPane.YES_NO_OPTION,
                        JOptionPane.QUESTION_MESSAGE,
                        null,
                        options,
                        options[1]);
				//System.out.println("result=" + result);
				if (result == 0){
					System.out.println("doing it...");
					if (cncBrowser.hasSelectedClipFile() && cncBrowser.hasSelectedChordsFile()){
						LiveClip lc = new LiveClip(0, 0);
						//LiveClip lv = new LiveClip(0, 0);
						try {
							lc.instantiateClipFromBufferedReader(new BufferedReader(new FileReader(cncBrowser.getSelectedClipFile())));
							lc.setChordsClipPath(cncBrowser.getSelectedChordsFile().getPath());
							String path = cncBrowser.getSelectedClipFile().getPath();
							writeLiveClipToFile(path, lc);
							System.out.println("file written to " + path);
						} catch (FileNotFoundException e1) {
							// TODO Auto-generated catch block
							e1.printStackTrace();
						}
					} else {
						System.out.println("clip and/or chord file missing. no file saved");
					}
					
				} else {
					System.out.println("not doing it....");
				}
			}
		});
		return butn;
	}



	private void writeLiveClipToFile(String filepath, LiveClip lc) {
		try {
			FileWriter fw = new FileWriter(filepath);
			BufferedWriter bw = new BufferedWriter(fw);
//			System.out.println("writeLiveClipToFile called:");
//			System.out.println(lc.toString());
			String str = lc.getClipAsTextFile();
//			System.out.println(str);
			bw.write(str);
			bw.close();
			fw.close();
		} catch (IOException ex){
			System.out.println("writeLiveClipToFile failed to work");
			System.out.println(ex.getMessage());
		}
		
	}


	private void addClipAndChordBrowser(JPanel cp) {
		cncBrowser = new ClipAndChordBrowser(this, FileBrowserPanel.BUTTONS_RIGHT, clipPath, chordsPath, clipSavePath, chordsSavePath, listLength);
		
		cp.add(cncBrowser, BorderLayout.CENTER);
	}



	private void addTerminationCode() {
		addWindowListener(new WindowAdapter(){
		    public void windowClosing(WindowEvent e){
		        System.out.println("termination event");
		        writeSettingsFile();
		    }
		});
		
	}



	protected void writeSettingsFile() {
		try {
			DocumentBuilderFactory docFactory = DocumentBuilderFactory.newInstance();
			DocumentBuilder docBuilder = docFactory.newDocumentBuilder();

			// root elements
			Document doc = docBuilder.newDocument();
			Element rootElement = doc.createElement("liveclipmanager");
			doc.appendChild(rootElement);
			
			Element settings = doc.createElement("settings");
			rootElement.appendChild(settings);
			
			// set attribute to settings element - for future use
//			Attr attr = doc.createAttribute("id");
//			attr.setValue("1");
//			settings.setAttributeNode(attr);
			
			// shortened way
			// settings.setAttribute("id", "1");
			
			// default_browser_path elements
			Element default_browser_path = doc.createElement("default_browser_path");
			default_browser_path.appendChild(doc.createTextNode(browserPath));
			settings.appendChild(default_browser_path);
			
			Element cheeky_test_element = doc.createElement("cheeky_test_element");
			cheeky_test_element.appendChild(doc.createTextNode("poopy:)"));
			settings.appendChild(cheeky_test_element);
			
			Element segmentation_dir_element = doc.createElement("segmentation_directory_path");
			segmentation_dir_element.appendChild(doc.createTextNode(segmentationDirPath));
			settings.appendChild(segmentation_dir_element);
			
			Element batch_dir_element = doc.createElement("batch_source_path");
			batch_dir_element.appendChild(doc.createTextNode(batchSourcePath));
			settings.appendChild(batch_dir_element);
			
			Element batch_dest_element = doc.createElement("batch_destination_path");
			batch_dest_element.appendChild(doc.createTextNode(batchDestinationPath));
			settings.appendChild(batch_dest_element);
			
			// write the content into xml file
			TransformerFactory transformerFactory = TransformerFactory.newInstance();
			Transformer transformer = transformerFactory.newTransformer();
			DOMSource source = new DOMSource(doc);
			StreamResult result = new StreamResult(new File(settingsPath));
			
			// Output to console for testing
			// StreamResult result = new StreamResult(System.out);

			transformer.transform(source, result);

			System.out.println("settings file saved!");
			
			
		} catch (DOMException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (ParserConfigurationException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (TransformerConfigurationException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (TransformerException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
	}



	private void readSettingsFile() {
		try {
			File fXmlFile = new File(settingsPath);
			DocumentBuilderFactory dbFactory = DocumentBuilderFactory.newInstance();
			DocumentBuilder dBuilder = dbFactory.newDocumentBuilder();
			Document doc = dBuilder.parse(fXmlFile);
			doc.getDocumentElement().normalize();
			
			NodeList nList = doc.getElementsByTagName("settings");
			
			Node nNode = nList.item(0);			// can't yet find a way to get element <settings> as a Node so i get it as a NodelIst of length of 1;
			Element eElement = (Element) nNode;
			
			browserPath = eElement.getElementsByTagName("default_browser_path").item(0).getTextContent();
			segmentationDirPath = eElement.getElementsByTagName("segmentation_directory_path").item(0).getTextContent();
			batchSourcePath = eElement.getElementsByTagName("batch_source_path").item(0).getTextContent();
			batchDestinationPath = eElement.getElementsByTagName("batch_destination_path").item(0).getTextContent();
			printSettings();
			
		} catch (ParserConfigurationException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (SAXException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
	}



	private void printSettings() {
		System.out.println("settings-----------");
		System.out.println("browser path: " + browserPath);
		System.out.println("settings end-------");
	}
	private String makeLatestRenderName() {
		Date dateobj = new Date();
		return df.format(dateobj);
		
	}



	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					LiveClipManager frame = new LiveClipManager();
					frame.setVisible(true);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}


// FileBrowserParent methods ======================================================
	@Override
	public void fileBrowserIsUpdated() {
		// TODO Auto-generated method stub
		
	}


// ClipInjectorParent methods ======================================================
	@Override
	public void consolePrint(String str) {
		// TODO Auto-generated method stub
		
	}



	@Override
	public void sendClipObjectMessage(Atom[] atArr) {
		// TODO Auto-generated method stub
		
	}

}
