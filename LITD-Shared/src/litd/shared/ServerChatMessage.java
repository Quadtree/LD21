package litd.shared;

import java.io.Serializable;

public class ServerChatMessage implements Serializable{
	/**
	 * 
	 */
	private static final long serialVersionUID = -3554626577082581521L;
	
	public String message;

	public ServerChatMessage(String message) {
		super();
		this.message = message;
	}
}
