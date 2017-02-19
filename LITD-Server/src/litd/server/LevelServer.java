package litd.server;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.Queue;
import java.util.Random;
import java.util.concurrent.ArrayBlockingQueue;

import litd.entity.Entity;
import litd.entity.Item;
import litd.entity.Monster;
import litd.entity.Player;
import litd.entity.Transition;
import litd.entity.VisualEffect;
import litd.server.monsterfactory.MonsterFactory;
import litd.shared.LevelGeometry;
import litd.shared.Nametag;
import litd.shared.RenderInfo;
import litd.shared.ServerChatMessage;
import litd.shared.ServerCombatMessage;
import litd.shared.SoundCommand;

public class LevelServer implements Runnable {
	public static LevelServer[] levelServers = new LevelServer[10];
	int level;
	
	int players = 0;
	int monsters = 0;
	
	Random rand = new Random();
	
	LevelGeometry geom;
	
	public LevelServer(int level) throws FileNotFoundException, IOException, ClassNotFoundException
	{
		this.level = level;
		
		ObjectInputStream ois = new ObjectInputStream(new FileInputStream("./media/level" + level + ".lvl"));
		geom = (LevelGeometry) ois.readObject();
		ois.close();
		
		for(LevelGeometry.TransitionInfo ti : geom.transitions)
		{
			entites.add(new Transition(ti.dest, ti.x, ti.y));
		}
		
		for(int i=0;i<10;i++)
			spawnMonster();
		
		// TEST CODE
		/*for(int i=0;i<15;i++)
			entites.add(new Item(55+i,55));
		
		Monster m = MonsterFactory.s.create(30, 60, (byte)level);
		
		if(m != null)
			addEntity(m);*/
	}
	
	ArrayList<Entity> entites = new ArrayList<Entity>();
	
	Queue<Entity> entityAddQueue = new ArrayBlockingQueue<Entity>(128);
	Queue<Entity> entityRemoveQueue = new ArrayBlockingQueue<Entity>(128);
	
	Queue<String> chatQueue = new ArrayBlockingQueue<String>(256);
	
	public static void addChatMessageToAll(String message)
	{
		for(LevelServer ls : levelServers)
			ls.addChatMessage(message);
	}
	
	public void addChatMessage(String message)
	{
		chatQueue.add(message);
	}
	
	public void addEntity(Entity pl)
	{
		//Log.log("LS " + level + ": Adding entity " + pl);
		if(pl instanceof Player) players++;
		if(pl instanceof Monster) monsters++;
		entityAddQueue.add(pl);
	}
	
	public void removeEntity(Entity ent)
	{
		//Log.log("LS " + level + ": Removing entity " + ent);
		if(ent instanceof Player) players--;
		if(ent instanceof Monster) monsters--;
		entityRemoveQueue.add(ent);
	}

