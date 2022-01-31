package DataObjects.note_buffer;
import acm.program.ConsoleProgram;


public class NoteOnBufferTest extends ConsoleProgram{
	
	public NoteOnBuffer nob  = new NoteOnBuffer();
	
	public void run(){
		setSize(700, 700);
		printBuffer(nob.toStringArray());
		nob.noteIn(100, 100, 1);
		printBuffer(nob.toStringArray());
		nob.noteIn(120, 100, 1);
		printBuffer(nob.toStringArray());
		nob.noteIn(120, 0, 1);
		printBuffer(nob.toStringArray());
		nob.noteIn(90, 0, 1);
		printBuffer(nob.toStringArray());
		nob.noteIn(100, 0, 1);
		printBuffer(nob.toStringArray());
	}
	
	private void printBuffer(String[] arr){
		println("Buffer contains.....");
		for (int i = 0; i < arr.length; i++){
			println(arr[i]);
		}		
		println(".....................");
	}

}
