package litd.server.abilitycommand;

import litd.entity.Player;
import litd.server.LevelServer;
import litd.shared.Set;

public class SleepCommand extends HostileAbilityCommand {

	public SleepCommand(int x, int y, Player user) {
		super(x, y, user);
	}
	
	@Override
	public boolean execute() {
		if(!LevelServer.levelServers[user.getCurLevel()].isInLOS(user.getX(), user.getY(), target.getX(), target.getY())) return true;
		
		if(user.isActionUp())
		{
			target.sleep((int)user.getStat(Set.STAT_MAGIC));
			LevelServer.levelServers[user.getCurLevel()].postSound("sleep", target.getX(), target.getY());
			
			user.actionUsed();
			
			return true;
		}
		
		return false;
	}

	@Override
	public byte getKey() {
		return 5;
	}

}
