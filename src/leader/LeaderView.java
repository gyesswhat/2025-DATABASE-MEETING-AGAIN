package leader;

import java.awt.Color;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JTextField;
import javax.swing.SwingConstants;
import javax.swing.border.LineBorder;

import app.*;
import common.model.UserView;

public class LeaderView extends UserView {
	private JTextField enabledRoomListField;
	private JTextField reservedRoomListField;
	String reservedRoomNum;
	int reservedPeopleNum;
	String deletedRoomNum;
	
    public LeaderView(BaseFrame baseframe){
    	super(baseframe);
    	
    	infoLabel.setText("회의실 검색");
    	JLabel infoLabel_2 = new JLabel("예약 가능한 회의실 목록");
		infoLabel_2.setFont(new Font("굴림", Font.PLAIN, 24));
		infoLabel_2.setBounds(12, 10, 276, 50);
		userPanel.add(infoLabel_2);
		
		enabledRoomListField = new JTextField();
		enabledRoomListField.setHorizontalAlignment(SwingConstants.CENTER);
		enabledRoomListField.setText("DB에서 가능한 회의실 목록이 출력될 예정");
		enabledRoomListField.setEditable(false);
		enabledRoomListField.setBounds(12, 50, 500, 655);
		enabledRoomListField.setBorder(new LineBorder(new Color(0, 0, 0), 5));
		userPanel.add(enabledRoomListField);
		enabledRoomListField.setColumns(10);
		
		JButton reserveRoomBtn = new JButton("회의실 예약하기");
		reserveRoomBtn.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				reservedRoomNum=JOptionPane.showInputDialog("예약할 회의실 번호를 입력하세요");
				reservedPeopleNum=Integer.parseInt(JOptionPane.showInputDialog("예약 인원을 입력하세요"));
				String info="["+reservedRoomNum+"]호실, ["+reservedPeopleNum+"]명 예약되었습니다.";
				JOptionPane.showMessageDialog(null,info);
				
				reservedRoomNum=null;
				reservedPeopleNum=(Integer) null;
			}
		});
		reserveRoomBtn.setBounds(605, 50, 177, 23);
		userPanel.add(reserveRoomBtn);
		
		reservedRoomListField = new JTextField();
		reservedRoomListField.setText("DB에서 예약된 회의실 목록이 출력될 예정");
		reservedRoomListField.setHorizontalAlignment(SwingConstants.CENTER);
		reservedRoomListField.setEditable(false);
		reservedRoomListField.setColumns(10);
		reservedRoomListField.setBorder(new LineBorder(new Color(0, 0, 0), 5));
		reservedRoomListField.setBounds(866, 50, 500, 655);
		userPanel.add(reservedRoomListField);
		
		JLabel infoLabel_3 = new JLabel("예약된 회의실");
		infoLabel_3.setFont(new Font("굴림", Font.PLAIN, 24));
		infoLabel_3.setBounds(866, 10, 276, 50);
		userPanel.add(infoLabel_3);
		
		JButton cancelReservationBtn = new JButton("회의실 예약 취소하기");
		cancelReservationBtn.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				deletedRoomNum=JOptionPane.showInputDialog("삭제할 회의실을 입력하세요");
				JOptionPane.showMessageDialog(null, "회의실이 삭제되었습니다.");
				
				deletedRoomNum=null;
			}
		});
		cancelReservationBtn.setBounds(605, 83, 177, 23);
		userPanel.add(cancelReservationBtn);
		
		JButton searchRoomBtn = new JButton("회의실 검색하기");
		searchRoomBtn.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				JOptionPane.showMessageDialog(null, "어떤 기능이 포함되어야 하는지??");
			}
		});
		searchRoomBtn.setBounds(605, 116, 177, 23);
		userPanel.add(searchRoomBtn);
    }
}