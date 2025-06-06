package leader;

import common.util.DBUtil;
import common.model.User;
import java.sql.*;

public class TeamManagementService {

    public String findTeamMemberInfo(int teamId) {
        StringBuilder result = new StringBuilder();
        String sql = """
            SELECT u.id, u.username, u.role, t.name as team_name
            FROM db2025_user u 
            LEFT JOIN db2025_team t ON u.team_id = t.id
            WHERE u.team_id = ? 
            ORDER BY 
                CASE u.role 
                    WHEN 'leader' THEN 1 
                    WHEN 'member' THEN 2 
                    ELSE 3 
                END, 
                u.username ASC
            """;

        try (Connection conn = DBUtil.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setInt(1, teamId);
            ResultSet rs = pstmt.executeQuery();

            if (!rs.next()) {
                return "팀원이 없습니다.";
            }

            String teamName = rs.getString("team_name");
            result.append("=== ").append(teamName != null ? teamName : "팀 " + teamId).append(" ===\n\n");

            do {
                String role = rs.getString("role");
                String roleDisplay = switch (role) {
                    case "leader" -> "리더";
                    case "member" -> "일반회원";
                    default -> role;
                };

                result.append("• ").append(rs.getString("username"))
                        .append(" (").append(roleDisplay).append(")")
                        .append(" [ID: ").append(rs.getInt("id")).append("]")
                        .append("\n");
            } while (rs.next());

        } catch (SQLException e) {
            System.err.println("팀원 정보 조회 중 오류: " + e.getMessage());
            return "팀원 정보를 불러올 수 없습니다.\n오류: " + e.getMessage();
        }

        return result.toString();
    }

    public User getUserByUsername(String username) {
        String sql = """
            SELECT u.id, u.username, u.role, u.team_id, t.name as team_name
            FROM db2025_user u 
            LEFT JOIN db2025_team t ON u.team_id = t.id
            WHERE u.username = ?
            """;

        try (Connection conn = DBUtil.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setString(1, username.trim());
            ResultSet rs = pstmt.executeQuery();

            if (rs.next()) {
                User user = new User();
                user.setId(rs.getInt("id"));
                user.setUsername(rs.getString("username"));
                user.setRole(rs.getString("role"));

                Object teamIdObj = rs.getObject("team_id");
                if (teamIdObj != null) {
                    user.setTeamId(rs.getInt("team_id"));
                } else {
                    user.setTeamId(0);
                }
                return user;
            }
        } catch (SQLException e) {
            System.err.println("사용자 조회 중 오류: " + e.getMessage());
        }
        return null;
    }

    public boolean isUserExists(String username) {
        return getUserByUsername(username) != null;
    }

    public TeamOperationResult addUserToTeam(String username, int targetTeamId, int leaderTeamId) {
        if (targetTeamId != leaderTeamId) {
            return new TeamOperationResult(false, "자신의 팀에만 팀원을 추가할 수 있습니다.");
        }

        Connection conn = null;
        try {
            conn = DBUtil.getConnection();
            conn.setAutoCommit(false);

            User targetUser = getUserByUsernameWithConnection(conn, username);
            if (targetUser == null) {
                return new TeamOperationResult(false, "존재하지 않는 사용자입니다: " + username);
            }

            if (!"member".equals(targetUser.getRole())) {
                return new TeamOperationResult(false, "일반 회원(member)만 팀에 추가할 수 있습니다.");
            }

            if (targetUser.getTeamId() == targetTeamId) {
                return new TeamOperationResult(false, "이미 해당 팀에 속한 팀원입니다.");
            }

            String updateSql = "UPDATE db2025_user SET team_id = ? WHERE username = ?";
            try (PreparedStatement pstmt = conn.prepareStatement(updateSql)) {
                pstmt.setInt(1, targetTeamId);
                pstmt.setString(2, username.trim());
                int affectedRows = pstmt.executeUpdate();
                if (affectedRows == 0) {
                    throw new SQLException("팀원 추가 실패: 업데이트된 행이 없습니다.");
                }
            }

            conn.commit();
            return new TeamOperationResult(true, "팀원 '" + username + "'이(가) 성공적으로 추가되었습니다.");

        } catch (SQLException e) {
            System.err.println("팀원 추가 중 오류: " + e.getMessage());
            if (conn != null) {
                try {
                    conn.rollback();
                } catch (SQLException rollbackEx) {
                    System.err.println("롤백 중 오류: " + rollbackEx.getMessage());
                }
            }
            return new TeamOperationResult(false, "팀원 추가 중 오류가 발생했습니다: " + e.getMessage());
        } finally {
            if (conn != null) {
                try {
                    conn.setAutoCommit(true);
                    conn.close();
                } catch (SQLException e) {
                    System.err.println("연결 종료 중 오류: " + e.getMessage());
                }
            }
        }
    }

