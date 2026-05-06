package com.parking.gui;

import com.parking.bll.StaffBLL;
import com.parking.entity.Staff;
import com.parking.utils.Session; // THÊM IMPORT SESSION

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;

public class LoginDialog extends JDialog {
    private JTextField txtUsername;
    private JPasswordField txtPassword;
    private JButton btnLogin;
    private JButton btnCancel;
    
    private boolean succeeded = false;
    private Staff loggedInStaff = null;
    private StaffBLL bll;

    public LoginDialog() {
        super((Frame) null, "Đăng nhập - NhàXe Pro", true);
        bll = new StaffBLL();
        initComponents();
    }

    private void initComponents() {
        setSize(400, 300);
        setLocationRelativeTo(null); // Căn giữa màn hình
        setResizable(false);
        setLayout(new BorderLayout());

        // --- Panel Header ---
        JPanel headerPanel = new JPanel();
        headerPanel.setBackground(Theme.ACCENT_BLUE);
        headerPanel.setPreferredSize(new Dimension(400, 60));
        JLabel lblTitle = new JLabel("ĐĂNG NHẬP HỆ THỐNG");
        lblTitle.setFont(Theme.FONT_HEADER);
        lblTitle.setForeground(Color.WHITE);
        lblTitle.setBorder(BorderFactory.createEmptyBorder(15, 0, 15, 0));
        headerPanel.add(lblTitle);
        add(headerPanel, BorderLayout.NORTH);

        // --- Panel Form ---
        JPanel formPanel = new JPanel(new GridBagLayout());
        formPanel.setBackground(Theme.BG_PRIMARY);
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(10, 10, 10, 10);
        gbc.fill = GridBagConstraints.HORIZONTAL;

        JLabel lblUser = new JLabel("Tên đăng nhập:");
        lblUser.setFont(Theme.FONT_TITLE);
        lblUser.setForeground(Theme.TEXT_PRIMARY);
        gbc.gridx = 0; gbc.gridy = 0;
        formPanel.add(lblUser, gbc);

        txtUsername = new JTextField(15);
        txtUsername.setFont(Theme.FONT_BODY);
        txtUsername.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(Theme.BORDER),
                BorderFactory.createEmptyBorder(5, 5, 5, 5)));
        gbc.gridx = 1; gbc.gridy = 0;
        formPanel.add(txtUsername, gbc);

        JLabel lblPass = new JLabel("Mật khẩu:");
        lblPass.setFont(Theme.FONT_TITLE);
        lblPass.setForeground(Theme.TEXT_PRIMARY);
        gbc.gridx = 0; gbc.gridy = 1;
        formPanel.add(lblPass, gbc);

        txtPassword = new JPasswordField(15);
        txtPassword.setFont(Theme.FONT_BODY);
        txtPassword.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(Theme.BORDER),
                BorderFactory.createEmptyBorder(5, 5, 5, 5)));
        gbc.gridx = 1; gbc.gridy = 1;
        formPanel.add(txtPassword, gbc);

        add(formPanel, BorderLayout.CENTER);

        // --- Panel Buttons ---
        JPanel btnPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 20, 15));
        btnPanel.setBackground(Theme.BG_SECONDARY);
        btnPanel.setBorder(BorderFactory.createMatteBorder(1, 0, 0, 0, Theme.BORDER));

        btnLogin = new JButton("Đăng Nhập");
        btnLogin.setFont(Theme.FONT_TITLE);
        btnLogin.setBackground(Theme.ACCENT_BLUE); // Màu xanh từ Theme[cite: 12]
        btnLogin.setForeground(Color.WHITE);       // Chữ trắng
        btnLogin.setBorderPainted(false);          // Quan trọng cho macOS
        btnLogin.setOpaque(true);                  // Quan trọng cho macOS

        btnCancel = new JButton("Thoát");
        btnCancel.setFont(Theme.FONT_TITLE);
        btnCancel.setBackground(Theme.ACCENT_RED);  // Màu đỏ[cite: 12]
        btnCancel.setForeground(Color.WHITE);
        btnCancel.setBorderPainted(false);
        btnCancel.setOpaque(true);

        btnPanel.add(btnLogin);
        btnPanel.add(btnCancel);
        add(btnPanel, BorderLayout.SOUTH);

        // --- Events ---
        btnLogin.addActionListener(e -> processLogin());

        btnCancel.addActionListener(e -> {
            succeeded = false;
            dispose();
        });

        // Hỗ trợ ấn Enter để đăng nhập
        KeyAdapter enterKeyAdapter = new KeyAdapter() {
            @Override
            public void keyPressed(KeyEvent e) {
                if (e.getKeyCode() == KeyEvent.VK_ENTER) {
                    processLogin();
                }
            }
        };
        txtUsername.addKeyListener(enterKeyAdapter);
        txtPassword.addKeyListener(enterKeyAdapter);
    }

    private void processLogin() {
        String username = txtUsername.getText().trim();
        String password = new String(txtPassword.getPassword());

        Staff staff = bll.login(username, password);
        
        if (staff != null) {
            this.loggedInStaff = staff;
            this.succeeded = true;
            
            // LƯU TRỮ VÀO SESSION Ở ĐÂY ĐỂ PHÂN QUYỀN TRÊN TOÀN HỆ THỐNG
            Session.currentUser = staff; 
            
            dispose(); // Đóng hộp thoại thành công
        } else {
            JOptionPane.showMessageDialog(this,
                    "Tên đăng nhập hoặc mật khẩu không chính xác!",
                    "Đăng nhập thất bại",
                    JOptionPane.ERROR_MESSAGE);
            txtPassword.setText("");
            txtPassword.requestFocus();
        }
    }

    // Hàm này sẽ được Main.java gọi để kiểm tra xem có được mở MainFrame không
    public boolean isSucceeded() {
        return succeeded;
    }

    // Lấy thông tin nhân viên đang đăng nhập để hiển thị lên MainFrame
    public Staff getLoggedInStaff() {
        return loggedInStaff;
    }
}