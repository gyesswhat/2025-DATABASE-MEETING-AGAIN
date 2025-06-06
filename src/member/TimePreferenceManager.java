package member;

import common.util.DBUtil;
import java.sql.*;
import java.util.*;
import java.time.LocalDateTime;

public class TimePreferenceManager {

    // 회의 가능 시간 등록
    public boolean save(TimePreference tp) {
        String sql = "INSERT INTO db2025_timeslot (user_id, team_id, starTtime, endTime, priority) VALUES (?, ?, ?, ?, ?)";

        try (Connection conn = DBUtil.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setInt(1, tp.getUserId());
            pstmt.setInt(2, tp.getTeamId());
            pstmt.setTimestamp(3, Timestamp.valueOf(tp.getStartTime()));
            pstmt.setTimestamp(4, Timestamp.valueOf(tp.getEndTime()));
            pstmt.setInt(5, tp.getPriority());

            pstmt.executeUpdate();
            return true;

        } catch (SQLException e) {
            System.err.println("시간 등록 실패: " + e.getMessage());
            return false;
        }
    }

    // 사용자별 회의 가능 시간 목록 조회
    public List<TimePreference> getAllByUser(int userId) {
        List<TimePreference> list = new ArrayList<>();
        String sql = "SELECT * FROM db2025_timeslot WHERE user_id = ? ORDER BY priority ASC";

        try (Connection conn = DBUtil.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setInt(1, userId);
            ResultSet rs = stmt.executeQuery();

            while (rs.next()) {
                TimePreference tp = new TimePreference();
                tp.setId(rs.getInt("id"));
                tp.setUserId(rs.getInt("user_id"));
                tp.setTeamId(rs.getInt("team_id"));
                tp.setStartTime(rs.getTimestamp("startTime").toLocalDateTime());
                tp.setEndTime(rs.getTimestamp("endTime").toLocalDateTime());
                tp.setPriority(rs.getInt("priority"));

                list.add(tp);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return list;
    }
}
