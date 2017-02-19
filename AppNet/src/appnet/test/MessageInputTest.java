package appnet.test;

import static org.junit.Assert.*;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;

import appnet.MessageInput;
import appnet.MessageOutput;

import org.junit.Test;

public class MessageInputTest {

	@Test
	public void testRead() throws IOException {
		
		byte[] m1 = {55,22,111};
		byte[] m2 = {12,-15,77,-5};
		byte[] m3 = {-100};
		
		ByteArrayOutputStream baos = new ByteArrayOutputStream();
		
		MessageOutput mo = new MessageOutput(baos);
		
		mo.write(m1);
		mo.write(m2);
		mo.write(m3);
		
		mo.flush();
		
		ByteArrayInputStream bais = new ByteArrayInputStream(baos.toByteArray());
		
		MessageInput mi = new MessageInput(bais);
		
		assertArrayEquals(m1, mi.read());
		assertArrayEquals(m2, mi.read());
		assertArrayEquals(m3, mi.read());
	}

}
