package dal;

import model.TaiKhoan;
import utils.DBHelper;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class TaiKhoanDAL {

    // Phương thức kiểm tra đăng nhập
    public TaiKhoan login(String username, String password) {
        TaiKhoan tk = null;
        String sql = "SELECT * FROM TaiKhoan WHERE TenDangNhap = ? AND MatKhau = ?";
        
        try (Connection conn = DBHelper.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            
            pstmt.setString(1, username);
            pstmt.setString(2, password);
            
            try (ResultSet rs = pstmt.executeQuery()) {
                if (rs.next()) {
                    tk = new TaiKhoan();
                    tk.setTenDangNhap(rs.getString("TenDangNhap"));
                    tk.setMatKhau(rs.getString("MatKhau"));
                    tk.setMaNV(rs.getString("MaNV"));
                    tk.setQuyen(rs.getString("Quyen"));
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return tk; // Nếu đăng nhập sai sẽ trả về null
    }
}
