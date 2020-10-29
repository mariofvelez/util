package utility;

import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.Polygon;
import java.awt.geom.AffineTransform;
import java.awt.image.BufferedImage;
import java.util.ArrayList;

public class ScreenManager {
	private float dx; //offset in x and y
	private float dy;
	private float t; //rotation
	private float sx; //scale of x and y
	private float sy;
	private ArrayList<ScreenButton> screenButtons = new ArrayList<ScreenButton>();
	private ArrayList<MapButton> mapButtons = new ArrayList<MapButton>();
	private ArrayList<ToggleButton> toggleButtons = new ArrayList<ToggleButton>();
	Color off;
	Color touched;
	Color clicked;
	boolean pdown;
	boolean down;
	
	public ScreenManager()
	{
		dx = 0;
		dy = 0;
		t = 0;
		sx = 1;
		sy = 1;
		setTheme("default");
		pdown = false;
		down = false;
	}
	public ScreenManager(float dx, float dy)
	{
		this.dx = dx;
		this.dy = dy;
	}
	public ScreenManager clone()
	{
		ScreenManager sm = new ScreenManager(dx, dy);
		sm.setScale(sx, sy);
		sm.setRotation(t);
		return sm;
	}
	public float[] center()
	{
		float[] topLeft = toScreen(0, 0);
		float[] bottomRight = toScreen(900, 600);
		
		float[] center = {(bottomRight[0] + topLeft[0])/2, (bottomRight[1] + topLeft[1])/2};
		return center;
	}
	public void setOffset(float dx, float dy)
	{
		this.dx = dx;
		this.dy = dy;
	}
	public void changeOffset(float dx, float dy)
	{
		this.dx += dx;
		this.dy += dy;
	}
	public float[] getOffset()
	{
		float[] offset = {dx, dy};
		return offset;
	}
	public void setRotation(float t)
	{
		this.t = t;
	}
	public void changeRotation(float t)
	{
		this.t += t;
	}
	public void setRotation(float t, float x, float y)
	{
		float[] original = toScreen(x, y);
		this.t = t;
		float[] New = toScreen(x, y);
		dx -= New[0] - original[0];
		dy -= New[1] - original[1];
	}
	public void changeRotation(float t, float x, float y)
	{
		float[] original = toScreen(x, y);
		this.t += t;
		float[] New = toScreen(x, y);
		dx -= New[0] - original[0];
		dy -= New[1] - original[1];
	}
	public float getRotation()
	{
		return t;
	}
	public void setScale(float sx, float sy)
	{
		this.sx = sx;
		this.sy = sy;
	}
	public void changeScale(float sx, float sy)
	{
		this.sx += sx;
		this.sy += sy;
	}
	public void multScale(float sx, float sy)
	{
		this.sx *= sx;
		this.sy *= sy;
	}
	public void setScale(float sx, float sy, float x, float y)
	{
		float[] original = toScreen(x, y);
		this.sx = sx;
		this.sy = sy;
		float[] New = toScreen(x, y);
		dx -= (New[0] - original[0]);
		dy -= (New[1] - original[1]);
	}
	public void changeScale(float sx, float sy, float x, float y) //zooms in and zooms out from the point (x, y)
	{
		float[] original = toScreen(x, y);
		this.sx += sx;
		this.sy += sy;
		float[] New = toScreen(x, y);
		dx -= (New[0] - original[0]);
		dy -= (New[1] - original[1]);
	}
	public void multScale(float sx, float sy, float x, float y) //zooms in and zooms out from the point (x, y)
	{
		float[] original = toScreen(x, y);
		this.sx *= sx;
		this.sy *= sy;
		float[] New = toScreen(x, y);
		dx -= (New[0] - original[0]);
		dy -= (New[1] - original[1]);
	}
	public float[] getScale()
	{
		return new float[] {sx, sy};
	}
	/**
	 * 
	 * @param x - the x position of the object to focus
	 * @param y - the y position of the object to focus
	 * @param t - the rotation to focus to
	 * @param fx - the x anchor to focus to on the screen
	 * @param fy - the y anchor to focus to on the screen
	 * @param easing - the amount of easing to focus with
	 * 				- put 1 for instant easing, put Integer.MAX_VALUE for virtually no easing
	 */
	public void focus(float x, float y, float t, float fx, float fy, int easing)
	{
		this.t += (-t - this.t)/easing;
		float[] sxy = toScreen(x, y);
		float dx = fx == Float.NaN? 0 : (fx-sxy[0])/easing;
		float dy = fy == Float.NaN? 0 : (fy-sxy[1])/easing;
		changeOffset(dx, dy);
	}
	public void focusX(float x, float fx, int easing)
	{
		float[] sxy = toScreen(x, 0);
		changeOffset((fx-sxy[0])/easing, 0);
	}
	public float[] toScreen(float x, float y) //takes (x, y) on the map ang gives (x1, y1) for the screen 
	{
		float[] xy = {x, y};
		if(t != 0)
		{
			//FIXME change Math for MathUtils
			float distance = (float) Math.hypot(xy[0], xy[1]);
			float theta = (float) Math.atan2(xy[1], xy[0]) + t;
			xy[0] = (float) Math.cos(theta)*distance;
			xy[1] = (float) Math.sin(theta)*distance;
		}
		xy[0] *= sx;
		xy[1] *= sy;
		
		xy[0] += dx;
		xy[1] += dy;
		return xy;
	}
	public void drawImageToScreen(Graphics g, Image img, float x, float y, float w, float h)
	{
		Graphics2D g2 = (Graphics2D) g;
		float[] xy = toScreen(x, y);
		int[] wh = {img.getWidth(null), img.getHeight(null)};
		AffineTransform tx = AffineTransform.getTranslateInstance(xy[0], xy[1]);
		tx.rotate(t);
		tx.scale(w/wh[0] * sx, h/wh[1] * sy);
		g2.drawImage(img, tx, null);
	}
	public void drawImageToScreen(Graphics g, Image img, float t, float x, float y, float w, float h)
	{
		Graphics2D g2 = (Graphics2D) g;
		float[] xy = toScreen(x, y);
		int[] wh = {img.getWidth(null), img.getHeight(null)};
		ScreenManager sm = this.clone();
		sm.setOffset(0, 0);
		float[] back = sm.toScreen(w, h);
		AffineTransform tx = AffineTransform.getTranslateInstance(xy[0] - back[0], xy[1] + back[1]);
		tx.rotate(this.t);
		tx.rotate(-t, back[0], -back[1]);
		tx.scale(w*2/wh[0] * sx, -h*2/wh[1] * sy);
		g2.drawImage(img, tx, null);
	}
	public float[] toMap(float x, float y) //takes (x1, y1) on the screen and gives (x, y) on the map
	{
		float[] xy = {x, y};
		
		xy[0] -= dx;
		xy[1] -= dy;
		
		xy[0] /= sx;
		xy[1] /= sy;
		
		if(t != 0)
		{
			//FIXME change Math for MathUtils
			float distance = (float) Math.hypot(xy[0], xy[1]);
			float theta = (float) Math.atan2(xy[1], xy[0]) - t;
			xy[0] = (float) Math.cos(theta)*distance;
			xy[1] = (float) Math.sin(theta)*distance;
		}
		
		return xy;
	}
	public void setTheme(Color off, Color touched, Color clicked)
	{
		this.off = off;
		this.touched = touched;
		this.clicked = clicked;
	}
	public void setTheme(String theme)
	{
		switch(theme)
		{
		case "default": 
			off = new Color(150, 150, 150);
			touched = new Color(100, 100, 100);
			clicked = new Color(50, 50, 50);
			break;
		case "cool":
			off = Color.blue;
			touched = Color.green;
			clicked = Color.magenta;
		}
	}
	public void addScreenButton(String tag, int x, int y, int w, int h)
	{
		screenButtons.add(new ScreenButton(tag, x, y, w, h));
	}
	public void removeAllScreenButtons()
	{
		screenButtons.removeAll(screenButtons);
	}
	public void addMapButton(float x, float y, float w, float h)
	{
		mapButtons.add(new MapButton(this, x, y, w, h));
	}
	public void addToggleButton(String tag, int x, int y, int w, int h)
	{
		toggleButtons.add(new ToggleButton(tag, x, y, w, h));
	}
	public void deselectToggleButtons()
	{
		for(ToggleButton i : toggleButtons)
			i.on = false;
	}
	public ScreenButton getSButton(String tag)
	{
		for(ScreenButton i : screenButtons)
		{
			if(i.tag.equals(tag))
				return i;
		}
		return null;
	}
	public ToggleButton getTButton(String tag)
	{
		for(ToggleButton i : toggleButtons)
		{
			if(i.tag.equals(tag))
				return i;
		}
		return null;
	}
	public void drawScreenButtons(Graphics g, int mx, int my, boolean down)
	{
		Graphics2D g2 = (Graphics2D)g;
		pdown = this.down;
		this.down = down;
		for(ScreenButton i : screenButtons)
		{
			if(i.touched(mx, my))
			{
				if(down)
				{
					i.down = true;
					i.draw(g2, clicked);
				}
				else
				{
					i.down = false;
					i.draw(g2, touched);
				}
			}
			else
			{
				i.down = false;
				i.draw(g2, off);
			}
		}
		for(ToggleButton i : toggleButtons)
		{
			if(i.touched(mx, my))
			{
				if(this.down && !pdown)
				{
					if(i.on)
						i.on = false;
					else
						i.on = true;
				}
			}
			if(i.on)
				i.draw(g2, clicked);
			else
			{
				if(i.touched(mx, my))
					i.draw(g2, touched);
				else
					i.draw(g2, off);
			}
		}
	}
	public void drawMapButtons(Graphics g, int mx, int my, boolean down)
	{
		Graphics2D g2 = (Graphics2D)g;
		for(MapButton i : mapButtons)
		{
			if(i.touched(this, mx, my))
			{
				if(down)
				{
					i.down = true;
					i.draw(this, g2, Color.magenta);
				}
				else
				{
					i.down = false;
					i.draw(this, g2, Color.green);
				}
			}
			else
			{
				i.down = false;
				i.draw(this, g2, Color.blue);
			}
		}
	}

}

