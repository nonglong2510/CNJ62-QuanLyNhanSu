package gui;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;

public class LuongCoBanPanel extends JPanel {
    private JTable table;
    private DefaultTableModel tableModel;

    public LuongCoBanPanel() {
        setLayout(new BorderLayout(10, 10));
        setBackground(new Color(245, 245, 245));
        setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));

        // ================= 1. HEADER =================
        JPanel headerPanel = new JPanel(new BorderLayout());
        headerPanel.setOpaque(false);
        
        // Title & Top Actions
        JPanel titleActionPanel = new JPanel();
        titleActionPanel.setLayout(new BoxLayout(titleActionPanel, BoxLayout.X_AXIS));
        titleActionPanel.setOpaque(false);
        
        JLabel titleLabel = new JLabel("Quản lý Lương cơ bản & Phụ cấp");
        titleLabel.setFont(new Font("Segoe UI", Font.BOLD, 22));
        
        JLabel subTitleLabel = new JLabel("<html><span style='color:#10B981'>●</span> Quy chế lương 2025 (Hiệu lực)</html>");
        subTitleLabel.setFont(new Font("Segoe UI", Font.BOLD, 12));
        
        titleActionPanel.add(titleLabel);
        titleActionPanel.add(Box.createHorizontalStrut(15));
        titleActionPanel.add(subTitleLabel);
        titleActionPanel.add(Box.createHorizontalGlue());
        
        titleActionPanel.add(createOutlineBtn("Xuất Excel"));
        titleActionPanel.add(Box.createHorizontalStrut(10));
        titleActionPanel.add(createOutlineBtn("Cập nhật hàng loạt"));
        titleActionPanel.add(Box.createHorizontalStrut(10));
        titleActionPanel.add(createBtn("+ Thiết lập ngạch bậc lương mới", new Color(13, 110, 253)));

        headerPanel.add(titleActionPanel, BorderLayout.NORTH);

        // Summary Cards
        JPanel cardsPanel = new JPanel(new GridLayout(1, 4, 15, 0));
        cardsPanel.setOpaque(false);
        cardsPanel.setBorder(BorderFactory.createEmptyBorder(15, 0, 15, 0));
        cardsPanel.add(createSummaryCard("TỔNG QUỸ LƯƠNG CƠ BẢN", "24,850,000,000 đ", "Áp dụng 1,248 nhân sự", true));
        cardsPanel.add(createSummaryCard("MỨC LƯƠNG BÌNH QUÂN", "19,910,000 đ", "+5.2% so với 2024", true));
        cardsPanel.add(createSummaryCard("TỔNG QUỸ PHỤ CẤP THÁNG", "3,420,000,000 đ", "Chiếm 13.8% tổng quỹ lương", false));
        cardsPanel.add(createSummaryCard("DANH MỤC PHỤ CẤP", "12 khoản mục", "100% hợp lệ", true));
        headerPanel.add(cardsPanel, BorderLayout.CENTER);

        // Toolbar
        JPanel toolbarPanel = new JPanel();
        toolbarPanel.setLayout(new BoxLayout(toolbarPanel, BoxLayout.X_AXIS));
        toolbarPanel.setOpaque(false);
        
        JPanel filterTabs = new JPanel(new FlowLayout(FlowLayout.LEFT));
        filterTabs.setOpaque(false);
        filterTabs.add(createFilterBtn("Ngạch bậc & Mức lương (1,248)", true));
        filterTabs.add(createFilterBtn("Danh mục Phụ cấp định mức (12)", false));
        filterTabs.add(createFilterBtn("Cấu hình Lương theo Hợp đồng", false));
        
        JPanel bulkActionPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        bulkActionPanel.setOpaque(false);
        bulkActionPanel.add(createOutlineBtn("Lọc theo Mã NV, tên..."));

        toolbarPanel.add(filterTabs);
        toolbarPanel.add(Box.createHorizontalGlue());
        toolbarPanel.add(bulkActionPanel);
        
        headerPanel.add(toolbarPanel, BorderLayout.SOUTH);
        add(headerPanel, BorderLayout.NORTH);

        // ================= 2. TABLE =================
        String[] columns = {"MÃ NV", "HỌ TÊN NHÂN SỰ", "PHÒNG BAN & VỊ TRÍ", "NGẠCH / BẬC", "LƯƠNG CƠ BẢN", "PC TRÁCH NHIỆM", "PC ĂN TRƯA", "TỔNG THU NHẬP CĐ"};
        tableModel = new DefaultTableModel(columns, 0);
        table = new JTable(tableModel);
        
        table.setRowHeight(50); 
        table.getTableHeader().setFont(new Font("Segoe UI", Font.BOLD, 11));
        table.getTableHeader().setBackground(Color.WHITE);
        table.getTableHeader().setPreferredSize(new Dimension(100, 40));
        table.setFont(new Font("Segoe UI", Font.PLAIN, 13));
        table.setShowVerticalLines(false);
        table.setGridColor(new Color(230, 230, 230));

        JScrollPane scrollPane = new JScrollPane(table);
        scrollPane.setBorder(BorderFactory.createLineBorder(new Color(230, 230, 230)));
        scrollPane.getViewport().setBackground(Color.WHITE);
        add(scrollPane, BorderLayout.CENTER);

        loadDummyData();
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

    private JButton createOutlineBtn(String text) {
        JButton btn = new JButton(text);
        btn.setBackground(Color.WHITE);
        btn.setForeground(Color.DARK_GRAY);
        btn.setFont(new Font("Segoe UI", Font.BOLD, 12));
        btn.setFocusPainted(false);
        btn.setBorder(BorderFactory.createLineBorder(new Color(200, 200, 200)));
        btn.setCursor(new Cursor(Cursor.HAND_CURSOR));
        return btn;
    }

    private JButton createFilterBtn(String text, boolean active) {
        JButton btn = new JButton(text);
        btn.setBackground(active ? new Color(230, 240, 255) : Color.WHITE);
        btn.setForeground(active ? new Color(13, 110, 253) : Color.DARK_GRAY);
        btn.setFont(new Font("Segoe UI", Font.BOLD, 12));
        btn.setFocusPainted(false);
        btn.setBorder(BorderFactory.createLineBorder(new Color(220, 220, 220)));
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

    private void loadDummyData() {
        tableModel.addRow(new Object[]{"NV-00812", "Nguyễn Tuấn Khôi", "Giám đốc Kỹ thuật (CTO)", "Bậc 7.2", "36,800,000", "8,000,000", "2,500,000", "47,300,000"});
        tableModel.addRow(new Object[]{"NV-01044", "Lê Thị Mai Anh", "Kỹ sư Kiến trúc Dữ liệu", "Bậc 5.1", "26,500,000", "3,500,000", "2,000,000", "32,000,000"});
        tableModel.addRow(new Object[]{"NV-01298", "Trần Minh Hoàng", "Trưởng phòng Kế toán", "Bậc 6.1", "28,500,000", "5,000,000", "2,000,000", "35,500,000"});
        tableModel.addRow(new Object[]{"NV-01452", "Phạm Ngọc Bích", "Chuyên viên Nhân sự", "Bậc 4.3", "19,200,000", "1,500,000", "1,800,000", "22,500,000"});
        tableModel.addRow(new Object[]{"NV-01773", "Đỗ Hoàng Nam", "Trưởng nhóm Kinh doanh", "Bậc 5.2", "21,500,000", "4,000,000", "3,200,000", "28,700,000"});
    }
}
