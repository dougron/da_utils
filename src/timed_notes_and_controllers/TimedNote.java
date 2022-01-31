package timed_notes_and_controllers;

public class TimedNote
{

	private Note note;
	private double position;
	private double length;

	
	// CONSTRUCTORS -----------------------------
	
	public TimedNote(Note aNote, double aPosition, double aLength)
	{
		setNote(aNote);
		setPosition(aPosition);
		setLength(aLength);
	}

	
	
	public TimedNote(int aPitch, int aVelocity, double aPosition, double aLength)
	{
		setNote(new Note(aPitch, aVelocity));
		setPosition(aPosition);
		setLength(aLength);
	}

	
	// INTERFACE ---------------------------------

	public Note getNote()
	{
		return note;
	}

	
	
	public void setNote(Note note)
	{
		this.note = note;
	}



	public double getPosition()
	{
		return position;
	}



	public void setPosition(double position)
	{
		this.position = position;
	}



	public double getLength()
	{
		return length;
	}



	public void setLength(double length)
	{
		this.length = length;
	}
	
	
	
	public String toString()
	{
		return note.toString() + " pos=" + position + " len=" + length;
	}
	
	
	// for junit testing. If you change, check any tests you may break.
	public String toShortString()
	{
		return note.getPitch() + "_" + position + "_" + length + "_" + note.getVelocity();
	}
	
}
