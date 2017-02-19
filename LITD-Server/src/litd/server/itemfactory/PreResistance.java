package litd.server.itemfactory;

import litd.shared.ItemStats;
import litd.shared.Set;

public class PreResistance extends ItemTemplate {

	@Override
	public void modify(ItemStats item) {
		item.name = "Armored " + item.name;
		item.stats[Set.STAT_ARMOR] += 10;
	}

}
