package litd.shared;

import java.io.Serializable;

public class ItemStats implements Serializable{
	/**
	 * 
	 */
	private static final long serialVersionUID = 3250959718493575164L;

	public byte sprite;
	
	public float[] stats;
	
	public String name;
	
	public boolean isWeapon;
	
	public long id;
	
	public boolean isEquipped;
	
	public ItemStats()
	{
		sprite = 0;
		stats = new float[7];
		id = (long)(Math.random() * Long.MAX_VALUE);
	}

	@Override
	public String toString() {
		return "ITEM: " + name;
	}
}
