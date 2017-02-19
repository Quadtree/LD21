package appnet;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.ObjectOutputStream;
import java.io.OutputStream;

public class ObjectMessageOutput {
	
	MessageOutput mout;
	
	public ObjectMessageOutput(OutputStream out)
	{
		mout = new MessageOutput(out);
	}
	
	public void write(Object o) throws IOException
	{
		ByteArrayOutputStream outBuf = new ByteArrayOutputStream();
		
		ObjectOutputStream osr = new ObjectOutputStream(outBuf);
		
		osr.writeObject(o);
		osr.flush();
		
		mout.write(outBuf.toByteArray());
	}
	
	public void flush() throws IOException
	{
		mout.flush();
	}
}
