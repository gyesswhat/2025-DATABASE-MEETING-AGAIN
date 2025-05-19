package common.util;

import java.sql.*;
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
            Properties prop = new Properties();
            prop.load(input);
            url = prop.getProperty("db.url");
            username = prop.getProperty("db.username");
            password = prop.getProperty("db.password");

            Class.forName("com.mysql.cj.jdbc.Driver"); // JDBC 4.0 이상이면 생략 가능

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static Connection getConnection() throws SQLException {
        return DriverManager.getConnection(url, username, password);
    }

    public static void testConnection() {
        try (Connection conn = getConnection()) {
            if (conn != null && !conn.isClosed()) {
                System.out.println("✅ DB 연결 성공!");
            } else {
                System.out.println("❌ DB 연결 실패...");
            }
        } catch (SQLException e) {
            System.out.println("❌ DB 연결 중 오류 발생:");
            e.printStackTrace();
        }
    }
}
