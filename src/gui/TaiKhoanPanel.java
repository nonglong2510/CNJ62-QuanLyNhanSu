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
        setBackground(new Color(245, 247, 250));

        // HEADER
        JPanel headerPanel = new JPanel(new BorderLayout());
        headerPanel.setBackground(Color.WHITE);
        headerPanel.setBorder(new EmptyBorder(20, 30, 20, 30));

        JLabel titleLabel = new JLabel("Quản lý Tài Khoản Hệ Thống");
        titleLabel.setFont(new Font("Segoe UI", Font.BOLD, 24));
        titleLabel.setForeground(new Color(15, 23, 42));
        headerPanel.add(titleLabel, BorderLayout.WEST);

        // TOOLBAR (Buttons)
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
        table.setRowHeight(40);
        table.getTableHeader().setFont(new Font("Segoe UI", Font.BOLD, 12));
        table.getTableHeader().setBackground(Color.WHITE);
        table.getTableHeader().setPreferredSize(new Dimension(100, 40));
        table.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        table.setShowVerticalLines(false);
        table.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        
        JScrollPane scrollPane = new JScrollPane(table);
        scrollPane.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));
        scrollPane.getViewport().setBackground(new Color(245, 247, 250));
        add(scrollPane, BorderLayout.CENTER);

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
        btn.setBackground(bg);
        btn.setForeground(Color.WHITE);
        btn.setFont(new Font("Segoe UI", Font.BOLD, 12));
        btn.setFocusPainted(false);
        btn.setBorderPainted(false);
        btn.setCursor(new Cursor(Cursor.HAND_CURSOR));
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
                JOptionPane.showMessageDialog(this, "Thêm tài khoản thành công! Mật khẩu đã được băm SHA-256.");
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
            // MaNV giữ nguyên, ta phải lấy từ DB hoặc tách từ chuỗi
            String maNVString = (String) tableModel.getValueAt(selectedRow, 1);
            String maNV = maNVString.substring(maNVString.lastIndexOf("(") + 1, maNVString.lastIndexOf(")"));
            tk.setMaNV(maNV);
            
            if (updatePass) {
                tk.setMatKhau(newPass);
            } else {
                tk.setMatKhau(""); // Sẽ không update mật khẩu do câu SQL chỉ chạy 1 phần, HOẶC ta phải viết BUS kỹ hơn.
                // Sửa lỗi: hàm update hiện tại bắt buộc phải có mật khẩu. Để đơn giản ta báo lỗi nếu để trống.
                JOptionPane.showMessageDialog(this, "Vui lòng nhập mật khẩu mới! (Tính năng giữ nguyên mật khẩu cũ đang phát triển)", "Thông báo", JOptionPane.WARNING_MESSAGE);
                return;
            }

            if (taiKhoanBUS.updateAccount(tk, updatePass)) {
                JOptionPane.showMessageDialog(this, "Cập nhật tài khoản thành công!");
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
