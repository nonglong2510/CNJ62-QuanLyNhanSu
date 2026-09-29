package bus;

import dal.BangLuongDAL;
import dal.ChamCongDAL;
import dal.ChucVuDAL;
import dal.NhanVienDAL;
import model.BangLuong;
import model.ChucVu;
import model.NhanVien;

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
        return bangLuongDAL.getByMonth(month, year);
    }

    public void calculateSalaryForMonth(int month, int year) {
        List<NhanVien> dsNhanVien = nhanVienDAL.getAll();
        List<ChucVu> dsChucVu = chucVuDAL.getAll();
        
        final double MUC_LUONG_CO_SO = 5000000; // 5 triệu VND
        final double LUONG_1_NGAY_CO_SO = MUC_LUONG_CO_SO / 22.0;
        
        for (NhanVien nv : dsNhanVien) {
            // Lấy số ngày làm việc thực tế
            int ngayCong = chamCongDAL.getTotalWorkDays(nv.getMaNV(), month, year);
            if (ngayCong == 0) continue; // Không có ngày công thì không tính lương
            
            // Tìm phụ cấp chức vụ
            double phuCap = 0;
            if (nv.getMaCV() != null) {
                for (ChucVu cv : dsChucVu) {
                    if (cv.getMaCV().equals(nv.getMaCV())) {
                        phuCap = cv.getPhuCapChucVu();
                        break;
                    }
                }
            }
            
            double luongCoBan = LUONG_1_NGAY_CO_SO * nv.getHeSoLuong() * ngayCong;
            double tienThuong = 0; // Giả sử thưởng
            double tienPhat = 0;   // Giả sử phạt
            double thucLanh = luongCoBan + phuCap + tienThuong - tienPhat;
            
            BangLuong bl = new BangLuong();
            bl.setMaNV(nv.getMaNV());
            bl.setThang(month);
            bl.setNam(year);
            bl.setLuongCoBan(luongCoBan);
            bl.setSoNgayCong(ngayCong);
            bl.setTongPhuCap(phuCap);
            bl.setTienThuong(tienThuong);
            bl.setTienPhat(tienPhat);
            bl.setThucLanh(thucLanh);
            bl.setNgayTinhLuong(new java.sql.Date(System.currentTimeMillis()));
            
            bangLuongDAL.insertOrUpdate(bl);
        }
    }
}
