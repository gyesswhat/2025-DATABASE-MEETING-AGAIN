package login;

import common.model.User;

/**
 * 로그인 시도 결과를 담는 DTO
 */
public class LoginResult {
    private final boolean success;
    private final String message;
    private final User user; // 로그인 성공 시 User 객체

    // --- 생성자 ---
    public LoginResult(boolean success, String message, User user) {
        this.success = success;
        this.message = message;
        this.user = user;
    }

    public LoginResult(boolean success, String message) {
        this(success, message, null);
    }

    // --- Getter ---
    public boolean isSuccess() {
        return success;
    }

    public String getMessage() {
        return message;
    }

    public User getUser() {
        return user;
    }
}