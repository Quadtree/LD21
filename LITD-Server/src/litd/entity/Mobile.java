package litd.entity;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.SortedSet;
import java.util.TreeSet;

import litd.server.LevelServer;
import litd.server.Log;
import litd.shared.NewDestOrder;
import litd.shared.RenderInfo;
import litd.shared.Set;

public class Mobile extends Entity {
	
	List<AStarNode> path;
	
	protected int destX, destY;
	
	protected float[] stats = new float[7];
	
	protected float hp;
	
	protected int mainActionTimer;
	
	int shieldDuration = -1;
	float shieldStrength;
	int blessDuration = -1;
	float blessStrength;
	
	int stunDuration = -1;
	int sleepDuration = -1;
	
	public void applyShield(float strength)
	{
		if(strength < shieldStrength) return;
		
		shieldDuration = Set.SHIELD_DURATION;
		shieldStrength = strength;
		
		recalcStats();
	}
	
	public void applyBless(float strength)
	{
		if(Math.abs(strength) < Math.abs(blessStrength)) return;
		
		blessDuration = Set.BLESS_DURATION;
		blessStrength = strength;
		
		recalcStats();
	}
	
	public void stun(int duration)
	{
		stunDuration = duration;
		if(stunDuration > 0)
		{
			LevelServer.levelServers[curLevel].postCombatMessage(getName() + " is stunned for " + (stunDuration / 4.0f) + " seconds", x, y);
		}
	}
	
	public void sleep(int duration)
	{
		sleepDuration = duration;
		if(sleepDuration > 0)
		{
			LevelServer.levelServers[curLevel].postCombatMessage(getName() + " falls asleep for " + (sleepDuration / 4.0f) + " seconds", x, y);
		}
	}
	
	protected void recalcStats()
	{
		if(blessDuration > 0)
		{
			for(int i=0;i<7;i++)
			{
				if(i != Set.STAT_AURA)
					stats[i] *= blessStrength / 200 + 1;
			}
		}
		if(shieldDuration > 0)
		{
			stats[Set.STAT_ARMOR] += shieldStrength;
			stats[Set.STAT_RESISTANCE] += shieldStrength;
		}
	}
	
	class AStarNode implements Comparable<AStarNode>
	{
		public int x,y; // position of this node
		public int g; // path cost to get here
		public int dist; // heuristic distance to the goal
		public AStarNode parent;
		
		public AStarNode(int x, int y, AStarNode parent, int goalX, int goalY)
		{
			this.x = x;
			this.y = y;
			if(parent != null)
			{
				this.g = parent.g + 1;
				this.parent = parent;
			}
			
			dist = Math.abs(x - goalX) + Math.abs(y - goalY);
		}

		@Override
		public int compareTo(AStarNode arg0) {
			return dist - arg0.dist;
		}

		@Override
		public boolean equals(Object obj) {
			if(!(obj instanceof AStarNode)) return false;
			return x == ((AStarNode)obj).x && y == ((AStarNode)obj).y;
		}
	}

	@Override
	public void update() {
		if(canAct() && mainActionTimer > 0) mainActionTimer--;
		
		if((destX != x || destY != y) && canAct())
		{
			if(LevelServer.levelServers[curLevel].getGeom().isWall(destX, destY))
			{
				destX = x;
				destY = y;
			} else {
				if(Math.abs(destX - x) <= 1 && Math.abs(destY - y) <= 1)
				{
					tryMove(destX, destY);
				} else {
					if(path == null)
					{
						List<AStarNode> closed = new ArrayList<AStarNode>();
						List<AStarNode> open = new ArrayList<AStarNode>();
						
						open.add(new AStarNode(x,y,null,destX,destY));
						
						while(open.size() > 0)
						{
							AStarNode curNode = null;
							for(AStarNode node : open)
							{
								if(curNode == null || node.dist < curNode.dist) curNode = node;
							}
							
							open.remove(curNode);
							closed.add(curNode);
							
							//System.out.println("Closed " + curNode.x + " " + curNode.y);
							
							if(curNode.x == destX && curNode.y == destY)
							{
								path = new ArrayList<AStarNode>();
								
								while(curNode != null)
								{
									path.add(0, curNode);
									curNode = curNode.parent;
								}
								
								//System.out.println(path);
								break;
							}
							
							for(int cx=curNode.x - 1;cx <= curNode.x + 1;cx++)
							{
								for(int cy=curNode.y - 1;cy <= curNode.y + 1;cy++)
								{
									if(cx == x && cy == y) continue;
									if(LevelServer.levelServers[curLevel].getGeom().isWall(cx, cy)) continue;
									AStarNode pnn = new AStarNode(cx,cy,curNode,destX,destY);
									
									boolean closedContains = false;
									for(AStarNode node : closed)
										if(node.x == cx && node.y == cy){ closedContains = true; break; }
									if(closedContains) continue;
									
									boolean openContains = false;
									for(AStarNode node : open)
										if(node.x == cx && node.y == cy){ openContains = true; break; }
									
									if(openContains)
									{
										for(Iterator<AStarNode> itr = open.iterator();itr.hasNext();)
										{
											AStarNode asn = itr.next();
											if(asn.x == pnn.x && asn.y == pnn.y && asn.g > pnn.g)
											{
												itr.remove();
												open.add(pnn);
												break;
											}
										}
									} else {
										open.add(pnn);
									}
								}
							}
						}
					}
					//System.out.println("Pathfind complete!");
					if(path != null && path.size() > 1)
					{
						if(tryMove(path.get(1).x, path.get(1).y)) path.remove(0);
					}
				}
			}
		}
		
		shieldDuration--;
		blessDuration--;
		stunDuration--;
		sleepDuration--;
		
		if(shieldDuration == 0)
		{
			recalcStats();
			LevelServer.levelServers[curLevel].postCombatMessage(getName() + "'s shield expires", x, y);
		}
		if(blessDuration == 0)
		{
			recalcStats();
			if(blessStrength > 0)
				LevelServer.levelServers[curLevel].postCombatMessage(getName() + "'s blessing expires", x, y);
			else
				LevelServer.levelServers[curLevel].postCombatMessage("The curse on " + getName() + " expires", x, y);
		}
		if(stunDuration == 0)
		{
			LevelServer.levelServers[curLevel].postCombatMessage(getName() + " is no longer stunned.", x, y);
		}
		if(sleepDuration == 0)
		{
			LevelServer.levelServers[curLevel].postCombatMessage(getName() + " wakes up.", x, y);
		}
		
		super.update();
	}
	
