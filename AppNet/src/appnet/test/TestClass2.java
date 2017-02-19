package appnet.test;

import java.io.Serializable;

public class TestClass2 implements Serializable
{
	/**
	 * 
	 */
	private static final long serialVersionUID = -6064629785747172656L;
	public float f;
	
	@Override
	public boolean equals(Object obj) {
		return ((TestClass2)obj).f == f;
	}
}
