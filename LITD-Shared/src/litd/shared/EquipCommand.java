package litd.shared;

import java.io.Serializable;

public class EquipCommand implements Serializable{

	/**
	 * 
	 */
	private static final long serialVersionUID = -1978272057025129399L;
	
	public long itemId;
	
	public EquipCommand(long itemId)
	{
		this.itemId = itemId;
	}
}
