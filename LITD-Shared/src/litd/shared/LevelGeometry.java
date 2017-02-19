package litd.shared;

import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import javax.imageio.ImageIO;

public class LevelGeometry implements Serializable{
	
	/**
	 * 
	 */
	private static final long serialVersionUID = -9011262160070080457L;
	
	public static final byte TRANSITION_0 = 0;
	public static final byte TRANSITION_1 = 0;

	int width, height;
	
	byte[] geom;
	
	public class TransitionInfo implements Serializable
	{
		/**
		 * 
		 */
		private static final long serialVersionUID = 2780765157396363112L;
		
		public byte dest;
		public int x,y;
		
		public TransitionInfo(byte dest, int x, int y) {
			super();
			this.dest = dest;
			this.x = x;
			this.y = y;
		}
	}
	
	public List<TransitionInfo> transitions = new ArrayList<TransitionInfo>();
	
	public LevelGeometry(File file) throws IOException
	{
		System.out.println("Loading level geometry from file " + file.getName());
		BufferedImage buf = ImageIO.read(file);
		
		width = buf.getWidth();
		height = buf.getHeight();
		
		geom = new byte[buf.getWidth() * buf.getHeight()];
		
		for(int y=0;y<height;y++)
		{
			for(int x=0;x<height;x++)
			{
				//System.out.println("D " + buf.getRGB(x, y));
				
				int rgb = buf.getRGB(x, y);
				
				if(rgb == -16777216)
					geom[x + y*width] = 1;
				else if(rgb == -1)
					geom[x + y*width] = 0;
				else if(rgb == -16777088)
					transitions.add(new TransitionInfo((byte) 0, x,y));
				else if(rgb == -16777087)
					transitions.add(new TransitionInfo((byte) 1, x,y));
				else if(rgb == -16777086)
					transitions.add(new TransitionInfo((byte) 2, x,y));
				else if(rgb == -16777085)
					transitions.add(new TransitionInfo((byte) 3, x,y));
				else if(rgb == -16777084)
					transitions.add(new TransitionInfo((byte) 4, x,y));
				else if(rgb == -16777083)
					transitions.add(new TransitionInfo((byte) 5, x,y));
				else if(rgb == -16777082)
					transitions.add(new TransitionInfo((byte) 6, x,y));
				else if(rgb == -16777081)
					transitions.add(new TransitionInfo((byte) 7, x,y));
				else if(rgb == -16777080)
					transitions.add(new TransitionInfo((byte) 8, x,y));
				else if(rgb == -16777079)
					transitions.add(new TransitionInfo((byte) 9, x,y));
				else
					System.out.println(x + " " + y + " = " + rgb);
			}
		}
	}
	
	public boolean isWall(int x, int y)
	{
		if(x < 0 || y < 0 || x >= width || y >= height) return true;
		return geom[x + y*width] == 1;
	}
}
