package server.controller;

import java.net.ServerSocket;
import java.net.Socket;
import java.util.Iterator;
import java.util.Map;

import server.model.UserManager;

/**
 * 连接管理类，管理服务器的serverSocket
 * @author Mr.He
 */
public class ServerManager {
	// 当前服务器状态
	public boolean isServerStart = false;
	// 服务器socket对象
	public ServerSocket serverSocket;
	// 服务器工作线程：只作用接受客服端的连接
	public Thread workThread;
	// 聊天记录
	public StringBuilder msgRecord = new StringBuilder();	
	
	// 单例
	private static ServerManager instance = null;
	private ServerManager(){
	}
	public static ServerManager getInstance(){
		if(instance == null){
			instance = new ServerManager();
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
				// 服务器开启，刷新界面
				isServerStart = true;
				Application.getInstance().getWindow().invalidate(null, Tools.INVALIDATE_START_SERVER);
			}
		}catch(Exception e){
			e.printStackTrace();
		}
	}
	
	// 关闭服务器
	@SuppressWarnings("deprecation")
	public void stopServer(){
		// 0.停止服务器接收连接的线程，不再accept
		if(workThread!=null){
			workThread.stop();
			workThread = null;
		}
		// 1.给每个已用户发送一条服务器已停止的消息,停止所有用户的工作线程
		if(!UserManager.getInstance().threads.isEmpty()){
			Iterator<Map.Entry<Socket,UserThread>> it = UserManager.getInstance().threads.entrySet().iterator();
			while (it.hasNext()) {
				Map.Entry<Socket,UserThread> entry = (Map.Entry<Socket,UserThread>) it.next();
				entry.getValue().sendMessage(Tools.MSG_SERVER_CLOSE);
			}
		}
		try{
			// 停止所有工作线程
			for(Socket s : UserManager.getInstance().threads.keySet()){
				s.close();
			}
			// 关闭服务器
			serverSocket.close();
		}catch(Exception e){
			e.printStackTrace();
		}
		serverSocket = null;
		isServerStart = false;
		// 删除在线用户
		UserManager.getInstance().clearUser();
		// 通知界面刷新
		Application.getInstance().getWindow().invalidate(null, Tools.INVALIDATE_STOP_SERVER);
	}
	
	/**
	 * 添加消息到消息记录
	 * @param socket 消息发送者的socket，若为null，表示为服务器发送的消息
	 * @param string 消息内容
	 */
	public void addMessage(Socket socket, String string) {
		// 获得一条用户聊天消息，添加到msg中
		
		// 通知界面刷新TextArea
		
	}
	
	/**
	 * 转发消息到其他用户
	 * @param socket 消息发送者的socket
	 * @param string 消息内容
	 */
	public void dispatchMessage(Socket socket, String string){
		
	}
}
