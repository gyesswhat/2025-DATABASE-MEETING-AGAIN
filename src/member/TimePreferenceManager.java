package member;

import common.util.DBUtil;
import java.sql.*;
import java.util.*;
import java.time.LocalDateTime;
/**
 * TimePreferenceManager
 * - 사용자의 회의 가능 시간을 DB에 저장하고 조회하는 DAO 클래스
 * - 사용 테이블: DB2025_timeslot
 */
public class TimePreferenceManager {

    // 회의 가능 시간 등록
    public boolean save(TimePreference tp) {
        String sql = "INSERT INTO DB2025_timeslot (user_id, team_id, start_time, end_time, priority) VALUES (?, ?, ?, ?, ?)";

        try (Connection conn = DBUtil.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setInt(1, tp.getUserId()); // 외래키: DB2025_user.id
            pstmt.setInt(2, tp.getTeamId()); // 외래키: DB2025_team.id
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
        String sql = "SELECT * FROM DB2025_timeslot WHERE user_id = ? ORDER BY priority ASC";

        try (Connection conn = DBUtil.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setInt(1, userId);
            ResultSet rs = stmt.executeQuery();

            while (rs.next()) {
                TimePreference tp = new TimePreference();
                tp.setId(rs.getInt("id")); // 기본키
                tp.setUserId(rs.getInt("user_id")); // 외래키
                tp.setTeamId(rs.getInt("team_id")); // 외래키
                tp.setStartTime(rs.getTimestamp("start_time").toLocalDateTime());
                tp.setEndTime(rs.getTimestamp("end_time").toLocalDateTime());
                tp.setPriority(rs.getInt("priority"));

                list.add(tp);
            }
        } catch (SQLException e) {
            e.printStackTrace(); // 상세 예외 출력
        }

        return list;
    }
}