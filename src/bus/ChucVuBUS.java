package bus;

import dal.ChucVuDAL;
import model.ChucVu;
import java.util.List;

public class ChucVuBUS {
    private ChucVuDAL chucVuDAL;

    public ChucVuBUS() {
        chucVuDAL = new ChucVuDAL();
    }

    public List<ChucVu> getAll() {
        return chucVuDAL.getAll();
    }

    public boolean save(ChucVu chucVu, boolean isNew) {
        if (chucVu == null || empty(chucVu.getMaCV()) || empty(chucVu.getTenCV())) {
            throw new IllegalArgumentException("Mã và tên chức vụ là bắt buộc.");
        }
        if (!Double.isFinite(chucVu.getPhuCapChucVu()) || chucVu.getPhuCapChucVu() < 0) {
            throw new IllegalArgumentException("Phụ cấp phải là số không âm hợp lệ.");
        }
        chucVu.setMaCV(chucVu.getMaCV().trim());
        chucVu.setTenCV(chucVu.getTenCV().trim());
        return isNew ? chucVuDAL.insert(chucVu) : chucVuDAL.update(chucVu);
    }

    public boolean delete(String maCV) {
        if (empty(maCV)) {
            throw new IllegalArgumentException("Chưa chọn chức vụ.");
        }
        return chucVuDAL.delete(maCV.trim());
    }

    private boolean empty(String value) {
        return value == null || value.trim().isEmpty();
    }
}
