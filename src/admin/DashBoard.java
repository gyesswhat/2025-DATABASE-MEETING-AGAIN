package admin;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import javax.swing.*;
import javax.swing.border.TitledBorder;

import app.BaseFrame;
import common.util.DBUtil;

public class DashBoard extends AdminView {
	public DashBoard(BaseFrame baseframe) {
		super(baseframe);

		JLabel headLabel = new JLabel("대시보드");
		headLabel.setBounds(12, 5, 134, 30);
		adminPanel.add(headLabel);

		addStatLabel("전체 회의실 수", 12, 60, totalRoomCount());
		addStatLabel("전체 사용자 수", 309, 60, totalUserCount());
		addStatLabel("오늘 예약 건수", 606, 60, todayReservationCount());
	}

	private void addStatLabel(String title, int x, int y, String text) {
		JLabel label = new JLabel(text);
		label.setBounds(x, y, 285, 64);
		TitledBorder border = new TitledBorder(title);
		border.setTitlePosition(TitledBorder.ABOVE_TOP);
		label.setBorder(border);
		adminPanel.add(label);
	}
	
	public String totalRoomCount() {
		String result=new String();
		String sql="SELECT COUNT(*) AS cnt FROM db2025_room";
		try (Connection conn = DBUtil.getConnection();
				PreparedStatement stmt = conn.prepareStatement(sql);
				ResultSet rs = stmt.executeQuery();){
			if(rs.next()) result=Integer.toString(rs.getInt("cnt"));
			return result;
		}catch (SQLException e) {
			e.printStackTrace();
			return null;
		}
	}
	public String totalUserCount()	{
		String result=new String();
		String sql="SELECT COUNT(*) AS cnt FROM db2025_user";
		try (Connection conn = DBUtil.getConnection();
				PreparedStatement stmt = conn.prepareStatement(sql);
				ResultSet rs = stmt.executeQuery();){
			if(rs.next()) result=Integer.toString(rs.getInt("cnt"));
			return result;
		}catch (SQLException e) {
			e.printStackTrace();
			return null;
		}
	}
	public String todayReservationCount() {
		String result=new String();
		String sql="SELECT COUNT(*)AS cnt FROM db2025_reservation";
		try (Connection conn = DBUtil.getConnection();
				PreparedStatement stmt = conn.prepareStatement(sql);
				ResultSet rs = stmt.executeQuery();){
			if(rs.next()) result=Integer.toString(rs.getInt("cnt"));
			return result;
		}catch (SQLException e) {
			e.printStackTrace();
			return null;
		}
	}
}
