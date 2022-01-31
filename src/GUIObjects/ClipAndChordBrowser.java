package GUIObjects;

/*
 * inelegant extension of the FileBrowserPanel class that is customised for finding
 * .liveclip files and automatically connecting it with related chord file
 * this relationship is established by filename only, though today I will
 * extend this to the chordsClipPath newly added into the LiveClip
 */

import java.awt.FlowLayout;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.util.ArrayList;

import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JFileChooser;
import javax.swing.JLabel;
import javax.swing.JPanel;

import main.java.da_utils.ableton_live.ableton_live_clip.LiveClip;

public class ClipAndChordBrowser extends JPanel implements FileBrowserParent{

	private static String clipDialogTitle = "Select a melody clip....";
	private String chordsDialogTitle = "Select a chord clip....";
	private static String[] clipFilter = new String[]{"liveclip"};
	private String[] chordsFilter = new String[]{"chords.liveclip"};
	private static String clipSavePath; //= "C:/Users/Doug/Documents/_MASTER OF UNIBERSE/Repetition text files/FileBrowserData/cncClipList.data";
	private String chordsSavePath; //= "C:/Users/Doug/Documents/_MASTER OF UNIBERSE/Repetition text files/FileBrowserData/cncChordsList.data";
	
	private static String clipPath;// = "C:/Users/Doug/Documents/_MASTER OF UNIBERSE/Repetition text files/CorpusMelodyFiles";	
	private String chordsPath;// = "C:/Users/Doug/Documents/_MASTER OF UNIBERSE/Repetition text files/ChordProgressionTestFiles";
	private static int listLength = 15;
	
	private FileBrowserPanel chordBrowser;
	
	public JComboBox<String> comboBox = new JComboBox<String>();;
	private String tempHomeDirectoryPath;
	private String dialogTitle;
	private String[] extensionFilter;
	private FileBrowserFilter fileFilter;
	File selectedFile;
	private boolean hasSelectedFile = false;
	public ArrayList<File> fileList = new ArrayList<File>();
	private String recentFilesSavePath;
	private FileBrowserParent parent;
	private boolean hasParent = false;
	private File parentMessageTestFile;
	private int layout = BUTTONS_RIGHT;
	private boolean hasCustomActionListener = false;
	private JButton nullButton;
	private JButton browseButton;


	public ClipAndChordBrowser(FileBrowserParent parent, int layout, String clipPath, String chordsPath, String clipSavePath, String chordsSavePath, int listLength) {
		this.listLength = listLength;
		this.clipPath = clipPath;
		this.chordsPath = chordsPath;
		this.clipSavePath = clipSavePath;
		this.chordsSavePath = chordsSavePath;
		this.parent = parent;
		this.layout = layout;
		this.hasParent = true;
		init(clipPath, clipDialogTitle, clipFilter, clipSavePath, listLength);


		makeChordsBrowser();
//		makeNewComboBoxActionListeners();
	}
	public boolean hasSelectedClipFile(){
		return hasSelectedFile;
	}
	public boolean hasSelectedChordsFile(){
		return chordBrowser.hasSelectedFile();
	}
	public File getSelectedChordsFile(){
		if (chordBrowser.hasSelectedFile()){
			return chordBrowser.getSelectedFile();
		} else {
			return null;
		}
	}
	public String selectedClipFilePath(){
		if (hasSelectedFile){
			return selectedFile.getPath();
		} else {
			return null;
		}
	}
	public String selectedChordsFilePath(){
		if (chordBrowser.hasSelectedFile()){
			return chordBrowser.selectedFile.getPath();
		} else {
			return null;
		}
	}
	public File getSelectedClipFile(){
		if (hasSelectedFile){
			return selectedFile;
		} else {
			return null;
		}
	}
	public void updateComboBox(int selectedIndex) {
		comboBox.removeAllItems();
		for (File file: fileList){
			comboBox.addItem(file.getName());
		}		
	}
	public void setSelectedFile(String path){
		//System.out.println("FileBrowserPanel.setSelectedFile() called: " + path);
		//for (String str: extensionFilter){
		//	System.out.println(str);
		//}		
		File file = new File(path);
		if (file.isFile()){
			if (fileFilter.accept(file)){
				//System.out.println("setting file...");
				addFile(file);
				parentMessage();
			}
		} else {
			//System.out.println("file does not exist");
		}
		
	}
	public void setSelectedFileToNull(){
		addFile(new NullFile());
		//parentMessage();
	}

