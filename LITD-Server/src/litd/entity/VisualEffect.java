package litd.entity;

public class VisualEffect extends Entity {
	byte sprite;
	
	public VisualEffect(int x, int y, byte sprite)
	{
		this.x = x;
		this.y = y;
		this.sprite = sprite;
	}

	@Override
	protected byte getSprite() {
		return sprite;
	}

	@Override
	public boolean keep() {
		return Math.random() > 0.2;
	}
}
