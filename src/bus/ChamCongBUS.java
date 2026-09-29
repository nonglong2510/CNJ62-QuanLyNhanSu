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

    public int getTotalWorkDays(String maNV, int month, int year) {
        return chamCongDAL.getTotalWorkDays(maNV, month, year);
    }

    public List<Object[]> getTongHopCongThang(int month, int year) {
        return chamCongDAL.getTongHopCongThang(month, year);
    }
}
