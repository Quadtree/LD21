package litd.entity;

import java.util.Arrays;

import litd.server.LevelServer;
import litd.server.Log;
import litd.server.itemfactory.ItemFactory;
import litd.shared.RenderInfo;
import litd.shared.Set;

public class Monster extends Mobile {
	boolean magicAttack;
	boolean meleeAttack;
	boolean mobile;
	String name;
	byte sprite;
	float treasureOdds;
	
	public Monster(boolean magicAttack, boolean meleeAttack, boolean mobile,
			String name, int x, int y, float[] stats, byte sprite, byte level, float treasureOdds) {
		super();
		this.magicAttack = magicAttack;
		this.meleeAttack = meleeAttack;
		this.mobile = mobile;
		this.name = name;
		this.x = x;
		this.y = y;
		this.destX = x;
		this.destY = y;
		this.stats = Arrays.copyOf(stats, stats.length);
		this.sprite = sprite;
		this.hp = stats[Set.STAT_ENDURANCE] * Set.END_MUL;
		this.curLevel = level;
		this.treasureOdds = treasureOdds;
	}

	@Override
	public void update() {
		
		int bestDist = Integer.MAX_VALUE;
		Player target = null;
		
		for(Player p : LevelServer.levelServers[curLevel].getPlayersInLOS(x, y))
		{
			int dist = Math.abs(p.getX() - x) + Math.abs(p.getY() - y);
			if(dist < bestDist)
			{
				bestDist = dist;
				target = p;
			}
		}
		
		if(target != null && canAct())
		{
			//Log.log(curLevel + " ==> " + target.curLevel);
			
			if(!magicAttack)
				setDest(target.getX(), target.getY());
			else
				fireMagicRayAt(target);
		}
		
		heal(0.125f);
		
		super.update();
	}
	
	protected void fireMagicRayAt(Mobile mob)
	{
		if(mainActionTimer > 0) return;
		
		float amount = stats[Set.STAT_MAGIC] * 0.35f * (float)Math.random();
		float actualAmount = mob.takeDamage(amount, false);
			
		LevelServer.levelServers[curLevel].postCombatMessage(getName() + " fires a blast of magic at " + mob.getName() + " for " + (int)actualAmount + " damage (" + (int)(amount - actualAmount) + " absorbed)", mob.x, mob.y);
		LevelServer.levelServers[curLevel].postSound("magicblast", mob.x, mob.y);
		
		float cx = getX();
		float cy = getY();
		float dist = (float) Math.sqrt(Math.pow(mob.getX() - getX(), 2) + Math.pow(mob.getY() - getY(), 2));
		float mx = (mob.getX() - getX()) / dist;
		float my = (mob.getY() - getY()) / dist;
		
		while(Math.abs(cx - mob.getX()) > 1.5f || Math.abs(cy - mob.getY()) > 1.5f)
		{
			cx += mx;
			cy += my;
			LevelServer.levelServers[curLevel].addEntity(new VisualEffect((int)cx, (int)cy, RenderInfo.EXPLOSION));
		}
		
		mainActionTimer = Set.ACTION_DELAY;
	}

	@Override
	public boolean canMove() {
		return mobile;
	}

	@Override
	public boolean keep() {
		return hp > 0;
	}

	@Override
	public void destroyed() {
		LevelServer.levelServers[curLevel].postCombatMessage(getName() + " is slain!", x, y);
		if(Math.random() < treasureOdds) LevelServer.levelServers[curLevel].addEntity(new Item(x,y));
		super.destroyed();
	}

	@Override
	protected byte getSprite() {
		return sprite;
	}

	@Override
	public String getName() {
		return "the " + name;
	}
}
