package member;

import common.model.User;
import common.util.DBUtil;

import java.sql.*;
import java.time.LocalDateTime;
import java.util.List;

public class MemberController {
    private TimePreferenceManager preferenceManager = new TimePreferenceManager();

    public boolean saveTimePreference(User user, String date, String startStr, String endStr, int priority) {
        try {
            LocalDateTime start = LocalDateTime.parse(date + "T" + startStr);
            LocalDateTime end = LocalDateTime.parse(date + "T" + endStr);

            TimePreference tp = new TimePreference(user.getId(), user.getTeamId(), start, end, priority);
            return preferenceManager.save(tp);

        } catch (Exception e) {
            System.err.println("[MemberController] 시간 등록 오류: " + e.getMessage());
            return false;
        }
    }

	public List<TimePreference> getAllByUser(int userId) {
		// TODO Auto-generated method stub
		return preferenceManager.getAllByUser(userId);
	}

    public int getLatestTimeSlotId(int userId, String date, String startTime, String endTime) {
        String sql = "SELECT id FROM db2025team01.db2025_timeslot WHERE user_id = ? AND startTime = ? AND endTime = ? ORDER BY created_at DESC LIMIT 1";
        try (Connection conn = DBUtil.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, userId);
            stmt.setString(2, date + " " + startTime);
            stmt.setString(3, date + " " + endTime);
            ResultSet rs = stmt.executeQuery();
            if (rs.next()) {
                return rs.getInt("id");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return -1;
    }

    public int createMeeting(int timeSlotId, int roomId) {
        String sql = "INSERT INTO db2025team01.db2025_meeting (timeSlot_id, room) VALUES (?, ?)";
        try (Connection conn = DBUtil.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            stmt.setInt(1, timeSlotId);
            String roomName = getRoomNameById(roomId);
            stmt.setInt(2, roomId);
            stmt.executeUpdate();
            ResultSet rs = stmt.getGeneratedKeys();
            if (rs.next()) {
                return rs.getInt(1);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return -1;
    }

    private String getRoomNameById(int roomId) {
        String sql = "SELECT name FROM db2025_room WHERE id = ?";
        try (Connection conn = DBUtil.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, roomId);
            ResultSet rs = stmt.executeQuery();
            if (rs.next()) {
                return rs.getString("name");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;  // 또는 throw 예외 처리해도 됨
    }


    public void inviteTeamMembersToMeeting(int meetingId, int teamId) {
        String fetchUsersSql = "SELECT id FROM db2025team01.db2025_user WHERE team_id = ?";
        String insertSql = "INSERT INTO db2025team01.db2025_meeting_participants (meeting_id, user_id, status) VALUES (?, ?, 'invited')";

        try (Connection conn = DBUtil.getConnection();
             PreparedStatement fetchStmt = conn.prepareStatement(fetchUsersSql);
             PreparedStatement insertStmt = conn.prepareStatement(insertSql)) {

            fetchStmt.setInt(1, teamId);
            ResultSet rs = fetchStmt.executeQuery();

            while (rs.next()) {
                insertStmt.setInt(1, meetingId);
                insertStmt.setInt(2, rs.getInt("id"));
                insertStmt.addBatch();
            }

            insertStmt.executeBatch();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

}
