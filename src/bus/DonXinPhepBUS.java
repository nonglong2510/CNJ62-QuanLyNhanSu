package bus;

import dal.DonXinPhepDAL;
import model.DonXinPhep;
import java.util.List;

public class DonXinPhepBUS {
    private DonXinPhepDAL dal;

    public DonXinPhepBUS() {
        dal = new DonXinPhepDAL();
    }

    public List<DonXinPhep> getAll() {
        return dal.getAll();
    }

    public List<DonXinPhep> getByEmployee(String maNV) {
        if (maNV == null || maNV.trim().isEmpty()) {
            throw new IllegalArgumentException("Mã nhân viên là bắt buộc.");
        }
        return dal.getByEmployee(maNV.trim());
    }

    public boolean insert(DonXinPhep d) {
        if (d == null || d.getMaNV() == null || d.getMaNV().trim().isEmpty()) {
            throw new IllegalArgumentException("Cần chọn nhân viên.");
        }
        if (d.getNgayBatDau() == null || d.getNgayKetThuc() == null) {
            throw new IllegalArgumentException("Ngày bắt đầu và kết thúc là bắt buộc.");
        }
        if (d.getNgayBatDau().after(d.getNgayKetThuc())) {
            throw new IllegalArgumentException("Ngày bắt đầu không được sau ngày kết thúc.");
        }
        if (d.getLyDo() == null || d.getLyDo().trim().isEmpty()) {
            throw new IllegalArgumentException("Lý do xin phép là bắt buộc.");
        }
        return dal.insert(d);
    }

    public boolean updateStatus(int maDon, String trangThai) {
        if (maDon <= 0 || !java.util.Arrays.asList("Đã duyệt", "Từ chối").contains(trangThai)) {
            throw new IllegalArgumentException("Mã đơn hoặc trạng thái xử lý không hợp lệ.");
        }
        return dal.updateStatus(maDon, trangThai);
    }

    public boolean delete(int maDon) {
        return dal.delete(maDon);
    }
}
