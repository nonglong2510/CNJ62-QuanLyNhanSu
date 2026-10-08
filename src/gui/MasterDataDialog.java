package gui;

import model.ChucVu;
import model.PhongBan;

import javax.swing.*;
import java.awt.*;

final class MasterDataDialog {
    private MasterDataDialog() {
    }

    static PhongBan editDepartment(Component parent, PhongBan original) {
        JTextField code = new JTextField(original == null ? "" : original.getMaPB());
        JTextField name = new JTextField(original == null ? "" : original.getTenPB());
        JTextField phone = new JTextField(original == null ? "" : nullToEmpty(original.getSoDienThoai()));
        code.setEnabled(original == null);
        JPanel form = form(new String[]{"Mã phòng ban:", "Tên phòng ban:", "Số điện thoại:"},
                new JComponent[]{code, name, phone});
        int result = JOptionPane.showConfirmDialog(parent, form,
                original == null ? "Thêm phòng ban" : "Sửa phòng ban",
                JOptionPane.OK_CANCEL_OPTION, JOptionPane.PLAIN_MESSAGE);
        if (result != JOptionPane.OK_OPTION) {
            return null;
        }
        PhongBan department = new PhongBan();
        department.setMaPB(code.getText());
        department.setTenPB(name.getText());
        department.setSoDienThoai(phone.getText());
        return department;
    }

    static ChucVu editPosition(Component parent, ChucVu original) {
        JTextField code = new JTextField(original == null ? "" : original.getMaCV());
        JTextField name = new JTextField(original == null ? "" : original.getTenCV());
        JTextField allowance = new JTextField(original == null ? "0" : Double.toString(original.getPhuCapChucVu()));
        code.setEnabled(original == null);
        JPanel form = form(new String[]{"Mã chức vụ:", "Tên chức vụ:", "Phụ cấp (VND):"},
                new JComponent[]{code, name, allowance});
        int result = JOptionPane.showConfirmDialog(parent, form,
                original == null ? "Thêm chức vụ" : "Sửa chức vụ",
                JOptionPane.OK_CANCEL_OPTION, JOptionPane.PLAIN_MESSAGE);
        if (result != JOptionPane.OK_OPTION) {
            return null;
        }
        try {
            ChucVu position = new ChucVu();
            position.setMaCV(code.getText());
            position.setTenCV(name.getText());
            position.setPhuCapChucVu(Double.parseDouble(allowance.getText().trim()));
            return position;
        } catch (NumberFormatException ex) {
            throw new IllegalArgumentException("Phụ cấp phải là số hợp lệ.");
        }
    }

    private static JPanel form(String[] labels, JComponent[] fields) {
        JPanel panel = new JPanel(new GridLayout(labels.length, 2, 8, 10));
        panel.setPreferredSize(new Dimension(360, labels.length * 38));
        panel.setBackground(Color.WHITE);
        panel.setBorder(BorderFactory.createEmptyBorder(8, 8, 8, 8));
        for (int i = 0; i < labels.length; i++) {
            JLabel label = new JLabel(labels[i]);
            label.setFont(new Font("Segoe UI", Font.PLAIN, 12));
            label.setForeground(GuiStyle.TEXT);
            panel.add(label);
            fields[i].setFont(new Font("Segoe UI", Font.PLAIN, 13));
            panel.add(fields[i]);
        }
        return panel;
    }

    private static String nullToEmpty(String value) {
        return value == null ? "" : value;
    }
}
