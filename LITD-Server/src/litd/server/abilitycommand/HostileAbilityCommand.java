package litd.server.abilitycommand;

import litd.entity.Player;
import litd.server.LevelServer;

public abstract class HostileAbilityCommand extends AbilityCommand {
	public HostileAbilityCommand(int x, int y, Player user)
	{
		super(LevelServer.levelServers[user.getCurLevel()].getNearestMonsterTo(x, y), user);
	}

	@Override
	public boolean keep() {
		return super.keep() && target.getHP() > 0;
	}
}
