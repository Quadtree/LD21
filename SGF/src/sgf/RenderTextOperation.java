package sgf;

import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics2D;
import java.awt.geom.Point2D;

public class RenderTextOperation extends RenderOperation{

	int cr,cg,cb;
	
	String text;
	
	int fontSize;
	
	float x,y;
	
	public RenderTextOperation(int cr, int cg, int cb, String text,
			int fontSize, float x, float y) {
		super();
		this.cr = cr;
		this.cg = cg;
		this.cb = cb;
		this.text = text;
		this.fontSize = fontSize;
		this.x = x;
		this.y = y;
	}

	@Override
	public void execute(Graphics2D g) {
		Point2D pt = new Point2D.Float(x,y);
		
		g.setColor(new Color(cr,cg,cb));
		g.setFont(new Font("Consolas", 0, fontSize));
		g.drawString(text, (float)pt.getX(), (float)pt.getY());
	}

}
