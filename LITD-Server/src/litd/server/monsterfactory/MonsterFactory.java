package litd.server.monsterfactory;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Random;

import litd.entity.Monster;
import litd.server.Log;
import litd.shared.RenderInfo;

public class MonsterFactory {
	public static MonsterFactory s;
	
	boolean[] spawnEnabled = new boolean[10];
	
	Random rand = new Random();
	
	class MonsterType
	{
		boolean magicAttack;
		boolean meleeAttack;
		boolean mobile;
		String name;
		byte sprite;
		float treasureOdds;
		float[] averageStats;
		boolean[] canAppearOnLevel;
		
		public Monster create(int x, int y, byte level)
		{
			return new Monster(magicAttack, meleeAttack, mobile, name, x, y, averageStats, sprite, level, treasureOdds);
		}

		public MonsterType(boolean magicAttack, boolean meleeAttack,
				boolean mobile, String name, byte sprite, float treasureOdds,
				boolean[] canAppearOnLevel, float[] averageStats) {
			super();
			this.magicAttack = magicAttack;
			this.meleeAttack = meleeAttack;
			this.mobile = mobile;
			this.name = name;
			this.sprite = sprite;
			this.averageStats = Arrays.copyOf(averageStats, averageStats.length);
			this.canAppearOnLevel = Arrays.copyOf(canAppearOnLevel, canAppearOnLevel.length);
			
			for(int i=0;i<10;i++)
				spawnEnabled[i] = spawnEnabled[i] || canAppearOnLevel[i];
			
			this.treasureOdds = treasureOdds;
		}
	}
	
	ArrayList<MonsterType> types = new ArrayList<MonsterType>();
	
	public MonsterFactory()
	{
		s = this;
		
		types.add(new MonsterType(false, true, true, "Goblin", RenderInfo.GOBLIN, 0.3f,
							  	//GD  	//LB	//RW	//BW	//TL	//EX	//SC1	//SC2	//FX1	//FX2
				new boolean[]{	true, 	true, 	false, 	false, 	false, 	false, 	false, 	false, 	false, 	false},
							//STR	//AGL	//END	//MGC	//ARA	//ARM	//RES
				new float[]{20,		10,		5,		0,		0,		4,		0}));
		
		types.add(new MonsterType(true, false, true, "Goblin Sorcerer", RenderInfo.GOBLIN_SORC, 0.4f,
			  					//GD  	//LB	//RW	//BW	//TL	//EX	//SC1	//SC2	//FX1	//FX2
				new boolean[]{	true, 	true, 	false, 	false, 	false, 	false, 	false, 	false, 	false, 	false},
							//STR	//AGL	//END	//MGC	//ARA	//ARM	//RES
				new float[]{5,		5,		4,		15,		0,		2,		2}));
		
		types.add(new MonsterType(true, false, false, "Tentacle", RenderInfo.TENTACLE, 1.0f,
								//GD  	//LB	//RW	//BW	//TL	//EX	//SC1	//SC2	//FX1	//FX2
				new boolean[]{	false, 	false, 	false, 	false, 	false, 	false, 	true, 	true, 	false, 	false},
							//STR	//AGL	//END	//MGC	//ARA	//ARM	//RES
				new float[]{5,		25,		150,	35,		0,		20,		20}));
		
		types.add(new MonsterType(false, true, true, "Cave Slug", RenderInfo.CAVE_SLUG, 0.4f,
								//GD  	//LB	//RW	//BW	//TL	//EX	//SC1	//SC2	//FX1	//FX2
				new boolean[]{	false, 	true, 	false, 	false, 	false, 	false, 	false, 	false, 	false, 	false},
							//STR	//AGL	//END	//MGC	//ARA	//ARM	//RES
				new float[]{65,		5,		15,		0,		0,		5,		0}));
		
		types.add(new MonsterType(false, true, true, "Demon", RenderInfo.DEMON, 0.75f,
								//GD  	//LB	//RW	//BW	//TL	//EX	//SC1	//SC2	//FX1	//FX2
				new boolean[]{	false, 	false, 	true, 	false, 	true, 	false, 	false, 	false, 	false, 	false},
							//STR	//AGL	//END	//MGC	//ARA	//ARM	//RES
				new float[]{20,		35,		10,		0,		0,		10,		0}));
		
		types.add(new MonsterType(true, false, true, "Lich", RenderInfo.LICH, 0.75f,
								//GD  	//LB	//RW	//BW	//TL	//EX	//SC1	//SC2	//FX1	//FX2
				new boolean[]{	false, 	false, 	false, 	true, 	true, 	false, 	false, 	false, 	false, 	false},
							//STR	//AGL	//END	//MGC	//ARA	//ARM	//RES
				new float[]{5,		15,		10,		20,		0,		6,		12}));
		
		types.add(new MonsterType(false, true, true, "Frost Elemental", RenderInfo.FROST_ELEMENTAL, 0.5f,
								//GD  	//LB	//RW	//BW	//TL	//EX	//SC1	//SC2	//FX1	//FX2
				new boolean[]{	false, 	false, 	false, 	true, 	false, 	false, 	false, 	false, 	false, 	false},
							//STR	//AGL	//END	//MGC	//ARA	//ARM	//RES
				new float[]{40,		10,		10,		0,		0,		2,		18}));
		
		types.add(new MonsterType(true, false, true, "Fire Elemental", RenderInfo.FIRE_ELEMENTAL, 0.5f,
								//GD  	//LB	//RW	//BW	//TL	//EX	//SC1	//SC2	//FX1	//FX2
				new boolean[]{	false, 	false, 	true, 	false, 	false, 	false, 	false, 	false, 	false, 	false},
							//STR	//AGL	//END	//MGC	//ARA	//ARM	//RES
				new float[]{5,		30,		10,		10,		0,		2,		18}));
		
		types.add(new MonsterType(false, true, true, "Death Spirit", RenderInfo.DEATH_SPIRIT, 1.0f,
								//GD  	//LB	//RW	//BW	//TL	//EX	//SC1	//SC2	//FX1	//FX2
				new boolean[]{	false, 	false, 	false, 	false, 	true, 	false, 	false, 	false, 	false, 	false},
							//STR	//AGL	//END	//MGC	//ARA	//ARM	//RES
				new float[]{10,		75,		14,		0,		0,		12,		12}));
	}
	
	public synchronized Monster create(int x, int y, byte level)
	{
		int n = 0;
		MonsterType type = null;
		for(MonsterType mt : types)
		{
			if(mt.canAppearOnLevel[level] && rand.nextInt(++n) == 0) type = mt;
		}
		
		if(type == null) return null;
		
		//Log.log("Factory creating new " + type.name);
		
		return type.create(x, y, level);
	}
	
	public boolean spawnEnabledOnLevel(int level)
	{
		return spawnEnabled[level];
	}
}
