package server.controller;

import java.net.ServerSocket;

import server.model.UserManager;

/**
 * 连接管理类，管理服务器的serverSocket
 * @author Mr.He
 */
public class ConnectManager {
	// 当前服务器状态
	public boolean isServerStart = false;
	// 服务器socket对象
	public ServerSocket serverSocket;
	// 服务器工作线程：只作用接受客服端的连接
	public Thread workThread;
	
	// 单例
	private static ConnectManager instance = null;
	private ConnectManager(){
	}
	public static ConnectManager getInstance(){
		if(instance == null){
			instance = new ConnectManager();
		}
		return instance;
	}
	
	// 启动服务器
	public void startServer(int port){
		try{
			if(!isServerStart){
				serverSocket = new ServerSocket(port); 
				workThread = new Thread(new Runnable() {
					@Override
					public void run() {
						// 开启线程进行服务器工作·等待用户链接
						try{
							while(true){
								// 此时连接了一个用户，添加用户Socket集合
								UserManager.getInstance().addSocket(serverSocket.accept());
							}
						}catch(Exception e){
							e.printStackTrace();
						}
					}
				});
				workThread.start();
				// TODO 服务器开启，刷新界面
				isServerStart = true;
			}
		}catch(Exception e){
			e.printStackTrace();
		}
	}
	
	// 关闭服务器
	public void stopServer(){
		// 0.停止服务器接收连接的线程，不再accept
		// 1.给每个已用户发送一条服务器已停止的消息
		// 2.停止所有用户的工作线程
		isServerStart = false;
		// 通知界面刷新
	}
}
