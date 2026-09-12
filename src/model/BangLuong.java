package model;

import java.sql.Date;

public class BangLuong {
    private int maLuong;
    private String maNV;
    private int thang;
    private int nam;
    private double luongCoBan;
    private int soNgayCong;
    private double tongPhuCap;
    private double tienThuong;
    private double tienPhat;
    private double thucLanh;
    private Date ngayTinhLuong;

    public BangLuong() {}

    public BangLuong(int maLuong, String maNV, int thang, int nam, double luongCoBan, int soNgayCong, 
                     double tongPhuCap, double tienThuong, double tienPhat, double thucLanh, Date ngayTinhLuong) {
        this.maLuong = maLuong;
        this.maNV = maNV;
        this.thang = thang;
        this.nam = nam;
        this.luongCoBan = luongCoBan;
        this.soNgayCong = soNgayCong;
        this.tongPhuCap = tongPhuCap;
        this.tienThuong = tienThuong;
        this.tienPhat = tienPhat;
        this.thucLanh = thucLanh;
        this.ngayTinhLuong = ngayTinhLuong;
    }

    public int getMaLuong() { return maLuong; }
    public void setMaLuong(int maLuong) { this.maLuong = maLuong; }

    public String getMaNV() { return maNV; }
    public void setMaNV(String maNV) { this.maNV = maNV; }

    public int getThang() { return thang; }
    public void setThang(int thang) { this.thang = thang; }

    public int getNam() { return nam; }
    public void setNam(int nam) { this.nam = nam; }

    public double getLuongCoBan() { return luongCoBan; }
    public void setLuongCoBan(double luongCoBan) { this.luongCoBan = luongCoBan; }

    public int getSoNgayCong() { return soNgayCong; }
    public void setSoNgayCong(int soNgayCong) { this.soNgayCong = soNgayCong; }

    public double getTongPhuCap() { return tongPhuCap; }
    public void setTongPhuCap(double tongPhuCap) { this.tongPhuCap = tongPhuCap; }

    public double getTienThuong() { return tienThuong; }
    public void setTienThuong(double tienThuong) { this.tienThuong = tienThuong; }

    public double getTienPhat() { return tienPhat; }
    public void setTienPhat(double tienPhat) { this.tienPhat = tienPhat; }

    public double getThucLanh() { return thucLanh; }
    public void setThucLanh(double thucLanh) { this.thucLanh = thucLanh; }

    public Date getNgayTinhLuong() { return ngayTinhLuong; }
    public void setNgayTinhLuong(Date ngayTinhLuong) { this.ngayTinhLuong = ngayTinhLuong; }
}
