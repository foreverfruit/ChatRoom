package server.controller;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;

import server.model.User;
import server.model.UserManager;

/**
 * User收发消息的工作线程
 * @author Mr.He
 *
 */
public class UserThread extends Thread{
	
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
				if(msg.toString().startsWith(Tools.MSG_CLIENT)){
					// 1、用户一般消息，刷新界面
					ServerManager.getInstance().addMessage(socket,msg.toString());
				}else if(msg.toString().startsWith(Tools.MSG_CLIENT_ONLINE)){
					// 2、用户连接消息，解析该用户名称，创建User对象，保存到Manager中，反馈一条成功登陆信息给用户
					User user = new User(msg.toString().substring("@#onLine#@".length()),
										 socket.getInetAddress().getHostAddress() );
					UserManager.getInstance().addUser(socket, user);
				}else if( msg.toString().startsWith(Tools.MSG_CLIENT_OFFLINE) ){
					// 3、用户下线消息
					UserManager.getInstance().removeUser(socket);
				}
			}catch(Exception e){
				e.printStackTrace();
			}
		}
	}
	
	// 发送消息到该用户
	public void sendMessage(String msg){
		writer.println(msg);
		writer.flush();
	}
}

