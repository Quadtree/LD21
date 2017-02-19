package litd.entity;

import java.io.IOException;
import java.net.Socket;
import java.util.ArrayList;
import java.util.List;

import litd.server.LevelServer;
import litd.server.Log;
import litd.server.abilitycommand.AbilityCommand;
import litd.server.abilitycommand.BlessCommand;
import litd.server.abilitycommand.ChargeCommand;
import litd.server.abilitycommand.CleaveCommand;
import litd.server.abilitycommand.CurseCommand;
import litd.server.abilitycommand.FireballCommand;
import litd.server.abilitycommand.HealCommand;
import litd.server.abilitycommand.ShieldCommand;
import litd.server.abilitycommand.SleepCommand;
import litd.server.abilitycommand.StrikeCommand;
import litd.shared.CarveCommand;
import litd.shared.CommandQueueCommand;
import litd.shared.CommandQueueUpdate;
import litd.shared.DropCommand;
import litd.shared.EquipCommand;
import litd.shared.FrameMessage;
import litd.shared.InventoryUpdate;
import litd.shared.KilledMessage;
import litd.shared.Nametag;
import litd.shared.NewDestOrder;
import litd.shared.PickupCommand;
import litd.shared.PlayerChatMessage;
import litd.shared.PlayerInitInfo;
import litd.shared.RenderInfo;
import litd.shared.Set;
import litd.shared.VictoryMessage;

import appnet.ObjectMessageInput;
import appnet.ObjectMessageOutput;

public class Player extends Mobile {
	public Player(Socket sock) throws IOException {
		super();
		this.sock = sock;
		this.in = new ObjectMessageInput(sock.getInputStream());
		this.out = new ObjectMessageOutput(sock.getOutputStream());
		
		x = 60;
		y = 60;

		this.destX = x;
		this.destY = y;
	}
	
	final protected Socket sock;
	final protected ObjectMessageInput in;
	final protected ObjectMessageOutput out;
	
	byte sprite;
	
	boolean inited = false;
	boolean linkDead = false;
	
	ArrayList<Item> inventory = new ArrayList<Item>();
	
	ArrayList<AbilityCommand> commandQueue = new ArrayList<AbilityCommand>();
	
	String name;
	
	float[] baseStats = new float[7];
	
	protected String getCompleteName()
	{
		return name + "/" + sock.getRemoteSocketAddress();
	}

