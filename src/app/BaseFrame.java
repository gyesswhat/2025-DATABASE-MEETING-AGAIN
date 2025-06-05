package app;
import java.awt.*;
import java.awt.event.*;
import javax.swing.*;

import admin.*;
import leader.*;
import login.*;
import member.*;

public class BaseFrame extends JFrame{
	public JPanel headerPanel;
	public JPanel panel;
	public LoginView lgv;
	public SignUp suv;
	public UserInfo uif;
	public DashBoard dbv;
	public RoomAdd adv;
	public RoomManagement rmv;
	public LeaderView lv;
	public MemberView mv;
	
    public BaseFrame(){
    	lgv=new LoginView(this);
    	suv=new SignUp(this);
    	uif=new UserInfo(this);
    	dbv=new DashBoard(this);
    	adv=new RoomAdd(this);
    	rmv=new RoomManagement(this);
    	lv=new LeaderView(this);
    	mv=new MemberView(this);
    	
    	setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
    	
    	setSize(1440, 1008);
    	headerPanel = new JPanel();
    	headerPanel.setBounds(12, 0, 1402, 114);
    	getContentPane().add(headerPanel);
    	headerPanel.setLayout(null);
    	
    	JLabel header = new JLabel("Meeting Again");
    	header.setFont(new Font("Arial", Font.BOLD, 39));
    	header.setBounds(39, 0, 1344, 64);
    	headerPanel.add(header);
    	
    	panel = new JPanel();
		panel.setBounds(12, 124, 1402, 803);
		getContentPane().add(panel);
		panel.setLayout(null);
    	
    	JButton loginViewBtn = new JButton("로그인");
    	loginViewBtn.addActionListener(new ActionListener() {
    		public void actionPerformed(ActionEvent e) {
    			change(panel, lgv);
    		}
    	});
    	loginViewBtn.setBounds(12, 74, 95, 23);
    	headerPanel.add(loginViewBtn);
    	
    	JButton signupViewBtn = new JButton("회원가입");
    	signupViewBtn.addActionListener(new ActionListener() {
    		public void actionPerformed(ActionEvent e) {
    			change(panel, suv);
    		}
    	});
    	signupViewBtn.setBounds(119, 74, 95, 23);
    	headerPanel.add(signupViewBtn);
    	
    	JButton userInfoBtn = new JButton("사용자정보");
    	userInfoBtn.addActionListener(new ActionListener() {
    		public void actionPerformed(ActionEvent e) {
    			change(panel, uif);
    		}
    	});
    	userInfoBtn.setBounds(226, 74, 95, 23);
    	headerPanel.add(userInfoBtn);
    	
    	JButton dashBoardBtn = new JButton("대시보드");
    	dashBoardBtn.addActionListener(new ActionListener() {
    		public void actionPerformed(ActionEvent e) {
    			change(panel, dbv);
    		}
    	});
    	dashBoardBtn.setBounds(333, 74, 95, 23);
    	headerPanel.add(dashBoardBtn);
    	
    	JButton roomAddBtn = new JButton("회의실추가");
    	roomAddBtn.addActionListener(new ActionListener() {
    		public void actionPerformed(ActionEvent e) {
    			change(panel, adv);
    		}
    	});
    	roomAddBtn.setBounds(440, 74, 95, 23);
    	headerPanel.add(roomAddBtn);
    	
    	JButton roomManagementBtn = new JButton("회의실관리");
    	roomManagementBtn.addActionListener(new ActionListener() {
    		public void actionPerformed(ActionEvent e) {
    			change(panel, rmv);
    		}
    	});
    	roomManagementBtn.setBounds(547, 74, 95, 23);
    	headerPanel.add(roomManagementBtn);
    	
    	JButton searchRoomBtn = new JButton("회의실검색");
    	searchRoomBtn.addActionListener(new ActionListener() {
    		public void actionPerformed(ActionEvent e) {
    			change(panel, lv);;
    		}
    	});
    	searchRoomBtn.setBounds(654, 74, 95, 23);
    	headerPanel.add(searchRoomBtn);
    	
    	JButton roomReservationBtn = new JButton("회의실예약");
    	roomReservationBtn.addActionListener(new ActionListener() {
    		public void actionPerformed(ActionEvent e) {
    			change(panel, lv);
    		}
    	});
    	roomReservationBtn.setBounds(761, 74, 95, 23);
    	headerPanel.add(roomReservationBtn);
    	
    	JButton meetingManagementBtn = new JButton("내회의관리");
    	meetingManagementBtn.addActionListener(new ActionListener() {
    		public void actionPerformed(ActionEvent e) {
    			change(panel, lv);
    		}
    	});
    	meetingManagementBtn.setBounds(868, 74, 95, 23);
    	headerPanel.add(meetingManagementBtn);
    	
    	JButton addTimeBtn = new JButton("회의시간추가");
    	addTimeBtn.addActionListener(new ActionListener() {
    		public void actionPerformed(ActionEvent e) {
    			change(panel, mv);
    		}
    	});
    	addTimeBtn.setBounds(975, 74, 111, 23);
    	headerPanel.add(addTimeBtn);
    }
    
    public static void main(String [] args) {
    	BaseFrame start=new BaseFrame();
    	start.setVisible(true);
    }
    
    public void change(JPanel now, JPanel changed) {
		now.removeAll();
		changed.setBounds(12, 124, 1402, 803);
		now.add(changed);
		changed.setLayout(null);
		revalidate();
		repaint();
    }
}