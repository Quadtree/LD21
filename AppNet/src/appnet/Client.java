package appnet;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.net.Socket;
import appnet.ObjectMessageInput;
import appnet.ObjectMessageOutput;

public class Client implements Runnable {
	private static final String PRINT_TAG = "CThread: ";

	String address;
	
	boolean run = true;
	
	boolean connected = false;
	
	Socket sock;
	
	ObjectMessageInput in;
	ObjectMessageOutput out;
	
	long lastHeartbeat = 0;
	
	public Client(String address)
	{
		this.address = address;
	}
	
	@Override
	public void run() {
		System.out.println(PRINT_TAG + "Launching...");
			
			try {
				System.out.println(PRINT_TAG + "Attempting to connect to " + address + ":" + 17998);
				sock = new Socket();
				sock.connect(new InetSocketAddress(address, 17998), 10000);
				
				in = new ObjectMessageInput(sock.getInputStream());
				out = new ObjectMessageOutput(sock.getOutputStream());
				
				while(run)
				{
					if(System.currentTimeMillis() > lastHeartbeat + 5000)
					{
						lastHeartbeat = System.currentTimeMillis();
						
						sendMessage(new Heartbeat());
					}
					
					Thread.sleep(20);
					connected = true;
				}
			} catch(Exception e)
			{
				System.out.println(PRINT_TAG + "Connection Lost: " + e.toString());
				connected = false;
			}
		
		System.out.println(PRINT_TAG + "Shutting down...");
	}
	
	public boolean isConnected() {
		return connected;
	}
	
	
	public Object getMessage() throws IOException, ClassNotFoundException
	{
		if(in.available())
			return in.read();
		
		return null;
	}

	public void sendMessage(Object o)
	{
		try {
			out.write(o);
			out.flush();
		} catch(Exception e)
		{
			System.out.println(PRINT_TAG + "Serialization Error: " + e.toString());
		}
	}
	
	public void stop()
	{
		try {
			run = false;
			sock.close();
		} catch(Exception e)
		{
			System.out.println(PRINT_TAG + "Shutdown Error: " + e.toString());
			connected = false;
		}
	}
}

	
