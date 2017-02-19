package sgf.demo;

import sgf.GameInterface;
import sgf.SGF;

public class SGFDemoGame implements GameInterface {

	@Override
	public void init() {
		// TODO Auto-generated method stub

	}

	@Override
	public void render() {
		SGF.getInstance().renderImage("x", (int)(200 + Math.random() * 100), (int)(200 + Math.random() * 100), 150, 150, 0.25f, true);
		SGF.getInstance().renderText("Test!", 0, 0, 255, 0, 0, true, 32);
		
		SGF.getInstance().setCamera(0, 0, 1);
	}

	@Override
	public void update() {
		if(Math.random() < 0.01)
			SGF.getInstance().playSound("test");
	}

	@Override
	public void shutdown() {
		// TODO Auto-generated method stub

	}

}
