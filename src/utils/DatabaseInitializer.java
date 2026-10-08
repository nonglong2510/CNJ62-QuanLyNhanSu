package utils;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

public final class DatabaseInitializer {
    private static final String[][] DEPARTMENTS = {
            {"PB01", "Phòng Giám Đốc", "0281111111"},
            {"PB02", "Phòng Hành Chính Nhân Sự", "0282222222"},
            {"PB03", "Phòng Kế Toán", "0283333333"},
            {"PB04", "Phòng Kinh Doanh", "0284444444"}
    };

    private static final Object[][] POSITIONS = {
            {"CV01", "Giám Đốc", 5000000.0},
            {"CV02", "Trưởng Phòng", 3000000.0},
            {"CV03", "Phó Phòng", 1500000.0},
            {"CV04", "Nhân Viên", 0.0}
    };

    private static final Object[][] EMPLOYEES = {
            {"NV001", "Nguyễn Văn A", "Nam", "1985-05-15", "0901123456",
                    "nguyenvana@example.com", "TP HCM", "PB01", "CV01", 3.0,
                    "2020-01-01", "Đang làm việc"},
            {"NV002", "Trần Thị B", "Nữ", "1990-10-20", "0902234567",
                    "tranthib@example.com", "TP HCM", "PB02", "CV02", 2.0,
                    "2021-03-15", "Đang làm việc"},
            {"NV003", "Lê Văn C", "Nam", "1995-12-05", "0903345678",
                    "levanc@example.com", "TP HCM", "PB03", "CV04", 1.0,
                    "2022-06-10", "Đang làm việc"}
    };

    private DatabaseInitializer() {
    }

    public static void initialize() {
        String databaseName = DBHelper.getDatabaseName();
        if (!databaseName.matches("[A-Za-z0-9_]+")) {
            throw new IllegalStateException("Tên database chỉ được chứa chữ, số và dấu gạch dưới.");
        }

        try (Connection server = DBHelper.getServerConnection();
             Statement statement = server.createStatement()) {
            statement.execute("CREATE DATABASE IF NOT EXISTS `" + databaseName
                    + "` CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci");
        } catch (SQLException e) {
            throw new IllegalStateException(
                    "Không thể tạo database '" + databaseName
                            + "'. Tài khoản MySQL cần quyền CREATE DATABASE.",
                    e);
        }

        try (Connection connection = DBHelper.getConnection()) {
            createTables(connection);
            seedDemoData(connection);
        } catch (SQLException e) {
            throw new IllegalStateException(
                    "Không thể khởi tạo schema hoặc dữ liệu demo trong database '"
                            + databaseName + "'.",
                    e);
        }
    }

    private static void createTables(Connection connection) throws SQLException {
        String[] ddl = {
                "CREATE TABLE IF NOT EXISTS PhongBan ("
                        + "MaPB VARCHAR(10) PRIMARY KEY, TenPB VARCHAR(100) NOT NULL, "
                        + "SoDienThoai VARCHAR(15)) ENGINE=InnoDB",
                "CREATE TABLE IF NOT EXISTS ChucVu ("
                        + "MaCV VARCHAR(10) PRIMARY KEY, TenCV VARCHAR(100) NOT NULL, "
                        + "PhuCapChucVu DECIMAL(15,2) DEFAULT 0) ENGINE=InnoDB",
                "CREATE TABLE IF NOT EXISTS NhanVien ("
                        + "MaNV VARCHAR(10) PRIMARY KEY, HoTen VARCHAR(100) NOT NULL, "
                        + "GioiTinh ENUM('Nam','Nữ','Khác') DEFAULT 'Nam', NgaySinh DATE, "
                        + "SoDienThoai VARCHAR(15), Email VARCHAR(100), DiaChi VARCHAR(255), "
                        + "MaPB VARCHAR(10), MaCV VARCHAR(10), HeSoLuong DECIMAL(5,2) DEFAULT 1.0, "
                        + "NgayVaoLam DATE, TrangThai ENUM('Đang làm việc','Nghỉ việc') "
                        + "DEFAULT 'Đang làm việc', "
                        + "FOREIGN KEY (MaPB) REFERENCES PhongBan(MaPB) ON DELETE SET NULL, "
                        + "FOREIGN KEY (MaCV) REFERENCES ChucVu(MaCV) ON DELETE SET NULL"
                        + ") ENGINE=InnoDB",
                "CREATE TABLE IF NOT EXISTS TaiKhoan ("
                        + "TenDangNhap VARCHAR(50) PRIMARY KEY, MatKhau VARCHAR(255) NOT NULL, "
                        + "MaNV VARCHAR(10), Quyen ENUM('Admin','User') DEFAULT 'User', "
                        + "FOREIGN KEY (MaNV) REFERENCES NhanVien(MaNV) ON DELETE CASCADE"
                        + ") ENGINE=InnoDB",
                "CREATE TABLE IF NOT EXISTS ChamCong ("
                        + "MaCC INT AUTO_INCREMENT PRIMARY KEY, MaNV VARCHAR(10) NOT NULL, "
                        + "NgayChamCong DATE NOT NULL, "
                        + "TrangThai ENUM('Đi làm','Nghỉ phép','Không phép','Đi trễ') DEFAULT 'Đi làm', "
                        + "SoGioLamThem DECIMAL(5,2) DEFAULT 0, GhiChu VARCHAR(255), "
                        + "UNIQUE KEY uq_ChamCong_MaNV_Ngay (MaNV, NgayChamCong), "
                        + "FOREIGN KEY (MaNV) REFERENCES NhanVien(MaNV) ON DELETE CASCADE"
                        + ") ENGINE=InnoDB",
                "CREATE TABLE IF NOT EXISTS BangLuong ("
                        + "MaLuong INT AUTO_INCREMENT PRIMARY KEY, MaNV VARCHAR(10) NOT NULL, "
                        + "Thang INT NOT NULL, Nam INT NOT NULL, LuongCoBan DECIMAL(15,2), "
                        + "SoNgayCong INT DEFAULT 0, TongPhuCap DECIMAL(15,2) DEFAULT 0, "
                        + "TienThuong DECIMAL(15,2) DEFAULT 0, TienPhat DECIMAL(15,2) DEFAULT 0, "
                        + "ThucLanh DECIMAL(15,2), NgayTinhLuong DATE, "
                        + "UNIQUE KEY uq_BangLuong_MaNV_Thang_Nam (MaNV, Thang, Nam), "
                        + "FOREIGN KEY (MaNV) REFERENCES NhanVien(MaNV) ON DELETE CASCADE"
                        + ") ENGINE=InnoDB",
                "CREATE TABLE IF NOT EXISTS donxinphep ("
                        + "MaDon INT AUTO_INCREMENT PRIMARY KEY, MaNV VARCHAR(10) NOT NULL, "
                        + "NgayBatDau DATE NOT NULL, NgayKetThuc DATE NOT NULL, "
                        + "LyDo VARCHAR(255) DEFAULT NULL, "
                        + "TrangThai VARCHAR(50) DEFAULT 'Chờ phê duyệt', "
                        + "KEY ix_donxinphep_MaNV (MaNV), "
                        + "FOREIGN KEY (MaNV) REFERENCES NhanVien(MaNV) ON DELETE CASCADE"
                        + ") ENGINE=InnoDB"
        };
        try (Statement statement = connection.createStatement()) {
            for (String sql : ddl) {
                statement.executeUpdate(sql);
            }
        }
    }

