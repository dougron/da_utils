package main.java.da_utils.xml_maker.clef;

public class XMLClefOption {

	
	private XMLClef clef;
	private String[] nameOptions;

	public XMLClefOption(XMLClef clef, String[] nameOptions){
		this.clef = clef;
		this.nameOptions = nameOptions;
	}
	public boolean isNameInOptionList(String name){
		for (String opt: nameOptions){
			if (name.equals(opt)) return true;
		}
		return false;
	}
	public XMLClef clef(){
		return clef;
	}
}
