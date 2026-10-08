package gui;

import bus.TaiKhoanBUS;
import model.TaiKhoan;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.util.List;

public class TaiKhoanPanel extends JPanel {
    private JTable table;
    private DefaultTableModel tableModel;
    private TaiKhoanBUS taiKhoanBUS;

    public TaiKhoanPanel() {
        taiKhoanBUS = new TaiKhoanBUS();
        setLayout(new BorderLayout());
        setBackground(GuiStyle.BACKGROUND);

        JPanel headerPanel = new JPanel(new BorderLayout());
        headerPanel.setOpaque(false);
        headerPanel.setBorder(new EmptyBorder(22, 22, 14, 22));

        JLabel titleLabel = new JLabel("Quản lý Tài Khoản Hệ Thống");
        titleLabel.setFont(new Font("Segoe UI", Font.BOLD, 23));
        titleLabel.setForeground(GuiStyle.TEXT);
        JLabel subtitle = new JLabel("Quản lý quyền truy cập và tài khoản người dùng");
        subtitle.setFont(new Font("Segoe UI", Font.PLAIN, 12));
        subtitle.setForeground(GuiStyle.MUTED);
        JPanel titleBlock = new JPanel();
        titleBlock.setOpaque(false);
        titleBlock.setLayout(new BoxLayout(titleBlock, BoxLayout.Y_AXIS));
        titleBlock.add(titleLabel);
        titleBlock.add(Box.createVerticalStrut(3));
        titleBlock.add(subtitle);
        headerPanel.add(titleBlock, BorderLayout.WEST);

        JPanel toolbarPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        toolbarPanel.setOpaque(false);
        
        JButton btnAdd = createBtn("+ Thêm Tài khoản", new Color(13, 110, 253));
        JButton btnEdit = createBtn("Đổi mật khẩu / Quyền", new Color(25, 135, 84));
        JButton btnDelete = createBtn("Xóa", new Color(220, 53, 69));

        btnAdd.addActionListener(e -> openAddDialog());
        btnEdit.addActionListener(e -> openEditDialog());
        btnDelete.addActionListener(e -> deleteAccount());

        toolbarPanel.add(btnAdd);
        toolbarPanel.add(btnEdit);
        toolbarPanel.add(btnDelete);
        headerPanel.add(toolbarPanel, BorderLayout.EAST);

        add(headerPanel, BorderLayout.NORTH);

        // TABLE
        String[] columns = {"TÊN ĐĂNG NHẬP", "NHÂN VIÊN SỞ HỮU", "QUYỀN HẠN", "TRẠNG THÁI"};
        tableModel = new DefaultTableModel(columns, 0) {
            @Override
            public boolean isCellEditable(int row, int column) { return false; }
        };
        table = new JTable(tableModel);
        GuiStyle.styleTable(table);
        table.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        
        JScrollPane scrollPane = new JScrollPane(table);
        scrollPane.setBorder(BorderFactory.createLineBorder(new Color(229, 234, 241)));
        scrollPane.getViewport().setBackground(Color.WHITE);
        JPanel tableContainer = new JPanel(new BorderLayout());
        tableContainer.setOpaque(false);
        tableContainer.setBorder(new EmptyBorder(0, 22, 22, 22));
        tableContainer.add(scrollPane, BorderLayout.CENTER);
        add(tableContainer, BorderLayout.CENTER);

        loadData();
    }

    private void loadData() {
        tableModel.setRowCount(0);
        List<TaiKhoan> list = taiKhoanBUS.getAll();
        for (TaiKhoan tk : list) {
            tableModel.addRow(new Object[]{
                tk.getTenDangNhap(),
                tk.getHoTen() + " (" + tk.getMaNV() + ")",
                tk.getQuyen(),
                "Đang hoạt động"
            });
        }
    }

    private JButton createBtn(String text, Color bg) {
        JButton btn = new JButton(text);
        GuiStyle.styleButton(btn, bg);
        return btn;
    }

