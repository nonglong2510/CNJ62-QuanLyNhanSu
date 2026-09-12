package bus;

import dal.TaiKhoanDAL;
import model.TaiKhoan;

public class TaiKhoanBUS {
    private TaiKhoanDAL taiKhoanDAL;

    public TaiKhoanBUS() {
        taiKhoanDAL = new TaiKhoanDAL();
    }

    public TaiKhoan login(String username, String password) {
        // Kiểm tra dữ liệu đầu vào cơ bản trước khi gọi CSDL
        if (username == null || username.trim().isEmpty() || password == null || password.trim().isEmpty()) {
            return null; // Không hợp lệ
        }
        
        // Gọi xuống tầng DAL để xử lý
        return taiKhoanDAL.login(username, password);
    }
}
