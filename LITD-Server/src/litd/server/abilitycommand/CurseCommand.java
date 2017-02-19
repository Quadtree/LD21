package litd.server.abilitycommand;

import litd.entity.Player;
import litd.server.LevelServer;
import litd.shared.Set;

public class CurseCommand extends HostileAbilityCommand {

	public CurseCommand(int x, int y, Player user) {
		super(x, y, user);
	}
	
	@Override
	public boolean execute() {
		if(!LevelServer.levelServers[user.getCurLevel()].isInLOS(user.getX(), user.getY(), target.getX(), target.getY())) return true;
		
		if(user.isActionUp())
		{
			LevelServer.levelServers[user.getCurLevel()].postCombatMessage(user.getName() + " curses " + target.getName(), target.getX(), target.getY());
			LevelServer.levelServers[user.getCurLevel()].postSound("curse", target.getX(), target.getY());
			target.applyBless(-(1.0f - (float)Math.pow(0.995315f, user.getStat(Set.STAT_MAGIC))));
			user.actionUsed();
			
			return true;
		}
		
		return false;
	}

	@Override
	public byte getKey() {
		return 4;
	}

}
