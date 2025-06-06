package leader;

import java.awt.Color;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.util.List;

import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.border.LineBorder;
import javax.swing.border.TitledBorder;

import app.BaseFrame;
import common.model.Room;
import common.model.UserView;
import leader.TeamManagementService.TeamOperationResult;

public class LeaderView extends UserView {
	private JTextArea enabledRoomListArea;
	private JTextArea reservedRoomListArea;
	private JTextArea teamMemberArea;
	private final LeaderController controller;
	private final BaseFrame baseframe;

	public LeaderView(BaseFrame baseframe){
		super(baseframe);
		this.baseframe = baseframe;
		this.controller = new LeaderController();

		try {
			//안내 라벨 변경
			infoLabel.setText("회의실 예약");

			// ================ 회의실 예약 섹션 ================

			// 회의실 예약 제목
			JLabel roomManagementLabel = new JLabel("회의실 예약");
			roomManagementLabel.setFont(new Font("굴림", Font.BOLD, 20));
			roomManagementLabel.setBounds(12, 320, 150, 30);
			userPanel.add(roomManagementLabel);

			//안내 라벨
			JLabel infoLabel_2 = new JLabel("예약 가능한 회의실 목록");
			infoLabel_2.setFont(new Font("굴림", Font.PLAIN, 16));
			infoLabel_2.setBounds(12, 350, 276, 30);
			userPanel.add(infoLabel_2);

			//예약 가능한 회의실 목록 출력 필드
			enabledRoomListArea = new JTextArea();
			enabledRoomListArea.setEditable(false);
			enabledRoomListArea.setBorder(new LineBorder(Color.BLACK, 5));
			JScrollPane enabledScroll = new JScrollPane(enabledRoomListArea);
			enabledScroll.setBounds(12, 385, 500, 280);
			userPanel.add(enabledScroll);

			//회의실 예약 버튼
			JButton reserveRoomBtn = new JButton("회의실 예약하기");
			reserveRoomBtn.addActionListener((ActionEvent e) -> {
				handleRoomReservation();
			});
			reserveRoomBtn.setBounds(605, 385, 177, 23);
			userPanel.add(reserveRoomBtn);

			//현재 예약된 회의실 출력 필드
			JLabel infoLabel_3 = new JLabel("예약된 회의실");
			infoLabel_3.setFont(new Font("굴림", Font.PLAIN, 16));
			infoLabel_3.setBounds(866, 350, 276, 30);
			userPanel.add(infoLabel_3);

			reservedRoomListArea = new JTextArea();
			reservedRoomListArea.setEditable(false);
			reservedRoomListArea.setBorder(new LineBorder(Color.BLACK, 5));
			JScrollPane reservedScroll = new JScrollPane(reservedRoomListArea);
			reservedScroll.setBounds(866, 385, 500, 280);
			userPanel.add(reservedScroll);

			//예약 취소 버튼
			JButton cancelReservationBtn = new JButton("회의실 예약 취소하기");
			cancelReservationBtn.addActionListener((ActionEvent e) -> {
				handleReservationCancellation();
			});
			cancelReservationBtn.setBounds(605, 418, 177, 23);
			userPanel.add(cancelReservationBtn);

			//회의실 검색 버튼
			JButton searchRoomBtn = new JButton("회의실 목록 새로고침");
			searchRoomBtn.addActionListener((ActionEvent e) -> {
				// 회의실 목록을 새로고침
				loadAvailableRooms();
				loadReservedRooms();
				JOptionPane.showMessageDialog(this, "회의실 목록이 새로고침되었습니다.",
						"새로고침 완료", JOptionPane.INFORMATION_MESSAGE);
			});
			searchRoomBtn.setBounds(605, 451, 177, 23);
			userPanel.add(searchRoomBtn);

			// ================ 팀 관리 섹션 ================

			// 팀 관리 제목
			JLabel teamManagementLabel = new JLabel("팀 관리");
			teamManagementLabel.setFont(new Font("굴림", Font.BOLD, 20));
			teamManagementLabel.setBounds(12, 150, 100, 30);
			userPanel.add(teamManagementLabel);

			// 팀원 목록 표시 영역
			JLabel teamMemberLabel = new JLabel("팀원 목록");
			teamMemberLabel.setFont(new Font("굴림", Font.PLAIN, 16));
			teamMemberLabel.setBounds(12, 185, 100, 25);
			userPanel.add(teamMemberLabel);

			teamMemberArea = new JTextArea();
			teamMemberArea.setEditable(false);
			teamMemberArea.setFont(new Font("Monospaced", Font.PLAIN, 12));
			teamMemberArea.setBorder(new LineBorder(Color.DARK_GRAY, 2));
			JScrollPane teamScroll = new JScrollPane(teamMemberArea);
			teamScroll.setBounds(12, 215, 500, 120);
			userPanel.add(teamScroll);

			// 팀 관리 버튼들
			JButton addMemberBtn = new JButton("팀원 추가");
			addMemberBtn.addActionListener((ActionEvent e) -> {
				handleAddTeamMember();
			});
			addMemberBtn.setBounds(605, 215, 177, 25);
			userPanel.add(addMemberBtn);

			JButton removeMemberBtn = new JButton("팀원 제거");
			removeMemberBtn.addActionListener((ActionEvent e) -> {
				handleRemoveTeamMember();
			});
			removeMemberBtn.setBounds(605, 250, 177, 25);
			userPanel.add(removeMemberBtn);

			JButton refreshTeamBtn = new JButton("팀원 목록 새로고침");
			refreshTeamBtn.addActionListener((ActionEvent e) -> {
				loadTeamMembers();
			});
			refreshTeamBtn.setBounds(605, 285, 177, 25);
			userPanel.add(refreshTeamBtn);

			// 초기 데이터 로딩
			loadAvailableRooms();
			loadReservedRooms();
			loadTeamMembers(); // 권한 확인은 loadTeamMembers() 내부에서 처리

		} catch (Exception e) {
			System.err.println("LeaderView 초기화 중 오류: " + e.getMessage());
			e.printStackTrace();

			// 오류 발생 시 기본 메시지 표시
			JLabel errorLabel = new JLabel("회의실 예약 기능을 사용할 수 없습니다.");
			errorLabel.setBounds(12, 100, 400, 30);
			errorLabel.setForeground(Color.RED);
			userPanel.add(errorLabel);
		}
	}

