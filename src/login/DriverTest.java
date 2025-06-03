package login;


/**
 * MySQL 드라이버 테스트 클래스
 * aws 연결 안돼서 테스트하는 용도로 만들었음
 */
public class DriverTest {

    public static void main(String[] args) {
        System.out.println("🧪 MySQL 드라이버 테스트");

        // 1. 드라이버 클래스 로드 테스트
        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
            System.out.println("✅ MySQL 드라이버 로드 성공!");
        } catch (ClassNotFoundException e) {
            System.out.println("❌ MySQL 드라이버 로드 실패: " + e.getMessage());
            System.out.println("💡 mysql-connector-java JAR 파일이 classpath에 없습니다.");
            return;
        }

        // 2. 시스템 정보 출력
        System.out.println("\n📋 시스템 정보:");
        System.out.println("Java Version: " + System.getProperty("java.version"));
        System.out.println("Classpath: " + System.getProperty("java.class.path"));

        // 3. 간단한 연결 테스트 (드라이버만)
        System.out.println("\n🔗 연결 테스트 (드라이버만)");
        try {
            java.sql.DriverManager.registerDriver(new com.mysql.cj.jdbc.Driver());
            System.out.println("✅ MySQL 드라이버 등록 성공!");
        } catch (Exception e) {
            System.out.println("❌ 드라이버 등록 실패: " + e.getMessage());
        }
    }
}