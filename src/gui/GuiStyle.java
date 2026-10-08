package gui;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.JTableHeader;
import java.awt.*;

final class GuiStyle {
    static final Color BACKGROUND = new Color(244, 247, 251);
    static final Color NAVY = new Color(22, 33, 49);
    static final Color BLUE = new Color(41, 105, 255);
    static final Color TEXT = new Color(24, 36, 54);
    static final Color MUTED = new Color(112, 126, 145);
    static final Color GREEN = new Color(27, 160, 104);
    static final Color RED = new Color(221, 75, 87);

    private GuiStyle() {
    }

    static void styleTable(JTable table) {
        table.setFont(new Font("Segoe UI", Font.PLAIN, 13));
        table.setRowHeight(38);
        table.setShowVerticalLines(false);
        table.setShowHorizontalLines(true);
        table.setGridColor(new Color(231, 236, 243));
        table.setSelectionBackground(new Color(225, 236, 255));
        table.setSelectionForeground(TEXT);
        table.setFillsViewportHeight(true);
        table.setIntercellSpacing(new Dimension(0, 1));

        JTableHeader header = table.getTableHeader();
        header.setFont(new Font("Segoe UI", Font.BOLD, 11));
        header.setForeground(new Color(82, 98, 119));
        header.setBackground(new Color(248, 250, 253));
        header.setPreferredSize(new Dimension(header.getPreferredSize().width, 42));
        header.setReorderingAllowed(false);

        DefaultTableCellRenderer renderer = new DefaultTableCellRenderer() {
            @Override
            public Component getTableCellRendererComponent(JTable source, Object value,
                    boolean selected, boolean focused, int row, int column) {
                Component cell = super.getTableCellRendererComponent(
                        source, value, selected, focused, row, column);
                if (!selected) {
                    cell.setBackground(row % 2 == 0 ? Color.WHITE : new Color(250, 251, 253));
                    cell.setForeground(TEXT);
                }
                setBorder(new EmptyBorder(0, 10, 0, 10));
                return cell;
            }
        };
        table.setDefaultRenderer(Object.class, renderer);
    }

    static void styleButton(JButton button, Color background) {
        button.setFont(new Font("Segoe UI", Font.BOLD, 12));
        button.setBackground(background);
        button.setForeground(Color.WHITE);
        button.setFocusPainted(false);
        button.setBorderPainted(false);
        button.setOpaque(true);
        button.setBorder(new EmptyBorder(9, 16, 9, 16));
        button.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
    }

    static JPanel metricCard(String caption, JLabel value, String hint) {
        JPanel card = new JPanel(new BorderLayout(0, 5));
        card.setBackground(Color.WHITE);
        card.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(new Color(229, 234, 241)),
                new EmptyBorder(12, 14, 11, 14)));

        JLabel title = new JLabel(caption.toUpperCase());
        title.setFont(new Font("Segoe UI", Font.BOLD, 10));
        title.setForeground(MUTED);
        value.setFont(new Font("Segoe UI", Font.BOLD, 23));
        value.setForeground(TEXT);
        JLabel detail = new JLabel(hint);
        detail.setFont(new Font("Segoe UI", Font.PLAIN, 11));
        detail.setForeground(MUTED);

        JPanel content = new JPanel();
        content.setOpaque(false);
        content.setLayout(new BoxLayout(content, BoxLayout.Y_AXIS));
        content.add(title);
        content.add(Box.createVerticalStrut(3));
        content.add(value);
        content.add(Box.createVerticalStrut(2));
        content.add(detail);
        card.add(content, BorderLayout.CENTER);
        return card;
    }
}