	private void handleRoomReservation() {
		try {
			String roomIdStr = JOptionPane.showInputDialog(this,
					"예약할 회의실 번호를 입력하세요:",
					"회의실 예약", JOptionPane.QUESTION_MESSAGE);

			if (roomIdStr == null || roomIdStr.trim().isEmpty()) {
				return; // 사용자가 취소했거나 빈 값 입력
			}

			String peopleCountStr = JOptionPane.showInputDialog(this,
					"예약 인원을 입력하세요:",
					"예약 인원", JOptionPane.QUESTION_MESSAGE);

			if (peopleCountStr == null || peopleCountStr.trim().isEmpty()) {
				return;
			}

			int roomId = Integer.parseInt(roomIdStr.trim());
			int peopleCount = Integer.parseInt(peopleCountStr.trim());

			if (peopleCount <= 0) {
				JOptionPane.showMessageDialog(this, "예약 인원은 1명 이상이어야 합니다.",
						"입력 오류", JOptionPane.WARNING_MESSAGE);
				return;
			}

			// 현재 로그인한 사용자 확인
			if (baseframe.getCurrentUser() == null) {
				JOptionPane.showMessageDialog(this, "로그인 정보가 없습니다. 다시 로그인해주세요.",
						"인증 오류", JOptionPane.ERROR_MESSAGE);
				return;
			}

			int userId = baseframe.getCurrentUser().getId();
			boolean success = controller.reserveRoom(roomId, peopleCount, userId);

			if (success) {
				JOptionPane.showMessageDialog(this, "예약 성공!");
				loadAvailableRooms();
				loadReservedRooms();
			} else {
				JOptionPane.showMessageDialog(this,
						"예약 실패했습니다. 회의실 번호를 확인해주세요.",
						"예약 실패", JOptionPane.WARNING_MESSAGE);
			}

		} catch (NumberFormatException ex) {
			JOptionPane.showMessageDialog(this, "숫자를 정확히 입력해주세요.",
					"입력 오류", JOptionPane.ERROR_MESSAGE);
		} catch (Exception ex) {
			System.err.println("회의실 예약 중 오류: " + ex.getMessage());
			ex.printStackTrace();
			JOptionPane.showMessageDialog(this,
					"예약 처리 중 오류가 발생했습니다: " + ex.getMessage(),
					"오류", JOptionPane.ERROR_MESSAGE);
		}
	}

