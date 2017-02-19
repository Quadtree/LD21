package litd.server.itemfactory;

import litd.shared.ItemStats;
import litd.shared.Set;

public class PostArmor extends ItemTemplate {

	@Override
	public void modify(ItemStats item) {
		item.name = item.name + " of Magic Defense";
		item.stats[Set.STAT_RESISTANCE] += 10;
	}

}
