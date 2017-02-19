package litd.server.itemfactory;

import litd.shared.ItemStats;
import litd.shared.RenderInfo;
import litd.shared.Set;

public class BaseSpear extends ItemTemplate {

	@Override
	public void modify(ItemStats item) {
		item.name = "Spear";
		item.sprite = RenderInfo.SPEAR;
		item.stats[Set.STAT_STRENGTH] = 20;
		item.stats[Set.STAT_ARMOR] = 5;
		item.isWeapon = true;
	}

}