	public void setFile(String path){
		//System.out.println("FileBrowserPanel.setFile() called: " + path);
		if (path == null){
			setSelectedFileToNull();
		} else {
			File file = new File(path);
			if (fileFilter.accept(file)){
				addFile(file);
//				parentMessage();
			}
		}
		
	}	
	public void setChordsFile(String path){
		chordBrowser.setFile(path);
	}

	@Override
	public void fileBrowserIsUpdated() {
		//System.out.println("ClipAndChordBrowser.fileBrowserIsUpdated() called...");
		parent.fileBrowserIsUpdated();
	}
	

		
		public String toString(){
			String str = "FileBrowserPanel";
			str += "\ntempHomeDirectoryPath:" + tempHomeDirectoryPath;
			str += "\ndialogTitle:" + dialogTitle;
			str += "\nextensionFilter[]:";
			for (String s: extensionFilter){
				str += s + ",";
			}
			str +="\nfileList contents:";
			for (File file: fileList){
				str += "\n   " + file.getPath();
			}
			return str;
		}
		
// privates =====================================================================================
		private void init(String homeDirPath, String dialogTitle, String[] extensionFilter, String recentFilesSavePath, int listLength){
			tempHomeDirectoryPath = homeDirPath;
			this.dialogTitle = dialogTitle;
			this.extensionFilter = extensionFilter;
			this.fileFilter = new FileBrowserFilter(extensionFilter);
			this.recentFilesSavePath = recentFilesSavePath;
			this.listLength = listLength;
			setOpaque(false);
			makeLayout();
			makeGUIElements();
			
			loadRecentFileList();
			updateComboBox(0);
			comboBox.setSelectedIndex(0);
			selectedFile = fileList.get(comboBox.getSelectedIndex());
			comboBox.addActionListener(comboBoxActionListener());
//			System.out.println(toString());
//			System.out.println(fileList.size());
		}
		private void makeChordsBrowser() {
			chordBrowser = new FileBrowserPanel(chordsPath, chordsDialogTitle, chordsFilter, chordsSavePath, listLength, this, layout);
			//	chordBrowser.
				
				
				GridBagConstraints gbc_chordsBrowser = new GridBagConstraints();
//				gbc_chordsBrowser.insets = new Insets(0, 0, 5, 5);
//				gbc_chordsBrowser.fill = GridBagConstraints.HORIZONTAL;
				gbc_chordsBrowser.gridx = 0;
				gbc_chordsBrowser.gridy = 2;
				add(chordBrowser, gbc_chordsBrowser);
			
		}
		
		private void makeGUIElements() {
			if (layout == this.BUTTONS_BELOW){
				makeComboBox();
				JPanel panel = new JPanel();
				panel.setLayout(new FlowLayout());
				panel.setOpaque(false);
				panel.add(browseButton = browseButton());
				panel.add(nullButton = nullButton());
				GridBagConstraints gbc_flowPanel = new GridBagConstraints();
//				gbc_selectedScrollPane.insets = new Insets(0, 0, 5, 5);
//				gbc_fillerLabel.fill = GridBagConstraints.HORIZONTAL;
				gbc_flowPanel.gridx = 0;
				gbc_flowPanel.gridy = 1;
				add(panel, gbc_flowPanel);
			} else {
				makeComboBox();
				makeBrowseButton();
				makeNullFileButton();
				//addFiller();
			}
			
//			makeTestButton();
		}
		private void addFiller() {
			if (layout == this.BUTTONS_BELOW){
				JLabel label = new JLabel("               ");
				GridBagConstraints gbc_fillerLabel = new GridBagConstraints();
//				gbc_selectedScrollPane.insets = new Insets(0, 0, 5, 5);
				gbc_fillerLabel.fill = GridBagConstraints.HORIZONTAL;
				gbc_fillerLabel.gridx = 2;
				gbc_fillerLabel.gridy = 1;
				add(label, gbc_fillerLabel);
			}
			
		}
		
