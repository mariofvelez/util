package user_interface;

import java.lang.reflect.Method;

public class WindowTest {
	static UIScreen testTitle;
	static UIScreen testScreen;
	static UIScreen joyconSetup;
	public static void main(String[] args) throws Exception {
		// TODO Auto-generated method stub
		Long t1 = System.currentTimeMillis();
		
		{ //setting up the screens
			
			int[][] tl = {
					{10, 10},
					{110, 60}
			};
			int[][] center = {
					{300, 200},
					{400, 280}
			};
			int[][] bl = {
					{20, 530},
					{120, 580}
			};
			
			Class<?> windowClass = Window.class;
			Class<?> testClass = WindowTest.class;
			
			Method exitMethod = windowClass.getMethod("close");
			Method setScreen = testClass.getMethod("setTestScreen");
			Method setTitle = testClass.getMethod("setTestTitle");
			Method setSetup = testClass.getMethod("setJoyconSetup");
			
			Button exitButton = new Button("exit", tl, exitMethod, windowClass);
			Button screenButton = new Button("screen", center, setScreen, testClass);
			Button titleButton = new Button("title", tl, setTitle, testClass);
			Button joyconSetupButton = new Button("setup", bl, setSetup, testClass);
			
			testTitle = new UIScreen(3, 0);
			testScreen = new UIScreen(1, 0);
			joyconSetup = new JoyconSetupScreen(setTitle);
			
			testTitle.addButton(exitButton);
			testTitle.addButton(screenButton);
			testTitle.addButton(joyconSetupButton);
			testScreen.addButton(titleButton);
			
			Window.uim.createScreen(testTitle);
			Window.uim.createScreen(testScreen);
			Window.uim.createScreen(joyconSetup);
		}
		
		Window.start("this is a test");
		Window.uim.setScreen(testTitle);
		//Window.close();
	}
	public static void setTestScreen()
	{
		Window.uim.setScreen(testScreen);
	}
	public static void setTestTitle()
	{
		Window.uim.setScreen(testTitle);
	}
	public static void setJoyconSetup()
	{
		Window.uim.setScreen(joyconSetup);
	}

}
