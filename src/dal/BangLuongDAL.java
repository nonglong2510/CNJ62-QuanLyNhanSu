package dal;

import model.BangLuong;
import utils.DBHelper;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class BangLuongDAL {

    public List<BangLuong> getByMonth(int month, int year) {
        List<BangLuong> list = new ArrayList<>();
        String sql = "SELECT b.*, n.HoTen, p.TenPB " +
                     "FROM BangLuong b " +
                     "JOIN NhanVien n ON b.MaNV = n.MaNV " +
                     "LEFT JOIN PhongBan p ON n.MaPB = p.MaPB " +
                     "WHERE b.Thang = ? AND b.Nam = ? " +
                     "ORDER BY b.MaNV ASC";
                     
        try (Connection conn = DBHelper.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            
            pstmt.setInt(1, month);
            pstmt.setInt(2, year);
            
            try (ResultSet rs = pstmt.executeQuery()) {
                while (rs.next()) {
                    BangLuong bl = new BangLuong();
                    bl.setMaLuong(rs.getInt("MaLuong"));
                    bl.setMaNV(rs.getString("MaNV"));
                    bl.setThang(rs.getInt("Thang"));
                    bl.setNam(rs.getInt("Nam"));
                    bl.setLuongCoBan(rs.getDouble("LuongCoBan"));
                    bl.setSoNgayCong(rs.getInt("SoNgayCong"));
                    bl.setTongPhuCap(rs.getDouble("TongPhuCap"));
                    bl.setTienThuong(rs.getDouble("TienThuong"));
                    bl.setTienPhat(rs.getDouble("TienPhat"));
                    bl.setThucLanh(rs.getDouble("ThucLanh"));
                    bl.setNgayTinhLuong(rs.getDate("NgayTinhLuong"));
                    
                    bl.setHoTen(rs.getString("HoTen"));
                    bl.setTenPB(rs.getString("TenPB") != null ? rs.getString("TenPB") : "Chưa xếp phòng");
                    
                    list.add(bl);
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return list;
    }

    public boolean insertOrUpdate(BangLuong bl) {
        String checkSql = "SELECT MaLuong FROM BangLuong WHERE MaNV = ? AND Thang = ? AND Nam = ?";
        String insertSql = "INSERT INTO BangLuong (MaNV, Thang, Nam, LuongCoBan, SoNgayCong, TongPhuCap, TienThuong, TienPhat, ThucLanh, NgayTinhLuong) VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";
        String updateSql = "UPDATE BangLuong SET LuongCoBan = ?, SoNgayCong = ?, TongPhuCap = ?, TienThuong = ?, TienPhat = ?, ThucLanh = ?, NgayTinhLuong = ? WHERE MaNV = ? AND Thang = ? AND Nam = ?";
        
        try (Connection conn = DBHelper.getConnection()) {
            boolean exists = false;
            try (PreparedStatement pstmt = conn.prepareStatement(checkSql)) {
                pstmt.setString(1, bl.getMaNV());
                pstmt.setInt(2, bl.getThang());
                pstmt.setInt(3, bl.getNam());
                try (ResultSet rs = pstmt.executeQuery()) {
                    if (rs.next()) exists = true;
                }
            }
            
            if (exists) {
                try (PreparedStatement pstmt = conn.prepareStatement(updateSql)) {
                    pstmt.setDouble(1, bl.getLuongCoBan());
                    pstmt.setInt(2, bl.getSoNgayCong());
                    pstmt.setDouble(3, bl.getTongPhuCap());
                    pstmt.setDouble(4, bl.getTienThuong());
                    pstmt.setDouble(5, bl.getTienPhat());
                    pstmt.setDouble(6, bl.getThucLanh());
                    pstmt.setDate(7, bl.getNgayTinhLuong());
                    pstmt.setString(8, bl.getMaNV());
                    pstmt.setInt(9, bl.getThang());
                    pstmt.setInt(10, bl.getNam());
                    return pstmt.executeUpdate() > 0;
                }
            } else {
                try (PreparedStatement pstmt = conn.prepareStatement(insertSql)) {
                    pstmt.setString(1, bl.getMaNV());
                    pstmt.setInt(2, bl.getThang());
                    pstmt.setInt(3, bl.getNam());
                    pstmt.setDouble(4, bl.getLuongCoBan());
                    pstmt.setInt(5, bl.getSoNgayCong());
                    pstmt.setDouble(6, bl.getTongPhuCap());
                    pstmt.setDouble(7, bl.getTienThuong());
                    pstmt.setDouble(8, bl.getTienPhat());
                    pstmt.setDouble(9, bl.getThucLanh());
                    pstmt.setDate(10, bl.getNgayTinhLuong());
                    return pstmt.executeUpdate() > 0;
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }
}
