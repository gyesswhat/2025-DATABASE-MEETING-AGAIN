package admin;
import common.util.DBUtil;
import common.model.Room;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class RoomManager {
    // 회의실 전체 조회
    public List<Room> getAllRooms() {
        List<Room> list = new ArrayList<>();
        String sql = "SELECT * FROM db2025_room";
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
    
    // 회의실 등록
    public boolean addRoom(Room room) {
        String sql = "INSERT INTO db2025_room (name, capacity) VALUES (?, ?)"; // 이 부분 db2025_room 으로 수정함
      // "Resolve merge conflict: use db2025_room table", 아래도 다 
        try (Connection conn = DBUtil.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, room.getName());
            stmt.setInt(2, room.getCapacity());
            return stmt.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }
    
    // 회의실 삭제
    public boolean deleteRoomById(int id) {
        String sql = "DELETE FROM db2025_room WHERE id = ?";
        try (Connection conn = DBUtil.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, id);
            return stmt.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }
    
    // 회의실 이름 수정
    public boolean updateRoomName(int id, String name) {
        String sql = "UPDATE db2025_room SET name = ? WHERE id = ?";
        try (Connection conn = DBUtil.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, name);
            stmt.setInt(2, id);
            return stmt.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }
    
    // 회의실 수용인원 수정
    public boolean updateRoomCapacity(int id, int capacity) {
        String sql = "UPDATE db2025_room SET capacity = ? WHERE id = ?";
        try (Connection conn = DBUtil.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, capacity);
            stmt.setInt(2, id);
            return stmt.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }
}