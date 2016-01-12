package client.controller;


public class ConnectManager {

	public boolean isConnect = false;
	
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
		
	}
	
	/**
	 * 断开连接
	 */
	public void disConnect() {
		
	}

}
