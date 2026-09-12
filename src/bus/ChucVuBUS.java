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
}
