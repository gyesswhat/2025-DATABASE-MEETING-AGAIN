package login;

import java.awt.*;
import javax.swing.*;
import javax.swing.border.TitledBorder;

import app.*;
import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;

public class SignUp extends JPanel {
	public JTextField textField;
	public JTextField textField_1;
	public JTextField textField_2;

	public SignUp(BaseFrame baseframe) {
		//회원 유형 선택
		JLabel lblNewLabel = new JLabel("회원 유형 선택");
		lblNewLabel.setHorizontalAlignment(SwingConstants.CENTER);
		lblNewLabel.setBounds(531, 176, 329, 15);
		add(lblNewLabel);
		
		JRadioButton member = new JRadioButton("일반 회원");
		member.setBounds(528, 197, 119, 23);
		add(member);

		JRadioButton leader = new JRadioButton("팀 리더");
		leader.setBounds(698, 197, 119, 23);
		add(leader);
		
		ButtonGroup group = new ButtonGroup();
		group.add(member);
		group.add(leader);
		
		//회원 유형에 따른 기능 안내
		JTextArea memebr_info = new JTextArea("팀에 소속된 일반 구성원\n회의실 예약 및 일정 \n제안 가능");
		memebr_info.setFont(new Font("Monospaced", Font.BOLD, 13));
		memebr_info.setEnabled(false);
		memebr_info.setEditable(false);
		memebr_info.setBounds(528, 226, 162, 70);
		add(memebr_info);

		JTextArea leader_info = new JTextArea("팀을 관리하는 책임자\n팀원 관리 및 회의 일정 \n조율 가능");
		leader_info.setFont(new Font("Monospaced", Font.BOLD, 13));
		leader_info.setEnabled(false);
		leader_info.setEditable(false);
		leader_info.setBounds(708, 226, 162, 70);
		add(leader_info);
		
		//회원 정보 입력 안내
		JLabel lblNewLabel_3 = new JLabel("계정 정보 입력");
		lblNewLabel_3.setHorizontalAlignment(SwingConstants.CENTER);
		lblNewLabel_3.setBounds(510, 316, 350, 15);
		add(lblNewLabel_3);
		
		//아이디 입력 필드
		textField = new JTextField();
		textField.setBounds(531, 356, 339, 40);
		textField.setColumns(10);
		TitledBorder idBorder=new TitledBorder("ID");
		idBorder.setTitlePosition(TitledBorder.ABOVE_TOP);
		idBorder.setTitleJustification(TitledBorder.LEADING);
		textField.setBorder(idBorder);
		add(textField);
		
		//비밀번호 입력 필드
		textField_1 = new JTextField();
		textField_1.setColumns(10);
		textField_1.setBounds(531, 412, 339, 40);
		TitledBorder pwBorder=new TitledBorder("PW");
		pwBorder.setTitlePosition(TitledBorder.ABOVE_TOP);
		pwBorder.setTitleJustification(TitledBorder.LEADING);
		textField_1.setBorder(pwBorder);
		add(textField_1);
		
		//팀이름 입력 필드
		textField_2 = new JTextField();
		textField_2.setColumns(10);
		textField_2.setBounds(531, 468, 339, 40);
		TitledBorder codeBorder=new TitledBorder("팀 이름");
		codeBorder.setTitlePosition(TitledBorder.ABOVE_TOP);
		codeBorder.setTitleJustification(TitledBorder.LEADING);
		textField_2.setBorder(codeBorder);
		add(textField_2);
		
		//가입 버튼
		JButton signup_btn = new JButton("가입하기");
		signup_btn.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				//가입하기 함수 수행
				String username=textField.getText();
				String password=textField_1.getText();
				String teamname=textField_2.getText();
				String role;
				if(member.isSelected()) role="member";
				else role="leader";
				
				RegisterResult registerResult=baseframe.loginController.register(username, password, role, teamname);
			}
		});
		signup_btn.setBackground(new Color(0, 0, 0));
		signup_btn.setForeground(new Color(255, 255, 255));
		signup_btn.setBounds(531, 528, 95, 23);
		add(signup_btn);
	}
}
