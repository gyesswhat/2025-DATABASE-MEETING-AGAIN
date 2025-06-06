package login;

import java.awt.*;
import java.awt.event.*;
import javax.swing.*;
import javax.swing.border.TitledBorder;
import app.*;
import leader.LeaderView;
import member.MemberView;

public class LoginView extends JPanel {
	private JTextField textField;
	private JTextField textField_1;
	
	public LoginView(BaseFrame baseframe) {
		
		//안내 라벨
		JLabel lblNewLabel_1 = new JLabel("회의실 예약 시스템에 로그인하세요");
		lblNewLabel_1.setHorizontalAlignment(SwingConstants.CENTER);
		lblNewLabel_1.setBounds(532, 348, 279, 15);
		add(lblNewLabel_1);
		
		JLabel lblNewLabel = new JLabel("로그인");
		lblNewLabel.setHorizontalAlignment(SwingConstants.CENTER);
		lblNewLabel.setBounds(532, 323, 279, 15);
		add(lblNewLabel);

		//아이디 입력 필드
		textField = new JTextField();
		textField.setBounds(532, 373, 279, 40);
		textField.setColumns(10);
		TitledBorder idBorder=new TitledBorder("ID");
		idBorder.setTitlePosition(TitledBorder.ABOVE_TOP);
		idBorder.setTitleJustification(TitledBorder.LEADING);
		textField.setBorder(idBorder);
		add(textField);
		
		//비밀번호 입력 필드
		textField_1 = new JTextField();
		textField_1.setBounds(532, 423, 279, 40);
		textField_1.setColumns(10);
		TitledBorder pwBorder=new TitledBorder("PW");
		pwBorder.setTitlePosition(TitledBorder.ABOVE_TOP);
		pwBorder.setTitleJustification(TitledBorder.LEADING);
		textField_1.setBorder(pwBorder);
		add(textField_1);
		
		//로그인 버튼
		JButton login_btn = new JButton("로그인");
		login_btn.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				//로그인 함수 실행
				String username=textField.getText();
				String password=textField_1.getText();
				baseframe.loginResult=baseframe.loginController.login(username, password);
				if(baseframe.loginResult.isSuccess()) {
					JOptionPane.showMessageDialog(null, baseframe.loginResult.getMessage());
					baseframe.setCurrentUser(baseframe.loginResult.getUser());
					baseframe.uif=new UserInfo(baseframe);
					baseframe.lv=new LeaderView(baseframe);
					baseframe.mv=new MemberView(baseframe);
					if(baseframe.getCurrentUser().getRole().equals("admin"))
						baseframe.change(baseframe.panel, baseframe.dbv);
					else baseframe.change(baseframe.panel, baseframe.uif);
				}
				else JOptionPane.showMessageDialog(null, baseframe.loginResult.getMessage(), "Message", JOptionPane.ERROR_MESSAGE);
			}
		});
		login_btn.setForeground(new Color(255, 255, 255));
		login_btn.setBackground(new Color(0, 0, 0));
		login_btn.setBounds(532, 473, 279, 21);
		add(login_btn);
		
		//회원가입 버튼
		JButton signup_btn = new JButton("회원가입");
		signup_btn.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				//회원가입 페이지로 이동
				baseframe.change(baseframe.panel, baseframe.suv);
			}
		});
		signup_btn.setBounds(532, 523, 279, 21);
		add(signup_btn);
	}
}