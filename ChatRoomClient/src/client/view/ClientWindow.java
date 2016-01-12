package client.view;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.GridLayout;
import java.awt.Toolkit;
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
import javax.swing.border.TitledBorder;

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
		tfPort = new JTextField(6);
		tfHost = new JTextField(6);
		tfNickname = new JTextField(6);
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
				// TODO 若连接存在，手动关闭资源 
				System.exit(0);
			}
		});
	}
	
	/**
	 * 
	 * @param msg 经过处理，可直接输出的消息记录
	 * @param flag 刷新的类型标志
	 */
	public void invalidate(String msg,int flag){
	}
	
	public void showMessageBox(String msg){
		JOptionPane.showMessageDialog(this, msg);
	}
}
