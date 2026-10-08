package gui;

import bus.ChucVuBUS;
import bus.NhanVienBUS;
import bus.PhongBanBUS;
import model.ChucVu;
import model.NhanVien;
import model.PhongBan;

import javax.swing.*;
import java.awt.*;
import java.sql.Date;
import java.util.List;

public class NhanVienDialog extends JDialog {
    private final JTextField txtMaNV = new JTextField();
    private final JTextField txtHoTen = new JTextField();
    private final JComboBox<String> cbGioiTinh = new JComboBox<>(new String[]{"Nam", "Nữ", "Khác"});
    private final JTextField txtNgaySinh = new JTextField();
    private final JTextField txtDienThoai = new JTextField();
    private final JTextField txtEmail = new JTextField();
    private final JTextField txtDiaChi = new JTextField();
    private final JComboBox<PhongBan> cbPhongBan = new JComboBox<>();
    private final JComboBox<ChucVu> cbChucVu = new JComboBox<>();
    private final JTextField txtHeSoLuong = new JTextField();
    private final JTextField txtNgayVaoLam = new JTextField();
    private final JComboBox<String> cbTrangThai =
            new JComboBox<>(new String[]{"Đang làm việc", "Nghỉ việc"});
    private final NhanVienBUS nhanVienBUS = new NhanVienBUS();
    private final NhanVien original;
    private boolean success;

    public NhanVienDialog(JFrame parent) {
        this(parent, null);
    }

    public NhanVienDialog(JFrame parent, NhanVien original) {
        super(parent, original == null ? "Thêm nhân viên" : "Cập nhật nhân viên", true);
        this.original = original;
        setDefaultCloseOperation(DISPOSE_ON_CLOSE);
        loadReferences();
        buildForm();
        if (original != null) {
            populate(original);
        } else {
            txtNgaySinh.setText("YYYY-MM-DD");
            txtNgayVaoLam.setText(Date.valueOf(java.time.LocalDate.now()).toString());
            txtHeSoLuong.setText("1.0");
        }
        pack();
        setMinimumSize(new Dimension(520, 520));
        setLocationRelativeTo(parent);
    }

    private void loadReferences() {
        List<PhongBan> departments = new PhongBanBUS().getAll();
        for (PhongBan department : departments) {
            cbPhongBan.addItem(department);
        }
        List<ChucVu> positions = new ChucVuBUS().getAll();
        for (ChucVu position : positions) {
            cbChucVu.addItem(position);
        }
    }

