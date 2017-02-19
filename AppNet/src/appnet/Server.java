package appnet;

import java.io.IOException;
import java.net.InetAddress;
import java.net.NetworkInterface;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.SocketException;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.concurrent.CopyOnWriteArrayList;

public class Server implements Runnable {

	volatile boolean run = true;
	
	protected class ServerClient
	{
		public ServerClient(Socket sock) throws IOException {
			super();
			this.sock = sock;
			this.in = new ObjectMessageInput(sock.getInputStream());
			this.out = new ObjectMessageOutput(sock.getOutputStream());
		}
		
		final public Socket sock;
		final public ObjectMessageInput in;
		final public ObjectMessageOutput out;
	}
	
	class ServerTransmitter implements Runnable
	{
		@Override
		public void run() {
			while(run)
			{
				// this is needed so if we get an exception, we can speculate about who caused it
				ServerClient lastSC = null;
				
				try {
					for(ServerClient sc : clients)
					{
						lastSC = sc;
						sc.out.flush();
					}
					
					Thread.sleep(20);
				} catch(Exception e){
					System.out.println("Server: " + e.toString() + " dropping client " + lastSC.sock.getRemoteSocketAddress());
					clients.remove(lastSC);
				}
			}
		}
		
	}
	
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
			System.out.println("Server Acceptor Listening on " + sock.getLocalSocketAddress() + " : " + sock.getLocalPort());
			
			try
			{
				while(run)
				{
					clients.add(new ServerClient(sock.accept()));
					System.out.println("*** Client List ***");
					
					for(ServerClient sc : clients)
					{
						System.out.println(sc.sock.getRemoteSocketAddress());
					}
					
					System.out.println("");
				}
			} catch(Exception e)
			{
				System.out.println("Server Acceptor Warning: " + e.toString());
			}
		}
		
		public void stop()
		{
			try
			{
				sock.close();
			} catch(Exception e)
			{
				System.out.println("Server Shutdown Error: " + e.toString());
			}
		}
	}
	
	final CopyOnWriteArrayList<ServerClient> clients = new CopyOnWriteArrayList<ServerClient>();
	
	@Override
	public void run() {
		try {
			System.out.println("Server: Thread launching...");
			
			Enumeration<NetworkInterface> nie = NetworkInterface.getNetworkInterfaces();
			
			ArrayList<AcceptorThread> acceptorThreads = new ArrayList<AcceptorThread>();
			
			while(nie.hasMoreElements())
			{
				NetworkInterface ni = nie.nextElement();
				
				Enumeration<InetAddress> addrs = ni.getInetAddresses();
				
				while(addrs.hasMoreElements())
				{
					InetAddress addr = addrs.nextElement();
					//System.out.println(addr);
					AcceptorThread at = new AcceptorThread(new ServerSocket(17998, 5, addr));
					Thread subThread = new Thread(at, "Server: Acceptor Thread " + addr.toString());
					subThread.start();
					acceptorThreads.add(at);
				}
			}
			
			Thread transmitterThread = new Thread(new ServerTransmitter());
			transmitterThread.start();
		
			while(run)
			{
				// this is needed so if we get an exception, we can speculate about who caused it
				ServerClient lastSC = null;
				
				try {
					for(ServerClient sc : clients)
					{
						lastSC = sc;
						while(sc.in.available())
						{
							messageReceived(sc, sc.in.read());
						}
					}
				} catch(SocketException e)
				{
					System.out.println("Server: " + e.toString() + " dropping client " + lastSC.sock.getRemoteSocketAddress());
					clients.remove(lastSC);
				}
					
				Thread.sleep(20);
			}
			
			for(AcceptorThread at : acceptorThreads)
				at.stop();
			
		} catch(Exception e)
		{
			// this basically means the Thread.sleep operation was interrupted... which should never happen
			System.out.println("Server: Critical Error: " + e.toString());
		}
		
		System.out.println("Server: Thread shutting down...");
	}
	
	protected void messageReceived(ServerClient client, Object o)
	{
	}
	
	protected void sendToClient(ServerClient client, Object o)
	{
		try {
			client.out.write(o);
		} catch(Exception e)
		{
			System.out.println("Server: " + e.toString() + " dropping client " + client.sock.getRemoteSocketAddress());
			clients.remove(client);
		}
	}
	
	protected void sendToAll(Object o)
	{
		// this is needed so if we get an exception, we can speculate about who caused it
		ServerClient lastSC = null;
		
		try {
			for(ServerClient sc : clients)
			{
				lastSC = sc;
				sc.out.write(o);
			}
		} catch(Exception e)
		{
			System.out.println("Server: " + e.toString() + " dropping client " + lastSC.sock.getRemoteSocketAddress());
			clients.remove(lastSC);
		}
	}

	public void stop() {
		run = false;
	}

}
