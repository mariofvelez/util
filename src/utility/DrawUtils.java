package utility;

import java.awt.BasicStroke;
import java.awt.Canvas;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Point;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.image.BufferedImage;
import java.awt.image.DataBuffer;
import java.awt.image.Raster;
import java.awt.image.SampleModel;
import java.io.IOException;
import java.net.URL;

import javax.imageio.ImageIO;
import javax.swing.JFrame;

import machine_learning.Network;
import utility.math.Vec2d;

/**
 * 
 * @author Mario Velez
 *
 */
public class DrawUtils {
	public static float sigmoid(float x)
	{
		return 1 / (1+(float)Math.exp(-x));
	}
	public static void drawRoundConnection(Graphics2D g2, int lerps, int x, int y, int x1, int y1)
	{
		int dist = x1-x;
		int s1y = 0;
		for(int i = 0; i < lerps; i++)
		{
			float fraction = (float) i/lerps;
			int index = (int) lerp(x, x1, fraction);
			int sy = (int) (sigmoid(lerp(-5, 5, fraction)) * (y1-y));
			g2.drawLine(index + dist/20, sy + y, index, s1y + y);
			s1y = sy;
		}
		
	}
	/**
	 * Draws an image with texture mapping
	 * @param img - the image of the texture
	 * @param canvas - the image to draw on. The dimensions of this image should be the bounding box of the transform.
	 * @param transform - an array of 4 points [4][2] to draw the texture on. The first point is the top left and goes clockwise.
	 */
	public static void drawImage(BufferedImage img, BufferedImage canvas, float[][] transform)
	{
		float[] point = {0, 0};
		float step_x = 1f/img.getWidth();
		float step_y = 1f/img.getHeight();
		for(int x = 0; x < img.getWidth(); x++)
		{
			for(int y = 0; y < img.getHeight(); y++)
			{
//				float[] pointa = DrawUtils.fromPoint(transform, new float[] {x, y}, point[1], point[0]); //decimal value of image
//				int[] xy = {(int) (pointa[0]*img.getWidth()), (int) (pointa[1]*img.getHeight())};
//				if(xy[0] >= 0 && xy[0] < img.getWidth() && xy[1] >= 0 && xy[1] < img.getHeight())
//					canvas.setRGB(x, y, img.getRGB(xy[0], xy[1]));
				float[] pointa = getPoint(transform, point);
				int[] xy = {(int) (pointa[0]), (int) (pointa[1])};
				if(x >= 0 && x < canvas.getWidth() && y >= 0 && y < canvas.getHeight())
					canvas.setRGB(xy[0], xy[1], img.getRGB(x, y));
				point[1] += step_y;
			}
			point[0] += step_x;
			point[1] = 0;
		}
	}
	public static float[] getPoint(float[][] transform, float[] point)
	{	
		//find position lerped from top edge and bottom edge
		float x1 = DrawUtils.lerp(transform[0][0], transform[1][0], point[0]);
		float x2 = DrawUtils.lerp(transform[3][0], transform[2][0], point[0]);
		//find position lerped from left edge and right edge
		float y1 = DrawUtils.lerp(transform[0][1], transform[1][1], point[0]);
		float y2 = DrawUtils.lerp(transform[3][1], transform[2][1], point[0]);
		
		//x position is from 
		float x = lerp(x1, x2, point[1]);
		float y = lerp(y1, y2, point[1]);
		return new float[] {x, y};
	}
	public static float[] getPoint1(float[][] transform, float[] point)
	{
		Vec2d pointv = new Vec2d(point[0], point[1]);
		
		Vec2d left = Vec2d.subtract(new Vec2d(transform[3][0], transform[3][1]), new Vec2d(transform[0][0], transform[0][1])).leftNormal();
		Vec2d bottom = Vec2d.subtract(new Vec2d(transform[2][0], transform[2][1]), new Vec2d(transform[3][0], transform[3][1])).leftNormal();
		Vec2d right = Vec2d.subtract(new Vec2d(transform[1][0], transform[1][1]), new Vec2d(transform[2][0], transform[2][1])).leftNormal();
		Vec2d top = Vec2d.subtract(new Vec2d(transform[0][0], transform[0][1]), new Vec2d(transform[1][0], transform[1][1])).leftNormal();
		left.normalize();
		right.normalize();
		top.normalize();
		bottom.normalize();
		float x1 = left.dotProduct(pointv);
		float x2 = right.dotProduct(pointv);
		float y1 = top.dotProduct(pointv);
		float y2 = bottom.dotProduct(pointv);
		return new float[] {x1+x2 / 2, y1+y2 / 2};
	}
	public static float[] fromPoint(float[][] transform, float[] point, float scale_y, float scale_x)
	{
		float x1 = lerp2(transform[0][0], transform[1][0], point[0]); //decimal of lerped top x pos
		float x2 = lerp2(transform[3][0], transform[2][0], point[0]); //decimal of lerped x bottom pos
		
		float y1 = lerp2(transform[0][1], transform[3][1], point[1]); //decimal of lerped left y pos
		float y2 = lerp2(transform[1][1], transform[2][1], point[1]); //decimal of lerped right y pos
		
//		float ax = transform[1][0]-transform[0][0] / transform[2][0]-transform[3][0];
//		float ay = transform[1][1]-transform[0][1] / transform[2][1]-transform[3][1];
		
		float px = lerp(x1, x2, scale_y);
		float py = lerp(y1, y2, scale_x);
		
		return new float[] {px, py};
		
//		val on the image
//		float x1 = DrawUtils.lerp(transform[0][0], transform[1][0], scale_x);
//		float x2 = DrawUtils.lerp(transform[3][0], transform[2][0], scale_x);
//		float y1 = DrawUtils.lerp(transform[0][1], transform[1][1], scale_x);
//		float y2 = DrawUtils.lerp(transform[3][1], transform[2][1], scale_x);
//		
//		float x = lerp2(x1, x2, point[1]);
//		float y = lerp2(y1, y2, point[1]);
//		return new float[] {x, y};
	}
	public static int raster(BufferedImage img, float[][] transform, int[][] raster, float[] point)
	{
		Vec2d a = new Vec2d(transform[1][0]-transform[0][0], transform[1][1]-transform[0][1]);
		Vec2d b = new Vec2d(transform[2][0]-transform[1][0], transform[2][1]-transform[1][1]);
		Vec2d pa = new Vec2d(point[0]-transform[1][0], point[1]-transform[1][1]);
		Vec2d pb = new Vec2d(point[0]-transform[2][0], point[1]-transform[2][1]);
		a.normalize2();
		b.normalize2();
		Vec2d an = a.leftNormal();
		Vec2d bn = b.leftNormal();
		float aproj = a.dotProduct(pa);
		float bproj = b.dotProduct(pb);
		float anproj = an.dotProduct(pa);
		float bnproj = bn.dotProduct(pb);
		
		Vec2d ra = new Vec2d(raster[1][0]-raster[0][0], raster[1][1]-raster[0][1]);
		Vec2d rb = new Vec2d(raster[2][0]-raster[1][0], raster[2][1]-raster[1][1]);
		
		Vec2d rpa = new Vec2d(raster[0][0], raster[0][1]);
		rpa.add(Vec2d.mult(ra, aproj));
		rpa.add(Vec2d.mult(rpa, anproj));
		Vec2d rpb = new Vec2d(raster[1][0], raster[1][1]);
		rpb.add(Vec2d.mult(rb, bproj));
		rpb.add(Vec2d.mult(rpb, bnproj));
		
		Vec2d pos = Vec2d.avg(rpa, rpb);
		
		try {
			return img.getRGB((int) pos.x, (int) pos.y);
		} catch (Exception e) {
			return 0;
		}
		
	}
	/**
	 * 
	 * @param a - start distance
	 * @param b - finish distance
	 * @param scale - fraction between them from a
	 * @returns the resulting distance
	 */
	public static float lerp(float a, float b, float scale)
	{
		return a + (b-a)*scale;
	}
	/**
	 * inverse of this.lerp
	 */
	public static float lerp2(float a, float b, float val)
	{
		return (val-a)/(b-a);
	}
	/**
	 * 
	 * @param start - first anchor value
	 * @param end - second anchor value
	 * @param val - value between anchors
	 * @param length - length between the anchors
	 * @return val in terms of length
	 */
	public static float lerp(float start, float end, float val, float length)
	{
		return 0;
	}
	public static void main(String[] args) throws IOException {
		
		URL url = DrawUtils.class.getResource("mr_electric_256x256.png");
		BufferedImage img = ImageIO.read(url);
		
		float[][] transform = {
				{1, 0},
				{45, 3},
				{11, 30},
				{0, 15}
		};
		int[][] texture_coord = {
				{20, img.getHeight()-20},
				{img.getWidth()/2, 20},
				{img.getWidth()-20, img.getHeight()-20}
		};
		
		float scale = 5f;
		for(int i = 0; i < transform.length; i++)
		{
			transform[i][0] *= scale;
			transform[i][1] *= scale;
		}
		
		BufferedImage texture = new BufferedImage(Utils.max(Utils.xPoints(transform)), Utils.max(Utils.yPoints(transform)), BufferedImage.TYPE_INT_ARGB);
		
		Long t1 = System.currentTimeMillis();
		
		
		
		System.out.println(System.currentTimeMillis()-t1);
		
		JFrame frame = new JFrame("Texture Mapping Test");
		frame.setSize(600, 300);
		frame.setVisible(true);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		
		frame.add(new Canvas()
		{
			/**
			 * 
			 */
			private static final long serialVersionUID = 5426356829940365026L;
			private int px = 0;
			private int py = 0;
			int hold = -1;
			int hold1 = -1;
			int imgx = 300;
			{
				this.addMouseMotionListener(new MouseAdapter()
				{
					public void mouseDragged(MouseEvent e)
					{
						if(hold != -1)
						{
							transform[hold][0] += e.getX() - px;
							transform[hold][1] += e.getY() - py;
						}
						if(hold1 != -1)
						{
							texture_coord[hold1][0] += e.getX() - px;
							texture_coord[hold1][1] += e.getY() - py;
						}
						px = e.getX();
						py = e.getY();
						repaint();
					}
				});
				this.addMouseListener(new MouseAdapter()
				{
					public void mousePressed(MouseEvent e)
					{
						for(int i = 0; i < transform.length; i++)
						{
							if(Math.hypot(transform[i][0]-e.getX(), transform[i][1]-e.getY()) < 5)
							{
								hold = i;
								break;
							}
						}
						for(int i = 0; i < texture_coord.length; i++)
						{
							if(Math.hypot((texture_coord[i][0]+imgx) - e.getX(), texture_coord[i][1] - e.getY()) < 5)
							{
								hold1 = i;
								break;
							}
						}
						px = e.getX();
						py = e.getY();
					}
					public void mouseReleased(MouseEvent e)
					{
						hold = -1;
						hold1 = -1;
					}
				});
			}
			public void paint(Graphics g)
			{
				texture.createGraphics().clearRect(0, 0, this.getWidth(), this.getHeight());
//				float[] point = {0, 0};
//				float step_x = 1f/texture.getWidth();
//				float step_y = 1f/texture.getHeight();
//				for(int x = 0; x < texture.getWidth(); x++)
//				{
//					for(int y = 0; y < texture.getHeight(); y++)
//					{
//						float[] pointa = DrawUtils.fromPoint(transform, new float[] {x, y}, point[1], point[0]); //decimal value of image
//						int[] xy = {(int) (pointa[0]*img.getWidth()), (int) (pointa[1]*img.getHeight())};
//						if(xy[0] >= 0 && xy[0] < img.getWidth() && xy[1] >= 0 && xy[1] < img.getHeight())
//							texture.setRGB(x, y, img.getRGB(xy[0], xy[1]));
////						float[] pointa = getPoint(transform, point);
////						int[] xy = {(int) (pointa[0]), (int) (pointa[1])};
////						if(x >= 0 && x < texture.getWidth() && y >= 0 && y < texture.getHeight())
////							texture.setRGB(xy[0], xy[1], img.getRGB(x, y));
//						point[1] += step_y;
//					}
//					point[0] += step_x;
//					point[1] = 0;
//				}
				for(int x = 0; x < texture.getWidth(); x++)
				{
					for(int y = 0; y < texture.getHeight(); y++)
					{
						texture.setRGB(x, y, raster(img, transform, texture_coord, new float[] {x, y}));
					}
				}
				Graphics2D g2 = (Graphics2D)g;
				g2.drawImage(img, imgx, 0, null);
//				drawImage(img, texture, transform);
				g2.drawImage(texture, 0, 0, null);
				g2.setColor(Color.RED);
				g2.drawPolygon(Utils.xPoints(transform), Utils.yPoints(transform), transform.length);
				float[][] text_coord = new float[texture_coord.length][texture_coord[0].length];
				for(int i = 0; i < text_coord.length; i++)
				{
					text_coord[i][0] = texture_coord[i][0] + imgx;
					text_coord[i][1] = texture_coord[i][1];
				}
				g2.setColor(Color.GREEN);
				g2.setStroke(new BasicStroke(2));
				g2.drawPolygon(Utils.xPoints(text_coord), Utils.yPoints(text_coord), text_coord.length);
			}
		});
	}

}
