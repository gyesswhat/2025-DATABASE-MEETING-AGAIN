package login;

import common.model.Team;
import common.model.User;
import common.util.DBUtil;

import java.sql.*;

/**
 * AuthService: 실제 RDS(MySQL)와 통신하여 로그인/회원가입/사용자 조회 기능을 수행
 */
public class AuthService {

    /**
     * 사용자 로그인
     * @param username 사용자가 입력한 로그인 아이디
     * @param password 사용자가 입력한 패스워드(평문)
     * @return LoginResult 로그인 성공/실패 + User 객체(성공 시)
     */
    public LoginResult login(String username, String password) {
        if (username == null || username.trim().isEmpty() ||
                password == null || password.trim().isEmpty()) {
            return new LoginResult(false, "사용자명과 비밀번호를 입력해주세요.");
        }

        // 1) 비밀번호 해시
        String hashedPwd = hashPassword(password.trim());

        // 2) DB 조회 (user 테이블)
        String sql = "SELECT id, username, password, role, team_id " +
                "FROM user WHERE username = ?";
        try (Connection conn = DBUtil.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setString(1, username.trim());
            try (ResultSet rs = pstmt.executeQuery()) {
                if (rs.next()) {
                    String storedHash = rs.getString("password");
                    if (storedHash.equals(hashedPwd)) {
                        // 로그인 성공 시 User 객체 생성
                        User user = new User();
                        user.setId(rs.getInt("id"));
                        user.setUsername(rs.getString("username"));
                        user.setPassword(storedHash);
                        user.setRole(rs.getString("role"));
                        user.setTeamId(rs.getInt("team_id"));
                        return new LoginResult(true, "로그인 성공", user);
                    } else {
                        return new LoginResult(false, "비밀번호가 일치하지 않습니다.");
                    }
                } else {
                    return new LoginResult(false, "존재하지 않는 사용자명입니다.");
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
            return new LoginResult(false, "로그인 중 오류가 발생했습니다: " + e.getMessage());
        } catch (Exception e) {
            e.printStackTrace();
            return new LoginResult(false, "알 수 없는 오류가 발생했습니다.");
        }
    }

    /**
     * 사용자 회원가입
     * @param username 새로 가입할 사용자명
     * @param password 평문 비밀번호
     * @param role     역할 ("admin", "leader", "member")
     * @param teamName 팀명 (예: "A-Team" 같이 team.name 컬럼과 일치)
     * @return RegisterResult 성공 여부 및 메시지
     */
    public RegisterResult registerUser(String username, String password, String role, String teamName) {
        if (username == null || username.trim().isEmpty() ||
                password == null || password.trim().isEmpty() ||
                role == null || role.trim().isEmpty() ||
                teamName == null || teamName.trim().isEmpty()) {
            return new RegisterResult(false, "모든 항목(사용자명, 비밀번호, 권한, 팀명)을 입력해주세요.");
        }

        // 1) 사용자명 중복 확인
        if (isUsernameExists(username.trim())) {
            return new RegisterResult(false, "이미 존재하는 사용자명입니다.");
        }

        // 2) 팀명 → team_id 매핑 (team 테이블 조회)
        Integer teamId = findTeamIdByName(teamName.trim());
        if (teamId == null) {
            return new RegisterResult(false, "존재하지 않는 팀명입니다.");
        }

        // 3) 비밀번호 해시
        String hashedPwd = hashPassword(password.trim());

        // 4) 사용자 INSERT
        String sql = "INSERT INTO user(username, password, role, team_id) VALUES(?, ?, ?, ?)";
        try (Connection conn = DBUtil.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {

            pstmt.setString(1, username.trim());
            pstmt.setString(2, hashedPwd);
            pstmt.setString(3, role.trim());
            pstmt.setInt(4, teamId);

            int affectedRow = pstmt.executeUpdate();
            if (affectedRow == 1) {
                return new RegisterResult(true, "회원가입이 완료되었습니다.");
            } else {
                return new RegisterResult(false, "회원가입에 실패했습니다.");
            }
        } catch (SQLException e) {
            e.printStackTrace();
            return new RegisterResult(false, "회원가입 중 오류가 발생했습니다: " + e.getMessage());
        } catch (Exception e) {
            e.printStackTrace();
            return new RegisterResult(false, "알 수 없는 오류가 발생했습니다.");
        }
    }

    /**
     * 사용자명 중복 여부 확인
     * @param username 검사할 사용자명
     * @return true (= 이미 존재 ), false (= 사용 가능 )
     */
    public boolean isUsernameExists(String username) {
        String sql = "SELECT COUNT(*) AS cnt FROM user WHERE username = ?";
        try (Connection conn = DBUtil.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setString(1, username);
            try (ResultSet rs = pstmt.executeQuery()) {
                if (rs.next()) {
                    return rs.getInt("cnt") > 0;
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
            // 오류 시에도 안전하게 true(중복)로 처리하도록 한다면
            // return true;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;
    }

    /**
     * 팀명으로 team_id 조회
     * @param teamName team 테이블의 name 컬럼 값
     * @return team_id (존재하지 않으면 null 반환)
     */
    private Integer findTeamIdByName(String teamName) {
        String sql = "SELECT id FROM team WHERE name = ?";
        try (Connection conn = DBUtil.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setString(1, teamName);
            try (ResultSet rs = pstmt.executeQuery()) {
                if (rs.next()) {
                    return rs.getInt("id");
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * SHA-256 해시 함수
     * @param input 평문 비밀번호
     * @return SHA-256으로 해시된 16진 문자열
     */
    private String hashPassword(String input) {
        try {
            java.security.MessageDigest md = java.security.MessageDigest.getInstance("SHA-256");
            byte[] hashBytes = md.digest(input.getBytes("UTF-8"));
            StringBuilder sb = new StringBuilder();
            for (byte b : hashBytes) {
                sb.append(String.format("%02x", b));
            }
            return sb.toString();
        } catch (Exception e) {
            throw new RuntimeException("비밀번호 해시 중 오류 발생: " + e.getMessage());
        }
    }
}