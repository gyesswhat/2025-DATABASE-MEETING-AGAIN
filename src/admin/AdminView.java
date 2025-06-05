package admin;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;

import app.BaseFrame;

public class AdminView extends JPanel {
	protected final BaseFrame baseframe;
	protected JPanel adminPanel;

	public AdminView(BaseFrame baseframe) {
		this.baseframe = baseframe;
		setLayout(null);

		JButton dashBtn = new JButton("대시보드");
		dashBtn.setBounds(12, 32, 200, 23);
		dashBtn.addActionListener(e -> baseframe.change(baseframe.panel, baseframe.dbv));
		add(dashBtn);

		JButton roomManageBtn = new JButton("회의실 관리");
		roomManageBtn.setBounds(12, 65, 200, 23);
		roomManageBtn.addActionListener(e -> baseframe.change(baseframe.panel, baseframe.rmv));
		add(roomManageBtn);

		JButton logoutBtn = new JButton("로그아웃");
		logoutBtn.setBounds(1104, 32, 95, 23);
		logoutBtn.addActionListener(e -> baseframe.change(baseframe.panel, baseframe.lgv));
		add(logoutBtn);

		JLabel title = new JLabel("관리자 대시보드");
		title.setFont(new Font("굴림", Font.PLAIN, 25));
		title.setBounds(315, 14, 357, 52);
		add(title);

		adminPanel = new JPanel();
		adminPanel.setBounds(309, 78, 900, 600);
		adminPanel.setLayout(null);
		add(adminPanel);
	}
}
