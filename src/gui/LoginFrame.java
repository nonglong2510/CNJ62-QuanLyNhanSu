package gui;

import bus.TaiKhoanBUS;
import model.TaiKhoan;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;

public class LoginFrame extends JFrame {
    private JTextField txtUsername;
    private JPasswordField txtPassword;
    private JButton btnLogin;
    private TaiKhoanBUS taiKhoanBUS;

    public LoginFrame() {
        taiKhoanBUS = new TaiKhoanBUS();

        setTitle("VHRM PRO - Đăng nhập Hệ thống");
        setSize(450, 550);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        setResizable(false);
        
        JPanel mainPanel = new JPanel();
        mainPanel.setLayout(new BoxLayout(mainPanel, BoxLayout.Y_AXIS));
        mainPanel.setBackground(Color.WHITE);
        mainPanel.setBorder(new EmptyBorder(40, 50, 40, 50));

        // ==========================================
        // HEADER: LOGO & TITLE
        // ==========================================
        JLabel logoIcon = new JLabel("H"); 
        logoIcon.setOpaque(true);
        logoIcon.setBackground(new Color(41, 105, 255));
        logoIcon.setForeground(Color.WHITE);
        logoIcon.setFont(new Font("Segoe UI", Font.BOLD, 22));
        logoIcon.setPreferredSize(new Dimension(45, 45));
        logoIcon.setMaximumSize(new Dimension(45, 45));
        logoIcon.setHorizontalAlignment(SwingConstants.CENTER);
        logoIcon.setAlignmentX(Component.CENTER_ALIGNMENT);
        
        JLabel lblLoginTitle = new JLabel("Đăng nhập Hệ thống");
        lblLoginTitle.setFont(new Font("Segoe UI", Font.BOLD, 26));
        lblLoginTitle.setForeground(new Color(15, 23, 42));
        lblLoginTitle.setAlignmentX(Component.CENTER_ALIGNMENT);

        JLabel lblLoginDesc = new JLabel("Nhập thông tin tài khoản được cấp để tiếp tục");
        lblLoginDesc.setFont(new Font("Segoe UI", Font.PLAIN, 13));
        lblLoginDesc.setForeground(new Color(100, 116, 139));
        lblLoginDesc.setAlignmentX(Component.CENTER_ALIGNMENT);

        // ==========================================
        // FORM: INPUT FIELDS (Căn giữa chuẩn xác bằng GridBagLayout)
        // ==========================================
        JPanel formPanel = new JPanel(new GridBagLayout());
        formPanel.setOpaque(false);
        formPanel.setAlignmentX(Component.CENTER_ALIGNMENT);
        
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.insets = new Insets(0, 0, 5, 0); // Margin bottom

        JLabel lblUser = new JLabel("TÊN ĐĂNG NHẬP / EMAIL");
        lblUser.setFont(new Font("Segoe UI", Font.BOLD, 11));
        lblUser.setForeground(new Color(71, 85, 105));
        
        txtUsername = new JTextField();
        txtUsername.setPreferredSize(new Dimension(350, 40));
        txtUsername.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        
        JLabel lblPass = new JLabel("MẬT KHẨU XÁC THỰC");
        lblPass.setFont(new Font("Segoe UI", Font.BOLD, 11));
        lblPass.setForeground(new Color(71, 85, 105));
        
        txtPassword = new JPasswordField();
        txtPassword.setPreferredSize(new Dimension(350, 40));
        txtPassword.setFont(new Font("Consolas", Font.PLAIN, 14));

        btnLogin = new JButton("Đăng nhập Hệ thống"); // Đã xóa mũi tên bị lỗi font
        btnLogin.setBackground(new Color(41, 105, 255));
        btnLogin.setForeground(Color.WHITE);
        btnLogin.setFont(new Font("Segoe UI", Font.BOLD, 14));
        btnLogin.setPreferredSize(new Dimension(350, 45));
        btnLogin.setFocusPainted(false);
        btnLogin.setCursor(new Cursor(Cursor.HAND_CURSOR));

        formPanel.add(lblUser, gbc);
        
        gbc.gridy++;
        gbc.insets = new Insets(0, 0, 15, 0);
        formPanel.add(txtUsername, gbc);
        
        gbc.gridy++;
        gbc.insets = new Insets(0, 0, 5, 0);
        formPanel.add(lblPass, gbc);
        
        gbc.gridy++;
        gbc.insets = new Insets(0, 0, 30, 0);
        formPanel.add(txtPassword, gbc);
        
        gbc.gridy++;
        gbc.insets = new Insets(0, 0, 0, 0);
        formPanel.add(btnLogin, gbc);

        // ==========================================
        // ASSEMBLE
        // ==========================================
        mainPanel.add(logoIcon);
        mainPanel.add(Box.createRigidArea(new Dimension(0, 15)));
        mainPanel.add(lblLoginTitle);
        mainPanel.add(Box.createRigidArea(new Dimension(0, 5)));
        mainPanel.add(lblLoginDesc);
        mainPanel.add(Box.createRigidArea(new Dimension(0, 40)));
        mainPanel.add(formPanel);

        add(mainPanel);

        // ==========================================
        // EVENTS
        // ==========================================
        btnLogin.addActionListener(e -> performLogin());

        KeyAdapter enterAdapter = new KeyAdapter() {
            @Override
            public void keyPressed(KeyEvent e) {
                if (e.getKeyCode() == KeyEvent.VK_ENTER) {
                    performLogin();
                }
            }
        };
        txtUsername.addKeyListener(enterAdapter);
        txtPassword.addKeyListener(enterAdapter);
    }

    private void performLogin() {
        String user = txtUsername.getText();
        String pass = new String(txtPassword.getPassword());

        if (user.isEmpty() || pass.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Vui lòng nhập đầy đủ tên đăng nhập và mật khẩu!", "Cảnh báo", JOptionPane.WARNING_MESSAGE);
            return;
        }

        TaiKhoan tk = taiKhoanBUS.login(user, pass);
        if (tk != null) {
            this.dispose(); 
            MainFrame mainFrame = new MainFrame(tk);
            mainFrame.setVisible(true);
        } else {
            JOptionPane.showMessageDialog(this, "Tên đăng nhập hoặc mật khẩu không chính xác!", "Lỗi", JOptionPane.ERROR_MESSAGE);
        }
    }

    public static void main(String[] args) {
        try {
            UIManager.setLookAndFeel("com.formdev.flatlaf.FlatLightLaf");
        } catch (Exception ex) {
            System.err.println("Lưu ý: Không tìm thấy thư viện FlatLaf.");
        }
        
        SwingUtilities.invokeLater(() -> {
            new LoginFrame().setVisible(true);
        });
    }
}
