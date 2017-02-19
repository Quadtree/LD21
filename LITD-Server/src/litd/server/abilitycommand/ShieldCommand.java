package litd.server.abilitycommand;

import litd.entity.Player;
import litd.server.LevelServer;
import litd.shared.Set;

public class ShieldCommand extends FriendlyAbilityCommand {

	public ShieldCommand(int x, int y, Player user) {
		super(x, y, user);
	}

	@Override
	public boolean execute() {
		if(!LevelServer.levelServers[user.getCurLevel()].isInLOS(user.getX(), user.getY(), target.getX(), target.getY())) return true;
		
		if(user.isActionUp())
		{
			LevelServer.levelServers[user.getCurLevel()].postCombatMessage(user.getName() + " shields " + target.getName(), target.getX(), target.getY());
			LevelServer.levelServers[user.getCurLevel()].postSound("shield", target.getX(), target.getY());
			target.applyShield(user.getStat(Set.STAT_AURA) / 6);
			user.actionUsed();
			return true;
		}
		return false;
	}

	@Override
	public byte getKey() {
		return 8;
	}
}
