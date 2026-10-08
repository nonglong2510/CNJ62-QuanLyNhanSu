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

public class TinhLuongPanel extends JPanel {
    private final BangLuongBUS bus = new BangLuongBUS();
    private final JSpinner month = new JSpinner(new SpinnerNumberModel(LocalDate.now().getMonthValue(), 1, 12, 1));
    private final JSpinner year = new JSpinner(new SpinnerNumberModel(LocalDate.now().getYear(), 2000, 2100, 1));
    private final JLabel periodLabel = new JLabel();
    private final JLabel totalLabel = new JLabel("Tổng thực lĩnh: 0 đ");
    private final DefaultTableModel model = new DefaultTableModel(new String[]{
            "MÃ NV", "HỌ VÀ TÊN", "PHÒNG BAN", "NGÀY CÔNG", "LƯƠNG CƠ BẢN",
            "PHỤ CẤP CHỨC VỤ", "THƯỞNG", "KHẤU TRỪ", "THỰC LĨNH"
    }, 0) {
        @Override
        public boolean isCellEditable(int row, int column) {
            return false;
        }
    };
    private final JTable table = new JTable(model);

    public TinhLuongPanel() {
        setLayout(new BorderLayout(0, 14));
        setBackground(GuiStyle.BACKGROUND);
        setBorder(BorderFactory.createEmptyBorder(22, 22, 22, 22));

        JPanel header = new JPanel(new BorderLayout(8, 12));
        header.setOpaque(false);
        JLabel title = new JLabel("Tính lương hàng tháng");
        title.setFont(new Font("Segoe UI", Font.BOLD, 23));
        title.setForeground(GuiStyle.TEXT);
        JPanel controls = new JPanel(new FlowLayout(FlowLayout.LEFT, 8, 0));
        controls.setOpaque(false);
        JButton calculate = new JButton("Tính / cập nhật kỳ lương");
        JButton reload = new JButton("Tải bảng lương đã lưu");
        JButton export = new JButton("Xuất CSV");
        JButton print = new JButton("In");
        GuiStyle.styleButton(calculate, GuiStyle.GREEN);
        GuiStyle.styleButton(reload, GuiStyle.BLUE);
        GuiStyle.styleButton(export, new Color(102, 119, 140));
        GuiStyle.styleButton(print, new Color(102, 119, 140));
        controls.add(new JLabel("Tháng:"));
        controls.add(month);
        controls.add(new JLabel("Năm:"));
        controls.add(year);
        controls.add(calculate);
        controls.add(reload);
        controls.add(export);
        controls.add(print);
        header.add(controls, BorderLayout.NORTH);
        header.add(title, BorderLayout.CENTER);
        JPanel info = new JPanel(new BorderLayout(10, 0));
        info.setOpaque(false);
        periodLabel.setFont(new Font("Segoe UI", Font.PLAIN, 12));
        periodLabel.setForeground(GuiStyle.MUTED);
        totalLabel.setFont(new Font("Segoe UI", Font.BOLD, 16));
        totalLabel.setForeground(GuiStyle.BLUE);
        info.add(periodLabel, BorderLayout.WEST);
        info.add(totalLabel, BorderLayout.EAST);
        header.add(info, BorderLayout.SOUTH);
        add(header, BorderLayout.NORTH);

        GuiStyle.styleTable(table);
        JScrollPane scrollPane = new JScrollPane(table);
        scrollPane.setBorder(BorderFactory.createLineBorder(new Color(229, 234, 241)));
        scrollPane.getViewport().setBackground(Color.WHITE);
        add(scrollPane, BorderLayout.CENTER);
        calculate.addActionListener(e -> calculate());
        reload.addActionListener(e -> loadSaved());
        export.addActionListener(e -> exportCsv());
        print.addActionListener(e -> printTable());
        updatePeriodLabel();
        loadSaved();
    }

    private int selectedMonth() {
        return (Integer) month.getValue();
    }

    private int selectedYear() {
        return (Integer) year.getValue();
    }

