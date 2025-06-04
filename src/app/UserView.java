package app;
import javax.swing.*;
import java.awt.*;

import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;

public class UserView extends JPanel{
	
	public JPanel userPanel;
	public JLabel infoLabel=new JLabel();
	public JButton logoutBtn = new JButton();
	public JButton userInfoBtn = new JButton();
	public UserView(BaseFrame baseframe) {
		setLayout(null);
		infoLabel.setText("기능 안내 라벨");
    	infoLabel.setFont(new Font("굴림", Font.PLAIN, 30));
    	infoLabel.setBounds(12, 10, 317, 47);
    	add(infoLabel);
    	logoutBtn.addActionListener(new ActionListener() {
    		public void actionPerformed(ActionEvent e) {
    			baseframe.change(baseframe.panel, baseframe.lgv);
    		}
    	});
    	
    	logoutBtn.setText("로그아웃");
    	logoutBtn.setBounds(1271, 27, 119, 23);
    	add(logoutBtn);
    	
    	
    	userInfoBtn.setText("사용자 정보");
    	userInfoBtn.setBounds(1136, 27, 123, 23);
    	userInfoBtn.addActionListener(new ActionListener() {
    		public void actionPerformed(ActionEvent e) {
    			baseframe.change(baseframe.panel, baseframe.uif);
    		}
    	});
    	add(userInfoBtn);
    	
    	setuserPanel();
	}
	public void setuserPanel() {
		userPanel=new JPanel();
		userPanel.setBounds(12, 78, 1378, 715);
		add(userPanel);
		userPanel.setLayout(null);
		
	}
}