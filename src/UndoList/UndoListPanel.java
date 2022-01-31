package UndoList;

import java.awt.Color;
import java.awt.Component;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.util.ArrayList;

import javax.swing.JLabel;
import javax.swing.JPanel;

public class UndoListPanel extends JPanel{
	
	private int listLength = 10;		// default length
	public Color bgColor = new Color(100, 100, 200);
	public Color selectedColor = new Color(200, 0, 0);
	public Color cuedColor = new Color(0, 200, 0);
	public boolean listDirection = LAST_ON_TOP;			// true for last item in list on top of list, false for first on top

	public UndoListPanel(int listLength){
		super();
		this.listLength = listLength;
		init();
	}
	public UndoListPanel(){
		super();
		init();
	}
	private void init(){
		setLayout(new GridBagLayout());
		setBackground(bgColor);
	}
	public void updatePanel(ArrayList<UndoListInterface> uList, int playIndex, int cuedIndex){
		//System.out.println("playIndex=" + playIndex + "cuedIndex=" + cuedIndex);
		removeAllComponents();
		int[] startFinish = getStartAndFinishIndexCenteredOnCuedIndex(uList, cuedIndex);
		addItemsToPanel(uList, startFinish, playIndex, cuedIndex);
		revalidate();
	}
	private void addItemsToPanel(ArrayList<UndoListInterface> uList, int[] startFinish, int selectedIndex, int cuedIndex) {
		
		GridBagConstraints gbc = new GridBagConstraints();
		gbc.gridx = 0;
		int y = 0;
		if (listDirection){
			for (int i = startFinish[1]; i >= startFinish[0]; i--){
				gbc.gridy = y;
				add(getLabel(i, uList, startFinish, selectedIndex, cuedIndex), gbc);
				y++;
			}
		} else {
			for (int i = startFinish[0]; i <= startFinish[1]; i++){
				gbc.gridy = y;
				add(getLabel(i, uList, startFinish, selectedIndex, cuedIndex), gbc);
				y++;
			}
		}
		
//		gbc.gridy = y;
//		add(new JLabel("----------------------------------"), gbc);
		
	}
	private JLabel getLabel(int index, ArrayList<UndoListInterface> uList, int[] startFinish, int selectedIndex, int cuedIndex){
		JLabel label;
		label = new JLabel(uList.get(index).listDescription());
		if (index == selectedIndex){
			label.setBackground(selectedColor);
		} else if (index == cuedIndex){
			label.setBackground(cuedColor);
		} else {
			label.setBackground(bgColor);
		}
		label.setOpaque(true);
		return label;
	}
	private int[] getStartAndFinishIndexCenteredOnCuedIndex(ArrayList<UndoListInterface> uList, int cuedIndex) {
		int start = cuedIndex;
		int end = cuedIndex;
		boolean topout = false;
		boolean bottomout = false;
		
		if (cuedIndex > -1 && cuedIndex < uList.size()){
			
			int count = 1;
			while (count < listLength || (!topout && !bottomout)){
				if (end < uList.size() - 1){
					end++;
					count++;
					//System.out.println("end increased to " + end + " count=" + count);
				} else {
					topout = true;
				}
				if (count == listLength) break;
				if (start > 0){
					start--;
					count++;
					//System.out.println("start decreased to " + start + " count=" + count);
				} else {
					bottomout = true;
				}
				if (topout && bottomout) break;
			}
		}
		
		return new int[]{start, end};
	}
	private void removeAllComponents() {
		for (Component c: this.getComponents()){
			remove(c);
		}
		repaint();
	}
	public static final boolean LAST_ON_TOP = true;
	public static final boolean FIRST_ON_TOP = false;

}
