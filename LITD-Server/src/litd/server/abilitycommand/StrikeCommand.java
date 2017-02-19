package litd.server.abilitycommand;

import litd.entity.Player;

public class StrikeCommand extends HostileAbilityCommand {

	public StrikeCommand(int x, int y, Player user) {
		super(x, y, user);
	}
	
	@Override
	public boolean execute() {
		user.setDest(target.getX(), target.getY());
		return false;
	}

	@Override
	public byte getKey() {
		return 0;
	}

}
