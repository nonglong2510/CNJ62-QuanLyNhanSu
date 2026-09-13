package model;

import java.sql.Date;

public class ChamCong {
    private int maCC;
    private String maNV;
    private Date ngayChamCong;
    private String trangThai;
    private double soGioLamThem;
    private String ghiChu;

    public ChamCong() {}

    public ChamCong(int maCC, String maNV, Date ngayChamCong, String trangThai, double soGioLamThem, String ghiChu) {
        this.maCC = maCC;
        this.maNV = maNV;
        this.ngayChamCong = ngayChamCong;
        this.trangThai = trangThai;
        this.soGioLamThem = soGioLamThem;
        this.ghiChu = ghiChu;
    }

    public int getMaCC() { return maCC; }
    public void setMaCC(int maCC) { this.maCC = maCC; }

    public String getMaNV() { return maNV; }
    public void setMaNV(String maNV) { this.maNV = maNV; }

    public Date getNgayChamCong() { return ngayChamCong; }
    public void setNgayChamCong(Date ngayChamCong) { this.ngayChamCong = ngayChamCong; }

    public String getTrangThai() { return trangThai; }
    public void setTrangThai(String trangThai) { this.trangThai = trangThai; }

    public double getSoGioLamThem() { return soGioLamThem; }
    public void setSoGioLamThem(double soGioLamThem) { this.soGioLamThem = soGioLamThem; }

    public String getGhiChu() { return ghiChu; }
    public void setGhiChu(String ghiChu) { this.ghiChu = ghiChu; }
}
