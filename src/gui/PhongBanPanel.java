package gui;

import bus.PhongBanBUS;
import model.PhongBan;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.util.List;

public class PhongBanPanel extends JPanel {
    private JTable table;
    private DefaultTableModel tableModel;
    private PhongBanBUS phongBanBUS;

    public PhongBanPanel() {
        phongBanBUS = new PhongBanBUS();
        
        setLayout(new BorderLayout(10, 10));
        setBackground(new Color(245, 245, 245));
        setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));

        // ================= 1. HEADER =================
        JPanel headerPanel = new JPanel(new BorderLayout());
        headerPanel.setOpaque(false);
        
        JPanel titleContainer = new JPanel(new FlowLayout(FlowLayout.LEFT, 10, 0));
        titleContainer.setOpaque(false);
        JLabel titleLabel = new JLabel("Danh mục Phòng ban");
        titleLabel.setFont(new Font("Segoe UI", Font.BOLD, 26));
        JLabel subTitleLabel = new JLabel("<html><span style='color:#10B981'>●</span> 12 Đơn vị trực thuộc</html>");
        subTitleLabel.setFont(new Font("Segoe UI", Font.BOLD, 14));
        titleContainer.add(titleLabel);
        titleContainer.add(subTitleLabel);
        headerPanel.add(titleContainer, BorderLayout.NORTH);

        // Summary Cards
        JPanel cardsPanel = new JPanel(new GridLayout(1, 4, 15, 0));
        cardsPanel.setOpaque(false);
        cardsPanel.setBorder(BorderFactory.createEmptyBorder(15, 0, 15, 0));
        cardsPanel.add(createSummaryCard("TỔNG SỐ PHÒNG BAN", "12", "100% Hoạt động", true));
        cardsPanel.add(createSummaryCard("NHÂN SỰ TRỰC THUỘC", "1.248", "Bình quân 104 NV / PB", false));
        cardsPanel.add(createSummaryCard("QUY MÔ LỚN NHẤT", "CN & Kỹ thuật", "Chiếm tỷ trọng 27.4%", false));
        cardsPanel.add(createSummaryCard("ĐỊNH BIÊN QUỸ LƯƠNG", "100%", "Phê duyệt đồng bộ", true));
        headerPanel.add(cardsPanel, BorderLayout.CENTER);

        // Toolbar
        JPanel toolbarPanel = new JPanel(new BorderLayout());
        toolbarPanel.setOpaque(false);
        
        JPanel filterPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        filterPanel.setOpaque(false);
        JTextField searchField = new JTextField(25);
        searchField.setPreferredSize(new Dimension(searchField.getPreferredSize().width, 35));
        searchField.putClientProperty("JTextField.placeholderText", "Tìm theo tên phòng, mã PB...");
        filterPanel.add(searchField);
        filterPanel.add(createFilterBtn("Tất cả (12)", true));
        filterPanel.add(createFilterBtn("Khối Kỹ thuật (3)", false));
        filterPanel.add(createFilterBtn("Khối Kinh doanh (4)", false));
        
        JPanel actionPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        actionPanel.setOpaque(false);
        actionPanel.add(createBtn("Thêm mới phòng ban", new Color(40, 167, 69)));
        actionPanel.add(createBtn("Sửa", new Color(0, 123, 255)));
        actionPanel.add(createBtn("Xóa", new Color(220, 53, 69)));

        toolbarPanel.add(filterPanel, BorderLayout.WEST);
        toolbarPanel.add(actionPanel, BorderLayout.EAST);
        
        headerPanel.add(toolbarPanel, BorderLayout.SOUTH);
        add(headerPanel, BorderLayout.NORTH);

        // ================= 2. TABLE =================
        String[] columns = {"MÃ PB", "TÊN PHÒNG BAN", "TRƯỞNG PHÒNG", "QUY MÔ", "VỊ TRÍ", "TRẠNG THÁI"};
        tableModel = new DefaultTableModel(columns, 0);
        table = new JTable(tableModel);
        
        table.setRowHeight(45);
        table.getTableHeader().setFont(new Font("Segoe UI", Font.BOLD, 12));
        table.getTableHeader().setBackground(Color.WHITE);
        table.getTableHeader().setPreferredSize(new Dimension(100, 40));
        table.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        table.setShowVerticalLines(false);
        table.setGridColor(new Color(230, 230, 230));

        JScrollPane scrollPane = new JScrollPane(table);
        scrollPane.setBorder(BorderFactory.createLineBorder(new Color(230, 230, 230)));
        scrollPane.getViewport().setBackground(Color.WHITE);
        add(scrollPane, BorderLayout.CENTER);

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
        btn.setPreferredSize(new Dimension(160, 35));
        if (text.equals("Sửa") || text.equals("Xóa")) {
            btn.setPreferredSize(new Dimension(80, 35));
        }
        return btn;
    }

    private JButton createFilterBtn(String text, boolean active) {
        JButton btn = new JButton(text);
        btn.setBackground(active ? new Color(230, 240, 255) : Color.WHITE);
        btn.setForeground(active ? new Color(0, 100, 200) : Color.DARK_GRAY);
        btn.setFont(new Font("Segoe UI", Font.BOLD, 12));
        btn.setFocusPainted(false);
        btn.setBorder(BorderFactory.createLineBorder(new Color(220, 220, 220)));
        btn.setPreferredSize(new Dimension(text.length() * 8 + 30, 35));
        return btn;
    }

    private JPanel createSummaryCard(String title, String value, String subText, boolean success) {
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
        lblValue.setFont(new Font("Segoe UI", Font.BOLD, 22));
        
        JLabel lblSub = new JLabel(subText);
        lblSub.setFont(new Font("Segoe UI", Font.PLAIN, 11));
        if (success) lblSub.setForeground(new Color(40, 167, 69));
        
        panel.add(lblTitle);
        panel.add(Box.createRigidArea(new Dimension(0, 5)));
        panel.add(lblValue);
        panel.add(Box.createRigidArea(new Dimension(0, 5)));
        panel.add(lblSub);
        return panel;
    }

    private void loadData() {
        tableModel.setRowCount(0);
        List<PhongBan> list = phongBanBUS.getAll();
        for (PhongBan pb : list) {
            tableModel.addRow(new Object[]{
                    pb.getMaPB(),
                    pb.getTenPB(),
                    "Admin (Dummy)", // Dummy data for Trưởng phòng
                    "35 NV", // Dummy data
                    "Tòa nhà A", // Dummy data
                    "Hoạt động" // Dummy data
            });
        }
    }
}
