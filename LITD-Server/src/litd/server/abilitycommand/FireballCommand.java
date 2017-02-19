package litd.server.abilitycommand;

import litd.entity.Monster;
import litd.entity.Player;
import litd.server.LevelServer;
import litd.shared.RenderInfo;
import litd.shared.Set;

public class FireballCommand extends HostileAbilityCommand {

	public FireballCommand(int x, int y, Player user) {
		super(x, y, user);
	}
	
	@Override
	public boolean execute() {
		if(!LevelServer.levelServers[user.getCurLevel()].isInLOS(user.getX(), user.getY(), target.getX(), target.getY())) return true;
		
		if(user.isActionUp())
		{
			for(Monster mon : LevelServer.levelServers[user.getCurLevel()].explosion(target.getX(), target.getY(), 2, 3, RenderInfo.EXPLOSION))
			{
				float amount = user.getStat(Set.STAT_MAGIC) * (float)Math.random() / 5;
				float actualAmount = mon.takeDamage(amount, true);
				
				LevelServer.levelServers[user.getCurLevel()].postCombatMessage(user.getName() + " burns " + mon.getName() + " for " + (int)actualAmount + " fire damage (" + (int)(amount - actualAmount) + " absorbed)", target.getX(), target.getY());
				LevelServer.levelServers[user.getCurLevel()].postSound("fireball", target.getX(), target.getY());
			}
			
			user.actionUsed();
			
			return true;
		}
		
		return false;
	}

	@Override
	public byte getKey() {
		return 3;
	}

}
