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

import app.BaseFrame;
import common.model.Room;
import common.model.UserView;

public class LeaderView extends UserView {
	private JTextArea enabledRoomListArea;
	private JTextArea reservedRoomListArea;
	private final LeaderController controller;

	public LeaderView(BaseFrame baseframe){
		super(baseframe);
		this.controller = new LeaderController();
		
		//안내 라벨 추가
		infoLabel.setText("회의실 예약");
		
		//안내 라벨
		JLabel infoLabel_2 = new JLabel("예약 가능한 회의실 목록");
		infoLabel_2.setFont(new Font("굴림", Font.PLAIN, 24));
		infoLabel_2.setBounds(12, 10, 276, 50);
		userPanel.add(infoLabel_2);
		
		//예약 가능한 회의실 목록 출력 필드
		enabledRoomListArea = new JTextArea();
		enabledRoomListArea.setEditable(false);
		enabledRoomListArea.setBorder(new LineBorder(Color.BLACK, 5));
		JScrollPane enabledScroll = new JScrollPane(enabledRoomListArea);
		enabledScroll.setBounds(12, 50, 500, 655);
		userPanel.add(enabledScroll);
		
		//회의실 예약 버튼
		JButton reserveRoomBtn = new JButton("회의실 예약하기");
		reserveRoomBtn.addActionListener((ActionEvent e) -> {
			String reservedRoomNumStr = JOptionPane.showInputDialog("예약할 회의실 번호를 입력하세요");
			String people = JOptionPane.showInputDialog("예약 인원을 입력하세요");

			try {
				int reservedRoomNum = Integer.parseInt(reservedRoomNumStr);
				int reservedPeopleNum = Integer.parseInt(people);
				int userId = baseframe.getCurrentUser().getId(); // 현재 로그인한 사용자

				boolean success = controller.reserveRoom(reservedRoomNum, reservedPeopleNum, userId);
				if (success) {
					JOptionPane.showMessageDialog(null, "예약 성공!");
					loadAvailableRooms();
					loadReservedRooms();
				} else {
					JOptionPane.showMessageDialog(null, "예약 실패");
				}
			} catch (NumberFormatException ex) {
				JOptionPane.showMessageDialog(null, "숫자를 입력하세요");
			} catch (NullPointerException ex) {
				JOptionPane.showMessageDialog(null, "로그인이 필요합니다");
			}
		});
		reserveRoomBtn.setBounds(605, 50, 177, 23);
		userPanel.add(reserveRoomBtn);
		
		//현재 예약된 회의실 출력 필드
		JLabel infoLabel_3 = new JLabel("예약된 회의실");
		infoLabel_3.setFont(new Font("굴림", Font.PLAIN, 24));
		infoLabel_3.setBounds(866, 10, 276, 50);
		userPanel.add(infoLabel_3);

		reservedRoomListArea = new JTextArea();
		reservedRoomListArea.setEditable(false);
		reservedRoomListArea.setBorder(new LineBorder(Color.BLACK, 5));
		JScrollPane reservedScroll = new JScrollPane(reservedRoomListArea);
		reservedScroll.setBounds(866, 50, 500, 655);
		userPanel.add(reservedScroll);
		
		//예약 취소 버튼
		JButton cancelReservationBtn = new JButton("회의실 예약 취소하기");
		cancelReservationBtn.addActionListener((ActionEvent e) -> {
			String deletedRoomNumStr = JOptionPane.showInputDialog("삭제할 회의실 번호를 입력하세요");

			try {
				int deletedRoomNum = Integer.parseInt(deletedRoomNumStr);
				boolean success = controller.cancelReservation(deletedRoomNum);

				if (success) {
					JOptionPane.showMessageDialog(null, "예약 취소 완료!");
					loadAvailableRooms();
					loadReservedRooms();
				} else {
					JOptionPane.showMessageDialog(null, "예약 취소 실패");
				}
			} catch (NumberFormatException ex) {
				JOptionPane.showMessageDialog(null, "숫자를 입력하세요");
			}
		});
		cancelReservationBtn.setBounds(605, 83, 177, 23);
		userPanel.add(cancelReservationBtn);
		
		//회의실 검색 버튼
		JButton searchRoomBtn = new JButton("회의실 검색하기");
		searchRoomBtn.addActionListener((ActionEvent e) -> {
			loadAvailableRooms();
		});
		searchRoomBtn.setBounds(605, 116, 177, 23);
		userPanel.add(searchRoomBtn);

		// 초기 데이터 로딩
		loadAvailableRooms();
		loadReservedRooms();
	}

	private void loadAvailableRooms() {
		List<Room> rooms = controller.getAvailableRooms();
		enabledRoomListArea.setText(rooms.isEmpty() ? "예약 가능한 회의실이 없습니다." : "");
		for (Room room : rooms) {
			enabledRoomListArea.append("번호: " + room.getId() + ", 이름: " + room.getName() + ", 정원: " + room.getCapacity() + "명\n");
		}
	}

	private void loadReservedRooms() {
		List<Room> rooms = controller.getReservedRooms();
		reservedRoomListArea.setText(rooms.isEmpty() ? "예약된 회의실이 없습니다." : "");
		for (Room room : rooms) {
			reservedRoomListArea.append("번호: " + room.getId() + ", 이름: " + room.getName() + ", 정원: " + room.getCapacity() + "명\n");
		}
	}
}