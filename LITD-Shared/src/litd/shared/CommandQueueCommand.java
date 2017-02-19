package litd.shared;

import java.io.Serializable;

public class CommandQueueCommand implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = -7524115894299645447L;

	public byte command;
	public int tileX, tileY;

	public CommandQueueCommand(byte command, int tileX, int tileY) {
		super();
		this.command = command;
		this.tileX = tileX;
		this.tileY = tileY;
	}
}
