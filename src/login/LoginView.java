package login;

import java.awt.*;
import java.awt.event.*;
import javax.swing.*;
import javax.swing.border.TitledBorder;

import admin.*;
import leader.*;
import login.*;
import member.*;
import app.*;

public class LogInView extends JPanel {
	private JTextField textField;

	public LogInView(BaseFrame baseframe) {
	
		
		JLabel lblNewLabel_1 = new JLabel("회의실 예약 시스템에 로그인하세요");
		lblNewLabel_1.setHorizontalAlignment(SwingConstants.CENTER);
		lblNewLabel_1.setBounds(532, 348, 279, 15);
		add(lblNewLabel_1);

		JLabel lblNewLabel = new JLabel("로그인");
		lblNewLabel.setHorizontalAlignment(SwingConstants.CENTER);
		lblNewLabel.setBounds(532, 323, 279, 15);
		add(lblNewLabel);

		textField = new JTextField();
		textField.setBounds(532, 373, 279, 21);
		textField.setColumns(10);
		add(textField);

		JButton login_btn = new JButton("로그인");
		login_btn.setForeground(new Color(255, 255, 255));
		login_btn.setBackground(new Color(0, 0, 0));
		login_btn.setBounds(532, 404, 279, 23);
		add(login_btn);

		JButton signup_btn = new JButton("회원가입");
		signup_btn.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				baseframe.change(baseframe.panel, baseframe.suv);
			}
		});
		signup_btn.setBounds(532, 454, 279, 23);
		add(signup_btn);

		JLabel lblNewLabel_2 = new JLabel(
				"-----------------------------------------------------------------------------");
		lblNewLabel_2.setBackground(new Color(0, 0, 0));
		lblNewLabel_2.setFont(new Font("굴림", Font.BOLD, 5));
		lblNewLabel_2.setHorizontalAlignment(SwingConstants.CENTER);
		lblNewLabel_2.setBounds(532, 437, 279, 15);
		add(lblNewLabel_2);
	}
}