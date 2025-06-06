package common.model;

import javax.swing.*;

import app.BaseFrame;

import java.awt.*;

import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;

public class UserView extends JPanel{

	public JPanel userPanel;
	public User user;
	public JLabel infoLabel=new JLabel();
	public JButton logoutBtn = new JButton();
	public JButton userInfoBtn = new JButton();
	public JButton addTimeBtn = new JButton();
	public JButton reserveRoomBtn = new JButton();

	public UserView(BaseFrame baseframe) {
		try {
			//로그인 결과 생성된 user 객체를 가져온다
			if (baseframe.loginResult != null && baseframe.loginResult.getUser() != null) {
				user = baseframe.loginResult.getUser();
			} else {
				user = baseframe.getCurrentUser(); // fallback
			}

			// user가 여전히 null이면 기본값 설정
			if (user == null) {
				throw new IllegalStateException("로그인된 사용자 정보가 없습니다.");
			}

			setLayout(null);
			infoLabel.setText("기능 안내 라벨");
			infoLabel.setFont(new Font("굴림", Font.PLAIN, 30));
			infoLabel.setBounds(12, 10, 317, 47);
			add(infoLabel);

			//로그아웃 버튼
			logoutBtn.setText("로그아웃");
			logoutBtn.setBounds(1271, 27, 123, 23);
			logoutBtn.addActionListener(new ActionListener() {
				public void actionPerformed(ActionEvent e) {
					// 로그아웃 처리
					baseframe.disableUserButtons();
					baseframe.change(baseframe.panel, baseframe.lgv);
				}
			});
			add(logoutBtn);

			//사용자 정보 버튼
			userInfoBtn.setText("사용자 정보");
			userInfoBtn.setBounds(1136, 27, 123, 23);
			userInfoBtn.addActionListener(new ActionListener() {
				public void actionPerformed(ActionEvent e) {
					if (baseframe.uif != null) {
						baseframe.change(baseframe.panel, baseframe.uif);
					}
				}
			});
			add(userInfoBtn);

			//회의 시간 추가 버튼
			addTimeBtn.setText("회의 시간 추가");
			addTimeBtn.setBounds(1001, 27, 123, 23);
			addTimeBtn.addActionListener(new ActionListener() {
				public void actionPerformed(ActionEvent e) {
					if (baseframe.mv != null) {
						baseframe.change(baseframe.panel, baseframe.mv);
					}
				}
			});
			add(addTimeBtn);

			//회의실 예약 버튼 정의
			reserveRoomBtn=new JButton("회의실 예약");
			reserveRoomBtn.addActionListener(new ActionListener() {
				public void actionPerformed(ActionEvent e) {
					if (baseframe.lv != null) {
						baseframe.change(baseframe.panel, baseframe.lv);
					}
				}
			});
			reserveRoomBtn.setBounds(866, 27, 123, 23);
			add(reserveRoomBtn);

			// 사용자 역할에 따라 버튼 가시성 설정
			boolean isLeader = user.getRole() != null && user.getRole().equals("leader");
			reserveRoomBtn.setVisible(isLeader);

			setuserPanel();

		} catch (Exception e) {
			System.err.println("UserView 초기화 중 오류: " + e.getMessage());
			e.printStackTrace();

			// 오류 발생 시 기본 설정
			setLayout(null);
			JLabel errorLabel = new JLabel("사용자 정보를 불러올 수 없습니다.");
			errorLabel.setBounds(12, 10, 400, 47);
			errorLabel.setForeground(Color.RED);
			add(errorLabel);

			// 기본 로그아웃 버튼만 추가
			JButton backBtn = new JButton("로그인 화면으로");
			backBtn.setBounds(12, 60, 150, 23);
			backBtn.addActionListener(e2 -> {
				baseframe.change(baseframe.panel, baseframe.lgv);
			});
			add(backBtn);
		}
	}

	public void setuserPanel() {
		userPanel=new JPanel();
		userPanel.setBounds(12, 78, 1378, 715);
		add(userPanel);
		userPanel.setLayout(null);
	}
}