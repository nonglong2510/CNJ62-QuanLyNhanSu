package gui;

import bus.BangLuongBUS;
import model.BangLuong;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.text.NumberFormat;
import java.time.LocalDate;
import java.util.List;
import java.util.Locale;

public class KhauTruPanel extends JPanel {
    private final BangLuongBUS bus = new BangLuongBUS();
    private final JSpinner month = new JSpinner(new SpinnerNumberModel(LocalDate.now().getMonthValue(), 1, 12, 1));
    private final JSpinner year = new JSpinner(new SpinnerNumberModel(LocalDate.now().getYear(), 2000, 2100, 1));
    private final JLabel notice = new JLabel(
            "Bản demo chưa tính thuế TNCN, BHXH, BHYT, BHTN hoặc tạm ứng; không tự giả lập khoản khấu trừ.");
    private final DefaultTableModel model = new DefaultTableModel(
            new String[]{"MÃ NV", "NHÂN VIÊN", "TỔNG THU NHẬP", "KHẤU TRỪ ĐÃ GHI", "THỰC LĨNH"}, 0) {
        @Override
        public boolean isCellEditable(int row, int column) {
            return false;
        }
    };

    public KhauTruPanel() {
        setLayout(new BorderLayout(0, 14));
        setBackground(GuiStyle.BACKGROUND);
        setBorder(BorderFactory.createEmptyBorder(22, 22, 22, 22));
        JPanel header = new JPanel(new BorderLayout(8, 12));
        header.setOpaque(false);
        JPanel titleBlock = new JPanel();
        titleBlock.setOpaque(false);
        titleBlock.setLayout(new BoxLayout(titleBlock, BoxLayout.Y_AXIS));
        JLabel title = new JLabel("Tổng hợp khấu trừ");
        title.setFont(new Font("Segoe UI", Font.BOLD, 23));
        title.setForeground(GuiStyle.TEXT);
        JLabel subtitle = new JLabel("Đối chiếu thu nhập, khoản khấu trừ đã ghi và thực lĩnh theo kỳ lương");
        subtitle.setFont(new Font("Segoe UI", Font.PLAIN, 12));
        subtitle.setForeground(GuiStyle.MUTED);
        titleBlock.add(title);
        titleBlock.add(Box.createVerticalStrut(3));
        titleBlock.add(subtitle);
        header.add(titleBlock, BorderLayout.NORTH);
        JPanel controls = new JPanel(new FlowLayout(FlowLayout.LEFT, 8, 0));
        controls.setOpaque(false);
        controls.add(new JLabel("Tháng:"));
        controls.add(month);
        controls.add(new JLabel("Năm:"));
        controls.add(year);
        JButton load = new JButton("Tải kỳ lương");
        GuiStyle.styleButton(load, GuiStyle.BLUE);
        controls.add(load);
        header.add(controls, BorderLayout.CENTER);
        notice.setForeground(new Color(145, 91, 20));
        notice.setOpaque(true);
        notice.setBackground(new Color(255, 248, 230));
        notice.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(new Color(247, 224, 172)),
                BorderFactory.createEmptyBorder(9, 12, 9, 12)));
        header.add(notice, BorderLayout.SOUTH);
        add(header, BorderLayout.NORTH);

        JTable table = new JTable(model);
        GuiStyle.styleTable(table);
        JScrollPane scrollPane = new JScrollPane(table);
        scrollPane.setBorder(BorderFactory.createLineBorder(new Color(229, 234, 241)));
        scrollPane.getViewport().setBackground(Color.WHITE);
        add(scrollPane, BorderLayout.CENTER);
        load.addActionListener(e -> loadData());
        loadData();
    }

    private void loadData() {
        try {
            List<BangLuong> salaries = bus.getByMonth((Integer) month.getValue(), (Integer) year.getValue());
            model.setRowCount(0);
            for (BangLuong salary : salaries) {
                double gross = salary.getLuongCoBan() + salary.getTongPhuCap() + salary.getTienThuong();
                double recordedDeductions = salary.getTienPhat();
                model.addRow(new Object[]{salary.getMaNV(), salary.getHoTen(), money(gross),
                        money(recordedDeductions), money(salary.getThucLanh())});
            }
            if (salaries.isEmpty()) {
                notice.setText("Chưa có dữ liệu lương kỳ " + month.getValue() + "/" + year.getValue()
                        + ". " + "Bản demo chưa tính thuế TNCN, bảo hiểm hoặc tạm ứng.");
            } else {
                notice.setText("Kỳ " + month.getValue() + "/" + year.getValue()
                        + " — các khoản thuế/bảo hiểm chưa được tính, số khấu trừ chỉ gồm khoản ghi trong bảng lương.");
            }
        } catch (RuntimeException ex) {
            model.setRowCount(0);
            JOptionPane.showMessageDialog(this, ex.getMessage(), "Lỗi", JOptionPane.ERROR_MESSAGE);
        }
    }

    private String money(double amount) {
        return NumberFormat.getNumberInstance(Locale.forLanguageTag("vi-VN")).format(Math.round(amount)) + " đ";
    }
}
