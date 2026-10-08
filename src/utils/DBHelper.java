package utils;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.Locale;

public class DBHelper {
    private static final String PROFILE = profile();
    private static final boolean DEMO_PROFILE = "demo".equals(PROFILE);
    private static final String HOST = configuredSetting("HR_DB_HOST", "localhost");
    private static final String PORT = configuredSetting("HR_DB_PORT", "3307");
    private static final String DATABASE = configuredSetting("HR_DB_NAME", "quanlynhansu");
    private static final String USER = configuredSetting("HR_DB_USER", "root");
    private static final String PASSWORD = configuredPassword();
    private static final String SSL_MODE = DEMO_PROFILE ? "DISABLED" : "VERIFY_IDENTITY";
    private static final String URL = "jdbc:mysql://" + HOST + ":" + PORT + "/" + DATABASE
            + "?sslMode=" + SSL_MODE + "&serverTimezone=UTC&characterEncoding=UTF-8"
            + "&connectTimeout=10000&socketTimeout=15000";
    private static final String SERVER_URL = "jdbc:mysql://" + HOST + ":" + PORT
            + "/?sslMode=" + SSL_MODE + "&serverTimezone=UTC&characterEncoding=UTF-8"
            + "&connectTimeout=10000&socketTimeout=15000";

    static {
        validateConfiguration();
    }

    public static Connection getConnection() {
        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
            return DriverManager.getConnection(URL, USER, PASSWORD);
        } catch (ClassNotFoundException e) {
            throw new IllegalStateException("Không tìm thấy MySQL JDBC Driver trong classpath.", e);
        } catch (SQLException e) {
            throw new IllegalStateException(
                    "Không thể kết nối MySQL. Hãy kiểm tra HR_DB_HOST, HR_DB_PORT, HR_DB_NAME, "
                            + "HR_DB_USER, HR_DB_PASSWORD và trạng thái máy chủ.",
                    e);
        }
    }

    public static Connection getServerConnection() {
        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
            return DriverManager.getConnection(SERVER_URL, USER, PASSWORD);
        } catch (ClassNotFoundException e) {
            throw new IllegalStateException("Không tìm thấy MySQL JDBC Driver trong classpath.", e);
        } catch (SQLException e) {
            throw new IllegalStateException(
                    "Không thể kết nối MySQL Server. Hãy kiểm tra HR_DB_HOST, HR_DB_PORT, "
                            + "HR_DB_USER, HR_DB_PASSWORD và trạng thái máy chủ.",
                    e);
        }
    }

    public static String getDatabaseName() {
        return DATABASE;
    }

    public static boolean isDemoProfile() {
        return DEMO_PROFILE;
    }

    private static String profile() {
        String value = setting("HR_DB_PROFILE", "demo").toLowerCase(Locale.ROOT);
        if (!"demo".equals(value) && !"production".equals(value)) {
            throw new IllegalStateException("HR_DB_PROFILE chỉ nhận giá trị demo hoặc production.");
        }
        return value;
    }

    private static String configuredSetting(String name, String defaultValue) {
        return DEMO_PROFILE ? setting(name, defaultValue) : requiredSetting(name);
    }

    private static String requiredSetting(String name) {
        String value = System.getenv(name);
        if (value == null || value.trim().isEmpty()) {
            throw new IllegalStateException("Thiếu biến môi trường bắt buộc " + name
                    + " trong cấu hình production.");
        }
        return value.trim();
    }

    private static String configuredPassword() {
        if (DEMO_PROFILE) {
            return setting("HR_DB_PASSWORD", "");
        }
        String value = System.getenv("HR_DB_PASSWORD");
        if (value == null || value.trim().isEmpty()) {
            throw new IllegalStateException(
                    "Thiếu mật khẩu MySQL không rỗng trong cấu hình production.");
        }
        return value;
    }

    private static void validateConfiguration() {
        if (!HOST.matches("[A-Za-z0-9.-]+")) {
            throw new IllegalStateException("HR_DB_HOST không hợp lệ.");
        }
        try {
            int port = Integer.parseInt(PORT);
            if (port < 1 || port > 65535) {
                throw new IllegalStateException("HR_DB_PORT phải nằm trong khoảng 1-65535.");
            }
        } catch (NumberFormatException e) {
            throw new IllegalStateException("HR_DB_PORT phải là số nguyên hợp lệ.", e);
        }
        if (!DATABASE.matches("[A-Za-z0-9_]+")) {
            throw new IllegalStateException("HR_DB_NAME chỉ được chứa chữ, số và dấu gạch dưới.");
        }
        if (!DEMO_PROFILE && ("root".equalsIgnoreCase(USER) || PASSWORD.trim().isEmpty())) {
            throw new IllegalStateException(
                    "Cấu hình production phải dùng tài khoản MySQL riêng và mật khẩu không rỗng.");
        }
    }

    private static String setting(String name, String defaultValue) {
        String value = System.getenv(name);
        return value == null || value.trim().isEmpty() ? defaultValue : value.trim();
    }

    public static void closeConnection(Connection conn) {
        if (conn != null) {
            try {
                conn.close();
            } catch (SQLException e) {
                throw new IllegalStateException("Không thể đóng kết nối cơ sở dữ liệu.", e);
            }
        }
    }

    public static void main(String[] args) {
        closeConnection(getConnection());
    }
}
