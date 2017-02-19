package litd.server;

import java.net.InetAddress;
import java.net.NetworkInterface;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.Enumeration;
import litd.entity.Player;

public class LoginServer implements Runnable {
	class AcceptorThread implements Runnable
	{
		ServerSocket sock;
		
		public AcceptorThread(ServerSocket sock)
		{
			this.sock = sock;
		}
		
		@Override
		public void run()
		{
			Log.log("Server Acceptor Listening on " + sock.getLocalSocketAddress() + " : " + sock.getLocalPort());
			
			while(true)
			{
				try
				{
					Socket s = sock.accept();
					Log.log("Player from " + s.getRemoteSocketAddress() + " connected...");
					
					LevelServer.levelServers[0].addEntity(new Player(s));
				} catch(Exception e)
				{
					Log.log("Server Acceptor Warning: " + e.toString());
				}
			}
		}
		
		public void stop()
		{
			try
			{
				sock.close();
			} catch(Exception e)
			{
				Log.log("Server Shutdown Error: " + e.toString());
			}
		}
	}
	
	@Override
	public void run() {
		try {
			Log.log("Server: Thread launching...");
			
			Enumeration<NetworkInterface> nie = NetworkInterface.getNetworkInterfaces();
			
			ArrayList<AcceptorThread> acceptorThreads = new ArrayList<AcceptorThread>();
			
			while(nie.hasMoreElements())
			{
				NetworkInterface ni = nie.nextElement();
				
				Enumeration<InetAddress> addrs = ni.getInetAddresses();
				
				while(addrs.hasMoreElements())
				{
					InetAddress addr = addrs.nextElement();
					AcceptorThread at = new AcceptorThread(new ServerSocket(17998, 5, addr));
					Thread subThread = new Thread(at, "Server: Acceptor Thread " + addr.toString());
					subThread.start();
					acceptorThreads.add(at);
				}
			}
		} catch(Exception e)
		{
			// this basically means the Thread.sleep operation was interrupted... which should never happen
			Log.log("Server: Critical Error: " + e.toString());
		}
	}
}
