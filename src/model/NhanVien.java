package model;

import java.sql.Date;

public class NhanVien {
    private String maNV;
    private String hoTen;
    private String gioiTinh;
    private Date ngaySinh;
    private String soDienThoai;
    private String email;
    private String diaChi;
    private String maPB;
    private String maCV;
    private double heSoLuong;
    private Date ngayVaoLam;
    private String trangThai;

    public NhanVien() {}

    public NhanVien(String maNV, String hoTen, String gioiTinh, Date ngaySinh, String soDienThoai, 
                    String email, String diaChi, String maPB, String maCV, double heSoLuong, 
                    Date ngayVaoLam, String trangThai) {
        this.maNV = maNV;
        this.hoTen = hoTen;
        this.gioiTinh = gioiTinh;
        this.ngaySinh = ngaySinh;
        this.soDienThoai = soDienThoai;
        this.email = email;
        this.diaChi = diaChi;
        this.maPB = maPB;
        this.maCV = maCV;
        this.heSoLuong = heSoLuong;
        this.ngayVaoLam = ngayVaoLam;
        this.trangThai = trangThai;
    }

    public String getMaNV() { return maNV; }
    public void setMaNV(String maNV) { this.maNV = maNV; }

    public String getHoTen() { return hoTen; }
    public void setHoTen(String hoTen) { this.hoTen = hoTen; }

    public String getGioiTinh() { return gioiTinh; }
    public void setGioiTinh(String gioiTinh) { this.gioiTinh = gioiTinh; }

    public Date getNgaySinh() { return ngaySinh; }
    public void setNgaySinh(Date ngaySinh) { this.ngaySinh = ngaySinh; }

    public String getSoDienThoai() { return soDienThoai; }
    public void setSoDienThoai(String soDienThoai) { this.soDienThoai = soDienThoai; }

    public String getEmail() { return email; }
    public void setEmail(String email) { this.email = email; }

    public String getDiaChi() { return diaChi; }
    public void setDiaChi(String diaChi) { this.diaChi = diaChi; }

    public String getMaPB() { return maPB; }
    public void setMaPB(String maPB) { this.maPB = maPB; }

    public String getMaCV() { return maCV; }
    public void setMaCV(String maCV) { this.maCV = maCV; }

    public double getHeSoLuong() { return heSoLuong; }
    public void setHeSoLuong(double heSoLuong) { this.heSoLuong = heSoLuong; }

    public Date getNgayVaoLam() { return ngayVaoLam; }
    public void setNgayVaoLam(Date ngayVaoLam) { this.ngayVaoLam = ngayVaoLam; }

    public String getTrangThai() { return trangThai; }
    public void setTrangThai(String trangThai) { this.trangThai = trangThai; }
    
    @Override
    public String toString() { return hoTen + " (" + maNV + ")"; }
}
