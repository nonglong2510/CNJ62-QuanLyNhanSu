package dal;

import model.PhongBan;
import utils.DBHelper;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class PhongBanDAL {
    
    public List<PhongBan> getAll() {
        List<PhongBan> list = new ArrayList<>();
        String sql = "SELECT * FROM PhongBan";
        
        try (Connection conn = DBHelper.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql);
             ResultSet rs = pstmt.executeQuery()) {
            
            while (rs.next()) {
                PhongBan pb = new PhongBan();
                pb.setMaPB(rs.getString("MaPB"));
                pb.setTenPB(rs.getString("TenPB"));
                pb.setSoDienThoai(rs.getString("SoDienThoai"));
                list.add(pb);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return list;
    }

    public boolean insert(PhongBan phongBan) {
        String sql = "INSERT INTO PhongBan (MaPB, TenPB, SoDienThoai) VALUES (?, ?, ?)";
        try (Connection conn = DBHelper.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setString(1, phongBan.getMaPB());
            pstmt.setString(2, phongBan.getTenPB());
            pstmt.setString(3, phongBan.getSoDienThoai());
            return pstmt.executeUpdate() == 1;
        } catch (SQLException e) {
            throw new IllegalStateException("Không thể thêm phòng ban.", e);
        }
    }

    public boolean update(PhongBan phongBan) {
        String sql = "UPDATE PhongBan SET TenPB = ?, SoDienThoai = ? WHERE MaPB = ?";
        try (Connection conn = DBHelper.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setString(1, phongBan.getTenPB());
            pstmt.setString(2, phongBan.getSoDienThoai());
            pstmt.setString(3, phongBan.getMaPB());
            return pstmt.executeUpdate() == 1;
        } catch (SQLException e) {
            throw new IllegalStateException("Không thể cập nhật phòng ban.", e);
        }
    }

    public boolean delete(String maPB) {
        String sql = "DELETE FROM PhongBan WHERE MaPB = ?";
        try (Connection conn = DBHelper.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setString(1, maPB);
            return pstmt.executeUpdate() == 1;
        } catch (SQLException e) {
            throw new IllegalStateException("Không thể xóa phòng ban đang được nhân viên sử dụng.", e);
        }
    }
}
