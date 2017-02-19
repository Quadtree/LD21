package appnet;

import java.io.IOException;
import java.io.OutputStream;
import java.nio.ByteBuffer;
import java.util.Queue;
import java.util.concurrent.ArrayBlockingQueue;

public class MessageOutput {
	
	OutputStream out;
	
	Queue<ByteBuffer> sendQueue = new ArrayBlockingQueue<ByteBuffer>(65535);
	
	public MessageOutput(OutputStream out)
	{
		this.out = out;
	}
	
	/**
	 * This merely QUEUES the message, it does not actually send it
	 * That is done by the flush() method
	 * @param message
	 * @throws IOException
	 */
	
	public void write(byte[] message) throws IOException
	{
		ByteBuffer toSend = ByteBuffer.allocate(message.length + 4);
		toSend.putInt(message.length);
		toSend.put(message);
		
		sendQueue.add(toSend);
	}
	
	/**
	 * This can be called by a different thread than write, concurrently with it
	 * This allows sending to be actually performed on a different thread, providing some safety from blocking
	 * @throws IOException 
	 */
	
	public void flush() throws IOException
	{
		ByteBuffer nextSend = sendQueue.poll();
		
		while(nextSend != null)
		{
			out.write(nextSend.array());
			nextSend = sendQueue.poll();
		}
	}
}
