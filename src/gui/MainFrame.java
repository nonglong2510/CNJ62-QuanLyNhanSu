package gui;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.util.ArrayList;
import java.util.List;

public class MainFrame extends JFrame {
    private JPanel contentPanel;
    private CardLayout cardLayout;
    private List<JButton> allMenuButtons = new ArrayList<>();

    public MainFrame() {
        setTitle("Hệ thống Quản lý nhân sự");
        setSize(1200, 800);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);

        // ================= Sidebar (Cột bên trái) =================
        JPanel sidebar = new JPanel();
        sidebar.setLayout(new BoxLayout(sidebar, BoxLayout.Y_AXIS));
        sidebar.setBackground(new Color(22, 33, 49));
        sidebar.setPreferredSize(new Dimension(280, 0));

        // Logo
        JPanel logoPanel = new JPanel(new FlowLayout(FlowLayout.LEFT, 15, 20));
        logoPanel.setOpaque(false);
        logoPanel.setMaximumSize(new Dimension(280, 80));
        JLabel logoIcon = new JLabel("H"); 
        logoIcon.setOpaque(true);
        logoIcon.setBackground(new Color(41, 105, 255));
        logoIcon.setForeground(Color.WHITE);
        logoIcon.setFont(new Font("Segoe UI", Font.BOLD, 18));
        logoIcon.setPreferredSize(new Dimension(30, 30));
        logoIcon.setHorizontalAlignment(SwingConstants.CENTER);
        
        JLabel logoText = new JLabel("<html><div style='color:white; font-size:12px; font-weight:bold;'>HRMS ENTERPRISE</div><div style='color:#A0AEC0; font-size:9px;'>NHÂN SỰ • CHẤM CÔNG • TIỀN LƯƠNG</div></html>");
        logoPanel.add(logoIcon);
        logoPanel.add(logoText);
        sidebar.add(logoPanel);
        sidebar.add(Box.createRigidArea(new Dimension(0, 10)));

        // Vùng chứa các menu item
        JPanel menuContainer = new JPanel();
        menuContainer.setLayout(new BoxLayout(menuContainer, BoxLayout.Y_AXIS));
        menuContainer.setOpaque(false);

        String[][] heThong = {
            {"Đổi mật khẩu", "ChangePassword"},
            {"Quản lý tài khoản", "Account"}
        };
        String[][] nhanSu = {
            {"Quản lý Phòng ban", "PhongBan"},
            {"Quản lý Chức vụ", "ChucVu"},
            {"Quản lý Nhân viên", "Employees"} 
        };
        String[][] chamCong = {
            {"Cập nhật điểm danh ngày", "DiemDanh"},
            {"Quản lý Đơn xin phép", "XinPhep"},
            {"Tổng hợp công trong tháng", "TongHopCong"}
        };
        String[][] tienLuong = {
            {"Quản lý Lương & Phụ cấp", "LuongCoBan"},
            {"Quản lý Khấu trừ", "KhauTru"},
            {"Tính lương hàng tháng", "TinhLuong"},
            {"Xuất bảng lương / In phiếu", "XuatLuong"}
        };

        addAccordionMenu(menuContainer, "1. QUẢN LÝ HỆ THỐNG", heThong, false);
        addAccordionMenu(menuContainer, "2. QUẢN LÝ NHÂN SỰ", nhanSu, true);  
        addAccordionMenu(menuContainer, "3. QUẢN LÝ CHẤM CÔNG", chamCong, false);
        addAccordionMenu(menuContainer, "4. QUẢN LÝ TIỀN LƯƠNG", tienLuong, false);

        JScrollPane sidebarScroll = new JScrollPane(menuContainer);
        sidebarScroll.setBorder(null);
        sidebarScroll.setOpaque(false);
        sidebarScroll.getViewport().setOpaque(false);
        sidebarScroll.getVerticalScrollBar().setUnitIncrement(16);
        sidebar.add(sidebarScroll);

