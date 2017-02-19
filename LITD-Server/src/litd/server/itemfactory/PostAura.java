package litd.server.itemfactory;

import litd.shared.ItemStats;
import litd.shared.Set;

public class PostAura extends ItemTemplate {

	@Override
	public void modify(ItemStats item) {
		item.name = item.name + " of Healing";
		item.stats[Set.STAT_AURA] += 10;
	}

}
