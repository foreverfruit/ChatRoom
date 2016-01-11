package server.model;

import java.net.Socket;
import java.util.HashMap;

import server.controller.UserThread;

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
	
	public void addUser(Socket s,User u){
		// 添加用户
		// 通知界面，刷新用户list
	}
	
	public void removeUser(Socket socket) {
		// 用户下线，删除该用户的socket、thread、user对象
		// 通知界面刷新用户列表
	}
	
	public void clearUser(){
		users.clear();
		threads.clear();
	}
}
