package bus;

import dal.BangLuongDAL;
import dal.ChamCongDAL;
import dal.ChucVuDAL;
import dal.NhanVienDAL;
import model.BangLuong;
import model.ChucVu;
import model.NhanVien;

import java.util.ArrayList;
import java.util.List;

public class BangLuongBUS {
    private BangLuongDAL bangLuongDAL;
    private NhanVienDAL nhanVienDAL;
    private ChamCongDAL chamCongDAL;
    private ChucVuDAL chucVuDAL;

    public BangLuongBUS() {
        bangLuongDAL = new BangLuongDAL();
        nhanVienDAL = new NhanVienDAL();
        chamCongDAL = new ChamCongDAL();
        chucVuDAL = new ChucVuDAL();
    }

    public List<BangLuong> getByMonth(int month, int year) {
        if (month < 1 || month > 12 || year < 2000 || year > 2100) {
            throw new IllegalArgumentException("Tháng hoặc năm xem bảng lương không hợp lệ.");
        }
        return bangLuongDAL.getByMonth(month, year);
    }

    public List<BangLuong> getByMonthForEmployee(int month, int year, String maNV) {
        if (month < 1 || month > 12 || year < 2000 || year > 2100
                || maNV == null || maNV.trim().isEmpty()) {
            throw new IllegalArgumentException("Kỳ lương hoặc mã nhân viên không hợp lệ.");
        }
        return bangLuongDAL.getByMonthAndEmployee(month, year, maNV.trim());
    }

    public void calculateSalaryForMonth(int month, int year) {
        if (month < 1 || month > 12 || year < 2000 || year > 2100) {
            throw new IllegalArgumentException("Tháng hoặc năm tính lương không hợp lệ.");
        }
        List<NhanVien> dsNhanVien = nhanVienDAL.getAll();
        List<ChucVu> dsChucVu = chucVuDAL.getAll();
        List<BangLuong> salaries = new ArrayList<>();

        for (NhanVien nv : dsNhanVien) {
            if (!"Đang làm việc".equals(nv.getTrangThai())) {
                continue;
            }
            int ngayCong = chamCongDAL.getTotalWorkDays(nv.getMaNV(), month, year);

            double phuCap = 0;
            if (nv.getMaCV() != null) {
                for (ChucVu cv : dsChucVu) {
                    if (cv.getMaCV().equals(nv.getMaCV())) {
                        phuCap = cv.getPhuCapChucVu();
                        break;
                    }
                }
            }

            SalaryCalculator.Salary calculation =
                    SalaryCalculator.calculate(nv.getHeSoLuong(), phuCap, ngayCong);
            double luongCoBan = calculation.getBaseSalary();
            double phuCapTheoCong = calculation.getAllowance();
            double tienThuong = 0;
            double tienPhat = 0;
            double thucLanh = calculation.getNetPay() + tienThuong - tienPhat;

            BangLuong bl = new BangLuong();
            bl.setMaNV(nv.getMaNV());
            bl.setThang(month);
            bl.setNam(year);
            bl.setLuongCoBan(luongCoBan);
            bl.setSoNgayCong(ngayCong);
            bl.setTongPhuCap(phuCapTheoCong);
            bl.setTienThuong(tienThuong);
            bl.setTienPhat(tienPhat);
            bl.setThucLanh(thucLanh);
            bl.setNgayTinhLuong(new java.sql.Date(System.currentTimeMillis()));

            salaries.add(bl);
        }
        try {
            bangLuongDAL.insertOrUpdateAll(salaries);
        } catch (IllegalStateException ex) {
            throw new IllegalStateException("Lỗi khi lưu bảng lương kỳ " + month + "/" + year + ".", ex);
        }
    }

    public List<Object[]> getDanhSachLuongCoBan() {
        return bangLuongDAL.getDanhSachLuongCoBan();
    }

}
