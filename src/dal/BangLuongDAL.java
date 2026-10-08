package dal;

import model.BangLuong;
import utils.DBHelper;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class BangLuongDAL {

    public List<BangLuong> getByMonth(int month, int year) {
        return findByMonth(month, year, null);
    }

    public List<BangLuong> getByMonthAndEmployee(int month, int year, String maNV) {
        return findByMonth(month, year, maNV);
    }

    private List<BangLuong> findByMonth(int month, int year, String maNV) {
        List<BangLuong> list = new ArrayList<>();
        String sql = "SELECT b.*, n.HoTen, p.TenPB " +
                     "FROM BangLuong b " +
                     "JOIN NhanVien n ON b.MaNV = n.MaNV " +
                     "LEFT JOIN PhongBan p ON n.MaPB = p.MaPB " +
                     "WHERE b.Thang = ? AND b.Nam = ? " +
                     (maNV == null ? "" : "AND b.MaNV = ? ") +
                     "ORDER BY b.MaNV ASC";

        try (Connection conn = DBHelper.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setInt(1, month);
            pstmt.setInt(2, year);
            if (maNV != null) {
                pstmt.setString(3, maNV);
            }

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
            throw new IllegalStateException("Không thể tải bảng lương theo kỳ.", e);
        }
        return list;
    }

    public boolean insertOrUpdate(BangLuong bl) {
        String sql = "INSERT INTO BangLuong "
                + "(MaNV, Thang, Nam, LuongCoBan, SoNgayCong, TongPhuCap, TienThuong, "
                + "TienPhat, ThucLanh, NgayTinhLuong) VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?) "
                + "ON DUPLICATE KEY UPDATE LuongCoBan = VALUES(LuongCoBan), "
                + "SoNgayCong = VALUES(SoNgayCong), TongPhuCap = VALUES(TongPhuCap), "
                + "TienThuong = VALUES(TienThuong), TienPhat = VALUES(TienPhat), "
                + "ThucLanh = VALUES(ThucLanh), NgayTinhLuong = VALUES(NgayTinhLuong)";
        try (Connection conn = DBHelper.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
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
        } catch (SQLException e) {
            throw new IllegalStateException("Không thể ghi bảng lương.", e);
        }
    }

    public void insertOrUpdateAll(List<BangLuong> salaries) {
            if (salaries.isEmpty()) return;
            String sql = "INSERT INTO BangLuong "
                    + "(MaNV, Thang, Nam, LuongCoBan, SoNgayCong, TongPhuCap, TienThuong, "
                    + "TienPhat, ThucLanh, NgayTinhLuong) VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?) "
                    + "ON DUPLICATE KEY UPDATE LuongCoBan = VALUES(LuongCoBan), "
                    + "SoNgayCong = VALUES(SoNgayCong), TongPhuCap = VALUES(TongPhuCap), "
                    + "TienThuong = VALUES(TienThuong), TienPhat = VALUES(TienPhat), "
                    + "ThucLanh = VALUES(ThucLanh), NgayTinhLuong = VALUES(NgayTinhLuong)";
            try (Connection conn = DBHelper.getConnection();
                 PreparedStatement statement = conn.prepareStatement(sql)) {
                boolean originalAutoCommit = conn.getAutoCommit();
                conn.setAutoCommit(false);
                try {
                    for (BangLuong salary : salaries) {
                        statement.setString(1, salary.getMaNV());
                        statement.setInt(2, salary.getThang());
                        statement.setInt(3, salary.getNam());
                        statement.setDouble(4, salary.getLuongCoBan());
                        statement.setInt(5, salary.getSoNgayCong());
                        statement.setDouble(6, salary.getTongPhuCap());
                        statement.setDouble(7, salary.getTienThuong());
                        statement.setDouble(8, salary.getTienPhat());
                        statement.setDouble(9, salary.getThucLanh());
                        statement.setDate(10, salary.getNgayTinhLuong());
                        statement.addBatch();
                    }
                    statement.executeBatch();
                    conn.commit();
                } catch (SQLException e) {
                    conn.rollback();
                    throw e;
                } finally {
                    conn.setAutoCommit(originalAutoCommit);
                }
            } catch (SQLException e) {
                throw new IllegalStateException("Không thể lưu đồng bộ toàn bộ bảng lương.", e);
        }
    }

    public List<Object[]> getDanhSachLuongCoBan() {
        List<Object[]> list = new ArrayList<>();
        String sql = "SELECT n.MaNV, n.HoTen, p.TenPB, c.TenCV, n.HeSoLuong, c.PhuCapChucVu " +
                     "FROM NhanVien n " +
                     "LEFT JOIN PhongBan p ON n.MaPB = p.MaPB " +
                     "LEFT JOIN ChucVu c ON n.MaCV = c.MaCV " +
                     "WHERE n.TrangThai = 'Đang làm việc' " +
                     "ORDER BY n.MaNV";
        try (Connection conn = DBHelper.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql);
             ResultSet rs = pstmt.executeQuery()) {

            double mucLuongTheoThangDuCong = 5_000_000;

            while (rs.next()) {
                String maNV = rs.getString("MaNV");
                String hoTen = rs.getString("HoTen");
                String viTri = (rs.getString("TenPB") != null ? rs.getString("TenPB") : "") + " - " +
                               (rs.getString("TenCV") != null ? rs.getString("TenCV") : "");
                double heSo = rs.getDouble("HeSoLuong");
                double phuCapCV = rs.getDouble("PhuCapChucVu");

                double luongCoBan = heSo * mucLuongTheoThangDuCong;
                double tongThuNhap = luongCoBan + phuCapCV;

                list.add(new Object[]{
                    maNV, hoTen, viTri, heSo, luongCoBan, phuCapCV, tongThuNhap
                });
            }
        } catch (SQLException e) {
            throw new IllegalStateException("Không thể tải mức lương và phụ cấp.", e);
        }
        return list;
    }
}
