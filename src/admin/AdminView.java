package admin;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import app.*;

public class AdminView extends JPanel{
	public JPanel adminPanel;
    public AdminView(BaseFrame baseframe){
    	setLayout(null);
		JButton btnNewButton = new JButton("대시보드");
		btnNewButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				baseframe.change(baseframe.panel, baseframe.dbv);
			}
		});
		btnNewButton.setBounds(12, 32, 200, 23);
		add(btnNewButton);
		
		JButton btnNewButton_1 = new JButton("회의실 관리");
		btnNewButton_1.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				baseframe.change(baseframe.panel, baseframe.rmv);
			}
		});
		btnNewButton_1.setBounds(12, 65, 200, 23);
		add(btnNewButton_1);
		
		JLabel lblNewLabel = new JLabel("관리자 대시보드");
		lblNewLabel.setFont(new Font("굴림", Font.PLAIN, 25));
		lblNewLabel.setBounds(315, 14, 357, 52);
		add(lblNewLabel);
		
		JButton btnNewButton_2 = new JButton("로그아웃");
		btnNewButton_2.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				baseframe.change(baseframe.panel, baseframe.lgv);
			}
		});
		btnNewButton_2.setBounds(1104, 32, 95, 23);
		add(btnNewButton_2);
		
		setadminPanel();
    }
   public void setadminPanel() {
	   adminPanel=new JPanel();
	   adminPanel.setBounds(309, 78, 900, 467);
	   add(adminPanel);
	   adminPanel.setLayout(null);
   }
}