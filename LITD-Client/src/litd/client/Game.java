package litd.client;

import java.awt.Image;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;
import java.io.FileInputStream;
import java.io.InputStream;
import java.io.ObjectInputStream;
import java.util.ArrayList;
import java.util.List;

import litd.shared.CarveCommand;
import litd.shared.CommandQueueCommand;
import litd.shared.CommandQueueUpdate;
import litd.shared.DropCommand;
import litd.shared.EquipCommand;
import litd.shared.FrameMessage;
import litd.shared.InventoryUpdate;
import litd.shared.ItemStats;
import litd.shared.KilledMessage;
import litd.shared.LevelGeometry;
import litd.shared.Nametag;
import litd.shared.NewDestOrder;
import litd.shared.PickupCommand;
import litd.shared.PlayerChatMessage;
import litd.shared.PlayerInitInfo;
import litd.shared.RenderInfo;
import litd.shared.ServerChatMessage;
import litd.shared.ServerCombatMessage;
import litd.shared.Set;
import litd.shared.SoundCommand;
import litd.shared.VictoryMessage;
import appnet.Client;
import sgf.GameInterface;
import sgf.SGF;

public class Game implements GameInterface, KeyListener, MouseListener, MouseMotionListener {
	
	String serverHostname = "127.0.0.1";

	Client client;
	
	boolean sentInitData = false;
	
	FrameMessage lastFrameMessage;
	InventoryUpdate lastInventoryUpdate = new InventoryUpdate();
	CommandQueueUpdate lastCommandQueueUpdate = new CommandQueueUpdate();
	int lastRenderedLevel = -1;
	
	LevelGeometry geom;
	
	Image[] sprites = new Image[64];
	
	String[] chatLog = new String[4];
	String[] combatLog = new String[6];
	
	List<String> itemInfoLines = null;
	
	boolean inventoryDisplayed = false;
	
	boolean defeatMessage = false;
	boolean victoryMessage = false;
	boolean helpMessage = false;
	
	public Game(String serverHostname)
	{
		this.serverHostname = serverHostname;
	}
	
