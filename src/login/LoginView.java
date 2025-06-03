package login;

import common.model.User;
import java.util.List;
import java.util.Scanner;

/**
 * 로그인 뷰 클래스 (콘솔 기반 테스트용)
 * (250524 필독) !!! RDS 연결이 안되어서 (더미 사용자 로그인 안됨) intelliJ 에서의 세팅 문제인듯..
 * 일단은 DummyAuthService로 연결해두어서, AuthService에서 관련 오류 해결되면 이걸로 다 바꿔야 함.
 */
public class LoginView {
    private final Scanner scanner;
    private final LoginController loginController;

    public LoginView() {
        this.scanner = new Scanner(System.in);
        this.loginController = new LoginController();
    }

    /**
     * 메인 실행 메서드
     */
    public void start() {
        printWelcomeMessage();

        while (true) {
            try {
                if (!loginController.isLoggedIn()) {
                    showMainMenu();
                    handleMainMenu();
                } else {
                    showUserMenu();
                    handleUserMenu();
                }
            } catch (Exception e) {
                System.err.println("❌ 예상치 못한 오류: " + e.getMessage());
                e.printStackTrace();
            }
        }
    }

    /**
     * 환영 메시지 출력
     */
    private void printWelcomeMessage() {
        System.out.println("=".repeat(50));
        System.out.println("🎉 MeetingAgain 시스템에 오신 것을 환영합니다!");
        System.out.println("📱 팀 프로젝트 로그인/회원가입 시스템 (더미 데이터)");
        System.out.println("=".repeat(50));
    }

    /**
     * 메인 메뉴 출력 (로그인 전)
     */
    private void showMainMenu() {
        System.out.println("\n" + "=".repeat(30));
        System.out.println("📋 메인 메뉴");
        System.out.println("=".repeat(30));
        System.out.println("1️⃣  로그인");
        System.out.println("2️⃣  회원가입");
        System.out.println("3️⃣  더미 사용자로 테스트 로그인");
        System.out.println("4️⃣  전체 사용자 보기");
        System.out.println("0️⃣  종료");
        System.out.println("=".repeat(30));
        System.out.print("👉 선택하세요: ");
    }

    /**
     * 사용자 메뉴 출력 (로그인 후)
     */
    private void showUserMenu() {
        User currentUser = loginController.getCurrentUser();
        System.out.println("\n" + "=".repeat(40));
        System.out.println("👋 " + currentUser.getUsername() + "님의 메뉴 (" + currentUser.getRole() + ")");
        System.out.println("=".repeat(40));
        System.out.println("1️⃣  내 정보 보기");
        System.out.println("2️⃣  전체 사용자 보기");

        if (loginController.isCurrentUserAdmin()) {
            System.out.println("3️⃣  관리자 메뉴");
        }

        if (loginController.isCurrentUserLeader()) {
            System.out.println("4️⃣  리더 메뉴 (팀원 관리)");
        }

        System.out.println("9️⃣  로그아웃");
        System.out.println("0️⃣  종료");
        System.out.println("=".repeat(40));
        System.out.print("👉 선택하세요: ");
    }

    /**
     * 메인 메뉴 처리
     */
    private void handleMainMenu() {
        int choice = getChoice();

        switch (choice) {
            case 1:
                handleLogin();
                break;
            case 2:
                handleRegister();
                break;
            case 3:
                handleDummyLogin();
                break;
            case 4:
                showAllUsers();
                break;
            case 0:
                exitProgram();
                break;
            default:
                System.out.println("❌ 잘못된 선택입니다. 0-4 사이의 숫자를 입력해주세요.");
        }
    }

    /**
     * 사용자 메뉴 처리 (로그인 후)
     */
    private void handleUserMenu() {
        int choice = getChoice();

        switch (choice) {
            case 1:
                showMyInfo();
                break;
            case 2:
                showAllUsers();
                break;
            case 3:
                if (loginController.isCurrentUserAdmin()) {
                    showAdminMenu();
                } else {
                    System.out.println("❌ 관리자 권한이 필요합니다.");
                }
                break;
            case 4:
                if (loginController.isCurrentUserLeader()) {
                    showLeaderMenu();
                } else {
                    System.out.println("❌ 리더 권한이 필요합니다.");
                }
                break;
            case 9:
                handleLogout();
                break;
            case 0:
                exitProgram();
                break;
            default:
                System.out.println("❌ 잘못된 선택입니다.");
        }
    }