    private static void seedDemoData(Connection connection) throws SQLException {
        connection.setAutoCommit(false);
        try {
            insertDepartments(connection);
            insertPositions(connection);
            insertEmployees(connection);
            insertAccountIfMissing(connection, "admin", "admin123", "NV001", "Admin");
            insertAccountIfMissing(connection, "user1", "user123", "NV002", "User");
            insertAccountIfMissing(connection, "user2", "user123", "NV003", "User");
            connection.commit();
        } catch (SQLException | RuntimeException e) {
            connection.rollback();
            throw e;
        } finally {
            connection.setAutoCommit(true);
        }
    }

    private static void insertDepartments(Connection connection) throws SQLException {
        String sql = "INSERT IGNORE INTO PhongBan (MaPB, TenPB, SoDienThoai) VALUES (?, ?, ?)";
        try (PreparedStatement statement = connection.prepareStatement(sql)) {
            for (String[] row : DEPARTMENTS) {
                statement.setString(1, row[0]);
                statement.setString(2, row[1]);
                statement.setString(3, row[2]);
                statement.addBatch();
            }
            statement.executeBatch();
        }
    }

    private static void insertPositions(Connection connection) throws SQLException {
        String sql = "INSERT IGNORE INTO ChucVu (MaCV, TenCV, PhuCapChucVu) VALUES (?, ?, ?)";
        try (PreparedStatement statement = connection.prepareStatement(sql)) {
            for (Object[] row : POSITIONS) {
                statement.setString(1, (String) row[0]);
                statement.setString(2, (String) row[1]);
                statement.setDouble(3, (Double) row[2]);
                statement.addBatch();
            }
            statement.executeBatch();
        }
    }

    private static void insertEmployees(Connection connection) throws SQLException {
        String sql = "INSERT IGNORE INTO NhanVien "
                + "(MaNV, HoTen, GioiTinh, NgaySinh, SoDienThoai, Email, DiaChi, MaPB, MaCV, "
                + "HeSoLuong, NgayVaoLam, TrangThai) VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";
        try (PreparedStatement statement = connection.prepareStatement(sql)) {
            for (Object[] row : EMPLOYEES) {
                for (int i = 0; i < row.length; i++) {
                    if (i == 3 || i == 10) {
                        statement.setDate(i + 1, java.sql.Date.valueOf((String) row[i]));
                    } else if (i == 9) {
                        statement.setDouble(i + 1, (Double) row[i]);
                    } else {
                        statement.setString(i + 1, (String) row[i]);
                    }
                }
                statement.addBatch();
            }
            statement.executeBatch();
        }
    }

    private static void insertAccountIfMissing(Connection connection, String username,
            String password, String employeeId, String role) throws SQLException {
        String existsSql = "SELECT 1 FROM TaiKhoan WHERE TenDangNhap = ?";
        try (PreparedStatement exists = connection.prepareStatement(existsSql)) {
            exists.setString(1, username);
            try (ResultSet result = exists.executeQuery()) {
                if (result.next()) {
                    return;
                }
            }
        }

        String sql = "INSERT IGNORE INTO TaiKhoan (TenDangNhap, MatKhau, MaNV, Quyen) VALUES (?, ?, ?, ?)";
        try (PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setString(1, username);
            statement.setString(2, PasswordUtils.hashPassword(password));
            statement.setString(3, employeeId);
            statement.setString(4, role);
            statement.executeUpdate();
        }
    }
}
