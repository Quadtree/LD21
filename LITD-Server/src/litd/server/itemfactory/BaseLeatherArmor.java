package litd.server.itemfactory;

import litd.shared.ItemStats;
import litd.shared.RenderInfo;
import litd.shared.Set;

public class BaseLeatherArmor extends ItemTemplate {

	@Override
	public void modify(ItemStats item) {
		item.name = "Leather Armor";
		item.sprite = RenderInfo.LEATHER_ARMOR;
		item.stats[Set.STAT_ARMOR] = 18;
		item.stats[Set.STAT_MAGIC] = -14;
		item.isWeapon = false;
	}

}
