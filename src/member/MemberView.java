package member;

import java.awt.*;
import java.awt.event.*;
import javax.swing.*;
import javax.swing.border.LineBorder;

import app.BaseFrame;
import common.model.UserView;
import common.model.User;

public class MemberView extends UserView {
	private String date, time;
	private final BaseFrame baseframe;
	private final JPanel addTimePanel = new JPanel();
	private final MemberController controller;

	public MemberView(BaseFrame baseframe) {
		super(baseframe);
		this.baseframe = baseframe;
		this.controller = new MemberController();

		setLayout(null);
		infoLabel.setText("회의 가능 시간 등록");

		JLabel addTimeLabel = new JLabel("가능 시간 등록");
		addTimeLabel.setFont(new Font("굴림", Font.PLAIN, 30));
		addTimeLabel.setHorizontalAlignment(SwingConstants.CENTER);
		addTimeLabel.setBounds(470, 135, 396, 47);
		userPanel.add(addTimeLabel);

		JButton dateChoiceBtn = new JButton("날짜 선택");
		dateChoiceBtn.setBounds(470, 192, 192, 23);
		dateChoiceBtn.addActionListener(e -> {
			date = JOptionPane.showInputDialog("2000-01-01 형식으로 날짜를 입력하세요", null);
			if (date != null && !date.isBlank()) {
				addTime(new JLabel(), "날짜: " + date);
			}
		});
		userPanel.add(dateChoiceBtn);

		JButton timeChoiceBtn = new JButton("시간 선택");
		timeChoiceBtn.setBounds(674, 192, 192, 23);
		timeChoiceBtn.addActionListener(e -> handleTimeInput());
		userPanel.add(timeChoiceBtn);

		addTimePanel.setBorder(new LineBorder(Color.BLACK, 5));
		addTimePanel.setBounds(470, 225, 396, 267);
		addTimePanel.setLayout(new GridLayout(0, 2, 0, 4));
		userPanel.add(addTimePanel);
	}

	private void handleTimeInput() {
		if (date == null || date.isBlank()) {
			JOptionPane.showMessageDialog(null, "날짜를 먼저 입력하세요", "", JOptionPane.ERROR_MESSAGE);
			return;
		}

		time = JOptionPane.showInputDialog("00:00-23:59 형식으로 시간을 입력하세요", null);
		if (time == null || !time.matches("\\d{2}:\\d{2}-\\d{2}:\\d{2}")) {
			JOptionPane.showMessageDialog(null, "시간 형식이 올바르지 않습니다", "", JOptionPane.ERROR_MESSAGE);
			return;
		}

		String[] parts = time.split("-");
		String start = parts[0];
		String end = parts[1];

		User currentUser = baseframe.getCurrentUser();
		if (currentUser == null) {
			JOptionPane.showMessageDialog(null, "로그인 정보가 없습니다", "", JOptionPane.ERROR_MESSAGE);
			return;
		}

		try {
			boolean saved = controller.saveTimePreference(currentUser, date, start, end, 1);
			if (saved) {
				addTime(new JLabel(), date + " " + time);
				JOptionPane.showMessageDialog(null, "회의 가능 시간이 등록되었습니다.");
			} else {
				JOptionPane.showMessageDialog(null, "저장에 실패했습니다.");
			}
		} catch (Exception ex) {
			JOptionPane.showMessageDialog(null, "오류: " + ex.getMessage());
			ex.printStackTrace();
		} finally {
			date = null;
			time = null;
		}
	}

	public void addTime(JLabel label, String timeText) {
		label.setText(timeText);
		addTimePanel.add(label);
		addTimePanel.revalidate();
		addTimePanel.repaint();
	}
}
