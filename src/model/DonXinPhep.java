package model;

import java.sql.Date;

public class DonXinPhep {
    private int maDon;
    private String maNV;
    private Date ngayBatDau;
    private Date ngayKetThuc;
    private String lyDo;
    private String trangThai;

    // Thuộc tính mở rộng để JOIN
    private String hoTen;

    public DonXinPhep() {
    }

    public DonXinPhep(int maDon, String maNV, Date ngayBatDau, Date ngayKetThuc, String lyDo, String trangThai) {
        this.maDon = maDon;
        this.maNV = maNV;
        this.ngayBatDau = ngayBatDau;
        this.ngayKetThuc = ngayKetThuc;
        this.lyDo = lyDo;
        this.trangThai = trangThai;
    }

    public int getMaDon() { return maDon; }
    public void setMaDon(int maDon) { this.maDon = maDon; }

    public String getMaNV() { return maNV; }
    public void setMaNV(String maNV) { this.maNV = maNV; }

    public Date getNgayBatDau() { return ngayBatDau; }
    public void setNgayBatDau(Date ngayBatDau) { this.ngayBatDau = ngayBatDau; }

    public Date getNgayKetThuc() { return ngayKetThuc; }
    public void setNgayKetThuc(Date ngayKetThuc) { this.ngayKetThuc = ngayKetThuc; }

    public String getLyDo() { return lyDo; }
    public void setLyDo(String lyDo) { this.lyDo = lyDo; }

    public String getTrangThai() { return trangThai; }
    public void setTrangThai(String trangThai) { this.trangThai = trangThai; }

    public String getHoTen() { return hoTen; }
    public void setHoTen(String hoTen) { this.hoTen = hoTen; }
}
