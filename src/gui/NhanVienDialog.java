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
    private JTextField txtMaNV;
    private JTextField txtHoTen;
    private JComboBox<String> cbGioiTinh;
    private JComboBox<PhongBan> cbPhongBan;
    private JComboBox<ChucVu> cbChucVu;
    private JComboBox<String> cbTrangThai;
    
    private NhanVienBUS nhanVienBUS;
    private PhongBanBUS phongBanBUS;
    private ChucVuBUS chucVuBUS;
    
    private boolean isSuccess = false;

    public NhanVienDialog(JFrame parent) {
        super(parent, "Thêm mới Nhân viên", true); // true = Modal
        setSize(450, 450);
        setLocationRelativeTo(parent);
        
        nhanVienBUS = new NhanVienBUS();
        phongBanBUS = new PhongBanBUS();
        chucVuBUS = new ChucVuBUS();

        JPanel mainPanel = new JPanel(new BorderLayout(10, 10));
        mainPanel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));
        mainPanel.setBackground(Color.WHITE);

        // Vùng chứa Form
        JPanel formPanel = new JPanel(new GridLayout(6, 2, 10, 15));
        formPanel.setBackground(Color.WHITE);
        
        formPanel.add(new JLabel("Mã Nhân viên (*):"));
        txtMaNV = new JTextField();
        formPanel.add(txtMaNV);

        formPanel.add(new JLabel("Họ và tên (*):"));
        txtHoTen = new JTextField();
        formPanel.add(txtHoTen);

        formPanel.add(new JLabel("Giới tính:"));
        cbGioiTinh = new JComboBox<>(new String[]{"Nam", "Nữ"});
        formPanel.add(cbGioiTinh);

        formPanel.add(new JLabel("Phòng ban:"));
        cbPhongBan = new JComboBox<>();
        loadPhongBanData();
        formPanel.add(cbPhongBan);

        formPanel.add(new JLabel("Chức vụ:"));
        cbChucVu = new JComboBox<>();
        loadChucVuData();
        formPanel.add(cbChucVu);

        formPanel.add(new JLabel("Trạng thái:"));
        cbTrangThai = new JComboBox<>(new String[]{"Đang làm việc", "Thử việc", "Nghỉ phép", "Nghỉ việc"});
        formPanel.add(cbTrangThai);

        mainPanel.add(formPanel, BorderLayout.CENTER);

        // Vùng chứa nút Lưu/Hủy
        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        buttonPanel.setBackground(Color.WHITE);
        
        JButton btnSave = new JButton("Lưu thông tin");
        btnSave.setBackground(new Color(40, 167, 69)); // Màu xanh lá
        btnSave.setForeground(Color.WHITE);
        btnSave.setFont(new Font("Segoe UI", Font.BOLD, 12));
        btnSave.setFocusPainted(false);
        
        JButton btnCancel = new JButton("Hủy");
        btnCancel.setFont(new Font("Segoe UI", Font.BOLD, 12));
        
        btnSave.addActionListener(e -> saveNhanVien());
        btnCancel.addActionListener(e -> dispose()); // Đóng form

        buttonPanel.add(btnCancel);
        buttonPanel.add(btnSave);

        mainPanel.add(buttonPanel, BorderLayout.SOUTH);
        add(mainPanel);
    }

    private void loadPhongBanData() {
        List<PhongBan> pbs = phongBanBUS.getAll();
        for (PhongBan pb : pbs) {
            cbPhongBan.addItem(pb);
        }
    }

    private void loadChucVuData() {
        List<ChucVu> cvs = chucVuBUS.getAll();
        for (ChucVu cv : cvs) {
            cbChucVu.addItem(cv);
        }
    }

    private void saveNhanVien() {
        String maNV = txtMaNV.getText().trim();
        String hoTen = txtHoTen.getText().trim();
        
        if (maNV.isEmpty() || hoTen.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Vui lòng nhập đầy đủ Mã và Tên nhân viên!", "Lỗi", JOptionPane.ERROR_MESSAGE);
            return;
        }

        NhanVien nv = new NhanVien();
        nv.setMaNV(maNV);
        nv.setHoTen(hoTen);
        nv.setGioiTinh(cbGioiTinh.getSelectedItem().toString());
        
        PhongBan pb = (PhongBan) cbPhongBan.getSelectedItem();
        if (pb != null) nv.setMaPB(pb.getMaPB());
        
        ChucVu cv = (ChucVu) cbChucVu.getSelectedItem();
        if (cv != null) nv.setMaCV(cv.getMaCV());
        
        nv.setTrangThai(cbTrangThai.getSelectedItem().toString());
        
        // Mặc định tạm thời
        nv.setNgaySinh(new Date(System.currentTimeMillis()));
        nv.setNgayVaoLam(new Date(System.currentTimeMillis()));
        nv.setHeSoLuong(1.0);

        boolean ok = nhanVienBUS.add(nv);
        if (ok) {
            JOptionPane.showMessageDialog(this, "Đã thêm mới nhân viên thành công!", "Thành công", JOptionPane.INFORMATION_MESSAGE);
            isSuccess = true;
            dispose();
        } else {
            JOptionPane.showMessageDialog(this, "Lỗi thêm mới! Có thể Mã nhân viên đã tồn tại.", "Lỗi", JOptionPane.ERROR_MESSAGE);
        }
    }

    public boolean isSuccess() {
        return isSuccess;
    }
}
