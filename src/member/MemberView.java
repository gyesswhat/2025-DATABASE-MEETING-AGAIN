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

		try {
			//안내 라벨 설정
			infoLabel.setText("회의 가능 시간 등록");

			//사용자 정보 버튼 추가
			add(userInfoBtn);

			//안내 라벨 추가
			JLabel addTimeLabel = new JLabel("가능 시간 등록 (우선순위 기반)");
			addTimeLabel.setFont(new Font("굴림", Font.PLAIN, 30));
			addTimeLabel.setHorizontalAlignment(SwingConstants.CENTER);
			addTimeLabel.setBounds(470, 135, 396, 47);
			userPanel.add(addTimeLabel);

			// 설명 라벨 추가
			JLabel descLabel = new JLabel("1~3순위로 회의 가능 시간을 등록해주세요");
			descLabel.setFont(new Font("굴림", Font.PLAIN, 14));
			descLabel.setHorizontalAlignment(SwingConstants.CENTER);
			descLabel.setBounds(470, 175, 396, 20);
			userPanel.add(descLabel);

			//날짜 선택
			JButton dateChoiceBtn = new JButton("날짜 선택");
			dateChoiceBtn.setBounds(470, 205, 192, 23);
			dateChoiceBtn.addActionListener(e -> {
				try {
					String inputDate = JOptionPane.showInputDialog(this,
							"2000-01-01 형식으로 날짜를 입력하세요",
							"날짜 입력", JOptionPane.QUESTION_MESSAGE);

					if (inputDate != null && !inputDate.trim().isBlank()) {
						// 날짜 형식 간단 검증
						if (inputDate.matches("\\d{4}-\\d{2}-\\d{2}")) {
							date = inputDate.trim();
							addTime(new JLabel(), "날짜: " + date);
						} else {
							JOptionPane.showMessageDialog(this,
									"날짜 형식이 올바르지 않습니다. YYYY-MM-DD 형식으로 입력하세요.",
									"입력 오류", JOptionPane.ERROR_MESSAGE);
						}
					}
				} catch (Exception ex) {
					System.err.println("날짜 선택 중 오류: " + ex.getMessage());
					JOptionPane.showMessageDialog(this,
							"날짜 선택 중 오류가 발생했습니다.",
							"오류", JOptionPane.ERROR_MESSAGE);
				}
			});
			userPanel.add(dateChoiceBtn);

			//시간 및 선호도 선택
			JButton timeChoiceBtn = new JButton("시간 및 선호도 입력");
			timeChoiceBtn.setBounds(674, 205, 192, 23);
			timeChoiceBtn.addActionListener(e -> handleTimeInput());
			userPanel.add(timeChoiceBtn);

			addTimePanel.setBorder(new LineBorder(Color.BLACK, 5));
			addTimePanel.setBounds(470, 240, 396, 252);
			addTimePanel.setLayout(new GridLayout(0, 1, 0, 2));
			userPanel.add(addTimePanel);

		} catch (Exception e) {
			System.err.println("MemberView 초기화 중 오류: " + e.getMessage());
			e.printStackTrace();

			// 오류 발생 시 기본 메시지 표시
			JLabel errorLabel = new JLabel("회의 시간 등록 기능을 사용할 수 없습니다.");
			errorLabel.setBounds(12, 100, 400, 30);
			errorLabel.setForeground(Color.RED);
			userPanel.add(errorLabel);
		}
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
				String priorityText = priority + "순위";
				addTime(new JLabel(), date + " " + time + " (" + priorityText + ")");
				JOptionPane.showMessageDialog(this,
						"회의 가능 시간이 " + priorityText + "로 등록되었습니다.");
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

	// 시간 유효성 검사 (HH:MM 형식이 유효한 시간인지 확인)
	private boolean isValidTime(String timeStr) {
		try {
			String[] parts = timeStr.split(":");
			if (parts.length != 2) return false;

			int hour = Integer.parseInt(parts[0]);
			int minute = Integer.parseInt(parts[1]);

			return (hour >= 0 && hour <= 23) && (minute >= 0 && minute <= 59);
		} catch (NumberFormatException e) {
			return false;
		}
	}

	//textArea에 추가한 시간 보이도록
	public void addTime(JLabel label, String timeText) {
		try {
			label.setText(timeText);
			label.setBorder(BorderFactory.createLineBorder(Color.GRAY));
			label.setHorizontalAlignment(SwingConstants.CENTER);
			addTimePanel.add(label);
			addTimePanel.revalidate();
			addTimePanel.repaint();
		} catch (Exception e) {
			System.err.println("시간 표시 중 오류: " + e.getMessage());
		}
	}
}