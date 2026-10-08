package dal;

import model.DonXinPhep;
import utils.DBHelper;

import java.sql.*;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

public class DonXinPhepDAL {

    public List<DonXinPhep> getAll() {
        List<DonXinPhep> list = new ArrayList<>();
        String sql = "SELECT d.*, n.HoTen FROM donxinphep d JOIN NhanVien n ON d.MaNV = n.MaNV ORDER BY d.MaDon DESC";
        try (Connection conn = DBHelper.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql);
             ResultSet rs = pstmt.executeQuery()) {

            while (rs.next()) {
                DonXinPhep d = new DonXinPhep();
                d.setMaDon(rs.getInt("MaDon"));
                d.setMaNV(rs.getString("MaNV"));
                d.setNgayBatDau(rs.getDate("NgayBatDau"));
                d.setNgayKetThuc(rs.getDate("NgayKetThuc"));
                d.setLyDo(rs.getString("LyDo"));
                d.setTrangThai(rs.getString("TrangThai"));
                d.setHoTen(rs.getString("HoTen"));
                list.add(d);
            }
        } catch (SQLException e) {
            throw new IllegalStateException("Không thể tải danh sách đơn xin phép.", e);
        }
        return list;
    }

    public List<DonXinPhep> getByEmployee(String maNV) {
        List<DonXinPhep> list = new ArrayList<>();
        String sql = "SELECT d.*, n.HoTen FROM donxinphep d "
                + "JOIN NhanVien n ON d.MaNV = n.MaNV "
                + "WHERE d.MaNV = ? ORDER BY d.MaDon DESC";
        try (Connection conn = DBHelper.getConnection();
             PreparedStatement statement = conn.prepareStatement(sql)) {
            statement.setString(1, maNV);
            try (ResultSet rs = statement.executeQuery()) {
                while (rs.next()) {
                    DonXinPhep request = new DonXinPhep();
                    request.setMaDon(rs.getInt("MaDon"));
                    request.setMaNV(rs.getString("MaNV"));
                    request.setNgayBatDau(rs.getDate("NgayBatDau"));
                    request.setNgayKetThuc(rs.getDate("NgayKetThuc"));
                    request.setLyDo(rs.getString("LyDo"));
                    request.setTrangThai(rs.getString("TrangThai"));
                    request.setHoTen(rs.getString("HoTen"));
                    list.add(request);
                }
            }
        } catch (SQLException e) {
            throw new IllegalStateException("Không thể tải đơn xin phép cá nhân.", e);
        }
        return list;
    }

    public boolean insert(DonXinPhep d) {
        String sql = "INSERT INTO donxinphep(MaNV, NgayBatDau, NgayKetThuc, LyDo, TrangThai) VALUES(?, ?, ?, ?, ?)";
        try (Connection conn = DBHelper.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setString(1, d.getMaNV());
            pstmt.setDate(2, d.getNgayBatDau());
            pstmt.setDate(3, d.getNgayKetThuc());
            pstmt.setString(4, d.getLyDo());
            pstmt.setString(5, d.getTrangThai());

            return pstmt.executeUpdate() > 0;
        } catch (SQLException e) {
            throw new IllegalStateException("Không thể tạo đơn xin phép.", e);
        }
    }

    public boolean updateStatus(int maDon, String trangThai) {
        try (Connection conn = DBHelper.getConnection()) {
            conn.setAutoCommit(false);
            try {
                DonXinPhep request = lockRequest(conn, maDon);
                if (!"Chờ phê duyệt".equals(request.getTrangThai())) {
                    throw new IllegalStateException("Chỉ có thể xử lý đơn đang chờ phê duyệt.");
                }

                if ("Đã duyệt".equals(trangThai)) {
                    lockEmployee(conn, request.getMaNV());
                    List<Date> conflicts = findExistingAttendance(
                            conn, request.getMaNV(), request.getNgayBatDau(), request.getNgayKetThuc());
                    if (!conflicts.isEmpty()) {
                        throw new IllegalStateException("Không thể duyệt vì đã có chấm công vào ngày: "
                                + formatDates(conflicts) + ". Hãy kiểm tra hoặc xử lý các bản ghi trước.");
                    }
                    insertLeaveAttendance(conn, request);
                }

                String sql = "UPDATE donxinphep SET TrangThai = ? "
                        + "WHERE MaDon = ? AND TrangThai = 'Chờ phê duyệt'";
                try (PreparedStatement statement = conn.prepareStatement(sql)) {
                    statement.setString(1, trangThai);
                    statement.setInt(2, maDon);
                    if (statement.executeUpdate() != 1) {
                        throw new IllegalStateException("Đơn đã được xử lý bởi thao tác khác. Hãy tải lại danh sách.");
                    }
                }
                conn.commit();
                return true;
            } catch (SQLException | RuntimeException ex) {
                try {
                    conn.rollback();
                } catch (SQLException rollbackException) {
                    ex.addSuppressed(rollbackException);
                }
                throw ex;
            }
        } catch (SQLException e) {
            throw new IllegalStateException("Không thể xử lý đơn xin phép và đồng bộ chấm công.", e);
        }
    }

    private DonXinPhep lockRequest(Connection conn, int maDon) throws SQLException {
        String sql = "SELECT MaDon, MaNV, NgayBatDau, NgayKetThuc, TrangThai "
                + "FROM donxinphep WHERE MaDon = ? FOR UPDATE";
        try (PreparedStatement statement = conn.prepareStatement(sql)) {
            statement.setInt(1, maDon);
            try (ResultSet rs = statement.executeQuery()) {
                if (!rs.next()) {
                    throw new IllegalStateException("Không tìm thấy đơn xin phép.");
                }
                DonXinPhep request = new DonXinPhep();
                request.setMaDon(rs.getInt("MaDon"));
                request.setMaNV(rs.getString("MaNV"));
                request.setNgayBatDau(rs.getDate("NgayBatDau"));
                request.setNgayKetThuc(rs.getDate("NgayKetThuc"));
                request.setTrangThai(rs.getString("TrangThai"));
                return request;
            }
        }
    }

    private void lockEmployee(Connection conn, String maNV) throws SQLException {
        String sql = "SELECT MaNV FROM NhanVien WHERE MaNV = ? FOR UPDATE";
        try (PreparedStatement statement = conn.prepareStatement(sql)) {
            statement.setString(1, maNV);
            try (ResultSet rs = statement.executeQuery()) {
                if (!rs.next()) {
                    throw new IllegalStateException("Không tìm thấy nhân viên của đơn xin phép.");
                }
            }
        }
    }

    private List<Date> findExistingAttendance(Connection conn, String maNV, Date start, Date end)
            throws SQLException {
        List<Date> foundDates = new ArrayList<>();
        String sql = "SELECT NgayChamCong FROM ChamCong "
                + "WHERE MaNV = ? AND NgayChamCong BETWEEN ? AND ?";
        try (PreparedStatement statement = conn.prepareStatement(sql)) {
            statement.setString(1, maNV);
            statement.setDate(2, start);
            statement.setDate(3, end);
            try (ResultSet rs = statement.executeQuery()) {
                while (rs.next()) {
                    foundDates.add(rs.getDate("NgayChamCong"));
                }
            }
        }
        return LeaveAttendancePolicy.weekdayConflicts(foundDates);
    }

    private void insertLeaveAttendance(Connection conn, DonXinPhep request) throws SQLException {
        String sql = "INSERT INTO ChamCong (MaNV, NgayChamCong, TrangThai, SoGioLamThem, GhiChu) "
                + "VALUES (?, ?, 'Nghỉ phép', 0, ?)";
        try (PreparedStatement statement = conn.prepareStatement(sql)) {
            for (LocalDate day : LeaveAttendancePolicy.weekdays(
                    request.getNgayBatDau().toLocalDate(), request.getNgayKetThuc().toLocalDate())) {
                statement.setString(1, request.getMaNV());
                statement.setDate(2, Date.valueOf(day));
                statement.setString(3, "Đơn xin phép được duyệt #" + request.getMaDon());
                statement.addBatch();
            }
            statement.executeBatch();
        }
    }

    private String formatDates(List<Date> dates) {
        List<String> formatted = new ArrayList<>();
        for (Date date : dates) {
            formatted.add(date.toString());
        }
        return String.join(", ", formatted);
    }

    public boolean delete(int maDon) {
        String sql = "DELETE FROM donxinphep WHERE MaDon = ?";
        try (Connection conn = DBHelper.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setInt(1, maDon);
            return pstmt.executeUpdate() > 0;
        } catch (SQLException e) {
            throw new IllegalStateException("Không thể xóa đơn xin phép.", e);
        }
    }
}
