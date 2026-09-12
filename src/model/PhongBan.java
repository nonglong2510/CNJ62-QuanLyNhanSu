package model;

public class PhongBan {
    private String maPB;
    private String tenPB;
    private String soDienThoai;

    public PhongBan() {}

    public PhongBan(String maPB, String tenPB, String soDienThoai) {
        this.maPB = maPB;
        this.tenPB = tenPB;
        this.soDienThoai = soDienThoai;
    }

    public String getMaPB() { return maPB; }
    public void setMaPB(String maPB) { this.maPB = maPB; }

    public String getTenPB() { return tenPB; }
    public void setTenPB(String tenPB) { this.tenPB = tenPB; }

    public String getSoDienThoai() { return soDienThoai; }
    public void setSoDienThoai(String soDienThoai) { this.soDienThoai = soDienThoai; }

    @Override
    public String toString() { return tenPB; }
}