	@Override
	public void init() {
		sprites[RenderInfo.PC_STRENGTH] = Main.s.getImage(Main.s.getDocumentBase(), "./media/pc_str.png");
		sprites[RenderInfo.PC_AGILITY] = Main.s.getImage(Main.s.getDocumentBase(), "./media/pc_agl.png");
		sprites[RenderInfo.PC_ENDURANCE] = Main.s.getImage(Main.s.getDocumentBase(), "./media/pc_end.png");
		sprites[RenderInfo.PC_MAGIC] = Main.s.getImage(Main.s.getDocumentBase(), "./media/pc_mgc.png");
		sprites[RenderInfo.PC_AURA] = Main.s.getImage(Main.s.getDocumentBase(), "./media/pc_ara.png");
		
		sprites[RenderInfo.DEST] = Main.s.getImage(Main.s.getDocumentBase(), "./media/dest.png");
		
		sprites[RenderInfo.TRANS_0] = Main.s.getImage(Main.s.getDocumentBase(), "./media/trans0.png");
		sprites[RenderInfo.TRANS_1] = Main.s.getImage(Main.s.getDocumentBase(), "./media/trans1.png");
		sprites[RenderInfo.TRANS_2] = Main.s.getImage(Main.s.getDocumentBase(), "./media/trans2.png");
		sprites[RenderInfo.TRANS_3] = Main.s.getImage(Main.s.getDocumentBase(), "./media/trans3.png");
		sprites[RenderInfo.TRANS_4] = Main.s.getImage(Main.s.getDocumentBase(), "./media/trans4.png");
		sprites[RenderInfo.TRANS_5] = Main.s.getImage(Main.s.getDocumentBase(), "./media/trans5.png");
		sprites[RenderInfo.TRANS_6] = Main.s.getImage(Main.s.getDocumentBase(), "./media/trans6.png");
		sprites[RenderInfo.TRANS_7] = Main.s.getImage(Main.s.getDocumentBase(), "./media/trans7.png");
		sprites[RenderInfo.TRANS_8] = Main.s.getImage(Main.s.getDocumentBase(), "./media/trans8.png");
		sprites[RenderInfo.TRANS_9] = Main.s.getImage(Main.s.getDocumentBase(), "./media/trans9.png");
		
		sprites[RenderInfo.SWORD] = Main.s.getImage(Main.s.getDocumentBase(), "./media/sword.png");
		sprites[RenderInfo.SPEAR] = Main.s.getImage(Main.s.getDocumentBase(), "./media/spear.png");
		sprites[RenderInfo.WARHAMMER] = Main.s.getImage(Main.s.getDocumentBase(), "./media/warhammer.png");
		sprites[RenderInfo.STAFF] = Main.s.getImage(Main.s.getDocumentBase(), "./media/staff.png");
		sprites[RenderInfo.ROBE] = Main.s.getImage(Main.s.getDocumentBase(), "./media/robe.png");
		sprites[RenderInfo.LEATHER_ARMOR] = Main.s.getImage(Main.s.getDocumentBase(), "./media/leatherarmor.png");
		sprites[RenderInfo.PLATE_ARMOR] = Main.s.getImage(Main.s.getDocumentBase(), "./media/platearmor.png");
		
		sprites[RenderInfo.CARVING] = Main.s.getImage(Main.s.getDocumentBase(), "./media/carving.png");
		
		sprites[RenderInfo.GOBLIN] = Main.s.getImage(Main.s.getDocumentBase(), "./media/goblin.png");
		sprites[RenderInfo.GOBLIN_SORC] = Main.s.getImage(Main.s.getDocumentBase(), "./media/goblin_sorc.png");
		sprites[RenderInfo.TENTACLE] = Main.s.getImage(Main.s.getDocumentBase(), "./media/tentacle.png");
		sprites[RenderInfo.CAVE_SLUG] = Main.s.getImage(Main.s.getDocumentBase(), "./media/caveslug.png");
		sprites[RenderInfo.DEMON] = Main.s.getImage(Main.s.getDocumentBase(), "./media/demon.png");
		sprites[RenderInfo.LICH] = Main.s.getImage(Main.s.getDocumentBase(), "./media/lich.png");
		sprites[RenderInfo.FROST_ELEMENTAL] = Main.s.getImage(Main.s.getDocumentBase(), "./media/frost_elemental.png");
		sprites[RenderInfo.FIRE_ELEMENTAL] = Main.s.getImage(Main.s.getDocumentBase(), "./media/fire_elemental.png");
		sprites[RenderInfo.DEATH_SPIRIT] = Main.s.getImage(Main.s.getDocumentBase(), "./media/death_spirit.png");
		
		sprites[RenderInfo.EXPLOSION] = Main.s.getImage(Main.s.getDocumentBase(), "./media/explosion.png");
		sprites[RenderInfo.CLEAVE] = Main.s.getImage(Main.s.getDocumentBase(), "./media/cleave.png");
		
		sprites[RenderInfo.EFF_SLEEP] = Main.s.getImage(Main.s.getDocumentBase(), "./media/sleep.png");
		sprites[RenderInfo.EFF_STUN] = Main.s.getImage(Main.s.getDocumentBase(), "./media/stun.png");
		sprites[RenderInfo.EFF_CURSE] = Main.s.getImage(Main.s.getDocumentBase(), "./media/curse.png");
		sprites[RenderInfo.EFF_BLESS] = Main.s.getImage(Main.s.getDocumentBase(), "./media/bless.png");
		sprites[RenderInfo.EFF_SHIELD] = Main.s.getImage(Main.s.getDocumentBase(), "./media/shield.png");
		sprites[RenderInfo.EFF_INJURED] = Main.s.getImage(Main.s.getDocumentBase(), "./media/injured.png");
		sprites[RenderInfo.EFF_DYING] = Main.s.getImage(Main.s.getDocumentBase(), "./media/dying.png");
		
		SGF.getInstance().addKeyListener(this);
		SGF.getInstance().addMouseListener(this);
		SGF.getInstance().addMouseMotionListener(this);
		
		System.out.print("Launching client thread...");
		client = new Client(serverHostname);
		new Thread(client).start();
	}

