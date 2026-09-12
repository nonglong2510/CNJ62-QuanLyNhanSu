-- Script tạo cơ sở dữ liệu Quản lý nhân sự
CREATE DATABASE IF NOT EXISTS QuanLyNhanSu CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci;
USE QuanLyNhanSu;

-- 1. Bảng Phòng Ban
CREATE TABLE IF NOT EXISTS PhongBan (
    MaPB VARCHAR(10) PRIMARY KEY,
    TenPB VARCHAR(100) NOT NULL,
    SoDienThoai VARCHAR(15)
);

-- 2. Bảng Chức Vụ
CREATE TABLE IF NOT EXISTS ChucVu (
    MaCV VARCHAR(10) PRIMARY KEY,
    TenCV VARCHAR(100) NOT NULL,
    PhuCapChucVu DECIMAL(15, 2) DEFAULT 0
);

-- 3. Bảng Nhân Viên
CREATE TABLE IF NOT EXISTS NhanVien (
    MaNV VARCHAR(10) PRIMARY KEY,
    HoTen VARCHAR(100) NOT NULL,
    GioiTinh ENUM('Nam', 'Nữ', 'Khác') DEFAULT 'Nam',
    NgaySinh DATE,
    SoDienThoai VARCHAR(15),
    Email VARCHAR(100),
    DiaChi VARCHAR(255),
    MaPB VARCHAR(10),
    MaCV VARCHAR(10),
    HeSoLuong DECIMAL(5, 2) DEFAULT 1.0,
    NgayVaoLam DATE,
    TrangThai ENUM('Đang làm việc', 'Nghỉ việc') DEFAULT 'Đang làm việc',
    FOREIGN KEY (MaPB) REFERENCES PhongBan(MaPB) ON DELETE SET NULL,
    FOREIGN KEY (MaCV) REFERENCES ChucVu(MaCV) ON DELETE SET NULL
);

-- 4. Bảng Tài Khoản (Đăng nhập hệ thống)
CREATE TABLE IF NOT EXISTS TaiKhoan (
    TenDangNhap VARCHAR(50) PRIMARY KEY,
    MatKhau VARCHAR(255) NOT NULL,
    MaNV VARCHAR(10),
    Quyen ENUM('Admin', 'User') DEFAULT 'User',
    FOREIGN KEY (MaNV) REFERENCES NhanVien(MaNV) ON DELETE CASCADE
);

-- 5. Bảng Chấm Công
CREATE TABLE IF NOT EXISTS ChamCong (
    MaCC INT AUTO_INCREMENT PRIMARY KEY,
    MaNV VARCHAR(10),
    NgayChamCong DATE,
    TrangThai ENUM('Đi làm', 'Nghỉ phép', 'Không phép', 'Đi trễ') DEFAULT 'Đi làm',
    SoGioLamThem DECIMAL(5, 2) DEFAULT 0,
    GhiChu VARCHAR(255),
    UNIQUE KEY(MaNV, NgayChamCong),
    FOREIGN KEY (MaNV) REFERENCES NhanVien(MaNV) ON DELETE CASCADE
);

-- 6. Bảng Bảng Lương (Tính lương hàng tháng)
CREATE TABLE IF NOT EXISTS BangLuong (
    MaLuong INT AUTO_INCREMENT PRIMARY KEY,
    MaNV VARCHAR(10),
    Thang INT,
    Nam INT,
    LuongCoBan DECIMAL(15, 2),
    SoNgayCong INT DEFAULT 0,
    TongPhuCap DECIMAL(15, 2) DEFAULT 0,
    TienThuong DECIMAL(15, 2) DEFAULT 0,
    TienPhat DECIMAL(15, 2) DEFAULT 0,
    ThucLanh DECIMAL(15, 2),
    NgayTinhLuong DATE,
    UNIQUE KEY(MaNV, Thang, Nam),
    FOREIGN KEY (MaNV) REFERENCES NhanVien(MaNV) ON DELETE CASCADE
);

-- Thêm dữ liệu mẫu (Dummy Data)
INSERT INTO PhongBan (MaPB, TenPB, SoDienThoai) VALUES 
('PB01', 'Phòng Giám Đốc', '0281111111'),
('PB02', 'Phòng Hành Chính Nhân Sự', '0282222222'),
('PB03', 'Phòng Kế Toán', '0283333333'),
('PB04', 'Phòng Kinh Doanh', '0284444444');

INSERT INTO ChucVu (MaCV, TenCV, PhuCapChucVu) VALUES 
('CV01', 'Giám Đốc', 5000000),
('CV02', 'Trưởng Phòng', 3000000),
('CV03', 'Phó Phòng', 1500000),
('CV04', 'Nhân Viên', 0);

INSERT INTO NhanVien (MaNV, HoTen, GioiTinh, NgaySinh, SoDienThoai, Email, DiaChi, MaPB, MaCV, HeSoLuong, NgayVaoLam, TrangThai) VALUES
('NV001', 'Nguyễn Văn A', 'Nam', '1985-05-15', '0901123456', 'nguyenvana@gmail.com', 'Quận 1, TP HCM', 'PB01', 'CV01', 3.0, '2020-01-01', 'Đang làm việc'),
('NV002', 'Trần Thị B', 'Nữ', '1990-10-20', '0902234567', 'tranthib@gmail.com', 'Quận 3, TP HCM', 'PB02', 'CV02', 2.0, '2021-03-15', 'Đang làm việc'),
('NV003', 'Lê Văn C', 'Nam', '1995-12-05', '0903345678', 'levanc@gmail.com', 'Quận 5, TP HCM', 'PB03', 'CV04', 1.0, '2022-06-10', 'Đang làm việc');

INSERT INTO TaiKhoan (TenDangNhap, MatKhau, MaNV, Quyen) VALUES
('admin', 'admin123', 'NV001', 'Admin'),
('user1', 'user123', 'NV002', 'User'),
('user2', 'user123', 'NV003', 'User');
