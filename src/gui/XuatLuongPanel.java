package gui;

import bus.BangLuongBUS;
import model.BangLuong;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.io.BufferedWriter;
import java.io.File;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.text.NumberFormat;
import java.time.LocalDate;
import java.util.List;
import java.util.Locale;
import model.TaiKhoan;
import utils.AccessPolicy;

public class XuatLuongPanel extends JPanel {
    private final BangLuongBUS bus = new BangLuongBUS();
    private final TaiKhoan currentUser;
    private final boolean isAdmin;
    private final JSpinner month = new JSpinner(new SpinnerNumberModel(LocalDate.now().getMonthValue(), 1, 12, 1));
    private final JSpinner year = new JSpinner(new SpinnerNumberModel(LocalDate.now().getYear(), 2000, 2100, 1));
    private final DefaultTableModel model = new DefaultTableModel(
            new String[]{"MÃ NV", "NHÂN VIÊN", "PHÒNG BAN", "NGÀY CÔNG", "THỰC LĨNH"}, 0) {
        @Override
        public boolean isCellEditable(int row, int column) {
            return false;
        }
    };
    private final JTable table = new JTable(model);
    private final JLabel preview = new JLabel("Chọn nhân viên để xem phiếu lương.");
    private List<BangLuong> salaries = java.util.Collections.emptyList();

