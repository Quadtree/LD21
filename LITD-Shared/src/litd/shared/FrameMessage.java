package litd.shared;

import java.io.Serializable;
import java.util.List;

public class FrameMessage implements Serializable {
	/**
	 * 
	 */
	private static final long serialVersionUID = 6262295319242292141L;
	
	public List<RenderInfo> renderInfos;
	public int x,y;
	public byte level;
	public String message;
	public List<Nametag> nametags;
	
	public float[] stats;
	public float hp;
	
	public FrameMessage(List<RenderInfo> renderInfos, int x, int y, byte level,
			String message, List<Nametag> nametags, float[] stats, float hp) {
		super();
		this.renderInfos = renderInfos;
		this.x = x;
		this.y = y;
		this.level = level;
		this.message = message;
		this.nametags = nametags;
		this.stats = stats;
		this.hp = hp;
	}
}
