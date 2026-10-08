package gui;

import bus.ChamCongBUS;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableRowSorter;
import java.awt.*;
import java.sql.Date;
import java.time.LocalDate;
import java.util.List;

public class DiemDanhPanel extends JPanel {
    private final ChamCongBUS bus = new ChamCongBUS();
    private final JLabel attendanceCount = new JLabel("0");
    private final JTextField dateField = new JTextField(LocalDate.now().toString(), 10);
    private final JTextField search = new JTextField(18);
    private final JComboBox<String> statusFilter = new JComboBox<>(
            new String[]{"Tất cả trạng thái", "Chưa điểm danh", "Đi làm", "Đi trễ", "Nghỉ phép", "Không phép"});
    private final DefaultTableModel model = new DefaultTableModel(
            new String[]{"MÃ NV", "HỌ TÊN", "PHÒNG BAN", "TRẠNG THÁI", "GIỜ OT", "GHI CHÚ"}, 0) {
        @Override
        public boolean isCellEditable(int row, int column) {
            return false;
        }
    };
    private final JTable table = new JTable(model);
    private final TableRowSorter<DefaultTableModel> sorter = new TableRowSorter<>(model);
    private Date currentDate = Date.valueOf(LocalDate.now());

    public DiemDanhPanel() {
        setLayout(new BorderLayout(0, 14));
        setBackground(GuiStyle.BACKGROUND);
        setBorder(BorderFactory.createEmptyBorder(22, 22, 22, 22));

        JButton refresh = new JButton("Tải ngày");
        JButton today = new JButton("Hôm nay");
        JButton update = new JButton("Cập nhật điểm danh");
        GuiStyle.styleButton(refresh, new Color(102, 119, 140));
        GuiStyle.styleButton(today, new Color(102, 119, 140));
        GuiStyle.styleButton(update, GuiStyle.BLUE);
        JPanel toolbar = new JPanel(new FlowLayout(FlowLayout.RIGHT, 8, 0));
        toolbar.setOpaque(false);
        search.putClientProperty("JTextField.placeholderText", "Tìm mã, tên hoặc phòng ban");
        search.setPreferredSize(new Dimension(205, 36));
        dateField.setPreferredSize(new Dimension(130, 36));
        toolbar.add(search);
        toolbar.add(statusFilter);
        toolbar.add(new JLabel("Ngày (YYYY-MM-DD):"));
        toolbar.add(dateField);
        toolbar.add(today);
        toolbar.add(refresh);
        toolbar.add(update);
        JLabel title = new JLabel("Chấm công hàng ngày");
        title.setFont(new Font("Segoe UI", Font.BOLD, 23));
        title.setForeground(GuiStyle.TEXT);
        JLabel subtitle = new JLabel("Ghi nhận trạng thái làm việc và giờ làm thêm của nhân viên");
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
        heading.add(GuiStyle.metricCard("Nhân viên trong ngày", attendanceCount, "Danh sách đang làm việc"), BorderLayout.CENTER);
        heading.add(toolbar, BorderLayout.SOUTH);
        add(heading, BorderLayout.NORTH);

        table.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        GuiStyle.styleTable(table);
        table.setRowSorter(sorter);
        JScrollPane scrollPane = new JScrollPane(table);
        scrollPane.setBorder(BorderFactory.createLineBorder(new Color(229, 234, 241)));
        scrollPane.getViewport().setBackground(Color.WHITE);
        add(scrollPane, BorderLayout.CENTER);
        refresh.addActionListener(e -> loadDate());
        today.addActionListener(e -> {
            dateField.setText(LocalDate.now().toString());
            loadDate();
        });
        update.addActionListener(e -> openUpdateDialog());
        dateField.addActionListener(e -> loadDate());
        search.getDocument().addDocumentListener(new javax.swing.event.DocumentListener() {
            @Override
            public void insertUpdate(javax.swing.event.DocumentEvent e) {
                applyFilters();
            }
            @Override
            public void removeUpdate(javax.swing.event.DocumentEvent e) {
                applyFilters();
            }
            @Override
            public void changedUpdate(javax.swing.event.DocumentEvent e) {
                applyFilters();
            }
        });
        statusFilter.addActionListener(e -> applyFilters());
        loadDataFromDB();
    }

