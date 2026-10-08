package dal;

import model.ChucVu;
import utils.DBHelper;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class ChucVuDAL {
    
    public List<ChucVu> getAll() {
        List<ChucVu> list = new ArrayList<>();
        String sql = "SELECT * FROM ChucVu";
        
        try (Connection conn = DBHelper.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql);
             ResultSet rs = pstmt.executeQuery()) {
            
            while (rs.next()) {
                ChucVu cv = new ChucVu();
                cv.setMaCV(rs.getString("MaCV"));
                cv.setTenCV(rs.getString("TenCV"));
                cv.setPhuCapChucVu(rs.getDouble("PhuCapChucVu"));
                list.add(cv);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return list;
    }

    public boolean insert(ChucVu chucVu) {
        String sql = "INSERT INTO ChucVu (MaCV, TenCV, PhuCapChucVu) VALUES (?, ?, ?)";
        try (Connection conn = DBHelper.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setString(1, chucVu.getMaCV());
            pstmt.setString(2, chucVu.getTenCV());
            pstmt.setDouble(3, chucVu.getPhuCapChucVu());
            return pstmt.executeUpdate() == 1;
        } catch (SQLException e) {
            throw new IllegalStateException("Không thể thêm chức vụ.", e);
        }
    }

    public boolean update(ChucVu chucVu) {
        String sql = "UPDATE ChucVu SET TenCV = ?, PhuCapChucVu = ? WHERE MaCV = ?";
        try (Connection conn = DBHelper.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setString(1, chucVu.getTenCV());
            pstmt.setDouble(2, chucVu.getPhuCapChucVu());
            pstmt.setString(3, chucVu.getMaCV());
            return pstmt.executeUpdate() == 1;
        } catch (SQLException e) {
            throw new IllegalStateException("Không thể cập nhật chức vụ.", e);
        }
    }

    public boolean delete(String maCV) {
        String sql = "DELETE FROM ChucVu WHERE MaCV = ?";
        try (Connection conn = DBHelper.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setString(1, maCV);
            return pstmt.executeUpdate() == 1;
        } catch (SQLException e) {
            throw new IllegalStateException("Không thể xóa chức vụ đang được nhân viên sử dụng.", e);
        }
    }
}
