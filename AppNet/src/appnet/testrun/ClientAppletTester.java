package appnet.testrun;

import java.applet.Applet;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.io.IOException;

import appnet.Client;

public class ClientAppletTester extends Applet {

	/**
	 * 
	 */
	private static final long serialVersionUID = -6021707728535335857L;

	@Override
	public void init() {
		/*final Client cl = new Client(new String[]{"192.168.239.139"});
		Thread t = new Thread(cl);
		t.start();
		
		this.addKeyListener(new KeyListener(){

			@Override
			public void keyPressed(KeyEvent arg0) {
				try {
					System.out.println(cl.getMessage());
				} catch (IOException e) {
					e.printStackTrace();
				} catch (ClassNotFoundException e) {
					e.printStackTrace();
				}
			}

			@Override
			public void keyReleased(KeyEvent arg0) {
				// TODO Auto-generated method stub
				
			}

			@Override
			public void keyTyped(KeyEvent arg0) {
				// TODO Auto-generated method stub
				
			}
		});
		
		super.init();*/
	}
	
	
}
