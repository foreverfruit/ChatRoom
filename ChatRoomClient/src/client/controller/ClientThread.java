package client.controller;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import client.tool.Constants;
import client.view.ClientWindow;

public class ClientThread extends Thread{
	
	public Socket socket;
	public PrintWriter writer;
	public BufferedReader reader;
	//  线程池，用来发信息
	public ExecutorService threadPool = Executors.newCachedThreadPool();
	
	public ClientThread(Socket socket){
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
				
				System.out.println(msg.toString());
				
				// 解析 
				if(msg.toString().startsWith(Constants.MSG_SERVER_CLOSE)){
					// 1、服务器停止了，关闭资源 - TODO 这里有点问题，因为在disConnect方法中需要停止该线程，也就是相当于在run中执行this.stop()
					// 暂时先采用另开线程尝试避免该问题
					threadPool.execute(new Runnable() {
						@Override
						public void run() {
							ConnectManager.getInstance().disConnect();
						}
					});
				}else {
					// 2、一般消息，显示
					ClientWindow.getInstance().invalidate(
							msg.toString().substring(Constants.MSG_CLIENT_ONLINE.length()),
							Constants.INVALIDATE_REC_MSG);
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