	@Override
	public void render() {
		try {
			if(defeatMessage)
			{
				SGF.getInstance().renderImage("dead", 512, 512, 1024, 1024, 0, false);
				return;
			}
			if(victoryMessage)
			{
				SGF.getInstance().renderImage("victory", 512, 512, 1024, 1024, 0, false);
				return;
			}
			if(helpMessage)
			{
				SGF.getInstance().renderImage("help", 512, 512, 1024, 1024, 0, false);
				return;
			}
			
			if(lastFrameMessage == null) return;
			
				if(lastRenderedLevel != lastFrameMessage.level)
				{
					System.out.println("Level changed to " + lastFrameMessage.level);
					if(lastRenderedLevel != -1)
					{
						if(lastFrameMessage.level == 0 || lastFrameMessage.level == 6 || lastFrameMessage.level == 7) addCombatLogMessage("This level exudes a disturbingly framiliar sense of wrongness");
						if(lastFrameMessage.level == 5 || lastFrameMessage.level == 8 || lastFrameMessage.level == 9) addCombatLogMessage("It looks like the outside! Could you have found the exit?");	
					}
					
					InputStream in = getClass().getResourceAsStream("/media/level" + lastFrameMessage.level + ".lvl");
					if(in == null) in = new FileInputStream("./media/level" + lastFrameMessage.level + ".lvl");
					
					ObjectInputStream ois = new ObjectInputStream(in);
					geom = (LevelGeometry) ois.readObject();
					lastRenderedLevel = lastFrameMessage.level;
				}
				
				//SGF.getInstance().renderImage("wall0", 50, 50, 0.5f, 0.5f, 0, false);
				
				for(int y=0;y<=Set.SCREEN_TILE_HEIGHT;++y)
				{
					for(int x=0;x<=Set.SCREEN_TILE_WIDTH;++x)
					{
						if(geom.isWall(x + lastFrameMessage.x - (Set.SCREEN_TILE_WIDTH / 2), y + lastFrameMessage.y - (Set.SCREEN_TILE_HEIGHT / 2)))
							SGF.getInstance().renderImage("wall" + lastFrameMessage.level, x*32, y*32, 32, 32, 0, false);
						else
							SGF.getInstance().renderImage("floor" + lastFrameMessage.level, x*32, y*32, 32, 32, 0, false);
					}
				}
				
				//System.out.println(lastFrameMessage.renderInfos);
				
				for(RenderInfo ri : lastFrameMessage.renderInfos)
				{
					if(sprites[ri.sprite] == null) System.out.println("Cant find sprite " + ri.sprite);
					SGF.getInstance().renderImage(sprites[ri.sprite], ri.x*32, ri.y*32, 32, 32, 0, false);
				}
				
				for(Nametag nt : lastFrameMessage.nametags)
				{
					SGF.getInstance().renderText(nt.tag, nt.x*32, nt.y*32 - 22, 255, 255, 255, false, 15);
				}
				
				SGF.getInstance().renderImage("dialogbackdrop", 30+(750/2) - 15, 820 + (220/2) - 20, 750, 220, 0, false);
				
				for(int i=0;i<chatLog.length;i++)
				{
					if(chatLog[i] != null)
						SGF.getInstance().renderText(chatLog[i], 30, 820 + i * 20, 0, 0, 0, false, 15);
				}
				
				for(int i=0;i<combatLog.length;i++)
				{
					if(combatLog[i] != null)
						SGF.getInstance().renderText(combatLog[i], 30, 900 + i * 20, 92, 0, 0, false, 15);
				}
				
				SGF.getInstance().renderImage("dialogbackdrop", 20+(120/2) - 15, 380 + (150/2) - 20, 120, 150, 0, false);
				SGF.getInstance().renderText("HP: " + (int)lastFrameMessage.hp + "/" + (int)(lastFrameMessage.stats[Set.STAT_ENDURANCE] * Set.END_MUL), 20, 380, 0, 0, 0, false, 15);
				SGF.getInstance().renderText("STR: " + (int)lastFrameMessage.stats[Set.STAT_STRENGTH], 20, 400, 0, 0, 0, false, 15);
				SGF.getInstance().renderText("AGL: " + (int)lastFrameMessage.stats[Set.STAT_AGILITY], 20, 420, 0, 0, 0, false, 15);
				SGF.getInstance().renderText("MGC: " + (int)lastFrameMessage.stats[Set.STAT_MAGIC], 20, 440, 0, 0, 0, false, 15);
				SGF.getInstance().renderText("ARA: " + (int)lastFrameMessage.stats[Set.STAT_AURA], 20, 460, 0, 0, 0, false, 15);
				SGF.getInstance().renderText("ARM: " + (int)lastFrameMessage.stats[Set.STAT_ARMOR], 20, 480, 0, 0, 0, false, 15);
				SGF.getInstance().renderText("RES: " + (int)lastFrameMessage.stats[Set.STAT_RESISTANCE], 20, 500, 0, 0, 0, false, 15);
				
				if(lastFrameMessage.message != null)
				{
					SGF.getInstance().renderImage("dialogbackdrop", 200+(750/2) - 15, 90 + (30/2) - 20, 750, 30, 0, false);
					SGF.getInstance().renderText("On Square: " + lastFrameMessage.message, 200, 90, 0, 0, 0, false, 15);
				}
				
				if(inventoryDisplayed)
				{
					SGF.getInstance().renderImage("dialogbackdrop", 250+(450/2) - 15, 300 + (500/2) - 25, 450, 500, 0, false);
					
					for(int i=0;i<lastInventoryUpdate.inventory.size();i++)
					{
						SGF.getInstance().renderText(lastInventoryUpdate.inventory.get(i).name, 400, 300+i*20, 0, 0, lastInventoryUpdate.inventory.get(i).isEquipped ? 255 : 0, false, 15);
						SGF.getInstance().renderImage("dropitem", 670, 300+i*20 - 5, 16, 16, 0, false);
					}
					if(itemInfoLines != null)
					{
						for(int i=0;i<itemInfoLines.size();i++)
						{
							if(itemInfoLines.get(i).charAt(0) == '-')
								SGF.getInstance().renderText(itemInfoLines.get(i), 250, 300+i*20, 255, 0, 0, false, 15);
							else
								SGF.getInstance().renderText("+" + itemInfoLines.get(i), 250, 300+i*20, 0, 255, 0, false, 15);
						}
					}
				}
				
				for(int i=0;i<9;i++)
				{
					SGF.getInstance().renderImage("ability" + i, 100+i*96, 30, 64, 64, 0, false);
					SGF.getInstance().renderText("" + (i + 1), 120+i*96, 55, 255, 0, 0, false, 15);
				}
				
				for(int i=0;i<lastCommandQueueUpdate.queue.size();++i)
				{
					SGF.getInstance().renderImage("ability" + lastCommandQueueUpdate.queue.get(i), 950, 100+54*i, 48, 48, 0, false);
				}
		} catch(Exception e)
		{
			e.printStackTrace();
		}
	}

