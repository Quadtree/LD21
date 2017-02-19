package litd.server.itemfactory;

import litd.shared.ItemStats;
import litd.shared.Set;

public class PostStrength extends ItemTemplate {

	@Override
	public void modify(ItemStats item) {
		item.name = item.name + " of Agility";
		item.stats[Set.STAT_AGILITY] += 10;
	}

}
