package gui;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;

public class KhauTruPanel extends JPanel {
    private JTable table;
    private DefaultTableModel tableModel;

    public KhauTruPanel() {
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
        
        JLabel titleLabel = new JLabel("Quản lý Khấu trừ & Nghĩa vụ Thuế, Bảo hiểm");
        titleLabel.setFont(new Font("Segoe UI", Font.BOLD, 22));
        
        JLabel subTitleLabel = new JLabel("<html><span style='color:#10B981'>●</span> Kỳ đối soát: Tháng 05/2025</html>");
        subTitleLabel.setFont(new Font("Segoe UI", Font.BOLD, 12));
        
        titleActionPanel.add(titleLabel);
        titleActionPanel.add(Box.createHorizontalStrut(15));
        titleActionPanel.add(subTitleLabel);
        titleActionPanel.add(Box.createHorizontalGlue());
        
        titleActionPanel.add(createOutlineBtn("Đồng bộ phạt chấm công"));
        titleActionPanel.add(Box.createHorizontalStrut(10));
        titleActionPanel.add(createOutlineBtn("Cập nhật biểu thuế / BHXH"));
        titleActionPanel.add(Box.createHorizontalStrut(10));
        titleActionPanel.add(createBtn("+ Thêm khoản khấu trừ đặc thù", new Color(25, 135, 84)));

        headerPanel.add(titleActionPanel, BorderLayout.NORTH);

        // Summary Cards
        JPanel cardsPanel = new JPanel(new GridLayout(1, 4, 15, 0));
        cardsPanel.setOpaque(false);
        cardsPanel.setBorder(BorderFactory.createEmptyBorder(15, 0, 15, 0));
        cardsPanel.add(createSummaryCard("TỔNG BHXH / BHYT / BHTN", "2,609,250,000 đ", "Khấu trừ lương NLĐ", true));
        cardsPanel.add(createSummaryCard("THUẾ TNCN TẠM TÍNH", "1,845,600,000 đ", "482 nhân sự nộp thuế", false));
        cardsPanel.add(createSummaryCard("TỔNG TẠM ỨNG TRONG KỲ", "340,000,000 đ", "42 đề xuất đã giải ngân", false));
        cardsPanel.add(createSummaryCard("PHẠT VI PHẠM & KHẤU TRỪ", "48,500,000 đ", "86 lượt đi muộn", false));
        headerPanel.add(cardsPanel, BorderLayout.CENTER);

        // Toolbar
        JPanel toolbarPanel = new JPanel();
        toolbarPanel.setLayout(new BoxLayout(toolbarPanel, BoxLayout.X_AXIS));
        toolbarPanel.setOpaque(false);
        
        JPanel filterTabs = new JPanel(new FlowLayout(FlowLayout.LEFT));
        filterTabs.setOpaque(false);
        filterTabs.add(createFilterBtn("Bảo hiểm & Thuế TNCN", true));
        filterTabs.add(createFilterBtn("Tạm ứng lương trong tháng", false));
        filterTabs.add(createFilterBtn("Khấu trừ khác & Phạt vi phạm", false));
        
        toolbarPanel.add(filterTabs);
        toolbarPanel.add(Box.createHorizontalGlue());
        
        headerPanel.add(toolbarPanel, BorderLayout.SOUTH);
        add(headerPanel, BorderLayout.NORTH);

        // ================= 2. TABLE =================
        String[] columns = {"MÃ NV", "HỌ TÊN & VỊ TRÍ", "LƯƠNG ĐÓNG BH", "BHXH (8%)", "BHYT (1.5%)", "BHTN (1%)", "THUẾ TNCN", "TẠM ỨNG / KHÁC", "TỔNG KHẤU TRỪ"};
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
        tableModel.addRow(new Object[]{"EMP-00108", "Trần Quốc Mạnh", "36,000,000", "2,880,000", "540,000", "360,000", "9,245,000", "0", "13,025,000"});
        tableModel.addRow(new Object[]{"EMP-00342", "Nguyễn Thu Hương", "22,500,000", "1,800,000", "337,500", "225,000", "912,500", "5,000,000", "8,275,000"});
        tableModel.addRow(new Object[]{"EMP-00419", "Lê Hoàng Duy", "18,000,000", "1,440,000", "270,000", "180,000", "382,500", "350,000", "2,622,500"});
        tableModel.addRow(new Object[]{"EMP-00582", "Vũ Thị Mai Phương", "16,500,000", "1,320,000", "247,500", "165,000", "0", "0", "1,732,500"});
        tableModel.addRow(new Object[]{"EMP-00714", "Đỗ Hữu Nghĩa", "28,000,000", "2,240,000", "420,000", "280,000", "1,825,000", "1,200,000", "5,965,000"});
    }
}
