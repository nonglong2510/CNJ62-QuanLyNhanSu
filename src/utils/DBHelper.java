package utils;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class DBHelper {
    // Thông tin cấu hình CSDL
    private static final String HOST = "localhost";
    private static final String PORT = "3307"; // Đổi thành 3307 theo cấu hình XAMPP của bạn
    private static final String DATABASE = "quanlynhansu"; // Tên database khớp với script SQL
    
    // URL kết nối (thêm useSSL=false và serverTimezone để tránh lỗi với MySQL bản mới)
    private static final String URL = "jdbc:mysql://" + HOST + ":" + PORT + "/" + DATABASE + "?useSSL=false&serverTimezone=UTC&characterEncoding=UTF-8";
    
    // Tên đăng nhập và mật khẩu MySQL của XAMPP (mặc định là root và rỗng)
    // Bạn hãy thay đổi nếu MySQL của bạn có mật khẩu nhé!
    private static final String USER = "root";
    private static final String PASSWORD = ""; 

    /**
     * Phương thức kết nối đến cơ sở dữ liệu MySQL
     * @return Đối tượng Connection
     */
    public static Connection getConnection() {
        Connection conn = null;
        try {
            // Load MySQL JDBC Driver (Thư viện mysql-connector-java)
            Class.forName("com.mysql.cj.jdbc.Driver");
            
            // Thiết lập kết nối
            conn = DriverManager.getConnection(URL, USER, PASSWORD);
            System.out.println("✅ Kết nối cơ sở dữ liệu thành công!");
        } catch (ClassNotFoundException e) {
            System.err.println("❌ Không tìm thấy Driver MySQL: " + e.getMessage());
        } catch (SQLException e) {
            System.err.println("❌ Lỗi kết nối CSDL: " + e.getMessage());
        }
        return conn;
    }

    /**
     * Phương thức đóng kết nối CSDL an toàn
     * @param conn Đối tượng Connection cần đóng
     */
    public static void closeConnection(Connection conn) {
        if (conn != null) {
            try {
                conn.close();
                System.out.println("🔒 Đã đóng kết nối CSDL.");
            } catch (SQLException e) {
                System.err.println("❌ Lỗi khi đóng kết nối: " + e.getMessage());
            }
        }
    }
    
    // Hàm main dùng để chạy test nhanh xem kết nối có hoạt động không
    public static void main(String[] args) {
        Connection conn = getConnection();
        if (conn != null) {
            closeConnection(conn);
        }
    }
}
