package login;

import common.model.User;
import java.util.ArrayList;
import java.util.List;

/**
 * 더미 데이터 기반 인증 서비스
 * DB 연결 없이 하드코딩된 사용자들로 테스트
 */
public class DummyAuthService {
    private static User currentUser = null;
    private static List<User> users = new ArrayList<>();

    // 더미 사용자들 초기화
    static {
        users.add(new User(1, "alice", "pass123", "admin", "A-Team"));
        users.add(new User(2, "bob", "bobpass", "member", "B-Team"));
        users.add(new User(3, "charlie", "charpass", "member", "A-Team"));
        System.out.println("✅ 더미 사용자 3명 로드됨");
    }

    /**
     * 로그인
     */
    public static User login(String username, String password) {
        if (username == null || password == null) {
            System.out.println("❌ 사용자명과 비밀번호를 입력해주세요.");
            return null;
        }

        for (User user : users) {
            if (user.getUsername().equals(username) && user.getPassword().equals(password)) {
                currentUser = user;
                System.out.println("✅ 로그인 성공! 환영합니다, " + user.getUsername() + "님 (" + user.getRole() + ")");
                return user;
            }
        }

        System.out.println("❌ 로그인 실패: 사용자명 또는 비밀번호가 틀렸습니다.");
        return null;
    }

    /**
     * 회원가입 (더미 버전)
     */
    public static boolean registerUser(String username, String password, String role, String team) {
        // 중복 체크
        for (User user : users) {
            if (user.getUsername().equals(username)) {
                System.out.println("❌ 이미 존재하는 사용자명입니다: " + username);
                return false;
            }
        }

        // 새 사용자 추가
        int newId = users.size() + 1;
        User newUser = new User(newId, username, password, role, team);
        users.add(newUser);

        System.out.println("✅ 회원가입 성공! 사용자: " + username);
        return true;
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
     * 현재 사용자
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
        if (username == null || username.trim().isEmpty()) {
            return false;
        }

        for (User user : users) {
            if (user.getUsername().equals(username.trim())) {
                return true;
            }
        }
        return false;
    }

    /**
     * 역할별 사용자 목록 조회
     */
    public static List<User> getUsersByRole(String role) {
        List<User> filteredUsers = new ArrayList<>();

        if (role == null) {
            return new ArrayList<>(users); // 전체 사용자 반환
        }

        for (User user : users) {
            if (role.equals(user.getRole())) {
                filteredUsers.add(user);
            }
        }
        return filteredUsers;
    }

    /**
     * 팀별 사용자 목록 조회
     */
    public static List<User> getUsersByTeam(String team) {
        List<User> filteredUsers = new ArrayList<>();

        if (team == null) {
            return filteredUsers;
        }

        for (User user : users) {
            if (team.equals(user.getTeam())) {
                filteredUsers.add(user);
            }
        }
        return filteredUsers;
    }

    /**
     * 사용자 목록 출력
     */
    public static void printAllUsers() {
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

    /**
     * 권한 확인
     */
    public static boolean isAdmin() {
        return currentUser != null && "admin".equals(currentUser.getRole());
    }

    public static boolean isLeader() {
        return currentUser != null && "leader".equals(currentUser.getRole());
    }
}