package gui;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;

public class DonXinPhepPanel extends JPanel {
    private JTable table;
    private DefaultTableModel tableModel;

    public DonXinPhepPanel() {
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
        
        JLabel titleLabel = new JLabel("Quản lý Đơn xin phép & Nghỉ lễ");
        titleLabel.setFont(new Font("Segoe UI", Font.BOLD, 22));
        
        JLabel subTitleLabel = new JLabel("<html><span style='color:#EF4444'>●</span> 24 đơn chờ phê duyệt</html>");
        subTitleLabel.setFont(new Font("Segoe UI", Font.BOLD, 12));
        
        titleActionPanel.add(titleLabel);
        titleActionPanel.add(Box.createHorizontalStrut(15));
        titleActionPanel.add(subTitleLabel);
        titleActionPanel.add(Box.createHorizontalGlue());
        
        titleActionPanel.add(createOutlineBtn("Chính sách nghỉ phép"));
        titleActionPanel.add(Box.createHorizontalStrut(10));
        titleActionPanel.add(createOutlineBtn("Xuất danh sách"));
        titleActionPanel.add(Box.createHorizontalStrut(10));
        titleActionPanel.add(createBtn("+ Tạo đơn xin phép hộ", new Color(40, 167, 69)));

        headerPanel.add(titleActionPanel, BorderLayout.NORTH);

        // Summary Cards
        JPanel cardsPanel = new JPanel(new GridLayout(1, 4, 15, 0));
        cardsPanel.setOpaque(false);
        cardsPanel.setBorder(BorderFactory.createEmptyBorder(15, 0, 15, 0));
        cardsPanel.add(createSummaryCard("ĐƠN CHỜ PHÊ DUYỆT", "24 đơn", "Cần xử lý trong 24h", false));
        cardsPanel.add(createSummaryCard("ĐÃ DUYỆT TRONG THÁNG", "142 đơn", "Tỷ lệ duyệt: 94.5%", true));
        cardsPanel.add(createSummaryCard("NGHỈ PHÉP HÔM NAY", "32 nhân sự", "2.5% tổng quân số (1.280)", false));
        cardsPanel.add(createSummaryCard("QUỸ PHÉP NĂM TỒN TB", "9.4 ngày / NV", "Tổng quỹ: 11.730 ngày", false));
        headerPanel.add(cardsPanel, BorderLayout.CENTER);

        // Toolbar
        JPanel toolbarPanel = new JPanel(new BorderLayout());
        toolbarPanel.setOpaque(false);
        
        JPanel filterTabs = new JPanel(new FlowLayout(FlowLayout.LEFT));
        filterTabs.setOpaque(false);
        filterTabs.add(createFilterBtn("Tất cả đơn (186)", true));
        filterTabs.add(createFilterBtn("Chờ Quản lý duyệt (18)", false));
        filterTabs.add(createFilterBtn("Chờ HR xác nhận (6)", false));
        filterTabs.add(createFilterBtn("Đã phê duyệt (142)", false));
        
        JPanel bulkActionPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        bulkActionPanel.setOpaque(false);
        JLabel lblSelected = new JLabel("Đã chọn: 2 đơn  ");
        lblSelected.setFont(new Font("Segoe UI", Font.BOLD, 12));
        bulkActionPanel.add(lblSelected);
        bulkActionPanel.add(createBtn("Duyệt nhanh", new Color(16, 185, 129)));
        bulkActionPanel.add(createBtn("Từ chối", new Color(239, 68, 68)));
        bulkActionPanel.add(createOutlineBtn("Gửi phản hồi"));

        toolbarPanel.add(filterTabs, BorderLayout.WEST);
        toolbarPanel.add(bulkActionPanel, BorderLayout.EAST);
        
        headerPanel.add(toolbarPanel, BorderLayout.SOUTH);
        add(headerPanel, BorderLayout.NORTH);

        // ================= 2. TABLE =================
        String[] columns = {"MÃ ĐƠN", "NGƯỜI LÀM ĐƠN", "LOẠI NGHỈ PHÉP", "THỜI GIAN NGHỈ", "LÝ DO XIN NGHỈ"};
        tableModel = new DefaultTableModel(columns, 0);
        table = new JTable(tableModel);
        
        table.setRowHeight(60); // Cao hơn để chứa nội dung 2 dòng
        table.getTableHeader().setFont(new Font("Segoe UI", Font.BOLD, 12));
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
        btn.setForeground(active ? new Color(0, 100, 200) : Color.DARK_GRAY);
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
        tableModel.addRow(new Object[]{"DP-2025-058", "Trần Ngọc Phương Vy", "Nghỉ phép năm (AL)", "16/05/2025 -> 17/05/2025 (2.0 ngày)", "Về quê giải quyết việc riêng..."});
        tableModel.addRow(new Object[]{"DP-2025-057", "Lê Hoàng Nam", "Nghỉ ốm đau (BHXH)", "15/05/2025 -> 16/05/2025 (2.0 ngày)", "Sốt virus điều trị ngoại trú..."});
        tableModel.addRow(new Object[]{"DP-2025-056", "Vũ Thị Mai Anh", "Nghỉ việc riêng (CL)", "20/05/2025 -> 20/05/2025 (1.0 ngày)", "Đám cưới em gái ruột..."});
        tableModel.addRow(new Object[]{"DP-2025-055", "Bùi Quang Huy", "Nghỉ phép năm (AL)", "19/05/2025 -> 23/05/2025 (5.0 ngày)", "Nghỉ phép đi du lịch..."});
        tableModel.addRow(new Object[]{"DP-2025-054", "Ngô Thuỳ Dung", "Nghỉ không lương (UL)", "26/05/2025 -> 28/05/2025 (3.0 ngày)", "Chăm sóc người thân..."});
    }
}
