package appnet;

import java.io.IOException;
import java.io.InputStream;
import java.nio.ByteBuffer;
import java.util.ArrayList;

public class MessageInput {
	
	ByteBuffer inBuffer;
	boolean bodyReceive = false;
	
	ArrayList<ByteBuffer> messages = new ArrayList<ByteBuffer>();
	
	InputStream in;
	
	public MessageInput(InputStream in)
	{
		this.in = in;
	}
	
	public boolean available() throws IOException
	{
		pump();
		
		return messages.size() > 0;
	}
	
	protected void pump() throws IOException
	{
		while(in.available() > 0)
		{
			if(inBuffer == null) inBuffer = ByteBuffer.allocate(4);
			
			inBuffer.put((byte)in.read());
			
			if(inBuffer.remaining() == 0)
			{
				// if the buffer is full and we haven't gotten the message length yet
				if(!bodyReceive)
				{
					inBuffer = ByteBuffer.allocate(inBuffer.getInt(0));
					bodyReceive = true;
				} else {
					messages.add(inBuffer);
					inBuffer = null;
					bodyReceive = false;
				}
			}
		}
	}
	
	public byte[] read() throws IOException
	{
		pump();
		
		if(messages.size() > 0)
		{
			ByteBuffer top = messages.get(0);
			messages.remove(0);
			return top.array();
		}
		
		return null;
	}
}
