package admin;

import common.util.DBUtil;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class RoomManager {

    public List<Room> getAllRooms() {
        List<Room> list = new ArrayList<>();
        String sql = "SELECT * FROM room";

        try (Connection conn = DBUtil.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql);
             ResultSet rs = stmt.executeQuery()) {

            while (rs.next()) {
                Room r = new Room();
                r.setId(rs.getInt("id"));
                r.setName(rs.getString("name"));
                r.setCapacity(rs.getInt("capacity"));
                list.add(r);
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return list;
    }

    public void addRoom(Room room) {
        String sql = "INSERT INTO room (name, capacity) VALUES (?, ?)";

        try (Connection conn = DBUtil.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, room.getName());
            stmt.setInt(2, room.getCapacity());
            stmt.executeUpdate();
            System.out.println("회의실 등록 완료");

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}