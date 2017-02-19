package litd.server.itemfactory;

import litd.shared.ItemStats;
import litd.shared.RenderInfo;
import litd.shared.Set;

public class BaseWarhammer extends ItemTemplate {

	@Override
	public void modify(ItemStats item) {
		item.name = "Warhammer";
		item.sprite = RenderInfo.WARHAMMER;
		item.stats[Set.STAT_STRENGTH] = 25;
		item.isWeapon = true;
	}

}
