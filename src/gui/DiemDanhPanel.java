package gui;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;

public class DiemDanhPanel extends JPanel {
    private JTable table;
    private DefaultTableModel tableModel;

    public DiemDanhPanel() {
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
        
        JLabel titleLabel = new JLabel("Điểm danh hàng ngày");
        titleLabel.setFont(new Font("Segoe UI", Font.BOLD, 22));
        
        JLabel subTitleLabel = new JLabel("<html><span style='color:#3B82F6'>●</span> Hôm nay: Thứ Tư, 15/05/2025</html>");
        subTitleLabel.setFont(new Font("Segoe UI", Font.BOLD, 12));
        
        titleActionPanel.add(titleLabel);
        titleActionPanel.add(Box.createHorizontalStrut(15));
        titleActionPanel.add(subTitleLabel);
        titleActionPanel.add(Box.createHorizontalGlue());
        
        titleActionPanel.add(createOutlineBtn("Đồng bộ máy chấm công"));
        titleActionPanel.add(Box.createHorizontalStrut(10));
        titleActionPanel.add(createOutlineBtn("Xuất báo cáo ngày"));

        headerPanel.add(titleActionPanel, BorderLayout.NORTH);

        // Summary Cards
        JPanel cardsPanel = new JPanel(new GridLayout(1, 4, 15, 0));
        cardsPanel.setOpaque(false);
        cardsPanel.setBorder(BorderFactory.createEmptyBorder(15, 0, 15, 0));
        cardsPanel.add(createSummaryCard("TỔNG QUÂN SỐ LÀM VIỆC", "1.248", "100% dữ liệu đã ghi nhận", true));
        cardsPanel.add(createSummaryCard("ĐÚNG GIỜ (ON-TIME)", "1.156", "92.6% tỷ lệ tuân thủ", true));
        cardsPanel.add(createSummaryCard("ĐI MUỘN / VỀ SỚM", "42", "34 muộn • 8 về sớm", false));
        cardsPanel.add(createSummaryCard("VẮNG MẶT / CHƯA ĐIỂM DANH", "50", "38 có phép • 12 chưa check-in", false));
        headerPanel.add(cardsPanel, BorderLayout.CENTER);

        // Toolbar
        JPanel toolbarPanel = new JPanel(new BorderLayout());
        toolbarPanel.setOpaque(false);
        
        JPanel filterTabs = new JPanel(new FlowLayout(FlowLayout.LEFT));
        filterTabs.setOpaque(false);
        filterTabs.add(createFilterBtn("Tất cả (1.248)", true));
        filterTabs.add(createFilterBtn("Đúng giờ (1.156)", false));
        filterTabs.add(createFilterBtn("Đi muộn (34)", false));
        filterTabs.add(createFilterBtn("Về sớm (8)", false));
        
        JPanel bulkActionPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        bulkActionPanel.setOpaque(false);
        bulkActionPanel.add(createBtn("+ Điểm danh bù / Thủ công", new Color(30, 64, 175)));
        bulkActionPanel.add(createBtn("Duyệt giải trình", new Color(59, 130, 246)));
        bulkActionPanel.add(createOutlineBtn("Gửi nhắc nhở qua Email"));

        toolbarPanel.add(filterTabs, BorderLayout.WEST);
        toolbarPanel.add(bulkActionPanel, BorderLayout.EAST);
        
        headerPanel.add(toolbarPanel, BorderLayout.SOUTH);
        add(headerPanel, BorderLayout.NORTH);

        // ================= 2. TABLE =================
        String[] columns = {"MÃ NV", "HỌ TÊN", "PHÒNG BAN", "CA LÀM VIỆC", "CHECK-IN", "CHECK-OUT", "TỔNG GIỜ", "THIẾT BỊ / ĐỊA ĐIỂM"};
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
        tableModel.addRow(new Object[]{"NV-1048", "Nguyễn Văn Tuấn", "Ban CN & Kỹ thuật", "Hành chính (08:00 - 17:30)", "07:52:14", "17:35:10", "8h 42m", "FaceID Cổng A - Tầng 8"});
        tableModel.addRow(new Object[]{"NV-1049", "Trần Thị Thu Hà", "Ban Kinh doanh", "Hành chính (08:00 - 17:30)", "08:18:40", "17:31:05", "8h 12m", "Vân tay Cổng B - Sảnh 1"});
        tableModel.addRow(new Object[]{"NV-1050", "Lê Hoàng Nam", "Phòng Kế toán", "Hành chính (08:00 - 17:30)", "07:58:02", "17:02:00", "8h 04m", "FaceID Cổng A - Tầng 8"});
        tableModel.addRow(new Object[]{"NV-1051", "Phạm Thị Mai Ly", "Phòng Nhân sự", "Hành chính (08:00 - 17:30)", "--:--:--", "--:--:--", "0h 00m", "Đơn nghỉ phép: #NP-8821"});
        tableModel.addRow(new Object[]{"NV-1052", "Vũ Đình Trọng", "Khối Vận hành", "Hành chính (08:00 - 17:30)", "Chưa ghi nhận", "--:--:--", "0h 00m", "Không có tín hiệu"});
    }
}
