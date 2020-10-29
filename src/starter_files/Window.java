package starter_files;

import javax.swing.JFrame;
import java.awt.Container;
import java.awt.Dimension;

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
	
	public Window(String name)
	{
		super(name);
		
		this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		
		Dimension windowSize = new Dimension(WIDTH, HEIGHT);
		this.setSize(windowSize);
	}
	public static void main(String[] args) throws Exception
	{
		window = new Window("New Window");
		
		Container contentPane = window.getContentPane();
		
		Field field = new Field(window.getSize());
		contentPane.add(field);
		
		window.setVisible(true);
		window.setLocation(200, 100);
	}
	public static void close()
	{
		window.dispose();
	}
}
