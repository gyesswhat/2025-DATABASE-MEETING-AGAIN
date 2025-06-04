package admin;

import javax.swing.*;
import javax.swing.border.TitledBorder;
import java.awt.*;

import app.*;

public class RoomAdd extends AdminView{
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
		adminPanel.add(roomName);
		roomName.setColumns(10);
		TitledBorder roomNameBorder=new TitledBorder("회의실 이름");
		roomNameBorder.setTitlePosition(TitledBorder.ABOVE_TOP);
		roomNameBorder.setTitleJustification(TitledBorder.LEADING);
		roomName.setBorder(roomNameBorder);
		
		roomCapacity = new JTextField();
		roomCapacity.setBounds(295, 138, 307, 40);
		adminPanel.add(roomCapacity);
		roomCapacity.setColumns(10);
		TitledBorder roomCapacityBorder=new TitledBorder("수용인원");
		roomCapacityBorder.setTitlePosition(TitledBorder.ABOVE_TOP);
		roomCapacityBorder.setTitleJustification(TitledBorder.LEADING);
		roomCapacity.setBorder(roomCapacityBorder);
	}
}
