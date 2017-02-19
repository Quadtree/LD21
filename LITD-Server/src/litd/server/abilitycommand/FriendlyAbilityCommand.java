package litd.server.abilitycommand;

import litd.entity.Player;
import litd.server.LevelServer;

public abstract class FriendlyAbilityCommand extends AbilityCommand {
	public FriendlyAbilityCommand(int x, int y, Player user)
	{
		super(LevelServer.levelServers[user.getCurLevel()].getNearestPlayerTo(x, y), user);
	}
}
