package gui;

import bus.DonXinPhepBUS;
import bus.NhanVienBUS;
import model.DonXinPhep;
import model.NhanVien;
import model.TaiKhoan;
import utils.AccessPolicy;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableRowSorter;
import java.awt.*;
import java.sql.Date;
import java.time.LocalDate;

public class DonXinPhepPanel extends JPanel {
    private final DonXinPhepBUS bus = new DonXinPhepBUS();
    private final TaiKhoan currentUser;
    private final boolean isAdmin;
    private final JLabel totalCount = new JLabel("0");
    private final JLabel pendingCount = new JLabel("0");
    private final JLabel approvedCount = new JLabel("0");
    private final JLabel rejectedCount = new JLabel("0");
    private final JTextField search = new JTextField(22);
    private final JComboBox<String> statusFilter = new JComboBox<>(
            new String[]{"Tất cả trạng thái", "Chờ phê duyệt", "Đã duyệt", "Từ chối"});
    private final DefaultTableModel model = new DefaultTableModel(
            new String[]{"MÃ ĐƠN", "NHÂN VIÊN", "TỪ NGÀY", "ĐẾN NGÀY", "LÝ DO", "TRẠNG THÁI"}, 0) {
        @Override
        public boolean isCellEditable(int row, int column) {
            return false;
        }
    };
    private final JTable table = new JTable(model);
    private final TableRowSorter<DefaultTableModel> sorter = new TableRowSorter<>(model);

    public DonXinPhepPanel(TaiKhoan currentUser) {
        AccessPolicy.validateAccount(currentUser);
        this.currentUser = currentUser;
        this.isAdmin = AccessPolicy.isAdmin(currentUser);
        setLayout(new BorderLayout(0, 14));
        setBackground(GuiStyle.BACKGROUND);
        setBorder(BorderFactory.createEmptyBorder(22, 22, 22, 22));

        JButton create = new JButton("Tạo đơn");
        JButton approve = new JButton("Duyệt");
        JButton reject = new JButton("Từ chối");
        GuiStyle.styleButton(create, GuiStyle.GREEN);
        GuiStyle.styleButton(approve, GuiStyle.BLUE);
        GuiStyle.styleButton(reject, GuiStyle.RED);
        JPanel toolbar = new JPanel(new FlowLayout(FlowLayout.RIGHT, 8, 0));
        toolbar.setOpaque(false);
        search.putClientProperty("JTextField.placeholderText", "Tìm mã đơn, nhân viên hoặc lý do");
        search.setPreferredSize(new Dimension(260, 36));
        toolbar.add(search);
        toolbar.add(statusFilter);
        if (isAdmin) {
            toolbar.add(approve);
            toolbar.add(reject);
        }

        JPanel titleRow = new JPanel(new BorderLayout(12, 0));
        titleRow.setOpaque(false);
        JPanel titleBlock = new JPanel();
        titleBlock.setOpaque(false);
        titleBlock.setLayout(new BoxLayout(titleBlock, BoxLayout.Y_AXIS));
        JLabel title = new JLabel(isAdmin ? "Quản lý đơn xin phép & nghỉ lễ" : "Đơn xin phép của tôi");
        title.setFont(new Font("Segoe UI", Font.BOLD, 23));
        title.setForeground(GuiStyle.TEXT);
        JLabel subtitle = new JLabel(isAdmin
                ? "Theo dõi yêu cầu nghỉ phép và xử lý phê duyệt"
                : "Tạo đơn mới và theo dõi trạng thái xử lý");
        subtitle.setFont(new Font("Segoe UI", Font.PLAIN, 12));
        subtitle.setForeground(GuiStyle.MUTED);
        titleBlock.add(title);
        titleBlock.add(Box.createVerticalStrut(3));
        titleBlock.add(subtitle);
        titleRow.add(titleBlock, BorderLayout.WEST);
        GuiStyle.styleButton(create, GuiStyle.GREEN);
        titleRow.add(create, BorderLayout.EAST);

        JPanel metrics = new JPanel(new GridLayout(1, 4, 12, 0));
        metrics.setOpaque(false);
        metrics.add(GuiStyle.metricCard("Tất cả đơn", totalCount, "Tổng số yêu cầu"));
        metrics.add(GuiStyle.metricCard("Chờ phê duyệt", pendingCount, "Cần được xử lý"));
        metrics.add(GuiStyle.metricCard("Đã duyệt", approvedCount, "Yêu cầu được chấp thuận"));
        metrics.add(GuiStyle.metricCard("Từ chối", rejectedCount, "Yêu cầu không được duyệt"));

        JPanel heading = new JPanel(new BorderLayout(0, 12));
        heading.setOpaque(false);
        heading.add(titleRow, BorderLayout.NORTH);
        heading.add(metrics, BorderLayout.CENTER);
        heading.add(toolbar, BorderLayout.SOUTH);
        add(heading, BorderLayout.NORTH);

        table.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        GuiStyle.styleTable(table);
        table.setRowSorter(sorter);
        JScrollPane scrollPane = new JScrollPane(table);
        scrollPane.setBorder(BorderFactory.createLineBorder(new Color(229, 234, 241)));
        scrollPane.getViewport().setBackground(Color.WHITE);
        add(scrollPane, BorderLayout.CENTER);
        create.addActionListener(e -> createRequest());
        approve.addActionListener(e -> updateStatus("Đã duyệt"));
        reject.addActionListener(e -> updateStatus("Từ chối"));
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
        loadData();
    }

