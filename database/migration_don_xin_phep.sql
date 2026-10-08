USE quanlynhansu;

CREATE TABLE IF NOT EXISTS donxinphep (
    MaDon INT AUTO_INCREMENT PRIMARY KEY,
    MaNV VARCHAR(10) NOT NULL,
    NgayBatDau DATE NOT NULL,
    NgayKetThuc DATE NOT NULL,
    LyDo VARCHAR(255) DEFAULT NULL,
    TrangThai VARCHAR(50) DEFAULT 'Chờ phê duyệt',
    KEY ix_donxinphep_MaNV (MaNV),
    CONSTRAINT fk_donxinphep_NhanVien FOREIGN KEY (MaNV)
        REFERENCES NhanVien(MaNV) ON DELETE CASCADE
) ENGINE=InnoDB;
