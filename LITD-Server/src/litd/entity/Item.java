package litd.entity;

import litd.server.itemfactory.ItemFactory;
import litd.shared.ItemStats;

public class Item extends Entity {
	ItemStats stats;
	int lifespan;
	
	public Item(int x, int y)
	{
		stats = ItemFactory.s.makeItem();
		this.x = x;
		this.y = y;
		resetLifespan();
	}
	
	public void resetLifespan()
	{
		lifespan = 240;
	}
	
	public String getItemName()
	{
		return stats.name;
	}
	
	public ItemStats getItemStats()
	{
		return stats;
	}

	@Override
	protected byte getSprite() {
		return stats.sprite;
	}

	@Override
	public void update() {
		lifespan--;
		super.update();
	}

	@Override
	public boolean keep() {
		return lifespan > 0;
	}
}