	@Override
	public void update() {
		try {
			if(!client.isConnected()) return;
			if(!sentInitData)
			{
				System.out.println("Connection established...");
				client.sendMessage(Main.s.playerInitInfo);
				Main.s.playerInitInfo = null;
				sentInitData = true;
			}
			Object o;
			while((o = client.getMessage()) != null)
			{
				if(o instanceof FrameMessage) lastFrameMessage = (FrameMessage) o;
				if(o instanceof ServerChatMessage)
				{
					for(int i=chatLog.length - 1;i > 0;i--)
					{
						chatLog[i] = chatLog[i - 1];
					}
					chatLog[0] = ((ServerChatMessage)o).message;
				}
				if(o instanceof ServerCombatMessage)
				{
					addCombatLogMessage(((ServerCombatMessage) o).message);
				}
				if(o instanceof InventoryUpdate)
				{
					lastInventoryUpdate = (InventoryUpdate)o;
					System.out.println(lastInventoryUpdate.inventory);
				}
				if(o instanceof CommandQueueUpdate)
				{
					lastCommandQueueUpdate = (CommandQueueUpdate) o;
					System.out.println(lastCommandQueueUpdate.queue);
				}
				if(o instanceof KilledMessage) defeatMessage = true;
				if(o instanceof VictoryMessage) victoryMessage = true;
				if(o instanceof SoundCommand)
				{
					SGF.getInstance().playSound(((SoundCommand)o).name);
				}
				//System.out.println(lastFrameMessage.x + " " + lastFrameMessage.y);
			}
		} catch(Exception e)
		{
			e.printStackTrace();
		}
	}
	