		private void makeTestButton() {
			JButton button = new JButton();
			button.setText("test");
			button.addActionListener(new ActionListener(){
				public void actionPerformed(ActionEvent ae){
					System.out.println("hasSelectedFile=" + hasSelectedFile + " " + selectedFile.getName());
				}
			});
			GridBagConstraints gbc_testButton = new GridBagConstraints();
//			gbc_selectedScrollPane.insets = new Insets(0, 0, 5, 5);
//			gbc_browseButton.fill = GridBagConstraints.HORIZONTAL;
			gbc_testButton.gridx = 3;
			gbc_testButton.gridy = 0;
			add(button, gbc_testButton);
			
		}
		private JButton nullButton(){
			JButton button = new JButton();
			button.setText("clr");
			button.addActionListener(new ActionListener(){
				public void actionPerformed(ActionEvent ae){
					for (ActionListener al: comboBox.getActionListeners()){
						comboBox.removeActionListener(al);
					}
					fileList.add(0, new NullFile());
					updateComboBox(0);
					comboBox.setSelectedIndex(0);
					selectedFile = fileList.get(comboBox.getSelectedIndex());
					hasSelectedFile = false;
					comboBox.addActionListener(comboBoxActionListener());
					parentMessage();
				}
			});
			return button;
		}
		private void makeNullFileButton() {
			nullButton = nullButton();
			GridBagConstraints gbc_nullButton = new GridBagConstraints();
//			gbc_selectedScrollPane.insets = new Insets(0, 0, 5, 5);
//			gbc_browseButton.fill = GridBagConstraints.HORIZONTAL;
			gbc_nullButton.gridx = nullGridX[layout];
			gbc_nullButton.gridy = nullGridY[layout];
			add(nullButton, gbc_nullButton);
			
		}

		private void loadRecentFileList() {
			fileList.clear();
			try{
				
				BufferedReader bf = new BufferedReader(new FileReader(recentFilesSavePath));
				String line;
			    while ((line = bf.readLine()) != null) {
			        File file = new File(line);
			        fileList.add(file);
			    }
			} catch(Exception ex){
				
			}
			fileList.add(0, new NullFile());
			//System.out.println(fileList.size());
		}


		private JButton browseButton(){
			JButton button = new JButton();
			button.setText("...");
			button.addActionListener(new ActionListener(){
				public void actionPerformed(ActionEvent ae){
					JButton open = new JButton();
					JFileChooser fc = new JFileChooser();
					fc.setCurrentDirectory(new File(tempHomeDirectoryPath));
					fc.setDialogTitle(dialogTitle);
					fc.setFileFilter(fileFilter);
					if (fc.showOpenDialog(open) == JFileChooser.APPROVE_OPTION){
						selectedFile = fc.getSelectedFile();
						//System.out.println(selectedFile.getPath());
						//System.out.println(selectedFile.getName());
						hasSelectedFile = true;
						doComboBoxClickThing(selectedFile);
						findRelatedChordsClip(selectedFile);
						parentMessage();
					}					
				}
			});
			return button;
		}
		private void makeBrowseButton() {
			browseButton = browseButton();
			GridBagConstraints gbc_browseButton = new GridBagConstraints();
//			gbc_selectedScrollPane.insets = new Insets(0, 0, 5, 5);
//			gbc_browseButton.fill = GridBagConstraints.HORIZONTAL;
			gbc_browseButton.gridx = browseGridX[layout];
			gbc_browseButton.gridy = browseGridY[layout];
			add(browseButton, gbc_browseButton);
		}

		protected void parentMessage() {
			parent.fileBrowserIsUpdated();
//			
		}
//		protected void addSelectedFileToFileList() {
//			ArrayList<File> tempList = new ArrayList<File>();
//			tempList.add(0, selectedFile);	// selected item goes to top of list
//			
//			for (File file: fileList){
//				if (!file.getPath().equals(selectedFile.getPath()) && tempList.size() < listLength && !(file instanceof NullFile)){
//					tempList.add(file);
//				}
//			}
//			fileList = tempList;
//			//updateComboBox(0);				// ..... and gets selected
//			saveListToFile();
//		}
		private void saveListToFile() {
			try{
				BufferedWriter br = new BufferedWriter(new FileWriter(recentFilesSavePath));
				for (int i = 0; i < fileList.size(); i++){
					File file = fileList.get(i);
					if (!(file instanceof NullFile)){
						br.write(file.getPath());
						if (i < fileList.size() - 1) br.newLine();
					}			
				}
				br.close();
			} catch (Exception ex){
				
			}
			
		}

		private void makeLayout() {
			GridBagLayout gridBagLayout = new GridBagLayout();
			gridBagLayout.columnWidths = layoutColumnWidths[layout];	//new int[]{200, 25, 25};
			gridBagLayout.rowHeights = new int[]{20, 20};
			gridBagLayout.columnWeights = new double[]{0.0, 0.0, 1.0};
			gridBagLayout.rowWeights = new double[]{1.0};
			setLayout(gridBagLayout);
			
		}
//		public void addItemToComboBox(String str){
//			comboBox.addItem(str);
//		}

