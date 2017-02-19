package litd.server.abilitycommand;

import litd.entity.Monster;
import litd.entity.Player;
import litd.server.LevelServer;
import litd.shared.RenderInfo;
import litd.shared.Set;

public class CleaveCommand extends HostileAbilityCommand {

	public CleaveCommand(int x, int y, Player user) {
		super(x, y, user);
	}
	
	@Override
	public boolean execute() {
		if(Math.abs(user.getX() - target.getX()) > 1 || Math.abs(user.getY() - target.getY()) > 1)
		{
			user.setDest(target.getX(), target.getY());
		} else {
			if(user.isActionUp())
			{
				for(Monster mon : LevelServer.levelServers[user.getCurLevel()].explosion(target.getX(), target.getY(), 1, 10, RenderInfo.CLEAVE))
				{
					float amount = user.getStat(Set.STAT_STRENGTH) * (float)Math.random() / 4;
					float actualAmount = mon.takeDamage(amount, false);
					
					LevelServer.levelServers[user.getCurLevel()].postSound("playermelee" + (int)(Math.random() * 3), target.getX(), target.getY());
					LevelServer.levelServers[user.getCurLevel()].postCombatMessage(user.getName() + " cleaves " + mon.getName() + " for " + (int)actualAmount + " damage (" + (int)(amount - actualAmount) + " absorbed)", target.getX(), target.getY());
				}
				
				user.actionUsed();
				return true;
			}
		}
		return false;
	}

	@Override
	public byte getKey() {
		return 2;
	}

}
