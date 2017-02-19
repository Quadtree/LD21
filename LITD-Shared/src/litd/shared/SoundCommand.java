package litd.shared;

import java.io.Serializable;

public class SoundCommand implements Serializable{

	/**
	 * 
	 */
	private static final long serialVersionUID = 4714202268901512024L;

	public String name;

	public SoundCommand(String name) {
		super();
		this.name = name;
	}
}
