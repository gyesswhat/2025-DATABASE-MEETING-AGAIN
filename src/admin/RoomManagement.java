package admin;

import javax.swing.*;
import javax.swing.border.LineBorder;
import java.awt.*;
import java.util.List;
import java.lang.StringBuilder;

import app.BaseFrame;
import common.model.Room;

public class RoomManagement extends AdminView {
	private JTextArea roomListArea;

	public RoomManagement(BaseFrame baseframe) {
		super(baseframe);
		RoomManager roomManager=new RoomManager();

		JLabel infoLabel = new JLabel("회의실 목록");
		infoLabel.setFont(new Font("굴림", Font.PLAIN, 24));
		infoLabel.setBounds(12, 10, 144, 34);
		adminPanel.add(infoLabel);

		roomListArea = new JTextArea();
		roomListArea.setEditable(false);
		roomListArea.setBorder(new LineBorder(Color.BLACK, 2));
		JScrollPane scrollPane = new JScrollPane(roomListArea);
		scrollPane.setBounds(12, 50, 460, 407);
		adminPanel.add(scrollPane);

		loadRoomList();

		JButton addRoomBtn = new JButton("회의실 추가");
		addRoomBtn.setBounds(518, 50, 126, 23);
		addRoomBtn.addActionListener(e -> baseframe.change(baseframe.panel, baseframe.adv));
		adminPanel.add(addRoomBtn);
		
		JButton deleteRoomBtn = new JButton("회의실 삭제");
		deleteRoomBtn.setBounds(518, 77, 126, 23);
		deleteRoomBtn.addActionListener(e -> {
			int deletedRoom=Integer.parseInt(JOptionPane.showInputDialog("삭제할 회의실 ID를 입력하세요"));
			if(roomManager.deleteRoomById(deletedRoom)) {
				JOptionPane.showMessageDialog(null, "회의실이 삭제되었습니다");
				loadRoomList();
			}
			else JOptionPane.showMessageDialog(null, "회의실 삭제에 실패했습니다", "Message", JOptionPane.ERROR_MESSAGE);
		});
		adminPanel.add(deleteRoomBtn);
		
		JButton updateRoomBtn = new JButton("회의실 수정");
		updateRoomBtn.setBounds(518, 104, 126, 23);
		updateRoomBtn.addActionListener(e -> {
			int updatedRoom=Integer.parseInt(JOptionPane.showInputDialog("수정할 회의실 ID를 입력하세요"));
			String [] options= {"이름", "수용 인원"};
			int updatedElement=JOptionPane.showOptionDialog(null, "수정할 요소를 선택하세요", "Update", JOptionPane.YES_NO_OPTION, JOptionPane.QUESTION_MESSAGE, null,options,null);
			if(updatedElement==0) {
				String name=JOptionPane.showInputDialog("회의실의 새로운 이름을 작성하세요");
				if(roomManager.updateRoomName(updatedRoom, name)) {
					JOptionPane.showMessageDialog(null, "회의실 이름이 수정되었습니다");
					loadRoomList();
				}
				else JOptionPane.showMessageDialog(null, "회의실 이름 수정에 실패했습니다", "Message", JOptionPane.ERROR_MESSAGE);
			}
			else {
				int capacity=Integer.parseInt(JOptionPane.showInputDialog("회의실의 새로운 수용인원을 작성하세요"));
				if(roomManager.updateRoomCapacity(updatedRoom, capacity)) {
					JOptionPane.showMessageDialog(null, "회의실 수용인원이 수정되었습니다");
					loadRoomList();
				}
				else JOptionPane.showMessageDialog(null, "회의실 수용인원 수정에 실패했습니다", "Message", JOptionPane.ERROR_MESSAGE);
			}
		});
		adminPanel.add(updateRoomBtn);
	}

	private void loadRoomList() {
		List<Room> rooms = new RoomManager().getAllRooms();
		StringBuilder sb = new StringBuilder();
		for (Room r : rooms) {
			sb.append("ID: ").append(r.getId())
					.append(" | 이름: ").append(r.getName())
					.append(" | 인원: ").append(r.getCapacity())
					.append("\n");
		}
		roomListArea.setText(sb.toString());
	}
}
