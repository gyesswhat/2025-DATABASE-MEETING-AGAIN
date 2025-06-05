package login;

/**
 * 회원가입 처리 결과를 담는 DTO
 */
public class RegisterResult {
    private final boolean success;
    private final String message;

    public RegisterResult(boolean success, String message) {
        this.success = success;
        this.message = message;
    }

    // --- Getter ---
    public boolean isSuccess() {
        return success;
    }

    public String getMessage() {
        return message;
    }
}