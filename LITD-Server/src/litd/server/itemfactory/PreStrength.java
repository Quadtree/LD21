package litd.server.itemfactory;

import litd.shared.ItemStats;
import litd.shared.Set;

public class PreStrength extends ItemTemplate {

	@Override
	public void modify(ItemStats item) {
		item.name = "Maneuverable " + item.name;
		item.stats[Set.STAT_AGILITY] += 10;
	}

}
