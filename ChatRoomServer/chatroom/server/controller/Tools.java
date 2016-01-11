package server.controller;

public class Tools {
	// 窗体宽高常量
	public static final int WIN_HEIGHT = 400;
	public static final int WIN_WIDTH = 600;
	// 刷新界面Flag
	public static final int INVALIDATE_START_SERVER = 10001;
	public static final int INVALIDATE_STOP_SERVER = 10002;
	public static final int INVALIDATE_MSG_ONLINE = 10003;
	
	// 常量消息
	public static final String MSG_CLIENT_OFFLINE = "@#offLine#@";
	public static final String MSG_CLIENT_ONLINE = "@#onLine#@";
	public static final String MSG_CLIENT = "@#clent_msg#@";
	public static final String MSG_SERVER = "@#server_msg#@";
	public static final String MSG_SERVER_CLOSE = "@#stopServer#@";
}