    private void createRequest() {
        JComboBox<NhanVien> employee = new JComboBox<>();
        if (isAdmin) {
            for (NhanVien item : new NhanVienBUS().getAll()) {
                if ("Đang làm việc".equals(item.getTrangThai())) employee.addItem(item);
            }
        }
        JTextField start = new JTextField(LocalDate.now().toString());
        JTextField end = new JTextField(LocalDate.now().toString());
        JTextField reason = new JTextField();
        Object[] fields = isAdmin
                ? new Object[]{"Nhân viên:", employee, "Từ ngày (YYYY-MM-DD):", start,
                        "Đến ngày (YYYY-MM-DD):", end, "Lý do:", reason}
                : new Object[]{"Nhân viên:", AccessPolicy.requireEmployeeId(currentUser), "Từ ngày (YYYY-MM-DD):", start,
                        "Đến ngày (YYYY-MM-DD):", end, "Lý do:", reason};
        if (JOptionPane.showConfirmDialog(this, fields, "Tạo đơn xin phép",
                JOptionPane.OK_CANCEL_OPTION) != JOptionPane.OK_OPTION) return;
        try {
            String employeeId;
            if (isAdmin) {
                NhanVien selected = (NhanVien) employee.getSelectedItem();
                if (selected == null) {
                    throw new IllegalArgumentException("Không có nhân viên đang làm việc.");
                }
                employeeId = selected.getMaNV();
            } else {
                employeeId = AccessPolicy.requireEmployeeId(currentUser);
            }
            DonXinPhep request = new DonXinPhep();
            request.setMaNV(employeeId);
            request.setNgayBatDau(Date.valueOf(start.getText().trim()));
            request.setNgayKetThuc(Date.valueOf(end.getText().trim()));
            request.setLyDo(reason.getText().trim());
            request.setTrangThai("Chờ phê duyệt");
            bus.insert(request);
            loadData();
        } catch (IllegalArgumentException | IllegalStateException ex) {
            showError(ex);
        }
    }

    private void updateStatus(String status) {
        if (!isAdmin) {
            throw new IllegalStateException("Chỉ Admin được duyệt hoặc từ chối đơn xin phép.");
        }
        int row = table.getSelectedRow();
        if (row < 0) {
            JOptionPane.showMessageDialog(this, "Vui lòng chọn đơn cần xử lý.");
            return;
        }
        int modelRow = table.convertRowIndexToModel(row);
        int requestId = ((Number) model.getValueAt(modelRow, 0)).intValue();
        String currentStatus = (String) model.getValueAt(modelRow, 5);
        if (!"Chờ phê duyệt".equals(currentStatus)) {
            JOptionPane.showMessageDialog(this, "Đơn này đã được xử lý trước đó.",
                    "Không thể xử lý", JOptionPane.WARNING_MESSAGE);
            return;
        }
        if ("Đã duyệt".equals(status)) {
            int answer = JOptionPane.showConfirmDialog(this,
                    "Duyệt đơn sẽ tự ghi nhận Nghỉ phép cho các ngày từ thứ Hai đến thứ Sáu. "
                            + "Nếu các ngày này đã có chấm công, thao tác sẽ bị từ chối. Tiếp tục?",
                    "Xác nhận duyệt đơn", JOptionPane.YES_NO_OPTION, JOptionPane.QUESTION_MESSAGE);
            if (answer != JOptionPane.YES_OPTION) {
                return;
            }
        }
        try {
            if (!bus.updateStatus(requestId, status)) {
                JOptionPane.showMessageDialog(this, "Đơn không còn tồn tại hoặc đã được xử lý.",
                        "Không thể xử lý", JOptionPane.WARNING_MESSAGE);
            }
            loadData();
        } catch (IllegalArgumentException | IllegalStateException ex) {
            showError(ex);
        }
    }

    private void loadData() {
        try {
            model.setRowCount(0);
            java.util.List<DonXinPhep> requests = isAdmin
                    ? bus.getAll()
                    : bus.getByEmployee(AccessPolicy.requireEmployeeId(currentUser));
            int pending = 0;
            int approved = 0;
            int rejected = 0;
            for (DonXinPhep request : requests) {
                if ("Chờ phê duyệt".equals(request.getTrangThai())) {
                    pending++;
                } else if ("Đã duyệt".equals(request.getTrangThai())) {
                    approved++;
                } else if ("Từ chối".equals(request.getTrangThai())) {
                    rejected++;
                }
                model.addRow(new Object[]{
                        request.getMaDon(), request.getHoTen() + " (" + request.getMaNV() + ")",
                        request.getNgayBatDau(), request.getNgayKetThuc(),
                        request.getLyDo(), request.getTrangThai()
                });
            }
            totalCount.setText(Integer.toString(requests.size()));
            pendingCount.setText(Integer.toString(pending));
            approvedCount.setText(Integer.toString(approved));
            rejectedCount.setText(Integer.toString(rejected));
            applyFilters();
        } catch (IllegalStateException ex) {
            showError(ex);
        }
    }

    private void applyFilters() {
        String query = search.getText().trim();
        String selectedStatus = (String) statusFilter.getSelectedItem();
        java.util.List<RowFilter<DefaultTableModel, Integer>> filters = new java.util.ArrayList<>();
        if (selectedStatus != null && !"Tất cả trạng thái".equals(selectedStatus)) {
            filters.add(RowFilter.regexFilter("^" + java.util.regex.Pattern.quote(selectedStatus) + "$", 5));
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