	@Override
	public void update() {
		try {
			if(!inited)	// this means we have NOT actually appeared yet
			{
				if(in.available())
				{
					Object o = in.read();
					if(o instanceof PlayerInitInfo)
					{
						PlayerInitInfo pii = (PlayerInitInfo)o;
						
						name = ((pii.name.length() > 0) ? pii.name : "Unknown");
						
						if(name.length() > 32)
							name = name.substring(0, 32);
						
						pii.normalizeStats();
						inited = true;
						Log.log(name + " has finished sending login info.");
						
						for(int i=0;i<5;i++)
						{
							Log.log(Set.statNames[i] + " = " + pii.stats[i]);
							baseStats[i] = pii.stats[i];
						}
						
						if(baseStats[Set.STAT_STRENGTH] > baseStats[Set.STAT_AGILITY] && baseStats[Set.STAT_STRENGTH] > baseStats[Set.STAT_ENDURANCE] &&
							baseStats[Set.STAT_STRENGTH] > baseStats[Set.STAT_MAGIC] && baseStats[Set.STAT_STRENGTH] > baseStats[Set.STAT_AURA]) sprite = RenderInfo.PC_STRENGTH;
						
						if(baseStats[Set.STAT_AGILITY] > baseStats[Set.STAT_STRENGTH] && baseStats[Set.STAT_AGILITY] > baseStats[Set.STAT_ENDURANCE] &&
							baseStats[Set.STAT_AGILITY] > baseStats[Set.STAT_MAGIC] && baseStats[Set.STAT_AGILITY] > baseStats[Set.STAT_AURA]) sprite = RenderInfo.PC_AGILITY;
						
						if(baseStats[Set.STAT_ENDURANCE] > baseStats[Set.STAT_STRENGTH] && baseStats[Set.STAT_ENDURANCE] > baseStats[Set.STAT_AGILITY] &&
							baseStats[Set.STAT_ENDURANCE] > baseStats[Set.STAT_MAGIC] && baseStats[Set.STAT_ENDURANCE] > baseStats[Set.STAT_AURA]) sprite = RenderInfo.PC_ENDURANCE;
						
						if(baseStats[Set.STAT_MAGIC] > baseStats[Set.STAT_AGILITY] && baseStats[Set.STAT_MAGIC] > baseStats[Set.STAT_ENDURANCE] &&
							baseStats[Set.STAT_MAGIC] > baseStats[Set.STAT_STRENGTH] && baseStats[Set.STAT_MAGIC] > baseStats[Set.STAT_AURA]) sprite = RenderInfo.PC_MAGIC;
						
						if(baseStats[Set.STAT_AURA] > baseStats[Set.STAT_AGILITY] && baseStats[Set.STAT_AURA] > baseStats[Set.STAT_ENDURANCE] &&
							baseStats[Set.STAT_AURA] > baseStats[Set.STAT_STRENGTH] && baseStats[Set.STAT_AURA] > baseStats[Set.STAT_MAGIC]) sprite = RenderInfo.PC_AURA;
						
						recalcStats();
						
						hp = stats[Set.STAT_ENDURANCE] * Set.END_MUL;
						
						LevelServer.addChatMessageToAll(name + " has joined the server");
					}
				}
			} else {
				
				List<RenderInfo> renderList = LevelServer.levelServers[curLevel].findRenderInfosOnScreen(x, y);
				List<Nametag> nametags = LevelServer.levelServers[curLevel].findNameTagsOnScreen(x, y);
				
				if(Math.abs(destX - x) > 1 || Math.abs(destY - y) > 1)
				{
					//Log.log((byte)(destX - x + (Set.SCREEN_TILE_WIDTH / 2)) + " " + (byte)(destY - y + (Set.SCREEN_TILE_HEIGHT / 2)));
					renderList.add(new RenderInfo((byte)(destX - x + (Set.SCREEN_TILE_WIDTH / 2)), (byte)(destY - y + (Set.SCREEN_TILE_HEIGHT / 2)), RenderInfo.DEST));
				}
				
				List<Entity> onSquare = LevelServer.levelServers[curLevel].findEntitesOnSquare(x, y);
				
				String squareText = null;
				
				for(Entity ent : onSquare)
				{
					if(ent instanceof Item)
					{
						squareText = ((Item)ent).getItemName();
					}
					if(ent instanceof Writing)
					{
						squareText = "\"" + ((Writing)ent).getMessage() + "\"";
					}
				}
				
				while(in.available())
				{
					Object o = in.read();
					
					if(o instanceof NewDestOrder)
					{
						//Log.log(name + " is moving towards " + ((NewDestOrder)o).x + " " + ((NewDestOrder)o).y);
						
						setDest(((NewDestOrder)o).x, ((NewDestOrder)o).y);
						if(commandQueue.size() > 0)
						{
							commandQueue.clear();
							sendCommandQueueUpdate();
						}
					}
					if(o instanceof PlayerChatMessage)
					{
						PlayerChatMessage pcm = (PlayerChatMessage)o;
						pcm.message = name + " says " + pcm.message;
						
						if(pcm.message.length() > 85)
							pcm.message = pcm.message.substring(0, 85);
						
						if(pcm.isGlobal)
						{
							Log.log(getCompleteName() + " said global: \"" + pcm.message + "\"");
							LevelServer.addChatMessageToAll(pcm.message);
						} else {
							Log.log(getCompleteName() + " said in level " + curLevel + ": \"" + pcm.message + "\"");
							LevelServer.levelServers[curLevel].addChatMessage(pcm.message);
						}
					}
					if(o instanceof PickupCommand)
					{
						for(Entity ent : onSquare)
						{
							if(ent instanceof Item && inventory.size() < 20)
							{
								LevelServer.levelServers[curLevel].removeEntity(ent);
								inventory.add((Item) ent);
								sendInventoryUpdate();
							}
						}
					}
					if(o instanceof EquipCommand)
					{
						int equippedArmor = 0;
						int equippedWeapons = 0;
						
						EquipCommand ec = (EquipCommand)o;
						for(Item itm : inventory)
						{
							if(ec.itemId == itm.getItemStats().id) itm.getItemStats().isEquipped = !itm.getItemStats().isEquipped;
							
							if(itm.getItemStats().isEquipped)
							{
								if(itm.getItemStats().isWeapon)
								{
									if(equippedWeapons >= 1)
										itm.getItemStats().isEquipped = false;
									else
										equippedWeapons++;
								} else {
									if(equippedArmor >= 2)
										itm.getItemStats().isEquipped = false;
									else
										equippedArmor++;
								}
							}
						}
						recalcStats();
						sendInventoryUpdate();
					}
					if(o instanceof DropCommand)
					{
						DropCommand dc = (DropCommand)o;
						Item toDrop = null;
						for(Item itm : inventory)
						{
							if(dc.itemId == itm.getItemStats().id) toDrop = itm;
						}
						
						inventory.remove(toDrop);
						toDrop.getItemStats().isEquipped = false;
						toDrop.setX(x);
						toDrop.setY(y);
						toDrop.resetLifespan();
						LevelServer.levelServers[curLevel].addEntity(toDrop);
						
						recalcStats();
						sendInventoryUpdate();
					}
					if(o instanceof CarveCommand)
					{
						CarveCommand cc = (CarveCommand)o;
						for(Entity ent : onSquare)
						{
							if(ent instanceof Writing) LevelServer.levelServers[curLevel].removeEntity(ent);
						}
						LevelServer.levelServers[curLevel].addEntity(new Writing(x,y,cc.message));
						Log.log(getCompleteName() + " carved: \"" + cc.message + "\" at " + x + ", " + y + " on level " + curLevel);
					}
					if(o instanceof CommandQueueCommand)
					{
						for(int i=0;i<commandQueue.size();++i)
						{
							if(commandQueue.get(i) instanceof StrikeCommand) commandQueue.remove(i--);
						}
						
						CommandQueueCommand cqc = (CommandQueueCommand) o;
						switch(cqc.command)
						{
							case 0: commandQueue.add(new StrikeCommand(cqc.tileX, cqc.tileY, this)); break;
							case 1: commandQueue.add(new ChargeCommand(cqc.tileX, cqc.tileY, this)); break;
							case 2: commandQueue.add(new CleaveCommand(cqc.tileX, cqc.tileY, this)); break;
							case 3: commandQueue.add(new FireballCommand(cqc.tileX, cqc.tileY, this)); break;
							case 4: commandQueue.add(new CurseCommand(cqc.tileX, cqc.tileY, this)); break;
							case 5: commandQueue.add(new SleepCommand(cqc.tileX, cqc.tileY, this)); break;
							case 6: commandQueue.add(new HealCommand(cqc.tileX, cqc.tileY, this)); break;
							case 7: commandQueue.add(new BlessCommand(cqc.tileX, cqc.tileY, this)); break;
							case 8: commandQueue.add(new ShieldCommand(cqc.tileX, cqc.tileY, this)); break;
						}
						sendCommandQueueUpdate();
					}
				}
				
				if(commandQueue.size() > 0)
				{
					destX = x;
					destY = y;
					
					if(!commandQueue.get(0).keep() || commandQueue.get(0).execute())
					{
						commandQueue.remove(0);
						sendCommandQueueUpdate();
					}
				}
				
				if(hp < 0)
					hp -= 0.45f;
				else
					heal(0.35f);
				
				if(curLevel == 5 && y < 20) sendTo(new VictoryMessage());
				
				out.write(new FrameMessage(renderList, x, y, curLevel, squareText, nametags, stats, hp));
				out.flush();
			}
		} catch(Exception e)
		{
			linkDead = true;
			Log.log(name + " has disconnected due to " + e.toString());
		}
		super.update();
	}
	
