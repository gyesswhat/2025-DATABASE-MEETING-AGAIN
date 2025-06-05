package admin;

import javax.swing.*;
import javax.swing.border.TitledBorder;

import app.*;

public class DashBoard extends AdminView{

	public DashBoard(BaseFrame baseframe) {
		super(baseframe);
		
		JLabel headLabel = new JLabel("대시보드");
		headLabel.setBounds(12, 5, 134, 30);
		adminPanel.add(headLabel);
		
		JLabel totalRoomLabel = new JLabel("New label");
		totalRoomLabel.setBounds(12, 60, 285, 64);
		TitledBorder totalRoomBorder = new TitledBorder("전체 회의실 수");
		totalRoomBorder.setTitlePosition(TitledBorder.ABOVE_TOP);
		totalRoomBorder.setTitleJustification(TitledBorder.LEADING);
		totalRoomLabel.setBorder(totalRoomBorder);
		adminPanel.add(totalRoomLabel);
		
		JLabel totalUserLabel = new JLabel("New label");
		totalUserLabel.setBounds(309, 60, 285, 64);
		TitledBorder totalUserBorder = new TitledBorder("전체 사용자 수");
		totalUserBorder.setTitlePosition(TitledBorder.ABOVE_TOP);
		totalUserBorder.setTitleJustification(TitledBorder.LEADING);
		totalUserLabel.setBorder(totalUserBorder);
		adminPanel.add(totalUserLabel);
		
		JLabel todayReservationLabel = new JLabel("New label");
		todayReservationLabel.setBounds(606, 60, 285, 64);
		TitledBorder todayReservationBorder = new TitledBorder("오늘 예약 건수");
		todayReservationBorder.setTitlePosition(TitledBorder.ABOVE_TOP);
		todayReservationBorder.setTitleJustification(TitledBorder.LEADING);
		todayReservationLabel.setBorder(todayReservationBorder);
		adminPanel.add(todayReservationLabel);
	}
}