    private void buildForm() {
        JPanel fields = new JPanel(new GridLayout(0, 2, 10, 8));
        fields.setBackground(Color.WHITE);
        fields.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(new Color(229, 234, 241)),
                BorderFactory.createEmptyBorder(16, 16, 16, 16)));
        addField(fields, "Mã nhân viên *", txtMaNV);
        addField(fields, "Họ và tên *", txtHoTen);
        addField(fields, "Giới tính", cbGioiTinh);
        addField(fields, "Ngày sinh (YYYY-MM-DD) *", txtNgaySinh);
        addField(fields, "Số điện thoại", txtDienThoai);
        addField(fields, "Email", txtEmail);
        addField(fields, "Địa chỉ", txtDiaChi);
        addField(fields, "Phòng ban", cbPhongBan);
        addField(fields, "Chức vụ", cbChucVu);
        addField(fields, "Hệ số lương *", txtHeSoLuong);
        addField(fields, "Ngày vào làm (YYYY-MM-DD) *", txtNgayVaoLam);
        addField(fields, "Trạng thái", cbTrangThai);

        JButton save = new JButton("Lưu");
        JButton cancel = new JButton("Hủy");
        GuiStyle.styleButton(save, GuiStyle.BLUE);
        GuiStyle.styleButton(cancel, new Color(102, 119, 140));
        JPanel buttons = new JPanel(new FlowLayout(FlowLayout.RIGHT, 8, 8));
        buttons.setBackground(GuiStyle.BACKGROUND);
        buttons.add(cancel);
        buttons.add(save);
        cancel.addActionListener(e -> dispose());
        save.addActionListener(e -> saveEmployee());

        JScrollPane scrollPane = new JScrollPane(fields);
        scrollPane.setBorder(BorderFactory.createEmptyBorder());
        scrollPane.getViewport().setBackground(Color.WHITE);
        add(scrollPane, BorderLayout.CENTER);
        add(buttons, BorderLayout.SOUTH);
    }

    private void addField(JPanel panel, String label, JComponent field) {
        panel.add(new JLabel(label));
        panel.add(field);
    }

    private void populate(NhanVien employee) {
        txtMaNV.setText(employee.getMaNV());
        txtMaNV.setEnabled(false);
        txtHoTen.setText(employee.getHoTen());
        cbGioiTinh.setSelectedItem(employee.getGioiTinh());
        txtNgaySinh.setText(dateText(employee.getNgaySinh()));
        txtDienThoai.setText(emptyIfNull(employee.getSoDienThoai()));
        txtEmail.setText(emptyIfNull(employee.getEmail()));
        txtDiaChi.setText(emptyIfNull(employee.getDiaChi()));
        selectDepartment(employee.getMaPB());
        selectPosition(employee.getMaCV());
        txtHeSoLuong.setText(Double.toString(employee.getHeSoLuong()));
        txtNgayVaoLam.setText(dateText(employee.getNgayVaoLam()));
        cbTrangThai.setSelectedItem(employee.getTrangThai());
    }

    private void selectDepartment(String code) {
        for (int i = 0; i < cbPhongBan.getItemCount(); i++) {
            if (cbPhongBan.getItemAt(i).getMaPB().equals(code)) {
                cbPhongBan.setSelectedIndex(i);
                return;
            }
        }
    }

    private void selectPosition(String code) {
        for (int i = 0; i < cbChucVu.getItemCount(); i++) {
            if (cbChucVu.getItemAt(i).getMaCV().equals(code)) {
                cbChucVu.setSelectedIndex(i);
                return;
            }
        }
    }

    private void saveEmployee() {
        try {
            NhanVien employee = new NhanVien();
            employee.setMaNV(txtMaNV.getText());
            employee.setHoTen(txtHoTen.getText());
            employee.setGioiTinh((String) cbGioiTinh.getSelectedItem());
            employee.setNgaySinh(Date.valueOf(txtNgaySinh.getText().trim()));
            employee.setSoDienThoai(blankToNull(txtDienThoai.getText()));
            employee.setEmail(blankToNull(txtEmail.getText()));
            employee.setDiaChi(blankToNull(txtDiaChi.getText()));
            PhongBan department = (PhongBan) cbPhongBan.getSelectedItem();
            ChucVu position = (ChucVu) cbChucVu.getSelectedItem();
            employee.setMaPB(department == null ? null : department.getMaPB());
            employee.setMaCV(position == null ? null : position.getMaCV());
            employee.setHeSoLuong(Double.parseDouble(txtHeSoLuong.getText().trim()));
            employee.setNgayVaoLam(Date.valueOf(txtNgayVaoLam.getText().trim()));
            employee.setTrangThai((String) cbTrangThai.getSelectedItem());

            boolean saved = original == null
                    ? nhanVienBUS.add(employee)
                    : nhanVienBUS.update(employee);
            if (!saved) {
                throw new IllegalStateException("Không có dữ liệu nào được lưu.");
            }
            success = true;
            dispose();
        } catch (IllegalArgumentException | IllegalStateException ex) {
            JOptionPane.showMessageDialog(this, ex.getMessage(),
                    "Không thể lưu nhân viên", JOptionPane.ERROR_MESSAGE);
        }
    }

    private String dateText(Date date) {
        return date == null ? "" : date.toString();
    }

    private String blankToNull(String value) {
        String trimmed = value.trim();
        return trimmed.isEmpty() ? null : trimmed;
    }

    private String emptyIfNull(String value) {
        return value == null ? "" : value;
    }

    public boolean isSuccess() {
        return success;
    }
}
