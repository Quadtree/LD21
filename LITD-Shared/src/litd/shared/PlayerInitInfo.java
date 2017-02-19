package litd.shared;

import java.io.Serializable;

public class PlayerInitInfo implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 3074623824479621906L;

	public String name;
	public float[] stats;

	public PlayerInitInfo(String name, float[] stats) {
		super();
		this.name = name;
		this.stats = stats;
	}
	
	public void normalizeStats()
	{
		final float TARGET_TOTAL = 220;
		float total = 0;
		
		for(int i=0;i<stats.length;i++)
			total += stats[i];
		
		float ratio = TARGET_TOTAL / total;
		
		for(int i=0;i<stats.length;i++)
			stats[i] *= ratio;
	}
}
