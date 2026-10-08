package bus;

import dal.NhanVienDAL;
import model.NhanVien;
import java.util.List;

public class NhanVienBUS {
    private NhanVienDAL nhanVienDAL;

    public NhanVienBUS() {
        nhanVienDAL = new NhanVienDAL();
    }

    public List<NhanVien> getAll() {
        return nhanVienDAL.getAll();
    }

    public NhanVien getById(String maNV) {
        if (empty(maNV)) {
            throw new IllegalArgumentException("Mã nhân viên là bắt buộc.");
        }
        return nhanVienDAL.getById(maNV.trim());
    }

    public boolean add(NhanVien nv) {
        NhanVienValidator.validate(nv);
        return nhanVienDAL.add(nv);
    }

    public boolean update(NhanVien nv) {
        NhanVienValidator.validate(nv);
        return nhanVienDAL.update(nv);
    }

    public boolean delete(String maNV) {
        if (maNV == null || maNV.trim().isEmpty()) {
            return false;
        }
        return nhanVienDAL.delete(maNV);
    }

    private boolean empty(String value) {
        return value == null || value.trim().isEmpty();
    }
}
