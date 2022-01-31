package main.java.da_utils.corpus_capture;

public class SaveData {

	public String path;
	public String trackName;
	public String extension;	// include period
	
	public SaveData(String trackName, String extension, String path) {
		this.trackName = trackName;
		this.extension = extension;
		this.path = path;
	}
}
