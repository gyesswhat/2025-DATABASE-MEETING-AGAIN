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
		//로그인 결과 생성된 user 객체를 가져온다
		user=baseframe.loginResult.getUser();
		
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
    			baseframe.change(baseframe.panel, baseframe.lgv);
    		}
    	});
    	add(logoutBtn);

    	//사용자 정보 버튼
    	userInfoBtn.setText("사용자 정보");
    	userInfoBtn.setBounds(1136, 27, 123, 23);
    	userInfoBtn.addActionListener(new ActionListener() {
    		public void actionPerformed(ActionEvent e) {
    			baseframe.change(baseframe.panel, baseframe.uif);
    		}
    	});
    	add(userInfoBtn);
    	
    	//회의 시간 추가 버튼
    	addTimeBtn.setText("회의 시간 추가");
    	addTimeBtn.setBounds(1001, 27, 123, 23);
    	addTimeBtn.addActionListener(new ActionListener() {
    		public void actionPerformed(ActionEvent e) {
    			baseframe.change(baseframe.panel, baseframe.mv);
    		}
    	});
		add(addTimeBtn);
		
		//회의실 예약 버튼 정의
		reserveRoomBtn=new JButton("회의실 예약");
		reserveRoomBtn.addActionListener(new ActionListener() {
    		public void actionPerformed(ActionEvent e) {
    			baseframe.change(baseframe.panel, baseframe.lv);
    		}
    	});
		reserveRoomBtn.setBounds(866, 27, 123, 23);
		add(reserveRoomBtn);
		reserveRoomBtn.setVisible(user.getRole().equals("leader"));
		
    	setuserPanel();
	}
	public void setuserPanel() {
		userPanel=new JPanel();
		userPanel.setBounds(12, 78, 1378, 715);
		add(userPanel);
		userPanel.setLayout(null);
		
	}
}