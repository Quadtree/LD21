package litd.server.abilitycommand;

import litd.entity.Mobile;
import litd.entity.Player;

public abstract class AbilityCommand {
	protected Mobile target;
	protected Player user;

	public AbilityCommand(Mobile subject, Player user) {
		super();
		this.target = subject;
		this.user = user;
	}
	
	public boolean keep()
	{
		return target != null;
	}
	
	/**
	 * Runs this command.
	 * @return If the command is complete
	 */
	
	public abstract boolean execute();
	public abstract byte getKey();
}
