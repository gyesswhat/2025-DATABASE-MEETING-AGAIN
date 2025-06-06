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


	public LoginController loginController;
	public LoginResult loginResult;
	public RegisterResult registerResult;

	public BaseFrame() {
		loginController=new LoginController();

		lgv = new LoginView(this);
		suv = new SignUp(this);
		dbv = new DashBoard(this);
		adv = new RoomAdd(this);
		rmv = new RoomManagement(this);

		// 로그인 후에 초기화될 View들은 처음에는 null로 두고
		// 버튼은 비활성화 상태로 생성
		uif = null;
		lv = null;
		mv = null;

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

		// 항상 사용 가능한 버튼들
		addHeaderButton("로그인", 12, lgv);
		addHeaderButton("회원가입", 119, suv);
		addHeaderButton("대시보드", 333, dbv);

		// 관리자 전용 버튼들
		addAdminOnlyButton("회의실추가", 440, adv);
		addAdminOnlyButton("회의실관리", 547, rmv);

		// 로그인 후에만 사용 가능한 버튼들 (처음엔 비활성화)
		addDisabledHeaderButton("사용자정보", 226);
		addDisabledHeaderButton("회의실예약", 654);
		addDisabledHeaderButton("내회의관리", 761);
		addDisabledHeaderButton("회의시간추가", 868);

		change(panel, lgv); // 기본화면: 로그인 뷰
	}

	private void addHeaderButton(String title, int x, Container view) {
		JButton btn = new JButton(title);
		btn.setBounds(x, 74, 95, 23);
		btn.addActionListener(e -> {
			if (view != null) {
				change(panel, view);
			} else {
				JOptionPane.showMessageDialog(this, "로그인이 필요합니다.");
			}
		});
		headerPanel.add(btn);
	}

	private void addDisabledHeaderButton(String title, int x) {
		JButton btn = new JButton(title);
		btn.setBounds(x, 74, 95, 23);
		btn.setEnabled(false);
		btn.addActionListener(e -> {
			JOptionPane.showMessageDialog(this, "로그인이 필요합니다.");
		});
		headerPanel.add(btn);
	}

	private void addAdminOnlyButton(String title, int x, Container view) {
		JButton btn = new JButton(title);
		btn.setBounds(x, 74, 95, 23);
		btn.setEnabled(false);
		btn.addActionListener(e -> {
			if (currentUser != null && "admin".equals(currentUser.getRole())) {
				if (view != null) {
					change(panel, view);
				}
			} else {
				JOptionPane.showMessageDialog(this, "관리자 권한이 필요합니다.");
			}
		});
		headerPanel.add(btn);
	}

	// 로그인 후 버튼들을 활성화하고 기능을 연결하는 메서드
	public void enableUserButtons() {
		Component[] components = headerPanel.getComponents();
		for (Component comp : components) {
			if (comp instanceof JButton) {
				JButton btn = (JButton) comp;
				String text = btn.getText();

				// 관리자 전용 버튼들
				if (text.equals("회의실추가") || text.equals("회의실관리")) {
					if (currentUser != null && "admin".equals(currentUser.getRole())) {
						btn.setEnabled(true);
					} else {
						btn.setEnabled(false);
					}
					continue;
				}

				// 각 버튼에 맞는 기능 연결
				switch (text) {
					case "사용자정보":
						btn.setEnabled(true);
						// 기존 ActionListener 제거 후 새로 추가
						btn.removeActionListener(btn.getActionListeners()[0]);
						btn.addActionListener(e -> {
							if (uif != null) change(panel, uif);
						});
						break;
					case "회의실예약":
					case "내회의관리":
						btn.setEnabled(true);
						btn.removeActionListener(btn.getActionListeners()[0]);
						btn.addActionListener(e -> {
							if (lv != null) change(panel, lv);
						});
						break;
					case "회의시간추가":
						btn.setEnabled(true);
						btn.removeActionListener(btn.getActionListeners()[0]);
						btn.addActionListener(e -> {
							if (mv != null) change(panel, mv);
						});
						break;
				}
			}
		}
	}

	// 로그아웃 시 버튼들을 비활성화하는 메서드
	public void disableUserButtons() {
		Component[] components = headerPanel.getComponents();
		for (Component comp : components) {
			if (comp instanceof JButton) {
				JButton btn = (JButton) comp;
				String text = btn.getText();

				if (text.equals("사용자정보") || text.equals("회의실예약") ||
						text.equals("내회의관리") || text.equals("회의시간추가") ||
						text.equals("회의실추가") || text.equals("회의실관리")) {
					btn.setEnabled(false);
				}
			}
		}

		// View들 초기화
		uif = null;
		lv = null;
		mv = null;
		currentUser = null;
	}

	public void change(Container now, Container changed) {
		if (changed == null) {
			JOptionPane.showMessageDialog(this, "해당 화면을 사용할 수 없습니다.");
			return;
		}

		now.removeAll();
		now.setLayout(null);
		changed.setBounds(0, 0, 1402, 803);
		changed.setLayout(null);

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