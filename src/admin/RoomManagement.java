package admin;

import javax.swing.*;
import javax.swing.border.LineBorder;
import java.awt.*;
import java.util.List;

import app.BaseFrame;
import common.model.Room;

public class RoomManagement extends AdminView {
	private JTextArea roomListArea;

	public RoomManagement(BaseFrame baseframe) {
		super(baseframe);

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
