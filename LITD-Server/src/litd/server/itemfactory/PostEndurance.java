package litd.server.itemfactory;

import litd.shared.ItemStats;
import litd.shared.Set;

public class PostEndurance extends ItemTemplate {

	@Override
	public void modify(ItemStats item) {
		item.name = item.name + " of Survival";
		item.stats[Set.STAT_ENDURANCE] += 10;
	}

}
