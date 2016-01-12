package server.model;

import java.io.IOException;
import java.net.Socket;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map.Entry;
import java.util.Vector;

import server.controller.Application;
import server.controller.UserThread;
import server.tool.Constants;

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
	
	public void addUser(Socket s,User u,UserThread t){
		// 1、添加用户
		users.put(s, u);
		// 2、反馈给该用户，成功登陆
		String msg = "SERVER：欢迎登陆聊天室！";
		t.sendMessage(Constants.MSG_SERVER + msg);
		// 3、更新界面，刷新用户list
		Application.getInstance().getWindow().invalidate("SERVER： " + u.getName() + " 已上线！！", Constants.INVALIDATE_UPDATE_USERS);
	}
	
	public void removeUser(Socket s ,UserThread t) {
		// 1、反馈给该用户，成功登陆
		String msg = "SERVER：您已下线，欢迎再来！";
		t.sendMessage(Constants.MSG_SERVER + msg);
		// 2、用户下线，删除该用户的socket、thread、user对象
		String username = users.remove(s).getName();
		threads.remove(s).stopThread();
		try{
				s.close();
		}catch(Exception e){
			e.printStackTrace();
		}
		// 3、更新界面，刷新用户list
		Application.getInstance().getWindow().invalidate("SERVER： " + username + " 已下线！！", Constants.INVALIDATE_UPDATE_USERS);
	}
	
	public void clearUser(){
		// 关闭资源
		if(!threads.isEmpty()){
			Iterator<Entry<Socket, UserThread>>  it = threads.entrySet().iterator();
			while(it.hasNext()){
				Entry<Socket, UserThread> entry = it.next();
				entry.getValue().stopThread();
				try {
					entry.getKey().close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}
		// 清空集合
		users.clear();
		threads.clear();
	}
	
	/**
	 * 返回当前所有在线用户名
	 * @return
	 */
	public Vector<String> getUsersName(){
		Vector<String> v = new Vector<>();
		for(User u: users.values()){
			v.add(u.getName());
		}
		return v;
	}
}
