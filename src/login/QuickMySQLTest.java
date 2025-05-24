package login;

import java.sql.*;
import java.util.Properties;

/**
 * 빠른 MySQL 연결 테스트 (타임아웃 포함)
 */
public class QuickMySQLTest {

    private static final String URL = "jdbc:mysql://meeting-again-db.ctc4a4ui64k9.ap-northeast-2.rds.amazonaws.com:3306/meetingdb?useSSL=false&serverTimezone=UTC";
    private static final String USERNAME = "admin";
    private static final String PASSWORD = "meetingagain";

    public static void main(String[] args) {
        System.out.println("⚡ 빠른 MySQL 연결 테스트 (타임아웃 포함)");
        System.out.println("=".repeat(50));

        // 1. 드라이버 확인 (이미 성공했으니 간단히)
        testDriverQuick();

        // 2. 타임아웃 설정으로 연결 테스트
        testConnectionWithTimeout();

        // 3. 대안 제시
        suggestAlternatives();
    }

    /**
     * 드라이버 빠른 확인
     */
    private static void testDriverQuick() {
        System.out.println("1️⃣ 드라이버 확인");
        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
            System.out.println("✅ MySQL 드라이버 OK");
        } catch (Exception e) {
            System.out.println("❌ 드라이버 문제: " + e.getMessage());
            return;
        }
    }

    /**
     * 타임아웃 설정으로 연결 테스트
     */
    private static void testConnectionWithTimeout() {
        System.out.println("\n2️⃣ RDS 연결 테스트 (10초 타임아웃)");
        System.out.println("🔗 " + URL.split("\\?")[0]);
        System.out.println("⏰ 최대 10초 대기...");

        Properties props = new Properties();
        props.setProperty("user", USERNAME);
        props.setProperty("password", PASSWORD);
        props.setProperty("connectTimeout", "10000");  // 10초
        props.setProperty("socketTimeout", "10000");   // 10초

        try {
            System.out.print("🔄 연결 시도 중");

            // 별도 스레드로 점 찍기
            Thread dotThread = new Thread(() -> {
                try {
                    for (int i = 0; i < 20; i++) {
                        Thread.sleep(500);
                        System.out.print(".");
                    }
                } catch (InterruptedException e) {
                    // 무시
                }
            });
            dotThread.start();

            Connection conn = DriverManager.getConnection(URL, props);

            dotThread.interrupt();
            System.out.println("\n✅ RDS 연결 성공!");

            // 간단한 쿼리 테스트
            testSimpleQuery(conn);

            conn.close();

        } catch (SQLException e) {
            System.out.println("\n❌ RDS 연결 실패!");
            System.out.println("📋 오류: " + e.getMessage());

            // 구체적인 오류 분석
            analyzeConnectionError(e);
        }
    }

    /**
     * 간단한 쿼리 테스트
     */
    private static void testSimpleQuery(Connection conn) {
        System.out.println("🧪 간단한 쿼리 테스트...");

        try {
            String sql = "SELECT 1 as test";
            PreparedStatement pstmt = conn.prepareStatement(sql);
            ResultSet rs = pstmt.executeQuery();

            if (rs.next()) {
                System.out.println("✅ 쿼리 실행 성공!");

                // 테이블 존재 확인
                testTableExists(conn);
            }

        } catch (SQLException e) {
            System.out.println("❌ 쿼리 실행 실패: " + e.getMessage());
        }
    }

    /**
     * User 테이블 존재 확인
     */
    private static void testTableExists(Connection conn) {
        System.out.println("🔍 User 테이블 확인...");

        try {
            String sql = "SELECT COUNT(*) FROM User";
            PreparedStatement pstmt = conn.prepareStatement(sql);
            ResultSet rs = pstmt.executeQuery();

            if (rs.next()) {
                int count = rs.getInt(1);
                System.out.println("✅ User 테이블 존재! (데이터 " + count + "개)");

                if (count > 0) {
                    System.out.println("🎉 로그인 시스템 바로 연결 가능!");
                } else {
                    System.out.println("💡 User 테이블은 있지만 데이터가 없음");
                }
            }

        } catch (SQLException e) {
            if (e.getMessage().contains("doesn't exist")) {
                System.out.println("❌ User 테이블이 없습니다");
                System.out.println("💡 테이블 생성 필요");
            } else {
                System.out.println("❌ 테이블 확인 실패: " + e.getMessage());
            }
        }
    }

    /**
     * 연결 오류 분석
     */
    private static void analyzeConnectionError(SQLException e) {
        String message = e.getMessage().toLowerCase();

        System.out.println("\n🔍 오류 분석:");

        if (message.contains("timeout") || message.contains("timed out")) {
            System.out.println("⏰ 타임아웃 오류 - 네트워크 또는 방화벽 문제");
            System.out.println("💡 해결 방법:");
            System.out.println("   - RDS 보안 그룹에서 3306 포트 허용");
            System.out.println("   - 네트워크 연결 확인");
            System.out.println("   - VPN 사용 여부 확인");
        } else if (message.contains("access denied")) {
            System.out.println("🔐 인증 오류 - 사용자명/비밀번호 문제");
            System.out.println("💡 해결 방법:");
            System.out.println("   - RDS 마스터 사용자명/비밀번호 확인");
            System.out.println("   - 팀원에게 정확한 접속 정보 요청");
        } else if (message.contains("unknown host")) {
            System.out.println("🌐 호스트 오류 - RDS 엔드포인트 문제");
            System.out.println("💡 해결 방법:");
            System.out.println("   - RDS 인스턴스가 실행 중인지 확인");
            System.out.println("   - 엔드포인트 주소 재확인");
        } else {
            System.out.println("❓ 기타 오류: " + message);
        }
    }

    /**
     * 대안 제시
     */
    private static void suggestAlternatives() {
        System.out.println("\n🚀 대안 방법들:");
        System.out.println("1️⃣ 더미 데이터로 계속 개발 (권장)");
        System.out.println("   - 현재 DummyAuthService 완벽 작동");
        System.out.println("   - 팀 프로젝트 진행에 지장 없음");

        System.out.println("\n2️⃣ 팀원과 RDS 설정 확인");
        System.out.println("   - 보안 그룹 설정 확인");
        System.out.println("   - 정확한 접속 정보 재확인");

        System.out.println("\n3️⃣ 로컬 MySQL 설치");
        System.out.println("   - Homebrew: brew install mysql");
        System.out.println("   - 로컬에서 개발 후 나중에 RDS 연결");

        System.out.println("\n4️⃣ 하이브리드 접근");
        System.out.println("   - 더미 데이터 + DB 연결 코드 동시 유지");
        System.out.println("   - 설정으로 전환 가능하게 구현");
    }
}