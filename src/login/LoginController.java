package login;

import common.model.User;
import java.util.List;

/**
 * 로그인 컨트롤러 클래스
 * GUI와 AuthService 사이의 인터페이스 역할
 * GUI 팀원들이 사용할 수 있는 메서드들 제공
 */
public class LoginController {

    /**
     * 사용자 로그인 처리
     * @param username 사용자명
     * @param password 비밀번호
     * @return 로그인 결과 객체
     */
    public LoginResult login(String username, String password) {
        try {
            User user = DummyAuthService.login(username, password);

            if (user != null) {
                return new LoginResult(true, "로그인 성공", user);
            } else {
                return new LoginResult(false, "사용자명 또는 비밀번호가 틀렸습니다.", null);
            }

        } catch (Exception e) {
            return new LoginResult(false, "로그인 중 오류가 발생했습니다: " + e.getMessage(), null);
        }
    }

    /**
     * 사용자 회원가입 처리
     * @param username 사용자명
     * @param password 비밀번호
     * @param role 역할 (admin, leader, member)
     * @param team 팀명 (선택사항)
     * @return 회원가입 결과 객체
     */
    public RegisterResult register(String username, String password, String role, String team) {
        try {
            // 입력값 검증
            String validationError = validateRegistrationInput(username, password, role);
            if (validationError != null) {
                return new RegisterResult(false, validationError);
            }

            boolean success = DummyAuthService.registerUser(username, password, role, team);

            if (success) {
                return new RegisterResult(true, "회원가입이 완료되었습니다.");
            } else {
                return new RegisterResult(false, "회원가입에 실패했습니다.");
            }

        } catch (Exception e) {
            return new RegisterResult(false, "회원가입 중 오류가 발생했습니다: " + e.getMessage());
        }
    }

    /**
     * 로그아웃 처리
     * @return 로그아웃 결과
     */
    public boolean logout() {
        try {
            DummyAuthService.logout();
            return true;
        } catch (Exception e) {
            System.err.println("로그아웃 중 오류: " + e.getMessage());
            return false;
        }
    }

    /**
     * 현재 로그인된 사용자 정보 반환
     * @return 현재 사용자 또는 null
     */
    public User getCurrentUser() {
        return DummyAuthService.getCurrentUser();
    }

    /**
     * 로그인 상태 확인
     * @return 로그인 상태면 true
     */
    public boolean isLoggedIn() {
        return DummyAuthService.isLoggedIn();
    }

    /**
     * 사용자명 중복 체크
     * @param username 확인할 사용자명
     * @return 중복 체크 결과
     */
    public UsernameCheckResult checkUsername(String username) {
        try {
            if (username == null || username.trim().isEmpty()) {
                return new UsernameCheckResult(false, false, "사용자명을 입력해주세요.");
            }

            if (username.length() < 3 || username.length() > 20) {
                return new UsernameCheckResult(false, false, "사용자명은 3-20자 사이여야 합니다.");
            }

            boolean exists = DummyAuthService.isUsernameExists(username);

            if (exists) {
                return new UsernameCheckResult(true, true, "이미 사용 중인 사용자명입니다.");
            } else {
                return new UsernameCheckResult(true, false, "사용 가능한 사용자명입니다.");
            }

        } catch (Exception e) {
            return new UsernameCheckResult(false, false, "사용자명 확인 중 오류가 발생했습니다.");
        }
    }

    /**
     * 역할별 사용자 목록 조회 (관리자용)
     * @param role 조회할 역할 (null이면 전체)
     * @return 사용자 목록
     */
    public List<User> getUsersByRole(String role) {
        return DummyAuthService.getUsersByRole(role);
    }

    /**
     * 팀별 사용자 목록 조회
     * @param team 조회할 팀명
     * @return 사용자 목록
     */
    public List<User> getUsersByTeam(String team) {
        return DummyAuthService.getUsersByTeam(team);
    }

    /**
     * 관리자 권한 확인
     * @return admin이면 true
     */
    public boolean isCurrentUserAdmin() {
        return DummyAuthService.isAdmin();
    }

    /**
     * 리더 권한 확인
     * @return leader이면 true
     */
    public boolean isCurrentUserLeader() {
        return DummyAuthService.isLeader();
    }

    /**
     * 회원가입 입력값 검증
     * @param username 사용자명
     * @param password 비밀번호
     * @param role 역할
     * @return 오류 메시지 (유효하면 null)
     */
    private String validateRegistrationInput(String username, String password, String role) {
        if (username == null || username.trim().isEmpty()) {
            return "사용자명을 입력해주세요.";
        }

        if (username.length() < 3 || username.length() > 20) {
            return "사용자명은 3-20자 사이여야 합니다.";
        }

        if (password == null || password.trim().isEmpty()) {
            return "비밀번호를 입력해주세요.";
        }

        if (password.length() < 4) {
            return "비밀번호는 최소 4자 이상이어야 합니다.";
        }

        if (role == null || role.trim().isEmpty()) {
            return "역할을 선택해주세요.";
        }

        if (!isValidRole(role)) {
            return "올바른 역할을 선택해주세요. (admin, leader, member)";
        }

        return null; // 유효함
    }

    /**
     * 유효한 역할인지 확인
     * @param role 확인할 역할
     * @return 유효하면 true
     */
    private boolean isValidRole(String role) {
        return "admin".equals(role) || "leader".equals(role) || "member".equals(role);
    }

    // === 결과 클래스들 ===

    /**
     * 로그인 결과 클래스
     */
    public static class LoginResult {
        private final boolean success;
        private final String message;
        private final User user;

        public LoginResult(boolean success, String message, User user) {
            this.success = success;
            this.message = message;
            this.user = user;
        }

        public boolean isSuccess() { return success; }
        public String getMessage() { return message; }
        public User getUser() { return user; }
    }

    /**
     * 회원가입 결과 클래스
     */
    public static class RegisterResult {
        private final boolean success;
        private final String message;

        public RegisterResult(boolean success, String message) {
            this.success = success;
            this.message = message;
        }

        public boolean isSuccess() { return success; }
        public String getMessage() { return message; }
    }

    /**
     * 사용자명 중복 체크 결과 클래스
     */
    public static class UsernameCheckResult {
        private final boolean valid;     // 입력값이 유효한지
        private final boolean exists;    // 사용자명이 존재하는지
        private final String message;

        public UsernameCheckResult(boolean valid, boolean exists, String message) {
            this.valid = valid;
            this.exists = exists;
            this.message = message;
        }

        public boolean isValid() { return valid; }
        public boolean exists() { return exists; }
        public String getMessage() { return message; }
        public boolean isAvailable() { return valid && !exists; }
    }
}
