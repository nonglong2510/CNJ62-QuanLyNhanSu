package utils;

import model.TaiKhoan;

public final class AccessPolicy {
    private AccessPolicy() {
    }

    public static void validateAccount(TaiKhoan account) {
        if (account == null || (!isAdmin(account) && !"User".equals(account.getQuyen()))) {
            throw new IllegalArgumentException("Tài khoản hoặc quyền truy cập không hợp lệ.");
        }
        if ("User".equals(account.getQuyen()) && empty(account.getMaNV())) {
            throw new IllegalArgumentException(
                    "Tài khoản User chưa được liên kết với hồ sơ nhân viên.");
        }
    }

    public static boolean isAdmin(TaiKhoan account) {
        return account != null && "Admin".equals(account.getQuyen());
    }

    public static String requireEmployeeId(TaiKhoan account) {
        validateAccount(account);
        if (isAdmin(account)) {
            throw new IllegalArgumentException("Tài khoản Admin không có phạm vi nhân viên cá nhân.");
        }
        return account.getMaNV().trim();
    }

    private static boolean empty(String value) {
        return value == null || value.trim().isEmpty();
    }
}