    public XuatLuongPanel(TaiKhoan currentUser) {
        AccessPolicy.validateAccount(currentUser);
        this.currentUser = currentUser;
        this.isAdmin = AccessPolicy.isAdmin(currentUser);
        setLayout(new BorderLayout(0, 14));
        setBackground(GuiStyle.BACKGROUND);
        setBorder(BorderFactory.createEmptyBorder(22, 22, 22, 22));

        JLabel title = new JLabel(isAdmin ? "Xuất bảng lương & phiếu lương" : "Bảng lương của tôi");
        title.setFont(new Font("Segoe UI", Font.BOLD, 23));
        title.setForeground(GuiStyle.TEXT);
        JLabel subtitle = new JLabel(isAdmin
                ? "Tra cứu, in hoặc xuất bảng lương theo kỳ"
                : "Tra cứu phiếu lương cá nhân theo kỳ");
        subtitle.setFont(new Font("Segoe UI", Font.PLAIN, 12));
        subtitle.setForeground(GuiStyle.MUTED);
        JPanel titleBlock = new JPanel();
        titleBlock.setOpaque(false);
        titleBlock.setLayout(new BoxLayout(titleBlock, BoxLayout.Y_AXIS));
        titleBlock.add(title);
        titleBlock.add(Box.createVerticalStrut(3));
        titleBlock.add(subtitle);

        JPanel toolbar = new JPanel(new FlowLayout(FlowLayout.LEFT, 8, 0));
        toolbar.setOpaque(false);
        toolbar.add(new JLabel("Kỳ lương tháng:"));
        toolbar.add(month);
        toolbar.add(new JLabel("Năm:"));
        toolbar.add(year);
        JButton refresh = new JButton("Tải bảng lương");
        JButton export = new JButton("Xuất CSV");
        JButton print = new JButton("In bảng");
        JButton email = new JButton("Gửi email");
        GuiStyle.styleButton(refresh, GuiStyle.BLUE);
        GuiStyle.styleButton(export, new Color(102, 119, 140));
        GuiStyle.styleButton(print, new Color(102, 119, 140));
        GuiStyle.styleButton(email, new Color(102, 119, 140));
        email.setEnabled(false);
        email.setForeground(new Color(235, 238, 242));
        email.setBackground(new Color(160, 169, 181));
        email.setToolTipText("Chưa tích hợp cấu hình SMTP và trạng thái gửi email.");
        toolbar.add(refresh);
        toolbar.add(export);
        toolbar.add(print);
        toolbar.add(email);
        JPanel header = new JPanel(new BorderLayout(8, 10));
        header.setOpaque(false);
        header.add(titleBlock, BorderLayout.NORTH);
        header.add(toolbar, BorderLayout.SOUTH);
        add(header, BorderLayout.NORTH);

        GuiStyle.styleTable(table);
        JPanel previewPanel = new JPanel(new BorderLayout());
        previewPanel.setBackground(Color.WHITE);
        previewPanel.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(new Color(229, 234, 241)),
                BorderFactory.createTitledBorder("Phiếu lương")));
        preview.setVerticalAlignment(SwingConstants.TOP);
        preview.setBorder(BorderFactory.createEmptyBorder(10, 12, 10, 12));
        preview.setFont(new Font("Segoe UI", Font.PLAIN, 13));
        previewPanel.add(preview, BorderLayout.CENTER);
        JSplitPane split = new JSplitPane(JSplitPane.HORIZONTAL_SPLIT,
                new JScrollPane(table), previewPanel);
        split.setResizeWeight(0.65);
        split.setBorder(null);
        split.setOpaque(false);
        split.setDividerSize(10);
        split.setDividerLocation(0.62);
        JScrollPane tableScroll = (JScrollPane) split.getLeftComponent();
        tableScroll.setBorder(BorderFactory.createLineBorder(new Color(229, 234, 241)));
        tableScroll.getViewport().setBackground(Color.WHITE);
        add(split, BorderLayout.CENTER);

        refresh.addActionListener(e -> loadData());
        export.addActionListener(e -> exportCsv());
        print.addActionListener(e -> printTable());
        table.getSelectionModel().addListSelectionListener(e -> {
            if (!e.getValueIsAdjusting()) showSelectedPayslip();
        });
        loadData();
    }

    private void loadData() {
        try {
            salaries = isAdmin
                    ? bus.getByMonth((Integer) month.getValue(), (Integer) year.getValue())
                    : bus.getByMonthForEmployee((Integer) month.getValue(), (Integer) year.getValue(),
                            AccessPolicy.requireEmployeeId(currentUser));
            model.setRowCount(0);
            for (BangLuong salary : salaries) {
                model.addRow(new Object[]{salary.getMaNV(), salary.getHoTen(), salary.getTenPB(),
                        salary.getSoNgayCong(), money(salary.getThucLanh())});
            }
            preview.setText(salaries.isEmpty()
                    ? (isAdmin ? "Chưa có bảng lương đã lưu cho kỳ này. Hãy tính lương ở màn hình Tính lương."
                            : "Chưa có phiếu lương cá nhân được lưu cho kỳ này.")
                    : "Chọn nhân viên trong bảng để xem phiếu lương.");
            if (!salaries.isEmpty()) table.setRowSelectionInterval(0, 0);
        } catch (RuntimeException ex) {
            salaries = java.util.Collections.emptyList();
            model.setRowCount(0);
            preview.setText("Không tải được bảng lương: " + ex.getMessage());
            showError(ex);
        }
    }

    private void showSelectedPayslip() {
        int selected = table.getSelectedRow();
        if (selected < 0 || selected >= salaries.size()) return;
        BangLuong salary = salaries.get(table.convertRowIndexToModel(selected));
        String html = "<html><h2>PHIẾU LƯƠNG THÁNG " + salary.getThang() + "/" + salary.getNam() + "</h2>"
                + "<p><b>Nhân viên:</b> " + escape(salary.getHoTen()) + " (" + escape(salary.getMaNV()) + ")</p>"
                + "<p><b>Phòng ban:</b> " + escape(salary.getTenPB()) + "</p>"
                + "<p><b>Số ngày công:</b> " + salary.getSoNgayCong() + "</p><hr>"
                + "<p>Lương theo ngày công: " + money(salary.getLuongCoBan()) + "</p>"
                + "<p>Phụ cấp chức vụ theo ngày công: " + money(salary.getTongPhuCap()) + "</p>"
                + "<p>Thưởng: " + money(salary.getTienThuong()) + "</p>"
                + "<p>Khấu trừ (demo): " + money(salary.getTienPhat()) + "</p>"
                + "<h3>THỰC LĨNH: " + money(salary.getThucLanh()) + "</h3>"
                + "<p><i>Chưa tính OT, thuế TNCN hoặc bảo hiểm.</i></p></html>";
        preview.setText(html);
    }

    private String escape(String text) {
        if (text == null) return "";
        return text.replace("&", "&amp;").replace("<", "&lt;").replace(">", "&gt;");
    }

    private String money(double amount) {
        return NumberFormat.getNumberInstance(Locale.forLanguageTag("vi-VN")).format(Math.round(amount)) + " đ";
    }

    private void exportCsv() {
        JFileChooser chooser = new JFileChooser();
        chooser.setSelectedFile(new File("phat-hanh-luong-" + month.getValue() + "-" + year.getValue() + ".csv"));
        if (chooser.showSaveDialog(this) != JFileChooser.APPROVE_OPTION) return;
        File file = chooser.getSelectedFile();
        if (!file.getName().toLowerCase(Locale.ROOT).endsWith(".csv")) {
            file = new File(file.getParentFile(), file.getName() + ".csv");
        }
        try (BufferedWriter writer = Files.newBufferedWriter(file.toPath(), StandardCharsets.UTF_8)) {
            writer.write('\ufeff');
            writeCsvRow(writer, new Object[]{"MÃ NV", "NHÂN VIÊN", "PHÒNG BAN", "NGÀY CÔNG",
                    "LƯƠNG THEO CÔNG", "PHỤ CẤP", "THƯỞNG", "KHẤU TRỪ", "THỰC LĨNH"});
            for (BangLuong salary : salaries) {
                writeCsvRow(writer, new Object[]{salary.getMaNV(), salary.getHoTen(), salary.getTenPB(),
                        salary.getSoNgayCong(), money(salary.getLuongCoBan()), money(salary.getTongPhuCap()),
                        money(salary.getTienThuong()), money(salary.getTienPhat()), money(salary.getThucLanh())});
            }
            JOptionPane.showMessageDialog(this, "Đã xuất: " + file.getAbsolutePath());
        } catch (Exception ex) {
            showError(new IllegalStateException("Không thể xuất danh sách phát hành lương.", ex));
        }
    }

    private void writeCsvRow(BufferedWriter writer, Object[] values) throws java.io.IOException {
        for (int column = 0; column < values.length; column++) {
            if (column > 0) writer.write(';');
            writer.write('"');
            writer.write(String.valueOf(values[column] == null ? "" : values[column]).replace("\"", "\"\""));
            writer.write('"');
        }
        writer.newLine();
    }

    private void printTable() {
        try {
            table.print(JTable.PrintMode.FIT_WIDTH,
                    new java.text.MessageFormat("BẢNG LƯƠNG THÁNG " + month.getValue() + "/" + year.getValue()),
                    new java.text.MessageFormat("Trang {0}"));
        } catch (java.awt.print.PrinterException ex) {
            showError(new IllegalStateException("Không thể in danh sách lương.", ex));
        }
    }

    private void showError(RuntimeException ex) {
        JOptionPane.showMessageDialog(this, ex.getMessage(), "Lỗi", JOptionPane.ERROR_MESSAGE);
    }
}
