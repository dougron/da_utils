package main.java.da_utils.render_name;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

public class RenderName {
	
	private static final DateFormat df = new SimpleDateFormat("yyyyMMdd_HHmmssSSS");

	public static String dateAndTime() {
		Date dateobj = new Date();
		return df.format(dateobj);
	}
	
	
	public static String[] twoDistinctButCloseDateAndTime() {
		Date dateobj = new Date();
		Calendar c = Calendar.getInstance();
		c.setTime(dateobj);
		c.add(Calendar.MILLISECOND, -1);
		Date earlierDateObj = c.getTime();
		return new String[] {df.format(earlierDateObj), df.format(dateobj)};
	}
	
	
	
	public static void main (String[] args)
	{
		String[] str = RenderName.twoDistinctButCloseDateAndTime();
		System.out.println(str[0]);
		System.out.println(str[1]);
	}
}
