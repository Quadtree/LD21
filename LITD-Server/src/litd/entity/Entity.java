package litd.entity;

import java.util.List;

import litd.shared.Nametag;
import litd.shared.RenderInfo;
import litd.shared.Set;

public class Entity {
	protected int x,y;
	protected byte curLevel;
	
	public void update(){}
	public boolean keep(){ return true; }
	public void destroyed(){}
	public RenderInfo getRenderInfo(int camX, int camY)
	{
		if(getSprite() != 0)
			return new RenderInfo((byte)(x - camX + (Set.SCREEN_TILE_WIDTH / 2)), (byte)(y - camY + (Set.SCREEN_TILE_HEIGHT / 2)), getSprite());
		else
			return null;
	}
	
	public Nametag getNametag(int camX, int camY)
	{
		return null;
	}
	
	public boolean isOnScreen(int camX, int camY)
	{
		return Math.abs(camX - x) < (Set.SCREEN_TILE_WIDTH / 2) && Math.abs(camY - y) < (Set.SCREEN_TILE_HEIGHT / 2);
	}
	
	protected byte getSprite(){ return 0; }
	
	public int getX() {
		return x;
	}
	public int getY() {
		return y;
	}
	public void setX(int x) {
		this.x = x;
	}
	public void setY(int y) {
		this.y = y;
	}
	
	public byte getCurLevel() {
		return curLevel;
	}
	public String getName()
	{
		return "";
	}
	public List<Byte> getExtraSprites()
	{
		return null;
	}
}
