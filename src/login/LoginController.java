package login;

import common.model.User;

import java.util.List;

/**
 * 컨트롤러 레벨에서 호출할 수 있는 인증·회원가입 API 인터페이스
 * - AuthService의 결과를 받아 화면(UI) 쪽으로 돌려준다.
 */
public class LoginController {

    private final AuthService authService;

    public LoginController() {
        authService = new AuthService();
    }

    /**
     * 로그인
     */
    public LoginResult login(String username, String password) {
        return authService.login(username, password);
    }

    /**
     * 회원가입
     */
    public RegisterResult register(String username, String password, String role, String teamName) {
        return authService.registerUser(username, password, role, teamName);
    }

    /**
     * 사용자명 중복 체크
     */
    public UsernameCheckResult checkUsername(String username) {
        boolean exists = authService.isUsernameExists(username);
        if (exists) {
            return new UsernameCheckResult(false, "이미 존재하는 사용자명입니다.");
        } else {
            return new UsernameCheckResult(true, "사용 가능한 사용자명입니다.");
        }
    }

    /**
     * 현재 로그인된 사용자 정보(앱 전역에 세션 개념이 있으면 확장 가능)
     */
    public User getCurrentUser() {
        // AuthService 내부에서 세션 개념을 구현했다면 가져올 수 있고,
        // 별도 세션 관리가 필요하다면 여기서 로직을 추가하면 됩니다.
        return null;
    }

}