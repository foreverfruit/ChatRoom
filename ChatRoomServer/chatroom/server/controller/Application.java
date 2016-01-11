package server.controller;

import server.view.MainWindow;
/**
 * 应用启动器
 * @author Mr.He
 *
 */
public class Application {
	
	private MainWindow app;
	
	private Application(){
		app = new MainWindow();
	}
	
	private static Application instance = null;
	public static Application getInstance(){
		if(instance == null){
			instance = new Application();
		}
		return instance;
	}
	
	public void start(){
		app.setVisible(true);
	}
	
	public MainWindow getWindow(){
		return app;
	}
	
	public static void main(String[] args) {
		Application.getInstance().start();
	}
}
