package user_interface;

import java.awt.Canvas;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.DisplayMode;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.GraphicsDevice;
import java.awt.GraphicsEnvironment;
import java.awt.Toolkit;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;
import java.awt.event.MouseWheelEvent;
import java.awt.event.MouseWheelListener;
import java.awt.image.BufferStrategy;
import java.util.ArrayList;

import java.lang.System;

/**
 * 
 * @author Mario Velez
 *
 */
public class Field extends Canvas
		implements KeyListener, MouseMotionListener, MouseListener, MouseWheelListener,
				   Runnable {
	/**
	 * 
	 */
	private static final long serialVersionUID = -796167392411348854L;
	Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
	
	public static ArrayList<Graphics> bufferGraphics;
	public static ArrayList<BufferStrategy> bufferStrategies;
	
	//private Graphics bufferGraphics; // graphics for backbuffer
	//private BufferStrategy bufferStrategy;
	
	public static int mousex = 0; // mouse values
	public static int mousey = 0;

	Font font = new Font("Guardians Regular", 0, 10); // font
	Font font1 = new Font("Airstrike Regular", 0, 80);
	Font font2 = new Font("Airstrike Regular", 0, 60);
	Font font3 = new Font("Airstrike Regular", 0, 30);
	Font font4 = new Font("Airstrike Regular", 0, 15);

	public static ArrayList<Integer> keysDown; // holds all the keys being held down
	boolean leftClick;

	private Thread thread;

	public static boolean running;
	public static int runTime;
	public static float seconds;
	public static int refreshTime;
	
	public static int[] anchor = new  int[2];
	public static boolean dragging;
	
	public Field(Dimension size) throws Exception {
		
		GraphicsEnvironment ge = GraphicsEnvironment.getLocalGraphicsEnvironment();
		GraphicsDevice gd = ge.getDefaultScreenDevice();
		DisplayMode dm = gd.getDisplayMode();
		
		int refreshRate = dm.getRefreshRate();
		if(refreshRate == DisplayMode.REFRESH_RATE_UNKNOWN)
			System.out.println("Unknown refresh rate");
		
		this.setPreferredSize(size);
		this.addKeyListener(this);
		this.addMouseListener(this);
		this.addMouseMotionListener(this);
		this.addMouseWheelListener(this);
		
		bufferGraphics = new ArrayList<Graphics>();
		bufferStrategies = new ArrayList<BufferStrategy>();

		this.thread = new Thread(this);
		running = true;
		runTime = 0;
		seconds = 0;
		refreshTime = (int) (1f/refreshRate * 1000);

		keysDown = new ArrayList<Integer>();
		
	}

	public void paint(Graphics g) {
//		for(int i = 0; i < numGraphics; i++)
//		if (bufferStrategy == null) {
//			this.createBufferStrategy(1);
//			bufferStrategy = this.getBufferStrategy();
//			bufferGraphics = bufferStrategy.getDrawGraphics();
//
//			this.thread.start();
//		}
	}

	@Override
	public void run() {
		// what runs when editor is running
		while (running) {
			long t1 = System.currentTimeMillis();
			DoLogic();
			Draw();

			DrawBackbufferToScreen();

			Thread.currentThread();
			try {
				Thread.sleep(refreshTime);
			} catch (Exception e) {
				e.printStackTrace();
			}
			long t2 = System.currentTimeMillis();
			
			if(t2 - t1 > 16)
			{
				if(refreshTime > 0)
					refreshTime --;
			}
			else
				refreshTime ++;
			
			seconds += refreshTime/1000f;
			
			//System.out.println(t2 - t1);
			

		}
	}

	public void DrawBackbufferToScreen() {
		for(int i = 0; i < bufferStrategies.size(); i++)
		{
			bufferStrategies.get(i).show();
			System.out.println("graphics shown to screen");
		}

		Toolkit.getDefaultToolkit().sync();
	}

	public void DoLogic() {
		
		runTime++;
	}

	public void Draw() // titleScreen
	{
		// clears the backbuffer
		if(Window.uim.setScreen)
			Window.uim.startSetScreen();
		System.out.println("graphics: " + bufferGraphics.size() + ", strategies: " + bufferStrategies.size());
		for(int i = 0; i < bufferGraphics.size(); i++)
		{
			//bufferGraphics.set(i, bufferStrategies.get(0).getDrawGraphics());
			try {
				bufferGraphics.get(i).clearRect(0, 0, this.getWidth(), this.getHeight());
				Window.uim.draw();
				Graphics2D g2 = (Graphics2D) bufferGraphics.get(i);
				
				g2.setFont(font);
				g2.setColor(Color.BLACK);
				
	
			} catch (Exception e) {
				e.printStackTrace();
			} finally {
				bufferGraphics.get(i).dispose();
			}
		}
	}

	@Override
	public void keyPressed(KeyEvent e) {
		if (!keysDown.contains(e.getKeyCode()) && e.getKeyCode() != 86)
			keysDown.add(new Integer(e.getKeyCode()));
		
	}

	@Override
	public void keyReleased(KeyEvent e) {
		keysDown.remove(new Integer(e.getKeyCode()));
		
	}

	@Override
	public void keyTyped(KeyEvent e) {

	}

	public void Sleep(int time) {
		try {
			Thread.sleep(time);
		} catch (Exception e) {
			e.printStackTrace();
		}

	}

	@Override
	public void mouseClicked(MouseEvent e) {
		//addShape = true;
//		float[] pos = {e.getX(), e.getY()};
//		pos = sm.toMap(pos[0], pos[1]);
//		Material mat = new Material(1.0f, 0.5f, 0.1f);
//		Body cube = new Body(pos[0], pos[1], true, mat);
//		cube.createBoxShape(new Vec2d(0.5f, 0.5f));
//		cm.addBody(cube);
	}

	@Override
	public void mouseEntered(MouseEvent e) {
	}

	@Override
	public void mouseExited(MouseEvent e) {

	}

	@Override
	public void mousePressed(MouseEvent e) {
		if(e.getButton() == 1)
		{
			leftClick = true;
		}
		else if(e.getButton() == 2)
		{
			
		}
	}

	@Override
	public void mouseReleased(MouseEvent e) {
		if(e.getButton() == 1)
			leftClick = false;
		if(e.getButton() == 2)
			dragging = false;
	}

	@Override
	public void mouseDragged(MouseEvent e) {
		if(leftClick)
			leftClick = true;
		mousex = e.getX();
		mousey = e.getY();
	}

	@Override
	public void mouseMoved(MouseEvent e) {
		mousex = e.getX();
		mousey = e.getY();
	}

	@Override
	public void mouseWheelMoved(MouseWheelEvent e) {
		
	}

}