    private void openAddDialog() {
        // Tạm thời hiển thị Dialog đơn giản
        JTextField txtUser = new JTextField();
        JPasswordField txtPass = new JPasswordField();
        JTextField txtMaNV = new JTextField();
        JComboBox<String> cbRole = new JComboBox<>(new String[]{"User", "Admin"});

        Object[] message = {
            "Tên đăng nhập:", txtUser,
            "Mật khẩu:", txtPass,
            "Mã nhân viên (VD: NV001):", txtMaNV,
            "Quyền hạn:", cbRole
        };

        int option = JOptionPane.showConfirmDialog(this, message, "Thêm tài khoản mới", JOptionPane.OK_CANCEL_OPTION);
        if (option == JOptionPane.OK_OPTION) {
            TaiKhoan tk = new TaiKhoan(txtUser.getText(), new String(txtPass.getPassword()), txtMaNV.getText(), cbRole.getSelectedItem().toString());
            if (taiKhoanBUS.addAccount(tk)) {
                JOptionPane.showMessageDialog(this, "Thêm tài khoản thành công! Mật khẩu được lưu bằng PBKDF2.");
                loadData();
            } else {
                JOptionPane.showMessageDialog(this, "Lỗi khi thêm! Tên đăng nhập có thể đã tồn tại hoặc Mã NV không đúng.", "Lỗi", JOptionPane.ERROR_MESSAGE);
            }
        }
    }

    private void openEditDialog() {
        int selectedRow = table.getSelectedRow();
        if (selectedRow < 0) {
            JOptionPane.showMessageDialog(this, "Vui lòng chọn một tài khoản để sửa!");
            return;
        }

        String username = (String) tableModel.getValueAt(selectedRow, 0);
        String currentRole = (String) tableModel.getValueAt(selectedRow, 2);
        
        JPasswordField txtPass = new JPasswordField();
        JComboBox<String> cbRole = new JComboBox<>(new String[]{"User", "Admin"});
        cbRole.setSelectedItem(currentRole);

        Object[] message = {
            "Nhập mật khẩu mới (Để trống nếu không đổi):", txtPass,
            "Quyền hạn:", cbRole
        };

        int option = JOptionPane.showConfirmDialog(this, message, "Cập nhật tài khoản: " + username, JOptionPane.OK_CANCEL_OPTION);
        if (option == JOptionPane.OK_OPTION) {
            String newPass = new String(txtPass.getPassword());
            boolean updatePass = !newPass.isEmpty();
            
            TaiKhoan tk = new TaiKhoan();
            tk.setTenDangNhap(username);
            tk.setQuyen(cbRole.getSelectedItem().toString());
            String maNVString = (String) tableModel.getValueAt(selectedRow, 1);
            String maNV = maNVString.substring(maNVString.lastIndexOf("(") + 1, maNVString.lastIndexOf(")"));
            tk.setMaNV(maNV);
            tk.setMatKhau(newPass);

            if (taiKhoanBUS.updateAccount(tk, updatePass)) {
                JOptionPane.showMessageDialog(this, updatePass
                        ? "Cập nhật tài khoản và mật khẩu thành công."
                        : "Cập nhật tài khoản thành công. Mật khẩu được giữ nguyên.");
                loadData();
            } else {
                JOptionPane.showMessageDialog(this, "Lỗi khi cập nhật!", "Lỗi", JOptionPane.ERROR_MESSAGE);
            }
        }
    }

    private void deleteAccount() {
        int selectedRow = table.getSelectedRow();
        if (selectedRow < 0) {
            JOptionPane.showMessageDialog(this, "Vui lòng chọn một tài khoản để xóa!");
            return;
        }
        
        String username = (String) tableModel.getValueAt(selectedRow, 0);
        
        if (username.equals("admin")) {
            JOptionPane.showMessageDialog(this, "Không thể xóa tài khoản Quản trị viên gốc!", "Lỗi", JOptionPane.ERROR_MESSAGE);
            return;
        }

        int confirm = JOptionPane.showConfirmDialog(this, "Bạn có chắc chắn muốn xóa tài khoản '" + username + "'?", "Xác nhận", JOptionPane.YES_NO_OPTION);
        if (confirm == JOptionPane.YES_OPTION) {
            if (taiKhoanBUS.deleteAccount(username)) {
                JOptionPane.showMessageDialog(this, "Xóa tài khoản thành công!");
                loadData();
            } else {
                JOptionPane.showMessageDialog(this, "Lỗi khi xóa tài khoản!", "Lỗi", JOptionPane.ERROR_MESSAGE);
            }
        }
    }
}
