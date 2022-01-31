package UndoList;

public class UndoListTestObject implements UndoListInterface {
	
	private static int inst = 0;
	private String name = "testObj-";
	
	public UndoListTestObject(){
		name += inst;
		inst++;		
	}

	@Override
	public String listDescription() {
		return name;
	}

	@Override
	public Object getUndoObject(int index) {
		// TODO Auto-generated method stub
		return null;
	}

}
