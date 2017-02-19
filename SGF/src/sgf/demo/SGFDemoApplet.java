package sgf.demo;

import java.applet.Applet;

import sgf.SGF;

public class SGFDemoApplet extends Applet {

	/**
	 * 
	 */
	private static final long serialVersionUID = -4018752311213670064L;

	@Override
	public void destroy() {
		SGF.getInstance().stop();
		super.destroy();
	}

	@Override
	public void init() {
		//SGF.getInstance().start(this, this, new SGFDemoGame());
		SGF.getInstance().setRenderFPS(true);
		
		super.init();
	}
}
