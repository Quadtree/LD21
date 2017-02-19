package litd.server.itemfactory;

import litd.shared.ItemStats;
import litd.shared.RenderInfo;
import litd.shared.Set;

public class BasePlateArmor extends ItemTemplate {

	@Override
	public void modify(ItemStats item) {
		item.name = "Plate Armor";
		item.sprite = RenderInfo.PLATE_ARMOR;
		item.stats[Set.STAT_ARMOR] = 35;
		item.stats[Set.STAT_MAGIC] = -35;
		item.stats[Set.STAT_AGILITY] = -12;
		item.isWeapon = false;
	}

}
