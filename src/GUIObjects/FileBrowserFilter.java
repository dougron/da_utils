package GUIObjects;

import java.io.File;

import javax.swing.filechooser.FileFilter;

public class FileBrowserFilter extends FileFilter {
	
	private String[] extensionArr;

	public FileBrowserFilter(String[] extensionArr){
		this.extensionArr = extensionArr;
	}

	@Override
	public boolean accept(File file) {
		if (file.isDirectory()){
			return true;
		} else if (fileEndsWithExtension(file)){
			return true;
		} else {
			return false;
		}
	}

	private boolean fileEndsWithExtension(File file) {
		for (String extension: extensionArr){
			if (file.getName().toLowerCase().endsWith(extension)){
				return true;
			}
		}
		return false;
	}

	@Override
	public String getDescription() {
		String str = "FileBrowserFilter for: ";
		for (String s: extensionArr){
			str += s + "/";
		}
		return str;
	}

}
