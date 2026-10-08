package gui;

import bus.ChamCongBUS;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableRowSorter;
import java.awt.*;
import java.io.BufferedWriter;
import java.io.File;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.time.LocalDate;
import java.util.List;

public class TongHopCongPanel extends JPanel {
    private final ChamCongBUS bus = new ChamCongBUS();
    private final JSpinner month = new JSpinner(new SpinnerNumberModel(LocalDate.now().getMonthValue(), 1, 12, 1));
    private final JSpinner year = new JSpinner(new SpinnerNumberModel(LocalDate.now().getYear(), 2000, 2100, 1));
    private final JTextField search = new JTextField(20);
    private final DefaultTableModel model = new DefaultTableModel(new String[]{
            "MÃ NV", "HỌ VÀ TÊN", "PHÒNG BAN", "CÔNG CHUẨN", "CÔNG THỰC TẾ",
            "NGHỈ PHÉP", "CÔNG CÒN LẠI (MỐC 22)"
    }, 0) {
        @Override
        public boolean isCellEditable(int row, int column) {
            return false;
        }
    };
    private final JTable table = new JTable(model);
    private final TableRowSorter<DefaultTableModel> sorter = new TableRowSorter<>(model);

    public TongHopCongPanel() {
        setLayout(new BorderLayout(0, 14));
        setBackground(GuiStyle.BACKGROUND);
        setBorder(BorderFactory.createEmptyBorder(22, 22, 22, 22));

        JButton load = new JButton("Xem kỳ");
        JButton export = new JButton("Xuất CSV");
        JButton print = new JButton("In");
        GuiStyle.styleButton(load, GuiStyle.BLUE);
        GuiStyle.styleButton(export, new Color(102, 119, 140));
        GuiStyle.styleButton(print, new Color(102, 119, 140));
        JPanel toolbar = new JPanel(new FlowLayout(FlowLayout.RIGHT, 8, 0));
        toolbar.setOpaque(false);
        search.putClientProperty("JTextField.placeholderText", "Tìm mã, tên hoặc phòng ban");
        search.setPreferredSize(new Dimension(220, 36));
        toolbar.add(search);
        toolbar.add(new JLabel("Tháng:"));
        toolbar.add(month);
        toolbar.add(new JLabel("Năm:"));
        toolbar.add(year);
        toolbar.add(load);
        toolbar.add(export);
        toolbar.add(print);
        JLabel title = new JLabel("Tổng hợp chấm công");
        title.setFont(new Font("Segoe UI", Font.BOLD, 23));
        title.setForeground(GuiStyle.TEXT);
        JLabel subtitle = new JLabel("Theo dõi ngày làm, nghỉ phép và công còn lại theo kỳ");
        subtitle.setFont(new Font("Segoe UI", Font.PLAIN, 12));
        subtitle.setForeground(GuiStyle.MUTED);
        JPanel titleBlock = new JPanel();
        titleBlock.setOpaque(false);
        titleBlock.setLayout(new BoxLayout(titleBlock, BoxLayout.Y_AXIS));
        titleBlock.add(title);
        titleBlock.add(Box.createVerticalStrut(3));
        titleBlock.add(subtitle);
        JPanel heading = new JPanel(new BorderLayout(10, 12));
        heading.setOpaque(false);
        heading.add(titleBlock, BorderLayout.NORTH);
        heading.add(GuiStyle.metricCard("Công chuẩn", new JLabel("22"), "Ngày công tham chiếu mỗi tháng"), BorderLayout.CENTER);
        heading.add(toolbar, BorderLayout.SOUTH);
        add(heading, BorderLayout.NORTH);

        GuiStyle.styleTable(table);
        table.setRowSorter(sorter);
        JScrollPane scrollPane = new JScrollPane(table);
        scrollPane.setBorder(BorderFactory.createLineBorder(new Color(229, 234, 241)));
        scrollPane.getViewport().setBackground(Color.WHITE);
        add(scrollPane, BorderLayout.CENTER);
        load.addActionListener(e -> loadData());
        export.addActionListener(e -> exportCsv());
        print.addActionListener(e -> printTable());
        search.getDocument().addDocumentListener(new javax.swing.event.DocumentListener() {
            @Override
            public void insertUpdate(javax.swing.event.DocumentEvent e) {
                applySearch();
            }
            @Override
            public void removeUpdate(javax.swing.event.DocumentEvent e) {
                applySearch();
            }
            @Override
            public void changedUpdate(javax.swing.event.DocumentEvent e) {
                applySearch();
            }
        });
        loadData();
    }

