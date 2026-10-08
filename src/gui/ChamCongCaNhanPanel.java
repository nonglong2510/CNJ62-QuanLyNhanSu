package gui;

import bus.ChamCongBUS;
import model.ChamCong;
import model.TaiKhoan;
import utils.AccessPolicy;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.time.LocalDate;
import java.util.List;

public class ChamCongCaNhanPanel extends JPanel {
    private final ChamCongBUS bus = new ChamCongBUS();
    private final String maNV;
    private final JSpinner month = new JSpinner(new SpinnerNumberModel(
            LocalDate.now().getMonthValue(), 1, 12, 1));
    private final JSpinner year = new JSpinner(new SpinnerNumberModel(
            LocalDate.now().getYear(), 2000, 2100, 1));
    private final JLabel summary = new JLabel("0 bản ghi");
    private final DefaultTableModel model = new DefaultTableModel(
            new String[]{"NGÀY", "TRẠNG THÁI", "GIỜ OT", "GHI CHÚ"}, 0) {
        @Override
        public boolean isCellEditable(int row, int column) {
            return false;
        }
    };
    private final JTable table = new JTable(model);

    public ChamCongCaNhanPanel(TaiKhoan currentUser) {
        if (currentUser == null || !"User".equals(currentUser.getQuyen())) {
            throw new IllegalArgumentException("Tài khoản User chưa được liên kết với hồ sơ nhân viên.");
        }
        maNV = AccessPolicy.requireEmployeeId(currentUser);
        setLayout(new BorderLayout(0, 14));
        setBackground(GuiStyle.BACKGROUND);
        setBorder(BorderFactory.createEmptyBorder(22, 22, 22, 22));

        JLabel title = new JLabel("Lịch sử chấm công của tôi");
        title.setFont(new Font("Segoe UI", Font.BOLD, 23));
        title.setForeground(GuiStyle.TEXT);
        JLabel subtitle = new JLabel("Tra cứu ngày làm việc và giờ làm thêm theo tháng");
        subtitle.setFont(new Font("Segoe UI", Font.PLAIN, 12));
        subtitle.setForeground(GuiStyle.MUTED);
        JPanel titleBlock = new JPanel();
        titleBlock.setOpaque(false);
        titleBlock.setLayout(new BoxLayout(titleBlock, BoxLayout.Y_AXIS));
        titleBlock.add(title);
        titleBlock.add(Box.createVerticalStrut(3));
        titleBlock.add(subtitle);

        JButton load = new JButton("Tải chấm công");
        GuiStyle.styleButton(load, GuiStyle.BLUE);
        JPanel controls = new JPanel(new FlowLayout(FlowLayout.LEFT, 8, 0));
        controls.setOpaque(false);
        controls.add(new JLabel("Tháng:"));
        controls.add(month);
        controls.add(new JLabel("Năm:"));
        controls.add(year);
        controls.add(load);
        controls.add(summary);

        JPanel heading = new JPanel(new BorderLayout(8, 12));
        heading.setOpaque(false);
        heading.add(titleBlock, BorderLayout.NORTH);
        heading.add(controls, BorderLayout.SOUTH);
        add(heading, BorderLayout.NORTH);

        GuiStyle.styleTable(table);
        JScrollPane scrollPane = new JScrollPane(table);
        scrollPane.setBorder(BorderFactory.createLineBorder(new Color(229, 234, 241)));
        scrollPane.getViewport().setBackground(Color.WHITE);
        add(scrollPane, BorderLayout.CENTER);
        load.addActionListener(event -> loadData());
        loadData();
    }

    private void loadData() {
        try {
            List<ChamCong> records = bus.getByEmployee(maNV,
                    (Integer) month.getValue(), (Integer) year.getValue());
            model.setRowCount(0);
            for (ChamCong record : records) {
                model.addRow(new Object[]{record.getNgayChamCong(), record.getTrangThai(),
                        record.getSoGioLamThem(), record.getGhiChu()});
            }
            summary.setText(records.size() + " bản ghi");
        } catch (IllegalArgumentException | IllegalStateException ex) {
            model.setRowCount(0);
            summary.setText("Không tải được dữ liệu");
            JOptionPane.showMessageDialog(this, ex.getMessage(), "Lỗi", JOptionPane.ERROR_MESSAGE);
        }
    }
}
