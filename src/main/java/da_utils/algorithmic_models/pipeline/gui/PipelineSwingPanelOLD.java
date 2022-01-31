package main.java.da_utils.algorithmic_models.pipeline.gui;

import static javax.swing.GroupLayout.Alignment.CENTER;

//import java.awt.BorderLayout;
import java.awt.Container;
import java.awt.Dimension;
//import java.awt.Font;
//import java.awt.GraphicsEnvironment;
//import java.awt.event.ActionEvent;
//import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

//import javax.swing.BorderFactory;
import javax.swing.DefaultListModel;
import javax.swing.GroupLayout;
//import javax.swing.JButton;
import javax.swing.JFrame;
//import javax.swing.JLabel;
import javax.swing.JList;
//import javax.swing.JOptionPane;
//import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.ListSelectionModel;
//import javax.swing.SwingUtilities;
//import javax.swing.event.ListSelectionEvent;
//import javax.swing.event.ListSelectionListener;

import main.java.da_utils.algorithmic_models.pipeline.Pipeline;


public class PipelineSwingPanelOLD extends JFrame {


    private JList optionList;
    private JList plugList;
    private DefaultListModel optionModel;
    private DefaultListModel plugModel;
    private Pipeline parent;


    public PipelineSwingPanelOLD(Pipeline p) {
    	parent = p;
        initUI();
    }
    private void initUI() {
    	makeLists();
    	populateLists();
 
        JScrollPane optionScrollpane = new JScrollPane(optionList);
        JScrollPane plugScrollpane = new JScrollPane(plugList);

        Container pane = getContentPane();
        GroupLayout gl = new GroupLayout(pane);
        pane.setLayout(gl);
        
        gl.setAutoCreateContainerGaps(true);
        gl.setAutoCreateGaps(true);

        gl.setHorizontalGroup(gl.createSequentialGroup()
               .addComponent(optionScrollpane)
               .addGroup(gl.createParallelGroup()
               .addComponent(plugScrollpane))
        );

        gl.setVerticalGroup(gl.createParallelGroup(CENTER)
        		.addComponent(optionScrollpane)
                .addGroup(gl.createSequentialGroup()
                .addComponent(plugScrollpane))
        );

       pane.setPreferredSize(new Dimension(400, 300));

        pack();
        setTitle("JList");
        setDefaultCloseOperation(DISPOSE_ON_CLOSE);
        setLocationRelativeTo(null);
    }
    private void populateLists(){
    	optionModel.clear();
    	for (String str: parent.optionNameArray()){
    		optionModel.addElement(str);
    	}
    	plugModel.clear();
    	for (String str: parent.plugNameArray()){
    		plugModel.addElement(str);
    	}
    }
    private void makeLists(){
    	makeOptionList();
    	makePipelineList();
    }
    private void makePipelineList(){
    	plugModel = new DefaultListModel();
        plugList = new JList(plugModel);
        plugList.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        plugList.addMouseListener(new MouseAdapter(){
        	public void mouseClicked(MouseEvent evt){
        		JList list = (JList)evt.getSource();
        		if (evt.getClickCount() == 2){
        			int index = list.locationToIndex(evt.getPoint());
        			parent.removePlugIn(index);
        			populateLists();
        		} 
        	}
        });
    }
    private void makeOptionList(){
    	optionModel = new DefaultListModel();
        optionList = new JList(optionModel);
        optionList.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        optionList.addMouseListener(new MouseAdapter(){
        	public void mouseClicked(MouseEvent evt){
        		JList list = (JList)evt.getSource();
        		if (evt.getClickCount() == 2){
        			int index = list.locationToIndex(evt.getPoint());
        			parent.addPlugIn(index);
        			populateLists();
        		} 
        	}
        });
    }
    
}    