	@Override
	public void run() {
		Log.log("LS " + level + ": starting up");
		
		while(true)
		{
			updateAll();
			
			try {
				Thread.sleep(250);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}
	}
	
	public synchronized void updateAll()
	{
		while(entityRemoveQueue.size() > 0)
		{
			entites.remove(entityRemoveQueue.poll());
		}
		
		while(entityAddQueue.size() > 0)
		{
			entites.add(entityAddQueue.poll());
		}
		
		while(chatQueue.size() > 0)
		{
			String message = chatQueue.poll();
			
			for(Entity ent : entites)
			{
				if(ent instanceof Player)
				{
					((Player)ent).sendTo(new ServerChatMessage(message));
				}
			}
		}
		
		if(MonsterFactory.s.spawnEnabledOnLevel(level) && monsters < (players)*10 && rand.nextInt(40) == 0)
		{
			spawnMonster();
		}
		
		if(players == 0)
		{
			//Log.log(level + " has no players. Nothing to do...");
			return;
		}
		
		for(int i=0;i<entites.size();++i)
		{
			if(entites.get(i).keep())
				entites.get(i).update();
			else
			{
				if(entites.get(i) instanceof Player) players--;
				if(entites.get(i) instanceof Monster) monsters--;
				entites.get(i).destroyed();
				entites.remove(i--);
			}
		}
		
		int carvings = 0;
		
		for(int i=entites.size() - 1;i >= 0;--i)
		{
			Entity ent = entites.get(i);
			if(ent instanceof litd.entity.Writing)
			{
				if(carvings < Main.maxCarvings)
					carvings++;
				else
					entites.remove(i--);
			}
		}
	}

	private void spawnMonster() {
		int sx=-1,sy=-1;
		boolean playerInRange = false;
		int attempt = 0;
		while(geom.isWall(sx, sy) || playerInRange)
		{
			sx = rand.nextInt(128);
			sy = rand.nextInt(128);
			
			playerInRange = false;
			
			for(Entity ent : entites)
			{
				if(ent instanceof Player)
				{
					if(Math.abs(ent.getX() - sx) < 16 || Math.abs(ent.getY() - sy) < 16) playerInRange = true;
				}
			}
			if(attempt++ > 5000) return;
		}
		
		Monster mon = MonsterFactory.s.create(sx, sy, (byte)level);
		
		if(mon != null)
		{
			Log.log("Spawning " + mon.getName() + " at " + sx + ", " + sy + " on level " + level);
			addEntity(mon);
		}
	}
	
	public LevelGeometry getGeom() {
		return geom;
	}
	
	public synchronized Transition findTransitionTo(byte level)
	{
		for(Entity e : entites)
		{
			if(e instanceof Transition)
			{
				Transition trans = (Transition)e;
				if(trans.getDest() == level) return trans;
			}
		}
		
		return null;
	}
	
	public synchronized List<Entity> findEntitesOnSquare(int x, int y)
	{
		ArrayList<Entity> ret = new ArrayList<Entity>();
		
		for(Entity e : entites)
			if(e.getX() == x && e.getY() == y) ret.add(e);
		
		return ret;
	}
	
	public boolean isInLOS(int sx, int sy, int ex, int ey)
	{
		float cx = sx;
		float cy = sy;
		float dist = (float) Math.sqrt(Math.pow(ex - sx, 2) + Math.pow(ey - sy, 2));
		if(dist > 17) return false;
		
		float mx = (ex - sx) / dist;
		float my = (ey - sy) / dist;
		
		while(Math.abs(cx - ex) > 1.5f || Math.abs(cy - ey) > 1.5f)
		{
			if(geom.isWall((int)cx, (int)cy)) return false;
			cx += mx;
			cy += my;
		}
		
		return true;
	}
	
	public synchronized Monster getNearestMonsterTo(int x, int y)
	{
		int bestDist = 6;
		Monster mon = null;
		
		for(Entity ent : entites)
		{
			if(ent instanceof Monster)
			{
				int dist = Math.abs(ent.getX() - x) + Math.abs(ent.getY() - y);
				if(dist < bestDist)
				{
					bestDist = dist;
					mon = (Monster) ent;
				}
			}
		}
		
		return mon;
	}
	
	public synchronized List<Monster> explosion(int x, int y, int boxSize, int maxRange, byte sprite)
	{
		List<Monster> ret = new ArrayList<Monster>();
		
		for(Entity ent : entites)
		{
			if(ent instanceof Monster)
			{
				int dist = Math.abs(ent.getX() - x) + Math.abs(ent.getY() - y);
				if(dist <= maxRange && Math.abs(ent.getX() - x) < boxSize && Math.abs(ent.getY() - y) < boxSize)
				{
					ret.add((Monster) ent);
				}
			}
		}
		
		for(int cx = x - boxSize;cx <= x + boxSize;cx++)
		{
			for(int cy = y - boxSize;cy <= y + boxSize;cy++)
			{
				int dist = Math.abs(cx - x) + Math.abs(cy - y);
				if(dist <= maxRange)
				{
					addEntity(new VisualEffect(cx,cy,sprite));
				}
			}
		}
		
		return ret;
	}
	
	public synchronized Player getNearestPlayerTo(int x, int y)
	{
		int bestDist = 6;
		Player mon = null;
		
		for(Entity ent : entites)
		{
			if(ent instanceof Player)
			{
				int dist = Math.abs(ent.getX() - x) + Math.abs(ent.getY() - y);
				if(dist < bestDist)
				{
					bestDist = dist;
					mon = (Player) ent;
				}
			}
		}
		
		return mon;
	}
	
	public synchronized void postCombatMessage(String message, int x, int y)
	{
		message = message.substring(0, 1).toUpperCase() + message.substring(1);
		for(Player pl : getPlayersInLOS(x,y))
		{
			pl.sendTo(new ServerCombatMessage(message));
		}
	}
	
	public synchronized void postSound(String name, int x, int y)
	{
		for(Player pl : getPlayersInLOS(x,y))
		{
			pl.sendTo(new SoundCommand(name));
		}
	}
	
	public synchronized List<Player> getPlayersInLOS(int x, int y)
	{
		ArrayList<Player> ret = new ArrayList<Player>();
		
		for(Entity e : entites)
			if(e instanceof Player && ((Player)e).canAct() && isInLOS(x,y,e.getX(),e.getY())) ret.add((Player)e);
		
		return ret;
	}

	public synchronized List<RenderInfo> findRenderInfosOnScreen(int camX, int camY)
	{
		ArrayList<RenderInfo> ret = new ArrayList<RenderInfo>();
		
		for(Entity e : entites)
			if(e.isOnScreen(camX, camY))
			{
				RenderInfo ri = e.getRenderInfo(camX, camY);
				ret.add(ri);
				
				List<Byte> ex = e.getExtraSprites();
				
				if(ex != null)
				{
					for(Byte b : ex)
						ret.add(new RenderInfo(ri.x, ri.y, b));
				}
				
			}
		
		//System.out.println(ret.size());
		
		return ret;
	}
	
	public synchronized List<Nametag> findNameTagsOnScreen(int camX, int camY)
	{
		ArrayList<Nametag> ret = new ArrayList<Nametag>();
		
		for(Entity e : entites)
			if(e.isOnScreen(camX, camY))
			{
				Nametag ri = e.getNametag(camX, camY);
				if(ri != null) ret.add(ri);
			}
		
		return ret;
	}
}
