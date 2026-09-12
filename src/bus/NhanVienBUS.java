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

    public boolean add(NhanVien nv) {
        // Kiểm tra logic nghiệp vụ: Mã nhân viên không được rỗng
        if (nv.getMaNV() == null || nv.getMaNV().trim().isEmpty()) {
            return false;
        }
        
        // Bạn có thể bổ sung các kiểm tra khác ở đây: số điện thoại hợp lệ, email hợp lệ...
        return nhanVienDAL.add(nv);
    }

    public boolean update(NhanVien nv) {
        if (nv.getMaNV() == null || nv.getMaNV().trim().isEmpty()) {
            return false;
        }
        return nhanVienDAL.update(nv);
    }

    public boolean delete(String maNV) {
        if (maNV == null || maNV.trim().isEmpty()) {
            return false;
        }
        return nhanVienDAL.delete(maNV);
    }
}
