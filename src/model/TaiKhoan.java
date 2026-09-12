package model;

public class TaiKhoan {
    private String tenDangNhap;
    private String matKhau;
    private String maNV;
    private String quyen;

    public TaiKhoan() {}

    public TaiKhoan(String tenDangNhap, String matKhau, String maNV, String quyen) {
        this.tenDangNhap = tenDangNhap;
        this.matKhau = matKhau;
        this.maNV = maNV;
        this.quyen = quyen;
    }

    public String getTenDangNhap() { return tenDangNhap; }
    public void setTenDangNhap(String tenDangNhap) { this.tenDangNhap = tenDangNhap; }

    public String getMatKhau() { return matKhau; }
    public void setMatKhau(String matKhau) { this.matKhau = matKhau; }

    public String getMaNV() { return maNV; }
    public void setMaNV(String maNV) { this.maNV = maNV; }

    public String getQuyen() { return quyen; }
    public void setQuyen(String quyen) { this.quyen = quyen; }
}