    private void loadDate() {
        try {
            currentDate = Date.valueOf(dateField.getText().trim());
            loadDataFromDB();
        } catch (IllegalArgumentException ex) {
            JOptionPane.showMessageDialog(this, "Ngày phải theo định dạng YYYY-MM-DD.",
                    "Ngày không hợp lệ", JOptionPane.WARNING_MESSAGE);
        }
    }

    private void loadDataFromDB() {
        try {
            List<Object[]> rows = bus.getDiemDanhNgay(currentDate);
            model.setRowCount(0);
            for (Object[] row : rows) model.addRow(row);
            attendanceCount.setText(Integer.toString(rows.size()));
            applyFilters();
        } catch (IllegalStateException ex) {
            showError(ex);
        }
    }

    private void openUpdateDialog() {
        int selected = table.getSelectedRow();
        if (selected < 0) {
            JOptionPane.showMessageDialog(this, "Vui lòng chọn nhân viên.");
            return;
        }
        int modelRow = table.convertRowIndexToModel(selected);
        String employeeId = (String) model.getValueAt(modelRow, 0);
        String employeeName = (String) model.getValueAt(modelRow, 1);
        String currentStatus = (String) model.getValueAt(modelRow, 3);
        double currentOt = ((Number) model.getValueAt(modelRow, 4)).doubleValue();
        String currentNote = (String) model.getValueAt(modelRow, 5);

        JComboBox<String> status = new JComboBox<>(
                new String[]{"-- Chọn trạng thái --", "Đi làm", "Đi trễ", "Nghỉ phép", "Không phép"});
        status.setSelectedItem("Chưa điểm danh".equals(currentStatus)
                ? "-- Chọn trạng thái --" : currentStatus);
        JTextField ot = new JTextField(Double.toString(currentOt));
        JTextField note = new JTextField(currentNote);
        Object[] fields = {"Nhân viên: " + employeeName + " (" + employeeId + ")",
                "Trạng thái:", status, "Giờ làm thêm:", ot, "Ghi chú:", note};
        if (JOptionPane.showConfirmDialog(this, fields, "Cập nhật chấm công",
                JOptionPane.OK_CANCEL_OPTION) != JOptionPane.OK_OPTION) return;

        try {
            String selectedStatus = (String) status.getSelectedItem();
            if (selectedStatus == null || selectedStatus.startsWith("--")) {
                throw new IllegalArgumentException("Vui lòng chọn trạng thái chấm công.");
            }
            double otHours = Double.parseDouble(ot.getText().trim());
            bus.upsertDiemDanh(employeeId, currentDate, selectedStatus, otHours, note.getText());
            loadDataFromDB();
        } catch (IllegalArgumentException | IllegalStateException ex) {
            showError(ex);
        }
    }

    private void applyFilters() {
        String query = search.getText().trim();
        String selectedStatus = (String) statusFilter.getSelectedItem();
        java.util.List<RowFilter<DefaultTableModel, Integer>> filters = new java.util.ArrayList<>();
        if (selectedStatus != null && !"Tất cả trạng thái".equals(selectedStatus)) {
            filters.add(RowFilter.regexFilter("^" + java.util.regex.Pattern.quote(selectedStatus) + "$", 3));
        }
        if (!query.isEmpty()) {
            filters.add(RowFilter.regexFilter("(?i)" + java.util.regex.Pattern.quote(query)));
        }
        sorter.setRowFilter(filters.isEmpty() ? null : RowFilter.andFilter(filters));
    }

    private void showError(RuntimeException ex) {
        JOptionPane.showMessageDialog(this, ex.getMessage(), "Lỗi", JOptionPane.ERROR_MESSAGE);
    }
}
