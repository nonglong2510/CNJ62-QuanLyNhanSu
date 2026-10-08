package gui;

import bus.ChucVuBUS;
import model.ChucVu;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableRowSorter;
import java.awt.*;

public class ChucVuPanel extends JPanel {
    private final ChucVuBUS bus = new ChucVuBUS();
    private final JLabel count = new JLabel("0");
    private final DefaultTableModel model = new DefaultTableModel(
            new String[]{"MÃ CHỨC VỤ", "TÊN CHỨC VỤ", "PHỤ CẤP (VND)"}, 0) {
        @Override
        public boolean isCellEditable(int row, int column) {
            return false;
        }
    };
    private final JTable table = new JTable(model);

    public ChucVuPanel() {
        setLayout(new BorderLayout(0, 14));
        setBackground(GuiStyle.BACKGROUND);
        setBorder(BorderFactory.createEmptyBorder(22, 22, 22, 22));

        JTextField search = new JTextField(24);
        search.putClientProperty("JTextField.placeholderText", "Tìm mã hoặc tên chức vụ");
        TableRowSorter<DefaultTableModel> sorter = new TableRowSorter<>(model);
        table.setRowSorter(sorter);
        search.getDocument().addDocumentListener(new javax.swing.event.DocumentListener() {
            private void filter() {
                String text = search.getText().trim();
                sorter.setRowFilter(text.isEmpty() ? null : RowFilter.regexFilter("(?i)" + java.util.regex.Pattern.quote(text)));
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
        JButton delete = new JButton("Xóa");
        GuiStyle.styleButton(add, GuiStyle.GREEN);
        GuiStyle.styleButton(edit, GuiStyle.BLUE);
        GuiStyle.styleButton(delete, GuiStyle.RED);
        JPanel toolbar = new JPanel(new FlowLayout(FlowLayout.RIGHT, 8, 0));
        toolbar.setOpaque(false);
        search.setPreferredSize(new Dimension(280, 36));
        toolbar.add(search);
        toolbar.add(add);
        toolbar.add(edit);
        toolbar.add(delete);

        JLabel title = new JLabel("Quản lý chức vụ & phụ cấp");
        title.setFont(new Font("Segoe UI", Font.BOLD, 23));
        title.setForeground(GuiStyle.TEXT);
        JLabel subtitle = new JLabel("Thiết lập chức danh và mức phụ cấp theo vị trí");
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
        heading.add(GuiStyle.metricCard("Chức vụ", count, "Danh mục vị trí"), BorderLayout.CENTER);
        heading.add(toolbar, BorderLayout.SOUTH);
        add(heading, BorderLayout.NORTH);

        table.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        GuiStyle.styleTable(table);
        JScrollPane scrollPane = new JScrollPane(table);
        scrollPane.setBorder(BorderFactory.createLineBorder(new Color(229, 234, 241)));
        scrollPane.getViewport().setBackground(Color.WHITE);
        add(scrollPane, BorderLayout.CENTER);
        add.addActionListener(e -> editPosition(null));
        edit.addActionListener(e -> {
            ChucVu selected = selectedPosition();
            if (selected != null) editPosition(selected);
        });
        delete.addActionListener(e -> deletePosition());
        loadData();
    }

    private void editPosition(ChucVu original) {
        try {
            ChucVu edited = MasterDataDialog.editPosition(this, original);
            if (edited != null && bus.save(edited, original == null)) {
                loadData();
            }
        } catch (IllegalArgumentException | IllegalStateException ex) {
            showError(ex);
        }
    }

    private ChucVu selectedPosition() {
        int selected = table.getSelectedRow();
        if (selected < 0) {
            JOptionPane.showMessageDialog(this, "Vui lòng chọn chức vụ.");
            return null;
        }
        int row = table.convertRowIndexToModel(selected);
        return new ChucVu((String) model.getValueAt(row, 0),
                (String) model.getValueAt(row, 1),
                ((Number) model.getValueAt(row, 2)).doubleValue());
    }

    private void deletePosition() {
        ChucVu selected = selectedPosition();
        if (selected == null) return;
        int answer = JOptionPane.showConfirmDialog(this,
                "Xóa chức vụ " + selected.getTenCV() + "?",
                "Xác nhận xóa", JOptionPane.YES_NO_OPTION);
        if (answer == JOptionPane.YES_OPTION) {
            try {
                bus.delete(selected.getMaCV());
                loadData();
            } catch (IllegalStateException ex) {
                showError(ex);
            }
        }
    }

    private void loadData() {
        try {
            model.setRowCount(0);
            java.util.List<ChucVu> positions = bus.getAll();
            for (ChucVu position : positions) {
                model.addRow(new Object[]{position.getMaCV(), position.getTenCV(), position.getPhuCapChucVu()});
            }
            count.setText(Integer.toString(positions.size()));
        } catch (IllegalStateException ex) {
            showError(ex);
        }
    }

    private void showError(RuntimeException ex) {
        JOptionPane.showMessageDialog(this, ex.getMessage(), "Lỗi", JOptionPane.ERROR_MESSAGE);
    }
}
