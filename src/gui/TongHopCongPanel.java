package gui;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.util.List;
import bus.ChamCongBUS;

public class TongHopCongPanel extends JPanel {
    private JTable table;
    private DefaultTableModel tableModel;

    public TongHopCongPanel() {
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
        
        JLabel titleLabel = new JLabel("Bảng tổng hợp công & Timesheet");
        titleLabel.setFont(new Font("Segoe UI", Font.BOLD, 22));
        
        titleActionPanel.add(titleLabel);
        titleActionPanel.add(Box.createHorizontalGlue());
        
        titleActionPanel.add(createOutlineBtn("Tính lại công tự động"));
        titleActionPanel.add(Box.createHorizontalStrut(10));
        titleActionPanel.add(createOutlineBtn("Xuất Excel tổng hợp"));
        titleActionPanel.add(Box.createHorizontalStrut(10));
        titleActionPanel.add(createBtn("Chốt bảng công kỳ này", new Color(13, 71, 161)));

        headerPanel.add(titleActionPanel, BorderLayout.NORTH);

        // Summary Cards
        JPanel cardsPanel = new JPanel(new GridLayout(1, 4, 15, 0));
        cardsPanel.setOpaque(false);
        cardsPanel.setBorder(BorderFactory.createEmptyBorder(15, 0, 15, 0));
        cardsPanel.add(createSummaryCard("TỔNG CÔNG CHUẨN ĐỊNH MỨC", "22.0", "Quy chuẩn: 176 giờ chuẩn", false));
        cardsPanel.add(createSummaryCard("TỔNG NGÀY CÔNG THỰC TẾ", "26,420", "Đạt 96.2% định mức", true));
        cardsPanel.add(createSummaryCard("TỔNG GIỜ LÀM THÊM (OT)", "1,480.5", "OT 150%: 1,120h • OT 200%: 360.5h", false));
        cardsPanel.add(createSummaryCard("TỶ LỆ CHUYÊN CẦN CHUNG", "97.8%", "114 trễ/sớm", false));
        headerPanel.add(cardsPanel, BorderLayout.CENTER);

        // Toolbar
        JPanel toolbarPanel = new JPanel(new BorderLayout());
        toolbarPanel.setOpaque(false);
        
        JPanel filterTabs = new JPanel(new FlowLayout(FlowLayout.LEFT));
        filterTabs.setOpaque(false);
        filterTabs.add(createFilterBtn("Bảng tổng hợp", true));
        filterTabs.add(createFilterBtn("Lưới lịch 1-31", false));
        filterTabs.add(createFilterBtn("Cảnh báo sai lệch (8)", false));
        
        JPanel bulkActionPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        bulkActionPanel.setOpaque(false);
        bulkActionPanel.add(createOutlineBtn("Gửi rà soát"));

        toolbarPanel.add(filterTabs, BorderLayout.WEST);
        toolbarPanel.add(bulkActionPanel, BorderLayout.EAST);
        
        headerPanel.add(toolbarPanel, BorderLayout.SOUTH);
        add(headerPanel, BorderLayout.NORTH);

        // ================= 2. TABLE =================
        String[] columns = {"MÃ NV", "HỌ VÀ TÊN", "PHÒNG BAN", "ĐỊNH MỨC", "CÔNG THỰC TẾ", "PHÉP NĂM (P)", "BHXH (Ô/TS)", "KHÔNG LƯƠNG"};
        tableModel = new DefaultTableModel(columns, 0);
        table = new JTable(tableModel);
        
        table.setRowHeight(45);
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

    private void loadDataFromDB(int month, int year) {
        ChamCongBUS bus = new ChamCongBUS();
        List<Object[]> list = bus.getTongHopCongThang(month, year);
        
        tableModel.setRowCount(0);
        
        for (Object[] row : list) {
            String maNV = (String) row[0];
            String hoTen = (String) row[1];
            String tenPB = (String) row[2];
            int ngayCong = (Integer) row[3];
            int nghiPhep = (Integer) row[4];
            
            double dinhMuc = 22.0; // Giả định tháng có 22 ngày công chuẩn
            double khongLuong = dinhMuc - ngayCong - nghiPhep;
            if (khongLuong < 0) khongLuong = 0;
            
            tableModel.addRow(new Object[]{
                maNV, hoTen, tenPB, 
                String.format("%.1f", dinhMuc), 
                String.format("%.1f", (double) ngayCong), 
                String.format("%.1f", (double) nghiPhep), 
                "0.0", 
                String.format("%.1f", khongLuong)
            });
        }
    }
}
