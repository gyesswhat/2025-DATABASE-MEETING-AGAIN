package admin;

import javax.swing.*;
import javax.swing.border.TitledBorder;

import app.BaseFrame;

public class DashBoard extends AdminView {
	public DashBoard(BaseFrame baseframe) {
		super(baseframe);

		JLabel headLabel = new JLabel("대시보드");
		headLabel.setBounds(12, 5, 134, 30);
		adminPanel.add(headLabel);

		addStatLabel("전체 회의실 수", 12, 60);
		addStatLabel("전체 사용자 수", 309, 60);
		addStatLabel("오늘 예약 건수", 606, 60);
	}

	private void addStatLabel(String title, int x, int y) {
		JLabel label = new JLabel("데이터 로딩 중...");
		label.setBounds(x, y, 285, 64);
		TitledBorder border = new TitledBorder(title);
		border.setTitlePosition(TitledBorder.ABOVE_TOP);
		label.setBorder(border);
		adminPanel.add(label);
	}
	
	
}
