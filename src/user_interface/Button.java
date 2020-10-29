package user_interface;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics2D;
import java.awt.font.FontRenderContext;
import java.awt.font.GlyphVector;
import java.awt.geom.AffineTransform;
import java.lang.reflect.Method;

public class Button {
	int[][] pos;
	int[] dimensions;
	
	String tag;
	AffineTransform at;
	GlyphVector gv;
	Font font; //TODO - implement a way to set a font
	static final BasicStroke border = new BasicStroke(3);
	
	Method path;
	UIScreen screen;
	Object methodReference;
	
	/**
	 * 
	 * @param tag - the tag it shows on screen
	 * @param pos - a 2x2 array, the first array should contain the top left x and y coordinates,
	 * and the second array should contain the bottom left x and y coordinates
	 * @param path - the method it should invoke when pressed
	 * @param methodReference - the reference object from which the path method is called
	 */
	public Button(String tag, int[][] pos, Method path, Object methodReference)
	{
		this.tag = tag;
		this.pos = pos;
		this.path = path;
		
		dimensions = new int[2];
		for(int i = 0; i < dimensions.length; i++)
			dimensions[i] = pos[1][i] - pos[0][i];
		
		font = new Font("", 0, dimensions[0]/4);
		
		at = new AffineTransform();
		at.setToTranslation(-50, 100); //FIXME - align the text
		at.rotate(1);
		FontRenderContext frc = new FontRenderContext(at, true, false);
		gv = font.createGlyphVector(frc, tag);
	}
	
	public void draw(Graphics2D g2)
	{
		//TODO - implement a theme for different colors
		g2.setColor(Color.DARK_GRAY);
		g2.fillRect(pos[0][0], pos[0][1], dimensions[0], dimensions[1]);
		if(contains(new int[] {Field.mousex, Field.mousey}))
			g2.setColor(Color.ORANGE);
		else
			g2.setColor(Color.GREEN);
		g2.setStroke(border);
		g2.drawRect(pos[0][0], pos[0][1], dimensions[0], dimensions[1]);
		g2.drawGlyphVector(gv, pos[0][0] + dimensions[0]/4, pos[0][1] + dimensions[1]/2);
	}
	
	public void select(Object... args)
	{
		try {
			path.invoke(methodReference, args);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	public boolean contains(int[] xy)
	{
		return (xy[0] > pos[0][0] && xy[0] < pos[1][0] &&
				xy[1] > pos[0][1] && xy[1] < pos[1][1]);
	}

}
