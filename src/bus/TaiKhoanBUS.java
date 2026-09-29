package bus;

import dal.TaiKhoanDAL;
import model.TaiKhoan;

import utils.PasswordUtils;

public class TaiKhoanBUS {
    private TaiKhoanDAL taiKhoanDAL;

    public TaiKhoanBUS() {
        taiKhoanDAL = new TaiKhoanDAL();
    }

    public TaiKhoan login(String username, String password) {
        if (username == null || username.trim().isEmpty() || password == null || password.trim().isEmpty()) {
            return null;
        }
        
        TaiKhoan tk = taiKhoanDAL.getByUsername(username);
        if (tk == null) {
            return null; // Không tồn tại user
        }

        String dbPassword = tk.getMatKhau();
        
        // Cơ chế tương thích ngược (Backward Compatibility):
        // Nếu mật khẩu trong DB có độ dài 64 ký tự (SHA-256), thì băm mật khẩu nhập vào để so sánh
        // Nếu không phải 64 ký tự (DB cũ), thì so sánh plain text
        if (dbPassword.length() == 64) {
            String hashedInput = PasswordUtils.hashPassword(password);
            if (hashedInput.equals(dbPassword)) {
                return tk;
            }
        } else {
            if (password.equals(dbPassword)) {
                return tk;
            }
        }
        
        return null; // Mật khẩu sai
    }

    public java.util.List<TaiKhoan> getAll() {
        return taiKhoanDAL.getAll();
    }

    public boolean addAccount(TaiKhoan tk) {
        // Mã hóa mật khẩu trước khi thêm vào DB
        tk.setMatKhau(PasswordUtils.hashPassword(tk.getMatKhau()));
        return taiKhoanDAL.insert(tk);
    }

    public boolean updateAccount(TaiKhoan tk, boolean updatePassword) {
        if (updatePassword) {
            tk.setMatKhau(PasswordUtils.hashPassword(tk.getMatKhau()));
        }
        return taiKhoanDAL.update(tk);
    }

    public boolean deleteAccount(String username) {
        return taiKhoanDAL.delete(username);
    }
}
