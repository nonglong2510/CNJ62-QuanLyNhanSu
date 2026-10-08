package bus;

import dal.PhongBanDAL;
import model.PhongBan;
import java.util.List;

public class PhongBanBUS {
    private PhongBanDAL phongBanDAL;

    public PhongBanBUS() {
        phongBanDAL = new PhongBanDAL();
    }

    public List<PhongBan> getAll() {
        return phongBanDAL.getAll();
    }

    public boolean save(PhongBan phongBan, boolean isNew) {
        if (phongBan == null || empty(phongBan.getMaPB()) || empty(phongBan.getTenPB())) {
            throw new IllegalArgumentException("Mã và tên phòng ban là bắt buộc.");
        }
        phongBan.setMaPB(phongBan.getMaPB().trim());
        phongBan.setTenPB(phongBan.getTenPB().trim());
        phongBan.setSoDienThoai(trimToNull(phongBan.getSoDienThoai()));
        return isNew ? phongBanDAL.insert(phongBan) : phongBanDAL.update(phongBan);
    }

    public boolean delete(String maPB) {
        if (empty(maPB)) {
            throw new IllegalArgumentException("Chưa chọn phòng ban.");
        }
        return phongBanDAL.delete(maPB.trim());
    }

    private boolean empty(String value) {
        return value == null || value.trim().isEmpty();
    }

    private String trimToNull(String value) {
        return empty(value) ? null : value.trim();
    }
}
