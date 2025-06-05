package common.util;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.Properties;
import java.io.InputStream;

public class DBUtil {
    private static String url;
    private static String username;
    private static String password;

    static {
        try (InputStream input = DBUtil.class.getClassLoader().getResourceAsStream("db.properties")) {
            if (input == null) {
                System.out.println("❌ db.properties 파일을 classpath에서 찾을 수 없습니다.");
            } else {
                Properties prop = new Properties();
                prop.load(input);
                url = prop.getProperty("db.url");
                username = prop.getProperty("db.username");
                password = prop.getProperty("db.password");

                System.out.println("✅ DB 설정 로딩 성공");

                Class.forName("com.mysql.cj.jdbc.Driver");
            }

        } catch (Exception e) {
            System.out.println("❌ DB 설정 로딩 중 오류 발생");
            e.printStackTrace();
        }
    }

    public static Connection getConnection() throws SQLException {
        return DriverManager.getConnection(url, username, password);
    }

    public static void testConnection() {
        System.out.println("🧪 testConnection() 진입"); // 로그 추가

        try (Connection conn = getConnection()) {
            System.out.println("🔗 getConnection() 반환됨");

            if (conn != null && !conn.isClosed()) {
                System.out.println("DB 연결 성공!");
            } else {
                System.out.println("DB 연결 실패...");
            }

        } catch (SQLException e) {
            System.out.println("DB 연결 중 오류 발생:");
            e.printStackTrace();
        }
    }
}
