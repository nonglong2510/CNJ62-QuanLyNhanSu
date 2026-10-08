package gui;

import bus.BangLuongBUS;

import javax.swing.*;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableRowSorter;
import java.awt.*;
import java.text.NumberFormat;
import java.util.List;
import java.util.Locale;

public class LuongCoBanPanel extends JPanel {
    private final BangLuongBUS bus = new BangLuongBUS();
    private final JTextField search = new JTextField(24);
    private final JLabel summary = new JLabel();
    private final DefaultTableModel model = new DefaultTableModel(new String[]{
            "MÃ NV", "HỌ TÊN", "PHÒNG BAN - CHỨC VỤ", "HỆ SỐ LƯƠNG",
            "LƯƠNG ĐỦ 22 NGÀY", "PHỤ CẤP CHỨC VỤ", "TỔNG DỰ KIẾN"
    }, 0) {
        @Override
        public boolean isCellEditable(int row, int column) {
            return false;
        }
    };
    private final JTable table = new JTable(model);
    private final TableRowSorter<DefaultTableModel> sorter = new TableRowSorter<>(model);

    public LuongCoBanPanel() {
        setLayout(new BorderLayout(0, 14));
        setBackground(GuiStyle.BACKGROUND);
        setBorder(BorderFactory.createEmptyBorder(22, 22, 22, 22));
        JPanel header = new JPanel(new BorderLayout(8, 12));
        header.setOpaque(false);
        JPanel titleBlock = new JPanel();
        titleBlock.setOpaque(false);
        titleBlock.setLayout(new BoxLayout(titleBlock, BoxLayout.Y_AXIS));
        JLabel title = new JLabel("Mức lương & phụ cấp");
        title.setFont(new Font("Segoe UI", Font.BOLD, 23));
        title.setForeground(GuiStyle.TEXT);
        JLabel subtitle = new JLabel("Tổng quan mức lương theo hệ số và chức vụ của nhân sự đang làm việc");
        subtitle.setFont(new Font("Segoe UI", Font.PLAIN, 12));
        subtitle.setForeground(GuiStyle.MUTED);
        titleBlock.add(title);
        titleBlock.add(Box.createVerticalStrut(3));
        titleBlock.add(subtitle);
        header.add(titleBlock, BorderLayout.NORTH);
        JPanel controls = new JPanel(new FlowLayout(FlowLayout.LEFT, 8, 0));
        controls.setOpaque(false);
        controls.add(new JLabel("Tìm nhân viên:"));
        search.setPreferredSize(new Dimension(260, 36));
        controls.add(search);
        JButton reload = new JButton("Tải lại");
        GuiStyle.styleButton(reload, GuiStyle.BLUE);
        controls.add(reload);
        summary.setBorder(BorderFactory.createEmptyBorder(4, 0, 4, 0));
        summary.setForeground(GuiStyle.MUTED);
        controls.add(summary);
        header.add(controls, BorderLayout.SOUTH);
        add(header, BorderLayout.NORTH);

        GuiStyle.styleTable(table);
        table.setRowSorter(sorter);
        JScrollPane scrollPane = new JScrollPane(table);
        scrollPane.setBorder(BorderFactory.createLineBorder(new Color(229, 234, 241)));
        scrollPane.getViewport().setBackground(Color.WHITE);
        add(scrollPane, BorderLayout.CENTER);
        reload.addActionListener(e -> loadData());
        search.getDocument().addDocumentListener(new DocumentListener() {
            @Override public void insertUpdate(DocumentEvent e) { filter(); }
            @Override public void removeUpdate(DocumentEvent e) { filter(); }
            @Override public void changedUpdate(DocumentEvent e) { filter(); }
        });
        loadData();
    }

    private void loadData() {
        try {
            List<Object[]> rows = bus.getDanhSachLuongCoBan();
            model.setRowCount(0);
            double total = 0;
            for (Object[] row : rows) {
                double base = ((Number) row[4]).doubleValue();
                double allowance = ((Number) row[5]).doubleValue();
                total += ((Number) row[6]).doubleValue();
                model.addRow(new Object[]{row[0], row[1], row[2], row[3], money(base),
                        money(allowance), money(base + allowance)});
            }
            summary.setText("Nhân viên đang làm việc: " + rows.size()
                    + "  |  Tổng dự kiến đủ 22 công: " + money(total));
        } catch (RuntimeException ex) {
            model.setRowCount(0);
            summary.setText("Không tải được dữ liệu lương");
            JOptionPane.showMessageDialog(this, ex.getMessage(), "Lỗi", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void filter() {
        String query = search.getText().trim();
        sorter.setRowFilter(query.isEmpty() ? null : RowFilter.regexFilter("(?i)" + java.util.regex.Pattern.quote(query)));
    }

    private String money(double amount) {
        return NumberFormat.getNumberInstance(Locale.forLanguageTag("vi-VN")).format(Math.round(amount)) + " đ";
    }
}
