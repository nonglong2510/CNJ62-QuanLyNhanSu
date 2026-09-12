package dal;

import model.NhanVien;
import utils.DBHelper;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class NhanVienDAL {

    // Lấy toàn bộ danh sách nhân viên
    public List<NhanVien> getAll() {
        List<NhanVien> list = new ArrayList<>();
        String sql = "SELECT * FROM NhanVien";

        try (Connection conn = DBHelper.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql);
             ResultSet rs = pstmt.executeQuery()) {

            while (rs.next()) {
                NhanVien nv = new NhanVien();
                nv.setMaNV(rs.getString("MaNV"));
                nv.setHoTen(rs.getString("HoTen"));
                nv.setGioiTinh(rs.getString("GioiTinh"));
                nv.setNgaySinh(rs.getDate("NgaySinh"));
                nv.setSoDienThoai(rs.getString("SoDienThoai"));
                nv.setEmail(rs.getString("Email"));
                nv.setDiaChi(rs.getString("DiaChi"));
                nv.setMaPB(rs.getString("MaPB"));
                nv.setMaCV(rs.getString("MaCV"));
                nv.setHeSoLuong(rs.getDouble("HeSoLuong"));
                nv.setNgayVaoLam(rs.getDate("NgayVaoLam"));
                nv.setTrangThai(rs.getString("TrangThai"));
                list.add(nv);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return list;
    }

    // Thêm nhân viên mới
    public boolean add(NhanVien nv) {
        String sql = "INSERT INTO NhanVien (MaNV, HoTen, GioiTinh, NgaySinh, SoDienThoai, Email, DiaChi, MaPB, MaCV, HeSoLuong, NgayVaoLam, TrangThai) VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";
        try (Connection conn = DBHelper.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setString(1, nv.getMaNV());
            pstmt.setString(2, nv.getHoTen());
            pstmt.setString(3, nv.getGioiTinh());
            pstmt.setDate(4, nv.getNgaySinh());
            pstmt.setString(5, nv.getSoDienThoai());
            pstmt.setString(6, nv.getEmail());
            pstmt.setString(7, nv.getDiaChi());
            pstmt.setString(8, nv.getMaPB());
            pstmt.setString(9, nv.getMaCV());
            pstmt.setDouble(10, nv.getHeSoLuong());
            pstmt.setDate(11, nv.getNgayVaoLam());
            pstmt.setString(12, nv.getTrangThai());

            return pstmt.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }

    // Cập nhật thông tin nhân viên
    public boolean update(NhanVien nv) {
        String sql = "UPDATE NhanVien SET HoTen=?, GioiTinh=?, NgaySinh=?, SoDienThoai=?, Email=?, DiaChi=?, MaPB=?, MaCV=?, HeSoLuong=?, NgayVaoLam=?, TrangThai=? WHERE MaNV=?";
        try (Connection conn = DBHelper.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setString(1, nv.getHoTen());
            pstmt.setString(2, nv.getGioiTinh());
            pstmt.setDate(3, nv.getNgaySinh());
            pstmt.setString(4, nv.getSoDienThoai());
            pstmt.setString(5, nv.getEmail());
            pstmt.setString(6, nv.getDiaChi());
            pstmt.setString(7, nv.getMaPB());
            pstmt.setString(8, nv.getMaCV());
            pstmt.setDouble(9, nv.getHeSoLuong());
            pstmt.setDate(10, nv.getNgayVaoLam());
            pstmt.setString(11, nv.getTrangThai());
            pstmt.setString(12, nv.getMaNV());

            return pstmt.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }

    // Xóa nhân viên (thực tế nên dùng xóa mềm bằng cách đổi TrangThai='Nghỉ việc')
    public boolean delete(String maNV) {
        String sql = "DELETE FROM NhanVien WHERE MaNV=?";
        try (Connection conn = DBHelper.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setString(1, maNV);
            return pstmt.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }
}
