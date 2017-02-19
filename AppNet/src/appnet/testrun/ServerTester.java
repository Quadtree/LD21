package appnet.testrun;

import appnet.Server;

public class ServerTester extends Server{

	public static void main(String[] args)
	{
		ServerTester st = new ServerTester();
		Thread t = new Thread(st);
		t.start();
	}
	
	@Override
	protected void messageReceived(ServerClient client, Object o) {
		
		sendToClient(client, "ServerTester hears you!");
		
		super.messageReceived(client, o);
	}
}
