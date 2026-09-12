package model;

public class ChucVu {
    private String maCV;
    private String tenCV;
    private double phuCapChucVu;

    public ChucVu() {}

    public ChucVu(String maCV, String tenCV, double phuCapChucVu) {
        this.maCV = maCV;
        this.tenCV = tenCV;
        this.phuCapChucVu = phuCapChucVu;
    }

    public String getMaCV() { return maCV; }
    public void setMaCV(String maCV) { this.maCV = maCV; }

    public String getTenCV() { return tenCV; }
    public void setTenCV(String tenCV) { this.tenCV = tenCV; }

    public double getPhuCapChucVu() { return phuCapChucVu; }
    public void setPhuCapChucVu(double phuCapChucVu) { this.phuCapChucVu = phuCapChucVu; }

    @Override
    public String toString() { return tenCV; }
}
