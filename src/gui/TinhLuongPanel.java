package gui;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.util.List;
import bus.BangLuongBUS;
import model.BangLuong;

public class TinhLuongPanel extends JPanel {
    private JTable table;
    private DefaultTableModel tableModel;

    public TinhLuongPanel() {
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
        
        JLabel titleLabel = new JLabel("Bảng Tính Lương Tháng 05/2025");
        titleLabel.setFont(new Font("Segoe UI", Font.BOLD, 22));
        
        JLabel subTitleLabel = new JLabel("<html><span style='color:#10B981'>●</span> Đang kiểm tra & rà soát</html>");
        subTitleLabel.setFont(new Font("Segoe UI", Font.BOLD, 12));
        
        titleActionPanel.add(titleLabel);
        titleActionPanel.add(Box.createHorizontalStrut(15));
        titleActionPanel.add(subTitleLabel);
        titleActionPanel.add(Box.createHorizontalGlue());
        
        titleActionPanel.add(createOutlineBtn("Kiểm tra lỗi logic"));
        titleActionPanel.add(Box.createHorizontalStrut(10));
        titleActionPanel.add(createBtn("Chạy tính toán lại", new Color(13, 110, 253)));
        titleActionPanel.add(Box.createHorizontalStrut(10));
        titleActionPanel.add(createBtn("Khóa & Phê duyệt kỳ lương", new Color(25, 135, 84)));

        headerPanel.add(titleActionPanel, BorderLayout.NORTH);

        // Summary Cards
        JPanel cardsPanel = new JPanel(new GridLayout(1, 4, 15, 0));
        cardsPanel.setOpaque(false);
        cardsPanel.setBorder(BorderFactory.createEmptyBorder(15, 0, 15, 0));
        cardsPanel.add(createSummaryCard("TỔNG THỰC CHI (NET PAY)", "21,385,400,000 đ", "Quy mô 1,248 nhân sự", true));
        cardsPanel.add(createSummaryCard("TỔNG LƯƠNG GỘP (GROSS)", "26,230,000,000 đ", "Thuế & BH: 18.47%", false));
        cardsPanel.add(createSummaryCard("TIỀN THÊM GIỜ (OT)", "685,200,000 đ", "Tổng số giờ: 1480.5", true));
        cardsPanel.add(createSummaryCard("THƯỞNG HIỆU SUẤT & KPI", "1,240,000,000 đ", "Đạt mốc KPI công ty: 96.8%", true));
        headerPanel.add(cardsPanel, BorderLayout.CENTER);

        // Toolbar
        JPanel toolbarPanel = new JPanel();
        toolbarPanel.setLayout(new BoxLayout(toolbarPanel, BoxLayout.X_AXIS));
        toolbarPanel.setOpaque(false);
        
        JPanel filterTabs = new JPanel(new FlowLayout(FlowLayout.LEFT));
        filterTabs.setOpaque(false);
        filterTabs.add(createFilterBtn("Tìm theo tên NV, mã NV... [Ctrl+F]", false));
        filterTabs.add(createFilterBtn("Tất cả phòng ban (7)", true));
        
        JPanel bulkActionPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        bulkActionPanel.setOpaque(false);
        bulkActionPanel.add(createOutlineBtn("Xuất Excel"));

        toolbarPanel.add(filterTabs);
        toolbarPanel.add(Box.createHorizontalGlue());
        toolbarPanel.add(bulkActionPanel);
        
        headerPanel.add(toolbarPanel, BorderLayout.SOUTH);
        add(headerPanel, BorderLayout.NORTH);

        // ================= 2. TABLE =================
        String[] columns = {"MÃ NV", "HỌ VÀ TÊN", "PHÒNG BAN", "LƯƠNG GROSS", "CÔNG THỰC TẾ", "LƯƠNG THỜI GIAN", "PHỤ CẤP & KPI", "LÀM THÊM (OT)", "TỔNG THU NHẬP"};
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

        loadDataFromDB(9, 2026);
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

    private void loadDataFromDB(int month, int year) {
        BangLuongBUS bus = new BangLuongBUS();
        
        // Tự động tính lương cho toàn bộ nhân viên trong tháng
        bus.calculateSalaryForMonth(month, year);
        
        // Lấy danh sách kết quả
        List<BangLuong> list = bus.getByMonth(month, year);
        
        tableModel.setRowCount(0);
        java.text.DecimalFormat df = new java.text.DecimalFormat("#,###");
        
        for (BangLuong bl : list) {
            tableModel.addRow(new Object[]{
                bl.getMaNV(),
                bl.getHoTen(),
                bl.getTenPB(),
                df.format(bl.getLuongCoBan()), // MỨC LƯƠNG
                bl.getSoNgayCong(), // NGÀY CÔNG
                df.format(bl.getLuongCoBan()), // LƯƠNG CHÍNH
                df.format(bl.getTongPhuCap() + bl.getTienThuong()), // PHỤ CẤP / THƯỞNG
                df.format(bl.getTienPhat()), // KHẤU TRỪ / PHẠT
                df.format(bl.getThucLanh()) // THỰC LÃNH
            });
        }
    }
}
