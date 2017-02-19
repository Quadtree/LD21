package appnet;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.ObjectInputStream;

public class ObjectMessageInput {
	
	MessageInput min;
	
	public ObjectMessageInput(InputStream in)
	{
		min = new MessageInput(in);
	}
	
	public boolean available() throws IOException
	{
		return min.available();
	}
	
	public Object read() throws IOException, ClassNotFoundException
	{
		ObjectInputStream ois = new ObjectInputStream(new ByteArrayInputStream(min.read()));
		
		return ois.readObject();
	}
}
