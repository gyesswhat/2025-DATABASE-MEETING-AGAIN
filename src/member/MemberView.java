package member;
import java.awt.*;
import java.awt.event.*;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import javax.swing.*;
import javax.swing.border.LineBorder;
import app.BaseFrame;
import common.model.UserView;
import common.util.DBUtil;
import common.model.User;
import java.util.*;
import java.util.List;

public class MemberView extends UserView {
	private String date, time;
	private final BaseFrame baseframe;

	private final JTextArea addTimeArea = new JTextArea();
	private MemberController controller= new MemberController();
	
	public MemberView(BaseFrame baseframe) {
		super(baseframe);
		this.baseframe = baseframe;
		
		//setLayout(null);
		//안내 라벨 설정
		infoLabel.setText("회의 가능 시간 등록");
		
		//사용자 정보 버튼 추가
		add(userInfoBtn);
		
		//안내 라벨 추가
		JLabel addTimeLabel = new JLabel("가능 시간 등록");
		addTimeLabel.setFont(new Font("굴림", Font.PLAIN, 30));
		addTimeLabel.setHorizontalAlignment(SwingConstants.CENTER);
		addTimeLabel.setBounds(470, 135, 396, 47);
		userPanel.add(addTimeLabel);
		
		//날짜 선택
		JButton dateChoiceBtn = new JButton("날짜 및 시간 선택");
		dateChoiceBtn.setBounds(470, 192, 394, 23);
		dateChoiceBtn.addActionListener(e -> {
			date = JOptionPane.showInputDialog("2000-01-01 형식으로 날짜를 입력하세요", null);
			handleTimeInput();
		});
		userPanel.add(dateChoiceBtn);

		addTimeArea.setBorder(new LineBorder(Color.BLACK, 5));
		addTimeArea.setBounds(470, 225, 396, 267);
		addTimeArea.setEditable(false);
		userPanel.add(addTimeArea);
		loadTime(user.getUsername());
	}

	private void handleTimeInput() {
		try {
			if (date == null || date.isBlank()) {
				JOptionPane.showMessageDialog(this, "날짜를 먼저 입력하세요",
						"입력 순서 오류", JOptionPane.WARNING_MESSAGE);
				return;
			}

			// 시간 입력 패널 생성
			JPanel timeInputPanel = new JPanel(new GridLayout(3, 2, 5, 5));

			JLabel timeLabel = new JLabel("시간 (예: 09:00-10:30):");
			JTextField timeField = new JTextField();

			JLabel priorityLabel = new JLabel("선호도 (1~3순위):");
			JComboBox<String> priorityCombo = new JComboBox<>(new String[]{"1순위 (가장 선호)", "2순위", "3순위"});

			timeInputPanel.add(timeLabel);
			timeInputPanel.add(timeField);
			timeInputPanel.add(priorityLabel);
			timeInputPanel.add(priorityCombo);

			int result = JOptionPane.showConfirmDialog(this, timeInputPanel,
					"회의 가능 시간 등록", JOptionPane.OK_CANCEL_OPTION, JOptionPane.QUESTION_MESSAGE);

			if (result != JOptionPane.OK_OPTION) {
				return; // 사용자가 취소
			}

			String inputTime = timeField.getText().trim();
			if (inputTime.isEmpty()) {
				JOptionPane.showMessageDialog(this, "시간을 입력해주세요.",
						"입력 오류", JOptionPane.WARNING_MESSAGE);
				return;
			}

			time = inputTime;

			if (!time.matches("\\d{2}:\\d{2}-\\d{2}:\\d{2}")) {
				JOptionPane.showMessageDialog(this,
						"시간 형식이 올바르지 않습니다.\n00:00-23:59 형식으로 입력하세요.",
						"형식 오류", JOptionPane.ERROR_MESSAGE);
				return;
			}

			String[] parts = time.split("-");
			String start = parts[0];
			String end = parts[1];

			// 시간 유효성 검사
			if (!isValidTime(start) || !isValidTime(end)) {
				JOptionPane.showMessageDialog(this,
						"유효하지 않은 시간입니다. 00:00-23:59 범위로 입력하세요.",
						"시간 오류", JOptionPane.ERROR_MESSAGE);
				return;
			}

			User currentUser = baseframe.getCurrentUser();
			if (currentUser == null) {
				JOptionPane.showMessageDialog(this,
						"로그인 정보가 없습니다. 다시 로그인해주세요.",
						"인증 오류", JOptionPane.ERROR_MESSAGE);
				return;
			}

			// 선호도 값 가져오기 (1, 2, 3)
			int priority = priorityCombo.getSelectedIndex() + 1;

			boolean saved = controller.saveTimePreference(currentUser, date, start, end, priority);
			if (saved) {
				loadTime(user.getUsername());
				JOptionPane.showMessageDialog(null, "회의 가능 시간이 등록되었습니다.");

			} else {
				JOptionPane.showMessageDialog(this,
						"저장에 실패했습니다. 다시 시도해주세요.",
						"저장 실패", JOptionPane.ERROR_MESSAGE);
			}

		} catch (Exception ex) {
			System.err.println("시간 입력 처리 중 오류: " + ex.getMessage());
			ex.printStackTrace();
			JOptionPane.showMessageDialog(this,
					"시간 등록 중 오류가 발생했습니다: " + ex.getMessage(),
					"오류", JOptionPane.ERROR_MESSAGE);
		} finally {
			// 입력 완료 후 초기화
			date = null;
			time = null;
		}
	}

	private boolean isValidTime(String start) {
		// TODO Auto-generated method stub
		
		return false;
	}

	public void loadTime(String username) {
		// 사용자의 정보를 하나의 string으로 만들어서 리턴
		String sql="SELECT startTime, endTime FROM DB2025_timeslot_summary_view WHERE username=?";
		try (Connection conn = DBUtil.getConnection();
				PreparedStatement stmt = conn.prepareStatement(sql);){
			stmt.setString(1, username);
			ResultSet rs=stmt.executeQuery();
			StringBuilder sb = new StringBuilder();;
			while (rs.next()) {
				sb.append(rs.getString("startTime")).append("\t").append(rs.getString("endTime")).append("\n");
			}
			addTimeArea.setText(sb.toString());
			
		}catch (SQLException e) {
			e.printStackTrace();
		}
	}


}