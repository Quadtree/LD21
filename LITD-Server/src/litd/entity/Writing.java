package litd.entity;

import litd.shared.RenderInfo;

public class Writing extends Entity {
	String message;
	
	public Writing(int x, int y, String message)
	{
		this.x = x;
		this.y = y;
		if(message.length() > 75)
			message = message.substring(0, 75);
		
		this.message = message;
	}

	public String getMessage() {
		return message;
	}

	@Override
	protected byte getSprite() {
		return RenderInfo.CARVING;
	}
}
