package admin;

import javax.swing.*;
import javax.swing.border.LineBorder;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import app.*;

public class RoomManagement extends AdminView{
	public JTextField roomListField;
	public RoomManagement(BaseFrame baseframe) {
		super(baseframe);
		
		JLabel infoLabel = new JLabel("화의실 목록");
		infoLabel.setFont(new Font("굴림", Font.PLAIN, 24));
		infoLabel.setBounds(12, 10, 144, 34);
		adminPanel.add(infoLabel);
		
		roomListField = new JTextField();
		roomListField.setEditable(false);
		roomListField.setHorizontalAlignment(SwingConstants.CENTER);
		roomListField.setText("DB의 회의실 목록 출력 화면");
		roomListField.setBounds(12, 50, 460, 407);
		adminPanel.add(roomListField);
		roomListField.setColumns(10);
		roomListField.setBorder(new LineBorder(new Color(0, 0, 0), 5));
		
		JButton deleteRoomBtn = new JButton("회의실 삭제");
		deleteRoomBtn.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				String deleteRoomNum=JOptionPane.showInputDialog("삭제할 회의실 번호를 입력하세요");
				if(deleteRoomNum!=null) JOptionPane.showMessageDialog(null, "회의실이 삭제되었습니다");
			}
		});
		deleteRoomBtn.setBounds(518, 50, 126, 23);
		adminPanel.add(deleteRoomBtn);
		
		JButton modifyRoomBtn = new JButton("회의실 수정");
		modifyRoomBtn.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				String modifiedRoomNum=JOptionPane.showInputDialog("수정할 회의실 번호를 입력하세요");
				String modifiedFactor, modifiedRoomName, modifiedRoomCapacity;
				while(true) {
					modifiedFactor=JOptionPane.showInputDialog("수정할 요소를 입력하세요", "화의실 이름, 수용인원");
					if(modifiedFactor.equals("회의실 이름")) {
						modifiedRoomName=JOptionPane.showInputDialog("수정된 회의실 이름을 입력하세요");
						break;
					}
					else if (modifiedFactor.equals("수용인원")) {
						modifiedRoomCapacity=JOptionPane.showInputDialog("새로운 수용인원을 입력하세요");
						break;
					}
					else JOptionPane.showMessageDialog(null, "수정할 요소를 정확히 다시 입력하세요","",JOptionPane.ERROR_MESSAGE);
				}
				JOptionPane.showMessageDialog(null, "수정되었습니다");
			}
		});
		modifyRoomBtn.setBounds(518, 83, 126, 23);
		adminPanel.add(modifyRoomBtn);
		
		JButton addRoomBtn = new JButton("회의실 추가");
		addRoomBtn.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				baseframe.change(baseframe.panel, baseframe.adv);
			}
		});
		addRoomBtn.setBounds(518, 116, 126, 23);
		adminPanel.add(addRoomBtn);
	}
}
