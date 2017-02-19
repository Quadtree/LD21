package litd.server.abilitycommand;

import litd.entity.Player;
import litd.server.LevelServer;
import litd.shared.Set;

public class BlessCommand extends FriendlyAbilityCommand {

	public BlessCommand(int x, int y, Player user) {
		super(x, y, user);
	}

	@Override
	public boolean execute() {
		if(!LevelServer.levelServers[user.getCurLevel()].isInLOS(user.getX(), user.getY(), target.getX(), target.getY())) return true;
		
		if(user.isActionUp())
		{
			LevelServer.levelServers[user.getCurLevel()].postCombatMessage(user.getName() + " blesses " + target.getName(), target.getX(), target.getY());
			LevelServer.levelServers[user.getCurLevel()].postSound("bless", target.getX(), target.getY());
			target.applyBless(user.getStat(Set.STAT_AURA));
			user.actionUsed();
			return true;
		}
		return false;
	}

	@Override
	public byte getKey() {
		return 7;
	}
}
