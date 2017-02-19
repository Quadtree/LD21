package litd.server.itemfactory;

import litd.shared.ItemStats;
import litd.shared.Set;

public class PreAura extends ItemTemplate {

	@Override
	public void modify(ItemStats item) {
		item.name = "Hallowed " + item.name;
		item.stats[Set.STAT_AURA] += 10;
	}

}
