package gui;

import bus.NhanVienBUS;
import model.NhanVien;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableRowSorter;
import java.awt.*;
import model.TaiKhoan;
import utils.AccessPolicy;

public class NhanVienPanel extends JPanel {
    private final NhanVienBUS bus = new NhanVienBUS();
    private final TaiKhoan currentUser;
    private final boolean isAdmin;
    private final JLabel totalCount = new JLabel("0");
    private final JLabel activeCount = new JLabel("0");
    private final JLabel inactiveCount = new JLabel("0");
    private final JLabel loadingStatus = new JLabel("Đang tải dữ liệu...");
    private final DefaultTableModel model = new DefaultTableModel(
            new String[]{"MÃ NV", "HỌ VÀ TÊN", "GIỚI TÍNH", "PHÒNG BAN", "CHỨC VỤ", "HỆ SỐ", "TRẠNG THÁI"}, 0) {
        @Override
        public boolean isCellEditable(int row, int column) {
            return false;
        }
    };
    private final JTable table = new JTable(model);

    public NhanVienPanel(TaiKhoan currentUser) {
        AccessPolicy.validateAccount(currentUser);
        this.currentUser = currentUser;
        this.isAdmin = AccessPolicy.isAdmin(currentUser);
        setLayout(new BorderLayout(0, 14));
        setBackground(GuiStyle.BACKGROUND);
        setBorder(BorderFactory.createEmptyBorder(22, 22, 22, 22));

        JTextField search = new JTextField(28);
        search.putClientProperty("JTextField.placeholderText", "Tìm mã nhân viên, họ tên, phòng ban, chức vụ");
        TableRowSorter<DefaultTableModel> sorter = new TableRowSorter<>(model);
        table.setRowSorter(sorter);
        search.getDocument().addDocumentListener(new javax.swing.event.DocumentListener() {
            private void filter() {
                String text = search.getText().trim();
                sorter.setRowFilter(text.isEmpty() ? null : RowFilter.regexFilter(
                        "(?i)" + java.util.regex.Pattern.quote(text)));
            }
            @Override
            public void insertUpdate(javax.swing.event.DocumentEvent e) { filter(); }
            @Override
            public void removeUpdate(javax.swing.event.DocumentEvent e) { filter(); }
            @Override
            public void changedUpdate(javax.swing.event.DocumentEvent e) { filter(); }
        });

        JButton add = new JButton("Thêm");
        JButton edit = new JButton("Sửa");
        JButton disable = new JButton("Cho nghỉ việc");
        JPanel toolbar = new JPanel(new FlowLayout(FlowLayout.RIGHT, 8, 0));
        toolbar.setOpaque(false);
        if (isAdmin) {
            GuiStyle.styleButton(add, GuiStyle.GREEN);
            GuiStyle.styleButton(edit, GuiStyle.BLUE);
            GuiStyle.styleButton(disable, GuiStyle.RED);
            search.setPreferredSize(new Dimension(300, 36));
            toolbar.add(search);
            toolbar.add(add);
            toolbar.add(edit);
            toolbar.add(disable);
        }

        JPanel heading = new JPanel(new BorderLayout(8, 12));
        heading.setOpaque(false);
        JPanel titleBlock = new JPanel();
        titleBlock.setOpaque(false);
        titleBlock.setLayout(new BoxLayout(titleBlock, BoxLayout.Y_AXIS));
        JLabel title = new JLabel(isAdmin ? "Danh sách nhân viên" : "Hồ sơ của tôi");
        title.setFont(new Font("Segoe UI", Font.BOLD, 23));
        title.setForeground(GuiStyle.TEXT);
        JLabel subtitle = new JLabel(isAdmin
                ? "Quản lý hồ sơ, phòng ban và trạng thái làm việc"
                : "Thông tin nhân sự được liên kết với tài khoản đang đăng nhập");
        subtitle.setFont(new Font("Segoe UI", Font.PLAIN, 12));
        subtitle.setForeground(GuiStyle.MUTED);
        titleBlock.add(title);
        titleBlock.add(Box.createVerticalStrut(3));
        titleBlock.add(subtitle);
        heading.add(titleBlock, BorderLayout.NORTH);

        JPanel metrics = new JPanel(new GridLayout(1, 3, 12, 0));
        metrics.setOpaque(false);
        if (isAdmin) {
            metrics.add(GuiStyle.metricCard("Tổng hồ sơ", totalCount, "Toàn bộ nhân sự"));
            metrics.add(GuiStyle.metricCard("Đang làm việc", activeCount, "Nhân viên hiện tại"));
            metrics.add(GuiStyle.metricCard("Nghỉ việc", inactiveCount, "Hồ sơ được lưu trữ"));
        } else {
            metrics.setLayout(new GridLayout(1, 1));
            metrics.add(GuiStyle.metricCard("Hồ sơ của tôi", totalCount, "Thông tin nhân sự cá nhân"));
        }
        heading.add(metrics, BorderLayout.CENTER);
        heading.add(toolbar, BorderLayout.SOUTH);
        add(heading, BorderLayout.NORTH);

        table.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        GuiStyle.styleTable(table);
        JScrollPane scrollPane = new JScrollPane(table);
        scrollPane.setBorder(BorderFactory.createLineBorder(new Color(229, 234, 241)));
        scrollPane.getViewport().setBackground(Color.WHITE);
        JPanel tableContainer = new JPanel(new BorderLayout(0, 6));
        tableContainer.setOpaque(false);
        loadingStatus.setFont(new Font("Segoe UI", Font.PLAIN, 12));
        loadingStatus.setForeground(GuiStyle.MUTED);
        tableContainer.add(loadingStatus, BorderLayout.NORTH);
        tableContainer.add(scrollPane, BorderLayout.CENTER);
        add(tableContainer, BorderLayout.CENTER);
        add.addActionListener(e -> openDialog(null));
        edit.addActionListener(e -> {
            NhanVien selected = selectedEmployee();
            if (selected != null) openDialog(selected);
        });
        disable.addActionListener(e -> terminateEmployee());
        loadData();
    }

