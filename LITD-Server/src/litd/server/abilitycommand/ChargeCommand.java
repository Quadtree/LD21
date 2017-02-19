package litd.server.abilitycommand;

import litd.entity.Player;
import litd.server.LevelServer;
import litd.shared.Set;

public class ChargeCommand extends HostileAbilityCommand {

	public ChargeCommand(int x, int y, Player user) {
		super(x, y, user);
	}
	
	@Override
	public boolean execute() {
		if(!LevelServer.levelServers[user.getCurLevel()].isInLOS(user.getX(), user.getY(), target.getX(), target.getY())) return true;
		
		if(user.isActionUp())
		{
			int bestDist = Integer.MAX_VALUE;
			int bx = -1,by = -1;
			
			for(int cx=target.getX() - 1;cx <= target.getX() + 1;cx++)
			{
				for(int cy=target.getY() - 1;cy <= target.getY() + 1;cy++)
				{
					int dist = Math.abs(cx - user.getX()) + Math.abs(cy - user.getY());
					if(dist < bestDist)
					{
						bestDist = dist;
						bx = cx;
						by = cy;
					}
				}
			}
			
			user.setX(bx);
			user.setY(by);
			user.setDest(bx, by);
			target.stun((int)(user.getStat(Set.STAT_STRENGTH) / 50.0f * bestDist));
			
			LevelServer.levelServers[user.getCurLevel()].postSound("charge", target.getX(), target.getY());
			
			user.actionUsed();
			
			return true;
		}
		
		return false;
	}

	@Override
	public byte getKey() {
		return 2;
	}

}
