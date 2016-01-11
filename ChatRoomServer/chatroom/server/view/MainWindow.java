package server.view;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JSplitPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.SwingUtilities;
import javax.swing.border.TitledBorder;

import server.controller.ServerManager;
import server.controller.Tools;
import server.controller.UserThread;
import server.model.UserManager;

/**
 * 应用主窗体
 * @author Mr.He
 */
public class MainWindow extends JFrame{
	// 默认的序列号
	private static final long serialVersionUID = 1L;
	
	// 数据
	String serviceInfo;
	String chatInfo;
	
	// 窗口组件
	public JPanel topPanel;
	public JSplitPane mainPanel;
	public JList<String> userList;
	public JScrollPane userListPanel;
	public JScrollPane chatRecordPanel;
	public JTextArea chatRecordTextArea;
	public JPanel messagePanel;
	public JPanel rightPanel;
	
	public JLabel laPort;
	public JTextField tfPort;
	public JButton btStar;
	public JButton btStop;
	public JTextField tfMesssage;
	public JButton btSendMsg;
	
	// 构造器：初始化窗体界面
	public MainWindow() {
		// 标题
		setTitle("ChatRoom-Service");
		// 获取屏幕大小
	    Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
	    // 设置窗体大小
	    setSize(Tools.WIN_WIDTH,Tools.WIN_HEIGHT);
	    // 让窗口居中显示 
	    setLocation(screenSize.width/2-Tools.WIN_WIDTH/2,screenSize.height/2-Tools.WIN_HEIGHT/2);
	    // 窗口大小固定
		setResizable(false);
		// 添加组件
		addComponents();
		// 注册事件监听
		addEventListener();
	}

	// 添加界面内容
	private void addComponents() {
		laPort = new JLabel("端口:");
		tfPort = new JTextField("8888", 6);
		btStar = new JButton("启动");
		btStop = new JButton("停止");
		btStop.setEnabled(false);
		topPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
		topPanel.add(laPort);
		topPanel.add(tfPort);
		topPanel.add(btStar);
		topPanel.add(btStop);
		topPanel.setBorder(new TitledBorder("配置信息"));
		
		userList = new JList<>();
		userListPanel = new JScrollPane(userList);
		userListPanel.setBorder(new TitledBorder("在线用户"));
		
		chatRecordTextArea = new JTextArea();
		chatRecordTextArea.setForeground(Color.blue); 
		chatRecordTextArea.setLineWrap(true);
		chatRecordTextArea.setWrapStyleWord(true);
		chatRecordTextArea.setTabSize(4);
		chatRecordTextArea.setEditable(false);
		chatRecordPanel = new JScrollPane(chatRecordTextArea);
		chatRecordPanel.setBorder(new TitledBorder("聊天记录"));
		
		messagePanel = new JPanel(new BorderLayout());  
		tfMesssage = new JTextField(10);
		btSendMsg = new JButton("发送");
		btSendMsg.setEnabled(false);
		messagePanel.add(tfMesssage,BorderLayout.CENTER);
		messagePanel.add(btSendMsg,BorderLayout.EAST);
		messagePanel.setBorder(new TitledBorder("写消息"));
		messagePanel.setBorder(new TitledBorder("写消息"));  

		rightPanel = new JPanel(new BorderLayout());
		rightPanel.add(messagePanel,BorderLayout.SOUTH);
		rightPanel.add(chatRecordPanel,BorderLayout.CENTER);
		
		mainPanel = new JSplitPane(JSplitPane.HORIZONTAL_SPLIT, userListPanel,rightPanel);  
		mainPanel.setDividerLocation(Tools.WIN_WIDTH/4); 
		mainPanel.setEnabled(false);
		
		this.add(topPanel,BorderLayout.NORTH);
		this.add(mainPanel,BorderLayout.CENTER);
	}
	

	private void addEventListener() {
		// 强制关闭窗口，进行关闭服务器处理
		this.addWindowListener(new WindowAdapter() {
			@Override
			public void windowClosing(WindowEvent e) {
				if(ServerManager.getInstance().isServerStart){
					ServerManager.getInstance().stopServer();
				}
				System.exit(0);
			}
		});
		
		// 开启服务器
		btStar.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				try{
					int port = Integer.parseInt(tfPort.getText());
					if( port >= 1024 && port <= 65535 ){
						ServerManager.getInstance().startServer(port);
					}else{
						throw new Exception("端口大小有误");
					}
				}catch(Exception ep){
					showMessageBox("端口设置有误，请重新输入(数字：1024~65535)！");
					ep.printStackTrace();
				}
			}
		});
		
		// 关闭服务器
		btStop.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				ServerManager.getInstance().stopServer();
			}
		});
		
		// 发送消息
		btSendMsg.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				// 构造消息
				String msg = Tools.MSG_SERVER + tfMesssage.getText();
				// 发送给每一个在线用户
				if(!UserManager.getInstance().threads.isEmpty()){
					for(UserThread t : UserManager.getInstance().threads.values()){
						t.sendMessage(msg);
					}
				}
				// 消息记录添加该消息记录
				ServerManager.getInstance().addMessage(null, msg);
			}
		});
	}
	
	/**
	 * 
	 * @param msg 若是因为消息刷新，则为消息内容，否则为null
	 * @param flag 刷新的类型标志
	 */
	public void invalidate(String msg,int flag){
		if( flag == Tools.INVALIDATE_START_SERVER ){
			SwingUtilities.invokeLater(new Runnable() {
				@Override
				public void run() {
					tfPort.setEnabled(false);
					btStar.setEnabled(false);
					btSendMsg.setEnabled(true);
					btStop.setEnabled(true);
				}
			});
		}else if( flag == Tools.INVALIDATE_STOP_SERVER ){
			SwingUtilities.invokeLater(new Runnable() {
				@Override
				public void run() {
					tfPort.setEnabled(true);
					btStar.setEnabled(true);
					btSendMsg.setEnabled(false);
					btStop.setEnabled(false);
				}
			});
		}
		
	}
	
	public void showMessageBox(String msg){
		JOptionPane.showMessageDialog(this, msg);
	}
}
