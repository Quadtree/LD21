package litd.server.itemfactory;

import litd.shared.ItemStats;
import litd.shared.RenderInfo;
import litd.shared.Set;

public class BaseStaff extends ItemTemplate {

	@Override
	public void modify(ItemStats item) {
		item.name = "Staff";
		item.sprite = RenderInfo.STAFF;
		item.stats[Set.STAT_MAGIC] = 25;
		item.isWeapon = true;
	}

}