	protected void addCombatLogMessage(String msg)
	{
		for(int i=combatLog.length - 1;i > 0;i--)
		{
			combatLog[i] = combatLog[i - 1];
		}
		combatLog[0] = msg;
	}
	
	protected void sendNewDest(int x, int y)
	{
		client.sendMessage(new NewDestOrder(x,y));
	}

	@Override
	public void shutdown() {
		client.stop();
	}

	@Override
	public void mouseClicked(MouseEvent arg0) {
		
	}

	@Override
	public void mouseEntered(MouseEvent arg0) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void mouseExited(MouseEvent arg0) {
		// TODO Auto-generated method stub
		
	}
	
	int tileX;
	int tileY;
	
	protected void setTilePos(int mx, int my)
	{
		if(lastFrameMessage == null) return;
		
		tileX = (mx+16) / 32 + lastFrameMessage.x - (Set.SCREEN_TILE_WIDTH / 2);
		tileY = (my+16) / 32 + lastFrameMessage.y - (Set.SCREEN_TILE_HEIGHT / 2);
	}

	@Override
	public void mousePressed(MouseEvent arg0) {
		
		if(helpMessage) helpMessage = false;
		
		int nmx = arg0.getX() * 1024 / 768, nmy = arg0.getY() * 1024 / 768;
		
		if(inventoryDisplayed && nmx > 300 && nmx < 700 && nmy > 288 && nmy < 800)
		{
			int itemInd = (nmy - 288) / 20;
			if(itemInd < lastInventoryUpdate.inventory.size())
			{
				if(nmx > 660) // its a DROP command
					client.sendMessage(new DropCommand(lastInventoryUpdate.inventory.get(itemInd).id));
				else
					client.sendMessage(new EquipCommand(lastInventoryUpdate.inventory.get(itemInd).id));
			}
			
			return;
		}
		
		setTilePos(nmx, nmy);
		
		lastFrameMessage.renderInfos.add(new RenderInfo((byte)(tileX - lastFrameMessage.x + (Set.SCREEN_TILE_WIDTH / 2)), (byte)(tileY - lastFrameMessage.y + (Set.SCREEN_TILE_HEIGHT / 2)), RenderInfo.DEST));
		
		sendNewDest(tileX,tileY);
	}

