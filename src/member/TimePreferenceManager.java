package member;

import common.util.DBUtil;
import java.sql.*;
import java.util.*;
import java.time.LocalDateTime;

public class TimePreferenceManager {

    // 테이블 존재 여부 확인
    private boolean tableExists(String tableName) {
        try (Connection conn = DBUtil.getConnection()) {
            DatabaseMetaData meta = conn.getMetaData();
            ResultSet resultSet = meta.getTables(null, null, tableName, new String[]{"TABLE"});
            return resultSet.next();
        } catch (SQLException e) {
            System.err.println("테이블 존재 확인 중 오류: " + e.getMessage());
            return false;
        }
    }

    // 시간 선호도 테이블이 없는 경우 생성
    private void createTimePreferenceTableIfNotExists() {
        String tableName = "db2025_timeslot";

        if (!tableExists(tableName)) {
            String createTableSQL = """
                CREATE TABLE IF NOT EXISTS db2025_timeslot (
                    id INT AUTO_INCREMENT PRIMARY KEY,
                    user_id INT NOT NULL,
                    team_id INT NOT NULL,
                    start_time DATETIME NOT NULL,
                    end_time DATETIME NOT NULL,
                    priority INT DEFAULT 1,
                    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
                    FOREIGN KEY (user_id) REFERENCES db2025_user(id),
                    FOREIGN KEY (team_id) REFERENCES db2025_team(id)
                )
                """;

            try (Connection conn = DBUtil.getConnection();
                 PreparedStatement stmt = conn.prepareStatement(createTableSQL)) {
                stmt.executeUpdate();
                System.out.println("✅ db2025_timeslot 테이블이 생성되었습니다.");
            } catch (SQLException e) {
                System.err.println("❌ 시간 선호도 테이블 생성 실패: " + e.getMessage());
                e.printStackTrace();
            }
        }
    }

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

            int result = pstmt.executeUpdate();
            if (result > 0) {
                System.out.println("✅ 시간 선호도 저장 성공");
                return true;
            }

        } catch (SQLException e) {
            System.err.println("시간 등록 실패: " + e.getMessage());
            e.printStackTrace();
        }

        return false;
    }

    // 사용자별 회의 가능 시간 목록 조회
    public List<TimePreference> getAllByUser(int userId) {
        List<TimePreference> list = new ArrayList<>();

        // 테이블이 없으면 빈 리스트 반환
        if (!tableExists("db2025_timeslot")) {
            System.out.println("시간 선호도 테이블이 없습니다.");
            return list;
        }

        String sql = "SELECT * FROM db2025_timeslot WHERE user_id = ? ORDER BY priority ASC, start_time ASC";

        try (Connection conn = DBUtil.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setInt(1, userId);
            ResultSet rs = stmt.executeQuery();

            while (rs.next()) {
                TimePreference tp = new TimePreference();
                tp.setId(rs.getInt("id"));
                tp.setUserId(rs.getInt("user_id"));
                tp.setTeamId(rs.getInt("team_id"));
                tp.setStartTime(rs.getTimestamp("start_time").toLocalDateTime());
                tp.setEndTime(rs.getTimestamp("end_time").toLocalDateTime());
                tp.setPriority(rs.getInt("priority"));

                list.add(tp);
            }
        } catch (SQLException e) {
            System.err.println("시간 선호도 조회 실패: " + e.getMessage());
            e.printStackTrace();
        }

        return list;
    }

    // 팀별 회의 가능 시간 목록 조회
    public List<TimePreference> getAllByTeam(int teamId) {
        List<TimePreference> list = new ArrayList<>();

        if (!tableExists("db2025_timeslot")) {
            System.out.println("시간 선호도 테이블이 없습니다.");
            return list;
        }

        String sql = "SELECT * FROM db2025_timeslot WHERE team_id = ? ORDER BY start_time ASC, priority ASC";

        try (Connection conn = DBUtil.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setInt(1, teamId);
            ResultSet rs = stmt.executeQuery();

            while (rs.next()) {
                TimePreference tp = new TimePreference();
                tp.setId(rs.getInt("id"));
                tp.setUserId(rs.getInt("user_id"));
                tp.setTeamId(rs.getInt("team_id"));
                tp.setStartTime(rs.getTimestamp("start_time").toLocalDateTime());
                tp.setEndTime(rs.getTimestamp("end_time").toLocalDateTime());
                tp.setPriority(rs.getInt("priority"));

                list.add(tp);
            }
        } catch (SQLException e) {
            System.err.println("팀 시간 선호도 조회 실패: " + e.getMessage());
            e.printStackTrace();
        }

        return list;
    }

    // 시간 선호도 삭제
    public boolean delete(int id) {
        if (!tableExists("db2025_timeslot")) {
            return false;
        }

        String sql = "DELETE FROM db2025_timeslot WHERE id = ?";

        try (Connection conn = DBUtil.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setInt(1, id);
            int result = stmt.executeUpdate();

            return result > 0;

        } catch (SQLException e) {
            System.err.println("시간 선호도 삭제 실패: " + e.getMessage());
            e.printStackTrace();
        }

        return false;
    }
}