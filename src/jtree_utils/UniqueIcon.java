package jtree_utils;

/**
 * Wraps an item for selection in a JTree with the name that will appear in the tree, the object
 * that will be returned if it is selected and the unique icon for...
 * 
 * This was used in the tree display of cell options in the RepetitionAnalysis project 
 */

public class UniqueIcon {
	

	private String name;
    private Object object;
	private String iconPath;
	private boolean hasUniqueIcon;
	

    public UniqueIcon(String name, String iconPath, Object object) {
        this.name = name;
        this.iconPath = iconPath;
        this.object = object;
        this.hasUniqueIcon = true;
    }
    public UniqueIcon(String name, Object object) {
    	// has no unique icon with this instantiator
        this.name = name;
        this.object = object;
        this.hasUniqueIcon = false;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getIconPath() {
        return iconPath;
    }
    public Object getObject(){
    	return object;
    }
    public boolean hasUniqueIcon(){
    	return hasUniqueIcon;
    }


//    public void setFlagIcon(String flagIcon) {
//        this.flagIcon = flagIcon;
//    }
}

