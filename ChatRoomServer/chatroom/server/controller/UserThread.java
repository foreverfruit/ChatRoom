package server.controller;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import server.model.User;
import server.model.UserManager;
import server.tool.Constants;

/**
 * User收发消息的工作线程
 * @author Mr.He
 *
 */
public class UserThread extends Thread{
	
	public Socket socket;
	public PrintWriter writer;
	public BufferedReader reader;
	//  线程池，用来发信息
	public ExecutorService threadPool = Executors.newCachedThreadPool();
	
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
		while(true){
			try{
				String msg = reader.readLine();
				
				System.out.println(msg);
				
				// 解析 
				if(msg.toString().startsWith(Constants.MSG_CLIENT)){
					// 1、用户一般消息:保存-分发-显示
					ServerManager.getInstance().dispatchMessage(msg.toString());
				}else if(msg.toString().startsWith(Constants.MSG_CLIENT_ONLINE)){
					// 2、用户连接消息，解析该用户名称，创建User对象，更新user
					User user = new User(msg.toString().substring(Constants.MSG_CLIENT_ONLINE.length()),
										 socket.getInetAddress().getHostAddress() );
					UserManager.getInstance().addUser(socket, user, this);
				}else if( msg.toString().startsWith(Constants.MSG_CLIENT_OFFLINE) ){
					// 3、用户下线消息，更新user
					UserManager.getInstance().removeUser(socket,this);
				}
			}catch(Exception e){
				e.printStackTrace();
			}
		}
	}
	
	// 发送消息到该用户
	public void sendMessage(final String msg){
		threadPool.execute(new Runnable() {
			@Override
			public void run() {
				writer.println(msg);
				writer.flush();
			}
		});
	}
	
	/**
	 * 关闭该Thread中的io资源，并Stop该线程
	 */
	@SuppressWarnings("deprecation")
	public void stopThread(){
		try{
			if(writer!=null){
				writer.close();
				writer = null;
			}
			if(reader!=null){
				reader.close();
				reader = null;
			}
			this.stop();
			// 关闭线程池
			threadPool.shutdown();
		}catch(Exception e ){
			e.printStackTrace();
		}
	}
}

