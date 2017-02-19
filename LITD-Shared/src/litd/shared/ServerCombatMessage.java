package litd.shared;

import java.io.Serializable;

public class ServerCombatMessage implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = -7010401235654594577L;

	public String message;

	public ServerCombatMessage(String message) {
		super();
		this.message = message;
	}
}
