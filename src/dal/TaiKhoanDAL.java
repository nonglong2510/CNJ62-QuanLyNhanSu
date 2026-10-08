package dal;

import model.TaiKhoan;
import utils.DBHelper;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class TaiKhoanDAL {

    public TaiKhoan getByUsername(String username) {
        TaiKhoan tk = null;
        String sql = "SELECT * FROM TaiKhoan WHERE TenDangNhap = ?";
        
        try (Connection conn = DBHelper.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            
            pstmt.setString(1, username);
            
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
            throw new IllegalStateException("Không thể tra cứu tài khoản.", e);
        }
        return tk;
    }

    public java.util.List<TaiKhoan> getAll() {
        java.util.List<TaiKhoan> list = new java.util.ArrayList<>();
        String sql = "SELECT t.*, n.HoTen FROM TaiKhoan t LEFT JOIN NhanVien n ON t.MaNV = n.MaNV";
        
        try (Connection conn = DBHelper.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql);
             ResultSet rs = pstmt.executeQuery()) {
            
            while (rs.next()) {
                TaiKhoan tk = new TaiKhoan();
                tk.setTenDangNhap(rs.getString("TenDangNhap"));
                tk.setMatKhau(rs.getString("MatKhau"));
                tk.setMaNV(rs.getString("MaNV"));
                tk.setQuyen(rs.getString("Quyen"));
                tk.setHoTen(rs.getString("HoTen") != null ? rs.getString("HoTen") : "Không xác định");
                list.add(tk);
            }
        } catch (SQLException e) {
            throw new IllegalStateException("Không thể tải danh sách tài khoản.", e);
        }
        return list;
    }

    public boolean insert(TaiKhoan tk) {
        String sql = "INSERT INTO TaiKhoan(TenDangNhap, MatKhau, MaNV, Quyen) VALUES(?, ?, ?, ?)";
        try (Connection conn = DBHelper.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            
            pstmt.setString(1, tk.getTenDangNhap());
            pstmt.setString(2, tk.getMatKhau());
            pstmt.setString(3, tk.getMaNV());
            pstmt.setString(4, tk.getQuyen());
            return pstmt.executeUpdate() > 0;
            
        } catch (SQLException e) {
            throw new IllegalStateException("Không thể tạo tài khoản.", e);
        }
    }

    public boolean update(TaiKhoan tk) {
        String sql = "UPDATE TaiKhoan SET MatKhau = ?, MaNV = ?, Quyen = ? WHERE TenDangNhap = ?";
        try (Connection conn = DBHelper.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            
            pstmt.setString(1, tk.getMatKhau());
            pstmt.setString(2, tk.getMaNV());
            pstmt.setString(3, tk.getQuyen());
            pstmt.setString(4, tk.getTenDangNhap());
            return pstmt.executeUpdate() > 0;
            
        } catch (SQLException e) {
            throw new IllegalStateException("Không thể cập nhật tài khoản.", e);
        }
    }

    public boolean updatePassword(String username, String passwordHash) {
        String sql = "UPDATE TaiKhoan SET MatKhau = ? WHERE TenDangNhap = ?";
        try (Connection conn = DBHelper.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setString(1, passwordHash);
            pstmt.setString(2, username);
            return pstmt.executeUpdate() > 0;
        } catch (SQLException e) {
            throw new IllegalStateException("Không thể cập nhật mật khẩu tài khoản.", e);
        }
    }

    public boolean delete(String username) {
        String sql = "DELETE FROM TaiKhoan WHERE TenDangNhap = ?";
        try (Connection conn = DBHelper.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            
            pstmt.setString(1, username);
            return pstmt.executeUpdate() > 0;
            
        } catch (SQLException e) {
            throw new IllegalStateException("Không thể xóa tài khoản.", e);
        }
    }
}