		private void makeComboBox() {

			GridBagConstraints gbc_comboBox = new GridBagConstraints();
//			gbc_selectedScrollPane.insets = new Insets(0, 0, 5, 5);
			gbc_comboBox.fill = GridBagConstraints.HORIZONTAL;
			gbc_comboBox.gridx = comboGridX[layout];
			gbc_comboBox.gridy = comboGridY[layout];
			gbc_comboBox.gridwidth = comboGridWidthArr[layout];
			add(comboBox, gbc_comboBox);
			
	//		comboBox.addActionListener(comboBoxActionListener());
			
			
		}
		private ActionListener comboBoxActionListener(){
			return new ActionListener(){
				public void actionPerformed(ActionEvent ae){
					//System.out.println(ae.toString());
					//System.out.println(ae.getActionCommand());
					int index = comboBox.getSelectedIndex();
					//System.out.println("comboBox.getSelectedIndex()=" + comboBox.getSelectedIndex());
					if (index < 0) index = 0;
					doComboBoxClickThing(fileList.get(index));
					//System.out.println("comboBox.getSelectedIndex()=" + comboBox.getSelectedIndex());
					findRelatedChordsClip(fileList.get(comboBox.getSelectedIndex()));
					parentMessage();
				}
			};
			
		}
		private void findRelatedChordsClip(File file) {
			//System.out.println("findRelatedChordsClip() called");
			LiveClip lc = new LiveClip(0, 0);
			boolean found = false;
			try {
				lc.instantiateClipFromBufferedReader(new BufferedReader(new FileReader(file)));
				found = true;
			} catch (FileNotFoundException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			if (found){
				if (lc.hasChordsClipPath()){
					chordBrowser.setSelectedFile(lc.getChordsClipPath());
				} else {
					findRelatedChordsClipByFilename(file);
				}
			} else {
				findRelatedChordsClipByFilename(file);
			}
			
			
			
			
			
		}; 
		private void findRelatedChordsClipByFilename(File file) {
			String path = file.getPath();
			String name = file.getName();
			//System.out.println("path:" + path);
			//System.out.println("name:" + name);
			String[] split = name.split("\\.");
			//for (String str: split){
			//	System.out.println(str);
			//}
			if (split.length == 2){
				String chordsName = split[0] + ".chords." + split[1];
				//System.out.println("chordsName=" + chordsName);
				String chordsFilePath = chordsPath + "/" + chordsName;
				//System.out.println(chordsFilePath);
				File chordFile = new File(chordsFilePath);
				if (chordFile.isFile()){
					//System.out.println("chords file found!!!!!!!!");
					chordBrowser.setSelectedFile(chordsFilePath);
				} else {
					chordBrowser.setSelectedFileToNull();
				}
			}
			
		}
		protected void doComboBoxClickThing(File file) {
			for (ActionListener al: comboBox.getActionListeners()){
				comboBox.removeActionListener(al);
			}
			ArrayList<File> tempList = new ArrayList<File>();
			tempList.add(file);
			for (File f: fileList){
				if (f instanceof NullFile){
					
				} else {
					if (!f.getPath().equals(file.getPath()) && tempList.size() < listLength){
						tempList.add(f);
					}
				}
			}
			fileList = tempList;
			updateComboBox(0);
			if (file instanceof NullFile){
				hasSelectedFile = false;
			} else {
				hasSelectedFile = true;
			}
			comboBox.setSelectedIndex(0);
			selectedFile = fileList.get(comboBox.getSelectedIndex());
			comboBox.addActionListener(comboBoxActionListener());
			saveListToFile();
		}
		
		

		private void addFile(File file){
//			fileList.add(0, file);
			doComboBoxClickThing(file);
			//System.out.println("FileBrowser set to " + file.getPath());
		}
	//public void setFile(String path, boolean b){
//		// b = false does not update mainFrame, avoids stackoverflow
//		if (b){
//			setFile(path);
//		} else {
//			File file = new File(path);
//			if (fileFilter.accept(file)){
//				addFile(file);
//				System.out.println("FileBrowser set to " + path);
//			}
//		}
	//	
	//}

		public static final int BUTTONS_RIGHT = 0;
		public static final int BUTTONS_BELOW = 1;
		public static final int[] comboGridWidthArr = new int[]{1, 3};
		public static final int[] comboGridX = new int[]{0, 0};
		public static final int[] comboGridY = new int[]{0, 0};
		public static final int[] browseGridX = new int[]{1, 0};
		public static final int[] browseGridY = new int[]{0, 1};
		public static final int[] nullGridX = new int[]{2, 2};
		public static final int[] nullGridY = new int[]{0, 1};
		public static final int[][] layoutColumnWidths = new int[][]{
			new int[]{200, 25, 25},
			new int[]{200, 0, 0}
		};


		public void setCombosEnabled(boolean b) {
			comboBox.setEnabled(b);
			nullButton.setEnabled(b);
			browseButton.setEnabled(b);
			chordBrowser.setComboEnabled(b);
		}

}
