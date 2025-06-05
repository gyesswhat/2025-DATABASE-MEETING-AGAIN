package app;

import java.awt.*;
import java.awt.event.*;
import javax.swing.*;

import admin.*;
import leader.*;
import login.*;
import member.*;
import common.model.*;

public class BaseFrame extends JFrame {
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

	private User currentUser;

	public BaseFrame() {
		lgv = new LoginView(this);
		suv = new SignUp(this);
		uif = new UserInfo(this);
		dbv = new DashBoard(this);
		adv = new RoomAdd(this);
		rmv = new RoomManagement(this);
		lv = new LeaderView(this);
		mv = new MemberView(this);

		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setSize(1440, 1008);
		setLayout(null);

		headerPanel = new JPanel();
		headerPanel.setBounds(12, 0, 1402, 114);
		headerPanel.setLayout(null);
		getContentPane().add(headerPanel);

		JLabel header = new JLabel("Meeting Again");
		header.setFont(new Font("Arial", Font.BOLD, 39));
		header.setBounds(39, 0, 1344, 64);
		headerPanel.add(header);

		panel = new JPanel();
		panel.setBounds(12, 124, 1402, 803);
		panel.setLayout(null);
		getContentPane().add(panel);

		addHeaderButton("로그인", 12, lgv);
		addHeaderButton("회원가입", 119, suv);
		addHeaderButton("사용자정보", 226, uif);
		addHeaderButton("대시보드", 333, dbv);
		addHeaderButton("회의실추가", 440, adv);
		addHeaderButton("회의실관리", 547, rmv);
		addHeaderButton("회의실검색", 654, lv);
		addHeaderButton("회의실예약", 761, lv);
		addHeaderButton("내회의관리", 868, lv);
		addHeaderButton("회의시간추가", 975, mv);

		change(panel, lgv); // 기본화면: 로그인 뷰
	}

	private void addHeaderButton(String title, int x, Container view) {
		JButton btn = new JButton(title);
		btn.setBounds(x, 74, 95, 23);
		btn.addActionListener(e -> change(panel, view));
		headerPanel.add(btn);
	}

	public void change(Container now, Container changed) {
		now.removeAll();
		now.setLayout(null);
		changed.setBounds(0, 0, 1402, 803);
		now.add(changed);
		revalidate();
		repaint();
	}

	public void setCurrentUser(User user) {
		this.currentUser = user;
	}

	public User getCurrentUser() {
		if (currentUser == null) {
			System.out.println("[경고] 로그인 사용자 없음 (getCurrentUser)");
		}
		return currentUser;
	}
}