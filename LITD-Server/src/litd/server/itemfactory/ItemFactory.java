package litd.server.itemfactory;

import java.util.ArrayList;
import java.util.Random;

import litd.shared.ItemStats;

public class ItemFactory {
	public static ItemFactory s;
	
	ArrayList<ItemTemplate> baseTemplates = new ArrayList<ItemTemplate>();
	ArrayList<ItemTemplate> preTemplates = new ArrayList<ItemTemplate>();
	ArrayList<ItemTemplate> postTemplates = new ArrayList<ItemTemplate>();
	
	Random rand = new Random();
	
	public ItemFactory()
	{
		s = this;
		
		baseTemplates.add(new BaseLeatherArmor());
		baseTemplates.add(new BasePlateArmor());
		baseTemplates.add(new BaseRobe());
		baseTemplates.add(new BaseSpear());
		baseTemplates.add(new BaseStaff());
		baseTemplates.add(new BaseSword());
		baseTemplates.add(new BaseWarhammer());
		
		preTemplates.add(new PreAgility());
		preTemplates.add(new PreArmor());
		preTemplates.add(new PreAura());
		preTemplates.add(new PreEndurance());
		preTemplates.add(new PreMagic());
		preTemplates.add(new PreResistance());
		preTemplates.add(new PreStrength());
		
		postTemplates.add(new PostAgility());
		postTemplates.add(new PostArmor());
		postTemplates.add(new PostAura());
		postTemplates.add(new PostEndurance());
		postTemplates.add(new PostMagic());
		postTemplates.add(new PostResistance());
		postTemplates.add(new PostStrength());
	}
	
	public synchronized ItemStats makeItem()
	{
		ItemStats item = new ItemStats();
		
		baseTemplates.get(rand.nextInt(baseTemplates.size())).modify(item);
		
		if(rand.nextFloat() < 0.4f) preTemplates.get(rand.nextInt(preTemplates.size())).modify(item);
		if(rand.nextFloat() < 0.4f) postTemplates.get(rand.nextInt(postTemplates.size())).modify(item);
		
		for(int i=0;i<7;i++)
			item.stats[i] *= 2.f;
		
		return item;
	}
}
