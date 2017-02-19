package sgf;

import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.geom.AffineTransform;

public class RenderImageOperation extends RenderOperation{

	float x,y;
	float rot;
	float w,h;
	
	String imageName;
	Image img;
	
	public RenderImageOperation(float x, float y, float rot, float w, float h,
			String imageName, Image img) {
		super();
		this.x = x;
		this.y = y;
		this.rot = rot;
		this.w = w;
		this.h = h;
		this.imageName = imageName;
		this.img = img;
	}

	@Override
	public void execute(Graphics2D g) {
		
		if(img == null) img = SGF.getInstance().getImage(imageName);
		
		AffineTransform at;
		
		at = new AffineTransform();
		
		at.translate(x, y);
		if(Math.abs(rot) > 0.001f)
			at.rotate(rot);
		
		if(w > 0)
			at.scale(w / img.getWidth(null), h / img.getHeight(null));
		else
			at.scale(-w, -h);
		
		at.translate(-img.getWidth(null) / 2, -img.getHeight(null) / 2);
		
		g.drawImage(img, at, null);
	}

}
