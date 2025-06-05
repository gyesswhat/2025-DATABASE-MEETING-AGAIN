package login;

public class AuthServiceTest {
    public static void main(String[] args) {
        AuthService service = new AuthService();

        // 1) 사용자명 중복 검사
        System.out.println("=== 중복 검사 ===");
        boolean exists = service.isUsernameExists("alice");
        System.out.println(exists ? "이미 존재" : "사용 가능");

        // 2) 회원가입 예제 (이미 등록된 팀명 사용)
        System.out.println("\n=== 회원가입 ===");
        RegisterResult rr = service.registerUser("newuser", "1234", "member", "A-Team");
        System.out.println(rr.getMessage());

        // 3) 로그인 예제
        System.out.println("\n=== 로그인 ===");
        LoginResult lr = service.login("newuser", "1234");
        System.out.println(lr.getMessage());
        if (lr.isSuccess()) {
            System.out.println("로그인된 User ID: " + lr.getUser().getId());
        }
    }
}