    private void loadData() {
        try {
            int selectedMonth = (Integer) month.getValue();
            int selectedYear = (Integer) year.getValue();
            List<Object[]> rows = bus.getTongHopCongThang(selectedMonth, selectedYear);
            int standardDays = 22;
            model.setRowCount(0);
            for (Object[] row : rows) {
                int worked = ((Number) row[3]).intValue();
                int leave = ((Number) row[4]).intValue();
                model.addRow(new Object[]{
                        row[0], row[1], row[2], standardDays, worked, leave,
                        Math.max(0, standardDays - worked - leave)
                });
            }
            applySearch();
        } catch (IllegalStateException | ClassCastException ex) {
            showError(ex);
        }
    }

    private void exportCsv() {
        JFileChooser chooser = new JFileChooser();
        chooser.setSelectedFile(new File("tong-hop-cong-" + month.getValue() + "-" + year.getValue() + ".csv"));
        if (chooser.showSaveDialog(this) != JFileChooser.APPROVE_OPTION) return;
        File file = chooser.getSelectedFile();
        if (!file.getName().toLowerCase().endsWith(".csv")) {
            file = new File(file.getParentFile(), file.getName() + ".csv");
        }
        try (BufferedWriter writer = Files.newBufferedWriter(file.toPath(), StandardCharsets.UTF_8)) {
            writer.write('\ufeff');
            writeCsvRow(writer, columnNames());
            for (int viewRow = 0; viewRow < table.getRowCount(); viewRow++) {
                int row = table.convertRowIndexToModel(viewRow);
                Object[] values = new Object[model.getColumnCount()];
                for (int column = 0; column < model.getColumnCount(); column++) {
                    values[column] = model.getValueAt(row, column);
                }
                writeCsvRow(writer, values);
            }
            JOptionPane.showMessageDialog(this, "Đã xuất: " + file.getAbsolutePath());
        } catch (Exception ex) {
            showError(new IllegalStateException("Không thể xuất file CSV.", ex));
        }
    }

    private void applySearch() {
        String query = search.getText().trim();
        sorter.setRowFilter(query.isEmpty() ? null
                : RowFilter.regexFilter("(?i)" + java.util.regex.Pattern.quote(query), 0, 1, 2));
    }

    private Object[] columnNames() {
        Object[] headers = new Object[model.getColumnCount()];
        for (int column = 0; column < headers.length; column++) {
            headers[column] = model.getColumnName(column);
        }
        return headers;
    }

    private void writeCsvRow(BufferedWriter writer, Object[] values) throws java.io.IOException {
        for (int column = 0; column < values.length; column++) {
            if (column > 0) writer.write(';');
            String text = values[column] == null ? "" : values[column].toString();
            writer.write('"');
            writer.write(text.replace("\"", "\"\""));
            writer.write('"');
        }
        writer.newLine();
    }

    private void printTable() {
        try {
            table.print(JTable.PrintMode.FIT_WIDTH);
        } catch (java.awt.print.PrinterException ex) {
            showError(new IllegalStateException("Không thể in bảng tổng hợp công.", ex));
        }
    }

    private void showError(RuntimeException ex) {
        JOptionPane.showMessageDialog(this, ex.getMessage(), "Lỗi", JOptionPane.ERROR_MESSAGE);
    }
}