    /**
     * 로그인 처리
     */
    private void handleLogin() {
        System.out.println("\n" + "🔐 로그인".toUpperCase());
        System.out.println("=".repeat(30));

        System.out.print("👤 사용자명: ");
        String username = scanner.nextLine().trim();

        if (username.isEmpty()) {
            System.out.println("❌ 사용자명을 입력해주세요.");
            return;
        }

        System.out.print("🔒 비밀번호: ");
        String password = scanner.nextLine();

        if (password.isEmpty()) {
            System.out.println("❌ 비밀번호를 입력해주세요.");
            return;
        }

        // LoginController를 통한 로그인
        LoginController.LoginResult result = loginController.login(username, password);

        if (result.isSuccess()) {
            System.out.println("🎊 " + result.getMessage());
            System.out.println("📋 역할: " + result.getUser().getRole());
            System.out.println("👥 팀: " + (result.getUser().getTeam() != null ? result.getUser().getTeam() : "없음"));
            pauseForUser("계속하려면 Enter를 눌러주세요...");
        } else {
            System.out.println("❌ " + result.getMessage());
        }
    }

    /**
     * 회원가입 처리
     */
    private void handleRegister() {
        System.out.println("\n" + "🆕 회원가입".toUpperCase());
        System.out.println("=".repeat(30));

        System.out.print("👤 사용자명 (3-20자): ");
        String username = scanner.nextLine().trim();

        if (!username.isEmpty()) {
            // 사용자명 중복 체크
            LoginController.UsernameCheckResult checkResult = loginController.checkUsername(username);
            System.out.println("💡 " + checkResult.getMessage());

            if (!checkResult.isAvailable()) {
                return;
            }
        }

        System.out.print("🔒 비밀번호 (최소 4자): ");
        String password = scanner.nextLine();

        System.out.println("👔 역할을 선택하세요:");
        System.out.println("   1. admin (관리자)");
        System.out.println("   2. leader (리더)");
        System.out.println("   3. member (멤버)");
        System.out.print("선택: ");

        String role = "";
        int roleChoice = getChoice();
        switch (roleChoice) {
            case 1: role = "admin"; break;
            case 2: role = "leader"; break;
            case 3: role = "member"; break;
            default:
                System.out.println("❌ 잘못된 선택입니다.");
                return;
        }

        System.out.print("👥 팀명 (선택사항): ");
        String team = scanner.nextLine().trim();
        if (team.isEmpty()) {
            team = null;
        }

        // LoginController를 통한 회원가입
        LoginController.RegisterResult result = loginController.register(username, password, role, team);

        if (result.isSuccess()) {
            System.out.println("🎉 " + result.getMessage());
            System.out.println("💡 이제 로그인하실 수 있습니다.");
        } else {
            System.out.println("❌ " + result.getMessage());
        }
    }

    /**
     * 더미 사용자 로그인 (테스트용)
     */
    private void handleDummyLogin() {
        System.out.println("\n" + "🧪 더미 사용자 테스트 로그인".toUpperCase());
        System.out.println("=".repeat(40));
        System.out.println("1️⃣  alice (admin, A-Team)");
        System.out.println("2️⃣  bob (member, B-Team)");
        System.out.println("3️⃣  charlie (member, A-Team)");
        System.out.print("선택: ");

        int choice = getChoice();
        String username = "";
        String password = "";

        switch (choice) {
            case 1: username = "alice"; password = "pass123"; break;
            case 2: username = "bob"; password = "bobpass"; break;
            case 3: username = "charlie"; password = "charpass"; break;
            default:
                System.out.println("❌ 잘못된 선택입니다.");
                return;
        }

        LoginController.LoginResult result = loginController.login(username, password);

        if (result.isSuccess()) {
            System.out.println("🎊 테스트 로그인 성공!");
            System.out.println("👤 " + result.getUser().getUsername() + " (" + result.getUser().getRole() + ")");
            pauseForUser("계속하려면 Enter를 눌러주세요...");
        } else {
            System.out.println("❌ 테스트 로그인 실패: " + result.getMessage());
        }
    }

    /**
     * 로그아웃 처리
     */
    private void handleLogout() {
        if (loginController.logout()) {
            System.out.println("👋 로그아웃되었습니다.");
        } else {
            System.out.println("❌ 로그아웃 중 오류가 발생했습니다.");
        }
    }

    /**
     * 내 정보 보기
     */
    private void showMyInfo() {
        User currentUser = loginController.getCurrentUser();

        System.out.println("\n" + "👤 내 정보".toUpperCase());
        System.out.println("=".repeat(40));
        System.out.println("🆔 사용자 ID: " + currentUser.getId());
        System.out.println("👤 사용자명: " + currentUser.getUsername());
        System.out.println("👔 역할: " + currentUser.getRole());
        System.out.println("👥 팀: " + (currentUser.getTeam() != null ? currentUser.getTeam() : "없음"));
        System.out.println("=".repeat(40));

        pauseForUser("계속하려면 Enter를 눌러주세요...");
    }

