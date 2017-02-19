package litd.shared;

import java.io.Serializable;

public class DropCommand implements Serializable{

	/**
	 * 
	 */
	private static final long serialVersionUID = 7759209146578246960L;

	public long itemId;
	
	public DropCommand(long itemId)
	{
		this.itemId = itemId;
	}
}
