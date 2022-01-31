package UndoList;



import java.awt.Color;
import java.awt.EventQueue;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.util.ArrayList;

import javax.swing.JFrame;

public class UndoListTestFrame extends JFrame{
	
	private int cueIndex = 2;
	private int playIndex = 4;
	private ArrayList<UndoListInterface> undoList;
	UndoListPanel panel;
	
	public UndoListTestFrame(){
		addKeyListeners();
		panel = new UndoListPanel();
		//panel.lastOnTop = false;
		setSize(300, 400);
		setLocationRelativeTo(null);
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		add(panel);
		setBackground(new Color(0, 1, 225));
		undoList = makeUndoList();
//		for (UndoListInterface uobj: undoList){
//			System.out.println(uobj.listDescription());
//		}
		panel.updatePanel(undoList, playIndex, cueIndex);
	}
	
	
	
	private ArrayList<UndoListInterface> makeUndoList() {
		ArrayList<UndoListInterface> uList = new ArrayList<UndoListInterface>();
		for (int i = 0; i < 20; i++){
			uList.add(new UndoListTestObject());
		}
		return uList;
	}



		// runnable =-=-=-=--=-====--------------------------------------------------
		public static void main(String[] args) {
	        EventQueue.invokeLater(new Runnable() {
	            @Override
	            public void run() {
	            	UndoListTestFrame ex = new UndoListTestFrame();
	                ex.setVisible(true);
	            }
	        });
	    }

		public void dealWithKeyPressed(KeyEvent e) {
			//System.out.println("keyPressed..");
			int key = e.getKeyCode();
			if (key == KeyEvent.VK_O){
				if (cueIndex < undoList.size() - 1){
					cueIndex++;
					panel.updatePanel(undoList, playIndex, cueIndex);
				}
			} else if (key == KeyEvent.VK_L){
				if (cueIndex > 0){
					cueIndex--;
					panel.updatePanel(undoList, playIndex, cueIndex);
				}			
			} else if (key == KeyEvent.VK_P){
				playIndex = cueIndex;
				panel.updatePanel(undoList, playIndex, cueIndex);
			}
			
		}
		public void dealWithKeyReleased(KeyEvent arg0) {
			// TODO Auto-generated method stub
			
		}
		public void dealWithKeyTyped(KeyEvent arg0) {
			// TODO Auto-generated method stub
			
		}
	    private void addKeyListeners(){
	    	setFocusable(true);
	        addKeyListener(new java.awt.event.KeyAdapter() {
	            @Override
	            public void keyTyped(KeyEvent e) {
	                //parent.statusMessage("you typed a key");
	            	dealWithKeyTyped(e);
	            }
	            @Override
	            public void keyPressed(KeyEvent e) {
	            	//parent.statusMessage("you pressed a key");
	        		dealWithKeyPressed(e);
	            }
	            @Override
	            public void keyReleased(KeyEvent e) {
	            	//parent.statusMessage("you released a key");
	            	dealWithKeyReleased(e);
	            }
	        });
	    }

}
