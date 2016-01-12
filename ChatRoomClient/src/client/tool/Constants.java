package client.tool;

public class Constants {
	// 窗体宽高常量
	public static final int WIN_HEIGHT = 400;
	public static final int WIN_WIDTH = 600;
	// 刷新界面Flag
	public static final int INVALIDATE_START_SERVER = 10001;
	public static final int INVALIDATE_STOP_SERVER = 10002;
	public static final int INVALIDATE_SEND_MSG = 10003;
	public static final int INVALIDATE_DISPATCH_MSG = 10004;
	public static final int INVALIDATE_UPDATE_USERS = 10005;
	
	// 常量消息
	public static final String MSG_CLIENT_OFFLINE = "#@#offLine#@#";
	public static final String MSG_CLIENT_ONLINE = "#@#onLine#@#";
	public static final String MSG_CLIENT = "#@#client_msg#@#";		// 客户端消息前缀
	public static final String MSG_SERVER = "#@#server_msg#@#";		// 服务器消息前缀
	public static final String MSG_SERVER_CLOSE = "#@#stopServer#@#";     // 关闭服务器发送的消息
	
	public static final String STRING_SERVER_START = "SERVER：服务器启动成功...";
	public static final String STRING_SERVER_CLOSE = "SERVER：服务器已关闭...";	// 关闭服务器在服务器端显示的消息
	
	// IP正则表达式
	public static final String IP_REG = "([1-9]|[1-9]//d|1//d{2}|2[0-4]//d|25[0-5])(//.(//d|[1-9]//d|1//d{2}|2[0-4]//d|25[0-5])){3}";
}
