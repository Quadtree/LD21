package litd.server.itemfactory;

import litd.shared.ItemStats;
import litd.shared.Set;

public class PreAgility extends ItemTemplate {

	@Override
	public void modify(ItemStats item) {
		item.name = "Strong " + item.name;
		item.stats[Set.STAT_STRENGTH] += 10;
	}

}
