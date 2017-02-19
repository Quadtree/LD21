package litd.shared;

import java.io.Serializable;

public class PlayerChatMessage implements Serializable{
	/**
	 * 
	 */
	private static final long serialVersionUID = -3554626577082581521L;
	
	public String message;
	public boolean isGlobal;

	public PlayerChatMessage(String message, boolean isGlobal) {
		super();
		this.message = message;
		this.isGlobal = isGlobal;
	}
}