        // User Profile ở dưới cùng
        JPanel profilePanel = new JPanel(new BorderLayout());
        profilePanel.setBackground(new Color(30, 41, 59));
        profilePanel.setMaximumSize(new Dimension(280, 70));
        profilePanel.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createMatteBorder(1, 0, 0, 0, new Color(50, 60, 75)),
            BorderFactory.createEmptyBorder(15, 15, 15, 15)
        ));
        
        JPanel userInfoPanel = new JPanel(new FlowLayout(FlowLayout.LEFT, 10, 0));
        userInfoPanel.setOpaque(false);
        JLabel avatar = new JLabel("A");
        avatar.setOpaque(true);
        avatar.setBackground(Color.GRAY);
        avatar.setForeground(Color.WHITE);
        avatar.setPreferredSize(new Dimension(35, 35));
        avatar.setHorizontalAlignment(SwingConstants.CENTER);
        
        JLabel userInfo = new JLabel("<html><div style='color:white; font-size:11px; font-weight:bold;'>Admin User</div><div style='color:#A0AEC0; font-size:9px;'>Quản trị viên Hệ thống</div></html>");
        userInfoPanel.add(avatar);
        userInfoPanel.add(userInfo);

        JButton btnLogout = new JButton("➔"); 
        btnLogout.setForeground(new Color(160, 174, 192));
        btnLogout.setBackground(new Color(30, 41, 59));
        btnLogout.setBorderPainted(false);
        btnLogout.setContentAreaFilled(false);
        btnLogout.setCursor(new Cursor(Cursor.HAND_CURSOR));
        
        profilePanel.add(userInfoPanel, BorderLayout.WEST);
        profilePanel.add(btnLogout, BorderLayout.EAST);
        
        sidebar.add(Box.createVerticalGlue());
        sidebar.add(profilePanel);

        // ================= Content Area (Vùng nội dung bên phải) =================
        cardLayout = new CardLayout();
        contentPanel = new JPanel(cardLayout);
        contentPanel.setBackground(new Color(245, 245, 245));

        // Đăng ký các Panel màn hình
        contentPanel.add(new NhanVienPanel(), "Employees");
        contentPanel.add(new PhongBanPanel(), "PhongBan");
        contentPanel.add(new ChucVuPanel(), "ChucVu");
        contentPanel.add(new DiemDanhPanel(), "DiemDanh");
        contentPanel.add(new DonXinPhepPanel(), "XinPhep");
        contentPanel.add(new TongHopCongPanel(), "TongHopCong");
        contentPanel.add(createPlaceholderPanel("Chức năng đang được phát triển..."), "Placeholder");

        cardLayout.show(contentPanel, "Employees");

        // ================= Lắp ráp Layout =================
        setLayout(new BorderLayout());
        add(sidebar, BorderLayout.WEST);
        add(contentPanel, BorderLayout.CENTER);
    }

    private void addAccordionMenu(JPanel container, String title, String[][] items, boolean isExpanded) {
        JPanel wrapper = new JPanel();
        wrapper.setLayout(new BoxLayout(wrapper, BoxLayout.Y_AXIS));
        wrapper.setOpaque(false);

        JButton headerBtn = new JButton((isExpanded ? "▼  " : "▶  ") + title);
        headerBtn.setForeground(new Color(200, 210, 225));
        headerBtn.setBackground(new Color(22, 33, 49));
        headerBtn.setFont(new Font("Segoe UI", Font.BOLD, 12));
        headerBtn.setFocusPainted(false);
        headerBtn.setBorderPainted(false);
        headerBtn.setContentAreaFilled(false);
        headerBtn.setOpaque(true);
        headerBtn.setAlignmentX(Component.LEFT_ALIGNMENT);
        headerBtn.setMaximumSize(new Dimension(280, 45));
        headerBtn.setHorizontalAlignment(SwingConstants.LEFT);
        headerBtn.setMargin(new Insets(0, 15, 0, 0));
        headerBtn.setCursor(new Cursor(Cursor.HAND_CURSOR));

        JPanel subContainer = new JPanel();
        subContainer.setLayout(new BoxLayout(subContainer, BoxLayout.Y_AXIS));
        subContainer.setOpaque(false);
        subContainer.setVisible(isExpanded);

        for (String[] item : items) {
            addMenuItem(subContainer, item[0], item[1]);
        }

        headerBtn.addActionListener(e -> {
            boolean visible = subContainer.isVisible();
            subContainer.setVisible(!visible);
            headerBtn.setText((!visible ? "▼  " : "▶  ") + title);
            container.revalidate();
            container.repaint();
        });

        wrapper.add(headerBtn);
        wrapper.add(subContainer);
        container.add(wrapper);
    }

    private void addMenuItem(JPanel container, String text, String cardName) {
        JButton btn = new JButton(text);
        btn.setForeground(new Color(160, 174, 192));
        btn.setBackground(new Color(22, 33, 49));
        btn.setFont(new Font("Segoe UI", Font.PLAIN, 13));
        btn.setFocusPainted(false);
        btn.setBorderPainted(false);
        // Quan trọng: Phải setContentAreaFilled để tô nền màu xanh không bị lỗi FlatLaf
        btn.setContentAreaFilled(cardName.equals("Employees"));
        btn.setOpaque(true);
        btn.setAlignmentX(Component.LEFT_ALIGNMENT);
        btn.setMaximumSize(new Dimension(280, 35));
        btn.setHorizontalAlignment(SwingConstants.LEFT);
        btn.setMargin(new Insets(0, 45, 0, 0)); 
        btn.setCursor(new Cursor(Cursor.HAND_CURSOR));
        
        if (cardName.equals("Employees")) {
            btn.setBackground(new Color(41, 105, 255)); 
            btn.setForeground(Color.WHITE);
        }

        allMenuButtons.add(btn);

        btn.addActionListener(e -> {
            for (JButton b : allMenuButtons) {
                b.setContentAreaFilled(false);
                b.setBackground(new Color(22, 33, 49));
                b.setForeground(new Color(160, 174, 192));
            }
            btn.setContentAreaFilled(true);
            btn.setBackground(new Color(41, 105, 255));
            btn.setForeground(Color.WHITE);

            if (cardName.equals("Employees") || cardName.equals("PhongBan") || cardName.equals("ChucVu") ||
                cardName.equals("DiemDanh") || cardName.equals("XinPhep") || cardName.equals("TongHopCong")) {
                cardLayout.show(contentPanel, cardName);
            } else {
                cardLayout.show(contentPanel, "Placeholder");
            }
        });

        container.add(btn);
    }

    private JPanel createPlaceholderPanel(String text) {
        JPanel p = new JPanel(new GridBagLayout());
        p.setBackground(new Color(245, 245, 245));
        JLabel l = new JLabel(text);
        l.setFont(new Font("Segoe UI", Font.ITALIC, 20));
        l.setForeground(Color.GRAY);
        p.add(l);
        return p;
    }

    public static void main(String[] args) {
        try {
            UIManager.setLookAndFeel("com.formdev.flatlaf.FlatLightLaf");
        } catch (Exception ex) {
            System.err.println("Lưu ý: Không tìm thấy thư viện FlatLaf.");
        }
        
        SwingUtilities.invokeLater(() -> {
            new MainFrame().setVisible(true);
        });
    }
}