	protected void sendCommandQueueUpdate() throws IOException
	{
		CommandQueueUpdate up = new CommandQueueUpdate();
		
		for(AbilityCommand ac : commandQueue)
			up.queue.add(ac.getKey());
		
		out.write(up);
		out.flush();
	}
	
	@Override
	protected void recalcStats()
	{
		for(int i=0;i<7;i++)
		{
			stats[i] = baseStats[i];
			for(Item itm : inventory)
			{
				if(itm.getItemStats().isEquipped)
					stats[i] += itm.getItemStats().stats[i];
			}
		}
		super.recalcStats();
	}
	
	public void sendTo(Object o)
	{
		try {
			out.write(o);
			out.flush();
		} catch(Exception e)
		{
			linkDead = true;
			Log.log(name + " has disconnected due to " + e.toString());
		}
	}
	
	public void sendInventoryUpdate() throws IOException
	{
		InventoryUpdate up = new InventoryUpdate();
		
		for(Item itm : inventory)
			up.inventory.add(itm.getItemStats());
		
		out.write(up);
		out.flush();
	}

	@Override
	public boolean keep() {
		return super.keep() && !linkDead && hp > -30;
	}

	@Override
	protected byte getSprite() {
		if(inited)
			return sprite;
		else
			return 0;
	}

	@Override
	public boolean canAct() {
		return super.canAct() && hp > 0;
	}

	@Override
	public String getName() {
		return name;
	}
	
	public boolean isActionUp()
	{
		return mainActionTimer <= 0 && canAct();
	}
	
	public void actionUsed()
	{
		mainActionTimer = Set.ACTION_DELAY;
	}

	@Override
	public void destroyed() {
		sendTo(new KilledMessage());
		
		while(inventory.size() > 0)
		{
			Item toDrop = inventory.get(0);
			inventory.remove(toDrop);
			toDrop.getItemStats().isEquipped = false;
			toDrop.setX(x);
			toDrop.setY(y);
			toDrop.resetLifespan();
			LevelServer.levelServers[curLevel].addEntity(toDrop);
		}
		
		super.destroyed();
	}

	@Override
	public Nametag getNametag(int camX, int camY) {
		return new Nametag((byte)(x - camX + (Set.SCREEN_TILE_WIDTH / 2)), (byte)(y - camY + (Set.SCREEN_TILE_HEIGHT / 2)), getName());
	}
}