    private void openDialog(NhanVien employee) {
        try {
            JFrame parent = (JFrame) SwingUtilities.getWindowAncestor(this);
            NhanVienDialog dialog = new NhanVienDialog(parent, employee);
            dialog.setVisible(true);
            if (dialog.isSuccess()) loadData();
        } catch (IllegalArgumentException | IllegalStateException ex) {
            showError(ex);
        }
    }

    private NhanVien selectedEmployee() {
        int selected = table.getSelectedRow();
        if (selected < 0) {
            JOptionPane.showMessageDialog(this, "Vui lòng chọn nhân viên.");
            return null;
        }
        int row = table.convertRowIndexToModel(selected);
        String employeeId = (String) model.getValueAt(row, 0);
        for (NhanVien employee : bus.getAll()) {
            if (employee.getMaNV().equals(employeeId)) return employee;
        }
        JOptionPane.showMessageDialog(this, "Không tìm thấy hồ sơ nhân viên được chọn.",
                "Lỗi dữ liệu", JOptionPane.ERROR_MESSAGE);
        return null;
    }

    private void terminateEmployee() {
        NhanVien employee = selectedEmployee();
        if (employee == null) return;
        if ("Nghỉ việc".equals(employee.getTrangThai())) {
            JOptionPane.showMessageDialog(this, "Nhân viên đã ở trạng thái nghỉ việc.");
            return;
        }
        int answer = JOptionPane.showConfirmDialog(this,
                "Chuyển " + employee.getHoTen() + " sang trạng thái nghỉ việc? Hồ sơ và lịch sử sẽ được giữ lại.",
                "Xác nhận", JOptionPane.YES_NO_OPTION);
        if (answer == JOptionPane.YES_OPTION) {
            try {
                bus.delete(employee.getMaNV());
                loadData();
            } catch (IllegalArgumentException | IllegalStateException ex) {
                showError(ex);
            }
        }
    }

    private void loadData() {
        loadingStatus.setText("Đang tải dữ liệu...");
        new SwingWorker<java.util.List<NhanVien>, Void>() {
            @Override
            protected java.util.List<NhanVien> doInBackground() {
                if (isAdmin) {
                    return bus.getAll();
                }
                NhanVien employee = bus.getById(AccessPolicy.requireEmployeeId(currentUser));
                return employee == null
                        ? java.util.Collections.emptyList()
                        : java.util.Collections.singletonList(employee);
            }

            @Override
            protected void done() {
                try {
                    java.util.List<NhanVien> employees = get();
                    model.setRowCount(0);
                    int active = 0;
                    int inactive = 0;
                    for (NhanVien employee : employees) {
                        if ("Đang làm việc".equals(employee.getTrangThai())) {
                            active++;
                        } else {
                            inactive++;
                        }
                        model.addRow(new Object[]{
                                employee.getMaNV(), employee.getHoTen(), employee.getGioiTinh(),
                                employee.getTenPB() == null ? "Chưa xếp phòng" : employee.getTenPB(),
                                employee.getTenCV() == null ? "Chưa xếp chức vụ" : employee.getTenCV(),
                                employee.getHeSoLuong(), employee.getTrangThai()
                        });
                    }
                    totalCount.setText(Integer.toString(employees.size()));
                    activeCount.setText(Integer.toString(active));
                    inactiveCount.setText(Integer.toString(inactive));
                    loadingStatus.setText("Đã tải " + employees.size() + " hồ sơ.");
                } catch (InterruptedException ex) {
                    Thread.currentThread().interrupt();
                    loadingStatus.setText("Đã hủy tải dữ liệu.");
                    showError(new IllegalStateException("Tải dữ liệu nhân viên đã bị gián đoạn.", ex));
                } catch (java.util.concurrent.ExecutionException ex) {
                    Throwable cause = ex.getCause();
                    loadingStatus.setText("Không thể tải dữ liệu. Dữ liệu đang hiển thị được giữ nguyên.");
                    showError(cause instanceof RuntimeException
                            ? (RuntimeException) cause
                            : new IllegalStateException("Không thể tải dữ liệu nhân viên.", cause));
                }
            }
        }.execute();
    }

    private void showError(RuntimeException ex) {
        JOptionPane.showMessageDialog(this, ex.getMessage(), "Lỗi", JOptionPane.ERROR_MESSAGE);
    }
}
