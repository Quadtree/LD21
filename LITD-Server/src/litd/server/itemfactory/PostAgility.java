package litd.server.itemfactory;

import litd.shared.ItemStats;
import litd.shared.Set;

public class PostAgility extends ItemTemplate {

	@Override
	public void modify(ItemStats item) {
		item.name = item.name + " of Strength";
		item.stats[Set.STAT_STRENGTH] += 10;
	}

}
