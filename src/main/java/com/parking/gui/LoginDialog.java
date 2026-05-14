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
        setLocationRelativeTo(null); 
        setResizable(false);
        setLayout(new BorderLayout());

        JPanel headerPanel = new JPanel();
        headerPanel.setBackground(Theme.ACCENT_BLUE);
        headerPanel.setPreferredSize(new Dimension(400, 60));
        JLabel lblTitle = new JLabel("ĐĂNG NHẬP HỆ THỐNG");
        lblTitle.setFont(Theme.FONT_HEADER);
        lblTitle.setForeground(Color.WHITE);
        lblTitle.setBorder(BorderFactory.createEmptyBorder(15, 0, 15, 0));
        headerPanel.add(lblTitle);
        add(headerPanel, BorderLayout.NORTH);

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

        JPanel btnPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 20, 15));
        btnPanel.setBackground(Theme.BG_SECONDARY);
        btnPanel.setBorder(BorderFactory.createMatteBorder(1, 0, 0, 0, Theme.BORDER));

        btnLogin = new JButton("Đăng Nhập");
        btnLogin.setUI(new javax.swing.plaf.basic.BasicButtonUI());
        btnLogin.setFont(Theme.FONT_TITLE);
        btnLogin.setBackground(Theme.ACCENT_BLUE);
        btnLogin.setForeground(Color.WHITE);
        btnLogin.setBorderPainted(false);
        btnLogin.setOpaque(true);
        btnLogin.setPreferredSize(new Dimension(130, 40));

        btnCancel = new JButton("Thoát");
        btnCancel.setUI(new javax.swing.plaf.basic.BasicButtonUI());
        btnCancel.setFont(Theme.FONT_TITLE);
        btnCancel.setBackground(Theme.ACCENT_RED);
        btnCancel.setForeground(Color.WHITE);
        btnCancel.setBorderPainted(false);
        btnCancel.setOpaque(true);
        btnCancel.setPreferredSize(new Dimension(130, 40));

        btnPanel.add(btnLogin);
        btnPanel.add(btnCancel);
        add(btnPanel, BorderLayout.SOUTH);

        btnLogin.addActionListener(e -> processLogin());

        btnCancel.addActionListener(e -> {
            succeeded = false;
            dispose();
        });

        KeyAdapter enterKeyAdapter = new KeyAdapter() {
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
            Session.currentUser = staff; 
            
            dispose(); 
        } else {
            JOptionPane.showMessageDialog(this,
                    "Tên đăng nhập hoặc mật khẩu không chính xác!",
                    "Đăng nhập thất bại",
                    JOptionPane.ERROR_MESSAGE);
            txtPassword.setText("");
            txtPassword.requestFocus();
        }
    }
    public boolean isSucceeded() {
        return succeeded;
    }
    public Staff getLoggedInStaff() {
        return loggedInStaff;
    }
}