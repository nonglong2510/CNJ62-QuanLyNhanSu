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
}
