package gui;

import bus.NhanVienBUS;
import model.NhanVien;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.util.List;

public class NhanVienPanel extends JPanel {
    private JTable table;
    private DefaultTableModel tableModel;
    private NhanVienBUS nhanVienBUS;

    public NhanVienPanel() {
        nhanVienBUS = new NhanVienBUS();
        
        setLayout(new BorderLayout(10, 10));
        setBackground(new Color(245, 245, 245));
        setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));

        // ================= 1. VÙNG HEADER (Tiêu đề + Summary + Toolbar) =================
        JPanel headerPanel = new JPanel(new BorderLayout());
        headerPanel.setOpaque(false);
        
        // Tiêu đề
        JPanel titleContainer = new JPanel(new FlowLayout(FlowLayout.LEFT, 10, 0));
        titleContainer.setOpaque(false);
        JLabel titleLabel = new JLabel("Danh sách Nhân viên");
        titleLabel.setFont(new Font("Segoe UI", Font.BOLD, 26));
        JLabel subTitleLabel = new JLabel("<html><span style='color:#10B981'>●</span> 1.248 Hồ sơ nhân sự</html>");
        subTitleLabel.setFont(new Font("Segoe UI", Font.BOLD, 14));
        titleContainer.add(titleLabel);
        titleContainer.add(subTitleLabel);
        headerPanel.add(titleContainer, BorderLayout.NORTH);

        // Các thẻ Summary (Card)
        JPanel cardsPanel = new JPanel(new GridLayout(1, 4, 15, 0));
        cardsPanel.setOpaque(false);
        cardsPanel.setBorder(BorderFactory.createEmptyBorder(15, 0, 15, 0));
        cardsPanel.add(createSummaryCard("TỔNG SỐ NHÂN VIÊN", "1.248"));
        cardsPanel.add(createSummaryCard("NGHỈ PHÉP HÔM NAY", "32"));
        cardsPanel.add(createSummaryCard("NHÂN VIÊN THỬ VIỆC", "27"));
        cardsPanel.add(createSummaryCard("PHÒNG BAN TRỰC THUỘC", "12"));
        headerPanel.add(cardsPanel, BorderLayout.CENTER);

        // Toolbar (Thanh tìm kiếm và các nút)
        JPanel toolbarPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        toolbarPanel.setOpaque(false);
        
        JTextField searchField = new JTextField(25);
        searchField.setPreferredSize(new Dimension(searchField.getPreferredSize().width, 35));
        searchField.putClientProperty("JTextField.placeholderText", "Tìm theo tên, mã NV, phòng ban, chức vụ... [Ctrl + F]");
        
        JButton btnAdd = createBtn("Thêm mới", new Color(40, 167, 69));
        // Sự kiện mở Form Thêm mới
        btnAdd.addActionListener(e -> {
            JFrame parentFrame = (JFrame) SwingUtilities.getWindowAncestor(this);
            NhanVienDialog dialog = new NhanVienDialog(parentFrame);
            dialog.setVisible(true); // Chương trình sẽ dừng ở đây chờ đến khi tắt Form
            if (dialog.isSuccess()) {
                loadData(); // Tải lại bảng ngay lập tức
            }
        });

        JButton btnEdit = createBtn("Sửa", new Color(0, 123, 255));
        JButton btnDelete = createBtn("Xóa", new Color(220, 53, 69));

        toolbarPanel.add(searchField);
        toolbarPanel.add(btnAdd);
        toolbarPanel.add(btnEdit);
        toolbarPanel.add(btnDelete);
        
        headerPanel.add(toolbarPanel, BorderLayout.SOUTH);

        add(headerPanel, BorderLayout.NORTH);

        // ================= 2. VÙNG TABLE =================
        String[] columns = {"MÃ NV", "HỌ VÀ TÊN", "GIỚI TÍNH", "PHÒNG BAN", "CHỨC VỤ", "TRẠNG THÁI"};
        tableModel = new DefaultTableModel(columns, 0);
        table = new JTable(tableModel);
        
        // Styling cho bảng
        table.setRowHeight(45);
        table.getTableHeader().setFont(new Font("Segoe UI", Font.BOLD, 12));
        table.getTableHeader().setBackground(Color.WHITE);
        table.getTableHeader().setPreferredSize(new Dimension(100, 40));
        table.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        table.setShowVerticalLines(false); // Ẩn viền dọc
        table.setGridColor(new Color(230, 230, 230));

        JScrollPane scrollPane = new JScrollPane(table);
        scrollPane.setBorder(BorderFactory.createLineBorder(new Color(230, 230, 230)));
        scrollPane.getViewport().setBackground(Color.WHITE);
        add(scrollPane, BorderLayout.CENTER);

        // Tải dữ liệu từ CSDL
        loadData();
    }

    private JButton createBtn(String text, Color bg) {
        JButton btn = new JButton(text);
        btn.setBackground(bg);
        btn.setForeground(Color.WHITE);
        btn.setFont(new Font("Segoe UI", Font.BOLD, 13));
        btn.setFocusPainted(false);
        btn.setBorderPainted(false);
        btn.setCursor(new Cursor(Cursor.HAND_CURSOR));
        btn.setPreferredSize(new Dimension(100, 35));
        return btn;
    }

    private JPanel createSummaryCard(String title, String value) {
        JPanel panel = new JPanel();
        panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));
        panel.setBackground(Color.WHITE);
        panel.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(new Color(230, 230, 230)),
                BorderFactory.createEmptyBorder(15, 15, 15, 15)
        ));
        
        JLabel lblTitle = new JLabel(title);
        lblTitle.setFont(new Font("Segoe UI", Font.BOLD, 10));
        lblTitle.setForeground(new Color(100, 100, 100));
        
        JLabel lblValue = new JLabel(value);
        lblValue.setFont(new Font("Segoe UI", Font.BOLD, 28));
        
        panel.add(lblTitle);
        panel.add(Box.createRigidArea(new Dimension(0, 8)));
        panel.add(lblValue);
        return panel;
    }

    private void loadData() {
        tableModel.setRowCount(0);
        List<NhanVien> list = nhanVienBUS.getAll();
        for (NhanVien nv : list) {
            tableModel.addRow(new Object[]{
                    nv.getMaNV(),
                    nv.getHoTen(),
                    nv.getGioiTinh(),
                    nv.getMaPB(), // Tạm hiển thị mã phòng ban
                    nv.getMaCV(), // Tạm hiển thị mã chức vụ
                    nv.getTrangThai()
            });
        }
    }
}
