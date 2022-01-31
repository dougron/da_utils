package main.java.da_utils.algorithmic_models.pipeline.gui;


import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JOptionPane;

import main.java.da_utils.algorithmic_models.pipeline.Pipeline;

public class PipelineSwingPanel {

  public PipelineSwingPanel(Pipeline p) {
	  makeDialog2();
  }
  public PipelineSwingPanel(){
	  makeDialog2();
  }
  public PipelineSwingPanel(PipelineSwingPanelConsoleTest p) {
	  makeDialog2();
  }
  private void makeDialog(){
	  String message = "\"The Comedy of Errors\"\n"
		        + "is considered by many scholars to be\n"
		        + "the first play Shakespeare wrote";
	  JOptionPane.showMessageDialog(null, message, "Dialog",
			  JOptionPane.ERROR_MESSAGE);
  }
  private void makeDialog2(){
	  JDialog jd = new JDialog(new JFrame(), "howdy doody", true);
	  jd.setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);
	  jd.setVisible(true);
  }
}

