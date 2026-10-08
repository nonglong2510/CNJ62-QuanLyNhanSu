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
            throw new IllegalStateException("Không thể tải danh sách chấm công.", e);
        }
        return list;
    }

    public List<ChamCong> getByEmployee(String maNV, int month, int year) {
        List<ChamCong> list = new ArrayList<>();
        String sql = "SELECT c.*, n.HoTen, p.TenPB FROM ChamCong c "
                + "JOIN NhanVien n ON c.MaNV = n.MaNV "
                + "LEFT JOIN PhongBan p ON n.MaPB = p.MaPB "
                + "WHERE c.MaNV = ? AND MONTH(c.NgayChamCong) = ? AND YEAR(c.NgayChamCong) = ? "
                + "ORDER BY c.NgayChamCong DESC";
        try (Connection conn = DBHelper.getConnection();
             PreparedStatement statement = conn.prepareStatement(sql)) {
            statement.setString(1, maNV);
            statement.setInt(2, month);
            statement.setInt(3, year);
            try (ResultSet rs = statement.executeQuery()) {
                while (rs.next()) {
                    ChamCong attendance = new ChamCong();
                    attendance.setMaCC(rs.getInt("MaCC"));
                    attendance.setMaNV(rs.getString("MaNV"));
                    attendance.setNgayChamCong(rs.getDate("NgayChamCong"));
                    attendance.setTrangThai(rs.getString("TrangThai"));
                    attendance.setSoGioLamThem(rs.getDouble("SoGioLamThem"));
                    attendance.setGhiChu(rs.getString("GhiChu"));
                    attendance.setHoTen(rs.getString("HoTen"));
                    attendance.setTenPB(rs.getString("TenPB"));
                    list.add(attendance);
                }
            }
        } catch (SQLException e) {
            throw new IllegalStateException("Không thể tải lịch sử chấm công cá nhân.", e);
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
            throw new IllegalStateException("Không thể tính ngày công.", e);
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
            throw new IllegalStateException("Không thể tải bảng tổng hợp công.", e);
        }
        return list;
    }

    // Lấy danh sách điểm danh của tất cả nhân viên trong 1 ngày cụ thể
    public List<Object[]> getDiemDanhNgay(Date date) {
        List<Object[]> list = new ArrayList<>();
        String sql = "SELECT n.MaNV, n.HoTen, p.TenPB, c.TrangThai, c.SoGioLamThem, c.GhiChu " +
                     "FROM NhanVien n " +
                     "LEFT JOIN PhongBan p ON n.MaPB = p.MaPB " +
                     "LEFT JOIN ChamCong c ON n.MaNV = c.MaNV AND c.NgayChamCong = ? " +
                     "WHERE n.TrangThai = 'Đang làm việc' " +
                     "ORDER BY n.MaNV";

        try (Connection conn = DBHelper.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setDate(1, date);

            try (ResultSet rs = pstmt.executeQuery()) {
                while (rs.next()) {
                    String maNV = rs.getString("MaNV");
                    String hoTen = rs.getString("HoTen");
                    String tenPB = rs.getString("TenPB") != null ? rs.getString("TenPB") : "Chưa có phòng ban";
                    String trangThai = rs.getString("TrangThai") != null ? rs.getString("TrangThai") : "Chưa điểm danh";
                    double ot = rs.getDouble("SoGioLamThem");
                    String ghiChu = rs.getString("GhiChu") != null ? rs.getString("GhiChu") : "";

                    list.add(new Object[]{ maNV, hoTen, tenPB, trangThai, ot, ghiChu });
                }
            }
        } catch (SQLException e) {
            throw new IllegalStateException("Không thể tải danh sách điểm danh.", e);
        }
        return list;
    }

    // Thêm mới hoặc Cập nhật điểm danh
    public boolean upsertDiemDanh(String maNV, Date date, String trangThai, double ot, String ghiChu) {
        String sql = "INSERT INTO ChamCong (MaNV, NgayChamCong, TrangThai, SoGioLamThem, GhiChu) "
                + "VALUES (?, ?, ?, ?, ?) ON DUPLICATE KEY UPDATE TrangThai = VALUES(TrangThai), "
                + "SoGioLamThem = VALUES(SoGioLamThem), GhiChu = VALUES(GhiChu)";
        try (Connection conn = DBHelper.getConnection();
             PreparedStatement statement = conn.prepareStatement(sql)) {
            statement.setString(1, maNV);
            statement.setDate(2, date);
            statement.setString(3, trangThai);
            statement.setDouble(4, ot);
            statement.setString(5, ghiChu);
            return statement.executeUpdate() > 0;
        } catch (SQLException e) {
            throw new IllegalStateException("Không thể cập nhật chấm công.", e);
        }
    }
}