class ScreenButton {
	int x;
	int y;
	int w;
	int h;
	String tag;
	boolean down;
	Font font = new Font("", 0, 30);
	public ScreenButton(String tag, int x, int y, int w, int h)
	{
		this.x = x;
		this.y = y;
		this.w = w;
		this.h = h;
		this.tag = tag;
	}
	public boolean touched(int mx, int my)
	{
		return (mx > x && mx < x + w && my > y && my < y + h);
	}
	public void draw(Graphics g, Color c)
	{
		Graphics2D g2 = (Graphics2D)g;
		g2.setColor(c);
		g2.fillRect(x, y, w, h);
		g2.setColor(Color.white);
		g2.drawRect(x, y, w, h);
		g2.setFont(font);
		g2.setColor(Color.black);
		g2.drawString(tag, x + (w/2) - (7*tag.length()), y + (h/2) + 15);
		
	}
	
}
class ToggleButton {
	String tag;
	int x;
	int y;
	int w;
	int h;
	boolean on;
	Font font = new Font("", 0, 30);

	public ToggleButton(String tag, int x, int y, int w, int h) {
		this.tag = tag;
		this.x = x;
		this.y = y;
		this.w = w;
		this.h = h;
		on = false;
		
	}
	public boolean touched(int mx, int my)
	{
		return (mx > x && mx < x + w && my > y && my < y + h);
	}
	public void draw(Graphics g, Color c)
	{
		Graphics2D g2 = (Graphics2D)g;
		g2.setColor(c);
		g2.fillRect(x, y, w, h);
		g2.setColor(Color.white);
		g2.drawRect(x, y, w, h);
		g2.setFont(font);
		g2.setColor(Color.black);
		g2.drawString(tag, x + (w/2) - (7*tag.length()), y + (h/2) + 15);
		
	}
	
}

