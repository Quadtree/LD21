package litd.server.abilitycommand;

import litd.entity.Player;
import litd.server.LevelServer;
import litd.shared.Set;

public class HealCommand extends FriendlyAbilityCommand {

	public HealCommand(int x, int y, Player user) {
		super(x, y, user);
	}

	@Override
	public boolean execute() {
		if(!LevelServer.levelServers[user.getCurLevel()].isInLOS(user.getX(), user.getY(), target.getX(), target.getY())) return true;
		
		if(user.isActionUp())
		{
			float amount = user.getStat(Set.STAT_AURA) / 2.5f * (float)Math.random();
			LevelServer.levelServers[user.getCurLevel()].postCombatMessage(user.getName() + " heals " + target.getName() + " for " + (int)amount, target.getX(), target.getY());
			LevelServer.levelServers[user.getCurLevel()].postSound("heal", target.getX(), target.getY());
			target.heal(amount);
			user.actionUsed();
			return true;
		}
		return false;
	}

	@Override
	public byte getKey() {
		return 6;
	}
}