	private void handleReservationCancellation() {
		try {
			String roomIdStr = JOptionPane.showInputDialog(this,
					"취소할 회의실 번호를 입력하세요:",
					"예약 취소", JOptionPane.QUESTION_MESSAGE);

			if (roomIdStr == null || roomIdStr.trim().isEmpty()) {
				return;
			}

			int roomId = Integer.parseInt(roomIdStr.trim());
			boolean success = controller.cancelReservation(roomId);

			if (success) {
				JOptionPane.showMessageDialog(this, "예약 취소 완료!");
				loadAvailableRooms();
				loadReservedRooms();
			} else {
				JOptionPane.showMessageDialog(this,
						"예약 취소에 실패했습니다. 예약된 회의실인지 확인해주세요.",
						"취소 실패", JOptionPane.WARNING_MESSAGE);
			}

		} catch (NumberFormatException ex) {
			JOptionPane.showMessageDialog(this, "숫자를 정확히 입력해주세요.",
					"입력 오류", JOptionPane.ERROR_MESSAGE);
		} catch (Exception ex) {
			System.err.println("예약 취소 중 오류: " + ex.getMessage());
			ex.printStackTrace();
			JOptionPane.showMessageDialog(this,
					"예약 취소 중 오류가 발생했습니다: " + ex.getMessage(),
					"오류", JOptionPane.ERROR_MESSAGE);
		}
	}

	private void loadAvailableRooms() {
		try {
			List<Room> rooms = controller.getAvailableRooms();
			enabledRoomListArea.setText("");

			if (rooms.isEmpty()) {
				enabledRoomListArea.setText("예약 가능한 회의실이 없습니다.");
			} else {
				StringBuilder sb = new StringBuilder();
				for (Room room : rooms) {
					sb.append("번호: ").append(room.getId())
							.append(", 이름: ").append(room.getName())
							.append(", 정원: ").append(room.getCapacity()).append("명\n");
				}
				enabledRoomListArea.setText(sb.toString());
			}
		} catch (Exception e) {
			System.err.println("예약 가능 회의실 로딩 중 오류: " + e.getMessage());
			enabledRoomListArea.setText("회의실 정보를 불러올 수 없습니다.");
		}
	}

	private void loadReservedRooms() {
		try {
			List<Room> rooms = controller.getReservedRooms();
			reservedRoomListArea.setText("");

			if (rooms.isEmpty()) {
				reservedRoomListArea.setText("예약된 회의실이 없습니다.");
			} else {
				StringBuilder sb = new StringBuilder();
				for (Room room : rooms) {
					sb.append("번호: ").append(room.getId())
							.append(", 이름: ").append(room.getName())
							.append(", 정원: ").append(room.getCapacity()).append("명\n");
				}
				reservedRoomListArea.setText(sb.toString());
			}
		} catch (Exception e) {
			System.err.println("예약된 회의실 로딩 중 오류: " + e.getMessage());
			reservedRoomListArea.setText("예약 정보를 불러올 수 없습니다.");
		}
	}

	// ================ 팀 관리 메서드들 ================

