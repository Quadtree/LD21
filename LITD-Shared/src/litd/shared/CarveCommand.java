package litd.shared;

import java.io.Serializable;

public class CarveCommand implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1930637611981942918L;

	public String message;

	public CarveCommand(String message) {
		super();
		this.message = message;
	}
}