    /**
     * 전체 사용자 보기 (DummyAuthService 사용)
     */
    private void showAllUsers() {
        System.out.println("\n" + "👥 전체 사용자 목록".toUpperCase());
        DummyAuthService.printAllUsers(); // ← AuthService 대신 DummyAuthService 사용
        pauseForUser("계속하려면 Enter를 눌러주세요...");
    }

    /**
     * 관리자 메뉴
     */
    private void showAdminMenu() {
        System.out.println("\n" + "🔧 관리자 메뉴".toUpperCase());
        System.out.println("=".repeat(30));
        System.out.println("1️⃣  역할별 사용자 조회");
        System.out.println("2️⃣  팀별 사용자 조회");
        System.out.print("선택: ");

        int choice = getChoice();

        switch (choice) {
            case 1:
                showUsersByRole();
                break;
            case 2:
                showUsersByTeam();
                break;
            default:
                System.out.println("❌ 잘못된 선택입니다.");
        }
    }

    /**
     * 리더 메뉴
     */
    private void showLeaderMenu() {
        User currentUser = loginController.getCurrentUser();
        String myTeam = currentUser.getTeam();

        System.out.println("\n" + "👑 리더 메뉴 (" + myTeam + " 팀)".toUpperCase());
        System.out.println("=".repeat(30));

        if (myTeam != null) {
            List<User> teamMembers = loginController.getUsersByTeam(myTeam);

            System.out.println("👥 우리 팀원 목록:");
            for (User member : teamMembers) {
                System.out.println("   • " + member.getUsername() + " (" + member.getRole() + ")");
            }
        } else {
            System.out.println("❌ 소속 팀 정보가 없습니다.");
        }

        pauseForUser("계속하려면 Enter를 눌러주세요...");
    }

    /**
     * 역할별 사용자 조회
     */
    private void showUsersByRole() {
        System.out.println("\n역할을 선택하세요:");
        System.out.println("1. admin");
        System.out.println("2. leader");
        System.out.println("3. member");
        System.out.print("선택: ");

        int choice = getChoice();
        String role = "";

        switch (choice) {
            case 1: role = "admin"; break;
            case 2: role = "leader"; break;
            case 3: role = "member"; break;
            default:
                System.out.println("❌ 잘못된 선택입니다.");
                return;
        }

        List<User> users = loginController.getUsersByRole(role);

        System.out.println("\n📋 " + role + " 역할 사용자 목록:");
        for (User user : users) {
            System.out.println("   • " + user.getUsername() + " (팀: " +
                    (user.getTeam() != null ? user.getTeam() : "없음") + ")");
        }

        pauseForUser("계속하려면 Enter를 눌러주세요...");
    }

    /**
     * 팀별 사용자 조회
     */
    private void showUsersByTeam() {
        System.out.print("팀명을 입력하세요: ");
        String team = scanner.nextLine().trim();

        if (team.isEmpty()) {
            System.out.println("❌ 팀명을 입력해주세요.");
            return;
        }

        List<User> users = loginController.getUsersByTeam(team);

        if (users.isEmpty()) {
            System.out.println("📭 " + team + " 팀에 속한 사용자가 없습니다.");
        } else {
            System.out.println("\n📋 " + team + " 팀 구성원:");
            for (User user : users) {
                System.out.println("   • " + user.getUsername() + " (" + user.getRole() + ")");
            }
        }

        pauseForUser("계속하려면 Enter를 눌러주세요...");
    }

    /**
     * 사용자 입력 받기 (숫자)
     */
    private int getChoice() {
        try {
            String input = scanner.nextLine().trim();
            if (input.isEmpty()) {
                return -1;
            }
            return Integer.parseInt(input);
        } catch (NumberFormatException e) {
            return -1;
        }
    }

    /**
     * 사용자 입력 대기 (일시정지)
     */
    private void pauseForUser(String message) {
        System.out.print("\n💡 " + message);
        scanner.nextLine();
    }

    /**
     * 프로그램 종료
     */
    private void exitProgram() {
        System.out.println("\n" + "👋 MeetingAgain 시스템을 종료합니다.");
        if (loginController.isLoggedIn()) {
            User currentUser = loginController.getCurrentUser();
            System.out.println("🔐 " + currentUser.getUsername() + "님이 자동으로 로그아웃됩니다.");
            loginController.logout();
        }
        System.out.println("💫 이용해 주셔서 감사합니다!");
        scanner.close();
        System.exit(0);
    }
}