    public TeamOperationResult removeUserFromTeam(String username, int leaderTeamId) {
        Connection conn = null;
        try {
            conn = DBUtil.getConnection();
            conn.setAutoCommit(false);

            User targetUser = getUserByUsernameWithConnection(conn, username);
            if (targetUser == null) {
                return new TeamOperationResult(false, "존재하지 않는 사용자입니다: " + username);
            }

            if (targetUser.getTeamId() != leaderTeamId) {
                return new TeamOperationResult(false, "자신의 팀원만 제거할 수 있습니다.");
            }

            if ("leader".equals(targetUser.getRole())) {
                return new TeamOperationResult(false, "리더는 팀에서 제거할 수 없습니다.");
            }

            // 관련 예약 삭제
            deleteUserReservations(conn, targetUser.getId());

            String updateSql = "UPDATE db2025_user SET team_id = NULL WHERE username = ?";
            try (PreparedStatement pstmt = conn.prepareStatement(updateSql)) {
                pstmt.setString(1, username.trim());
                int affectedRows = pstmt.executeUpdate();
                if (affectedRows == 0) {
                    throw new SQLException("팀원 제거 실패: 업데이트된 행이 없습니다.");
                }
            }

            conn.commit();
            return new TeamOperationResult(true, "팀원 '" + username + "'이(가) 성공적으로 제거되었습니다.");

        } catch (SQLException e) {
            System.err.println("팀원 제거 중 오류: " + e.getMessage());
            if (conn != null) {
                try {
                    conn.rollback();
                } catch (SQLException rollbackEx) {
                    System.err.println("롤백 중 오류: " + rollbackEx.getMessage());
                }
            }
            return new TeamOperationResult(false, "팀원 제거 중 오류가 발생했습니다: " + e.getMessage());
        } finally {
            if (conn != null) {
                try {
                    conn.setAutoCommit(true);
                    conn.close();
                } catch (SQLException e) {
                    System.err.println("연결 종료 중 오류: " + e.getMessage());
                }
            }
        }
    }

    private User getUserByUsernameWithConnection(Connection conn, String username) throws SQLException {
        String sql = "SELECT id, username, role, team_id FROM db2025_user WHERE username = ?";
        try (PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setString(1, username.trim());
            ResultSet rs = pstmt.executeQuery();

            if (rs.next()) {
                User user = new User();
                user.setId(rs.getInt("id"));
                user.setUsername(rs.getString("username"));
                user.setRole(rs.getString("role"));

                Object teamIdObj = rs.getObject("team_id");
                if (teamIdObj != null) {
                    user.setTeamId(rs.getInt("team_id"));
                } else {
                    user.setTeamId(0);
                }
                return user;
            }
        }
        return null;
    }

    private void deleteUserReservations(Connection conn, int userId) throws SQLException {
        if (!tableExists(conn, "db2025_reservation")) {
            return;
        }

        String sql = "DELETE FROM db2025_reservation WHERE user_id = ?";
        try (PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setInt(1, userId);
            int deletedCount = pstmt.executeUpdate();
            if (deletedCount > 0) {
                System.out.println("사용자 " + userId + "의 예약 " + deletedCount + "건이 삭제되었습니다.");
            }
        }
    }

    private boolean tableExists(Connection conn, String tableName) throws SQLException {
        DatabaseMetaData meta = conn.getMetaData();
        ResultSet resultSet = meta.getTables(null, null, tableName, new String[]{"TABLE"});
        return resultSet.next();
    }

    public static class TeamOperationResult {
        private final boolean success;
        private final String message;

        public TeamOperationResult(boolean success, String message) {
            this.success = success;
            this.message = message;
        }

        public boolean isSuccess() { return success; }
        public String getMessage() { return message; }
    }
}