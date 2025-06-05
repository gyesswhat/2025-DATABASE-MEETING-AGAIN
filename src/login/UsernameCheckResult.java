package login;

/**
 * 사용자명 중복 검사 결과를 담는 DTO
 */
public class UsernameCheckResult {
    private final boolean available; // 사용 가능한가?
    private final String message;

    public UsernameCheckResult(boolean available, String message) {
        this.available = available;
        this.message = message;
    }

    // --- Getter ---
    public boolean isAvailable() {
        return available;
    }

    public String getMessage() {
        return message;
    }
}