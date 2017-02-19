package litd.server.itemfactory;

import litd.shared.ItemStats;
import litd.shared.Set;

public class PreEndurance extends ItemTemplate {

	@Override
	public void modify(ItemStats item) {
		item.name = "Durable " + item.name;
		item.stats[Set.STAT_ENDURANCE] += 10;
	}

}
