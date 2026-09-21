package gui;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;

public class XuatLuongPanel extends JPanel {
    private JTable table;
    private DefaultTableModel tableModel;

    public XuatLuongPanel() {
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
        
        JLabel titleLabel = new JLabel("Trung tâm Xuất Bảng lương & Phát hành Phiếu lương");
        titleLabel.setFont(new Font("Segoe UI", Font.BOLD, 22));
        
        titleActionPanel.add(titleLabel);
        titleActionPanel.add(Box.createHorizontalGlue());
        
        titleActionPanel.add(createOutlineBtn("Xuất file UNC (Bank)"));
        titleActionPanel.add(Box.createHorizontalStrut(10));
        titleActionPanel.add(createOutlineBtn("In hàng loạt (PDF)"));
        titleActionPanel.add(Box.createHorizontalStrut(10));
        titleActionPanel.add(createBtn("Gửi Email hàng loạt", new Color(25, 135, 84)));

        headerPanel.add(titleActionPanel, BorderLayout.NORTH);

        // Summary Cards
        JPanel cardsPanel = new JPanel(new GridLayout(1, 4, 15, 0));
        cardsPanel.setOpaque(false);
        cardsPanel.setBorder(BorderFactory.createEmptyBorder(15, 0, 15, 0));
        cardsPanel.add(createSummaryCard("TRẠNG THÁI PHÁT HÀNH", "1,248 / 1,248", "100% sẵn sàng", true));
        cardsPanel.add(createSummaryCard("ĐÃ PHÁT HÀNH EMAIL", "1,180 / 1,248", "94.5% đã gửi thành công", true));
        cardsPanel.add(createSummaryCard("ĐÃ KÝ XÁC NHẬN ĐIỆN TỬ", "892 / 1,248", "71.5% đã ký nhận", false));
        cardsPanel.add(createSummaryCard("TỔNG ỦY NHIỆM CHI (NET)", "21,385,400,000 đ", "Chờ Kế toán trưởng duyệt", true));
        headerPanel.add(cardsPanel, BorderLayout.CENTER);

        add(headerPanel, BorderLayout.NORTH);

        // ================= 2. CONTENT (2 Columns) =================
        JPanel contentPanel = new JPanel(new GridLayout(1, 2, 20, 0));
        contentPanel.setOpaque(false);

        // Left Column - Table
        JPanel tableContainer = new JPanel(new BorderLayout());
        tableContainer.setOpaque(false);
        
        JPanel filterTabs = new JPanel(new FlowLayout(FlowLayout.LEFT));
        filterTabs.setOpaque(false);
        filterTabs.add(createFilterBtn("Tất cả phòng ban", false));
        filterTabs.add(createFilterBtn("Trạng thái", false));
        tableContainer.add(filterTabs, BorderLayout.NORTH);

        String[] columns = {"MÃ NV", "NHÂN VIÊN", "LƯƠNG NET", "GỬI EMAIL", "KÝ NHẬN"};
        tableModel = new DefaultTableModel(columns, 0);
        table = new JTable(tableModel);
        
        table.setRowHeight(40); 
        table.getTableHeader().setFont(new Font("Segoe UI", Font.BOLD, 11));
        table.getTableHeader().setBackground(Color.WHITE);
        table.setFont(new Font("Segoe UI", Font.PLAIN, 12));
        table.setShowVerticalLines(false);
        table.setGridColor(new Color(230, 230, 230));

        JScrollPane scrollPane = new JScrollPane(table);
        scrollPane.setBorder(BorderFactory.createLineBorder(new Color(230, 230, 230)));
        scrollPane.getViewport().setBackground(Color.WHITE);
        tableContainer.add(scrollPane, BorderLayout.CENTER);

        // Right Column - Payslip Preview
        JPanel previewContainer = new JPanel(new BorderLayout());
        previewContainer.setBackground(Color.WHITE);
        previewContainer.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(new Color(230, 230, 230)),
                BorderFactory.createEmptyBorder(20, 20, 20, 20)
        ));

        JLabel lblPreviewTitle = new JLabel("XEM TRƯỚC PHIẾU LƯƠNG ĐIỆN TỬ");
        lblPreviewTitle.setFont(new Font("Segoe UI", Font.BOLD, 14));
        lblPreviewTitle.setForeground(new Color(13, 110, 253));
        previewContainer.add(lblPreviewTitle, BorderLayout.NORTH);

        String payslipHtml = "<html><body style='font-family:sans-serif; padding:10px;'>"
                + "<h2 style='text-align:center; color:#1e3a8a;'>PHIẾU LƯƠNG THÁNG 05/2025</h2>"
                + "<hr>"
                + "<table width='100%'>"
                + "<tr><td><b>Họ và tên:</b> Trần Quang Minh</td> <td><b>Mã nhân viên:</b> VN-10492</td></tr>"
                + "<tr><td><b>Chức vụ:</b> Senior Tech Lead</td> <td><b>Phòng ban:</b> Kỹ thuật phần mềm</td></tr>"
                + "</table>"
                + "<br>"
                + "<table width='100%' border='1' cellspacing='0' cellpadding='5' style='border-collapse:collapse;'>"
                + "<tr style='background-color:#f0f8ff;'><th>I. CÁC KHOẢN THU NHẬP (GROSS)</th><th style='text-align:right;'>28,500,000</th></tr>"
                + "<tr><td>1. Lương cơ bản</td><td style='text-align:right;'>22,000,000</td></tr>"
                + "<tr><td>2. Phụ cấp ăn trưa & Công tác</td><td style='text-align:right;'>1,100,000</td></tr>"
                + "<tr><td>3. Phụ cấp trách nhiệm</td><td style='text-align:right;'>2,400,000</td></tr>"
                + "<tr><td>4. Thưởng hiệu suất KPI</td><td style='text-align:right;'>3,000,000</td></tr>"
                + "<tr style='background-color:#fff0f5;'><th>II. CÁC KHOẢN GIẢM TRỪ</th><th style='text-align:right;'>- 3,850,000</th></tr>"
                + "<tr><td>1. Bảo hiểm bắt buộc</td><td style='text-align:right;'>2,310,000</td></tr>"
                + "<tr><td>2. Thuế TNCN</td><td style='text-align:right;'>1,540,000</td></tr>"
                + "</table>"
                + "<br><h3 style='text-align:right; color:#10B981;'>THỰC LĨNH (NET PAY): 24,650,000 VNĐ</h3>"
                + "</body></html>";

        JLabel lblPayslip = new JLabel(payslipHtml);
        lblPayslip.setVerticalAlignment(SwingConstants.TOP);
        previewContainer.add(lblPayslip, BorderLayout.CENTER);

        contentPanel.add(tableContainer);
        contentPanel.add(previewContainer);

        add(contentPanel, BorderLayout.CENTER);

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
        tableModel.addRow(new Object[]{"VN-10492", "Trần Quang Minh", "24,650,000", "Đã gửi", "Đã ký"});
        tableModel.addRow(new Object[]{"VN-10518", "Nguyễn Thị Hải Yến", "18,920,000", "Đã gửi", "Chờ ký"});
        tableModel.addRow(new Object[]{"VN-09312", "Lê Hoàng Nam", "32,500,000", "Đã gửi", "Đã ký"});
        tableModel.addRow(new Object[]{"VN-11004", "Đỗ Mai Linh", "15,400,000", "Chưa gửi", "Chưa"});
        tableModel.addRow(new Object[]{"VN-08249", "Phạm Quốc Dũng", "28,110,000", "Đã gửi", "Đã ký"});
    }
}
