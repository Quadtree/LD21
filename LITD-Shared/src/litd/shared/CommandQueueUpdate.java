package litd.shared;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class CommandQueueUpdate implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = -3343858835038364133L;

	public List<Byte> queue = new ArrayList<Byte>();
}