class MapButton {
	float[] x = new float[4];
	float[] y = new float[4];
	boolean down;
	
	public MapButton(ScreenManager sm, float x, float y, float w, float h)
	{
		float[][]xy = {
				{x + w, y},
				{x, y},
				{x, y + h},
				{x + w, y + h}
		};
		for(int i = 0; i < 4; i++)
		{
			this.x[i] = xy[i][0];
			this.y[i] = xy[i][1];
		}
	}
	public boolean touched(ScreenManager sm, int mx, int my)
	{
		int[] x = new int[4];
		int[] y = new int[4];
		for(int i = 0; i < 4; i++)
		{
			float[] xy = sm.toScreen(this.x[i], this.y[i]);
			x[i] = (int) xy[0];
			y[i] = (int) xy[1];
		}
		Polygon p = new Polygon(x, y, 4);
		if(p.contains(mx, my))
			return true;
		return false;
	}
	public void draw(ScreenManager sm, Graphics g, Color c)
	{
		Graphics2D g2 = (Graphics2D)g;
		int[] x = new int[4];
		int[] y = new int[4];
		for(int i = 0; i < 4; i++)
		{
			float[] xy = sm.toScreen(this.x[i], this.y[i]);
			x[i] = (int) xy[0];
			y[i] = (int) xy[1];
		}
		g2.setColor(c);
		g2.fillPolygon(x, y, 4);
		g2.setColor(Color.white);
		g2.drawPolygon(x, y, 4);
	}
}
