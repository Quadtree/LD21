package litd.server.itemfactory;

import litd.shared.ItemStats;
import litd.shared.Set;

public class PreArmor extends ItemTemplate {

	@Override
	public void modify(ItemStats item) {
		item.name = "Antimagic " + item.name;
		item.stats[Set.STAT_RESISTANCE] += 10;
	}

}
