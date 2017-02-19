package litd.server.itemfactory;

import litd.shared.ItemStats;
import litd.shared.Set;

public class PostMagic extends ItemTemplate {

	@Override
	public void modify(ItemStats item) {
		item.name = item.name + " of Invocation";
		item.stats[Set.STAT_MAGIC] += 10;
	}

}