    private void calculate() {
        int selectedMonth = selectedMonth();
        int selectedYear = selectedYear();
        int answer = JOptionPane.showConfirmDialog(this,
                "Tính lại sẽ ghi đè bảng lương đã lưu của kỳ " + selectedMonth + "/" + selectedYear + ". Tiếp tục?",
                "Xác nhận tính lương", JOptionPane.YES_NO_OPTION, JOptionPane.WARNING_MESSAGE);
        if (answer != JOptionPane.YES_OPTION) return;
        try {
            bus.calculateSalaryForMonth(selectedMonth, selectedYear);
            loadSaved();
            JOptionPane.showMessageDialog(this, "Đã tính và lưu lương kỳ " + selectedMonth + "/" + selectedYear + ".");
        } catch (RuntimeException ex) {
            showError(ex);
        }
    }

    private void loadSaved() {
        int selectedMonth = selectedMonth();
        int selectedYear = selectedYear();
        updatePeriodLabel();
        try {
            List<BangLuong> rows = bus.getByMonth(selectedMonth, selectedYear);
            model.setRowCount(0);
            double total = 0;
            for (BangLuong salary : rows) {
                total += salary.getThucLanh();
                model.addRow(new Object[]{
                        salary.getMaNV(), salary.getHoTen(), salary.getTenPB(), salary.getSoNgayCong(),
                        money(salary.getLuongCoBan()), money(salary.getTongPhuCap()),
                        money(salary.getTienThuong()), money(salary.getTienPhat()), money(salary.getThucLanh())
                });
            }
            totalLabel.setText("Tổng thực lĩnh: " + money(total));
        } catch (RuntimeException ex) {
            model.setRowCount(0);
            totalLabel.setText("Không tải được dữ liệu");
            showError(ex);
        }
    }

    private void updatePeriodLabel() {
        periodLabel.setText("Bảng lương tháng " + selectedMonth() + "/" + selectedYear()
                + "  |  Lương ngày = 5.000.000 × hệ số / 22");
    }

    private String money(double amount) {
        return NumberFormat.getNumberInstance(Locale.forLanguageTag("vi-VN")).format(Math.round(amount)) + " đ";
    }

    private void exportCsv() {
        JFileChooser chooser = new JFileChooser();
        chooser.setSelectedFile(new File("bang-luong-" + selectedMonth() + "-" + selectedYear() + ".csv"));
        if (chooser.showSaveDialog(this) != JFileChooser.APPROVE_OPTION) return;
        File file = chooser.getSelectedFile();
        if (!file.getName().toLowerCase(Locale.ROOT).endsWith(".csv")) {
            file = new File(file.getParentFile(), file.getName() + ".csv");
        }
        try (BufferedWriter writer = Files.newBufferedWriter(file.toPath(), StandardCharsets.UTF_8)) {
            writer.write('\ufeff');
            writeCsvRow(writer, headers());
            for (int row = 0; row < model.getRowCount(); row++) {
                Object[] values = new Object[model.getColumnCount()];
                for (int column = 0; column < values.length; column++) {
                    values[column] = model.getValueAt(row, column);
                }
                writeCsvRow(writer, values);
            }
            JOptionPane.showMessageDialog(this, "Đã xuất: " + file.getAbsolutePath());
        } catch (Exception ex) {
            showError(new IllegalStateException("Không thể xuất bảng lương.", ex));
        }
    }

    private Object[] headers() {
        Object[] values = new Object[model.getColumnCount()];
        for (int column = 0; column < values.length; column++) values[column] = model.getColumnName(column);
        return values;
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
                    new java.text.MessageFormat("BẢNG LƯƠNG THÁNG " + selectedMonth() + "/" + selectedYear()),
                    new java.text.MessageFormat("Trang {0}"));
        } catch (java.awt.print.PrinterException ex) {
            showError(new IllegalStateException("Không thể in bảng lương.", ex));
        }
    }

    private void showError(RuntimeException ex) {
        JOptionPane.showMessageDialog(this, ex.getMessage(), "Lỗi", JOptionPane.ERROR_MESSAGE);
    }
}
