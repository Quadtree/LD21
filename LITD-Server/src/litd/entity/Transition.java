package litd.entity;

import litd.shared.RenderInfo;

public class Transition extends Entity {

	byte dest;
	
	public Transition(byte dest, int x, int y)
	{
		this.dest = dest;
		this.x = x;
		this.y = y;
	}
	
	@Override
	protected byte getSprite() {
		return (byte) (RenderInfo.TRANS_0 + dest);
	}

	public byte getDest() {
		return dest;
	}
	
}
