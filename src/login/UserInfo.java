package login;

import java.awt.*;
import javax.swing.*;
import javax.swing.border.TitledBorder;

import app.*;

public class UserInfo extends JPanel {
	private JTextField pwNow;
	private JTextField pwNew;
	private JTextField pwCheck;

	public UserInfo(BaseFrame baserframe) {
		JLabel headLabel = new JLabel("사용자 정보");
		headLabel.setFont(new Font("굴림", Font.PLAIN, 25));
		headLabel.setForeground(new Color(0, 0, 0));
		headLabel.setBounds(54, 41, 178, 37);
		add(headLabel);

		JLabel img = new JLabel("사진");
		img.setBounds(54, 88, 52, 53);
		add(img);

		JLabel nameInfo = new JLabel("김이화");
		nameInfo.setBounds(118, 88, 188, 53);
		TitledBorder nameborder = new TitledBorder("이름");
		nameborder.setTitlePosition(TitledBorder.ABOVE_TOP);
		nameborder.setTitleJustification(TitledBorder.LEADING);
		nameInfo.setBorder(nameborder);
		add(nameInfo);

		JLabel infoLabel = new JLabel("팀 정보 및 변경");
		infoLabel.setFont(new Font("굴림", Font.PLAIN, 20));
		infoLabel.setBounds(54, 173, 178, 37);
		add(infoLabel);

		JButton teamChangeBtn = new JButton("팀 변경");
		teamChangeBtn.setBounds(54, 220, 252, 23);
		add(teamChangeBtn);

		JLabel infoLabel2 = new JLabel("비밀번호 변경");
		infoLabel2.setFont(new Font("굴림", Font.PLAIN, 20));
		infoLabel2.setBounds(54, 317, 178, 37);
		add(infoLabel2);

		JLabel pwNowLabel = new JLabel("현재 비밀번호");
		pwNowLabel.setBounds(54, 370, 252, 15);
		add(pwNowLabel);

		pwNow = new JTextField();
		pwNowLabel.setLabelFor(pwNow);
		pwNow.setBounds(54, 395, 252, 21);
		add(pwNow);
		pwNow.setColumns(10);

		pwNew = new JTextField();
		pwNew.setColumns(10);
		pwNew.setBounds(54, 451, 252, 21);
		add(pwNew);

		JLabel pwNewLabel = new JLabel("새 비밀번호");
		pwNewLabel.setLabelFor(pwNew);
		pwNewLabel.setBounds(54, 426, 252, 15);
		add(pwNewLabel);

		pwCheck = new JTextField();
		pwCheck.setColumns(10);
		pwCheck.setBounds(54, 507, 252, 21);
		add(pwCheck);

		JLabel pwCheckLabel = new JLabel("비밀번호 확인");
		pwCheckLabel.setLabelFor(pwCheckLabel);
		pwCheckLabel.setBounds(54, 482, 252, 15);
		add(pwCheckLabel);

		JButton storeBtn = new JButton("저장");
		storeBtn.setBounds(54, 538, 121, 23);
		add(storeBtn);

		JButton cancelBtn = new JButton("취소");
		cancelBtn.setBounds(187, 538, 121, 23);
		add(cancelBtn);
	}
}
