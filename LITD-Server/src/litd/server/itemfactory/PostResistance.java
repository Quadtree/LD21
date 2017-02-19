package litd.server.itemfactory;

import litd.shared.ItemStats;
import litd.shared.Set;

public class PostResistance extends ItemTemplate {

	@Override
	public void modify(ItemStats item) {
		item.name = item.name + " of Blocking";
		item.stats[Set.STAT_ARMOR] += 10;
	}

}