	public boolean canAct()
	{
		return stunDuration <= 0 && sleepDuration <= 0;
	}
	
	public void setDest(int x, int y)
	{
		if(x == destX && y == destY) return;
		destX = x;
		destY = y;
		path = null;
	}
	
	protected void meleeAttack(Mobile target)
	{
		if(mainActionTimer > 0) return;
		
		float attackRoll = stats[Set.STAT_AGILITY] * (float)Math.random();
		float defenseRoll = target.stats[Set.STAT_AGILITY] * (float)Math.random();
		
		if(attackRoll > defenseRoll)
		{
			float amount = stats[Set.STAT_STRENGTH] * (float)Math.random();
			float actualAmount = target.takeDamage(amount, false);
			
			LevelServer.levelServers[curLevel].postCombatMessage(getName() + " strikes " + target.getName() + " for " + (int)actualAmount + " damage (" + (int)(amount - actualAmount) + " absorbed)", target.x, target.y);
		
			if(this instanceof Player)
			{
				LevelServer.levelServers[curLevel].postSound("playermelee" + (int)(Math.random() * 3), target.x, target.y);
			} else {
				LevelServer.levelServers[curLevel].postSound("monstermelee", target.x, target.y);
			}
		} else {
			LevelServer.levelServers[curLevel].postCombatMessage(getName() + " misses " + target.getName(), target.x, target.y);
		}
		
		mainActionTimer = Set.ACTION_DELAY;
	}
	
	public float takeDamage(float amount, boolean isMagic)
	{
		float armorAmount = 0;
		if(isMagic)
			armorAmount = stats[Set.STAT_RESISTANCE];
		else
			armorAmount = stats[Set.STAT_ARMOR];
		
		if(amount > armorAmount)
		{
			amount -= armorAmount * 0.8f;
		} else {
			amount *= 0.8f;
		}
		
		hp -= amount;
		if(sleepDuration > 0) sleepDuration = 1;
		return amount;
	}
	
	public void heal(float amount)
	{
		hp = Math.min(hp + amount, stats[Set.STAT_ENDURANCE] * Set.END_MUL);
	}
	
	public boolean canMove()
	{
		return true;
	}
	
	public boolean tryMove(int x, int y)
	{
		if(!LevelServer.levelServers[curLevel].getGeom().isWall(x, y))
		{
			List<Entity> sq = LevelServer.levelServers[curLevel].findEntitesOnSquare(x, y);
			
			for(Entity e : sq)
			{
				if(e instanceof Transition && this instanceof Player)
				{
					Transition trans = (Transition)e;
					LevelServer.levelServers[curLevel].removeEntity(this);
					
					Transition targetTrans = LevelServer.levelServers[trans.dest].findTransitionTo(curLevel);
					this.x = targetTrans.getX();
					this.y = targetTrans.getY();
					this.destX = targetTrans.getX();
					this.destY = targetTrans.getY();
					
					Log.log("BACK: " + targetTrans.getX() + " " + targetTrans.getY());
					
					curLevel = trans.dest;
					LevelServer.levelServers[curLevel].addEntity(this);
					return true;
				}
				if(this instanceof Monster && e instanceof Player || this instanceof Player && e instanceof Monster)
				{
					meleeAttack((Mobile)e);
					return true;
				}
			}
			
			if(canMove())
			{
				this.x = x;
				this.y = y;
			}
			
			return true;
		}
		
		return false;
	}
	
	public float getStat(int stat)
	{
		return stats[stat];
	}

	@Override
	public List<Byte> getExtraSprites() {
		List<Byte> extras = new ArrayList<Byte>();
		
		if(sleepDuration > 0) extras.add(RenderInfo.EFF_SLEEP);
		if(stunDuration > 0) extras.add(RenderInfo.EFF_STUN);
		if(shieldDuration > 0) extras.add(RenderInfo.EFF_SHIELD);
		if(blessDuration > 0 && blessStrength > 0) extras.add(RenderInfo.EFF_BLESS);
		if(blessDuration > 0 && blessStrength < 0) extras.add(RenderInfo.EFF_CURSE);
		if(hp < stats[Set.STAT_ENDURANCE] * Set.END_MUL / 2) extras.add(RenderInfo.EFF_INJURED);
		if(hp < 0) extras.add(RenderInfo.EFF_DYING);
		
		return extras;
	}

	public float getHP() {
		return hp;
	}
}
