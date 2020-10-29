package user_interface;

import javax.swing.JFrame;
import javax.swing.JOptionPane;

import java.awt.Color;
import java.awt.Container;
import java.awt.Dimension;
import java.awt.GraphicsConfiguration;
import java.awt.GridLayout;
import java.awt.Toolkit;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
/**
 * 
 * @author Mario Velez
 * 
 *
 */
public class Window extends JFrame
{
	/**
	 * 
	 */
	private static final long serialVersionUID = -3494605708565768482L;
	public static final int WIDTH = 900;
	public static final int HEIGHT = 600;
	
	public static Window window;
	public static Field edit;
	public static UIManager uim = new UIManager();
	
	public static Thread editThread;
	public static Thread uimThread;
	
	public Window(String name)
	{
		super(name);
		
		this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		this.setBackground(Color.BLACK);
		
		Dimension windowSize = new Dimension(WIDTH, HEIGHT);
		this.setSize(windowSize);
	}
	/**
	 * Creates a new window and starts both threads:
	 * - a graphics thread that's used for drawing on the screen
	 * - a logic screen for all computations
	 * @param name - the name of the window
	 * @throws Exception
	 */
	public static void start(String name) throws Exception
	{
		window = new Window(name);
		
		Container contentPane = window.getContentPane();
		//contentPane.setLayout(new GridLayout(1,1));
		
		edit = new Field(window.getSize());
		contentPane.add(edit);
		
		editThread = new Thread(edit);
		uimThread = new Thread(uim);
		
		editThread.start();
		uimThread.start();
		
		window.setVisible(true);
		window.setLocation(200, 100);
		//window.setDefaultCloseOperation(DO_NOTHING_ON_CLOSE);
		//window.setAlwaysOnTop(true);
		//window.setResizable(false);
	}
	public static void close()
	{
		window.dispose();
		UIManager.running = false;
		Field.running = false;
		System.exit(0);
	}
}
