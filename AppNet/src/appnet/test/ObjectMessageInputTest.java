package appnet.test;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import appnet.ObjectMessageInput;
import appnet.ObjectMessageOutput;

import org.junit.Test;
import static org.junit.Assert.*;

public class ObjectMessageInputTest {
	@Test
	public void testRead() throws IOException, ClassNotFoundException
	{
		TestClass1 t1 = new TestClass1();
		t1.x = 5;
		t1.y = 22;
		
		TestClass2 t2 = new TestClass2();
		t2.f = 4.7f;
		
		ByteArrayOutputStream baos = new ByteArrayOutputStream();
		
		ObjectMessageOutput mo = new ObjectMessageOutput(baos);
		
		mo.write(t1);
		mo.write(t2);
		mo.flush();
		
		ByteArrayInputStream bais = new ByteArrayInputStream(baos.toByteArray());
		
		ObjectMessageInput mi = new ObjectMessageInput(bais);
		
		assertEquals(t1, mi.read());
		assertEquals(t2, mi.read());
	}
}
