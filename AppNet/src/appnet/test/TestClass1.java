package appnet.test;

import java.io.Serializable;

public class TestClass1 implements Serializable
{
	/**
	 * 
	 */
	private static final long serialVersionUID = -3011810015885209862L;
	public int x;
	public int y;
	
	@Override
	public boolean equals(Object obj) {
		return ((TestClass1)obj).x == x && ((TestClass1)obj).y == y;
	}
}