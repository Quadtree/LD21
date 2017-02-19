package litd.shared;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class InventoryUpdate implements Serializable{
	/**
	 * 
	 */
	private static final long serialVersionUID = 6757801680549394088L;
	
	public List<ItemStats> inventory = new ArrayList<ItemStats>();
}
