package login;

import common.model.User;
import java.sql.*;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;
import java.util.List;

/**
 * 인증 서비스 클래스 (임시 DB 연결 버전)
 * db.properties 문제 해결 전까지 사용
 */
public class AuthService {
    private static User currentUser = null;

    // 임시 DB 연결 정보 (하드코딩)
    private static final String URL = "jdbc:mysql://meeting-again-db.ctc4a4ui64k9.ap-northeast-2.rds.amazonaws.com:3306/meetingdb?useSSL=false&serverTimezone=UTC";
    private static final String USERNAME = "admin";
    private static final String PASSWORD = "meetingagain";

    /**
     * 임시 DB 연결 메서드
     */
    private static Connection getConnection() throws SQLException {
        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
            return DriverManager.getConnection(URL, USERNAME, PASSWORD);
        } catch (ClassNotFoundException e) {
            throw new SQLException("MySQL 드라이버를 찾을 수 없습니다: " + e.getMessage());
        }
    }

    /**
     * 사용자 회원가입
     */
    public static boolean registerUser(String username, String password, String role, String team) {
        if (username == null || username.trim().isEmpty() ||
                password == null || password.trim().isEmpty() ||
                role == null || role.trim().isEmpty()) {
            System.out.println("❌ 필수 입력값이 누락되었습니다.");
            return false;
        }

        if (isUsernameExists(username)) {
            System.out.println("❌ 이미 존재하는 사용자명입니다: " + username);
            return false;
        }

        String sql = "INSERT INTO User (username, password, role, team) VALUES (?, ?, ?, ?)";

        try (Connection conn = getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            String hashedPassword = hashPassword(password);

            pstmt.setString(1, username.trim());
            pstmt.setString(2, hashedPassword);
            pstmt.setString(3, role.trim());
            pstmt.setString(4, team != null ? team.trim() : "");

            int result = pstmt.executeUpdate();
            if (result > 0) {
                System.out.println("✅ 회원가입 성공! 사용자: " + username);
                return true;
            }

        } catch (SQLException e) {
            System.err.println("❌ 회원가입 실패: " + e.getMessage());
        }

        return false;
    }

    /**
     * 사용자 로그인
     */
    public static User login(String username, String password) {
        if (username == null || username.trim().isEmpty() ||
                password == null || password.trim().isEmpty()) {
            System.out.println("❌ 사용자명과 비밀번호를 입력해주세요.");
            return null;
        }

        String sql = "SELECT * FROM User WHERE username = ?";

        try (Connection conn = getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setString(1, username.trim());
            ResultSet rs = pstmt.executeQuery();

            if (rs.next()) {
                String storedPassword = rs.getString("password");

                // 평문 비밀번호 먼저 체크 (더미데이터 호환)
                if (password.equals(storedPassword) || hashPassword(password).equals(storedPassword)) {
                    User user = new User(
                            rs.getInt("id"),
                            rs.getString("username"),
                            rs.getString("password"),
                            rs.getString("role"),
                            rs.getString("team")
                    );

                    currentUser = user;
                    System.out.println("✅ 로그인 성공! 환영합니다, " + user.getUsername() + "님 (" + user.getRole() + ")");
                    return user;
                } else {
                    System.out.println("❌ 비밀번호가 틀렸습니다.");
                }
            } else {
                System.out.println("❌ 존재하지 않는 사용자명입니다.");
            }

        } catch (SQLException e) {
            System.err.println("❌ 로그인 중 오류 발생: " + e.getMessage());
        }

        return null;
    }

    /**
     * 로그아웃
     */
    public static void logout() {
        if (currentUser != null) {
            System.out.println("👋 " + currentUser.getUsername() + "님이 로그아웃했습니다.");
            currentUser = null;
        }
    }

    /**
     * 현재 로그인된 사용자 반환
     */
    public static User getCurrentUser() {
        return currentUser;
    }

    /**
     * 로그인 상태 확인
     */
    public static boolean isLoggedIn() {
        return currentUser != null;
    }

    /**
     * 사용자명 중복 체크
     */
    public static boolean isUsernameExists(String username) {
        String sql = "SELECT COUNT(*) FROM User WHERE username = ?";

        try (Connection conn = getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setString(1, username.trim());
            ResultSet rs = pstmt.executeQuery();

            if (rs.next()) {
                return rs.getInt(1) > 0;
            }

        } catch (SQLException e) {
            System.err.println("❌ 사용자명 중복 체크 중 오류: " + e.getMessage());
        }

        return false;
    }

    /**
     * 역할별 사용자 목록 조회
     */
    public static List<User> getUsersByRole(String role) {
        List<User> users = new ArrayList<>();
        String sql = role != null ?
                "SELECT * FROM User WHERE role = ? ORDER BY username" :
                "SELECT * FROM User ORDER BY role, username";

        try (Connection conn = getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            if (role != null) {
                pstmt.setString(1, role);
            }

            ResultSet rs = pstmt.executeQuery();

            while (rs.next()) {
                User user = new User(
                        rs.getInt("id"),
                        rs.getString("username"),
                        rs.getString("password"),
                        rs.getString("role"),
                        rs.getString("team")
                );
                users.add(user);
            }

        } catch (SQLException e) {
            System.err.println("❌ 사용자 목록 조회 중 오류: " + e.getMessage());
        }

        return users;
    }

    /**
     * 팀별 사용자 목록 조회
     */
    public static List<User> getUsersByTeam(String team) {
        List<User> users = new ArrayList<>();
        String sql = "SELECT * FROM User WHERE team = ? ORDER BY role, username";

        try (Connection conn = getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setString(1, team);
            ResultSet rs = pstmt.executeQuery();

            while (rs.next()) {
                User user = new User(
                        rs.getInt("id"),
                        rs.getString("username"),
                        rs.getString("password"),
                        rs.getString("role"),
                        rs.getString("team")
                );
                users.add(user);
            }

        } catch (SQLException e) {
            System.err.println("❌ 팀별 사용자 조회 중 오류: " + e.getMessage());
        }

        return users;
    }

    /**
     * 권한 확인 (admin인지 체크)
     */
    public static boolean isAdmin() {
        return currentUser != null && "admin".equals(currentUser.getRole());
    }

    /**
     * 권한 확인 (leader인지 체크)
     */
    public static boolean isLeader() {
        return currentUser != null && "leader".equals(currentUser.getRole());
    }

    /**
     * 비밀번호 해시화 (SHA-256)
     */
    private static String hashPassword(String password) {
        try {
            MessageDigest md = MessageDigest.getInstance("SHA-256");
            byte[] hashedBytes = md.digest(password.getBytes());

            StringBuilder sb = new StringBuilder();
            for (byte b : hashedBytes) {
                sb.append(String.format("%02x", b));
            }
            return sb.toString();

        } catch (NoSuchAlgorithmException e) {
            System.err.println("❌ 해시 알고리즘 오류: " + e.getMessage());
            return password;
        }
    }

    /**
     * 사용자 정보 출력 (관리자용)
     */
    public static void printAllUsers() {
        List<User> users = getUsersByRole(null);

        if (users.isEmpty()) {
            System.out.println("📭 등록된 사용자가 없습니다.");
            return;
        }

        System.out.println("\n" + "=".repeat(60));
        System.out.println("👥 전체 사용자 목록 (총 " + users.size() + "명)");
        System.out.println("=".repeat(60));
        System.out.printf("%-5s %-15s %-10s %-15s%n", "ID", "사용자명", "역할", "팀");
        System.out.println("-".repeat(60));

        for (User user : users) {
            System.out.printf("%-5d %-15s %-10s %-15s%n",
                    user.getId(),
                    user.getUsername(),
                    user.getRole(),
                    user.getTeam() != null ? user.getTeam() : "");
        }
        System.out.println("=".repeat(60));
    }
}