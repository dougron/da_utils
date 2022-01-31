package GUIObjects;

import java.awt.BorderLayout;
import java.awt.EventQueue;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;
import java.awt.FlowLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class FileBrowserPanelTestFrame extends JFrame implements FileBrowserParent{

	private JPanel contentPane;

	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					FileBrowserPanelTestFrame frame = new FileBrowserPanelTestFrame();
					
					frame.setVisible(true);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}
	
	private String homeDir = "C:/Users/Doug/Documents/_MASTER OF UNIBERSE/Repetition text files/CorpusMelodyFiles";
	private String dialogTitle = "Select a liveclip";
	private String[] filter = new String[]{"liveclip"};
	private String savePath = "C:/Users/Doug/Documents/_MASTER OF UNIBERSE/Repetition text files/FileBrowserData/testBrowser.data";
	private FileBrowserPanel fbp;
	/**
	 * Create the frame.
	 */
	public FileBrowserPanelTestFrame() {
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setBounds(100, 100, 450, 300);
		contentPane = new JPanel();
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		setContentPane(contentPane);
		contentPane.setLayout(new FlowLayout(FlowLayout.CENTER, 5, 5));
		addTestPanel(contentPane);
		addSetFileTestButton(contentPane);
	}

	private void addSetFileTestButton(JPanel contentPane2) {
		JButton button = new JButton("TestSetFile");
		button.addActionListener(new ActionListener(){
			public void actionPerformed(ActionEvent ae){
				fbp.setFile(homeDir + "/BlueBossa.liveclip");
			}
		});
		contentPane.add(button);
		
	}

	private void addTestPanel(JPanel contentPane2) {
		fbp = new FileBrowserPanel(homeDir, dialogTitle, filter, savePath, 5, this);
//		fbp.addItemToComboBox("...");
//		fbp.addItemToComboBox("poopyy");
//		fbp.addItemToComboBox("xxxcy");
		contentPane.add(fbp);
		
	}

	@Override
	public void fileBrowserIsUpdated() {
		System.out.println("FileBrowserTestFrame.fileBrowserIsUpdated() called"); 
		
	}

}
