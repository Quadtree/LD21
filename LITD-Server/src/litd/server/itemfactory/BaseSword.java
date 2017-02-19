package litd.server.itemfactory;

import litd.shared.ItemStats;
import litd.shared.RenderInfo;
import litd.shared.Set;

public class BaseSword extends ItemTemplate {

	@Override
	public void modify(ItemStats item) {
		item.name = "Sword";
		item.sprite = RenderInfo.SWORD;
		item.stats[Set.STAT_STRENGTH] = 15;
		item.stats[Set.STAT_AGILITY] = 10;
		item.isWeapon = true;
	}

}
