package timed_notes_and_controllers;

public class Note
{

	private int pitch;
	private int velocity;

	
	
	public Note(int aPitch, int aVelocity)
	{
		setPitch(aPitch);
		setVelocity(aVelocity);
	}

	
	
	public int getPitch()
	{
		return pitch;
	}

	
	
	public void setPitch(int aPitch)
	{
		this.pitch = aPitch;
	}

	
	
	public int getVelocity()
	{
		return velocity;
	}

	
	
	public void setVelocity(int velocity)
	{
		this.velocity = velocity;
	}
	
	
	
	public String toString()
	{
		return "pitch=" + pitch + " vel=" + velocity;
	}
}
