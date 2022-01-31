package jtree_utils;

import java.awt.Component;
import java.awt.Image;
import java.awt.event.MouseListener;
import java.net.URL;

import javax.swing.ImageIcon;
import javax.swing.JLabel;
import javax.swing.JTree;
import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.DefaultTreeCellRenderer;
import javax.swing.tree.TreeCellRenderer;

public class UniqueIconRenderer extends DefaultTreeCellRenderer { //implements TreeCellRenderer {
	private static final int IMAGE_WIDTH = 20;
	private JLabel label;

	public UniqueIconRenderer() {
        label = new JLabel();
    }
	public Component getTreeCellRendererComponent(JTree tree,
		      Object value,boolean sel,boolean expanded,boolean leaf,
		      int row,boolean hasFocus) {
		        super.getTreeCellRendererComponent(tree, value, sel, 
		          expanded, leaf, row, hasFocus);
		        Object o = ((DefaultMutableTreeNode)value).getUserObject();
		        // check whatever you need to on the node user object
		        if (o instanceof UniqueIcon) {
		        	UniqueIcon icon = (UniqueIcon) o;
		        	if (icon.hasUniqueIcon()){
		        		ImageIcon icon2 = getScaledIcon(icon.getIconPath());			

			            setIcon(icon2);
		        	} else {
		        		setIcon(null);
		        	}
		            setText(icon.getName());
		        } else {
		            setIcon(null);
		            setText("" + value);
		        }
		        
		        return this;
		    }


	private ImageIcon getScaledIcon(String path) {
		ImageIcon icon = new ImageIcon(path);
		Image img = icon.getImage() ;  
		Image newimg = img.getScaledInstance(IMAGE_WIDTH, IMAGE_WIDTH,  java.awt.Image.SCALE_SMOOTH) ;  
		icon = new ImageIcon( newimg );
		return icon;
	}
}

