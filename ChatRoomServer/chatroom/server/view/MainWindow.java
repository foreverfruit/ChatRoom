package server.view;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Toolkit;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JSplitPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.border.TitledBorder;

/**
 * 应用主窗体
 * @author Mr.He
 *
 */
public class MainWindow extends JFrame{
	// 默认的序列号
	private static final long serialVersionUID = 1L;
	// 窗体宽高常量
	public final int WIN_HEIGHT = 400;
	public final int WIN_WIDTH = 600;
	
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
	    setSize(WIN_WIDTH,WIN_HEIGHT);
	    // 让窗口居中显示 
	    setLocation(screenSize.width/2-WIN_WIDTH/2,screenSize.height/2-WIN_HEIGHT/2);
	    // 窗口大小固定
		setResizable(false);
		// 添加组件
		addComponents();
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
		messagePanel.add(tfMesssage,BorderLayout.CENTER);
		messagePanel.add(btSendMsg,BorderLayout.EAST);
		messagePanel.setBorder(new TitledBorder("写消息"));
		messagePanel.setBorder(new TitledBorder("写消息"));  

		rightPanel = new JPanel(new BorderLayout());
		rightPanel.add(messagePanel,BorderLayout.SOUTH);
		rightPanel.add(chatRecordPanel,BorderLayout.CENTER);
		
		mainPanel = new JSplitPane(JSplitPane.HORIZONTAL_SPLIT, userListPanel,rightPanel);  
		mainPanel.setDividerLocation(WIN_WIDTH/4); 
		mainPanel.setEnabled(false);
		
		this.add(topPanel,BorderLayout.NORTH);
		this.add(mainPanel,BorderLayout.CENTER);
	}
	
	/**
	 * 更新在线用户列表，从UserManager中取用户数据
	 */
	public void updateUserList(){
	}
	
}
