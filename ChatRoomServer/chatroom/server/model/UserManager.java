package server.model;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.HashMap;

/**
 * 用户管理类，管理当前连接的用户：User、Socket、Thread一一对应
 * @author Mr.He
 *
 */
public class UserManager {
	
	// 单例
	private static UserManager instance = null;
	private UserManager(){
	}
	public static UserManager getInstance(){
		if(instance == null){
			instance = new UserManager();
		}
		return instance;
	}
	
	public HashMap<Socket,User> users = new HashMap<>();	// 所有用户对象
	public HashMap<Socket,UserThread> threads = new HashMap<>(); // 所有用户的工作线程对象
	
	public void addSocket(Socket socket){
		// 用户连接，为该用户创建其工作线程，并保存线程与Socket对象
		UserThread thread = new UserThread(socket);
		threads.put(socket, thread);
		// 启动其线程，开始收发该用户的消息
		thread.start();
	}
}

/**
 * User收发消息的工作线程
 * @author Mr.He
 *
 */
class UserThread extends Thread{
	public Socket socket;
	public PrintWriter writer;
	public BufferedReader reader;
	
	public UserThread(Socket socket){
		try{
			this.socket = socket;
			reader = new BufferedReader(new InputStreamReader(socket.getInputStream()));  
			writer = new PrintWriter(socket.getOutputStream());
		}catch(Exception e){
			e.printStackTrace();
		}
	}
	
	@Override
	public void run() {
		// 用户线程永远工作接收用户的来信
		StringBuilder msg = new StringBuilder();
		while(true){
			try{
				// 清空上一条消息
				msg.delete(0, msg.length()-1);
				String tmp = "";
				while( !( ( tmp = reader.readLine() ) == null ) ){
					msg.append(tmp);
				}
				// 本条消息读完，解析 TODO 
				// 1、用户连接消息，解析该用户名称，创建User对象，保存到Manager中，反馈一条成功登陆信息给用户
				
				// 2、用户一般消息，解析消息，刷新界面
				
				// 3、用户下线消息
				
			}catch(Exception e){
				e.printStackTrace();
			}
		}
	}
}
