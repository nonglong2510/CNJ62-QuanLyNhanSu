package bus;

import dal.ChamCongDAL;
import model.ChamCong;

import java.util.List;

public class ChamCongBUS {
    private ChamCongDAL chamCongDAL;

    public ChamCongBUS() {
        chamCongDAL = new ChamCongDAL();
    }

    public List<ChamCong> getAll() {
        return chamCongDAL.getAll();
    }

    public List<ChamCong> getByEmployee(String maNV, int month, int year) {
        if (maNV == null || maNV.trim().isEmpty()) {
            throw new IllegalArgumentException("Mã nhân viên là bắt buộc.");
        }
        if (month < 1 || month > 12 || year < 2000 || year > 2100) {
            throw new IllegalArgumentException("Tháng hoặc năm xem chấm công không hợp lệ.");
        }
        return chamCongDAL.getByEmployee(maNV.trim(), month, year);
    }

    public int getTotalWorkDays(String maNV, int month, int year) {
        return chamCongDAL.getTotalWorkDays(maNV, month, year);
    }

    public List<Object[]> getTongHopCongThang(int month, int year) {
        return chamCongDAL.getTongHopCongThang(month, year);
    }

    public List<Object[]> getDiemDanhNgay(java.sql.Date date) {
        return chamCongDAL.getDiemDanhNgay(date);
    }

    public boolean upsertDiemDanh(String maNV, java.sql.Date date, String trangThai, double ot, String ghiChu) {
        if (maNV == null || maNV.trim().isEmpty() || date == null) {
            throw new IllegalArgumentException("Nhân viên và ngày chấm công là bắt buộc.");
        }
        if (!java.util.Arrays.asList("Đi làm", "Đi trễ", "Nghỉ phép", "Không phép").contains(trangThai)) {
            throw new IllegalArgumentException("Trạng thái chấm công không hợp lệ.");
        }
        if (!Double.isFinite(ot) || ot < 0 || ot > 999.99) {
            throw new IllegalArgumentException("Giờ làm thêm phải nằm trong khoảng 0 đến 999.99.");
        }
        return chamCongDAL.upsertDiemDanh(maNV, date, trangThai, ot, ghiChu);
    }
}
