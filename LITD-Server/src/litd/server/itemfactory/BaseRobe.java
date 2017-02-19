package litd.server.itemfactory;

import litd.shared.ItemStats;
import litd.shared.RenderInfo;
import litd.shared.Set;

public class BaseRobe extends ItemTemplate {

	@Override
	public void modify(ItemStats item) {
		item.name = "Robe";
		item.sprite = RenderInfo.ROBE;
		item.stats[Set.STAT_ARMOR] = 3;
		item.isWeapon = false;
	}

}