	/**
	 * 팀원 목록 로딩 (권한 확인)
	 */
	private void loadTeamMembers() {
		try {
			// 권한 확인
			if (baseframe.getCurrentUser() == null) {
				if (teamMemberArea != null) {
					teamMemberArea.setText("로그인 정보가 없습니다.");
				}
				return;
			}

			String userRole = baseframe.getCurrentUser().getRole();
			if (!"leader".equals(userRole) && !"admin".equals(userRole)) {
				if (teamMemberArea != null) {
					teamMemberArea.setText("팀 관리 권한이 없습니다.");
				}
				return;
			}

			int teamId = baseframe.getCurrentUser().getTeamId();
			String memberInfo = controller.getTeamMemberInfo(teamId);
			if (teamMemberArea != null) {
				teamMemberArea.setText(memberInfo);
			}

		} catch (Exception e) {
			System.err.println("팀원 목록 로딩 중 오류: " + e.getMessage());
			if (teamMemberArea != null) {
				teamMemberArea.setText("팀원 정보를 불러올 수 없습니다.\n오류: " + e.getMessage());
			}
		}
	}

	/**
	 * 팀원 추가 처리
	 */
	private void handleAddTeamMember() {
		try {
			String username = JOptionPane.showInputDialog(this,
					"추가할 팀원의 사용자명을 입력하세요:",
					"팀원 추가", JOptionPane.QUESTION_MESSAGE);

			if (username == null || username.trim().isEmpty()) {
				return;
			}

			if (baseframe.getCurrentUser() == null) {
				JOptionPane.showMessageDialog(this, "로그인 정보가 없습니다.",
						"인증 오류", JOptionPane.ERROR_MESSAGE);
				return;
			}

			int teamId = baseframe.getCurrentUser().getTeamId();
			TeamOperationResult result = controller.addTeamMember(username.trim(), teamId);

			if (result.isSuccess()) {
				JOptionPane.showMessageDialog(this, result.getMessage(),
						"성공", JOptionPane.INFORMATION_MESSAGE);
				loadTeamMembers(); // 팀원 목록 새로고침
			} else {
				JOptionPane.showMessageDialog(this, result.getMessage(),
						"팀원 추가 실패", JOptionPane.WARNING_MESSAGE);
			}

		} catch (Exception e) {
			System.err.println("팀원 추가 중 오류: " + e.getMessage());
			JOptionPane.showMessageDialog(this,
					"팀원 추가 중 오류가 발생했습니다: " + e.getMessage(),
					"오류", JOptionPane.ERROR_MESSAGE);
		}
	}

	/**
	 * 팀원 제거 처리
	 */
	private void handleRemoveTeamMember() {
		try {
			String username = JOptionPane.showInputDialog(this,
					"제거할 팀원의 사용자명을 입력하세요:",
					"팀원 제거", JOptionPane.QUESTION_MESSAGE);

			if (username == null || username.trim().isEmpty()) {
				return;
			}

			// 확인 대화상자
			int confirmation = JOptionPane.showConfirmDialog(this,
					"정말로 '" + username.trim() + "' 사용자를 팀에서 제거하시겠습니까?\n" +
							"제거된 사용자는 무소속 상태가 되며, 관련 예약도 취소됩니다.",
					"팀원 제거 확인", JOptionPane.YES_NO_OPTION, JOptionPane.WARNING_MESSAGE);

			if (confirmation != JOptionPane.YES_OPTION) {
				return;
			}

			if (baseframe.getCurrentUser() == null) {
				JOptionPane.showMessageDialog(this, "로그인 정보가 없습니다.",
						"인증 오류", JOptionPane.ERROR_MESSAGE);
				return;
			}

			int teamId = baseframe.getCurrentUser().getTeamId();
			TeamOperationResult result = controller.removeTeamMember(username.trim(), teamId);

			if (result.isSuccess()) {
				JOptionPane.showMessageDialog(this, result.getMessage(),
						"성공", JOptionPane.INFORMATION_MESSAGE);
				loadTeamMembers(); // 팀원 목록 새로고침
				loadAvailableRooms(); // 회의실 목록도 새로고침 (예약 취소로 인해)
				loadReservedRooms();
			} else {
				JOptionPane.showMessageDialog(this, result.getMessage(),
						"팀원 제거 실패", JOptionPane.WARNING_MESSAGE);
			}

		} catch (Exception e) {
			System.err.println("팀원 제거 중 오류: " + e.getMessage());
			JOptionPane.showMessageDialog(this,
					"팀원 제거 중 오류가 발생했습니다: " + e.getMessage(),
					"오류", JOptionPane.ERROR_MESSAGE);
		}
	}
}