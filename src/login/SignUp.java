package login;

import java.awt.*;
import javax.swing.*;

import app.*;

public class SignUp extends JPanel {
	public JTextField textField;
	public JTextField textField_1;
	public JTextField textField_2;

	public SignUp(BaseFrame baseframe) {
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

		JLabel lblNewLabel_3 = new JLabel("계정 정보 입력");
		lblNewLabel_3.setHorizontalAlignment(SwingConstants.CENTER);
		lblNewLabel_3.setBounds(510, 306, 350, 15);
		add(lblNewLabel_3);

		JLabel name_label = new JLabel("이름");
		name_label.setBounds(531, 331, 329, 15);
		add(name_label);

		textField = new JTextField();
		textField.setBounds(531, 356, 339, 21);
		add(textField);
		textField.setColumns(10);

		JLabel pw_label = new JLabel("비밀번호");
		pw_label.setBounds(531, 387, 329, 15);
		add(pw_label);

		textField_1 = new JTextField();
		textField_1.setColumns(10);
		textField_1.setBounds(531, 412, 339, 21);
		add(textField_1);

		JLabel code_label = new JLabel("verificatio code");
		code_label.setBounds(531, 443, 329, 15);
		add(code_label);

		textField_2 = new JTextField();
		textField_2.setColumns(10);
		textField_2.setBounds(531, 468, 339, 21);
		add(textField_2);

		JButton signup_btn = new JButton("가입하기");
		signup_btn.setBackground(new Color(0, 0, 0));
		signup_btn.setForeground(new Color(255, 255, 255));
		signup_btn.setBounds(528, 510, 95, 23);
		add(signup_btn);
	}
}
