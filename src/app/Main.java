package app;

import common.util.DBUtil;
import login.LoginView;
import login.LoginController;
import login.AuthService;

public class Main {

	public static void main(String[] args) {
		System.out.println("🚀 MeetingAgain 프로그램 시작");

		// 로그인 시스템 시작 (콘솔 버전) - DB 테스트는 AuthService에서 처리
		System.out.println("🔐 로그인 시스템 시작");

		try {
			// LoginView를 통한 콘솔 테스트
			LoginView loginView = new LoginView();
			loginView.start();

		} catch (Exception e) {
			System.err.println("❌ 프로그램 실행 중 오류 발생: " + e.getMessage());
			e.printStackTrace();
		}

		System.out.println("🏁 프로그램 종료");
	}

	/**
	 * GUI 팀원들을 위한 LoginController 사용 예제
	 */
	public static void exampleForGUITeam() {
		System.out.println("📖 GUI 팀원들을 위한 LoginController 사용 예제");

		LoginController loginController = new LoginController();

		// 로그인 예제
		LoginController.LoginResult loginResult = loginController.login("alice", "pass123");
		if (loginResult.isSuccess()) {
			System.out.println("✅ 로그인 성공: " + loginResult.getUser().getUsername());
		} else {
			System.out.println("❌ 로그인 실패: " + loginResult.getMessage());
		}

		// 회원가입 예제
		LoginController.RegisterResult registerResult =
				loginController.register("newuser", "password", "member", "C-Team");
		if (registerResult.isSuccess()) {
			System.out.println("✅ 회원가입 성공");
		} else {
			System.out.println("❌ 회원가입 실패: " + registerResult.getMessage());
		}

		// 사용자명 중복 체크 예제
		LoginController.UsernameCheckResult checkResult =
				loginController.checkUsername("alice");
		System.out.println("사용자명 체크 결과: " + checkResult.getMessage());

		// 권한 확인 예제
		if (loginController.isCurrentUserAdmin()) {
			System.out.println("현재 사용자는 관리자입니다.");
		}

		// 로그아웃 예제
		loginController.logout();
	}
}