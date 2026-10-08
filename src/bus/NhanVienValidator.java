package bus;

import model.NhanVien;

public final class NhanVienValidator {
    private NhanVienValidator() {
    }

    public static void validate(NhanVien employee) {
        if (employee == null || empty(employee.getMaNV()) || empty(employee.getHoTen())) {
            throw new IllegalArgumentException("Mã nhân viên và họ tên là bắt buộc.");
        }
        if (employee.getNgaySinh() == null || employee.getNgayVaoLam() == null) {
            throw new IllegalArgumentException("Ngày sinh và ngày vào làm là bắt buộc.");
        }
        if (employee.getNgaySinh().after(new java.sql.Date(System.currentTimeMillis()))) {
            throw new IllegalArgumentException("Ngày sinh không được ở tương lai.");
        }
        if (!employee.getNgayVaoLam().after(employee.getNgaySinh())) {
            throw new IllegalArgumentException("Ngày vào làm phải sau ngày sinh.");
        }
        if (!Double.isFinite(employee.getHeSoLuong()) || employee.getHeSoLuong() <= 0) {
            throw new IllegalArgumentException("Hệ số lương phải là số dương hợp lệ.");
        }
        if (!empty(employee.getEmail())
                && !employee.getEmail().matches("^[^@\\s]+@[^@\\s]+\\.[^@\\s]+$")) {
            throw new IllegalArgumentException("Email không hợp lệ.");
        }
        if (!empty(employee.getSoDienThoai())
                && !employee.getSoDienThoai().matches("[0-9+() .-]{7,20}")) {
            throw new IllegalArgumentException("Số điện thoại không hợp lệ.");
        }
        employee.setMaNV(employee.getMaNV().trim());
        employee.setHoTen(employee.getHoTen().trim());
    }

    private static boolean empty(String value) {
        return value == null || value.trim().isEmpty();
    }
}
