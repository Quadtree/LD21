package litd.shared;

import java.io.Serializable;

public class NewDestOrder implements Serializable{
	/**
	 * 
	 */
	private static final long serialVersionUID = -2207461509077897126L;
	
	public int x,y;

	public NewDestOrder(int x, int y) {
		super();
		this.x = x;
		this.y = y;
	}
}
