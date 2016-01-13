package client.controller;

import java.net.Socket;

import client.tool.Constants;
import client.view.ClientWindow;


public class ConnectManager {

	public boolean isConnect = false;
	public Socket socket;
	public ClientThread thread;
	public String nickname;
	public StringBuffer msgRecord = new StringBuffer();
	
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

	/**
	 * 连接服务器
	 * @param port
	 * @param ip
	 * @param nickname
	 */
	public void connect(String port, String ip, String nickname) {
		try {
			this.nickname = nickname;
			socket = new Socket(ip, Integer.parseInt(port));
			thread = new ClientThread(socket);
			thread.start();
			isConnect = true;
			ClientWindow.getInstance().invalidate(null, Constants.INVALIDATE_CONNECTED);
			// 给服务器发送一条上线消息
			thread.sendMessage(Constants.MSG_CLIENT_ONLINE + nickname );
		} catch (Exception e) {
			ClientWindow.getInstance().showMessageBox("socket异常，连接失败");
			e.printStackTrace();
		} 
	}
	
	/**
	 * 断开连接
	 */
	public void disConnect() {
		try{
			// 给服务器发送一条下线消息
			thread.sendMessage(Constants.MSG_CLIENT_ONLINE + nickname );
			// 关闭工作线程
			thread.stopThread();
			thread = null;
			// 关闭socket
			socket.close();
			socket = null;
			// 清理资源
			nickname = "";
			msgRecord.delete(0, msgRecord.length());
			isConnect = false;
			ClientWindow.getInstance().invalidate(null, Constants.INVALIDATE_DISCONNECTED);
		}catch(Exception e){
			e.printStackTrace();
		}
	}

}
