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
        if (username == null || username.trim().isEmpty() || password == null || password.isEmpty()) {
            return null;
        }
        
        TaiKhoan tk = taiKhoanDAL.getByUsername(username.trim());
        if (tk == null) {
            return null; // Không tồn tại user
        }

        String dbPassword = tk.getMatKhau();
        if (!PasswordUtils.verifyPassword(password, dbPassword)) {
            return null;
        }

        if (PasswordUtils.needsUpgrade(dbPassword)) {
            String upgradedHash = PasswordUtils.hashPassword(password);
            if (!taiKhoanDAL.updatePassword(tk.getTenDangNhap(), upgradedHash)) {
                throw new IllegalStateException("Đăng nhập hợp lệ nhưng không thể nâng cấp mật khẩu đã lưu.");
            }
            tk.setMatKhau(upgradedHash);
        }
        return tk;
    }

    public boolean changePassword(String username, String currentPassword, String newPassword) {
        if (username == null || username.trim().isEmpty()
                || currentPassword == null || currentPassword.isEmpty()
                || newPassword == null || newPassword.isEmpty()) {
            throw new IllegalArgumentException("Tên đăng nhập và các mật khẩu không được để trống.");
        }
        if (currentPassword.equals(newPassword)) {
            throw new IllegalArgumentException("Mật khẩu mới phải khác mật khẩu hiện tại.");
        }

        TaiKhoan account = taiKhoanDAL.getByUsername(username.trim());
        if (account == null) {
            throw new IllegalStateException("Không tìm thấy tài khoản cần đổi mật khẩu.");
        }
        if (!PasswordUtils.verifyPassword(currentPassword, account.getMatKhau())) {
            return false;
        }
        if (!taiKhoanDAL.updatePassword(account.getTenDangNhap(), PasswordUtils.hashPassword(newPassword))) {
            throw new IllegalStateException("Không thể lưu mật khẩu mới.");
        }
        return true;
    }

    public java.util.List<TaiKhoan> getAll() {
        return taiKhoanDAL.getAll();
    }

    public boolean addAccount(TaiKhoan tk) {
        if (tk == null || tk.getTenDangNhap() == null || tk.getTenDangNhap().trim().isEmpty()
                || tk.getMatKhau() == null || tk.getMatKhau().isEmpty()) {
            return false;
        }
        tk.setMatKhau(PasswordUtils.hashPassword(tk.getMatKhau()));
        return taiKhoanDAL.insert(tk);
    }

    public boolean updateAccount(TaiKhoan tk, boolean updatePassword) {
        if (tk == null || tk.getTenDangNhap() == null || tk.getTenDangNhap().trim().isEmpty()
                || (updatePassword && (tk.getMatKhau() == null || tk.getMatKhau().isEmpty()))) {
            return false;
        }
        if (updatePassword) {
            tk.setMatKhau(PasswordUtils.hashPassword(tk.getMatKhau()));
        } else {
            TaiKhoan current = taiKhoanDAL.getByUsername(tk.getTenDangNhap());
            if (current == null) {
                return false;
            }
            tk.setMatKhau(current.getMatKhau());
        }
        return taiKhoanDAL.update(tk);
    }

    public boolean deleteAccount(String username) {
        return taiKhoanDAL.delete(username);
    }
}
