package client.view;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.GridLayout;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.SwingConstants;
import javax.swing.SwingUtilities;
import javax.swing.border.TitledBorder;

import client.controller.ConnectManager;
import client.tool.Constants;

public class ClientWindow extends JFrame{

	private static final long serialVersionUID = 1L;

	// 客户端窗体单例化
	private static ClientWindow instance = null;
	public static ClientWindow getInstance(){
		if(instance == null){
			instance = new ClientWindow();
		}
		return instance;
	}

	public JTextArea taChatRecord;
	public JTextField tfMsg;
	public JButton btConnect,btDisconnect,btSendMsg;
	public JTextField tfPort,tfHost,tfNickname;
	
	// 构造器
	private ClientWindow(){
		// 创建窗体
		createWindow();
		// 添加事件
		addEventListeners();
	}

	private void createWindow() {
		// ************ 窗体设置 *************
		// 标题
		setTitle("ChatRoom-Client");
		// 获取屏幕大小
	    Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
	    // 设置窗体大小
	    setSize(Constants.WIN_WIDTH,Constants.WIN_HEIGHT);
	    // 让窗口居中显示 
	    setLocation(screenSize.width/2-Constants.WIN_WIDTH/2,screenSize.height/2-Constants.WIN_HEIGHT/2);
	    // 窗口大小固定
		setResizable(false);
		
		// ********* 添加组件 *************
		// 上端组件
		JPanel topPanel = new JPanel(new GridLayout(1, 8, 5, 0));
		JLabel lbPort = new JLabel("端口");
		lbPort.setHorizontalAlignment(SwingConstants.RIGHT);
		JLabel lbHost = new JLabel("ip");
		lbHost.setHorizontalAlignment(SwingConstants.RIGHT);
		JLabel lbNickname = new JLabel("昵称");
		lbNickname.setHorizontalAlignment(SwingConstants.RIGHT);
		tfPort = new JTextField("8888",6);
		tfHost = new JTextField("127.0.0.1",6);
		tfNickname = new JTextField("nickname",6);
		btConnect = new JButton("连接");
		btDisconnect = new JButton("断开");
		btDisconnect.setEnabled(false);
		topPanel.setBorder(new TitledBorder("配置信息"));
		topPanel.add(lbHost);
		topPanel.add(tfHost);
		topPanel.add(lbPort);
		topPanel.add(tfPort);
		topPanel.add(lbNickname);
		topPanel.add(tfNickname);
		topPanel.add(btConnect);
		topPanel.add(btDisconnect);
		
		// 中间组件
		taChatRecord = new JTextArea();
		taChatRecord.setForeground(Color.blue); 
		taChatRecord.setLineWrap(true);
		taChatRecord.setWrapStyleWord(true);
		taChatRecord.setTabSize(4);
		taChatRecord.setEditable(false);
		JScrollPane midPanel = new JScrollPane(taChatRecord);
		midPanel.setBorder(new TitledBorder("聊天记录"));

		// 底部组件
		JPanel bottomPanel = new JPanel(new BorderLayout());  
		tfMsg = new JTextField(10);
		btSendMsg = new JButton("发送");
		btSendMsg.setEnabled(false);
		bottomPanel.add(tfMsg,BorderLayout.CENTER);
		bottomPanel.add(btSendMsg,BorderLayout.EAST);
		bottomPanel.setBorder(new TitledBorder("写消息"));

		this.add(topPanel, BorderLayout.NORTH);
		this.add(midPanel, BorderLayout.CENTER);
		this.add(bottomPanel, BorderLayout.SOUTH);
	}
	
	
	private void addEventListeners() {
		// 强制关闭窗口，进行关闭服务器处理
		this.addWindowListener(new WindowAdapter() {
			@Override
			public void windowClosing(WindowEvent e) {
				// 若连接存在，关闭资源 
				if(ConnectManager.getInstance().isConnect){
					ConnectManager.getInstance().disConnect();
				}
				System.exit(0);
			}
		});
		
		// 连接
		btConnect.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				// 检查ip、端口、用户名
				if(!isConnectInfoCorrect()){
					return ;
				}
				// 开启socket连接
				ConnectManager.getInstance().connect(tfPort.getText(),tfHost.getText(),tfNickname.getText());
			}
		});
		
		// 断开
		btDisconnect.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				ConnectManager.getInstance().disConnect();
			}
		});
		
		// 发送消息
		btSendMsg.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				String txt = tfMsg.getText();
				if( txt!=null && !txt.isEmpty() ){
					// 拼接消息
					String msg = Constants.MSG_CLIENT + ConnectManager.getInstance().nickname + "：" + txt;
					// 发送给服务器
					ConnectManager.getInstance().thread.sendMessage(msg);
					// 刷新消息框
					invalidate("我：" + txt, Constants.INVALIDATE_SEND_MSG);
				}else{
					showMessageBox("节约资源哦，俺们不发送空消息");
				}
			}
		});
	}
	
	/**
	 * 检查配置信息是否正确
	 */
	private boolean isConnectInfoCorrect() {
		// 检查port
		try{
			String portString = tfPort.getText();
			int portValue = Integer.parseInt(portString);
			if( portValue<1024 || portValue>65535 ){
				// port值不正确
				throw new Exception("port值不合法");
			}
		}catch(Exception e ){
			// port格式不合法
			e.printStackTrace();
			showMessageBox("error：错误的端口值");
			return false;
		}
		
		// 检查用户名:不能有消息前缀格式，不能是服务器通讯名称
		String nickname = tfNickname.getText();
		if(nickname.contains("#@#") || nickname.contains("SERVER") || nickname.contains("server")){
			showMessageBox("error:非法的昵称");
			return false;
		}
		
		// 检查ip
		if(!tfHost.getText().trim().matches(Constants.IP_REG)){
			showMessageBox("error:错误的ip地址");
			return false;
		}
		
		return true;
	}

	/**
	 * 
	 * @param msg 经过处理，可直接输出的消息记录
	 * @param flag 刷新的类型标志
	 */
	public void invalidate(String msg,int flag){
		// 先保存消息
		if( msg!=null && !msg.isEmpty() ){
			ConnectManager.getInstance().msgRecord.append( msg + "\n\r" );
		}
		
		if( flag == Constants.INVALIDATE_CONNECTED ){
			// 连接到服务器后的UI刷新
			SwingUtilities.invokeLater(new Runnable() {
				@Override
				public void run() {
					tfPort.setEnabled(false);
					tfNickname.setEnabled(false);
					tfHost.setEnabled(false);
					btConnect.setEnabled(false);
					btSendMsg.setEnabled(true);
					btDisconnect.setEnabled(true);
				}
			});
		}else if( flag == Constants.INVALIDATE_DISCONNECTED ){
			// 连接到服务器后的UI刷新
			SwingUtilities.invokeLater(new Runnable() {
				@Override
				public void run() {
					taChatRecord.setText(ConnectManager.getInstance().msgRecord.toString());
					tfPort.setEnabled(true);
					tfNickname.setEnabled(true);
					tfHost.setEnabled(true);
					btConnect.setEnabled(true);
					btSendMsg.setEnabled(false);
					btDisconnect.setEnabled(false);
				}
			});
		}else if( flag == Constants.INVALIDATE_REC_MSG ){
			// 连接到服务器后的UI刷新
			SwingUtilities.invokeLater(new Runnable() {
				@Override
				public void run() {
					taChatRecord.setText(ConnectManager.getInstance().msgRecord.toString());
				}
			});
		}else if( flag == Constants.INVALIDATE_SEND_MSG ){
			// 连接到服务器后的UI刷新
			SwingUtilities.invokeLater(new Runnable() {
				@Override
				public void run() {
					tfMsg.setText("");
					taChatRecord.setText(ConnectManager.getInstance().msgRecord.toString());
				}
			});
		}
	}
	
	public void showMessageBox(String msg){
		JOptionPane.showMessageDialog(this, msg);
	}
}
