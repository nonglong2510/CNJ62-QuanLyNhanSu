package gui;

import bus.TaiKhoanBUS;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.util.Arrays;

public class DoiMatKhauPanel extends JPanel {
    private final String username;
    private final TaiKhoanBUS taiKhoanBUS = new TaiKhoanBUS();
    private final JPasswordField currentPassword = new JPasswordField(24);
    private final JPasswordField newPassword = new JPasswordField(24);
    private final JPasswordField confirmPassword = new JPasswordField(24);

    public DoiMatKhauPanel(String username) {
        this.username = username;
        setLayout(new BorderLayout());
        setBackground(GuiStyle.BACKGROUND);
        setBorder(new EmptyBorder(22, 22, 22, 22));

        JLabel title = new JLabel("Bảo mật tài khoản");
        title.setFont(new Font("Segoe UI", Font.BOLD, 23));
        title.setForeground(GuiStyle.TEXT);
        JLabel subtitle = new JLabel("Cập nhật mật khẩu đăng nhập của bạn");
        subtitle.setFont(new Font("Segoe UI", Font.PLAIN, 12));
        subtitle.setForeground(GuiStyle.MUTED);
        JPanel titleBlock = new JPanel();
        titleBlock.setOpaque(false);
        titleBlock.setLayout(new BoxLayout(titleBlock, BoxLayout.Y_AXIS));
        titleBlock.add(title);
        titleBlock.add(Box.createVerticalStrut(3));
        titleBlock.add(subtitle);
        add(titleBlock, BorderLayout.NORTH);

        JPanel form = new JPanel(new GridBagLayout());
        form.setBackground(Color.WHITE);
        form.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(new Color(229, 234, 241)),
                new EmptyBorder(20, 20, 20, 20)));
        GridBagConstraints constraints = new GridBagConstraints();
        constraints.insets = new Insets(6, 6, 6, 6);
        constraints.anchor = GridBagConstraints.WEST;
        constraints.gridx = 0;
        constraints.gridy = 0;
        form.add(new JLabel("Tài khoản:"), constraints);
        constraints.gridx = 1;
        form.add(new JLabel(username), constraints);
        addPasswordField(form, constraints, "Mật khẩu hiện tại:", currentPassword);
        addPasswordField(form, constraints, "Mật khẩu mới:", newPassword);
        addPasswordField(form, constraints, "Nhập lại mật khẩu mới:", confirmPassword);

        JButton save = new JButton("Cập nhật mật khẩu");
        GuiStyle.styleButton(save, GuiStyle.BLUE);
        constraints.gridx = 1;
        constraints.gridy++;
        constraints.anchor = GridBagConstraints.EAST;
        form.add(save, constraints);
        save.addActionListener(event -> changePassword());
        JPanel formWrapper = new JPanel(new FlowLayout(FlowLayout.LEFT, 0, 18));
        formWrapper.setOpaque(false);
        formWrapper.add(form);
        add(formWrapper, BorderLayout.CENTER);
    }

    private void addPasswordField(JPanel form, GridBagConstraints constraints,
                                  String label, JPasswordField field) {
        constraints.gridx = 0;
        constraints.gridy++;
        form.add(new JLabel(label), constraints);
        constraints.gridx = 1;
        form.add(field, constraints);
    }

    private void changePassword() {
        char[] current = currentPassword.getPassword();
        char[] replacement = newPassword.getPassword();
        char[] confirmation = confirmPassword.getPassword();
        try {
            if (!Arrays.equals(replacement, confirmation)) {
                throw new IllegalArgumentException("Mật khẩu nhập lại không khớp.");
            }
            if (taiKhoanBUS.changePassword(username, new String(current), new String(replacement))) {
                clearFields();
                JOptionPane.showMessageDialog(this, "Đổi mật khẩu thành công.");
            } else {
                currentPassword.setText("");
                JOptionPane.showMessageDialog(this, "Mật khẩu hiện tại không chính xác.",
                        "Không thể đổi mật khẩu", JOptionPane.WARNING_MESSAGE);
            }
        } catch (IllegalArgumentException | IllegalStateException ex) {
            JOptionPane.showMessageDialog(this, ex.getMessage(),
                    "Không thể đổi mật khẩu", JOptionPane.ERROR_MESSAGE);
        } finally {
            Arrays.fill(current, '\0');
            Arrays.fill(replacement, '\0');
            Arrays.fill(confirmation, '\0');
        }
    }

    private void clearFields() {
        currentPassword.setText("");
        newPassword.setText("");
        confirmPassword.setText("");
    }
}