	@Override
	public void mouseReleased(MouseEvent arg0) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void keyPressed(KeyEvent arg0) {
		if(arg0.getKeyCode() == KeyEvent.VK_H)
			helpMessage = !helpMessage;
		else if(helpMessage)
			helpMessage = false;
		
		switch(arg0.getKeyCode())
		{
			case KeyEvent.VK_NUMPAD1: sendNewDest(lastFrameMessage.x - 1, lastFrameMessage.y + 1); break;
			case KeyEvent.VK_NUMPAD2: sendNewDest(lastFrameMessage.x, lastFrameMessage.y + 1); break;
			case KeyEvent.VK_NUMPAD3: sendNewDest(lastFrameMessage.x + 1, lastFrameMessage.y + 1); break;
			case KeyEvent.VK_NUMPAD4: sendNewDest(lastFrameMessage.x - 1, lastFrameMessage.y); break;
			//
			case KeyEvent.VK_NUMPAD6: sendNewDest(lastFrameMessage.x + 1, lastFrameMessage.y); break;
			case KeyEvent.VK_NUMPAD7: sendNewDest(lastFrameMessage.x - 1, lastFrameMessage.y - 1); break;
			case KeyEvent.VK_NUMPAD8: sendNewDest(lastFrameMessage.x, lastFrameMessage.y - 1); break;
			case KeyEvent.VK_NUMPAD9: sendNewDest(lastFrameMessage.x + 1, lastFrameMessage.y - 1); break;
			
			case KeyEvent.VK_I: inventoryDisplayed = !inventoryDisplayed; break;
			
			case KeyEvent.VK_P: client.sendMessage(new PickupCommand()); break;
			
			case KeyEvent.VK_1: client.sendMessage(new CommandQueueCommand((byte)0, tileX, tileY)); break;
			case KeyEvent.VK_2: client.sendMessage(new CommandQueueCommand((byte)1, tileX, tileY)); break;
			case KeyEvent.VK_3: client.sendMessage(new CommandQueueCommand((byte)2, tileX, tileY)); break;
			case KeyEvent.VK_4: client.sendMessage(new CommandQueueCommand((byte)3, tileX, tileY)); break;
			case KeyEvent.VK_5: client.sendMessage(new CommandQueueCommand((byte)4, tileX, tileY)); break;
			case KeyEvent.VK_6: client.sendMessage(new CommandQueueCommand((byte)5, tileX, tileY)); break;
			case KeyEvent.VK_7: client.sendMessage(new CommandQueueCommand((byte)6, tileX, tileY)); break;
			case KeyEvent.VK_8: client.sendMessage(new CommandQueueCommand((byte)7, tileX, tileY)); break;
			case KeyEvent.VK_9: client.sendMessage(new CommandQueueCommand((byte)8, tileX, tileY)); break;
			
			case KeyEvent.VK_ENTER:
				Main.s.textPromptLabel.setText("Say Local: ");
				Main.s.textPromptBox.setText("");
				Main.s.textPromptPanel.setVisible(true);
				Main.s.textPromptBox.requestFocus();
				Main.s.textPromptButtonAction = new ActionListener(){

					@Override
					public void actionPerformed(ActionEvent arg0) {
						client.sendMessage(new PlayerChatMessage(Main.s.textPromptBox.getText(), false));
					}
				};
			break;
			
			case KeyEvent.VK_G:
				Main.s.textPromptLabel.setText("Say Global: ");
				Main.s.textPromptBox.setText("");
				Main.s.textPromptPanel.setVisible(true);
				Main.s.textPromptBox.requestFocus();
				Main.s.textPromptButtonAction = new ActionListener(){

					@Override
					public void actionPerformed(ActionEvent arg0) {
						client.sendMessage(new PlayerChatMessage(Main.s.textPromptBox.getText(), true));
					}
				};
			break;
			
			case KeyEvent.VK_C:
				Main.s.textPromptLabel.setText("Carve: ");
				Main.s.textPromptBox.setText("");
				Main.s.textPromptPanel.setVisible(true);
				Main.s.textPromptBox.requestFocus();
				Main.s.textPromptButtonAction = new ActionListener(){

					@Override
					public void actionPerformed(ActionEvent arg0) {
						client.sendMessage(new CarveCommand(Main.s.textPromptBox.getText()));
					}
				};
			break;
		}
	}

	@Override
	public void keyReleased(KeyEvent arg0) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void keyTyped(KeyEvent arg0) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void mouseDragged(MouseEvent arg0) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void mouseMoved(MouseEvent arg0) {
		
		int nmx = arg0.getX() * 1024 / 768, nmy = arg0.getY() * 1024 / 768;
		
		setTilePos(nmx, nmy);
		
		if(inventoryDisplayed && nmx > 300 && nmx < 700 && nmy > 288 && nmy < 800)
		{
			int itemInd = (nmy - 288) / 20;
			if(itemInd < lastInventoryUpdate.inventory.size())
			{
				List<String> infoLines = new ArrayList<String>();
				for(int i=0;i<7;i++)
				{
					if(Math.abs(lastInventoryUpdate.inventory.get(itemInd).stats[i]) > 0)
					{
						infoLines.add((int)lastInventoryUpdate.inventory.get(itemInd).stats[i] + " " + Set.statNames[i]);
					}
				}
				itemInfoLines = infoLines;
			}
		}
	}

}
