package dal;

import model.ChamCong;
import utils.DBHelper;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class ChamCongDAL {

    // Lấy toàn bộ danh sách chấm công kết hợp thông tin Nhân viên và Phòng ban
    public List<ChamCong> getAll() {
        List<ChamCong> list = new ArrayList<>();
        String sql = "SELECT c.*, n.HoTen, p.TenPB " +
                     "FROM ChamCong c " +
                     "JOIN NhanVien n ON c.MaNV = n.MaNV " +
                     "LEFT JOIN PhongBan p ON n.MaPB = p.MaPB " +
                     "ORDER BY c.NgayChamCong DESC, c.MaNV ASC";
        
        try (Connection conn = DBHelper.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql);
             ResultSet rs = pstmt.executeQuery()) {
            
            while (rs.next()) {
                ChamCong cc = new ChamCong();
                cc.setMaCC(rs.getInt("MaCC"));
                cc.setMaNV(rs.getString("MaNV"));
                cc.setNgayChamCong(rs.getDate("NgayChamCong"));
                cc.setTrangThai(rs.getString("TrangThai"));
                cc.setSoGioLamThem(rs.getDouble("SoGioLamThem"));
                cc.setGhiChu(rs.getString("GhiChu"));
                
                // Trường JOIN
                cc.setHoTen(rs.getString("HoTen"));
                cc.setTenPB(rs.getString("TenPB") != null ? rs.getString("TenPB") : "Chưa xếp phòng");
                
                list.add(cc);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return list;
    }

    // Lấy tổng số ngày công của một nhân viên trong tháng
    public int getTotalWorkDays(String maNV, int month, int year) {
        String sql = "SELECT COUNT(*) AS TotalDays FROM ChamCong " +
                     "WHERE MaNV = ? AND MONTH(NgayChamCong) = ? AND YEAR(NgayChamCong) = ? " +
                     "AND TrangThai IN ('Đi làm', 'Đi trễ')";
        int total = 0;
        try (Connection conn = DBHelper.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            
            pstmt.setString(1, maNV);
            pstmt.setInt(2, month);
            pstmt.setInt(3, year);
            
            try (ResultSet rs = pstmt.executeQuery()) {
                if (rs.next()) {
                    total = rs.getInt("TotalDays");
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return total;
    }

    // Lấy báo cáo tổng hợp công trong tháng của tất cả nhân viên
    public List<Object[]> getTongHopCongThang(int month, int year) {
        List<Object[]> list = new ArrayList<>();
        String sql = "SELECT n.MaNV, n.HoTen, p.TenPB, " +
                     "       (SELECT COUNT(*) FROM ChamCong c WHERE c.MaNV = n.MaNV AND MONTH(NgayChamCong) = ? AND YEAR(NgayChamCong) = ? AND TrangThai IN ('Đi làm', 'Đi trễ')) as NgayCong, " +
                     "       (SELECT COUNT(*) FROM ChamCong c WHERE c.MaNV = n.MaNV AND MONTH(NgayChamCong) = ? AND YEAR(NgayChamCong) = ? AND TrangThai = 'Nghỉ phép') as NghiPhep " +
                     "FROM NhanVien n " +
                     "LEFT JOIN PhongBan p ON n.MaPB = p.MaPB";
                     
        try (Connection conn = DBHelper.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            
            pstmt.setInt(1, month);
            pstmt.setInt(2, year);
            pstmt.setInt(3, month);
            pstmt.setInt(4, year);
            
            try (ResultSet rs = pstmt.executeQuery()) {
                while (rs.next()) {
                    String maNV = rs.getString("MaNV");
                    String hoTen = rs.getString("HoTen");
                    String tenPB = rs.getString("TenPB") != null ? rs.getString("TenPB") : "Chưa có phòng ban";
                    int ngayCong = rs.getInt("NgayCong");
                    int nghiPhep = rs.getInt("NghiPhep");
                    
                    list.add(new Object[]{ maNV, hoTen, tenPB, ngayCong, nghiPhep });
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return list;
    }
}
