package LegacyStuff;
import java.util.ArrayList;

import com.cycling74.max.Atom;

/*
 * renamed with _DAPlayAlong tag 14 Sept 2016 to avoid conflict with the COntrollerInfo interface in 
 * DAPlayletTwo which is being used by the SortaSelekta
 */

public interface ControllerInfo_DAPlayAlong {

	public Atom[] initAtomArray();
	public ArrayList<Atom> initAtomList();

}
