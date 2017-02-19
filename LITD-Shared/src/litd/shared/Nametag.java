package litd.shared;

import java.io.Serializable;

public class Nametag implements Serializable{
	/**
	 * 
	 */
	private static final long serialVersionUID = 4764014050190424972L;
	
	public byte x,y;
	public String tag;
	
	public Nametag(byte x, byte y, String tag) {
		super();
		this.x = x;
		this.y = y;
		this.tag = tag;
	}
}
