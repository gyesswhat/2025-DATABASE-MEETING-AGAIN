package admin;

import javax.swing.*;
import javax.swing.border.TitledBorder;
import java.awt.*;

import app.BaseFrame;
import common.model.Room;

public class RoomAdd extends AdminView {
	private JTextField roomName;
	private JTextField roomCapacity;

	public RoomAdd(BaseFrame baseframe) {
		super(baseframe);

		JLabel headerLabel = new JLabel("회의실 추가");
		headerLabel.setBounds(0, 5, 900, 30);
		headerLabel.setHorizontalAlignment(SwingConstants.CENTER);
		headerLabel.setFont(new Font("굴림", Font.PLAIN, 25));
		adminPanel.add(headerLabel);

		JLabel infoLabel = new JLabel("새로운 회의실을 추가하려면 아래의 필드를 입력하세요");
		infoLabel.setHorizontalAlignment(SwingConstants.CENTER);
		infoLabel.setBounds(0, 48, 900, 30);
		adminPanel.add(infoLabel);

		roomName = new JTextField();
		roomName.setBounds(295, 88, 307, 40);
		roomName.setBorder(new TitledBorder("회의실 이름"));
		adminPanel.add(roomName);

		roomCapacity = new JTextField();
		roomCapacity.setBounds(295, 138, 307, 40);
		roomCapacity.setBorder(new TitledBorder("수용인원"));
		adminPanel.add(roomCapacity);

		JButton saveBtn = new JButton("저장");
		saveBtn.setBounds(400, 200, 100, 30);
		saveBtn.addActionListener(e -> {
			String name = roomName.getText();
			int capacity = Integer.parseInt(roomCapacity.getText());

			Room room = new Room(name, capacity);
			new RoomManager().addRoom(room);
			JOptionPane.showMessageDialog(null, "회의실이 등록되었습니다.");

			baseframe.change(baseframe.panel, baseframe.rmv);
		});
		adminPanel.add(saveBtn);
	}
}
