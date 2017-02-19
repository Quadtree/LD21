package litd.server;

import java.io.IOException;

import litd.server.itemfactory.ItemFactory;
import litd.server.monsterfactory.MonsterFactory;

public class Main {
	
	public static int maxCarvings = 10;

	/**
	 * @param args
	 * @throws IOException 
	 * @throws ClassNotFoundException 
	 */
	public static void main(String[] args) throws IOException, ClassNotFoundException {
		
		if(args.length > 0) maxCarvings = Integer.parseInt(args[0]);
		
		new Log();
		new ItemFactory();
		new MonsterFactory();
		
		Log.log("Server starting. Max carvings set to " + maxCarvings);
		
		for(int i=0;i<LevelServer.levelServers.length;i++)
		{
			LevelServer.levelServers[i] = new LevelServer(i);
			new Thread(LevelServer.levelServers[i]).start();
		}
		
		new Thread(new LoginServer()).start();
	}

}
