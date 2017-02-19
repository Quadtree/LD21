package litd.server.itemfactory;

import litd.shared.ItemStats;
import litd.shared.Set;

public class PreMagic extends ItemTemplate {

	@Override
	public void modify(ItemStats item) {
		item.name = "Mystical " + item.name;
		item.stats[Set.STAT_MAGIC] += 10;
	}

}
